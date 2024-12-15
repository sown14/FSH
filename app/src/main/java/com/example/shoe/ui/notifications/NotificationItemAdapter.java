package com.example.shoe.ui.notifications;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.Notification;
import com.example.shoe.models.NotificationType;
import com.example.shoe.ui.orderInfo.OrderInfoActivity;
import com.example.shoe.utils.Formats;

import java.util.List;

public class NotificationItemAdapter extends RecyclerView.Adapter<NotificationItemAdapter.ViewHolder> {

    private List<Notification> localDataSet;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime, tvDesc;
        LinearLayout layoutCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_description);
            tvTime = itemView.findViewById(R.id.tv_time);
            layoutCard = itemView.findViewById(R.id.layout_card);
        }
    }
    public NotificationItemAdapter(List<Notification> notifications){
        this.localDataSet=notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification notification = localDataSet.get(position);

        holder.tvDesc.setText(notification.getDescription());
        holder.tvTitle.setText(notification.getTitle());

        String formattedDate = Formats.formatDateFull(notification.getCreatedAt());
        holder.tvTime.setText(formattedDate);
        holder.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.getType().equals(NotificationType.ORDER)){
                    Intent intent = new Intent(view.getContext(), OrderInfoActivity.class);

                    intent.putExtra("order", notification.getOrder());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }

    public void setData(List<Notification> newData){
        this.localDataSet = newData;
    }
}
