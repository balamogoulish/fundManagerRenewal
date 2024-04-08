package com.example.fundmanager_renewal.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeListActivity extends AppCompatActivity {
    String user_index;
    Call<List<gain_model>> call;
    public static Context tContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_list);
        tContext = this;

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        bringGainList();
    }
    public void bringGainList(){
        call = retrofit_client.getApiService().getGainListByIndex(user_index);
        call.enqueue(new Callback<List<gain_model>>() {
            @Override
            public void onResponse(Call<List<gain_model>> call, Response<List<gain_model>> response) {
                if(response.isSuccessful()){
                    List<gain_model> result = response.body();
                    List<gain_model> gains = new ArrayList<>();
                    for(gain_model model : result){
                        gain_model gain = new gain_model();
                        gain.setGain(model.getGain());
                        gain.setGain_time(model.getGain_time());
                        gain.setPrincipal(model.getPrincipal());
                        gains.add(gain);
                    }
                    populateTable(gains);
                }else{
                    Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("NotFailBut:", response+"");
                }
            }
            @Override
            public void onFailure(Call<List<gain_model>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("TRANAPI ERROR:", t+"");
            }
        });
    }
    public void populateTable(List<gain_model> gains) {
        TableLayout tableLayout = findViewById(R.id.trade_table);

        for (gain_model gain : gains) {
            // Create a new row to be added to the tableLayout
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 30);
            row.setLayoutParams(layoutParams);

            // Create TextViews to hold transaction data
            TextView gainTime_txt = new TextView(this);
            gainTime_txt.setTextSize(20);
            gainTime_txt.setText(formatDate(gain.getGain_time()));
            gainTime_txt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            gainTime_txt.setGravity(Gravity.CENTER);
            row.addView(gainTime_txt);

            TextView principal_txt = new TextView(this);
            principal_txt.setTextSize(20);
            principal_txt.setText(String.valueOf(gain.getPrincipal()));
            principal_txt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            principal_txt.setGravity(Gravity.RIGHT);
            row.addView(principal_txt);

            TextView gain_txt = new TextView(this);
            gain_txt.setTextSize(20);
            gain_txt.setText(String.valueOf(gain.getGain()));
            gain_txt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            gain_txt.setGravity(Gravity.RIGHT);
            row.addView(gain_txt);



            // Add the row to the tableLayout
            tableLayout.addView(row);
        }
    }
    public String formatDate(String datetimeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = inputFormat.parse(datetimeString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return empty string if parsing fails
        }
    }
}