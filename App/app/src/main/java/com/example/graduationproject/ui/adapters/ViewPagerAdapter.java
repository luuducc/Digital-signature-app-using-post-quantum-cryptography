package com.example.graduationproject.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.graduationproject.KeyManagerFragment;
import com.example.graduationproject.TranscriptManagerFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new KeyManagerFragment();
            case 1:
                return new TranscriptManagerFragment();
            default:
                return new KeyManagerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}