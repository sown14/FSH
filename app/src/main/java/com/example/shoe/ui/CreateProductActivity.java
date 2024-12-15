package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.models.Product;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.productManagement.ProductManagementActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductActivity extends AppCompatActivity {


    AppCompatButton btnSubmit;

    EditText edtName, edtPrice, edtMaterial, edtSize, edtDesc, edtImageURL;

    ImageButton btnBack;

    TextView txtTitle;

    NestedScrollView scrollview;

    Spinner spinnerCategories;

    List<String> categoryNames= new ArrayList<>();
    List<Category> categories;

    Integer selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_product);

        spinnerCategories = findViewById(R.id.spinner_categories);

        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        edtMaterial = findViewById(R.id.edt_material);
        edtSize = findViewById(R.id.edt_size);
        edtDesc = findViewById(R.id.edt_desc);
        edtImageURL = findViewById(R.id.edt_image);

        btnBack = findViewById(R.id.btn_back);
        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText("Tạo sản phẩm");

        btnSubmit = findViewById(R.id.btn_submit);
        scrollview = findViewById(R.id.scrollview);

        _btnBackOnClick();
        _btnSubmitOnClick();
        _getCategories();
        _handleCategorySelected();

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
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String material = edtMaterial.getText().toString();
                String size = edtSize.getText().toString();
                String desc = edtDesc.getText().toString();
                String image = edtImageURL.getText().toString();
                if (name.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập tên sản phẩm");
                } else if (price.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập giá");
                } else if (image.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập địa chỉ ảnh");
                } else if (material.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập chất liệu");
                } else if (size.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập size");
                } else {
                    _createProduct(name, Integer.valueOf(price), material, Integer.valueOf(size), desc, image);
                }
            }
        });
    }

    private void _createProduct(String name, Integer price, String material, Integer size, String desc, String image){
        Call<ApiResponse<Product>> call = ApiClient.getInstance().createProduct(name, price, material, size, desc, selectedCategoryId, image);
        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if(response.isSuccessful()){
                    _alert("Tạo sản phẩm thành công");
                    edtName.setText("");
                    edtPrice.setText("");
                    edtMaterial.setText("");
                    edtSize.setText("");
                    edtDesc.setText("");

                    ProductManagementActivity.getData();

                }
//                else {
//                    AppUtils.alertError(CreateProductActivity.this, response);
//                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable throwable) {

            }
        });
    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private  void _getCategories(){
        Call<ApiResponse<List<Category>>> call = ApiClient.getInstance().getCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if(response.isSuccessful()){
                    List<Category> categoryList = response.body().getData();
                    categories = categoryList;
                    if(categoryList !=null && !categoryList.isEmpty()){

                        for (Category category : categoryList) {
                            categoryNames.add(category.getName());

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateProductActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                        // Specify the layout to use when the list of choices appears.
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner.
                        spinnerCategories.setAdapter(adapter);
                        selectedCategoryId = categoryList.get(0).getId();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {

            }
        });
    }

    private void _handleCategorySelected (){
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedCategoryId = categories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}