import requests
import json
import datetime
import time
import yaml
from time import sleep

from UseDB import insert_fund_everyday, update_fund_finally



with open('C:/Users/balam/VsCodeProject/FundManager/koreainvestment-autotrade-main/koreainvestment-autotrade-main/config.yaml', encoding='UTF-8') as f:
    _cfg = yaml.load(f, Loader=yaml.FullLoader)
APP_KEY = _cfg['APP_KEY']
APP_SECRET = _cfg['APP_SECRET']
ACCESS_TOKEN = ""
CANO = _cfg['CANO']
ACNT_PRDT_CD = _cfg['ACNT_PRDT_CD']
DISCORD_WEBHOOK_URL = _cfg['DISCORD_WEBHOOK_URL']
URL_BASE = _cfg['URL_BASE']


#디스코드 메세지 전송
def send_message(msg):
    """디스코드 메세지 전송"""
    now = datetime.datetime.now()
    message = {"content": f"[{now.strftime('%Y-%m-%d %H:%M:%S')}] {str(msg)}"}
    requests.post(DISCORD_WEBHOOK_URL, data=message)
    print(message)

#필요 토큰 발급
def get_access_token():
    """토큰 발급"""
    headers = {"content-type":"application/json"}
    body = {"grant_type":"client_credentials",
    "appkey":APP_KEY, 
    "appsecret":APP_SECRET}
    PATH = "oauth2/tokenP"
    URL = f"{URL_BASE}/{PATH}"
    res = requests.post(URL, headers=headers, data=json.dumps(body))
    ACCESS_TOKEN = res.json()["access_token"]
    return ACCESS_TOKEN
    
#암호화
def hashkey(datas):
    """암호화"""
    PATH = "uapi/hashkey"
    URL = f"{URL_BASE}/{PATH}"
    headers = {
    'content-Type' : 'application/json',
    'appKey' : APP_KEY,
    'appSecret' : APP_SECRET,
    }
    res = requests.post(URL, headers=headers, data=json.dumps(datas))
    hashkey = res.json()["HASH"]
    return hashkey

#현재 가격 조회
def get_current_price(code):
    """현재가 조회"""
    PATH = "uapi/domestic-stock/v1/quotations/inquire-price"
    URL = f"{URL_BASE}/{PATH}"
    headers = {"Content-Type":"application/json", 
            "authorization": f"Bearer {ACCESS_TOKEN}",
            "appKey":APP_KEY,
            "appSecret":APP_SECRET,
            "tr_id":"FHKST01010100"}
    params = {
    "fid_cond_mrkt_div_code":"J",
    "fid_input_iscd":code,
    }
    res = requests.get(URL, headers=headers, params=params)
    return int(res.json()['output']['stck_prpr'])

#매수 목표 가격 조회
def get_target_price(code):
    """변동성 돌파 전략으로 매수 목표가 조회"""
    PATH = "uapi/domestic-stock/v1/quotations/inquire-daily-price"
    URL = f"{URL_BASE}/{PATH}"
    headers = {"Content-Type":"application/json", 
        "authorization": f"Bearer {ACCESS_TOKEN}",
        "appKey":APP_KEY,
        "appSecret":APP_SECRET,
        "tr_id":"FHKST01010400"}
    params = {
    "fid_cond_mrkt_div_code":"J",
    "fid_input_iscd":code,
    "fid_org_adj_prc":"1",
    "fid_period_div_code":"D"
    }
    res = requests.get(URL, headers=headers, params=params)
    stck_oprc = int(res.json()['output'][0]['stck_oprc']) #오늘 시가
    stck_hgpr = int(res.json()['output'][1]['stck_hgpr']) #전일 고가
    stck_lwpr = int(res.json()['output'][1]['stck_lwpr']) #전일 저가
    target_price = stck_oprc + (stck_hgpr - stck_lwpr) * 0.5
    return target_price

