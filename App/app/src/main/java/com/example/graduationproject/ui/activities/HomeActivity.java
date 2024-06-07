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
import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.network.services.TranscriptApiService;
import com.example.graduationproject.ui.adapters.ViewPagerAdapter;
import com.example.graduationproject.utils.FileHelper;
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
    private List<Transcript> transcripts;
    private List<PublicKeyToStore> retrievedPublicKeys;
    private final String SHARED_PREFERENCES_NAME = MyConstant.SHARED_PREFERENCES_NAME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        fetchKeysInKeyStore();
        fetchTranscripts();
    }

    private void fetchTranscripts() {
        TranscriptApiService transcriptApiService = TranscriptApiService.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        String userId =  sharedPreferences.getString("userId", "defaultId");
        String accessToken =  sharedPreferences.getString("accessToken", "defautAccessToken");
        Log.d("MyHomeActivity", "hello" + userId + accessToken);

        transcriptApiService.getTranscripts(userId, "Bearer " + accessToken).enqueue(new Callback<List<Transcript>>() {
            @Override
            public void onResponse(Call<List<Transcript>> call, Response<List<Transcript>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("MyHomeActivity", response.message()); // http status message
                    transcripts = response.body();

                    // Only setup view pager when the transcript is fetched
                    setupViewPager();
                } else {
                    try {
                        Log.d("MyHomeActivity", response.message()); // http status message
                        Log.d("MyHomeActivity", response.errorBody().string()); // actual server response message
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transcript>> call, Throwable throwable) {
                Log.d("MyHomeActivity", "error when fetch");
            }
        });
    }
    private void fetchKeysInKeyStore() {
        retrievedPublicKeys = FileHelper.retrievePublicKeyFromFile(getApplicationContext());
    }
    private void setupViewPager() {
        ViewPagerAdapter adapter;
        if (retrievedPublicKeys == null) {
            adapter = new ViewPagerAdapter(this, transcripts, new ArrayList<>());
        } else {
            adapter = new ViewPagerAdapter(this, transcripts, retrievedPublicKeys);
        }
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
    public List<Transcript> getTranscripts() {
        return this.transcripts;
    }
}
