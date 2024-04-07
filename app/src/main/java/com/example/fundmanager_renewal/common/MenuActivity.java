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
import com.example.fundmanager_renewal.callbacks.bringGainPrincipalCallback;
import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.transaction_model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements TotalAmountCallback, bringGainPrincipalCallback {
    String user_index, username;
    TextView name, user_money, user_gain;
    Call<gain_model> callGain;
    Call<transaction_model> callTran;
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
        bringGain(this);
        bringTotalAmount(this);
    }

    public void bringGain(bringGainPrincipalCallback callback){
        callGain = retrofit_client.getApiService().bringGain(user_index);
        callGain.enqueue(new Callback<gain_model>() {
            @Override
            public void onResponse(Call<gain_model> callGain, Response<gain_model> response) {
                if(response.isSuccessful()){
                    gain_model gain_result = response.body();
                    if(gain_result == null){
                        Toast.makeText(getApplicationContext(), "사용자의 gain이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        callback.gainPrincipalReceived(gain_result);
                    }
                }
            }
            @Override
            public void onFailure(Call<gain_model> callGain, Throwable t) {
                Toast.makeText(getApplicationContext(), "api 응답 실패!!", Toast.LENGTH_SHORT).show();
                Log.d("<<API ERROR in Gain>>", t+"");
            }
        });

    }
    public void bringTotalAmount(TotalAmountCallback callback){
        callTran = retrofit_client.getApiService().bringTran(user_index);
        callTran.enqueue(new Callback<transaction_model>() {
            @Override
            public void onResponse(Call<transaction_model> callTran, Response<transaction_model> response) {
                if(response.isSuccessful()){
                    transaction_model tran_result = response.body();
                    if(tran_result == null){
                        Toast.makeText(getApplicationContext(), "사용자의 거래 내역이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        callback.onTotalAmountReceived(tran_result);
                    }
                }
            }
            @Override
            public void onFailure(Call<transaction_model> callTran, Throwable t) {
                Toast.makeText(getApplicationContext(), "api 응답 실패!!", Toast.LENGTH_SHORT).show();
                Log.d("<<API ERROR in Tran>>", t+"");
            }
        });
    }
    @Override
    public void onTotalAmountReceived(transaction_model tran_result) {
        user_money.setText(tran_result.getTotal_amount()+"");
    }
    @Override
    public void gainPrincipalReceived(gain_model gain_result) {
        user_gain.setText(gain_result.getGain()+"");
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