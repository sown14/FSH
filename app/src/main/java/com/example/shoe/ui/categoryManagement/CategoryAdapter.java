package com.example.shoe.ui.categoryManagement;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.Category;
import com.example.shoe.ui.EditCategoryActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private List<Category> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        Button btnEdit;

        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            txtTitle = view.findViewById(R.id.txt_title);
            btnEdit = view.findViewById(R.id.btn_edit);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public CategoryAdapter(List<Category> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_category_management, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Category category = localDataSet.get(position);
        viewHolder.txtTitle.setText(category.getName());
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Category category = localDataSet.get(viewHolder.getAdapterPosition());
               int categoryId = category.getId();
                Intent intent = new Intent(view.getContext(), EditCategoryActivity.class);
                intent.putExtra("category", category);
//                intent.putExtra("categoryId", categoryId);
//                intent.putExtra("name", category.getName());
//                intent.putExtra("description", category.getDescription());
                view.getContext().startActivity(intent);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }


    public void setCategories(List<Category> newdata) {
        this.localDataSet = newdata;
    }

}
