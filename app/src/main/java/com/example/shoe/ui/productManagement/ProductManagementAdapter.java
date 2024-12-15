package com.example.shoe.ui.productManagement;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoe.R;
import com.example.shoe.models.Product;
import com.example.shoe.ui.EditProductActivity;
import com.example.shoe.utils.Formats;

import java.util.List;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ViewHolder> {

    private List<Product> localDataSet;


    public static  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvName, tvPrice, tvTime;

        ImageButton btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnEdit = itemView.findViewById(R.id.btn_edit);

        }
    }

    public ProductManagementAdapter(List<Product> dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_management, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = localDataSet.get(position);
        Glide.with(holder.itemView)
                .load(product.getImageURL())
                .into(holder.imgProduct);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(Formats.formatCurrency(product.getPrice()));

        String isoDate = product.getCreatedAt();
        String formattedDate = Formats.formatIsoDate(isoDate);
        holder.tvTime.setText(formattedDate);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditProductActivity.class);

                intent.putExtra("product", product);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }

    public void setData(List<Product> newData){
        this.localDataSet = newData;
    }

}
