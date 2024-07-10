package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.local.MyViewModel;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.ui.adapters.KeyAdapter;
import com.example.graduationproject.ui.adapters.TranscriptAdapter;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.RequirePermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranscriptManagerFragment extends Fragment {
    private Button btnCreatePdf, btnSign, btnVerify;
    private Spinner spinner;
    private TextView isSignedJson, isSignedPdf;
    private RecyclerView recyclerView;
    private List<Transcript> transcripts;
    private Transcript selectedTranscript;
    private MyViewModel myViewModel;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";
    // allow the fragment to fetch data and display
    public TranscriptManagerFragment(List<Transcript> transcripts) {
        this.transcripts = transcripts;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transcript_manager, container, false);
        recyclerView = view.findViewById(R.id.transcript_recycler_view);
        spinner = view.findViewById(R.id.spinner);
        btnCreatePdf = view.findViewById(R.id.btnCreatePdf);
        btnSign = view.findViewById(R.id.btnSign);
        btnVerify = view.findViewById(R.id.btnVerify);
        isSignedJson = view.findViewById(R.id.isSignedJson);
        isSignedPdf = view.findViewById(R.id.isSignedPdf);

        newSetupSpinner();
        setupCreatePdfButton();
        setupSignButton();
        setupVerifyButton();

        return view;
    }
    private void newSetupSpinner() {
        myViewModel.getTranscripts().observe(getViewLifecycleOwner(), transcripts1 -> {
            if (transcripts1 == null) {
                return;
            }
            List<String> classNames = new ArrayList<>();
            for (Transcript transcript : transcripts1) {
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
                    String selectedClassname = classNames.get(position);
                    for (Transcript transcript : transcripts1) {
                        if (transcript.getClassName().equals(selectedClassname)) {
                            // assign the selected transcript
                            selectedTranscript = transcript;
                            setIsSignedTextView(transcript);
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
        });
    }
    private void setupCreatePdfButton() {
        btnCreatePdf.setOnClickListener(v -> {
            String className = spinner.getSelectedItem().toString();
            TranscriptAdapter adapter = (TranscriptAdapter) recyclerView.getAdapter();
            List<Transcript.StudentGrade> studentGradeList = adapter.getDataList();
            // Get username
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(MyConstant.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            // require read/write file permission
            RequirePermission.verifyStoragePermissions(getActivity());
            try {
                FileHelper.createPdf(studentGradeList, className, username);
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
            Button btnOk = popupView.findViewById(R.id.btnSignOk);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(popupView);
            final AlertDialog dialog = builder.create();

            btnSignJson.setOnClickListener(view -> {
                showKeySelectionDialog(KeyDialogFragment.MODE_SIGN_JSON, KeyAdapter.MODE_SIGN);
            });

            btnSignPdf.setOnClickListener(view -> {
                showKeySelectionDialog(KeyDialogFragment.MODE_SIGN_PDF, KeyAdapter.MODE_SIGN);
            });

            btnSignAll.setOnClickListener(view -> {
                showKeySelectionDialog(KeyDialogFragment.MODE_SIGN_ALL, KeyAdapter.MODE_SIGN);
            });

            btnOk.setOnClickListener(view -> dialog.dismiss());

            // show the dialog
            dialog.show();
        });
    }
    private void setupVerifyButton() {
        btnVerify.setOnClickListener(v -> {
            // create custom layout view
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View popupView = inflater.inflate(R.layout.popup_verify_options, null);

            Button btnVerifyJson = popupView.findViewById(R.id.btnVerifyJson);
            Button btnVerifyPdf = popupView.findViewById(R.id.btnVerifyPdf);
            Button btnVerifyOk = popupView.findViewById(R.id.btnVerifyOk);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(popupView);
            final AlertDialog dialog = builder.create();

            btnVerifyJson.setOnClickListener(view -> {
                showKeySelectionDialog(KeyDialogFragment.MODE_VERIFY_JSON, KeyAdapter.MODE_VERIFY);
            });
            btnVerifyPdf.setOnClickListener(view -> {
                showKeySelectionDialog(KeyDialogFragment.MODE_VERIFY_PDF, KeyAdapter.MODE_VERIFY);
            });
            btnVerifyOk.setOnClickListener(view -> dialog.dismiss());

            // show the dialog
            dialog.show();
        });
    }
    private void showKeySelectionDialog(int keyDialogModeType, int keyAdapterModeType) {
        KeyDialogFragment keyDialogFragment = new KeyDialogFragment(
                selectedTranscript, keyDialogModeType, keyAdapterModeType);
        keyDialogFragment.show(getActivity().getSupportFragmentManager(), "keyDialogFragment");
    }
    private void setIsSignedTextView(Transcript transcript) {
        String keyIdJson = transcript.getKeyIdJson();
        String keyIdPdf = transcript.getKeyIdPdf();
        String keyJsonAlias = null;
        String keyPdfAlias = null;
        Log.d("TranscriptFragment", "hi" + keyIdJson);
        List<PublicKeyToStore> publicKeyList = FileHelper.retrievePublicKeyFromFile(getContext());
        if (publicKeyList == null) return;
        for (PublicKeyToStore publicKey : publicKeyList) {
            String keyId = publicKey.getUuid().toString();
            String keyAlias = publicKey.getKeyAlias();
            if (keyId.equals(keyIdJson)) {
                keyJsonAlias = keyAlias;
            }
            if (keyId.equals(keyIdPdf)) {
                keyPdfAlias = keyAlias;
            }
            if (keyJsonAlias != null && keyPdfAlias != null) {
                break;
            }
        }
        String jsonText = keyJsonAlias != null ? (" - " + keyJsonAlias) : "";
        String pdfText = keyPdfAlias != null ? (" - " + keyPdfAlias) : "";
        isSignedJson.setText("Signed JSON: " + transcript.isSignedJson() + jsonText);
        isSignedPdf.setText("Signed PDF: " + transcript.isSignedPdf() + pdfText);
    }
}