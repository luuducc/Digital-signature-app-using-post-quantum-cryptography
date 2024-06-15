package com.example.graduationproject.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.local.PrivateKeyToStore;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.data.remote.VerifyRequest;
import com.example.graduationproject.data.remote.VerifyResponse;
import com.example.graduationproject.network.services.SignatureApiService;
import com.example.graduationproject.ui.activities.HomeActivity;
import com.example.graduationproject.ui.adapters.KeyAdapter;
import com.example.graduationproject.utils.DilithiumHelper;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.HashHelper;
import com.example.graduationproject.utils.RSADecryptor;
import com.example.graduationproject.utils.RSAHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeyDialogFragment extends androidx.fragment.app.DialogFragment {
    private Transcript selectedTranscript;
    private final String SHARED_PREFERENCES_NAME = MyConstant.SHARED_PREFERENCES_NAME;
    public KeyDialogFragment(Transcript selectedTranscript) {
        this.selectedTranscript = selectedTranscript;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.popup_key_list, container, false);

        RecyclerView keyRecyclerView = view.findViewById(R.id.key_list_popup_recycler_view);
        List<PublicKeyToStore> publicKeyToStoreList = FileHelper.retrievePublicKeyFromFile(view.getContext());

        KeyAdapter keyAdapter = new KeyAdapter(
                getActivity(),
                publicKeyToStoreList, KeyAdapter.MODE_SIGN,
                key -> signAndPostTranscript(selectedTranscript, key));

        keyRecyclerView.setAdapter(keyAdapter);
        keyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        return view;
    }
    private void signAndPostTranscript(Transcript transcript, PublicKeyToStore publicKeyToStore) {
        List<PrivateKeyToStore> privateKeyList = FileHelper.retrievePrivateKeyFromFile(getContext());
        PrivateKeyToStore privateKeyToStore = null;
        UUID keyId = publicKeyToStore.getUuid();
        // get the corresponding private key
        for (PrivateKeyToStore key : privateKeyList) {
            if (key.getUuid().equals(keyId)) {
                privateKeyToStore = key;
            }
        }
        assert privateKeyToStore != null;

        byte[] encryptedPrivateKeyByte = privateKeyToStore.getEncryptedPrivateKey();
        byte[] privateKeyByte = RSADecryptor.decryptData(encryptedPrivateKeyByte, RSAHelper.getPrivateKey());
        byte[] publicKeyByte = publicKeyToStore.getPublicKey();

        DilithiumPublicKeyParameters publicKeyParameters = DilithiumHelper.retrievePublicKey(privateKeyToStore.getDilithiumParametersType(), publicKeyByte);
        DilithiumPrivateKeyParameters privateKeyParameters = DilithiumHelper.retrievePrivateKey(publicKeyToStore.getDilithiumParametersType(), privateKeyByte, publicKeyParameters);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting().create();
        String jsonTranscript = gson.toJson(transcript);
        byte[] hashedMessage = HashHelper.hashString(jsonTranscript);
        byte[] transcriptToSign = jsonTranscript.getBytes();
        String initialHash = Base64.getEncoder().encodeToString(hashedMessage);

        // sign the transcript
        byte[] signature = DilithiumHelper.sign(privateKeyParameters, hashedMessage);
        String signatureString = Base64.getEncoder().encodeToString(signature);
        boolean verifyResult = DilithiumHelper.verify(publicKeyParameters, transcriptToSign, signature);

        // send the verify request
        SignatureApiService signatureApiService = SignatureApiService.getInstance();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String userId =  sharedPreferences.getString("userId", "defaultId");
        String accessToken =  sharedPreferences.getString("accessToken", "defaultAccessToken");
        VerifyRequest verifyRequest = new VerifyRequest(keyId.toString(), initialHash, signatureString);
        signatureApiService.verifyTranscript(userId, "Bearer " + accessToken, verifyRequest).enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.isSuccessful()) {
                    boolean result = response.body().isResult();
                    Toast.makeText(getContext(), Boolean.toString(result), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TranscriptFragment", String.valueOf(response.code())); // http status message
                    Toast.makeText(getContext(), "Verify key failed", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d("TranscriptFragment", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (response.code() == 403) { // token is not valid
                        // delete old access token and navigate to login screen
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences .edit();
                        editor.remove("accessToken");
                        editor.apply();
                        ((HomeActivity) getContext()).navigateToLoginScreen();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable throwable) {
                Log.d("TranscriptFragment", "error when verify");
                if (throwable instanceof IOException) {
                    Log.e("TranscriptFragment", "Network error or conversion error: " + throwable.getMessage());
                } else {
                    Log.e("TranscriptFragment", "Unexpected error: " + throwable.getMessage());
                }
            }
        });
    }
}
