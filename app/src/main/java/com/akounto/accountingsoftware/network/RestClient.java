package com.akounto.accountingsoftware.network;

import android.app.Activity;
import android.content.Context;
import com.akounto.accountingsoftware.Constants.Constant;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static APIServices REST_CLIENT;

    public static APIServices getInstance(Activity context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        APIServices aPIServices = new Retrofit.Builder().baseUrl(Constant.BASE_URL).client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).build().create(APIServices.class);
        REST_CLIENT = aPIServices;
        return aPIServices;
    }

    public static APIServices getInstance(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        APIServices aPIServices = new Retrofit.Builder().baseUrl(Constant.BASE_URL).client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).build().create(APIServices.class);
        REST_CLIENT = aPIServices;
        return aPIServices;
    }
}
