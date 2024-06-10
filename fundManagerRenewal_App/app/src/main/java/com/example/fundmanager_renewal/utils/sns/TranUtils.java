package com.example.fundmanager_renewal.utils.sns;

import com.example.fundmanager_renewal.callbacks.getTranCallback;
import com.example.fundmanager_renewal.callbacks.postTranCallback;
import com.example.fundmanager_renewal.model.transaction_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranUtils {
    private static final String TAG = "TranUtils";
    static Call<Void> call;
    static Call<transaction_model> callTran;
    public static void postTran(String index, String deposit, String withdrawal, String total_amount, long total, String input, postTranCallback callback){
        call = retrofit_client.getApiService().postTran(index, deposit, withdrawal, total_amount);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callback.postTranSuccess(total, input);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.postTranFail(t+"");
            }
        });
    }

    public static void getTran(String index, getTranCallback callback){
        callTran = retrofit_client.getApiService().getTranByIndex(index);
        callTran.enqueue(new Callback<transaction_model>() {
            @Override
            public void onResponse(Call<transaction_model> call, Response<transaction_model> response) {
                if(response.isSuccessful() && response.body()!=null){
                    transaction_model result = response.body();
                    callback.getTranSuccess(result);
                }
            }
            @Override
            public void onFailure(Call<transaction_model> call, Throwable t) {
                callback.getTranFail(t+"");
            }
        });
    }
}
