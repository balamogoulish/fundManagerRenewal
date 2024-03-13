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

public class SignUpActivity extends AppCompatActivity {
    EditText edit_name, edit_id, edit_pw, edit_checkPw, edit_email, edit_account;
    Call<Void> call;
    Call<user_model> call_user;

    String user_index;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_name = (EditText) findViewById(R.id.name_signup_edit);
        edit_id = (EditText) findViewById(R.id.id_signup_edit);
        edit_pw = (EditText) findViewById(R.id.pw_signup_edit);
        edit_checkPw = (EditText) findViewById(R.id.checkpw_edit);
        edit_email =(EditText) findViewById(R.id.email_signup_edit);
        edit_account =(EditText) findViewById(R.id.account_edit);
    }

    public void signUp(View target){
        String name = edit_name.getText().toString();
        String id = edit_id.getText().toString();
        String pw = edit_pw.getText().toString();
        String pwRe = edit_checkPw.getText().toString();
        String email = edit_email.getText().toString();
        String account = edit_account.getText().toString();

        String valid = checkSignUpValid(name, id, pw, pwRe, email, account);
        if(valid.equals("SUCCESS")){
            call = retrofit_client.getApiService().signUp(name, id, pw, email, account);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "성공적으로 가입되었습니다!! :)", Toast.LENGTH_SHORT).show();

                        call_user = retrofit_client.getApiService().login(id,pw);
                        call_user.enqueue(new Callback<user_model>() {
                            @Override
                            public void onResponse(Call<user_model> call, Response<user_model> response) {
                                user_model result = response.body();
                                user_index = result.getUserIndex()+"";
                                if(initTransaction().equals(initGain())){
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<user_model> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"유저 정보를 초기화하는데 실패했습니다..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Log.d("notFailureBut", response+"");
                        Toast.makeText(getApplicationContext(),"가입 실패...:(", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "가입 실패...:(", Toast.LENGTH_SHORT).show();
                    Log.d("SignUPAPI ERROR", t+"");
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), valid, Toast.LENGTH_SHORT).show();
        }
    }

    public String checkSignUpValid(String name, String id, String pw, String pwRe, String email, String account){
        String result = "SUCCESS";
        String pwPattern = "([0-9].*[!,@,#,^,*,(,)])|([!,@,#,^,*,(,)].*[0-9])";
        Pattern pattern_pw = Pattern.compile(pwPattern);
        Matcher matcher = pattern_pw.matcher(pw);
        if(id.length() < 6 ||id.length() > 12){
            result = "6-12자리의 아이디를 입력해주세요.";
        } else if(!matcher.find()){
            result = "숫자, 특수문자가 포함된 0-9자를 비밀번호로 입력해주세요.";
        } else if(name.length() < 2 ){
            result = "이름을 두 자 이상입력해주세요.";
        } else if(account.length() == 0){
            result = "계좌번호를 입력해주세요.";
        } else if(pw.equals(pwRe) != true){
            result = "비밀번호가 일치하지 않습니다.";
        } else if(email.length() == 0){
            result = "이메일을 입력해주세요.";
        }

        return result;
    }

    public String initTransaction(){
        final String[] initTran = {"Fail"};
        call = retrofit_client.getApiService().postTran(user_index, "0", "0", "0");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    initTran[0] = "Success";
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TranAPI ERROR", t+"");
            }
        });
        return initTran[0];
    }
    public String initGain(){
        final String[] initGain = {"Fail"};
        call = retrofit_client.getApiService().postGain(user_index, "0", "0");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    initGain[0] = "Success";
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TranAPI ERROR", t+"");
            }
        });
        return initGain[0];
    }
}