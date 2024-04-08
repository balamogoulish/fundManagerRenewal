package com.example.fundmanager_renewal.utils.sns;

import android.app.Activity;
import android.util.Log;

import com.example.fundmanager_renewal.callbacks.sendEmailCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailUtils {
    private static final String TAG = "EmailUtils";
    static Call<String> callStr;

    public static void sendEmailCode(String email, String EmailOrPw, sendEmailCallback callback){
        if(EmailOrPw.equals("email")){
            callStr = retrofit_client.getApiService().checkEmail(email);
        } else if(EmailOrPw.equals("pw")){
            callStr = retrofit_client.getApiService().sendNewPwToEmail(email);
        }
        callStr.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String verificationCode = response.body();
                    callback.sendEmailSuccess(verificationCode);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.sendEmailFail(t+"");
            }
        });
    }
}
