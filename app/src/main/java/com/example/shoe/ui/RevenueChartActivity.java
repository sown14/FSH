package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.User;
import com.example.shoe.models.YearlyRevenueResponse;
import com.example.shoe.services.ApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevenueChartActivity extends AppCompatActivity {

    BarChart barChart;

    ImageButton btnBack;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_chart);

        barChart = findViewById(R.id.barChart);

        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText("Biểu đồ doanh thu");

        _getData();
        _btnBackOnClick();
    }
    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void _getData() {
        Call<ApiResponse<YearlyRevenueResponse>> call = ApiClient.getInstance().getYearlyRevenue();
        call.enqueue(new Callback<ApiResponse<YearlyRevenueResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<YearlyRevenueResponse>> call, Response<ApiResponse<YearlyRevenueResponse>> response) {
                if (response.isSuccessful() && response.body().getData() != null) {
                    _setupBarChart(response.body().getData().getRevenue());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<YearlyRevenueResponse>> call, Throwable throwable) {

            }
        });
    }

    private void _setupBarChart(List<Integer> revenue) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < revenue.size(); i++) {
            entries.add(new BarEntry(i, revenue.get(i)));
        }


        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.setFitBars(true);


        String[] months = {"T.1", "T.2", "T.3", "T.4", "T.5", "T.6", "T.7", "T.8", "T.9", "T.10", "T.11", "T.12"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(12); // Hiển thị đủ 12 tháng
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(12f);


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setTextSize(12f);
        yAxisLeft.setAxisMinimum(0f);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);


        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.animateY(500);
        barChart.invalidate();

    }
}