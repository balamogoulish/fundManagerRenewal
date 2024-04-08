package com.example.fundmanager_renewal.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.putPostGainCallback;
import com.example.fundmanager_renewal.callbacks.getUserCallback;
import com.example.fundmanager_renewal.callbacks.sendEmailCallback;
import com.example.fundmanager_renewal.callbacks.postTranCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.utils.sns.EmailUtils;
import com.example.fundmanager_renewal.utils.sns.GainUtils;
import com.example.fundmanager_renewal.utils.sns.TranUtils;
import com.example.fundmanager_renewal.utils.sns.UserUtils;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements getUserCallback, sendEmailCallback, putPostGainCallback, postTranCallback {
    EditText edit_name, edit_id, edit_pw, edit_checkPw, edit_email, edit_emailCheck, edit_account;
    TextView warn_name, warn_id, warn_pw, warn_pwRe, warn_email, warn_emailCheck, warn_account;
    Call<Void> call;
    Call<user_model> call_user;
    String user_index;
    private String emailVerificationCode;
    private boolean emailValid = false,
            initGainValid = false,
            initTranValid = false;
    private boolean idValid = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_name = (EditText) findViewById(R.id.name_signup_edit);
        edit_id = (EditText) findViewById(R.id.id_signup_edit);
        edit_pw = (EditText) findViewById(R.id.pw_signup_edit);
        edit_checkPw = (EditText) findViewById(R.id.checkpw_edit);
        edit_email =(EditText) findViewById(R.id.email_signup_edit);
        edit_emailCheck = (EditText) findViewById(R.id.checkEmail_edit);
        edit_account =(EditText) findViewById(R.id.account_edit);
        warn_name = (TextView) findViewById(R.id.warnName_txt);
        warn_id = (TextView) findViewById(R.id.warnId_txt);
        warn_pw = (TextView) findViewById(R.id.warnPw_txt);
        warn_pwRe = (TextView) findViewById(R.id.warnPwRe_txt);
        warn_email = (TextView) findViewById(R.id.warnEmail_txt);
        warn_emailCheck = (TextView) findViewById(R.id.warnEmailCheck_txt);
        warn_account = (TextView) findViewById(R.id.warnAccount_txt);

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
            String pwHashed = BCrypt.hashpw(pw, BCrypt.gensalt());
            call_user = retrofit_client.getApiService().postUser(name, id, pwHashed, email, account);
            call_user.enqueue(new Callback<user_model>() {
                @Override
                public void onResponse(Call<user_model> call, Response<user_model> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "성공적으로 가입되었습니다!! :)", Toast.LENGTH_SHORT).show();
                        user_model result = response.body();
                        user_index = result.getUserIndex()+"";
                        initGainTran();
                        if(initTranValid && initGainValid){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                @Override
                public void onFailure(Call<user_model> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "가입 실패...:(", Toast.LENGTH_SHORT).show();
                    Log.d("SignUPAPI ERROR", t+"");
                }
            });
        }
    }
    public String checkSignUpValid(String name, String id, String pw, String pwRe, String email, String account){
        String result = "SUCCESS";
        String pwPattern = "([0-9].*[!,@,#,^,*,(,)])|([!,@,#,^,*,(,)].*[5, ])";
        Pattern pattern_pw = Pattern.compile(pwPattern);
        Matcher matcher = pattern_pw.matcher(pw);
        if(name.length() < 2 ){
            warn_name.setText("이름을 두 자 이상입력해주세요.");
            result = "FAIL";
        }
        if(id.length() < 6 ||id.length() > 12){
            warn_id.setText("6-12자리의 아이디를 입력해주세요.");
            result = "FAIL";

        } else if(!idValid){
            warn_id.setText("아이디 중복 여부를 확인해주세요.");
            result = "FAIL";
        } else {
            warn_id.setText("");
        }
        if(!matcher.find()){
            warn_pw.setText("숫자, 특수문자가 포함된 5-9자를 비밀번호로 입력해주세요.");
            result = "FAIL";
        } else{
            warn_pw.setText("");
        }
        if(pw.equals(pwRe) != true){
            warn_pwRe.setText("비밀번호가 일치하지 않습니다.");
            result = "FAIL";
        } else{
            warn_pwRe.setText("");
        }
        if(email.length() == 0){
            warn_email.setText("이메일을 입력해주세요.");
            result = "FAIL";
        } else{
            warn_email.setText("");
        }
        if(!emailValid){
            warn_emailCheck.setText("이메일을 인증해주세요.");
            result = "FAIL";
        } else{
            warn_emailCheck.setText("");
        }
        if(account.length() == 0){
            warn_account.setText("계좌번호를 입력해주세요.");
            result = "FAIL";
        } else{
            warn_account.setText("");
        }

        return result;
    }
    public void idDuplication(View target){
        String id = edit_id.getText().toString();
        UserUtils.getUser(id, "id",this);
    }
    public void sendEmailVerificationCode(View target){
        String email = edit_email.getText().toString();
        EmailUtils.sendEmailCode(email, "email", this);
    }
    public void emailCheck(View target){
        String emailCheck = edit_emailCheck.getText().toString();
        if(emailCheck.equals(emailVerificationCode)){
            Toast.makeText(getApplicationContext(), "이메일이 인증되었습니다!", Toast.LENGTH_SHORT).show();
            emailValid = true;
        } else{
            Toast.makeText(getApplicationContext(), "인증번호가 일치하지 않습니다.\n다시 시도해주세요...", Toast.LENGTH_SHORT).show();
            emailValid = false;
        }
    }
    @Override
    public void getUserSuccess(user_model result){
        Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
        idValid = false;
    }
    @Override
    public void getUserFail(String t){
        Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
        idValid = true;
    }
    @Override
    public void sendEmailSuccess(String code) {
        Log.d("EmailVerification", code);
        emailVerificationCode = code;
    }
    @Override
    public void sendEmailFail(String t) {
        Log.e("SendEmail", t);
    }
    public void initGainTran(){
        GainUtils.PutOrPostGain("post", user_index, "0", "0", this);
        TranUtils.postTran(user_index, "0", "0", "0", 0, "", this);
    }
    @Override
    public void putPostGainSuccess() {
        initGainValid = true;
    }
    @Override
    public void putPostGainFail(String t) {
        Log.e("GainAPI ERROR", t+"");
    }
    @Override
    public void postTranSuccess(long total, String input) {
        initTranValid =true;
    }
    @Override
    public void postTranFail(String t) {
        Log.e("TranAPI ERROR", t);
    }
}