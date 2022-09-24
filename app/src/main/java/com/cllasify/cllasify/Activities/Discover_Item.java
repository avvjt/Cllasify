package com.cllasify.cllasify.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpClass;
import com.cllasify.cllasify.ModelClasses.Class_Admission;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
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

public class Discover_Item extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    /*
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Adapter_Discover_Item adapter_discover_item;
    */

    Adaptor_ShowGrpClass showGrpClassList;
    List<Class_Group_Names> listGrpClassList;
    DatabaseReference refGroupClassList;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    TextView schBio, schoolEmail, tv_ServerName, numbStudents, numbTeachers;
    Button join_as_teacher;
    ImageView schoolLogoImg, btn_Back;
    String serverEmail;
    LinearLayout emailLayout;
    LinearLayout bioLayout;
    TextView studentCount, teacherCount;
    String userID, userName, userEmail;
    FirebaseUser currentUser;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#050505"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#eff0f5"));

                break;


        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Discover_Item.this, Discover_Activity.class);
        startActivity(intent);

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
        setContentView(R.layout.activity_explore_item);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();


        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
/*
        recyclerView = findViewById(R.id.class_items_rv);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter_discover_item = new Adapter_Discover_Item(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_discover_item);
*/


//        ImageView iv_ServerDP= findViewById(R.id.iv_ServerDP);
//        TextView tv_ServerBio= findViewById(R.id.tv_ServerBio);

        btn_Back = findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Discover_Item.this, Discover_Activity.class);
                startActivity(intent);

                /*Code shared animation*/

                /*Pair[] pair = new Pair[2];
                pair[0] = new Pair<View,String>(schoolLogoImg , "pic_shared");
                pair[1] = new Pair<View,String>(tv_ServerName , "name_shared");
                pair[2] = new Pair<View,String>(studentCount , "student_no_shared");
                pair[3] = new Pair<View,String>(teacherCount , "teacher_no_shared");
                pair[4] = new Pair<View,String>(numbStudents , "student_shared");
                pair[4] = new Pair<View,String>(numbTeachers , "teacher_shared");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Discover_Item.this, pair);

                Intent i = new Intent(Discover_Item.this, Discover_Activity.class);
                startActivity(i, options.toBundle());*/
            }
        });
        Button btn_Share = findViewById(R.id.btn_Share);
        tv_ServerName = findViewById(R.id.schoolName);
        RecyclerView rv_ShowClass = findViewById(R.id.rv_ShowClass);
        join_as_teacher = findViewById(R.id.join_as_teacher);
        schoolLogoImg = findViewById(R.id.schoolLogoImg);

        studentCount = findViewById(R.id.numbStudentsInt);
        teacherCount = findViewById(R.id.numbTeachersInt);

        numbStudents = findViewById(R.id.numbStudents);
        numbTeachers = findViewById(R.id.numbTeachers);

        emailLayout = findViewById(R.id.ll_email);
        bioLayout = findViewById(R.id.ll_bio);

        schoolEmail = findViewById(R.id.schoolEmail);


        schBio = findViewById(R.id.schoolBio);


        String groupName = getIntent().getStringExtra("groupName");
        String groupPushId = getIntent().getStringExtra("groupPushId");


        DatabaseReference userNotiCheck = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(userID).child(groupPushId).child("classPushId");

        userNotiCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("joiningStatus").exists()) {
                        if (snapshot.child("joiningStatus").getValue().equals("req_sent")) {
                            join_as_teacher.setText("Requested");
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Approve")) {
                            join_as_teacher.setText("Joined");
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Reject")) {
                            join_as_teacher.setText("Join as a teacher");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference referenceALLGroup = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("All_GRPs").child(groupPushId);


        referenceALLGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSize = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.exists()) {

                        totalSize += dataSnapshot.child("classStudentList").getChildrenCount();
                    }
                }

                studentCount.setText(String.valueOf(totalSize));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference referenceALLGroupTeachers = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList");


        referenceALLGroupTeachers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(groupPushId).child("ServerBio").exists()) {
                    bioLayout.setVisibility(View.VISIBLE);
                    String serverBio = snapshot.child(groupPushId).child("ServerBio").getValue(String.class);
                    schBio.setText(serverBio);
                }
                if (!(snapshot.exists()) || snapshot.getValue().toString().equals("")) {
                    bioLayout.setVisibility(View.GONE);
                }
                if (snapshot.child(groupPushId).child("ServerEmail").exists()) {
                    emailLayout.setVisibility(View.VISIBLE);
                    serverEmail = snapshot.child(groupPushId).child("ServerEmail").getValue(String.class);
                    schoolEmail.setText(serverEmail);
                }
                if (!(snapshot.exists()) || snapshot.getValue().toString().equals("")) {
                    emailLayout.setVisibility(View.GONE);
                }
                if (snapshot.child(groupPushId).child("serverProfilePic").exists()) {
                    String serverLogo = snapshot.child(groupPushId).child("serverProfilePic").getValue(String.class);
                    Glide.with(Discover_Item.this).load(serverLogo).into(schoolLogoImg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tv_ServerName.setText(groupName);
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Discover_Item.this));

        listGrpClassList = new ArrayList<>();
        showGrpClassList = new Adaptor_ShowGrpClass(Discover_Item.this, listGrpClassList);
        rv_ShowClass.setAdapter(showGrpClassList);

        refGroupClassList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
        listGrpClassList.clear();


        showGrpClassList.setOnItemClickListener(new Adaptor_ShowGrpClass.OnItemClickListener() {

            @Override
            public void joinAndAdmission(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId, String classPushId, String classReqPosition) {
                joinAdmissionBS(adminGroupID, adminUserName, groupName, groupPushId, subGroupName, pushId, classPushId, classReqPosition);
            }


        });

        refGroupClassList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);fatten
