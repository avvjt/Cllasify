package com.cllasify.cllasify.ProfileMine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.cllasify.cllasify.Adaptor.Adaptor_SocialTab;
import com.cllasify.cllasify.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Social extends AppCompatActivity {



    TabLayout tabLayout;
    TabItem tabItem1,tabItem2;
    ViewPager viewPager;
    Adaptor_SocialTab adaptor_socialTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);


        tabLayout= findViewById(R.id.tabLayout);
        tabItem1= findViewById(R.id.friendsTab);
        tabItem2= findViewById(R.id.requestTab);
        viewPager= findViewById(R.id.viewPager);

        adaptor_socialTab =new Adaptor_SocialTab(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adaptor_socialTab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1)
                    adaptor_socialTab.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //listen for scroll or page change
    }
}