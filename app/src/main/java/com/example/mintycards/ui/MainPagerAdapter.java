package com.example.mintycards.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mintycards.ui.collection.CollectionFragment;
import com.example.mintycards.ui.mintcard.MintCardFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 2;

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MintCardFragment.newInstance();
            case 1:
                return CollectionFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mint Card";
            case 1:
                return "Collection";
        }
        return null;
    }
}
