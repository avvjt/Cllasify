package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Notify;
import com.cllasify.cllasify.Adaptor.JoiningReqVPAdapter;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Service.NetworkBroadcast;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GRPJoinReqs extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;


    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton btn_back;

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_grpjoin_reqs);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        tabLayout = findViewById(R.id.reqTabLayout);
        viewPager = findViewById(R.id.view_Pager);
        btn_back = findViewById(R.id.btn_Back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tabLayout.setupWithViewPager(viewPager);

        JoiningReqVPAdapter joiningReqVPAdapter = new JoiningReqVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        joiningReqVPAdapter.addFragment(new Student_Joining_Reqs(),"Students");
        joiningReqVPAdapter.addFragment(new Teacher_Joining_Reqs(),"Teachers");

        viewPager.setAdapter(joiningReqVPAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}