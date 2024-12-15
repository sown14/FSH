package com.example.shoe.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private Integer price;
    @SerializedName("material")
    private String material;
    @SerializedName("size")
    private Integer size;
    @SerializedName("description")
    private String description;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("category")
    private  Category category;

    @SerializedName("status")
    private ProductStatus status;

    public Product(int id, String name, Integer price, String material, Integer size, String description, String imageURL, String createdAt, Category category, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.material = material;
        this.size = size;
        this.description = description;
        this.imageURL = imageURL;
        this.createdAt = createdAt;
        this.category = category;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
