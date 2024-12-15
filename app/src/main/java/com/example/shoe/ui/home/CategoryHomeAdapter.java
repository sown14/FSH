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
import com.example.shoe.models.Category;
import com.example.shoe.ui.ProductsByCategoryActivity;

import java.util.List;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder> {
    private List<Category> localDataSet;

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img  = itemView.findViewById(R.id.img_avatar);
            tvName =itemView.findViewById(R.id.tv_name);
        }
    }


    public CategoryHomeAdapter(List<Category> dataSet){
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category category = localDataSet.get(position);
        Glide.with(holder.itemView)
                .load(category.getImage())
                .into(holder.img);

        holder.tvName.setText(category.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductsByCategoryActivity.class);
                intent.putExtra("category", category);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }

    public void setData(List<Category> newData){
        this.localDataSet = newData;
    }
}
