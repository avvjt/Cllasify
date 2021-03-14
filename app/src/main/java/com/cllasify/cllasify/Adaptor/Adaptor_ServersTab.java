package com.cllasify.cllasify.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.cllasify.cllasify.Fragment.ServersRequestTab;
import com.cllasify.cllasify.Fragment.ServersTab;

public class Adaptor_ServersTab extends FragmentPagerAdapter {
    int tabcount;

    public Adaptor_ServersTab(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ServersTab();
            case 1:
                return new ServersRequestTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
