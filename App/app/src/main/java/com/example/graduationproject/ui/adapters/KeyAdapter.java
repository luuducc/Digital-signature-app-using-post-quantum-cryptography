package com.example.graduationproject.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.local.PrivateKeyToStore;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.RegisterKeyRequest;
import com.example.graduationproject.data.remote.RegisterKeyResponse;
import com.example.graduationproject.network.services.SignatureApiService;
import com.example.graduationproject.ui.activities.HomeActivity;
import com.example.graduationproject.utils.AuthenticateFingerprint;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.callback.OnKeyItemClickListener;
import com.example.graduationproject.utils.KeystoreHelper;
import com.example.graduationproject.utils.RequirePermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.MyViewHolder> {
    Context context;
    List<PublicKeyToStore> keyList;
    List<Boolean> expandedStates;
    private final String SHARED_PREFERENCES_NAME = "graduation_preferences";
    private final int recyclerViewType;
    private final OnKeyItemClickListener keyItemClickListener;
    public static final int MODE_SHOW = 1;
    public static final int MODE_SIGN = 2;
    public static final int MODE_VERIFY = 3;

    public KeyAdapter(
            Context context, List<PublicKeyToStore> keyList,
            int recyclerViewType, OnKeyItemClickListener listener) {
        this.context = context;
        this.keyList = keyList;
        this.expandedStates = new ArrayList<>(Collections.nCopies(keyList == null ? 0 : keyList.size(), false));
        this.recyclerViewType = recyclerViewType;
        this.keyItemClickListener = listener;
    }

    public List<PublicKeyToStore> getKeyList() {
        return keyList;
    }

    @NonNull
    @Override
    public KeyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.key_row, parent, false);
        return new KeyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyAdapter.MyViewHolder holder, int position) {
        PublicKeyToStore key = keyList.get(position);
        holder.keyAlias.setText("Alias: " + key.getKeyAlias());
        holder.isRegistered.setText("Registered: " + Boolean.toString(key.isRegistered()));
        // Reset visibility and other properties to ensure proper recycling
        holder.expandedLayout.setVisibility(View.GONE);

        switch (recyclerViewType) {
            case MODE_SHOW: {
                holder.keyId.setText("UUID: " + key.getUuid().toString());
                holder.keyParaType.setText("Type: " + key.getDilithiumParametersType());
                ConstraintLayout expandedLayout = holder.expandedLayout;
                Button registerButton = holder.btnRegister;
                Button extractButton = holder.btnExtract;

                registerButton.setOnClickListener(v -> AuthenticateFingerprint.authenticate(
                        context,
                        () -> sendRegisterKeyRequest(
                                key.getUuid().toString(),
                                key.getDilithiumParametersType(),
                                key.getPublicKeyString()),
                        "Authenticate to register key"
                ));
                extractButton.setOnClickListener(v -> AuthenticateFingerprint.authenticate(
                        context,
                        () -> extractPrivateKey(key.getUuid().toString()),
                        "Authenticate to extract key"
                ));
                holder.rowLayout.setOnClickListener(v -> {
                    if (expandedLayout.getVisibility() == View.GONE) {
                        expandedLayout.setVisibility(View.VISIBLE);
                    } else {
                        expandedLayout.setVisibility(View.GONE);
                    }
                });
            }
                break;
            case MODE_SIGN: {
                holder.rowLayout.setOnClickListener(v -> AuthenticateFingerprint.authenticate(
                        v.getContext(),
                        () -> {
                            if (keyItemClickListener != null) {
                                keyItemClickListener.onKeyItemClick(key);
                            }
                        },
                        "Authenticate to sign transcript"
                ));
            }
                break;
            case MODE_VERIFY: {
                holder.rowLayout.setOnClickListener(v -> {
                    if (keyItemClickListener != null) {
                        keyItemClickListener.onKeyItemClick(key);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return keyList == null ? 0 : keyList.size();
    }

    public void updateKeyList(List<PublicKeyToStore> newKeyList) {
        this.keyList = newKeyList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView keyAlias, isRegistered, keyId, keyParaType;
        Button btnRegister, btnExtract;
        ConstraintLayout rowLayout, expandedLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            keyAlias = itemView.findViewById(R.id.keyAlias);
            isRegistered = itemView.findViewById(R.id.isRegistered);
            keyId = itemView.findViewById(R.id.keyId);
            keyParaType = itemView.findViewById(R.id.keyParaType);
            btnRegister = itemView.findViewById(R.id.btnRegisterKey);
            btnExtract = itemView.findViewById(R.id.btnExtract);
            rowLayout = itemView.findViewById(R.id.key_row_layout);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
        }
    }
    private void sendRegisterKeyRequest(String uuid, String paraType, String keyString) {
        Log.d("KeyAdapter", uuid + " " + paraType + " " + keyString);
        SignatureApiService signatureApiService = SignatureApiService.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String userId =  sharedPreferences.getString("userId", "defaultId");
        String accessToken =  sharedPreferences.getString("accessToken", "defaultAccessToken");
        RegisterKeyRequest keyRequest = new RegisterKeyRequest(uuid, paraType, keyString);

        signatureApiService.registerKey(userId, "Bearer " + accessToken, keyRequest).enqueue(new Callback<RegisterKeyResponse>() {
            @Override
            public void onResponse(Call<RegisterKeyResponse> call, Response<RegisterKeyResponse> response) {
                if (response.isSuccessful()) {
                    RegisterKeyResponse keyResponse = response.body();
                    // update the ui
                    updateKeyRegistrationStatus(keyResponse.get_id(), keyResponse.isRegistered());
                    Toast.makeText(context, "Register key successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("KeyAdapter", String.valueOf(response.code())); // http status message
                    Toast.makeText(context, "Register key failed", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d("KeyAdapter", response.errorBody().string()); // actual server response message
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (response.code() == 403) { // token is not valid
                        // delete old access token and navigate to login screen
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences .edit();
                        editor.remove("accessToken");
                        editor.apply();
                        ((HomeActivity) context).navigateToLoginScreen();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterKeyResponse> call, Throwable throwable) {
                Log.d("KeyAdapter", "error when register key");
                if (throwable instanceof IOException) {
                    Log.e("KeyAdapter", "Network error or conversion error: " + throwable.getMessage());
                } else {
                    Log.e("KeyAdapter", "Unexpected error: " + throwable.getMessage());
                }
            }
        });
    }
    private void updateKeyRegistrationStatus(String uuid, boolean isRegistered) {
        for (PublicKeyToStore key : keyList) {
            if (key.getUuid().toString().equals(uuid)) {
                key.setRegistered(isRegistered);

                // update the UI (temporary)
                notifyItemChanged(keyList.indexOf(key));

                // update the keystore (actual database)
                FileHelper.updateIsRegisteredField(key, context);

                Log.d("KeyAdapter", "updated key");
                break;
            }
        }
    }
    private void extractPrivateKey(String uuid) {
        // require read/write file permission
        RequirePermission.verifyStoragePermissions((Activity) context);
        List<PrivateKeyToStore> retrievedPrivateKeys = FileHelper.retrievePrivateKeyFromFile(context);
        PrivateKeyToStore returnedPrivateKey = null;

        for (PrivateKeyToStore privateKey : retrievedPrivateKeys) {
            if (privateKey.getUuid().toString().equals(uuid)) {
                returnedPrivateKey = privateKey;
                break;
            }
        }
        byte[] encryptedPrivateKeyByte = returnedPrivateKey.getEncryptedPrivateKey();

        // get the initial dilithium private key
        byte[] initialDilithiumKey = KeystoreHelper.decryptData(encryptedPrivateKeyByte, KeystoreHelper.getPrivateKey());

        // get the string value
        String initialDilithiumKeyString = Base64.getEncoder().encodeToString(initialDilithiumKey);

        String extractedKeyFolderPath = MyConstant.GRADUATION_PROJECT_FOLDER + "/Private Key";
        File customFolder = new File(extractedKeyFolderPath);
        // create private key folder if not exist
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }

        String fileName = returnedPrivateKey.getKeyAlias() + "_" + MyConstant.EXTRACTED_PRIVATE_KEY_FILE_NAME;
        // create file to store private key
        File file = new File(extractedKeyFolderPath, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            // write PEM format
            writer.write("-----BEGIN DILITHIUM PRIVATE KEY-----\n");
            int index = 0;
            while (index < initialDilithiumKeyString.length()) {
                writer.write(initialDilithiumKeyString.substring(index, Math.min(index + 64, initialDilithiumKeyString.length())));
                writer.write("\n");
                index += 64;
            }
            writer.write("-----END DILITHIUM PRIVATE KEY-----");

            // close file
            writer.close();
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "Extracted " + returnedPrivateKey.getKeyAlias() + " successfully" , Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to extract: " + returnedPrivateKey.getKeyAlias() + " key" , Toast.LENGTH_SHORT).show();
            Log.d("KeyAdapter", e.toString());
            e.printStackTrace();
        }
    }
}
