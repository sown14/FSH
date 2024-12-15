package com.example.shoe.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoe.MainActivity;
import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.RevenueResponse;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.example.shoe.utils.Formats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevenueActivity extends AppCompatActivity {

    private EditText edtStartDate, edtEndDate;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    AppCompatButton btnSubmit;

    ImageButton btnBack;
    TextView tvTitle, tvChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revenue);

        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.txt_title);
        tvTitle.setText("Doanh thu");

        tvChart = findViewById(R.id.tv_chart);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtEndDate = findViewById(R.id.edt_end_date);
        btnSubmit = findViewById(R.id.btn_submit);
        _edtStartDateOnClick();
        _edtEndDateOnClick();
        _btnSubmitOnClick();
        _btnBackOnClick();
        _navigateToChart();
    }


    private void _navigateToChart(){
        tvChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(RevenueActivity.this, RevenueChartActivity.class);
                startActivity(intent3);
            }
        });
    }

    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void _btnSubmitOnClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtStartDate.getText().toString().isEmpty() || edtStartDate.getText().toString().isEmpty()){
                    Toast.makeText(RevenueActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                }
                else {
                    _getData();
                }
            }
        });
    }

    private void _showRevenueAlert(String amount){
        String message = "Tổng doanh thu: " + amount;

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin doanh thu")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _getData (){
        Call<ApiResponse<RevenueResponse>> call = ApiClient.getInstance().getTotalRevenue(edtStartDate.getText().toString(), edtEndDate.getText().toString());
        call.enqueue(new Callback<ApiResponse<RevenueResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<RevenueResponse>> call, Response<ApiResponse<RevenueResponse>> response) {
                if(response.isSuccessful()){
                    RevenueResponse revenueResponse = response.body().getData();
                    if(revenueResponse.getTotalRevenue() != null){

                        _showRevenueAlert(revenueResponse.getTotalRevenue());
                    }
                    else {
                        _showRevenueAlert("0");
                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RevenueResponse>> call, Throwable throwable) {

            }
        });
    }
    private void _edtStartDateOnClick (){

        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(true);
            }
        });
    }

    private  void _edtEndDateOnClick () {
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(false);

//                _showDatePickerDialog(startDateCalendar, (date)->{
//                    Date selectedEndDate = _parseDate(date);
//                    Date selectedStartDate = startDateCalendar.getTime();
//
//                    if (selectedEndDate.before(selectedStartDate)) {
//                        Toast.makeText(RevenueActivity.this, "Ngày kết thúc phải lớn hơn ngày bắt đầu!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        edtEndDate.setText(date);
//                        endDateCalendar.setTime(selectedEndDate);
//                    }
//                });
            }
        });
    }

    private void openDatePicker(boolean isStartDate) {
        // Chọn Calendar dựa vào ngày bắt đầu hoặc ngày kết thúc
        Calendar calendar = isStartDate ? startDateCalendar : endDateCalendar;

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RevenueActivity.this,
                (view, year, month, dayOfMonth) -> {
                    // Thiết lập ngày đã chọn cho Calendar
                    calendar.set(year, month, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String selectedDate = sdf.format(calendar.getTime());


                    if (isStartDate) {
                        edtStartDate.setText(selectedDate);

                        if (endDateCalendar.before(startDateCalendar)) {
                            edtEndDate.setText("");
                        }
                    } else {

                        if (startDateCalendar != null && calendar.before(startDateCalendar)) {
                            Toast.makeText(RevenueActivity.this, "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu", Toast.LENGTH_SHORT).show();
                            edtEndDate.setText("");
                        } else if (calendar.after(Calendar.getInstance())) {
                            Toast.makeText(RevenueActivity.this, "Ngày kết thúc không được lớn hơn ngày hiện tại", Toast.LENGTH_SHORT).show();
                            edtEndDate.setText("");
                        } else {
                            edtEndDate.setText(selectedDate);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );


        if (isStartDate) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }


        if (!isStartDate) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            if (startDateCalendar != null) {
                datePickerDialog.getDatePicker().setMinDate(startDateCalendar.getTimeInMillis());
            }
        }

        // Hiển thị dialog
        datePickerDialog.show();
    }



    private Date _parseDate(String dateString) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
        } catch (Exception e) {
            return new Date();
        }
    }
    private void _showDatePickerDialog(Calendar calendar, DatePickerListener listener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                    listener.onDateSelected(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    interface DatePickerListener {
        void onDateSelected(String date);
    }
}