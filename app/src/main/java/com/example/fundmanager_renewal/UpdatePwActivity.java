package com.example.fundmanager_renewal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePwActivity  extends AppCompatActivity {

    EditText edit_originPw, edit_newPw;
    String id;
    Call<user_model> callUser;
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
        String originPw = edit_originPw.getText().toString();
        String newPw = edit_newPw.getText().toString();

        String pwPattern = "([0-9].*[!,@,#,^,*,(,)])|([!,@,#,^,*,(,)].*[5,])";
        Pattern pattern_pw = Pattern.compile(pwPattern);
        Matcher matcher = pattern_pw.matcher(newPw);

        if(!matcher.find()){
            Toast.makeText(getApplicationContext(), "숫자, 특수문자가 포함된 5-9자를 비밀번호로 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else{
            callUser = retrofit_client.getApiService().login(id, originPw);
            callUser.enqueue(new Callback<user_model>() {
                @Override
                public void onResponse(Call<user_model> call, Response<user_model> response) {
                    if(response.isSuccessful() && response != null){
                        updatePw(newPw);
                    } else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<user_model> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void updatePw(String newPw){
        call = retrofit_client.getApiService().updatePw(id, newPw);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "정상적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
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
}