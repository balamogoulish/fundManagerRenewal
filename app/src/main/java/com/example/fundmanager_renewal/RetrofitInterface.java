package com.example.fundmanager_renewal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("user/{user_index}")
    Call<user_model> find_user_info(@Path("user_index") String user_index);

    @GET("user/{id}/{password}")
    Call<user_model> login(@Path("id") String id, @Path("password") String password);
    @GET("user/id/{id}")
    Call<user_model> checkIdDuplicate(@Path("id") String id);

    @GET("user/email/{email}")
    Call<user_model> findUserId(@Path("email") String email);

    @DELETE("user/{user_index}")
    Call<Void> deleteAccount(@Path("user_index") String user_index);

    @FormUrlEncoded
    @POST("user")
    Call<Void> signUp(@Field("username") String username, @Field("id") String id, @Field("password") String password, @Field("email") String email, @Field("account") String account);

    @FormUrlEncoded
    @PUT("user/pw/{id}")
    Call<Void> updatePw(@Path("id") String id, @Field("password") String password);

    @GET("gain/{user_index}")
    Call<gain_model> bringGain(@Path("user_index") String user_index);

    @GET("transaction/{user_index}")
    Call<transaction_model> bringTran(@Path("user_index") String user_index);

    @GET("transaction/list/{user_index}")
    Call<List<transaction_model>> bringTranList(@Path("user_index") String user_index);

    @FormUrlEncoded
    @POST("transaction")
    Call<Void> postTran(@Field("user_index_t") String user_index, @Field("deposit") String deposit, @Field("withdrawal") String withdrawal, @Field("total_amount") String total_amount);

    @FormUrlEncoded
    @POST("gain")
    Call<Void> postGain(@Field("user_index_g") String user_index_g, @Field("gain") String gain, @Field("principal") String principal);

    @FormUrlEncoded
    @PUT("gain/{user_index_g}")
    Call<Void> putGain(@Path("user_index_g") String user_index_g, @Field("gain") String gain, @Field("principal") String principal);

    @GET("gain/list/{user_index}")
    Call<List<gain_model>> bringGainList(@Path("user_index") String user_index);

    @FormUrlEncoded
    @POST("user/email")
    Call<String> checkEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("user/newPw")
    Call<String> sendNewPw(@Field("email") String email);
}
