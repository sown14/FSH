package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public enum Sort {
    @SerializedName("DESC")
    DESC,

    @SerializedName("ASC")
    ASC,


    @SerializedName("PRICE_ASC")
    PRICE_ASC,
    @SerializedName("PRICE_DESC")
    PRICE_DESC

}
