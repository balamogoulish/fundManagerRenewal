package com.example.fundmanager_renewal.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;

import org.mindrot.jbcrypt.BCrypt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPwActivity extends AppCompatActivity {
    EditText edit_email, edit_id;
    Call<user_model> callUser;
    Call<String> callStr;
    Call<Void> call;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        edit_email = (EditText) findViewById(R.id.pw_email_edit);
        edit_id = (EditText) findViewById(R.id.pw_id_edit);
    }

    public void sendNewPw(View target){
        String email = edit_email.getText().toString();
        String id = edit_id.getText().toString();
        callUser = retrofit_client.getApiService().findUserId(email);
        callUser.enqueue(new Callback<user_model>() {
            @Override
            public void onResponse(Call<user_model> call, Response<user_model> UserResponse) {
                if(UserResponse.isSuccessful() && UserResponse!=null){
                    user_model UserResult = UserResponse.body();
                    if(id.equals(UserResult.getID())){ // 이메일과 아이디가 대응하면, 비밀번호 재발급
                        sendNewPwEmail(email, id);
                    }else{
                        Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "등록되지 않은 이메일입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<user_model> call, Throwable t) {
                Log.d("FindIdERROR", t+"");
                Toast.makeText(getApplicationContext(), "등록되지 않은 이메일입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNewPwEmail(String email, String id){
        Log.d("EMAILANDID", email+id);
        callStr = retrofit_client.getApiService().sendNewPw(email);
        callStr.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "메일로 비밀번호를 재발급했습니다.", Toast.LENGTH_SHORT).show();
                    updateNewPw(response.body(), id);
                } else{
                    Log.d("EMAILERROR", "Response not successful or body is null");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("EMAILERROR", t+"");
            }
        });
    }
    public void updateNewPw(String pw, String id){
        call = retrofit_client.getApiService().updatePw(id, BCrypt.hashpw(pw, BCrypt.gensalt()));
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
}