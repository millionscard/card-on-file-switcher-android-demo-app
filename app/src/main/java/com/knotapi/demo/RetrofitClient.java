package com.knotapi.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Api myApi;

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Environment", "sandbox")
                        .addHeader("Client-Id", "ab86955e-22f4-49c3-97d7-369973f4cb9e")
                        .addHeader("Client-Secret", "d1a5cde831464cd3840ccf762f63ceb7")
                        .build();
                return chain.proceed(newRequest);
            }).build();

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }


    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}
