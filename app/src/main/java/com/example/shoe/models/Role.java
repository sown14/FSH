package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public enum Role {
    @SerializedName("admin")
    ADMIN,
    @SerializedName("user")
    USER,
    @SerializedName("Guest")
    GUEST
}

