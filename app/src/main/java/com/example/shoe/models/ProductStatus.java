package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;


public enum ProductStatus {

    @SerializedName("FOR_SALE")
    FOR_SALE,

    @SerializedName("OUT_OF_STOCK")
    OUT_OF_STOCK,

    @SerializedName("STOP_SELLING")
    STOP_SELLING


}

