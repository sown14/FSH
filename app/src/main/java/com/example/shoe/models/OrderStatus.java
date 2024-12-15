package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    @SerializedName("PENDING")
    PENDING,
    @SerializedName("WAITING")
    WAITING,
    @SerializedName("DELIVERING")
    DELIVERING,

    @SerializedName("COMPLETED")
    COMPLETED,

    @SerializedName("CANCELED")
    CANCELED,
}
