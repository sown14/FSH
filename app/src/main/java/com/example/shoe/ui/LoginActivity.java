package com.example.shoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Role;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.example.shoe.utils.AppUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 2;
    GoogleSignInClient mGoogleSignInClient;
    ImageButton btnGoogle;
    TextView txtCreateAccount;

    EditText edtUsername, edtPassword;
    AppCompatButton btnLogin;


    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCreateAccount = findViewById(R.id.txt_create_account);

        btnGoogle = findViewById(R.id.btn_google);
        btnLogin = findViewById(R.id.btn_login);

        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        // google info
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        _btnClickLoginWithGoogle();
        _btnClickLogin();
        _navigateToSignUp();
        _getDeviceToken();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String idToken = account.getIdToken();
                    _loginWithGoogle(idToken);
                }
            } catch (ApiException e) {
                Log.e("GOOGLE_ERR", "Google Sign-In failed", e);
            }
        }
    }

    private void _btnClickLoginWithGoogle() {
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_ONE_TAP);
            }
        });
    }


    private void _loginWithGoogle(String idToken) {

        Call<ApiResponse<User>> call = ApiClient.getInstance().loginWithGoogle(idToken, token);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {

                if (response.isSuccessful()) {
                    User user = response.body().getData();
                    _loginSuccessAction(user);

                } else {
                    AppUtils.alertError(LoginActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {
                Log.e("API_ERROR", "Failed to fetch users", throwable);
            }
        });


    }

    private void _navigateToSignUp() {
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    private void _btnClickLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (username.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập tên người dùng");
                } else if (password.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập mật khẩu");
                } else {
                    _login(username.trim(), password.trim());
                }
            }
        });
    }

    private void _login(String username, String password) {

        Call<ApiResponse<User>> call = ApiClient.getInstance().login(username, password, token);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                Log.d("vo", "vo1");
                if (response.isSuccessful()) {
                    User user = response.body().getData();
                    _loginSuccessAction(user);

                } else {
                    AppUtils.alertError(LoginActivity.this, response);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {
//                Log.e("API_ERROR", "Failed to fetch users", throwable);
            }
        });


    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private void _loginSuccessAction(User user) {
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        MyApplication.saveUser(user);

        if (user.getRole().equals(Role.ADMIN)) {
            Intent intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
            startActivity(intent);
            finish();
        } else if (user.getRole().equals(Role.USER)) {
            Intent intent = new Intent(LoginActivity.this, MyTabsActivity.class);
            startActivity(intent);
            finish();
        }

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