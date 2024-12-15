package com.example.shoe.ui.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.Order;
import com.example.shoe.models.Role;
import com.example.shoe.ui.orderInfo.OrderInfoActivity;
import com.example.shoe.utils.Formats;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private final OnOrderItemUpdateListener handleUpdate;
    public interface OnOrderItemUpdateListener {
        void onUpdate(int position, Order item);
    }

    public OrderAdapter(List<Order> orders, OnOrderItemUpdateListener newH) {
        this.orders = orders;
        this.handleUpdate = newH;
    }

    public void updateData(List<Order> newOrders) {
        this.orders = newOrders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = orders.get(position);

        holder.name.setText("Đơn hàng: " + Formats.formatCurrency(order.getTotalPrice()));


        String formattedDate = Formats.formatDateFull(order.getCreatedAt());

        if(MyApplication.getUser().getRole().equals(Role.ADMIN)){
            holder.date.setText("Thời gian đặt hàng: " + formattedDate + "\n"
                    + "Người dùng: " + order.getUser().getFullname() + "\n"
                    + "Địa chỉ: " + order.getAddress() + "\n"
            );
        }
        else{
            holder.date.setText("Thời gian đặt hàng: " + formattedDate);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrderInfoActivity.class);

                intent.putExtra("order", order);
                view.getContext().startActivity(intent);
            }
        });
        if(MyApplication.getUser().getRole().equals(Role.ADMIN)){
            holder.editButton.setVisibility(View.VISIBLE);
        }
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpdate.onUpdate(position, order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, orderUser, date;
        CardView cardView;

        ImageButton editButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.orderName);
//            status = itemView.findViewById(R.id.orderStatus);
            date = itemView.findViewById(R.id.orderDate);
            cardView = itemView.findViewById(R.id.layout_item);
            editButton = itemView.findViewById(R.id.editButton);

        }
    }
}
