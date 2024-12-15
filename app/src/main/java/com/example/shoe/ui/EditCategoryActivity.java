package com.example.shoe.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.categoryManagement.CategoryManagementActivity;
import com.example.shoe.utils.AppUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;

    AppCompatButton btnSubmit;

    EditText edtName, edtDesc;

    ImageButton btnBack;

    TextView txtTitle;


    View btnChangeAvatar;
    CircleImageView imgAvatar;

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_edit_category);

        category = (Category) getIntent().getSerializableExtra("category");




        btnSubmit = findViewById(R.id.btn_submit);
        btnBack = findViewById(R.id.btn_back);
        edtName = findViewById(R.id.edt_name);
        edtDesc = findViewById(R.id.edt_desc);

        edtName.setText(category.getName());
        edtDesc.setText(category.getDescription());

        btnChangeAvatar = findViewById(R.id.btn_change_avatar);
        imgAvatar = findViewById(R.id.img_avatar);

        Glide.with(imgAvatar).load(category.getImage()).into(imgAvatar);

        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText("Chỉnh sửa danh mục");

        _btnSubmitOnClick();
        _btnBackOnClick();
        _btnChangeAvatar();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                _handleImageResult(data, imgAvatar);
            }
        }
    }

    private void _handleImageResult(Intent data, CircleImageView imageView) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Glide.with(this).load(bitmap).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void _openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, "No app available to handle image pick", Toast.LENGTH_SHORT).show();
        }

    }
    private void _btnChangeAvatar(){
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _openGallery();
            }
        });
    }

    private void _btnSubmitOnClick () {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageBase64 = AppUtils.convertBitmapToBase64(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap());
                String name = edtName.getText().toString();
                String desc = edtDesc.getText().toString();
                if (name.trim().equalsIgnoreCase("")) {
                    _alert("Vui lòng nhập tên");
                } else {
                    _editCategory(name.trim(), desc.trim(), imageBase64);
                }
            }
        });
    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void _editCategory(String name, String desc, String base64) {
        Call<ApiResponse<Category>> call = ApiClient.getInstance().updateCategory(category.getId(),name, desc, base64);
        call.enqueue(new Callback<ApiResponse<Category>>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if(response.isSuccessful()){
                    _alert("Chỉnh sửa danh mục thành công");
                    CategoryManagementActivity.getData();
                }
                else {
                    AppUtils.alertError(EditCategoryActivity.this, response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable throwable) {

            }
        });
    }

   private  void _btnBackOnClick () {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}