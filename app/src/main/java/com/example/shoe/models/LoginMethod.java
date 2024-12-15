package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public enum LoginMethod {
    @SerializedName("google")
    google,
    @SerializedName("username")
    username
}
