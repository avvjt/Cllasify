package com.cllasify.cllasify.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpClass;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    TextView schBio, schoolEmail;
    Button join_as_teacher;
    ImageView schoolLogoImg;
    String serverEmail;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#050505"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#eff0f5"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_discover_item);
/*
        recyclerView = findViewById(R.id.class_items_rv);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter_discover_item = new Adapter_Discover_Item(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_discover_item);
*/


//        ImageView iv_ServerDP= findViewById(R.id.iv_ServerDP);
//        TextView tv_ServerBio= findViewById(R.id.tv_ServerBio);
        Button btn_Share = findViewById(R.id.btn_Share);
        TextView tv_ServerName = findViewById(R.id.tv_ServerName);
        RecyclerView rv_ShowClass = findViewById(R.id.rv_ShowClass);
        join_as_teacher = findViewById(R.id.join_as_teacher);
        schoolLogoImg = findViewById(R.id.schoolLogoImg);

        schoolEmail = findViewById(R.id.schoolEmail);


        schBio = findViewById(R.id.schoolBio);


        String groupName = getIntent().getStringExtra("groupName");
        String groupPushId = getIntent().getStringExtra("groupPushId");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String serverBio = snapshot.child(groupPushId).child("ServerBio").getValue(String.class);
                schBio.setText(serverBio);

                if (snapshot.child(groupPushId).child("ServerEmail").exists()) {
                    serverEmail = snapshot.child(groupPushId).child("ServerEmail").getValue(String.class);
                    schoolEmail.setText(serverEmail);
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
            public void JoinGroupClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId, String classPushId, String classReqPosition) {
                sentGroupJoinInvitation(adminGroupID, adminUserName, groupName, groupPushId, subGroupName, classPushId, "StudentJoin");

            }

            @Override
            public void admissionClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String adminEmailId) {
                sentAdmissionRequest(adminGroupID, adminUserName, groupName, groupPushId, subGroupName, adminEmailId);
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

                join_as_teacher.setEnabled(false);

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

                                Log.d("JOIN", "adminGroupID: " + adminGroupID + "\nsubGroupName: "+
                                        "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId);

                                sentGroupJoinInvitation(adminGroupID,adminUserName,groupName,groupPushId,"className","classPushId","TeacherJoin");

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
                String app_url = " https://play.google.com/store/apps/details?id=in.dreamworld.fillformonline";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body + app_url);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });


    }

    private void sentAdmissionRequest(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String adminEmailId) {

        BottomSheetDialog bottomSheetDialogLogin = new BottomSheetDialog(this);
        bottomSheetDialogLogin.setCancelable(false);
        bottomSheetDialogLogin.setContentView(R.layout.btmdialog_admission);
        ImageButton btn_Cancel = bottomSheetDialogLogin.findViewById(R.id.btn_Cancel);
        Button btn_Submit = bottomSheetDialogLogin.findViewById(R.id.btn_Submit);

        TextView teacherName, classNumber;
        teacherName = bottomSheetDialogLogin.findViewById(R.id.teacherName);
        classNumber = bottomSheetDialogLogin.findViewById(R.id.classNumber);

        teacherName.setText("Teacher's name: " + adminUserName);
        classNumber.setText("Class number: " + subGroupName);

        EditText et_name, et_phoneNumber, et_address;
        et_name = bottomSheetDialogLogin.findViewById(R.id.et_name);
        et_phoneNumber = bottomSheetDialogLogin.findViewById(R.id.et_phoneNumber);
        et_address = bottomSheetDialogLogin.findViewById(R.id.et_address);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogLogin.dismiss();
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
                    Toast.makeText(Discover_Item.this, "Enter phoneNumber", Toast.LENGTH_SHORT).show();
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


                    bottomSheetDialogLogin.dismiss();
                }
            }

        });

        bottomSheetDialogLogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialogLogin.show();

    }


    private void sentGroupJoinInvitation(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String classPushId, String JoinStatus) {

        Log.d("JOINTT", "adminGroupID: " + adminGroupID + "\nsubGroupName: " + subGroupName +
                "\nadminUserName: " + adminUserName + "\ngroupName: " + groupName + "\ngroupPushId: " + groupPushId);

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Discover_Item.this);
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Do you want to send Group Joining request to Admin?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                final String userID = currentUser.getUid();
                                final String userName = currentUser.getDisplayName();
                                final String userEmail = currentUser.getEmail();
                                final Uri userPhoto = currentUser.getPhotoUrl();
                                DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(userID);

                                DatabaseReference grpJoiningReqs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPushId).child("groupJoiningReqs");

                                grpJoiningReqs.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                        String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                                        if (JoinStatus.equals("StudentJoin")) {
                                            Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq", classPushId);
                                            grpJoiningReqs.child(pushLong).setValue(userAddComment);
                                            refacceptingReq.child(pushLong).setValue(userAddComment);
                                        }


                                        showGrpClassList.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(groupPushId).child("groupTeacherJoiningReqs");

                                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                        String pushLong = "Joining B Reqno_" + noofQuesinCategory;

                                        if (JoinStatus.equals("TeacherJoin")) {
                                            Class_Group userAddComment = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, pushLong, groupName, groupPushId, subGroupName, "Group_JoiningReq_Teacher", classPushId);
                                            refjoiningReq.child(pushLong).setValue(userAddComment);
                                            refacceptingReq.child(pushLong).setValue(userAddComment);
                                        }


                                        showGrpClassList.notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertdialogbuilder.create();
        alert.show();

    }

}