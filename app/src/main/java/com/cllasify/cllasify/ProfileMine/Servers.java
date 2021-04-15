package com.cllasify.cllasify.ProfileMine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.cllasify.cllasify.Adaptor.Adaptor_ServersTab;
import com.cllasify.cllasify.Adaptor.Adaptor_SocialTab;
import com.cllasify.cllasify.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Servers extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabItem1,tabItem2;
    ViewPager viewPager;
    Adaptor_ServersTab adaptor_serversTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);


        tabLayout= findViewById(R.id.tablayout1);
        tabItem1= findViewById(R.id.serversTab);
        tabItem2= findViewById(R.id.requestTab);
        viewPager= findViewById(R.id.vpager);

        adaptor_serversTab =new Adaptor_ServersTab(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adaptor_serversTab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1)
                    adaptor_serversTab.notifyDataSetChanged();
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