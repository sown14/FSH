package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public class Voucher {

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    @SerializedName("min")
    private int min;

    @SerializedName("max")
    private int max;

    @SerializedName("limit")
    private int limit;

    @SerializedName("timesUsed")
    private int timesUsed;

    @SerializedName("minOrderValue")
    private int minOrderValue;

    @SerializedName("discount")
    private int discount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
