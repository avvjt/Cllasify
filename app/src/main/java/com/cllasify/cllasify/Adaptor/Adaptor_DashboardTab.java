package com.cllasify.cllasify.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cllasify.cllasify.Fragment.DashboardTab.ChatTab;
import com.cllasify.cllasify.Fragment.DashboardTab.DobutTab;

public class Adaptor_DashboardTab extends FragmentPagerAdapter {

    int tabcount;

    public Adaptor_DashboardTab(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new ChatTab();
            case 1 : return new DobutTab();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
