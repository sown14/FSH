package com.example.shoe.ui.orderInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.Order;
import com.example.shoe.models.OrderItem;
import com.example.shoe.utils.Formats;

import java.util.List;

public class OrderInfoActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageButton btnBack;

    TextView tvCode, tvCreateAt, tvAmount;

    Order order;

    List<OrderItem> orderItems;

    RecyclerView recyclerView;

    OrderInfoItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        // titlebar
        tvTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("Chi tiết đơn hàng");

        tvCode = findViewById(R.id.tv_code);
        tvCreateAt = findViewById(R.id.tv_create_at);
        tvAmount = findViewById(R.id.tv_amount);

        recyclerView = findViewById(R.id.recycler_view);

        _setUpOrderInfo();
        _setUpRecyclerView();
        _btnBackOnClick();



    }

    private void _setUpRecyclerView (){

        itemAdapter = new OrderInfoItemAdapter(order.getOrderItemList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
    }
    private void _setUpOrderInfo () {
        order = (Order) getIntent().getSerializableExtra("order");
        if(order != null){
            tvCode.setText(Integer.toString(order.getId()));
            String formattedDate = Formats.formatDateFull(order.getCreatedAt());
            tvCreateAt.setText(formattedDate);
            tvAmount.setText(Formats.formatCurrency(order.getTotalPrice()) + "");

        }
    }

    private void _btnBackOnClick () {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}