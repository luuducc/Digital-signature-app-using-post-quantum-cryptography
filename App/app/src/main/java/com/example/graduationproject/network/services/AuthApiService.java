package com.example.graduationproject.network.services;

import com.example.graduationproject.data.remote.LoginRequest;
import com.example.graduationproject.data.remote.LoginResponse;
import com.example.graduationproject.network.RetrofitClient;
import com.example.graduationproject.network.api.AuthApiInterface;

import retrofit2.Call;

public class AuthApiService {
    private static AuthApiService instance;
    private AuthApiInterface apiInterface;
    private AuthApiService() {
        apiInterface = RetrofitClient.getInstance().create(AuthApiInterface.class);
    }
    public static AuthApiService getInstance() {
        if (instance == null) {
            instance = new AuthApiService();
        }
        return instance;
    }
    public Call<LoginResponse> login(LoginRequest loginRequest) {
        return apiInterface.login(loginRequest);
    }

//    public Call<RegisterResponse> register(RegisterRequest registerRequest) {
//        return apiInterface.register(registerRequest);
//    }
}
