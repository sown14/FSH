package com.example.shoe.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.ItemActionProfile;

import java.util.List;

public class ItemActionProfileAdapter extends RecyclerView.Adapter<ItemActionProfileAdapter.ViewHolder> {

    private final List<ItemActionProfile> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.iv_icon);
            label = itemView.findViewById(R.id.tv_label);
        }
    }
    public ItemActionProfileAdapter(List<ItemActionProfile> itemList) {
        this.localDataSet = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_action_profile_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemActionProfile item = localDataSet.get(position);

        holder.icon.setImageResource(item.getIcon());
        holder.label.setText(item.getLabel());
        holder.itemView.setOnClickListener(item.getOnClickListener());
    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }


}
