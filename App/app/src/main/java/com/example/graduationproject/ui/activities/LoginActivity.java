package com.example.graduationproject.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;
import com.example.graduationproject.data.remote.LoginRequest;
import com.example.graduationproject.data.remote.LoginResponse;
import com.example.graduationproject.network.services.AuthApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequestAndNavigateToMainScreen();
            }
        });
    }
    private void navigateToMainScreen() {
        // Start the main activity
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the LoginActivity
    }
    private void sendLoginRequestAndNavigateToMainScreen() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        AuthApiService authApiService = AuthApiService.getInstance();
        LoginRequest loginRequest = new LoginRequest(email, password);

        authApiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(LoginActivity.this, "Hello " + loginResponse.getUsername(), Toast.LENGTH_SHORT).show();

                    // Store access token and user id to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences .edit();
                    editor.putString("accessToken", loginResponse.getAccessToken());
                    editor.putString("userId", loginResponse.get_id());
                    editor.apply();

                    navigateToMainScreen();
                } else {
                    Toast.makeText(LoginActivity.this,"Failed to login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                Toast.makeText(LoginActivity.this,"Server error", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Server error", throwable);
                // Print the error message to the console
                throwable.printStackTrace();;
            }
        });
    }
}
