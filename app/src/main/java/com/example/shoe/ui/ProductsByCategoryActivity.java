package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.models.Product;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.home.ProductHomeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsByCategoryActivity extends AppCompatActivity {
    ImageButton btnBack;

    TextView tvTitle;

    RecyclerView recyclerView;

    ProductHomeAdapter adapter;

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_products_by_category);

        category = (Category) getIntent().getSerializableExtra("category");

        recyclerView = findViewById(R.id.recycler_view);
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText(category.getName());



        _btnBackOnClick();
        _setUpRecyclerView();
        _getData(category.getId());
    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void _setUpRecyclerView (){
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductHomeAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    private void _getData (Integer categoryId) {
        Call<ApiResponse<List<Product>>> call = ApiClient.getInstance().getProductsByCategory(categoryId);
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if(response.isSuccessful()){
                    List<Product> productRes = response.body().getData();
                    if(productRes !=null & !productRes.isEmpty()){
                        adapter.setData(productRes);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        adapter.setData(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });
    }
}