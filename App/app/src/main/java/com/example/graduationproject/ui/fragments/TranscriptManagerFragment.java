package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.example.graduationproject.data.local.PrivateKeyToStore;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.data.remote.VerifyRequest;
import com.example.graduationproject.data.remote.VerifyResponse;
import com.example.graduationproject.network.services.SignatureApiService;
import com.example.graduationproject.ui.activities.HomeActivity;
import com.example.graduationproject.ui.adapters.KeyAdapter;
import com.example.graduationproject.ui.adapters.TranscriptAdapter;
import com.example.graduationproject.utils.CreatePDF;
import com.example.graduationproject.utils.DilithiumHelper;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.HashHelper;
import com.example.graduationproject.utils.RSADecryptor;
import com.example.graduationproject.utils.RSAHelper;
import com.example.graduationproject.utils.RequirePermission;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranscriptManagerFragment extends Fragment {
    private Button btnCreatePdf, btnSign;
    private Spinner spinner;
    private TextView isSigned;
    private RecyclerView recyclerView;
    private List<Transcript> transcripts;
    private Transcript selectedTranscript;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";

    // allow the fragment to fetch data and display
    public TranscriptManagerFragment(List<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        // assign the selected transcript
                        selectedTranscript = transcript;

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
        btnCreatePdf.setOnClickListener(v -> {
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
        });
    }
    private void setupSignButton() {
        btnSign.setOnClickListener(v -> {
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

            btnSignJson.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Sign Json clicked" + selectedTranscript.getClassName(), Toast.LENGTH_SHORT).show();
                showKeySelectionDialog();
            });

            btnSignPdf.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Sign PDF clicked" + selectedTranscript.getClassName(), Toast.LENGTH_SHORT).show();
                showKeySelectionDialog();
            });

            btnSignAll.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Sign All clicked", Toast.LENGTH_SHORT).show();
                showKeySelectionDialog();
            });

            btnOk.setOnClickListener(view -> dialog.dismiss());

            // show the dialog
            dialog.show();
        });
    }
    private void showKeySelectionDialog() {
        KeyDialogFragment keyDialogFragment = new KeyDialogFragment(selectedTranscript);
        keyDialogFragment.show(getActivity().getSupportFragmentManager(), "keyDialogFragment");
    }
}