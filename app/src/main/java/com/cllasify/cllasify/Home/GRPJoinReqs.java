package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Notify;
import com.cllasify.cllasify.Adaptor.JoiningReqVPAdapter;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
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

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GRPJoinReqs.this,Server_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grpjoin_reqs);

        tabLayout = findViewById(R.id.reqTabLayout);
        viewPager = findViewById(R.id.view_Pager);


        tabLayout.setupWithViewPager(viewPager);

        JoiningReqVPAdapter joiningReqVPAdapter = new JoiningReqVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        joiningReqVPAdapter.addFragment(new Student_Joining_Reqs(),"Students");
        joiningReqVPAdapter.addFragment(new Teacher_Joining_Reqs(),"Teachers");

        viewPager.setAdapter(joiningReqVPAdapter);

    }

}