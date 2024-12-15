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
import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.example.shoe.utils.AppUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    ImageButton btnBack;
    TextView tvTitle;
    AppCompatButton btnSubmit;

    CircleImageView imgAvatar;

    EditText edtFullname, edtUsername, edtEmail, edtPhone, edtDateOfBirth;

    View btnChangeAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText("Cập nhật thông tin");

        imgAvatar = findViewById(R.id.img_avatar);
        edtFullname = findViewById(R.id.edt_fullname);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtDateOfBirth = findViewById(R.id.edt_date_of_birth);

        btnSubmit = findViewById(R.id.btn_submit);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);

        _btnBackOnClick();
        _btnSubmit();
        _btnChangeAvatar();
        _getUserInfo();
        _setLocalUserInfo();


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
//        try {
//            // Lấy URI của ảnh đã chọn
//            Uri selectedImage = data.getData();
//
//            // Tải ảnh vào ImageView mà không tự động xoay
//            Picasso.get()
//                    .load(selectedImage)
//                    .into(imageView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Glide.with(this).load(bitmap).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _btnSubmit(){

      btnSubmit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String imageBase64 = AppUtils.convertBitmapToBase64(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap());

              String fullname = edtFullname.getText().toString();
              String username = edtUsername.getText().toString();
              String email = edtEmail.getText().toString();
              String phone = edtPhone.getText().toString();
              String dateOfBirth = edtDateOfBirth.getText().toString();

                      if(fullname.trim().equalsIgnoreCase("")){
                          _alert("Vui lòng nhập họ tên ");
                      } else if (username.trim().equalsIgnoreCase("")) {
                          _alert("Vui lòng nhập tên người dùng");
                      } else if (phone.trim().equalsIgnoreCase("")) {
                          _alert("Vui lòng nhập số điện thoại");
                      } else if (dateOfBirth.trim().equalsIgnoreCase("")) {
                          _alert("Vui lòng nhập ngày sinh");
                      }
                      else {
                          _update(fullname, username, email, phone, dateOfBirth, imageBase64);
                      }

          }
      });
    }

    private void _alert(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private void _setLocalUserInfo(){
        User user = MyApplication.getUser();
        if(user !=null){
            _setUser(user);
        }
    }
    private  void _getUserInfo(){
        Call<ApiResponse<User>> call = ApiClient.getInstance().getUserInfo();
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if(response.isSuccessful()){

                    User user = response.body().getData();
                    _setUser(user);
                    MyApplication.updateUser(user);

                }
                else {
                    AppUtils.alertError(EditProfileActivity.this, response);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {

            }
        });
    }

    private void _setUser(User user){
        edtFullname.setText(user.getFullname());
        edtUsername.setText(user.getUsername());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhoneNumber());
        edtDateOfBirth.setText(user.getDateOfBirth());
        Glide.with(imgAvatar).load(user.getPicture()).into(imgAvatar);
    }
    private void _update(String fullname, String username, String email, String phone, String dateOfBirth, String avatar){
        Call<ApiResponse<User>> call = ApiClient.getInstance().updateProfile(fullname, username, dateOfBirth, phone, avatar);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if(response.isSuccessful()){
                    User user = response.body().getData();
                    _alert("Cập nhật thành công");
                    MyApplication.updateUser(user);
                }
                else {
//                    _alert("Cập nhật thất bại");
                    AppUtils.alertError(EditProfileActivity.this, response);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {

            }
        });
    }
    private void _btnBackOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
}