package com.example.fundmanager_renewal.retrofit;

import java.util.List;

import com.example.fundmanager_renewal.model.gain_model;
import com.example.fundmanager_renewal.model.sns_model;
import com.example.fundmanager_renewal.model.transaction_model;
import com.example.fundmanager_renewal.model.user_model;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    //user model 조회, 수정, 삽입, 삭제
    @GET("user/{user_index}")
    Call<user_model> getUserByIndex(@Path("user_index") String user_index);
    @GET("user/id/{id}")
    Call<user_model> getUserById(@Path("id") String id);
    @GET("user/email/{email}")
    Call<user_model> getUserByEmail(@Path("email") String email);
    @FormUrlEncoded
    @POST("user")
    Call<user_model> postUser(@Field("username") String username,
                        @Field("id") String id,
                        @Field("password") String password,
                        @Field("email") String email,
                        @Field("account") String account);
    @FormUrlEncoded
    @PUT("user/pw/{id}")
    Call<Void> putUserPw(@Path("id") String id, @Field("password") String password);
    @DELETE("user/{user_index}")
    Call<Void> deleteUser(@Path("user_index") String user_index);


    //fund model least 삽입
    @FormUrlEncoded
    @POST("plus")
    Call<Void> plusLeast(@Field("change") String change);
    @FormUrlEncoded
    @POST("minus")
    Call<Void> minusLeast(@Field("change") String change);


    //gain_model 조회, 수정, 삽입
    @GET("gain/{user_index}")
    Call<gain_model> getGainByIndex(@Path("user_index") String user_index);
    @GET("gain/list/{user_index}")
    Call<List<gain_model>> getGainListByIndex(@Path("user_index") String user_index);
    @FormUrlEncoded
    @POST("gain")
    Call<Void> postGain(@Field("user_index_g") String user_index_g,
                        @Field("gain") String gain,
                        @Field("principal") String principal);
    @FormUrlEncoded
    @PUT("gain/{user_index_g}")
    Call<Void> putGain(@Path("user_index_g") String user_index_g,
                       @Field("gain") String gain,
                       @Field("principal") String principal);


    //transaction_model 조회, 수정, 삽입
    @GET("transaction/{user_index}")
    Call<transaction_model> getTranByIndex(@Path("user_index") String user_index);
    @GET("transaction/list/{user_index}")
    Call<List<transaction_model>> getTranListByIndex(@Path("user_index") String user_index);
    @FormUrlEncoded
    @POST("transaction")
    Call<Void> postTran(@Field("user_index_t") String user_index,
                        @Field("deposit") String deposit,
                        @Field("withdrawal") String withdrawal,
                        @Field("total_amount") String total_amount);


    //email 인증
    @FormUrlEncoded
    @POST("user/email")
    Call<String> checkEmail(@Field("email") String email);
    @FormUrlEncoded
    @POST("user/email/newPw")
    Call<String> sendNewPwToEmail(@Field("email") String email);


    //sns 연동 interface
    @GET("sns")
    Call<sns_model> getSnsId(@Query("sns_id") String sns_id, @Query("sns_type") String sns_type);
    @FormUrlEncoded
    @POST("sns")
    Call<Void> postSnsToUser(@Field("id") String id, @Field("sns_id") String sns_id, @Field("sns_type") String sns_type);
}
