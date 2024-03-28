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

public class InActivity extends AppCompatActivity implements TotalAmountCallback, bringGainPrincipalCallback{
    TextView txt_total_amount;
    EditText edit_input;
    String user_index;
    long total_amount, principal, gain;
    Call<Void> call;
    public static Context iContext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        iContext = this;

        txt_total_amount = (TextView) findViewById(R.id.total_money_txt);
        edit_input = (EditText) findViewById(R.id.in_edit);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        ((MenuActivity)MenuActivity.mContext).bringTotalAmount(this);
    }

    public void insertMoney(View target){
        String input = edit_input.getText().toString();
        long total = total_amount+Long.parseLong(input);
        Log.d("TOTALAMOUNT", total_amount+"");
        call = retrofit_client.getApiService().postTran(user_index, input,"0", String.valueOf(total));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"입금에 성공했습니다!", Toast.LENGTH_SHORT).show();
                    edit_input.setText("");
                    txt_total_amount.setText(total+"");
                    total_amount = total;
                    ((MenuActivity)MenuActivity.mContext).bringGain(new bringGainPrincipalCallback() {
                        @Override
                        public void gainPrincipalReceived(gain_model gain_result){
                            principal = gain_result.getPrincipal();
                            gain = gain_result.getGain();
                            Log.d("PRINCIPALinInsert" ,principal+"");
                            updatePrincipal(gain, principal+Long.parseLong(input));
                            updateLeast(input);
                        }
                    });
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

    @Override
    public void onTotalAmountReceived(transaction_model tran_result) {
        total_amount = tran_result.getTotal_amount();
        txt_total_amount.setText(total_amount+"");
    }
    @Override
    public void gainPrincipalReceived(gain_model gain_result) {
        Toast.makeText(getApplicationContext(),"이상한 거 불러옴..", Toast.LENGTH_SHORT).show();
    }
    public void updatePrincipal(long gain, long change){
        call = retrofit_client.getApiService().putGain(user_index, gain+"", change+"");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("PRINCIPAL", "원금에 반영되었습니다.");
                } else{
                    Log.d("NOTFAILBUT", "response");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("PRINCIPALFAIL", t+"");
            }
        });
    }

    public void updateLeast(String input){
        call = retrofit_client.getApiService().plusLeast(input);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("LEAST", "least에 반영되었습니다.");
                } else{
                    Log.d("NOTFAILBUT", "response");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("LEASTFAIL", t+"");
            }
        });
    }


}