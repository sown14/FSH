package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.example.shoe.services.ApiService;
import com.example.shoe.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText edtFullname, edtUsername, edtEmail, edtPass, edtRepass, edtPhone, edtDateOfBirth;
    AppCompatButton btnSignUp;

    ApiService apiService;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btn_signup);

        edtFullname = findViewById(R.id.edt_fullname);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        edtRepass = findViewById(R.id.edt_repass);
        edtPhone = findViewById(R.id.edt_phone);
        edtDateOfBirth = findViewById(R.id.edt_date_of_birth);
        
        apiService = ApiClient.getInstance();

        _btnClickSignUp();
        _getDeviceToken();

    }

    private void _btnClickSignUp() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname = edtFullname.getText().toString();
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                String repass = edtRepass.getText().toString();
                String phone = edtPhone.getText().toString();
                String dateOfBirth = edtDateOfBirth.getText().toString();

                if (fullname.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập họ và tên");
                } else if (username.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập tên người dùng");
                } else if (email.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập địa chỉ email");
                } else if (phone.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập số điện thoại");
                } else if (dateOfBirth.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập ngày sinh");
                } else if (pass.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập mật khẩu");
                } else if (repass.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập xác nhận mật khẩu");
                } else if (!pass.equals(repass)) {
                    _alert("Mật khẩu và xác nhận mật khẩu không khớp");
                } else {
                    _onSubmit(username.trim(), fullname.trim(), email.trim(), pass.trim(), phone.trim(), dateOfBirth.trim());
                }
            }
        });
    }


    private void _onSubmit(String username, String fullname, String email, String password, String phone, String dateOfBirth) {
        Call<ApiResponse<User>> call = apiService.signUp(username, fullname, password, email, phone, dateOfBirth, token);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    _alert("Đăng ký thành công");
                    onBackPressed();
                } else {
                    AppUtils.alertError(SignUpActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {

            }
        });

    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void _getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    token = task.getResult();
//                     Log.d("FCM_TOKEN", token);
                }
            }
        });
    }

}