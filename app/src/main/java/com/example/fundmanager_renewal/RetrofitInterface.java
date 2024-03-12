package com.example.fundmanager_renewal;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("user/{user_index}")
    Call<user_model> find_user_info(@Path("user_index") String user_index);

    @GET("user/{id}/{password}")
    Call<user_model> login(@Path("id") String id, @Path("password") String password);

    @DELETE("user/{user_index}")
    Call<Void> deleteAccount(@Path("user_index") String user_index);

    @GET("gain/{user_index}")
    Call<gain_model> bringGain(@Path("user_index") String user_index);

    @GET("transaction/{user_index}")
    Call<transaction_model> bringTran(@Path("user_index") String user_index);

}
