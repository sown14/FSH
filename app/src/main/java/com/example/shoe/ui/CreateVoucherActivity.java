package com.example.shoe.ui;


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
import com.example.shoe.models.Product;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.voucherManagement.VoucherManagementActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVoucherActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvTitle;

    EditText edtCode, edtDesc, edtLimit, edtMin, edtMax, edtMinOrderValue;

    AppCompatButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voucher);

        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText("Tạo mã voucher");

        btnSubmit = findViewById(R.id.btn_submit);

        edtCode = findViewById(R.id.edt_code);
        edtDesc = findViewById(R.id.edt_desc);
        edtLimit = findViewById(R.id.edt_limit);
        edtMin = findViewById(R.id.edt_min);
        edtMax = findViewById(R.id.edt_max);
        edtMinOrderValue = findViewById(R.id.edt_min_order_value);

        _btnSubmitOnClick();
        _btnBackOnClick();
    }

    private void _btnBackOnClick(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void _resetAllField(){
        edtCode.setText(null);
        edtDesc.setText(null);
        edtMin.setText(null);
        edtMax.setText(null);
        edtMinOrderValue.setText(null);
        edtLimit.setText(null);
    }
    private void _btnSubmitOnClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtCode.getText().toString();
                String desc = edtDesc.getText().toString();
                String limit = edtLimit.getText().toString();
                String min = edtMin.getText().toString();
                String max = edtMax.getText().toString();
                String minOrderValue = edtMinOrderValue.getText().toString();

                if (code.trim().equalsIgnoreCase("")
                        || desc.trim().equalsIgnoreCase("")
                        || limit.trim().equalsIgnoreCase("")
                        || min.trim().equalsIgnoreCase("")
                        || max.trim().equalsIgnoreCase("")
                        || minOrderValue.trim().equalsIgnoreCase("")
                ) {
                    _alert("Vui lòng nhập đủ các trường");
                } else {
                    _createVoucher(code.trim(), desc.trim(), Integer.valueOf(limit), Integer.valueOf(min), Integer.valueOf(max), Integer.valueOf(minOrderValue));
                }
            }
        });
    }

    private void _createVoucher(String code, String desc, Integer limit, Integer min, Integer max, Integer minOrderValue) {
        Call<ApiResponse<Product>> call = ApiClient.getInstance().createVoucher(code, desc, limit, min, max, minOrderValue);
        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                _alert("Tạo mã voucher thành công");
                if (response.isSuccessful()) {
                    VoucherManagementActivity.fetchVouchers();
                    _resetAllField();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable throwable) {

            }
        });
    }
}