# 자동매매 시작
try:
    ACCESS_TOKEN = get_access_token()
    # ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6IjdjNjNkZGViLWY3NDEtNDRkOC05Y2ZhLTU2MTgxODY0NjU5MiIsImlzcyI6InVub2d3IiwiZXhwIjoxNzEzMzEzODM2LCJpYXQiOjE3MTMyMjc0MzYsImp0aSI6IlBTUVg5dTkzUlFoWkNlcGpoYm5vRXh3dnhKZEZzRUUwVWxvayJ9.xQ1h4er9_DXI5k7NRV5YMRQifQ_IQAbHasTnz6bVn5i0wMSIG9YF-HdcaXCHv8n_jUmhj7mFrqGnyci6JDqzBQ"
    
    # 매수 희망 종목 리스트
    ## 005930: 삼성전자
    ## 035720: 카카오
    ## 000660: SK 하이닉스
    ## 069500: KODEX 200
    symbol_list = ["005930","035720","000660","069500"] 
    bought_list = [] # 매수 완료된 종목 리스트
    target_buy_count = 3 # 매수할 종목 수
    buy_percent = 0.33 # 종목 당 매수 금액 비율

    soldout = False
    total_cash = 0 # 총 현금 조회
    real_input_cash = 0 # 실투자금: 총 현금의 60%
    
    buy_qty_dict = dict.fromkeys(symbol_list)
    buy_amount = 0 # 종목 별 주문 가능 금액
    least_of_real_input_cash = 0 # 총 현금의 40%

    send_message("========국내 주식 자동매매 프로그램을 시작합니다========")
    send_message(f"TOKEN: {ACCESS_TOKEN}")
    
    while True:
        t_now = datetime.datetime.now()
        t_9 = t_now.replace(hour=9, minute=45, second=0, microsecond=0)
        t_start = t_now.replace(hour=9, minute=46, second=0, microsecond=0)
        t_sell = t_now.replace(hour=15, minute=15, second=0, microsecond=0)

        today = datetime.datetime.today().weekday()

        if today == 5 or today == 6:  # 토요일이나 일요일이면 자동 종료
            send_message("주말이므로 프로그램을 종료합니다.")
            break

        if t_9 < t_now < t_start: # AM 09:00 ~ AM 09:01 : 펀드 시작
            total_cash = insert_fund_everyday()
            real_input_cash = total_cash*0.6
            buy_amount = real_input_cash * buy_percent
            if total_cash is not None:
                send_message(f"===펀드 정보를 성공적으로 불러왔습니다===")
                soldout=True
                time.sleep(60)
            else:
                send_message(f"===펀드 정보를 불러오는데 실패했습니다===")
                break

        if t_start < t_now < t_sell :  # AM 09:01 ~ PM 03:15 : 매수
            for sym in symbol_list:
                if len(bought_list) < target_buy_count: # 매수 가능한 종목 수보다 매수한 종목 수가 적은 경우
                    if sym in bought_list: # 이미 매수한 종목이면 다음 종목으로
                        continue
                    target_price = get_target_price(sym)
                    current_price = get_current_price(sym)
                    if(target_price < current_price):
                        buy_qty_dict[sym]= int(buy_amount // current_price)
                        if buy_qty_dict[sym] > 0:
                            real_input_cash = real_input_cash - (buy_qty_dict[sym]*current_price)
                            send_message(f"===목표가 달성({target_price} < {current_price}), 매수를 시도합니다===")
                            send_message(f"종목: {sym}")
                            send_message(f"매수 수량: {buy_qty_dict[sym]}")
                            send_message(f"매수 가격: {current_price}")
                            send_message(f"잔여금: {real_input_cash}")
                            soldout = False
                            bought_list.append(sym)
                    time.sleep(1)
            time.sleep(1)
            if t_now.minute == 30 and t_now.second <= 10: 
                send_message("===정상적으로 실행 중입니다===")
                time.sleep(10)
            time.sleep(10)


        if t_sell < t_now:  # PM 03:15 ~ : 일괄 매도 및 종료
            if soldout == False:
                least_of_real_input_cash = real_input_cash
                real_input_cash = 0
                send_message(f"===일괄 매도를 시도합니다===")
                for sym, qty in buy_qty_dict.items():
                    if qty is not None:
                        sell_price = get_current_price(sym)
                        real_input_cash = real_input_cash + (sell_price*qty)
                        send_message(f"종목: {sym}")
                        send_message(f"매도 수량: {qty}")
                        send_message(f"매도 가격: {sell_price}")
                        send_message(f"실매도금: {sell_price*qty}")
                        send_message(f"real_input_cash: {real_input_cash}")
                send_message("========프로그램을 종료합니다========")
                send_message(f"총 투자금: {real_input_cash+least_of_real_input_cash}")
                send_message(f"total_cash: {total_cash}")
                update_fund_finally(real_input_cash+least_of_real_input_cash, total_cash)
            else:
                send_message("===일괄 매도할 주식이 없습니다.===")
                send_message("========프로그램을 종료합니다========")
                update_fund_finally(total_cash*0.6, total_cash)
            break

except Exception as e:
    send_message(f"[오류 발생!] {str(e)}")
    time.sleep(1)
