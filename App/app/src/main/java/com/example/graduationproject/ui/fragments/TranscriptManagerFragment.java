package com.example.graduationproject.ui.fragments;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.ui.adapters.TranscriptAdapter;
import com.example.graduationproject.utils.CreatePDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranscriptManagerFragment extends Fragment {
    private Button btnCreatePdf, btnSign;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private List<Transcript> transcripts;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";

    // allow the fragment to fetch data and display
    public static TranscriptManagerFragment newInstance(List<Transcript> transcripts) {
        TranscriptManagerFragment fragment = new TranscriptManagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("transcripts", new ArrayList<>(transcripts));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transcripts = (List<Transcript>) getArguments().getSerializable("transcripts");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transcript_manager, container, false);
        recyclerView = view.findViewById(R.id.transcript_recycler_view);
        spinner = view.findViewById(R.id.spinner);
        btnCreatePdf = view.findViewById(R.id.btnCreatePdf);

        setupSpinner();
        setupCreatePdfButton();

        return view;
    }

    private void setupSpinner() {
        if (transcripts == null) {
            return;
        }
        List<String> classNames = new ArrayList<>();
        for (Transcript transcript : transcripts) {
            classNames.add(transcript.getClassName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                classNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClassName = classNames.get(position);

                for (Transcript transcript : transcripts) {
                    if (transcript.getClassName().equals(selectedClassName)) {
                        List<Transcript.StudentGrade> studentGradeList =  transcript.getStudentGrades();
                        TranscriptAdapter transcriptAdapter = new TranscriptAdapter(getActivity(), studentGradeList);
                        recyclerView.setAdapter(transcriptAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCreatePdfButton() {
        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String className = spinner.getSelectedItem().toString();
                TranscriptAdapter adapter = (TranscriptAdapter) recyclerView.getAdapter();
                List<Transcript.StudentGrade> studentGradeList = adapter.getDataList();
                try {
                    CreatePDF.createPdf(v.getContext(), studentGradeList, className);
                    Toast.makeText(v.getContext(), "Created PDF for class: " + className, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "Failed to create PDF", Toast.LENGTH_LONG).show();
                    Log.d("TranscriptFragment", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
}