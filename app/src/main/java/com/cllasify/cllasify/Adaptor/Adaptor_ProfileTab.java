package com.cllasify.cllasify.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cllasify.cllasify.Fragment.AboutTab;
import com.cllasify.cllasify.Fragment.AnswersTab;
import com.cllasify.cllasify.Fragment.HomeTab;
import com.cllasify.cllasify.Fragment.QuestionsTab;


public class Adaptor_ProfileTab extends FragmentPagerAdapter
{
    int tabcount;

    public Adaptor_ProfileTab(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new HomeTab();
            case 1 : return new QuestionsTab();
            case 2 : return new AnswersTab();
            case 3 : return new AboutTab();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}

