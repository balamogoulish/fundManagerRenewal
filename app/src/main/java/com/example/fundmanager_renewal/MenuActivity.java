package com.example.fundmanager_renewal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    String user_index, username, gain, total_amount;
    TextView name, user_money, user_gain;
    Call<gain_model> callGain;
    Call<transaction_model> callTran;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        bringGain();
        bringTotalAmount();
    }

    public void bringGain(){
        callGain = retrofit_client.getApiService().bringGain(user_index);
        callGain.enqueue(new Callback<gain_model>() {
            @Override
            public void onResponse(Call<gain_model> callGain, Response<gain_model> response) {
                if(response.isSuccessful()){
                    gain_model result = response.body();
                    if(result == null){
                        Toast.makeText(getApplicationContext(), "사용자의 gain이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        user_gain.setText(result.getGain()+"");
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

    public void bringTotalAmount(){
        callTran = retrofit_client.getApiService().bringTran(user_index);
        callTran.enqueue(new Callback<transaction_model>() {
            @Override
            public void onResponse(Call<transaction_model> callTran, Response<transaction_model> response) {
                if(response.isSuccessful()){
                    transaction_model result = response.body();
                    if(result == null){
                        Toast.makeText(getApplicationContext(), "사용자의 거래 내역이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        user_money.setText(result.getTotal_amount()+"");
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
        startActivity(intent);
    }
    public void goToMyInfo(View target){
        Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
        intent.putExtra("user_index", user_index);
        startActivity(intent);
    }
}