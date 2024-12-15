package com.example.shoe.ui.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.Api.CreateOrder;
import com.example.shoe.MyApplication;
import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Cart;
import com.example.shoe.models.Order;
import com.example.shoe.models.Voucher;
import com.example.shoe.services.ApiClient;
import com.example.shoe.utils.AppUtils;
import com.example.shoe.utils.Formats;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnCartItemIncreaseListener, CartItemAdapter.OnCartItemDecreaseListener {

    TextView tvTitle, tvTotalPrice, tvEmpty;
    ImageButton btnBack;

    RecyclerView recyclerView;

    MaterialButton btnPayment;

    CartItemAdapter cartItemAdapter;

    List<Cart> cartItems;

    LinearLayout layoutHasData;

    EditText edtVoucher, edtAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        tvTitle = findViewById(R.id.txt_title);
        btnBack = findViewById(R.id.btn_back);
        btnPayment = findViewById(R.id.btn_payment);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        layoutHasData = findViewById(R.id.layoutHasData);
        tvEmpty = findViewById(R.id.tv_empty);
        tvTitle.setText("Giỏ hàng");
        edtVoucher = findViewById(R.id.edt_check_voucher);
        edtAddress= findViewById(R.id.edt_address);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemAdapter = new CartItemAdapter(cartItems, this, this);
        recyclerView.setAdapter(cartItemAdapter);

        _btnBackOnClick();
        _getData();
        _btnPaymentOnClick();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

    }


    //Cần bắt sự kiện OnNewIntent vì ZaloPay App sẽ gọi deeplink về app của Merchant
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    @Override
    public void onDecrease(int position, Cart item) {
//        Log.d("ON_DECREASE_ITEM_CART", position + "");
        MyApplication.decreaseItemCart(item.getId());
        _getData();
    }

    @Override
    public void onIncrease(int position, Cart item) {
//        Log.d("ON_INCREASE_ITEM_CART", position + "");
        MyApplication.increaseItemCart(item.getId());
        _getData();
    }


    private void _withPayment() {
        int selectedId = ((RadioGroup) findViewById(R.id.payment_options_group)).getCheckedRadioButtonId();
        String selectedPayment = selectedId == R.id.rb_cod ? "COD" : "ZaloPay";
        if (selectedPayment.equals("COD")) {
            _paymentWithApi("DEFAULT");
        } else {
            _paymentWithZalo();
        }
    }

    private void _btnPaymentOnClick() {
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voucherCode = edtVoucher.getText().toString();
                if (_calculateTotalPrice() == 0) {
                    Toast.makeText(CartActivity.this, "Vui lòng mua sắm", Toast.LENGTH_SHORT).show();
                }

                else if(edtAddress.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }
                else if(!voucherCode.trim().equalsIgnoreCase("")) {
                    Call<ApiResponse<Voucher>> call = ApiClient.getInstance().checkDiscount(voucherCode.trim(), _calculateTotalPrice());
                    call.enqueue(new Callback<ApiResponse<Voucher>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Voucher>> call, Response<ApiResponse<Voucher>> response) {
                            if(response.isSuccessful()){
                                int amount = response.body().getData().getDiscount();
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                                int total = _calculateTotalPrice() - amount;
                                String message = "Bạn được giảm " + Formats.formatCurrency(amount) + "\n"
                                        + "Tổng số tiền sau giảm là " + Formats.formatCurrency(total) + "\n"
                                        + "Thanh toán?";

                                builder.setTitle("Thông báo")
                                        .setMessage(message)

                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                _withPayment();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss(); // Đóng dialog
                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else {
                                AppUtils.alertError(CartActivity.this, response);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Voucher>> call, Throwable throwable) {

                        }
                    });

                }
                else {
                    _withPayment();
                }


            }
        });
    }


    private void _paymentWithZalo() {
        CreateOrder orderApi = new CreateOrder();
        try {


            JSONObject data = orderApi.createOrder(_calculateTotalPrice() + "");
            String code = data.getString("return_code");
//            Log.d("ZALO_PAY_CODE","with code " + data);
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        _paymentWithApi("ZALOPAY");
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _paymentWithApi(String type) {

        Gson gson = new Gson();
        String json = gson.toJson(cartItems);

        Call<ApiResponse<Order>> call = ApiClient.getInstance().createOrder(json, type, edtVoucher.getText().toString().trim(), edtAddress.getText().toString().trim());
        call.enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    MyApplication.removeAllCart();
                    _getData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable throwable) {

            }
        });
    }

    private void _btnBackOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void _getData() {
        cartItems = MyApplication.getCartItems();
        cartItemAdapter.setData(cartItems);
        cartItemAdapter.notifyDataSetChanged();

        if (_calculateTotalPrice() == 0) {
            // hide view
            layoutHasData.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            // show view
            layoutHasData.setVisibility(View.VISIBLE);
            tvTotalPrice.setText(Formats.formatCurrency(_calculateTotalPrice()));
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private int _calculateTotalPrice() {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0;
        } else {
            int totalPrice = 0;
            for (Cart cart : cartItems) {
                totalPrice += cart.getQuantity() * cart.getPrice();
            }
            return totalPrice;
        }

    }


}