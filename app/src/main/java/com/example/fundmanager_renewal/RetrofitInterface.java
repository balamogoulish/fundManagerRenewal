package com.example.fundmanager_renewal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("user/{user_index}")
    Call<user_model> find_user_info(@Path("user_index") String user_index);

    @GET("user/{id}/{password}")
    Call<user_model> login(@Path("id") String id, @Path("password") String password);

}
