package com.cllasify.cllasify.Activities.Server;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adaptor_Student_Result;
import com.cllasify.cllasify.ModelClasses.Class_Result_Info;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result_Students extends AppCompatActivity {

    //    String currUserID;
    RecyclerView rv_showStudents;
    TextView tv_studentList, tv_className;
    ImageButton btn_Back;
    //Students
    Adaptor_Student_Result showGrpMemberList;
    List<Class_Student_Details> listGrpMemberList;
    private BroadcastReceiver broadcastReceiver;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    private void checkOnlineStatus(String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        setStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userStatus").exists()) {
                    setStatus.child("userStatus").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_student_result);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

//        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

        tv_studentList = findViewById(R.id.studentListText);
        btn_Back = findViewById(R.id.btn_Back);
        tv_className = findViewById(R.id.classTextView);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            rv_showStudents = findViewById(R.id.studentList);
            listGrpMemberList = new ArrayList<>();
            showGrpMemberList = new Adaptor_Student_Result(Result_Students.this);
            showGrpMemberList.setOnItemClickListener(new Adaptor_Student_Result.OnItemClickListener() {
                @Override
                public void clickedStudent(String userID, String userName, String position) {

                    Intent intent = new Intent(Result_Students.this, Result_Subjects.class);
                    intent.putExtra("uniGroupPushId", uniGrpPushId);
                    intent.putExtra("uniClassPushId", uniClassPushId);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);
                    intent.putExtra("position", position);
                    startActivity(intent);


                    DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result")
                            .child(uniGrpPushId).child(uniClassPushId).child(userID);


                    resDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                Log.d("SUBCHK", "onDataChange: " + snapshot.getKey());

                            } else {

                                DatabaseReference setSubs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                                        .child(uniGrpPushId).child(uniClassPushId).child("classSubjectData");

                                setSubs.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            Log.d("SUBCHK", "onDataChange: not present" + dataSnapshot.getChildrenCount());

                                            String subVal = dataSnapshot.child("subjectName").getValue().toString();

                                            Class_Result_Info class_result_info = new Class_Result_Info(0, 0, 0, 0, 0, 0, subVal, "");
//
//                                            Class_Result class_result = new Class_Result(userName, "", 0, class_result_info);

                                            resDb.child("username").setValue(userName);
                                            resDb.child("totalMarks").setValue(0);
                                            resDb.child("totalGrades").setValue("");
                                            resDb.child("totalFullMarks").setValue(0);
                                            resDb.child("subjectMarksInfo").child(Objects.requireNonNull(dataSnapshot.getKey())).setValue(class_result_info);


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            });

            rv_showStudents.setLayoutManager(new LinearLayoutManager(Result_Students.this));


            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String className = Objects.requireNonNull(snapshot.child("className").getValue()).toString();

                    tv_className.setText(className);

                    listGrpMemberList.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                        Log.d("CHKINGSTUD", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKINGSTUD", "onDataChange: " + object.getUserId());
                        listGrpMemberList.add(object);

                    }

                    showGrpMemberList.setClassStudents(listGrpMemberList);
                    rv_showStudents.setAdapter(showGrpMemberList);
                    showGrpMemberList.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}