package com.cllasify.cllasify.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.cllasify.cllasify.ProfileMine.UnderSocial.FriendsTab;
import com.cllasify.cllasify.ProfileMine.UnderSocial.RequestTab;

public class Adaptor_SocialTab extends FragmentPagerAdapter {
    int tabcount;

    public Adaptor_SocialTab(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendsTab();
            case 1:
                return new RequestTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
