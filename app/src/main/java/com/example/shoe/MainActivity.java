package com.example.shoe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.shoe.models.Role;
//import com.example.shoe.models.User;
//import com.example.shoe.ui.AdminPanelActivity;
//import com.example.shoe.ui.LoginActivity;
//import com.example.shoe.ui.MyTabsActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Looper.getMainLooper();
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
////                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
////                startActivity(intent);
////                finish();
//
//
//                User user = MyApplication.getUser();
//                if (user == null) {
//                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent1);
//                    finish();
//                } else if (user.getRole().equals(Role.ADMIN)) {
//                    Intent intent2 = new Intent(MainActivity.this, AdminPanelActivity.class);
//                    startActivity(intent2);
//                    finish();
//                } else {
//                    Intent intent3 = new Intent(MainActivity.this, MyTabsActivity.class);
//                    startActivity(intent3);
//                    finish();
//                }
//            }
//        }, 100);

    }

}