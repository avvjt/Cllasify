package com.cllasify.cllasify.Activities.Server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Create_Subject extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userEmailID, userName;
    String uniPushClassId;
    List<Subject_Details_Model> childItemArrayListClassName;
    List<Class_Group_Names> parentItemArrayListClassName;
    String groupPushId;
    ImageButton btn_Back;

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("justBack")) {
            finish();
        } else {
            showToastBack();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_create_subject);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        childItemArrayListClassName = new ArrayList<>();
        parentItemArrayListClassName = new ArrayList<>();

        if (getIntent().hasExtra("classUniPushId")) {
            uniPushClassId = getIntent().getStringExtra("classUniPushId");
        }

        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
        }

        Button btn_CreateTopic = findViewById(R.id.btn_CreateTopic);
        Button btn_Skip = findViewById(R.id.skipBtn);
        btn_Back = findViewById(R.id.btn_Back);



        btn_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast();
                if (getIntent().hasExtra("justBack")) {
                    finish();
                } else {
                    Intent intent = new Intent(Create_Subject.this, Server_Activity.class);
                    intent.putExtra("stateShimmering","stop");
                    startActivity(intent);
                }
            }
        });

        EditText et_TopicName = findViewById(R.id.et_TopicName);


        btn_CreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showToast();
                if (!et_TopicName.getText().toString().isEmpty()) {

                    saveSubject(et_TopicName.getText().toString(), uniPushClassId, groupPushId);

                    if (getIntent().hasExtra("justBack")) {
                        finish();
                    } else {
                        Intent intent = new Intent(Create_Subject.this, Server_Activity.class);
                        intent.putExtra("stateShimmering","stop");
                        startActivity(intent);
                    }


                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    //Toast for server icon click command
    public void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_server_create_successful,  (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Toast for on backed pressed
    public void showToastBack(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_backpressed,  (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void saveSubject(String topicName, String uniPushClassId, String groupPushId) {

        DatabaseReference getServerTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        getServerTemp.child("subjectName").setValue(topicName);

        getServerTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("SerT", "Temp Server name: " + (snapshot.child("serverName").getValue()));
                Log.d("SerT", "Temp Group Push Id: " + (snapshot.child("tpGroupPushId").getValue()));

                DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);

                String[] pushSubject = String.valueOf(testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(String.valueOf(snapshot.getChildrenCount())).push()).split("/");

                testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("TAG", "showChildGroupAdaptor: ClickedDDS" + dataSnapshot.getKey());
                            Log.d("TAG", "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());
                            Class_Group_Names class_group_names = new Class_Group_Names();
                            class_group_names.setGroupPushId(groupPushId);
                            class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));
                            class_group_names.setClassBio(dataSnapshot.child("classBio").getValue(String.class));
                            class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));


                            testDatabaseReference.child(uniPushClassId).child("classSubjectData").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(pushSubject[9]).child("subjectName").setValue(topicName);
                                    testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(pushSubject[9]).child("subjectUniPushId").setValue(pushSubject[9]);


                                    GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                            new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                            };


                                    if (snapshot.hasChild("classSubjectData")) {
                                        childItemArrayListClassName = snapshot.child("classSubjectData").getValue(genericTypeIndicator);
                                    }
                                    Subject_Details_Model subject_details_model = new Subject_Details_Model();
                                    subject_details_model.setSubjectName(topicName);
                                    subject_details_model.setSubjectUniPushId(pushSubject[9]);
                                    childItemArrayListClassName.add(subject_details_model);

                                    class_group_names.setChildItemList(childItemArrayListClassName);

                                    GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
                                            new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
                                            };

                                    class_group_names.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));


                                    parentItemArrayListClassName.add(class_group_names);

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}