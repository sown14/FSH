package com.example.shoe.ui.voucherManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.models.Order;
import com.example.shoe.models.Voucher;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.CreateCategoryActivity;
import com.example.shoe.ui.CreateVoucherActivity;
import com.example.shoe.ui.categoryManagement.CategoryManagementActivity;
import com.example.shoe.ui.voucherManagement.VoucherAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherManagementActivity extends AppCompatActivity {


    TextView tvTitle;
    ImageButton btnBack;

    RecyclerView recyclerView;

    FloatingActionButton fab;

    static  VoucherAdapter voucherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_management);

        tvTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        voucherAdapter = new VoucherAdapter(null);
        recyclerView.setAdapter(voucherAdapter);

        tvTitle.setText("Quản lý mã voucher");
        _btnBackOnClick();
        fetchVouchers();
        _fabOnClick();
    }

    private  void _fabOnClick (){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoucherManagementActivity.this, CreateVoucherActivity.class);
                startActivity(intent);
            }
        });
    }
    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public  static void fetchVouchers () {
        Call<ApiResponse<List<Voucher>>> call = ApiClient.getInstance().getVouchers();
        call.enqueue(new Callback<ApiResponse<List<Voucher>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Voucher>>> call, Response<ApiResponse<List<Voucher>>> response) {
                if(response.isSuccessful()){
                    List<Voucher> vouchers = response.body().getData();
                    if(vouchers !=null && ! vouchers.isEmpty()){
                        voucherAdapter.setData(vouchers);
                        voucherAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Voucher>>> call, Throwable throwable) {

            }
        });
    }
}