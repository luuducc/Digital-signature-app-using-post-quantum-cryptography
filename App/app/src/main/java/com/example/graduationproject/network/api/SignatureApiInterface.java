package com.example.graduationproject.network.api;

import com.example.graduationproject.data.remote.RegisterKeyRequest;
import com.example.graduationproject.data.remote.RegisterKeyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SignatureApiInterface {
    @POST("/api/key/register/{userId}")
    Call<RegisterKeyResponse> registerKey(
            @Path("userId") String userId,
            @Header("Authorization") String accessToken,
            @Body RegisterKeyRequest keyRequest);
}
