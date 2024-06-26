package com.example.fundmanager_renewal.common;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundmanager_renewal.R;
import com.example.fundmanager_renewal.callbacks.getUserCallback;
import com.example.fundmanager_renewal.retrofit.retrofit_client;
import com.example.fundmanager_renewal.model.user_model;
import com.example.fundmanager_renewal.utils.sns.UserUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIdActivity extends AppCompatActivity implements getUserCallback {
    EditText edit_email;
    TextView id_result;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        edit_email = (EditText) findViewById(R.id.email_edit);
        id_result = (TextView) findViewById(R.id.idresult_txt);
    }

    public void findId(View target){
        String email = edit_email.getText().toString();
        UserUtils.getUser(email,"email", this);
    }
    @Override
    public void getUserSuccess(user_model result) {
        id_result.setText(result.getID());
    }
    @Override
    public void getUserFail(String t) {
        Log.e("GetUser", t);
        Toast.makeText(getApplicationContext(), "등록되지 않은 이메일입니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
    }
}
