package com.example.fundmanager_renewal.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.getUserCallback;
import com.example.fundmanager_renewal.callbacks.sendEmailCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.utils.sns.EmailUtils;
import com.example.fundmanager_renewal.utils.sns.UserUtils;

import org.mindrot.jbcrypt.BCrypt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPwActivity extends AppCompatActivity implements getUserCallback, sendEmailCallback {
    EditText edit_email, edit_id;
    String email, id;
    Call<Void> call;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        edit_email = (EditText) findViewById(R.id.pw_email_edit);
        edit_id = (EditText) findViewById(R.id.pw_id_edit);
    }

    public void sendNewPw(View target){
        email = edit_email.getText().toString();
        id = edit_id.getText().toString();
        UserUtils.getUser(email, "email", this);
    }
    public void updateNewPw(String pw, String id){
        call = retrofit_client.getApiService().putUserPw(id, BCrypt.hashpw(pw, BCrypt.gensalt()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "문제가 발생했습니다, 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("UpdatePwERROR", t+"");
            }
        });
    }

    @Override
    public void getUserSuccess(user_model result) {
        if(id.equals(result.getID())){ // 이메일과 아이디가 대응하면, 비밀번호 재발급
            EmailUtils.sendEmailCode(email, "pw", this);
        }else{
            Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void getUserFail(String t) {
        Log.e("GetUser", t);
        Toast.makeText(getApplicationContext(), "등록되지 않은 이메일입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendEmailSuccess(String code) {
        Toast.makeText(getApplicationContext(), "메일로 비밀번호를 재발급했습니다.", Toast.LENGTH_SHORT).show();
        updateNewPw(code, id);
    }
    @Override
    public void sendEmailFail(String t) {
        Log.e("EMAILERROR", t+"");
    }
}