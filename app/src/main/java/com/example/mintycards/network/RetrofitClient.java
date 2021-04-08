package com.example.mintycards.network;


import com.example.mintycards.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";

    private static RetrofitClient retrofitClient;
    private        Retrofit       retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.APP_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    private OkHttpClient buildClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        RequestInterception requestInterception = new RequestInterception();

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(requestInterception)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}

