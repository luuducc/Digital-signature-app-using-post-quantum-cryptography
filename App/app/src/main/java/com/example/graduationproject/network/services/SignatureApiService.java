package com.example.graduationproject.network.services;

import com.example.graduationproject.data.remote.RegisterKeyRequest;
import com.example.graduationproject.data.remote.RegisterKeyResponse;
import com.example.graduationproject.data.remote.VerifyRequest;
import com.example.graduationproject.data.remote.VerifyResponse;
import com.example.graduationproject.network.RetrofitClient;
import com.example.graduationproject.network.api.SignatureApiInterface;

import retrofit2.Call;

public class SignatureApiService {
    private static SignatureApiService instance;
    private SignatureApiInterface apiInterface;
    private SignatureApiService() {
        apiInterface = RetrofitClient.getInstance().create(SignatureApiInterface.class);
    }
    public static SignatureApiService getInstance() {
        if (instance == null) {
            instance = new SignatureApiService();
        }
        return instance;
    }

    public Call<RegisterKeyResponse> registerKey(String userId, String accessToken, RegisterKeyRequest keyRequest) {
        return apiInterface.registerKey(userId, accessToken, keyRequest);
    }

    public Call<VerifyResponse> verifyTranscript(String accessToken, VerifyRequest verifyRequest) {
        return apiInterface.verifyTranscript(accessToken, verifyRequest);
    }
}
