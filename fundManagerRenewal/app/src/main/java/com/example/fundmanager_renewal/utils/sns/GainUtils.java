package com.example.fundmanager_renewal.utils.sns;

import com.example.fundmanager_renewal.callbacks.putPostGainCallback;
import com.example.fundmanager_renewal.callbacks.getGainCallback;
import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GainUtils {
    private static final String TAG = "GainUtils";
    static Call<Void> call;
    static Call<gain_model> callGain;

    public static void PutOrPostGain(String PutorPost, String index, String gain, String principal, putPostGainCallback callback){
        if(PutorPost.equals("put")){
            call = retrofit_client.getApiService().putGain(index,gain,principal);
        }else if(PutorPost.equals("post")){
            call = retrofit_client.getApiService().postGain(index, gain, principal);
        }
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callback.putPostGainSuccess();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.putPostGainFail(t+"");
            }
        });
    }

    public static void getGain(String index, getGainCallback callback){
        callGain = retrofit_client.getApiService().getGainByIndex(index);
        callGain.enqueue(new Callback<gain_model>() {
            @Override
            public void onResponse(Call<gain_model> callGain, Response<gain_model> response) {
                if(response.isSuccessful() && response.body()!=null){
                    gain_model result = response.body();
                    callback.getGainSuccess(result);
                }
            }
            @Override
            public void onFailure(Call<gain_model> callGain, Throwable t) {
                callback.getGainFail(t+"");
            }
        });

    }
}
