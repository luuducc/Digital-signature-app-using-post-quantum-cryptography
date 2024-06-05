package com.example.graduationproject;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TranscriptManagerFragment extends Fragment {

    public TranscriptManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transcript_manager, container, false);

//        Button backButton = view.findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> {
//            ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
//            viewPager.setCurrentItem(0);
//        });

        return view;
    }
}