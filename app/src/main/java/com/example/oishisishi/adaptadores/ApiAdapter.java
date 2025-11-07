package com.example.oishisishi.adaptadores;

import com.example.oishisishi.servicios.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter {
    private static ApiService API_SERVICE;
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    public static ApiService getApiService() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            API_SERVICE = retrofit.create(ApiService.class);
        }
        return API_SERVICE;
    }
}
