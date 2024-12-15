package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR
}
