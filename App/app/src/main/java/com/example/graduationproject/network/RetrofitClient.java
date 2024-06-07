package com.example.graduationproject.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL_AUTH = "http://172.16.113.96:5000";
    private static final String BASE_URL_TRANSCRIPT = "http://172.16.113.96:5000";
    private static final String BASE_URL_SIGNATURE = "http://172.16.113.96:5000";

    private static Retrofit retrofitAuth;
    private static Retrofit retrofitTranscript;
    private static Retrofit retrofitSignature;

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
                    .baseUrl(BASE_URL_TRANSCRIPT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitTranscript;
    }
    public static Retrofit getSignatureRetrofitInstance() {
        if (retrofitSignature == null) {
            retrofitSignature = new Retrofit.Builder()
                    .baseUrl(BASE_URL_SIGNATURE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitSignature;
    }
}
