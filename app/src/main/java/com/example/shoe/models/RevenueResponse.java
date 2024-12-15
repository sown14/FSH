package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public class RevenueResponse {

    @SerializedName("totalRevenue")
    private String totalRevenue;


    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
