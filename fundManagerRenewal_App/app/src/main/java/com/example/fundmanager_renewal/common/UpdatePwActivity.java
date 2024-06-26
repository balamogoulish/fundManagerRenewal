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
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.utils.sns.UserUtils;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePwActivity  extends AppCompatActivity implements getUserCallback {

    EditText edit_originPw, edit_newPw;
    String id, originPw, newPw;
    Call<Void> call;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pw);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        edit_originPw = (EditText) findViewById(R.id.origin_pw_edit);
        edit_newPw = (EditText) findViewById(R.id.new_pw_edit);
    }

    public void checkOriginPw(View target){
        originPw = edit_originPw.getText().toString();
        newPw = edit_newPw.getText().toString();

        String pwPattern = "([0-9].*[!,@,#,^,*,(,)])|([!,@,#,^,*,(,)].*[5,])";
        Pattern pattern_pw = Pattern.compile(pwPattern);
        Matcher matcher = pattern_pw.matcher(newPw);

        if(!matcher.find()){
            Toast.makeText(getApplicationContext(), "숫자, 특수문자가 포함된 5-9자를 비밀번호로 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else{
            UserUtils.getUser(id,"id",this);
        }
    }

    public void updatePw(String newPw){
        call = retrofit_client.getApiService().putUserPw(id, BCrypt.hashpw(newPw, BCrypt.gensalt()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "정상적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
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
        if( BCrypt.checkpw(originPw, result.getPassword())){
            updatePw(newPw);
        } else{
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void getUserFail(String t) {
        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        Log.e("GetUser", t);
    }
}