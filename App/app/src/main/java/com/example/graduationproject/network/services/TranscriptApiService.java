package com.example.graduationproject.network.services;

import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.network.RetrofitClient;
import com.example.graduationproject.network.api.TranscriptApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Path;

public class TranscriptApiService {
    private static TranscriptApiService instance;
    private TranscriptApiInterface apiInterface;

    private TranscriptApiService() {
        apiInterface = RetrofitClient.getInstance().create(TranscriptApiInterface.class);
    }

    public static TranscriptApiService getInstance() {
        if (instance == null) {
            instance = new TranscriptApiService();
        }
        return instance;
    }

    public Call<List<Transcript>> getTranscripts(String accessToken) {
        return apiInterface.getTranscripts(accessToken);
    }
}
