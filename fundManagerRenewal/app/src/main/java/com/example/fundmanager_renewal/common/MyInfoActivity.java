package com.example.fundmanager_renewal.common;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.KakaoCallback;
import com.example.fundmanager_renewal.callbacks.NaverCallback;
import com.example.fundmanager_renewal.callbacks.getUserCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.utils.sns.KakaoUtils;
import com.example.fundmanager_renewal.utils.sns.NaverUtils;
import com.example.fundmanager_renewal.utils.sns.UserUtils;
import com.kakao.sdk.user.model.User;
import com.navercorp.nid.NaverIdLoginSDK;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInfoActivity extends AppCompatActivity implements KakaoCallback, NaverCallback, getUserCallback {
    TextView name, email, account;
    String user_index;
    private String id;
    NaverIdLoginSDK naverLoginModule;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        user_index = intent.getStringExtra("user_index");

        name = findViewById(R.id.name_txt);
        email = findViewById(R.id.email_txt);
        account = findViewById(R.id.account_txt);

        naverLoginModule = NaverIdLoginSDK.INSTANCE;
        naverLoginModule.initialize(this,
                getResources().getString(R.string.naver_client_id),
                getResources().getString(R.string.naver_client_secret),
                getResources().getString(R.string.naver_client_name));

        UserUtils.getUser(user_index, "index", this);
    }
    @Override
    public void getUserSuccess(user_model result) {
        name.setText(result.getUsername());
        email.setText(result.getEmail());
        account.setText(result.getAccount());
        id = result.getID();
    }
    @Override
    public void getUserFail(String t) {
        Log.e("getUser", t+"");
    }

    public void goToKakao(View target){
        KakaoUtils.linkKakao(this, this);
    }
    @Override
    public void kakaoLoginReceived(long sns_id, String nickname) {
        newSNSId(sns_id+"", "KAKAO");
    }
    public void goToNaver(View target){ NaverUtils.linkNaver(this, this);}
    @Override
    public void naverLoginReceived(String sns_id, String nickname){
        newSNSId(sns_id+"", "NAVER");
    }
    public void newSNSId(String sns_id, String sns_type){
        //user_index, sns_id, sns_type을 sns_info table에 insert
        Log.d(TAG, "invoke: id =" + sns_id);
        Log.d(TAG, "invoke: type =" + sns_type);
        Call<Void> call = retrofit_client.getApiService().postSnsToUser(user_index, sns_id, sns_type);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "정상적으로 연동되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요 :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToUpdatePw(View target){
        Intent intent = new Intent(getApplicationContext(), UpdatePwActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    public void checkDeleteReally(View target){
        AlertDialog.Builder ad = new AlertDialog.Builder(MyInfoActivity.this);
        ad.setMessage("탈퇴하시겠습니까?");
        ad.setPositiveButton("확인", (dialog, which) -> deleteMyAccount());
        ad.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        ad.show();
    }
    public void deleteMyAccount(){
        Call<Void> call = retrofit_client.getApiService().deleteUser(user_index);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "정상적으로 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요 :(", Toast.LENGTH_SHORT).show();
            }
        });
    }


}