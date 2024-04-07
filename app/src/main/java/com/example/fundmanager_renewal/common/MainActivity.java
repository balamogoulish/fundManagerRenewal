package com.example.fundmanager_renewal.common;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.KakaoCallback;
import com.example.fundmanager_renewal.model.sns_model;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.sns.KakaoUtils;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements KakaoCallback{
    EditText edit_id, edit_pw;
    Call<user_model> call;
    Call<sns_model> snsCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_id = findViewById(R.id.id_edit);
        edit_pw = findViewById(R.id.pw_edit);
    }

    public void login(View target){
        String id = edit_id.getText().toString();
        String pw = edit_pw.getText().toString();

        call = retrofit_client.getApiService().checkIdDuplicate(id);

        if(id.length()>0 && pw.length()>0){
            call.enqueue(new Callback<user_model>(){
                @Override
                public void onResponse(Call<user_model> call, Response<user_model> response){
                    if (response.isSuccessful() && response.body()!=null) {
                        user_model result = response.body();
                        if(BCrypt.checkpw(pw, result.getPassword())){
                            Toast.makeText(getApplicationContext(), "로그인에 성공했습니다! :)", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.putExtra("user_index", result.getUserIndex()+"");
                            intent.putExtra("username", result.getUsername());
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            edit_pw.setText("");
                        }
                    }
                }
                @Override
                public void onFailure(Call<user_model> call, Throwable t){
                    Toast.makeText(getApplicationContext(), "아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    edit_id.setText("");
                    edit_pw.setText("");
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginKakao(View target){
        KakaoUtils.linkKakao(this, this);
    }

    public void loginSns(long sns_id, String sns_type, String nickName){
        //sns_id와 sns_type가 일치하는 user_index로 로그인함
        snsCall = retrofit_client.getApiService().getSnsId(sns_id, sns_type);
        snsCall.enqueue(new Callback<sns_model>() {
            @Override
            public void onResponse(Call<sns_model> call, Response<sns_model> response) {
                if(response.isSuccessful() && response.body()!=null){
                    sns_model result = response.body();
                    Log.d("loginSns", "sns_id: "+sns_id+"\nsns_type: "+sns_type+"\nid: "+result.getUser_index()+"\nnickName: "+nickName);
                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다! :)", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.putExtra("user_index", result.getUser_index()+"");
                    intent.putExtra("username", nickName);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "<내 정보>에서 sns와 계정을 연동해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<sns_model> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "일치하는 계정을 찾을 수 없습니다.\n<내 정보>에서 sns와 계정을 연동해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void kakaoLoginReceived(long sns_id, String nickName) {
        loginSns(sns_id, "KAKAO", nickName);
    }

    public void goToSignUp(View target){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void goToFindId(View target){
        Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
        startActivity(intent);
    }

    public void goToFindPw(View target){
        Intent intent = new Intent(getApplicationContext(), FindPwActivity.class);
        startActivity(intent);
    }
}