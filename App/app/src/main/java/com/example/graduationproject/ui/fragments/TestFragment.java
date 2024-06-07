package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;

public class TestFragment extends Fragment {
    private Button btnSayHello;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.key_row, container, false);

        // Initialize the button
        btnSayHello = view.findViewById(R.id.btnRegisterKey);

        // Set click listener for the button
        btnSayHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast or log a message when the button is clicked
                Toast.makeText(v.getContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
