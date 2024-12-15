package com.example.shoe.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.R;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Product;
import com.example.shoe.models.Sort;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.home.ProductHomeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ImageButton btnBack, btnFilter;

    SearchView searchView;

    RecyclerView recyclerView;


    ProductHomeAdapter adapter;

    Sort sort;

    private Button btnOk, btnClear;

    String queryS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false); // Mở rộng SearchView
        searchView.onActionViewExpanded(); // Tự động hiển thị chế độ nhập

        recyclerView = findViewById(R.id.recycler_view);
        btnBack = findViewById(R.id.searchActivityButtonGoBack);
        btnFilter = findViewById(R.id.searchActivityButtonFilter);

        sort = Sort.ASC;


        _setUpRecyclerView();
        _searchListener();
        _btnFilterOnClick();
        _btnBackOnClick();

    }


    private  void _btnFilterOnClick(){
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _showDialog();
            }
        });
    }
    private void _showDialog (){
        final android.app.Dialog dialog = new android.app.Dialog(SearchActivity.this);
        dialog.setContentView(R.layout.search_filter_dialog);
//        btnClear = dialog.findViewById(R.id.btnClear);
        btnOk = dialog.findViewById(R.id.btnOk);
        RadioGroup radioGroup = dialog.findViewById(R.id.rgSortOptions);
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (sort == Sort.ASC) {
            radioGroup.check(R.id.rb_asc);
        } else if (sort == Sort.DESC) {
            radioGroup.check(R.id.rb_desc);
        } else if (sort == Sort.PRICE_ASC) {
            radioGroup.check(R.id.rb_price_asc);
        } else if (sort == Sort.PRICE_DESC) {
            radioGroup.check(R.id.rb_price_desc);
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int newCheckId = radioGroup.getCheckedRadioButtonId();
                if (newCheckId == R.id.rb_asc) {
                    sort = Sort.ASC;
                } else if (newCheckId == R.id.rb_desc) {
                    sort = Sort.DESC;
                } else if (newCheckId == R.id.rb_price_desc) {
                    sort = Sort.PRICE_DESC;

                } else if (newCheckId == R.id.rb_price_asc) {
                    sort = Sort.PRICE_ASC;
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    private void _btnBackOnClick (){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private  void _searchListener (){
        // Thiết lập listener cho SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!query.trim().equalsIgnoreCase("")){
                    queryS = query;
                    _getData(query.trim());
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void _setUpRecyclerView (){
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductHomeAdapter(null);
        recyclerView.setAdapter(adapter);
    }
    private void _getData (String search) {
        Call<ApiResponse<List<Product>>> call = ApiClient.getInstance().getProductsWithUser(search, sort);
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if(response.isSuccessful()){
                    List<Product> productRes = response.body().getData();
                    if(productRes !=null & !productRes.isEmpty()){
                        adapter.setData(productRes);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        adapter.setData(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });
    }
}