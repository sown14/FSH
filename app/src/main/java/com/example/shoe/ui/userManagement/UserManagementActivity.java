package com.example.shoe.ui.userManagement;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.User;
import com.example.shoe.models.Voucher;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.voucherManagement.VoucherAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementActivity extends AppCompatActivity {


    TextView tvTitle;
    ImageButton btnBack;

    RecyclerView recyclerView;

    static UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);


        tvTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(null);
        recyclerView.setAdapter(userAdapter);

        tvTitle.setText("Quản lỹ người dùng");

        _btnBackOnClick();
        fetchUsers();
    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public  static  void fetchUsers (){
        Call<ApiResponse<List<User>>> call = ApiClient.getInstance().getUsers();
        call.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if(response.isSuccessful()){
                    List<User> users = response.body().getData();
                    if(users !=null && ! users.isEmpty()){
                        userAdapter.setData(users);
                        userAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable throwable) {

            }
        });
    }
}