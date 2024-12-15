package com.example.shoe.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoe.R;
import com.example.shoe.models.Product;
import com.example.shoe.ui.ProductInfoActivity;
import com.example.shoe.utils.Formats;

import java.util.List;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ViewHolder> {
    private List<Product> localDataSet;

    public static  class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imgProduct;
    TextView tvName, tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct  = itemView.findViewById(R.id.img_product);
            tvName =itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);


        }
    }


    public ProductHomeAdapter(List<Product> dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_home, parent, false);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductInfoActivity.class);
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
