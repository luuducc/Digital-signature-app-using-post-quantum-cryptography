package com.example.graduationproject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class FetchData {

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestTranscript requestTranscript = retrofit.create(RequestTranscript.class);

        requestTranscript.getTranscript("physics").enqueue(new Callback<TranscriptData>() {
            @Override
            public void onResponse(Call<TranscriptData> call, Response<TranscriptData> response) {
                String className = response.body().getClassName();
                System.out.println(className);
            }

            @Override
            public void onFailure(Call<TranscriptData> call, Throwable throwable) {
                System.err.println("Failed to fetch transcript: " + throwable.getMessage());
            }
        });
    }
    interface RequestTranscript {
        @GET("/api/transcript/{className}")
        Call<TranscriptData> getTranscript(@Path("className") String className);
    }
}
