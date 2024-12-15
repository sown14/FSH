package com.example.shoe.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoe.databinding.FragmentNotificationsBinding;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Notification;
import com.example.shoe.services.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    RecyclerView recyclerView;

    NotificationItemAdapter notificationItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        _setupRecyclerView();
        _getData();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void _setupRecyclerView() {
        notificationItemAdapter = new NotificationItemAdapter(null);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(notificationItemAdapter);
    }

    private void _getData(){
        Call<ApiResponse<List<Notification>>> call = ApiClient.getInstance().getNotifications();
        call.enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if(response.isSuccessful()){
                    List<Notification> notifications = response.body().getData();
                    notificationItemAdapter.setData(notifications);
                    notificationItemAdapter.notifyDataSetChanged();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable throwable) {

            }
        });
    }


}