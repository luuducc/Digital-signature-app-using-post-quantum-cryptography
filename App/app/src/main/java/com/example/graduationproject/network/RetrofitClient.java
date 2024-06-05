package com.example.graduationproject.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL_AUTH = "http://192.168.1.196:5000";
    private static final String BASE_URL_USER = "http://192.168.1.196:5000";

    private static Retrofit retrofitAuth;
    private static Retrofit retrofitTranscript;

    public static Retrofit getAuthRetrofitInstance() {
        if (retrofitAuth == null) {
            retrofitAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL_AUTH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitAuth;
    }

    public static Retrofit getTranscriptRetrofitInstance() {
        if (retrofitTranscript == null) {
            retrofitTranscript = new Retrofit.Builder()
                    .baseUrl(BASE_URL_USER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitTranscript;
    }
}
