package com.example.fundmanager_renewal.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.TotalAmountCallback;
import com.example.fundmanager_renewal.callbacks.getGainCallback;
import com.example.fundmanager_renewal.callbacks.getTranCallback;
import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.transaction_model;
import com.example.fundmanager_renewal.utils.sns.GainUtils;
import com.example.fundmanager_renewal.utils.sns.TranUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements TotalAmountCallback, getGainCallback, getTranCallback {

    String user_index, username;
    TextView name, user_money, user_gain;
    public static Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mContext = this;

        name = findViewById(R.id.user_name_txt);
        user_money = findViewById(R.id.money_txt);
        user_gain = findViewById(R.id.gain_txt);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");
        username = intent.getStringExtra("username");
        name.setText(username);
    }

    @Override
    public void onResume(){
        super.onResume();
        GainUtils.getGain(user_index, this);
        TranUtils.getTran(user_index, this);
    }

    @Override
    public void onTotalAmountReceived(transaction_model tran_result) {
        user_money.setText(tran_result.getTotal_amount()+"");
    }

    @Override
    public void getGainSuccess(gain_model result) {
        user_gain.setText(result.getGain()+"");
    }
    @Override
    public void getGainFail(String t) {
        Log.e("GAIN", t);
        Toast.makeText(getApplicationContext(), "사용자의 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void getTranSuccess(transaction_model result) {
        user_money.setText(result.getTotal_amount()+"");
    }
    @Override
    public void getTranFail(String t) {
        Log.e("TRANSACTION", t);
        Toast.makeText(getApplicationContext(), "사용자의 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
    }

    public void goToIn(View target){
        Intent intent = new Intent(getApplicationContext(), InActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
    public void goToOut(View target){
        Intent intent = new Intent(getApplicationContext(), OutActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
    public void goToInOutList(View target){
        Intent intent = new Intent(getApplicationContext(), InOutListActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
    public void goToTradeList(View target){
        Intent intent = new Intent(getApplicationContext(), TradeListActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
    public void logout(View target){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void goToMyInfo(View target){
        Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
}