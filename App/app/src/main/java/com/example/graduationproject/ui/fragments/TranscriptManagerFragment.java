package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.ui.adapters.KeyAdapter;
import com.example.graduationproject.ui.adapters.TranscriptAdapter;
import com.example.graduationproject.utils.CreatePDF;
import com.example.graduationproject.utils.DilithiumHelper;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.RequirePermission;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranscriptManagerFragment extends Fragment {
    private Button btnCreatePdf, btnSign;
    private Spinner spinner;
    private TextView isSigned;
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transcript_manager, container, false);
        recyclerView = view.findViewById(R.id.transcript_recycler_view);
        spinner = view.findViewById(R.id.spinner);
        btnCreatePdf = view.findViewById(R.id.btnCreatePdf);
        btnSign = view.findViewById(R.id.btnSign);
        isSigned = view.findViewById(R.id.isSigned);

        setupSpinner();
        setupCreatePdfButton();
        setupSignButton();

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
                        isSigned.setText("Signed: " + transcript.isSigned());
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

                // require read/write file permission
                RequirePermission.verifyStoragePermissions(getActivity());
                try {
                    CreatePDF.createPdf(studentGradeList, className);
                    Toast.makeText(v.getContext(), "Created PDF for class: " + className, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), "Failed to create PDF", Toast.LENGTH_LONG).show();
                    Log.d("TranscriptFragment", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
    private void setupSignButton() {
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create custom layout view
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View popupView = inflater.inflate(R.layout.popup_sign_options, null);

                Button btnSignJson = popupView.findViewById(R.id.btnSignJson);
                Button btnSignPdf = popupView.findViewById(R.id.btnSignPdf);
                Button btnSignAll = popupView.findViewById(R.id.btnSignAll);
                Button btnOk = popupView.findViewById(R.id.btnOk);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(popupView);
                final AlertDialog dialog = builder.create();

                // Set button click listeners
                btnSignJson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String className = spinner.getSelectedItem().toString();
                        for (Transcript transcript : transcripts) {
                            if (transcript.getClassName().equals(className)) {
                                String name = transcript.getStudentGrades().get(0).getName();
                                Gson gson = new GsonBuilder()
                                        .setPrettyPrinting().create();
                                String jsonKeyString =  gson.toJson(transcript);
                                byte[] jsonKeyByte = jsonKeyString.getBytes();
//                                byte[] signature = DilithiumHelper.sign()
                                showKeySelectionDialog();
                                Toast.makeText(v.getContext(), "Sign Json clicked" + name, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btnSignPdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showKeySelectionDialog();
                        Toast.makeText(v.getContext(), "Sign PDF clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                btnSignAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showKeySelectionDialog();
                        Toast.makeText(v.getContext(), "Sign All clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // show the dialog
                dialog.show();
            }
        });
    }
    private void showKeySelectionDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View keyListView = inflater.inflate(R.layout.popup_key_list, null);

        RecyclerView keyRecyclerView = keyListView.findViewById(R.id.key_list_popup_recycler_view);
        List<PublicKeyToStore> keylist = FileHelper.retrievePublicKeyFromFile(getActivity());
        KeyAdapter keyAdapter = new KeyAdapter(getContext(), keylist, KeyAdapter.MODE_SIGN);

        keyRecyclerView.setAdapter(keyAdapter);
        keyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(keyListView);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}