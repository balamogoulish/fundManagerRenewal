package com.example.fundmanager_renewal.common;

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
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.transaction_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InOutListActivity extends AppCompatActivity {
    String user_index;
    Call<List<transaction_model>> call;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inout_list);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        bringTransactionList();
    }
    public void bringTransactionList(){
        call = retrofit_client.getApiService().getTranListByIndex(user_index);
        call.enqueue(new Callback<List<transaction_model>>() {
            @Override
            public void onResponse(Call<List<transaction_model>> call, Response<List<transaction_model>> response) {
                if(response.isSuccessful()) {
                    List<transaction_model> result = response.body();
                    List<transaction_model> transactions = new ArrayList<>();
                    for (transaction_model model : result) {
                        transaction_model tran = new transaction_model();
                        tran.setTransaction_time(model.getTransaction_time());
                        tran.setDeposit(model.getDeposit());
                        tran.setWithdrawal(model.getWithdrawal());
                        tran.setTotal_amount(model.getTotal_amount());
                        transactions.add(tran);
                    }

                    populateTable(transactions);
                } else{
                    Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("NotFailBut:", response+"");
                }
            }
            @Override
            public void onFailure(Call<List<transaction_model>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("TRANAPI ERROR:", t+"");
            }
        });
    }
    public void populateTable(List<transaction_model> transactions) {
        TableLayout tableLayout = findViewById(R.id.inout_table);

        for (transaction_model transaction : transactions) {
            // Create a new row to be added to the tableLayout
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 30, 0, 30);
            row.setLayoutParams(layoutParams);

            // Create TextViews to hold transaction data
            TextView textViewTransactionTime = new TextView(this);
            textViewTransactionTime.setText(formatDate(transaction.getTransaction_time()));
            textViewTransactionTime.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textViewTransactionTime.setGravity(Gravity.CENTER);
            textViewTransactionTime.setTextSize(18);
            row.addView(textViewTransactionTime);

            TextView textViewDeposit = new TextView(this);
            textViewDeposit.setText(String.valueOf(transaction.getDeposit()));
            textViewDeposit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textViewDeposit.setTextSize(18);
            row.addView(textViewDeposit);

            TextView textViewWithdrawal = new TextView(this);
            textViewWithdrawal.setText(String.valueOf(transaction.getWithdrawal()));
            textViewWithdrawal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textViewWithdrawal.setTextSize(18);
            row.addView(textViewWithdrawal);

            TextView textViewTotalAmount = new TextView(this);
            textViewTotalAmount.setText(String.valueOf(transaction.getTotal_amount()));
            textViewTotalAmount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textViewTotalAmount.setTextSize(18);
            row.addView(textViewTotalAmount);

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