////                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
////                listView.setAdapter(arrayAdapter);

                Class_Group_Names class_userDashBoard = snapshot.getValue(Class_Group_Names.class);
                Log.d("CLLGRP", "onChildAdded: " + snapshot.child("classUniPushId"));
                class_userDashBoard.setUniPushClassId(String.valueOf(snapshot.child("classUniPushId").getValue()));
                listGrpClassList.add(class_userDashBoard);


                showGrpClassList.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        });

        join_as_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                assert currentUser != null;
                String userID = currentUser.getUid();

                DatabaseReference databaseReferenceGetPush = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                databaseReferenceGetPush.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String groupPushId = snapshot.child("clickedJoinSearchGroup").getValue().toString();


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                                .child(groupPushId);

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String adminGroupID = snapshot.child("userId").getValue().toString();
                                String adminUserName = snapshot.child("userName").getValue().toString();
                                String groupName = snapshot.child("groupName").getValue().toString();

                                Log.d("JOIN", "adminGroupID: " + adminGroupID + "\nsubGroupName: " +
                                        "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId);

                                sentGroupJoinInvitation(adminGroupID, adminUserName, groupName, groupPushId, "className", "classPushId", "TeacherJoin");

                                String userID = currentUser.getUid();

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
        });

        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String body = "Cllasify is the best App to Discuss Study Material with Classmates using Servers," +
                        "\nPlease Click on Below Link to Install:";
                String subject = "Install Classify App";
                String app_url = " https://play.google.com/store/apps/details?id=com.cllasify.cllasify";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body + app_url);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });


    }

    private void joinAdmissionBS(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId, String classPushId, String classReqPosition) {

        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_join_admission);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_join = bottomSheetDialog.findViewById(R.id.btn_join);
        Button btn_Admission = bottomSheetDialog.findViewById(R.id.btn_Admission);
        NestedScrollView ll_Admission = bottomSheetDialog.findViewById(R.id.ll_admission);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentGroupJoinInvitation(adminGroupID, adminUserName, groupName, groupPushId, "className", classPushId, "StudentJoin");
            }
        });

        btn_Admission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPushId);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!(snapshot.child("admissionFees").getValue().toString().equals("₹0"))) {

                            Intent intent = new Intent(Discover_Item.this, Admission_Structure.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("admissionFee", snapshot.child("admissionFees").getValue().toString());
                            intent.putExtra("groupPushId", groupPushId);
                            intent.putExtra("classPushId", classPushId);
                            intent.putExtra("dateTimeCC", dateTimeCC);
                            intent.putExtra("adminGroupID", adminGroupID);
                            intent.putExtra("userEmail", userEmail);
                            intent.putExtra("subGroupName", subGroupName);
                            intent.putExtra("userName", userName);
                            intent.putExtra("groupName", groupName);

                            startActivity(intent);

                        } else {
                            Toast.makeText(Discover_Item.this, "Admission Fee haven't set yet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                sentGroupJoinInvitation(adminGroupID, adminUserName, groupName, groupPushId, "className", classPushId, "StudentAdmission");
            }
        });

        DatabaseReference userNotiCheck = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(userID).child(groupPushId).child(classPushId);

        userNotiCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("joiningStatus").exists()) {
                        if (snapshot.child("joiningStatus").getValue().equals("admission_req_sent")) {
                            btn_Admission.setText("In Progress");
//                            join_as_teacher.setEnabled(false);
                            btn_Admission.setEnabled(false);
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("req_sent")) {
                            btn_join.setText("Requested");
//                            join_as_teacher.setEnabled(false);
//                            btn_Admission.setEnabled(false);
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Approve")) {
                            btn_join.setText("Joined");
//                            join_as_teacher.setEnabled(false);
//                            btn_Admission.setEnabled(false);

                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Reject")) {
                            btn_join.setText("Join");
//                            join_as_teacher.setEnabled(true);
//                            btn_Admission.setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        moveTaskToBack(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    // BottomDialog for admission request
    private void sentAdmissionRequest(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String adminEmailId) {

        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_admission);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Cancel, btn_Submit;

        btn_Cancel = bottomSheetDialog.findViewById(R.id.btn_Cancel);
        btn_Submit = bottomSheetDialog.findViewById(R.id.btn_Submit);

        TextView classNumber = bottomSheetDialog.findViewById(R.id.classNumber);
        TextView serverName = bottomSheetDialog.findViewById(R.id.serverName);

        serverName.setText("Admission from");
        classNumber.setText(groupName + " - " + subGroupName);

        EditText et_name, et_phoneNumber, et_address;
        et_name = bottomSheetDialog.findViewById(R.id.et_name);
        et_phoneNumber = bottomSheetDialog.findViewById(R.id.et_phoneNumber);
        et_address = bottomSheetDialog.findViewById(R.id.et_address);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_name.getText().toString().isEmpty()) {
                    Toast.makeText(Discover_Item.this, "Enter name", Toast.LENGTH_SHORT).show();
                    et_name.setError("Enter name");
                }
                if (et_phoneNumber.getText().toString().isEmpty()) {
//                    Toast.makeText(Discover_Item.this, "Enter phoneNumber", Toast.LENGTH_SHORT).show();
                    et_phoneNumber.setError("Enter phoneNumber");
                }
                if (et_address.getText().toString().isEmpty()) {
                    Toast.makeText(Discover_Item.this, "Enter address", Toast.LENGTH_SHORT).show();
                    et_address.setError("Enter address");
                } else {

                    String name = "Name : " + et_name.getText().toString().trim();
                    String phoneNumber = "Phone Number : " + et_phoneNumber.getText().toString().trim();
                    String address = "Address : " + et_address.getText().toString().trim();

                    String together = name + "\n" + phoneNumber + "\n" + address;

                    String subject = "Admission request from " + name + " to " + adminUserName;
                    String gmailPackage = "com.google.android.gm";

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setPackage(gmailPackage);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{adminEmailId});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, together);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));


                    bottomSheetDialog.dismiss();
                }
            }

        });

        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        moveTaskToBack(false);

    }

    //for student joining request toast
    public void showToastStudent() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_student_req, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //for teacher joining request toast
    public void showToastTeacher() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_teacher_req, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    private void sentGroupJoinInvitation(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String classPushId, String JoinStatus) {

        Log.d("JOINTT", "adminGroupID: " + adminGroupID + "\nsubGroupName: " + subGroupName +
                "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId);

        DatabaseReference userNoti = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(userID).child(groupPushId).child(classPushId);
        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(groupPushId).child("groupTeacherJoiningReqs");
        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(userID);
        DatabaseReference grpJoiningReqs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPushId).child("groupJoiningReqs");

        DatabaseReference userNotiCheck = FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications").child(userID).child(groupPushId).child(classPushId);
        userNotiCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("joiningStatus").exists()) {
                        if (snapshot.child("joiningStatus").getValue().equals("req_sent")) {
                            Toast.makeText(Discover_Item.this, "Your joining request has been sent please wait...", Toast.LENGTH_SHORT).show();
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Approve")) {
                            Toast.makeText(Discover_Item.this, "Your joining request has been approved", Toast.LENGTH_SHORT).show();
                        }
                        if (snapshot.child("joiningStatus").getValue().equals("Reject") || (snapshot.child("joiningStatus").getValue().equals("admission_req_sent"))) {

                            refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                    String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                                    if (JoinStatus.equals("TeacherJoin")) {
                                        Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq_Teacher", classPushId);
                                        refjoiningReq.child(pushLong).setValue(userAddComment);
                                        refacceptingReq.child(pushLong).setValue(userAddComment);
                                        userNoti.child("notificationPushId").setValue(pushLong);
                                        userNoti.child("joiningStatus").setValue("req_sent");
                                        showToastTeacher();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });


                            grpJoiningReqs.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                    String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                                    if (JoinStatus.equals("StudentJoin")) {
                                        Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq", classPushId);
                                        grpJoiningReqs.child(pushLong).setValue(userAddComment);
                                        refacceptingReq.child(pushLong).setValue(userAddComment);
                                        userNoti.child("notificationPushId").setValue(pushLong);
                                        userNoti.child("joiningStatus").setValue("req_sent");
                                        showToastStudent();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
                    }

                } else {


                    refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                            String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                            if (JoinStatus.equals("TeacherJoin")) {
                                Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq_Teacher", classPushId);
                                refjoiningReq.child(pushLong).setValue(userAddComment);
                                refacceptingReq.child(pushLong).setValue(userAddComment);
                                userNoti.child("notificationPushId").setValue(pushLong);
                                userNoti.child("joiningStatus").setValue("req_sent");
                                userNoti.child("notificationPushId").setValue(pushLong);
                                userNoti.child("joiningStatus").setValue("req_sent");
                                showToastTeacher();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    grpJoiningReqs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                            String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                            if (JoinStatus.equals("StudentJoin")) {
                                Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq", classPushId);
                                grpJoiningReqs.child(pushLong).setValue(userAddComment);
                                refacceptingReq.child(pushLong).setValue(userAddComment);
                                userNoti.child("notificationPushId").setValue(pushLong);
                                userNoti.child("joiningStatus").setValue("req_sent");
                                showToastStudent();
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

}