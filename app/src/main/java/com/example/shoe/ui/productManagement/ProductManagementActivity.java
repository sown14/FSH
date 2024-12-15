package com.example.shoe.ui.productManagement;

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
import com.example.shoe.models.Product;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.CreateProductActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagementActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageButton btnBack;

    RecyclerView recyclerView;

    FloatingActionButton fab;


    static ProductManagementAdapter productManagementAdapter;

    static List<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);



        tvTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        fab = findViewById(R.id.fab);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productManagementAdapter = new ProductManagementAdapter(products);
        recyclerView.setAdapter(productManagementAdapter);

        tvTitle.setText("Quản lý sản phẩm");


        _btnBackOnClick();
        _fabOnClick();
         getData();
    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private  void _fabOnClick (){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateToCreate();
            }
        });
    }

    private void _navigateToCreate (){
        Intent intent = new Intent(ProductManagementActivity.this, CreateProductActivity.class);
        startActivity(intent);
    }

    public static void getData(){
        Call<ApiResponse<List<Product>>> call = ApiClient.getInstance().getProducts();
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if(response.isSuccessful()){
                    List<Product> productRes = response.body().getData();
                    if(productRes !=null & !productRes.isEmpty()){
                        products = productRes;
                        productManagementAdapter.setData(products);
                        productManagementAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });
    }


}