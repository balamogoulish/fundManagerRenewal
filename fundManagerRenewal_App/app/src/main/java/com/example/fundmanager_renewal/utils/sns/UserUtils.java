package com.example.fundmanager_renewal.utils.sns;

import android.app.Activity;

import com.example.fundmanager_renewal.callbacks.getUserCallback;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserUtils {
    private static final String TAG = "UserUtils";
    static Call<user_model> callUser;
    public static void getUser(String x, String by, getUserCallback callback){
        if(by.equals("id")){
            callUser =  retrofit_client.getApiService().getUserById(x);
        } else if(by.equals("index")){
            callUser = retrofit_client.getApiService().getUserByIndex(x);
        } else if(by.equals("email")){
            callUser = retrofit_client.getApiService().getUserByEmail(x);
        }
        callUser.enqueue(new Callback<user_model>() {
            @Override
            public void onResponse(Call<user_model> call, Response<user_model> response) {
                if (response.isSuccessful() && response.body()!=null){
                    user_model result = response.body();
                    //callback if Success
                    callback.getUserSuccess(result);
                }
            }
            @Override
            public void onFailure(Call<user_model> call, Throwable t) {
                //callback if Failure
                callback.getUserFail(t+"");
            }
        });
    }
}
