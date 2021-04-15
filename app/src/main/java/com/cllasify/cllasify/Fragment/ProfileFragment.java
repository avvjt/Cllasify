package com.cllasify.cllasify.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cllasify.cllasify.Adaptor.Adaptor_ProfileTab;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileFragment extends Fragment {

    TabLayout tabLayout;
    TabItem tabItem1,tabItem2,tabItem3,tabItem4;
    ViewPager viewPager;
    Adaptor_ProfileTab adaptor_profileTab;
    ChipNavigationBar chipNavigationBar;
    Button btn_EditProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_profile,true);

        tabLayout= view.findViewById(R.id.tablayout1);
        tabItem1= view.findViewById(R.id.HomeTab);
        tabItem2= view.findViewById(R.id.QuestionsTab);
        tabItem3= view.findViewById(R.id.AnswersTab);
        tabItem4= view.findViewById(R.id.AboutTab);
        viewPager= view.findViewById(R.id.vpager);

        btn_EditProfile=view.findViewById(R.id.btn_EditProfile);

        adaptor_profileTab =new Adaptor_ProfileTab(getChildFragmentManager(),tabLayout.getTabCount());
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

        btn_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        return view;
      }
    }
