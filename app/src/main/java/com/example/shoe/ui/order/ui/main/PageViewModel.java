package com.example.shoe.ui.order.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shoe.models.Order;

import java.util.List;

public class PageViewModel extends ViewModel {



    private MutableLiveData<List<Order>> localData = new MutableLiveData<>();

    public LiveData<List<Order>> getOrders() {
        return localData;
    }

    public void setOrders(List<Order> newData) {
        localData.setValue(newData);
    }


}