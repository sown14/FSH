package com.example.shoe.ui.userManagement;

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
import com.example.shoe.models.User;
import com.example.shoe.ui.EditProductActivity;
import com.example.shoe.ui.EditUserActivity;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    private List<User> localDataSet;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView tvName, tvEmail, tvUsername;

        ImageButton btnEdit;

        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            imgUser = view.findViewById(R.id.img_avatar);
            tvName = view.findViewById(R.id.tv_name);
            tvEmail = view.findViewById(R.id.tv_email);
            tvUsername = view.findViewById(R.id.tv_username);
            btnEdit = view.findViewById(R.id.btn_edit);


        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public UserAdapter(List<User> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_user, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        User user = localDataSet.get(position);

        Glide.with(viewHolder.itemView)
                .load(user.getPicture())
                .into(viewHolder.imgUser);

        viewHolder.tvName.setText(user.getFullname());
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvEmail.setText(user.getEmail());

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditUserActivity.class);

                intent.putExtra("user", user);
                view.getContext().startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }


    public void setData(List<User> newdata) {
        this.localDataSet = newdata;
    }

}
