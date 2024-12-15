package com.example.shoe.ui.order.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shoe.R;
import com.example.shoe.databinding.FragmentOrderBinding;
import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Order;
import com.example.shoe.models.OrderStatus;
import com.example.shoe.services.ApiClient;
import com.example.shoe.ui.order.OrderActivity;
import com.example.shoe.ui.order.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OrderAdapter.OnOrderItemUpdateListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private FragmentOrderBinding binding;

    private OrderAdapter orderAdapter;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(requireActivity()).get(PageViewModel.class);


        pageViewModel.getOrders().observe(this, data -> {
            if (data != null) {
                orderAdapter.updateData(_filterOrdersByStatus(data));  // Update adapter with filtered data
                orderAdapter.notifyDataSetChanged();

            }
            else {
                orderAdapter.updateData(new ArrayList<>());
                orderAdapter.notifyDataSetChanged();

            }



        });
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        _setupRecyclerView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    private List<Order> _filterOrdersByStatus(List<Order> allOrders) {
        int index = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;
        OrderStatus statusToFilter = _getStatusForTab(index);

        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus() == statusToFilter) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    private OrderStatus _getStatusForTab(int index) {
        switch (index) {
            case 1:
                return OrderStatus.WAITING;
            case 2:
                return OrderStatus.PENDING;
            case 3:
                return OrderStatus.DELIVERING;
            case 4:
                return OrderStatus.COMPLETED;
            case 5:
                return OrderStatus.CANCELED;
            default:
                return OrderStatus.WAITING;
        }
    }

    private void _setupRecyclerView() {
        orderAdapter = new OrderAdapter(new ArrayList<>(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(orderAdapter);
    }


    @Override
    public void onUpdate(int position, Order item) {
        final android.app.Dialog dialog = new android.app.Dialog(getContext());
        dialog.setContentView(R.layout.update_order_status_dialog);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        RadioGroup radioGroup = dialog.findViewById(R.id.rgOptions);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        OrderStatus status;
        status = item.getStatus();

        switch (status) {
            case PENDING:
                radioGroup.check(R.id.rb_pending);
                break;
            case WAITING:
                radioGroup.check(R.id.rb_waiting);
                break;
            case DELIVERING:
                radioGroup.check(R.id.rb_delivering);
                break;
            case COMPLETED:
                radioGroup.check(R.id.rb_completed);
                break;
            case CANCELED:
                radioGroup.check(R.id.rb_canceled);
                break;
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newCheckId = radioGroup.getCheckedRadioButtonId();
                OrderStatus newStatus;
                if (newCheckId == R.id.rb_pending) {
                    newStatus = OrderStatus.PENDING;
                } else if (newCheckId == R.id.rb_waiting) {
                    newStatus = OrderStatus.WAITING;
                } else if (newCheckId == R.id.rb_delivering) {
                    newStatus = OrderStatus.DELIVERING;
                } else if (newCheckId == R.id.rb_completed) {
                    newStatus = OrderStatus.COMPLETED;
                } else if (newCheckId == R.id.rb_canceled) {
                    newStatus = OrderStatus.CANCELED;
                } else {
                    newStatus = OrderStatus.PENDING;
                }

                Call<ApiResponse<Order>> call = ApiClient.getInstance().updateOrder(item.getId(), newStatus );
               call.enqueue(new Callback<ApiResponse<Order>>() {
                   @Override
                   public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                       dialog.dismiss();
                       OrderActivity.fetchOrders();

                   }

                   @Override
                   public void onFailure(Call<ApiResponse<Order>> call, Throwable throwable) {

                   }
               });
            }
        });
        dialog.show();


    }
}