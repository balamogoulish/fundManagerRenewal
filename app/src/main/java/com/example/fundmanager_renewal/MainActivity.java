package com.example.fundmanager_renewal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit_id, edit_pw;

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
//        String user_index;

        if(id.length()>0 && pw.length()>0){
            if(id.equals("abc") && pw.equals("123")){
                Toast.makeText(getApplicationContext(), "로그인에 성공했습니다! :)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//                intent.putExtra("user_index", user_index);
                startActivity(intent);
            }
            else{
                edit_id.setText("");
                edit_pw.setText("");
                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다...", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSignUp(View target){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}