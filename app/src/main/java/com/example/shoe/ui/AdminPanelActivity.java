package com.example.shoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.ui.categoryManagement.CategoryManagementActivity;
import com.example.shoe.ui.order.OrderActivity;
import com.example.shoe.ui.productManagement.ProductManagementActivity;
import com.example.shoe.ui.userManagement.UserManagementActivity;
import com.example.shoe.ui.voucherManagement.VoucherManagementActivity;

public class AdminPanelActivity extends AppCompatActivity {


    Button btnLogout;

    CardView cardProduct, cardCategory, cardOrder, cardStatistics, cardUser, cardVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        btnLogout = findViewById(R.id.btn_logout);
        cardProduct = findViewById(R.id.card_product);
        cardCategory = findViewById(R.id.card_category);
        cardOrder= findViewById(R.id.card_order);
        cardStatistics= findViewById(R.id.card_statistics);
        cardVoucher = findViewById(R.id.card_voucher);
        cardUser = findViewById(R.id.card_user);


        _btnClickLogout();
        _cardClick();

    }

    private void _btnClickLogout () {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _logout();
            }
        });
    }

    private void _cardClick(){

        cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(UserManagementActivity.class);
            }
        });
        cardVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(VoucherManagementActivity.class);
            }
        });
        cardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(ProductManagementActivity.class);
            }
        });

        cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(CategoryManagementActivity.class);
            }
        });

        cardOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(OrderActivity.class);
            }
        });

        cardStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _navigateWithCard(RevenueActivity.class);
            }
        });
    }

    private void _navigateWithCard ( Class<?> cls){
        Intent intent = new Intent(AdminPanelActivity.this,cls);
        startActivity(intent);
    }

    private void _logout (){
        MyApplication.removeUser();
        Intent intent = new Intent(AdminPanelActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}