package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

public class Cart extends  Product {



    @SerializedName("quantity")
    private int quantity;

    public Cart(int id, String name, Integer price, String material, Integer size, String description, String imageURL, String createdAt, Category category, ProductStatus status) {
        super(id, name, price, material, size, description, imageURL, createdAt, category, status);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
