package com.example.shoe.ui.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.shoe.R;
import com.example.shoe.databinding.ActivityOrderBinding;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Order;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.order.ui.main.PageViewModel;
import com.example.shoe.ui.order.ui.main.PlaceholderFragment;
import com.example.shoe.ui.order.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;

    private EditText edtInput;
    private Button btnOk, btnClear;

    static String dateChoose;

    static PageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        binding.tvTitle.setText("Quản lý đơn hàng");

        _btnBackOnClick();
        _btnFilterOnClick();

        viewModel = new ViewModelProvider(this).get(PageViewModel.class);

        PlaceholderFragment fragment = new PlaceholderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        fetchOrders();

    }

    private void _btnBackOnClick (){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void _btnFilterOnClick() {
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showInputDialog();
            }
        });
    }

    private void _showInputDialog() {

        final android.app.Dialog dialog = new android.app.Dialog(OrderActivity.this);
        dialog.setContentView(R.layout.order_filter_diglog);

         edtInput = dialog.findViewById(R.id.edtInput);
         btnClear = dialog.findViewById(R.id.btnClear);
         btnOk = dialog.findViewById(R.id.btnOk);

         edtInput.setText(dateChoose);




        edtInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _openDatePicker();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = edtInput.getText().toString().trim();
                dateChoose = inputText;
                dialog.dismiss();
                fetchOrders();

            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtInput.setText("");
                dateChoose = null;
                dialog.dismiss();
                fetchOrders();
            }
        });

        dialog.show();
    }

    private void _openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                OrderActivity.this,
                (view, year1, month1, dayOfMonth) -> {

                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = sdf.format(selectedDate.getTime());
                    edtInput.setText(formattedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    public static void fetchOrders() {

        Call<ApiResponse<List<Order>>> call = ApiClient.getInstance().getOrdersHistory(dateChoose);

        call.enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful()) {
                    List<Order> allOrders = response.body().getData();
                    viewModel.setOrders(allOrders);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable throwable) {

            }
        });
    }


}