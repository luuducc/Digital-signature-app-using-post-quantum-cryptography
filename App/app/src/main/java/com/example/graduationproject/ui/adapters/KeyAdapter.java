package com.example.graduationproject.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.data.local.PublicKeyToStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.MyViewHolder> {
    Context context;
    List<PublicKeyToStore> keyList;
    List<Boolean> expandedStates;

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
            btnRegister = itemView.findViewById(R.id.btnRegister);
            btnExtract = itemView.findViewById(R.id.btnExtract);
            rowLayout = itemView.findViewById(R.id.key_row_layout);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
        }
    }
}
