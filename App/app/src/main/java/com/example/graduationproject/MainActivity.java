package com.example.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonAction;
    private TextView textViewToggleAction;

    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonAction = findViewById(R.id.buttonLogin);
        textViewToggleAction = findViewById(R.id.textViewToggleAction);

        buttonAction.setText("Login");
        textViewToggleAction.setText("Don't have an account? Sign up");

        textViewToggleAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginMode = !isLoginMode;
                if (isLoginMode) {
                    buttonAction.setText("Login");
                    textViewToggleAction.setText("Don't have an account? Sign up");
                } else {
                    buttonAction.setText("Sign up");
                    textViewToggleAction.setText("Already have an account? Log in");
                }
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginMode) {
                    // Implement login logic here
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.43.79:5000")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    String email, password;
                    email = editTextUsername.getText().toString();
                    password = editTextPassword.getText().toString();
                    LoginRequest loginRequest = new LoginRequest(email, password);
                    AuthService authService = retrofit.create(AuthService.class);
                    Call<LoginResponse> login = authService.login(loginRequest);
                    login.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                Toast.makeText(MainActivity.this, "hello " + loginResponse.getUsername(), Toast.LENGTH_SHORT).show();

                                // Switch activity
                                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
//                                startActivity(myIntent);
                                Bundle myBundle = new Bundle();
                                myBundle.putString("userId", loginResponse.get_id());
                                myIntent.putExtras(myBundle);
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            Toast.makeText(MainActivity.this,"Cannot login", Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity", "Failed to login", throwable);
                            // Print the error message to the console
                            throwable.printStackTrace();;
                        }
                    });
//
                } else {
//                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        });
    }

    interface AuthService {
        @POST("/api/auth/login")
        Call<LoginResponse> login(@Body LoginRequest user);
    }
}
