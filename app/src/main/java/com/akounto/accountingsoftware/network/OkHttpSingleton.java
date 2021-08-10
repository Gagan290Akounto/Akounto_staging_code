package com.akounto.accountingsoftware.network;

import android.content.Context;

import okhttp3.OkHttpClient;

public class OkHttpSingleton {
    /* access modifiers changed from: private */
    private static Context context;
   /* private static OkHttpSingleton singletonInstance;
    private OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).addInterceptor(this.interceptor).addInterceptor(new Interceptor() {
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            return chain.proceed(newBuilder.addHeader("Authorization", "Bearer " + UiUtil.getUserDetails(OkHttpSingleton.context).getAccess_token()).addHeader("X-Company", Integer.toString(((UserBusiness) new Gson().fromJson(UiUtil.getUserDetails(OkHttpSingleton.context).getUserDetails(), UserBusiness.class)).getActiveBusiness().getId())).addHeader("Content-Type", "application/json").build());
        }
    }).retryOnConnectionFailure(false).connectionPool(new ConnectionPool(5, 30000, TimeUnit.MILLISECONDS)).build();
    HttpLoggingInterceptor interceptor;

    public static OkHttpSingleton getInstance(Context mcontext) {
        context = mcontext;
        if (singletonInstance == null) {
            singletonInstance = new OkHttpSingleton();
        }
        return singletonInstance;
    }

    private OkHttpSingleton() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        this.interceptor = httpLoggingInterceptor;
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public OkHttpClient getClient() {
        return this.client;
    }*/
   private static OkHttpSingleton singletonInstance;

    // No need to be static; OkHttpSingleton is unique so is this.
    private final OkHttpClient client;

    // Private so that this cannot be instantiated.
    private OkHttpSingleton() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }

    public static OkHttpSingleton getInstance(Context context) {
        if (singletonInstance == null) {
            singletonInstance = new OkHttpSingleton();
        }
        return singletonInstance;
    }

    // In case you just need the unique OkHttpClient instance.
    public OkHttpClient getClient() {
        return client;
    }

    public void closeConnections() {
        client.dispatcher().cancelAll();
    }
}
