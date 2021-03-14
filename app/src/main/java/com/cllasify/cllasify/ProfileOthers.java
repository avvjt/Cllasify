package com.cllasify.cllasify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.cllasify.cllasify.Adaptor.Adaptor_ProfileTab;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProfileOthers extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabItem1,tabItem2,tabItem3,tabItem4;
    ViewPager viewPager;
    Adaptor_ProfileTab adaptor_profileTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        tabLayout= findViewById(R.id.tablayout1);
        tabItem1= findViewById(R.id.HomeTab);
        tabItem2= findViewById(R.id.QuestionsTab);
        tabItem3= findViewById(R.id.AnswersTab);
        tabItem4= findViewById(R.id.AboutTab);
        viewPager= findViewById(R.id.vpager);

        adaptor_profileTab =new Adaptor_ProfileTab(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adaptor_profileTab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2 || tab.getPosition()==3)
                    adaptor_profileTab.notifyDataSetChanged();
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