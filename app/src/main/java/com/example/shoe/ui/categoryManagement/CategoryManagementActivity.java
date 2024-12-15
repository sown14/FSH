package com.example.shoe.ui.categoryManagement;

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
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.CreateCategoryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryManagementActivity extends AppCompatActivity {


    TextView txtTitle;
    ImageButton btnBack;

    RecyclerView recyclerView;

    FloatingActionButton fab;

    static CategoryAdapter categoryAdapter;
    static List<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_management);


        txtTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(categoryAdapter);


        txtTitle.setText("Quản lý danh mục");
        _btnBackOnClick();
        getData();
        _fabOnClick();
    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void _navigateToCreate (){
        Intent intent = new Intent(CategoryManagementActivity.this, CreateCategoryActivity.class);
        startActivity(intent);
    }

    private  void _fabOnClick (){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateToCreate();
            }
        });
    }
    public  static void getData(){
        Call<ApiResponse<List<Category>>> call = ApiClient.getInstance().getCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if(response.isSuccessful()){
                    List<Category> categoryList = response.body().getData();

                    if(categoryList !=null && !categoryList.isEmpty()){
                        categories = categoryList;
                        categoryAdapter.setCategories(categories);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {

            }
        });
    }


}

