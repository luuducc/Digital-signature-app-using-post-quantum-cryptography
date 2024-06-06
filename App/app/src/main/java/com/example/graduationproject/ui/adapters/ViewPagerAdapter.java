package com.example.graduationproject.ui.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.data.remote.Transcript;
import com.example.graduationproject.ui.fragments.KeyManagerFragment;
import com.example.graduationproject.ui.fragments.TestFragment;
import com.example.graduationproject.ui.fragments.TranscriptManagerFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Transcript> transcripts;
    private List<PublicKeyToStore> retrievedPublicKeys;

    public ViewPagerAdapter(
            @NonNull FragmentActivity fragmentActivity,
            List<Transcript> transcripts,
            List<PublicKeyToStore> retrievedPublicKeys) {
        super(fragmentActivity);
        this.transcripts = transcripts;
        this.retrievedPublicKeys = retrievedPublicKeys;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return TranscriptManagerFragment.newInstance(transcripts);
            default:
                return KeyManagerFragment.newInstance(retrievedPublicKeys);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}