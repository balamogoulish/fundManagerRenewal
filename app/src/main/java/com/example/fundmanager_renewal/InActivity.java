package com.example.fundmanager_renewal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InActivity extends AppCompatActivity{
    TextView txt_total_amount;
    EditText edit_input;
    String user_index;
    long total_amount;
    Call<transaction_model> callTran;
    Call<Void> call;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        txt_total_amount = (TextView) findViewById(R.id.total_money_txt);
        edit_input = (EditText) findViewById(R.id.in_edit);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        bringTotalAmount();

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
                        total_amount = result.getTotal_amount();
                        txt_total_amount.setText(total_amount+"");
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

    public void insertMoney(View target){
        String input = edit_input.getText().toString();
        long total = total_amount+Long.parseLong(input);
        call = retrofit_client.getApiService().postTran(user_index, input,"0", String.valueOf(total));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"입금에 성공했습니다!", Toast.LENGTH_SHORT).show();
                    edit_input.setText("");
                    bringTotalAmount();
                }
                else{
                    Toast.makeText(getApplicationContext(),"입금에 실패했습니다..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"입금에 실패했습니다..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}