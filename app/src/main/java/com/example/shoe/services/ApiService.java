package com.example.shoe.services;

import com.example.shoe.models.ApiResponse;
import com.example.shoe.models.Category;
import com.example.shoe.models.Notification;
import com.example.shoe.models.Order;
import com.example.shoe.models.OrderStatus;
import com.example.shoe.models.Product;
import com.example.shoe.models.ProductStatus;
import com.example.shoe.models.RevenueResponse;
import com.example.shoe.models.Sort;
import com.example.shoe.models.User;
import com.example.shoe.models.Voucher;
import com.example.shoe.models.YearlyRevenueResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    /*
     * AUTH
     * */
    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse<User>> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("fcmToken") String fcmToken
    );

    @FormUrlEncoded
    @POST("login/google")
    Call<ApiResponse<User>> loginWithGoogle(
            @Field("idToken") String idToken,
            @Field("fcmToken") String fcmToken
    );

    @FormUrlEncoded
    @PUT("profile/updatePassword")
    Call<ApiResponse<User>> updatePassword(
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword
    );


    @FormUrlEncoded
    @PUT("profile")
    Call<ApiResponse<User>> updateProfile(
            @Field("fullname") String fullname,
            @Field("username") String username,
            @Field("dateOfBirth") String dateOfBirth,
            @Field("phoneNumber") String phoneNumber,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ApiResponse<User>> updateUser(
            @Path("id") Integer id,
            @Field("fullname") String fullname,
            @Field("username") String username,
            @Field("dateOfBirth") String dateOfBirth,
            @Field("phoneNumber") String phoneNumber,
            @Field("picture") String picture
    );

    @GET("profile")
    Call<ApiResponse<User>> getUserInfo();


    @FormUrlEncoded
    @POST("signup")
    Call<ApiResponse<User>> signUp(
            @Field("username") String username,
            @Field("fullname") String fullname,
            @Field("password") String password,
            @Field("email") String email,
            @Field("phoneNumber") String phoneNumber,
            @Field("dateOfBirth") String dateOfBirth,
            @Field("fcmToken") String fcmToken

    );


    /*
     * CATEGORY
     * */
    @FormUrlEncoded
    @POST("categories")
    Call<ApiResponse<Category>> createCategory(
            @Field("name") String name,
            @Field("description") String description,
            @Field("image") String image
    );

    @GET("categories")
    Call<ApiResponse<List<Category>>> getCategories();

    @FormUrlEncoded
    @PUT("categories/{categoryId}")
    Call<ApiResponse<Category>> updateCategory(
            @Path("categoryId") Integer categoryId,
            @Field("name") String name,
            @Field("description") String description,
            @Field("image") String image
    );

    /*
     * PRODUCT
     * */
    @FormUrlEncoded
    @POST("products")
    Call<ApiResponse<Product>> createProduct(
            @Field("name") String name,
            @Field("price") Integer price,
            @Field("material") String material,
            @Field("size") Integer size,
            @Field("description") String description,
            @Field("categoryId") Integer categoryId,
            @Field("imageURL") String imageURL
    );

    @FormUrlEncoded
    @PUT("products/{productId}")
    Call<ApiResponse<Product>> updateProduct(
            @Path("productId") Integer productId,
            @Field("status") ProductStatus status
    );


    // Dành riêng cho admin
    @GET("products")
    Call<ApiResponse<List<Product>>> getProducts();


    // Dành riêng cho user
    @GET("products")
    Call<ApiResponse<List<Product>>> getProductsWithUser(
            @Query("search") String text,
            @Query("sort")Sort sort
    );

    @GET("products/category/{categoryId}")
    Call<ApiResponse<List<Product>>> getProductsByCategory(
            @Path("categoryId") Integer categoryId
    );

    @GET("products/newest")
    Call<ApiResponse<List<Product>>> getProductsNewest();


    /*
     * ORDER
     * */
    @FormUrlEncoded
    @POST("orders")
    Call<ApiResponse<Order>> createOrder(
            @Field("items") String carts,
            @Field("paymentType") String paymentType,
            @Field("code") String code,
            @Field("address") String address
            //
    );


    @GET("orders/history")
    Call<ApiResponse<List<Order>>> getOrdersHistory(
            @Query("date") String date
    );

    @FormUrlEncoded
    @PUT("orders/{orderId}")
    Call<ApiResponse<Order>> updateOrder(
            @Path("orderId") Integer orderId,
            @Field("status") OrderStatus status
    );

    @GET("orders/{orderId}")
    Call<ApiResponse<Order>> getOrder(
            @Path("orderId") Integer orderId
    );

    /*
     * NOTIFICATION
     * */
    @GET("notifications")
    Call<ApiResponse<List<Notification>>> getNotifications();

    /*
     * VOUCHER
     * */

    @FormUrlEncoded
    @POST("check-discount")
    Call<ApiResponse<Voucher>> checkDiscount(
            @Field("code") String code,
            @Field("amount") int amount
    );

    @GET("vouchers")
    Call<ApiResponse<List<Voucher>>> getVouchers();

    @FormUrlEncoded
    @POST("vouchers")
    Call<ApiResponse<Product>> createVoucher(
            @Field("code") String code,
            @Field("description") String description,
            @Field("limit") Integer limit,
            @Field("min") Integer min,
            @Field("max") Integer max,
            @Field("minOrderValue") Integer minOrderValue

    );

    @GET("users")
    Call<ApiResponse<List<User>>> getUsers();

    @GET("yearly-revenue")
    Call<ApiResponse<YearlyRevenueResponse>> getYearlyRevenue();

    @GET("revenue")
    Call<ApiResponse<RevenueResponse>> getTotalRevenue(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );
}
