package com.example.shoe.ui.cart;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoe.R;
import com.example.shoe.models.Cart;
import com.example.shoe.utils.Formats;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

        private List<Cart> localDataSet;
    private final OnCartItemIncreaseListener increase;
    private final OnCartItemDecreaseListener decrease;


    public interface OnCartItemIncreaseListener {
        void onIncrease(int position, Cart item);
    }

    public interface OnCartItemDecreaseListener {
        void onDecrease(int position, Cart item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvName, tvPrice, tvQuantity;
        AppCompatImageButton btnIncrease, btnDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.orderContentAvatar);
            tvName = itemView.findViewById(R.id.orderContentName);
            tvPrice = itemView.findViewById(R.id.orderContentPrice);
            tvQuantity = itemView.findViewById(R.id.orderContentQuantity);
            btnIncrease = itemView.findViewById(R.id.orderContentButtonAdd);
            btnDecrease = itemView.findViewById(R.id.orderContentButtonMinus);
        }
    }

    public CartItemAdapter(List<Cart> dataSet, OnCartItemIncreaseListener increase, OnCartItemDecreaseListener decrease) {
        localDataSet = dataSet;
        this.increase = increase;
        this.decrease = decrease;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_content_recycler_view_adapter_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Cart cart = localDataSet.get(position);
        Glide.with(holder.itemView)
                .load(cart.getImageURL())
                .into(holder.imgAvatar);
        holder.tvName.setText(cart.getName());
        holder.tvPrice.setText(Formats.formatCurrency(cart.getPrice()));
        holder.tvQuantity.setText(cart.getQuantity() + "");
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increase.onIncrease(position, cart);
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrease.onDecrease(position, cart);
            }
        });

    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }

    public void setData(List<Cart> newData) {
        this.localDataSet = newData;
    }
}
