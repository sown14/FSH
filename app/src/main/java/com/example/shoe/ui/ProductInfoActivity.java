package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.Product;
import com.example.shoe.models.ProductStatus;
import com.example.shoe.utils.Formats;
import com.google.android.material.button.MaterialButton;

public class ProductInfoActivity extends AppCompatActivity {

    ImageButton btnBack;

    TextView tvTitle;

    Product product;
    ImageView ivProduct;
    TextView tvName, tvPrice, tvDesc,tvInfo;

    MaterialButton btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_info);

        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText("Chi tiết sản phẩm");

        tvInfo = findViewById(R.id.tv_info);
        ivProduct = findViewById(R.id.img_product);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);
        tvDesc = findViewById(R.id.tv_description);

        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        product = (Product) getIntent().getSerializableExtra("product");
        if(product!=null){
            Glide.with(ivProduct).load(product.getImageURL()).into(ivProduct);
            tvName.setText(product.getName());
            tvDesc.setText(product.getDescription() + "\n" + "\n"
            + "Size: " + product.getSize() + "\n"
                    + "Chất liệu: " + product.getMaterial()
            );
            tvPrice.setText(Formats.formatCurrency(product.getPrice()));
            if(product.getStatus().equals(ProductStatus.OUT_OF_STOCK)){
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText("(Sản phẩm đã hết hàng)");
            } else if (product.getStatus().equals(ProductStatus.STOP_SELLING)) {
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText("(Sản phẩm đã dừng kinh doanh)");
            }

        }
        _btnBackOnClick();
        _btnAddToCartOnClick();


    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void _btnAddToCartOnClick () {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getStatus().equals(ProductStatus.FOR_SALE)){
                    MyApplication.addToCart(product);
                    Toast.makeText(ProductInfoActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                } else if (product.getStatus().equals(ProductStatus.OUT_OF_STOCK)) {
                    Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();

                } else if (product.getStatus().equals(ProductStatus.STOP_SELLING)) {
                    Toast.makeText(ProductInfoActivity.this, "Sản phẩm đã dừng kinh doanh", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
}