package com.example.shoe.ui;

import android.os.Bundle;
import android.util.Log;
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
import com.example.shoe.models.ProductStatus;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.productManagement.ProductManagementActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {

    AppCompatButton btnSubmit;

    EditText edtName, edtPrice, edtMaterial, edtSize, edtDesc, edtImageURL, edtCategoryName;

    ImageButton btnBack;

    TextView txtTitle;

    NestedScrollView scrollview;

    Spinner spinnerCategories, spinnerStatuses;

    List<String> categoryNames = new ArrayList<>();
    List<Category> categories;

    Integer selectedCategoryId;

    ProductStatus selectedStatus;

    List<ProductStatus> productKeys = new ArrayList<>();

    Map<ProductStatus, String> productStatusMap = new HashMap<ProductStatus, String>();

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_product);

//        spinnerCategories = findViewById(R.id.spinner_categories);

        spinnerStatuses = findViewById(R.id.spinner_statuses);

        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        edtMaterial = findViewById(R.id.edt_material);
        edtSize = findViewById(R.id.edt_size);
        edtDesc = findViewById(R.id.edt_desc);
        edtImageURL = findViewById(R.id.edt_image);
        edtCategoryName = findViewById(R.id.edt_category_name);

        btnBack = findViewById(R.id.btn_back);
        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText("Sửa sản phẩm");

        btnSubmit = findViewById(R.id.btn_submit);
        scrollview = findViewById(R.id.scrollview);

        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            edtName.setText(product.getName());
            edtPrice.setText(product.getPrice().toString());
            edtImageURL.setText(product.getImageURL());
            edtMaterial.setText(product.getMaterial());
            edtSize.setText(product.getSize().toString());
            edtDesc.setText(product.getDescription());
            edtCategoryName.setText(product.getCategory().getName());


            _handleStatus();


        }


        _btnBackOnClick();
        _btnSubmitOnClick();
//        _getCategories();
        _handleCategorySelected();
        _handleStatusSelected();

    }


    private void _handleStatus() {

        productStatusMap.put(ProductStatus.FOR_SALE, "Đang bán");
        productStatusMap.put(ProductStatus.STOP_SELLING, "Dừng bán");
        productStatusMap.put(ProductStatus.OUT_OF_STOCK, "Hết hàng");

        List<String> productStatusValues = new ArrayList<>(productStatusMap.values());
        productKeys = new ArrayList<>(productStatusMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productStatusValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatuses.setAdapter(adapter);

        if (product.getStatus() != null) {
            selectedStatus = product.getStatus();

            if (productStatusMap.containsKey(selectedStatus)) {

                String statusDisplayValue = productStatusMap.get(selectedStatus);

                int position = productStatusValues.indexOf(statusDisplayValue);
                if (position != -1) {
                    spinnerStatuses.setSelection(position);
                }
            }
        }
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
                _updateProduct();
            }
        });
    }

    private void _updateProduct() {

        Call<ApiResponse<Product>> call = ApiClient.getInstance().updateProduct(product.getId(), selectedStatus);
        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful()) {
                    _alert("Cập nhật sản phẩm thành công");

                    ProductManagementActivity.getData();

                }
                else {
                    Log.d("EK", selectedStatus + "" );
                }
//                else {
//                    AppUtils.alertError(CreateProductActivity.this, response);
//                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable throwable) {
                Log.d("EK2", selectedStatus + "" );
            }
        });
    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void _getCategories() {
        Call<ApiResponse<List<Category>>> call = ApiClient.getInstance().getCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body().getData();
                    categories = categoryList;
                    if (categoryList != null && !categoryList.isEmpty()) {


                        for (Category category : categoryList) {
                            categoryNames.add(category.getName());

                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProductActivity.this, android.R.layout.simple_spinner_item, categoryNames);
//                        // Specify the layout to use when the list of choices appears.
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        // Apply the adapter to the spinner.
//                        spinnerCategories.setAdapter(adapter);


//                        if(product.getCategory() != null){
//                           int targetId = product.getCategory().getId();
//
//                            int position = IntStream.range(0, categoryList.size())
//                                    .filter(i -> categoryList.get(i).getId() == targetId)
//                                    .findFirst()
//                                    .orElse(-1);
//
//                            if (position != -1) {
//                                spinnerCategories.setSelection(position);
//                                selectedCategoryId = product.getCategory().getId();
//                            } else {
//                                selectedCategoryId = categoryList.get(0).getId();
//                            }
//
//                        }
//                        else {
//                            selectedCategoryId = categoryList.get(0).getId();
//                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {

            }
        });
    }

    private void _handleCategorySelected() {
//        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                selectedCategoryId = categories.get(position).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void _handleStatusSelected() {
        spinnerStatuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedStatus = productKeys.get(position);
                Gson gson = new Gson();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}