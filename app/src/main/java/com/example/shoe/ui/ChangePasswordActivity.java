package com.example.shoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.example.shoe.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    AppCompatButton btnSubmit;

    EditText edtOldPass, edtNewPass;

    ImageButton btnBack;

    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnBack = findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btn_submit);
        edtOldPass = findViewById(R.id.edt_old);
        edtNewPass = findViewById(R.id.edt_new);
        tvTitle = findViewById(R.id.txt_title);

        tvTitle.setText("Thay đổi mật khẩu");

        _btnBackOnClick();
        _btnSubmitOnClick();

    }

    private void _btnBackOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void _btnSubmitOnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                if (oldPass.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập mật khẩu hiện tại");
                } else if (newPass.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập mật khẩu mới");
                } else {
                    _updatePass(oldPass, newPass);
                }

            }
        });
    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void _updatePass(String oldP, String newP) {
        Call<ApiResponse<User>> call = ApiClient.getInstance().updatePassword(oldP, newP);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    _alert("Đổi mật khẩu thành công");
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AppUtils.alertError(ChangePasswordActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {

            }
        });
    }

}