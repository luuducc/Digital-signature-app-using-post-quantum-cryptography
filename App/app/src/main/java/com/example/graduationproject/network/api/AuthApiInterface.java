package com.example.graduationproject.network.api;

import com.example.graduationproject.data.remote.LoginRequest;
import com.example.graduationproject.data.remote.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiInterface {
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

//    @POST("/register")
//    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
