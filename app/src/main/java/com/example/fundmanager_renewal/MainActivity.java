package com.example.fundmanager_renewal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText edit_id, edit_pw;
    Call<user_model> call;

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
                    if (response.isSuccessful()) {

                        user_model result = response.body();
                        if(result == null){

                        } else {
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