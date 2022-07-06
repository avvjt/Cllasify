
package com.cllasify.cllasify.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cllasify.cllasify.Adapters.Adaptor_Attendance;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Attendance_History extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    String currUserId,groupPushId,subGroupPushId,classPushId,currentDate;
    List<Class_Group> list_showAttend;
    Adaptor_Attendance showAttendanceStatus;

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
        setContentView(R.layout.activity_attendance_history);


        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        currentDate = getIntent().getStringExtra("currentDate");
        groupPushId = getIntent().getStringExtra("groupPushId");
        subGroupPushId = getIntent().getStringExtra("subGroupPushId");
        classPushId = getIntent().getStringExtra("classPushId");

        RecyclerView rv_ShowAttend=findViewById(R.id.rv_ShowAttend);
        TextView tv_titleAttendance=findViewById(R.id.tv_titleAttendance);
        ImageButton btn_Back = findViewById(R.id.btn_Back);
        rv_ShowAttend.setLayoutManager(new LinearLayoutManager(this));
        list_showAttend = new ArrayList<>();
        showAttendanceStatus = new Adaptor_Attendance(this, list_showAttend);
        rv_ShowAttend.setAdapter(showAttendanceStatus);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_titleAttendance.setText("Attendance "+currentDate+"\n");

        DatabaseReference refChildGroup1 = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance").child(groupPushId).child(subGroupPushId).child(classPushId).child(currentDate);
        ChildEventListener childEventListenerAttend= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
                list_showAttend.add(class_userDashBoard);
                showAttendanceStatus.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                myArrayAdaptor.notifyDataSetChanged();
                showAttendanceStatus.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refChildGroup1.orderByChild("userName").addChildEventListener(childEventListenerAttend);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}