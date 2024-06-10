import yaml
import pymysql
import datetime
import requests

apiAddr = "127.0.0.1"

with open('C:/Users/balam/VsCodeProject/FundManager/koreainvestment-autotrade-main/koreainvestment-autotrade-main/config.yaml', encoding='UTF-8') as f:
    _cfg = yaml.load(f, Loader=yaml.FullLoader)
DISCORD_WEBHOOK_URL = _cfg['DISCORD_WEBHOOK_URL']

def send_message(msg):
    """디스코드 메세지 전송"""
    now = datetime.datetime.now()
    message = {"content": f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {str(msg)}"}
    requests.post(DISCORD_WEBHOOK_URL, data=message)
    print(message)

#매일 주식이 시작될 때 fund 테이블을 불러와 추가함
def insert_fund_everyday():
    connection = pymysql.connect(host='127.0.0.1', user='root', password='sal0925', db='fund',charset='utf8')
    cursor = connection.cursor()

    try:
        # Calculate fund_origin
        cursor.execute("""
            SELECT SUM(principal)
            FROM (
                SELECT principal, ROW_NUMBER() OVER (PARTITION BY user_index_g ORDER BY gain_time DESC) as rn
                FROM gain
            ) A
            WHERE rn = 1
        """)
        # fund_origin_result = cursor.fetchone()
        fund_principal_result = cursor.fetchone()

        # Calculate fund_money
        cursor.execute("""
            SELECT SUM(total_amount)
            FROM (
                SELECT total_amount, ROW_NUMBER() OVER (PARTITION BY user_index_t ORDER BY transaction_time DESC) as rn
                FROM transaction
            ) A
            WHERE rn = 1;
        """)
        # fund_money_result = cursor.fetchone()
        fund_money_result = cursor.fetchone()


        if fund_principal_result and fund_money_result:
            fund_principal = fund_principal_result[0]
            fund_money = fund_money_result[0]
            least_value = 0.4 * fund_money

            # Insert a new row into the Fund table
            cursor.execute("""
                INSERT INTO Fund (fund_money, fund_principal, least)
                VALUES (%s, %s, %s);
            """, (fund_money, fund_principal, least_value))

            # Commit the transaction
            connection.commit()

            send_message(f"총 펀드 원금: {fund_principal}")
            send_message(f"총 펀드 투자금: {fund_money}")
            send_message(f"총 펀드 실투자금: {fund_money*0.6}")
            send_message(f"총 펀드 잔여금: {least_value}")
            return fund_money
        else:
            print("Error retrieving data for calculations.")
            return None

    finally:
        try:
            cursor.close()
            connection.close()
        except Exception as e:
            print(f"An error occurred while closing cursor/connection: {e}")


#장이 끝나고 펀드 정산
def update_fund_finally(output_cash, total_cash):
    connection = pymysql.connect(host='127.0.0.1', user='root', password='sal0925', db='fund',charset='utf8')
    cursor = connection.cursor()

    try:
        # Retrieve the current value of least
        cursor.execute("SELECT least FROM Fund WHERE fund_time = (SELECT MAX(fund_time) FROM FUND);")
        least_result = cursor.fetchone()

        if least_result:
            least_value = least_result[0]

            # Calculate total_fund_gain
            fund_gain = ((output_cash + total_cash*0.4 - total_cash) / total_cash) * 100

            # Update the Fund table
            cursor.execute("""
                UPDATE Fund AS f1
                JOIN (
                    SELECT MAX(fund_time) AS max_fund_time
                    FROM Fund
                ) AS f2 ON f1.fund_time = f2.max_fund_time
                SET f1.fund_gain = %s, f1.fund_output = %s;
            """, (fund_gain, output_cash))

            #Update Invest, Check table
            update_user_money_and_insert_check_gain(total_cash, fund_gain)
            
            # Commit the transaction
            connection.commit()
            send_message("===오늘의 투자 결과===")
            send_message(f"총 펀드 게인: {fund_gain}")
            send_message(f"총 펀드 주식 output: {output_cash+total_cash*0.4}")
            send_message(f"총 펀드 보유금액: {output_cash+least_value}")
        else:
            send_message("투자 결과 업데이트에 실패했습니다. 다시 시도해 주세요.")
    except Exception as e:
        print(f"Error updating Fund table: {e}")

    finally:
        # Close the cursor and connection
        cursor.close()
        connection.close()

def update_user_money_and_insert_check_gain(total_cash, fund_gain):
    connection = pymysql.connect(host='127.0.0.1', user='root', password='sal0925', db='fund',charset='utf8')

    cursor = connection.cursor()

    try:
        # 각 user_index에 대해 가장 최근 invest_date를 가진 user_index 및 user_money 검색
        cursor.execute("""
            SELECT user_index_t, total_amount
            FROM (
                SELECT user_index_t, total_amount, ROW_NUMBER() OVER (PARTITION BY user_index_t ORDER BY transaction_time DESC) AS rn
                FROM transaction
            ) AS subquery
            WHERE rn = 1;
        """)
        
        user_data = cursor.fetchall()
        if user_data:
            for user_index_t, total_amount in user_data:
                # user_gain 계산
                user_gain = total_amount / total_cash * fund_gain

                # user_money 업데이트
                updated_user_money = total_amount * (1 + user_gain / 100)

                # invest 테이블에 삽입
                cursor.execute("""
                    INSERT INTO transaction (user_index_t, total_amount, deposit, withdrawal)
                    VALUES (%s, %s, 0, 0);
                """, (user_index_t, round(updated_user_money,3)))
                
                #완료!!

                # user_index의 가장 최근 check_date에 대한 origin_money 검색
                cursor.execute("""
                    SELECT principal
                    FROM (
                        SELECT user_index_g, principal, ROW_NUMBER() OVER (PARTITION BY user_index_g ORDER BY gain_time DESC) AS rn
                        FROM gain 
                        WHERE user_index_g = %s
                    ) AS subquery
                    WHERE rn = 1;
                """, (user_index_t))
                
                principal = cursor.fetchone()
                principal_result=principal[0]
                
                # check_gain 테이블에 삽입
                cursor.execute("""
                    INSERT INTO gain (user_index_g, principal, gain)
                    VALUES (%s, %s, %s)
                """, (user_index_t, principal_result, user_gain))
                
            # 트랜잭션 커밋
            connection.commit()
            send_message("===사용자 이익 및 자금 삽입이 완료되었습니다===")
        else:
            send_message("===사용자 이익 삽입 실패. 다시 시도해주세요===")
    except Exception as e:
        print(f"테이블 업데이트 오류: {e}")
    finally:
        # 커서와 연결 닫기
        cursor.close()
        connection.close()