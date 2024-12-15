package com.example.shoe.services;

import android.util.Log;

import com.example.shoe.MyApplication;
import com.example.shoe.models.User;
import com.google.firebase.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.71.102/api/";
    private static ApiService apiInstance;

    public static void resetInstance() {
        apiInstance = null;
        createInstance();
    }
    private static void createInstance(){
        User user = MyApplication.getUser();
        String token = user != null ? user.getAccessToken() : null;
        Interceptor interceptor = chain -> {
            Request originalRequest = chain.request();
            Request.Builder modifiedRequest = originalRequest.newBuilder();
            if (token != null) {
                Log.d("TOKEN", "token not null");
                modifiedRequest.addHeader("Authorization", "Bearer " + token);
            }
//                modifiedRequest.addHeader("Authorization", "Bearer " + token);
            return chain.proceed(modifiedRequest.build());
        };

        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .cache(null);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build();

        apiInstance = retrofit.create(ApiService.class);
    }

    public static ApiService getInstance() {
        if (apiInstance == null) {
            createInstance();
        }

        return apiInstance;
    }

}
