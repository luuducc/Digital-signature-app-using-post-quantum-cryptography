package com.example.graduationproject.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.graduationproject.R;
import com.example.graduationproject.ViewPagerAdapter;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.network.services.TranscriptApiService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        fetchTranscripts();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Key Manager");
                    break;
                case 1:
                    tab.setText("Transcript Manager");
                    break;
            }
        }).attach();
    }

    private void fetchTranscripts() {
        TranscriptApiService transcriptApiService = TranscriptApiService.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        String userId =  sharedPreferences.getString("userId", "defaultId");
        String accessToken =  sharedPreferences.getString("accessToken", "defautAccessToken");
        Toast.makeText(HomeActivity.this,"baby", Toast.LENGTH_SHORT).show();
        Log.d("HomeActivity", "hello" + userId + accessToken);

        transcriptApiService.getTranscripts(userId, "Bearer " + accessToken).enqueue(new Callback<List<Transcript>>() {
            @Override
            public void onResponse(Call<List<Transcript>> call, Response<List<Transcript>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transcript> transcripts = response.body();
                    List<String> classNames = new ArrayList<>();
                    for (Transcript transcript : transcripts) {
                        classNames.add(transcript.getClassName());

                    }
                    String[] classNameArray = classNames.toArray(new String[0]);
                    Log.d("HomeActivity", classNameArray[0]);
                } else {
                    Log.d("HomeActivity", "failed to get trancsript");
                }
            }

            @Override
            public void onFailure(Call<List<Transcript>> call, Throwable throwable) {
                Log.d("HomeActivity", "error when fetch");
            }
        });
    }
}
