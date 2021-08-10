package com.akounto.accountingsoftware.Services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitClient {

    private static volatile Retrofit INSTANCE;
    //Retrofit adapts a Java interface to HTTP calls by using annotations on the declared methods to define how requests are made.
    //Create instances using the builder and pass your interface to create to generate an implementation
    public static Retrofit getClient(String baseUrl) {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(logging);
                    httpClient.readTimeout(60, TimeUnit.SECONDS);
                    httpClient.connectTimeout(60, TimeUnit.SECONDS);
                    INSTANCE = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build()/*for printing logs in Logcat --all request header and response printed*/)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
