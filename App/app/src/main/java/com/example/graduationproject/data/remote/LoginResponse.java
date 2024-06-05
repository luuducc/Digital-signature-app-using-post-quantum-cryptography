package com.example.graduationproject.data.remote;

public class LoginResponse {
    private String _id;
    private String username;
    private String email;
    private String accessToken;


    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
