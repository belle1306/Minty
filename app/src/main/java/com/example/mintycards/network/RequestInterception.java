package com.example.mintycards.network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterception implements Interceptor {

    private static final String TAG = "RequestInterception";


    RequestInterception() {
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        return chain.proceed(requestBuilder.build());
    }
}
