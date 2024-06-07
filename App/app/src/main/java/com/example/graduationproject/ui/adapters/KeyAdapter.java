package com.example.graduationproject.ui.adapters;

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
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.RegisterKeyRequest;
import com.example.graduationproject.data.remote.RegisterKeyResponse;
import com.example.graduationproject.network.services.SignatureApiService;
import com.example.graduationproject.utils.FileHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final String PUBLIC_FILE_NAME = "public.dat";


    public KeyAdapter(Context context, List<PublicKeyToStore> keyList) {
        this.context = context;
        this.keyList = keyList;
        this.expandedStates = new ArrayList<>(Collections.nCopies(keyList.size(), false));
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
        holder.keyId.setText("UUID: " + key.getUuid().toString());
        holder.keyParaType.setText("Type: " + key.getDilithiumParametersType());
        ConstraintLayout expandedLayout = holder.expandedLayout;
        Button registerButton = holder.btnRegister;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterKeyRequest(key.getUuid().toString(), key.getDilithiumParametersType(), key.getPublicKeyString());
            }
        });
        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedLayout.getVisibility() == View.GONE) {
                    expandedLayout.setVisibility(View.VISIBLE);
                } else {
                    expandedLayout.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return keyList.size();
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
    public void updateKeyRegistrationStatus(String uuid, boolean isRegistered) {
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
}
