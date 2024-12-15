package com.example.shoe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.databinding.FragmentHomeBinding;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.models.Product;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.SearchActivity;
import com.example.shoe.ui.cart.CartActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    List<Product> products;

    List<Category> categories;

    ProductHomeAdapter productHomeAdapter;
    CategoryHomeAdapter categoryHomeAdapter;

    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productHomeAdapter = new ProductHomeAdapter(products);

        binding.rvProducts.setAdapter(productHomeAdapter);

        recyclerView = binding.rvCategories;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        categoryHomeAdapter = new CategoryHomeAdapter(categories);
        recyclerView.setAdapter(categoryHomeAdapter);
        _getProducts();
        _navigateToCart();
        _navigateToSearch();
        _getCategories();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void _navigateToSearch (){
        binding.homeFragmentSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void _navigateToCart () {
        binding.homeFragmentButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void _getCategories (){
        Call<ApiResponse<List<Category>>> call = ApiClient.getInstance().getCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if(response.isSuccessful()){
                    List<Category> categoriesRes = response.body().getData();
                    if(categoriesRes !=null & !categoriesRes.isEmpty()){
                        categories = categoriesRes;
                        categoryHomeAdapter.setData(categories);
                        categoryHomeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {

            }
        });
    }
    private void _getProducts () {
        Call<ApiResponse<List<Product>>> call = ApiClient.getInstance().getProductsNewest();
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if(response.isSuccessful()){
                    List<Product> productRes = response.body().getData();
                    if(productRes !=null & !productRes.isEmpty()){

                        products = productRes;
                        productHomeAdapter.setData(products);
                        productHomeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable throwable) {

            }
        });
    }
}