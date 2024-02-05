package com.example.whattsapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.whattsapp.fragments.CallsFragment;
import com.example.whattsapp.fragments.ChatsFragment;
import com.example.whattsapp.fragments.SettingsFragment;
import com.example.whattsapp.fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 1:
                return new SettingsFragment();
            case 2:
                return new CallsFragment();
            default:
                return new ChatsFragment();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "Chats";
        } else if(position == 1){
            title = "Settings";
        } else if(position == 2){
            title = "Calls";
        }
        return title;
    }
}
