package com.example.fundmanager_renewal.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.getGainCallback;
import com.example.fundmanager_renewal.callbacks.getTranCallback;
import com.example.fundmanager_renewal.callbacks.postTranCallback;
import com.example.fundmanager_renewal.callbacks.putPostGainCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.model.transaction_model;
import com.example.fundmanager_renewal.utils.sns.GainUtils;
import com.example.fundmanager_renewal.utils.sns.TranUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutActivity extends AppCompatActivity implements  getGainCallback, getTranCallback, putPostGainCallback, postTranCallback {
    TextView txt_total_amount;
    EditText edit_output;
    String user_index, output;
    long total_amount, principal, gain, total;
    Call<Void> call;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        txt_total_amount = (TextView) findViewById(R.id.total_money_txt);
        edit_output = (EditText) findViewById(R.id.out_edit);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        TranUtils.getTran(user_index, this);
    }
    @Override
    public void getTranSuccess(transaction_model result) {
        total_amount = result.getTotal_amount();
        txt_total_amount.setText(total_amount+"");
    }
    @Override
    public void getTranFail(String t) {
        Log.e("TRANSACTION", t);
        Toast.makeText(getApplicationContext(), "사용자의 총액를을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
    }
    public void outMoney(View target){
        output = edit_output.getText().toString();
        total = total_amount-Long.parseLong(output);

        TranUtils.postTran(user_index,
                output+"",
                "0",
                String.valueOf(total),
                total, output,
                this);
    }
    @Override
    public void postTranSuccess(long total, String input) { //입출금 내역 반영 성공 시
        Toast.makeText(getApplicationContext(),"출금에 성공했습니다!", Toast.LENGTH_SHORT).show();
        edit_output.setText("");
        txt_total_amount.setText(total+"");
        total_amount = total;

        GainUtils.getGain(user_index, this);
    }
    @Override
    public void postTranFail(String t) {
        Toast.makeText(getApplicationContext(),"출금에 실패했습니다..", Toast.LENGTH_SHORT).show();
        Log.e("TRANSACTION", t+"");
    }
    @Override
    public void getGainSuccess(gain_model result) {
        principal = result.getPrincipal();
        gain = result.getGain();
        GainUtils.PutOrPostGain("put", user_index, gain+"", principal-Long.parseLong(output)+"", this);
        updateLeast(output);
    }
    @Override
    public void getGainFail(String t) {
        Log.e("GetGain", t);
    }
    @Override
    public void putPostGainSuccess() {
        Log.d("PRINCIPAL", "원금에 반영되었습니다.");
    }
    @Override
    public void putPostGainFail(String t) {
        Log.e("PRINCIPAL", t+"");
    }

    public void updateLeast(String output){
        call = retrofit_client.getApiService().minusLeast(output);
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