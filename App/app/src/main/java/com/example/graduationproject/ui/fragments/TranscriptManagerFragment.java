package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.graduationproject.HomeActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.network.services.TranscriptApiService;
import com.example.graduationproject.ui.activities.LoginActivity;
import com.example.graduationproject.ui.adapters.TranscriptAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranscriptManagerFragment extends Fragment {
    private Button btnCreatePdf, btnSign;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";

    public TranscriptManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transcript_manager, container, false);
//        recyclerView = view.findViewById(R.id.transcript_recycler_view);
//        spinner = view.findViewById(R.id.spinner);
//        btnCreatePdf = view.findViewById(R.id.btnCreatePdf);
//
//        // get user's transcripts
//        TranscriptApiService transcriptApiService = TranscriptApiService.getInstance();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
//        String userId =  sharedPreferences.getString("userId", "defaultId");
//        String accessToken =  sharedPreferences.getString("accessToken", "defautAccessToken");
//        Toast.makeText(getContext(),"baby", Toast.LENGTH_SHORT).show();
//
//        transcriptApiService.getTranscripts(userId, accessToken).enqueue(new Callback<List<Transcript>>() {
//            @Override
//            public void onResponse(Call<List<Transcript>> call, Response<List<Transcript>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Transcript> transcripts = response.body();
//                    List<String> classNames = new ArrayList<>();
//                    for (Transcript transcript : transcripts) {
//                        classNames.add(transcript.getClassName());
//
//                    }
//                    String[] classNameArray = classNames.toArray(new String[0]);
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                            getActivity(),
//                            android.R.layout.simple_spinner_item,
//                            classNameArray
//                    );
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner.setAdapter(adapter);
//                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String selectedClassName = classNameArray[position];
//
//                            Transcript transcript;
//                            for(Transcript data : transcripts) {
//                                if(data.getClassName().equals(selectedClassName)) {
//                                    transcript = data;
//                                    String className = transcript.getClassName();
//                                    List<Transcript.StudentGrade> studentGradeList = transcript.getStudentGrades();
//                                    TranscriptAdapter adapter = new TranscriptAdapter(getActivity(), studentGradeList);
//                                    recyclerView.setAdapter(adapter);
//                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                } else {
//                    // Handle unsuccessful response
//                    Log.e("Transcript Fragment", "Unsuccessful response: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Transcript>> call, Throwable throwable) {
//                Log.e("Transcript Fragment", "Failed to fetch transcript", throwable);
//                // Print the error message to the console
//                throwable.printStackTrace();
//            }
//        });

        return view;
    }

}