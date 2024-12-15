package com.example.shoe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.shoe.models.Cart;
import com.example.shoe.models.Product;
import com.example.shoe.models.User;
import com.example.shoe.services.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static SharedPreferences sharedPreferences;
    private static final String SHARED_USER_INFO = "USER_INFO_NEW";
    private static final String SHARED_CART_ITEMS = "CART_ITEMS";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE);

    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }



    public static void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        Log.d("SAVE_USER_JSON", userJson); // Log thông tin được lưu
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_USER_INFO, userJson);
        editor.apply();
        ApiClient.resetInstance();
    }

    public static User getUser() {
        String userJson = sharedPreferences.getString(SHARED_USER_INFO, null);
        if (userJson != null) {

            Gson gson = new Gson();
            Log.d("USER_JSON",userJson);
            return gson.fromJson(userJson, User.class);
        }
        Log.d("USER_JSON","null");
        return null;
    }

    public static User updateUser(User newInfoUser) {

        User currentUser = getUser();
        if (currentUser != null) {
            String token = currentUser.getAccessToken();
            newInfoUser.setAccessToken(token);
            saveUser(newInfoUser);
            ApiClient.resetInstance();
        }

        return currentUser;
    }


    public static void removeUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_USER_INFO);
        editor.apply();
    }


    public static void removeAllCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_CART_ITEMS);
        editor.apply();
    }

    private static void saveCartItems(List<Cart> cartItems) {
        Gson gson = new Gson();
        String cartItemsJson = gson.toJson(cartItems);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_CART_ITEMS, cartItemsJson);
        editor.apply();
    }
    public static void removeItemCart(int productId) {
        List<Cart> cartItems = getCartItems();
        cartItems.removeIf(cart -> cart.getId() == productId);
        saveCartItems(cartItems);
    }

    public static void increaseItemCart(int productId) {
        List<Cart> cartItems = getCartItems();
        for (Cart cart : cartItems) {
            if (cart.getId() == productId) {
                cart.setQuantity(cart.getQuantity() + 1);
                break;
            }
        }
        saveCartItems(cartItems);
    }

    public static void decreaseItemCart(int productId) {
        List<Cart> cartItems = getCartItems();
        for (Cart cart : cartItems) {
            if (cart.getId() == productId) {
                if (cart.getQuantity() > 1) { // Giảm số lượng nếu lớn hơn 1
                    cart.setQuantity(cart.getQuantity() - 1);
                } else {
                    cartItems.remove(cart); // Xóa mục nếu số lượng bằng 1
                }
                break;
            }
        }
        saveCartItems(cartItems);
    }
    public static List<Cart> getCartItems() {
        String cartItemsJson = sharedPreferences.getString(SHARED_CART_ITEMS, null);
        if (cartItemsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Cart>>() {}.getType();
            return gson.fromJson(cartItemsJson, type);
        }
        return new ArrayList<>();
    }
    public static void addToCart(Product newProduct) {

        List<Cart> cartItems = getCartItems();
        boolean exists = false;
        if(cartItems !=null){
            for (Cart cart : cartItems) {
                if (cart.getId() == newProduct.getId()) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {

            Cart newCartItem = new Cart(
                    newProduct.getId(),
                    newProduct.getName(),
                    newProduct.getPrice(),
                    newProduct.getMaterial(),
                    newProduct.getSize(),
                    newProduct.getDescription(),
                    newProduct.getImageURL(),
                    newProduct.getCreatedAt(),
                    newProduct.getCategory(),
                    newProduct.getStatus()
            );
            newCartItem.setQuantity(1);
            cartItems.add(newCartItem);
        }
        saveCartItems(cartItems);
    }

}
