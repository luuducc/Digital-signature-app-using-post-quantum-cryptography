package com.example.keystorelearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keystorelearning.keytostore.PublicKeyToStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Key_RecyclerViewAdapter extends RecyclerView.Adapter<Key_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    public static List<PublicKeyToStore> keyList;
    private List<Boolean> expandedStates;
    public Key_RecyclerViewAdapter(Context context, List<PublicKeyToStore> keyList) {
        this.context = context;
        this.keyList = keyList;
        this.expandedStates = new ArrayList<>(Collections.nCopies(keyList.size(), false));
    }

    public List<PublicKeyToStore> getKeyList() {
        return keyList;
    }

    @NonNull
    @Override
    public Key_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.key_row, parent, false);
        return new Key_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Key_RecyclerViewAdapter.MyViewHolder holder, int position) {
        PublicKeyToStore key = keyList.get(position);
        holder.keyAlias.setText(key.getKeyAlias());
        holder.keyInfo.setText(key.getDilithiumParametersType());

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.isVisibility()) {
                    holder.expandedLayout.setVisibility(View.GONE);
                    key.setVisibility(false);
                } else {
                    holder.expandedLayout.setVisibility(View.VISIBLE);
                    key.setVisibility(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView keyAlias;
        TextView keyInfo;
        ConstraintLayout rowLayout, expandedLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            keyAlias = itemView.findViewById(R.id.keyAlias);
            keyInfo = itemView.findViewById(R.id.keyInfo);
            rowLayout = itemView.findViewById(R.id.recycler_view_item);
            expandedLayout = itemView.findViewById(R.id.expanded_layout);
            expandedLayout.setVisibility(View.GONE);
        }
    }
 }
