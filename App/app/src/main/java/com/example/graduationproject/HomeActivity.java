package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.graduationproject.utils.CreatePDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class HomeActivity extends AppCompatActivity {
    private Button pdfButton, signButton;
    private Spinner spinner;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get intent from Main Activity
        Intent myCallerIntent = getIntent();
        Bundle myBundle = myCallerIntent.getExtras();
        String userId = myBundle.getString("userId");

        recyclerView = findViewById(R.id.recycler_view);
        spinner = findViewById(R.id.spinner);
        pdfButton = findViewById(R.id.buttonPDF);

        // CREATE PDF BUTTON
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = spinner.getSelectedItem().toString();
                TranscriptAdapter adapter = (TranscriptAdapter) recyclerView.getAdapter();
                List<TranscriptData.StudentGrade> studentGradeList = adapter.getDataList();
//                try {
//                    CreatePDF.createPdf(v.getContext(), studentGradeList, className);
//                    Toast.makeText(v.getContext(), "Created PDF for class: " + className, Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    Toast.makeText(v.getContext(), "Failed to create PDF", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                }
            }
        });

        // Request transcript from user
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.79:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestTranscript requestTranscript = retrofit.create(RequestTranscript.class);
        requestTranscript.getTranscript(userId).enqueue(new Callback<List<TranscriptData>>() {
            @Override
            public void onResponse(Call<List<TranscriptData>> call, @NonNull Response<List<TranscriptData>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<TranscriptData> transcriptList= response.body();
                    List<String> classNames = new ArrayList<>();
                    for(TranscriptData data : transcriptList) {
                        String className = data.getClassName();
                        classNames.add(className);
                    }
                    String[] classNamesArray = classNames.toArray(new String[0]);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            HomeActivity.this,
                            android.R.layout.simple_spinner_item,
                            classNamesArray
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedClassName = classNamesArray[position];

                            TranscriptData transcriptData;
                            for(TranscriptData data : transcriptList) {
                                if(data.getClassName().equals(selectedClassName)) {
                                    transcriptData = data;
                                    String className = transcriptData.getClassName();
                                    List<TranscriptData.StudentGrade> studentGradeList = transcriptData.getStudentGrades();
                                    TranscriptAdapter adapter = new TranscriptAdapter(HomeActivity.this, studentGradeList);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
//                    TranscriptData transcriptData = response.body();
//                    String className = transcriptData.getClassName();
//                    List<TranscriptData.StudentGrade> studentGradeList = transcriptData.getStudentGrades();
//
//                    // Check if studentGradeList is not null and not empty
//                    if (studentGradeList != null && !studentGradeList.isEmpty()) {
//                        Log.d("MainActivity", "Student grades available: " + studentGradeList.size());
//
//                        // set up the Recycler View
//                        TranscriptAdapter adapter = new TranscriptAdapter(HomeActivity.this, studentGradeList);
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
//
//                        try {
//                            CreatePDF.createPdf(HomeActivity.this, studentGradeList, className);
//                        } catch (IOException e) {
//                            Toast.makeText(HomeActivity.this, "Failed to create PDF", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                            throw new RuntimeException(e);
//                        }
//                    } else {
//                        Log.d("MainActivity", "No student grades available");
//                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("MainActivity", "Unsuccessful response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TranscriptData>> call, Throwable throwable) {
                Log.e("MainActivity", "Failed to fetch transcript", throwable);
                // Print the error message to the console
                throwable.printStackTrace();
            }
        });
    }

    interface RequestTranscript {
        @GET("/api/transcript/{userId}")
        Call<List<TranscriptData>> getTranscript(@Path("userId") String userId);
    }
}

