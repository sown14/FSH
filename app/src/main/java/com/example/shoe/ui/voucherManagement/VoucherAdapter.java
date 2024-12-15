package com.example.shoe.ui.voucherManagement;


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
import com.example.shoe.models.Voucher;
import com.example.shoe.ui.EditCategoryActivity;
import com.example.shoe.utils.Formats;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {


    private List<Voucher> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvDescription, tvLimit, tvTimesUsed, tvMin, tvMax,tvMinOrderValue;


        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvCode = view.findViewById(R.id.tv_code);
            tvDescription = view.findViewById(R.id.tv_description);
            tvLimit = view.findViewById(R.id.tv_limit);
            tvTimesUsed = view.findViewById(R.id.tv_times_used);
            tvMin = view.findViewById(R.id.tv_min);
            tvMax = view.findViewById(R.id.tv_max);
            tvMinOrderValue = view.findViewById(R.id.tv_min_order_value);



        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public VoucherAdapter(List<Voucher> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_voucher, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Voucher voucher = localDataSet.get(position);

        viewHolder.tvCode.setText("Mã: " + voucher.getCode());
        viewHolder.tvDescription.setText("Mô tả: " + voucher.getDescription());
        viewHolder.tvLimit.setText("Số lượt sử dụng tối đa: " + voucher.getLimit());
        viewHolder.tvTimesUsed.setText("Đã dùng: " + voucher.getTimesUsed());
        viewHolder.tvMin.setText("Giá trị giảm tối thiểu: " + Formats.formatCurrency(voucher.getMin()));
        viewHolder.tvMax.setText("Giá trị giảm tối đa: " + Formats.formatCurrency(voucher.getMax()));
        viewHolder.tvMinOrderValue.setText("Ngưỡng kích hoạt voucher: " + Formats.formatCurrency(voucher.getMinOrderValue()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet != null ? localDataSet.size() : 0;
    }


    public void setData(List<Voucher> newdata) {
        this.localDataSet = newdata;
    }

}
