package com.example.graduationproject.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.graduationproject.KeyManagerFragment;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.ui.fragments.TranscriptManagerFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Transcript> transcripts;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Transcript> transcripts) {
        super(fragmentActivity);
        this.transcripts = transcripts;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new KeyManagerFragment();
            case 1:
                return TranscriptManagerFragment.newInstance(transcripts);
            default:
                return new KeyManagerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}