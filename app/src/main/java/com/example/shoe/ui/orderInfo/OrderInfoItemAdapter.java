package com.example.shoe.ui.orderInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoe.R;
import com.example.shoe.models.OrderItem;
import com.example.shoe.models.Product;
import com.example.shoe.utils.Formats;

import java.util.List;

public class OrderInfoItemAdapter extends RecyclerView.Adapter<OrderInfoItemAdapter.ViewHolder> {

    private List<OrderItem> localDataSet;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice;
        ImageView imgAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

    public OrderInfoItemAdapter(List<OrderItem> orderItems){
        this.localDataSet = orderItems;
    }

    public void setData(List<OrderItem> newData){
        this.localDataSet = newData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderItem orderItem = localDataSet.get(position);
        Product product = orderItem.getProduct();

        Glide.with(holder.itemView)
                .load(product.getImageURL())
                .into(holder.imgAvatar);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(Formats.formatCurrency(product.getPrice()));
        holder.tvQuantity.setText("x"+Integer.toString(orderItem.getQuantity()));


    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }
}
