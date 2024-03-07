package com.example.fundmanager_renewal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    TextView name, user_money, user_gain;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        name = findViewById(R.id.user_name_txt);
        user_money = findViewById(R.id.money_txt);
        user_gain = findViewById(R.id.gain_txt);

        bringInfo();
    }

    public void bringInfo(){ //유저의 이름, 돈, 게인을 가져와 보임
        //todo: user_index를 통해 서버에서 name, money, gain을 가져옴
        name.setText("곽성은");
        user_money.setText("1,000,000");
        user_gain.setText("20");
    }

    public void goToIn(View target){
        Intent intent = new Intent(getApplicationContext(), InActivity.class);
        startActivity(intent);
    }
    public void goToOut(View target){
        Intent intent = new Intent(getApplicationContext(), OutActivity.class);
        startActivity(intent);
    }
    public void goToInOutList(View target){
        Intent intent = new Intent(getApplicationContext(), InOutListActivity.class);
        startActivity(intent);
    }
    public void goToTradeList(View target){
        Intent intent = new Intent(getApplicationContext(), TradeListActivity.class);
        startActivity(intent);
    }
    public void logout(View target){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void goToMyInfo(View target){
        Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
        startActivity(intent);
    }
}