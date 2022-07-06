package com.cllasify.cllasify.Activities.Server;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Create_Class extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;


    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userEmailID, userName, groupName;

    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    private DatabaseReference refAllGroup, refGroupSubsList, refuserAllGroup, refuserPersonalGroup, refuserPublicGroup, addedOrJoinedGroup;
    Class_Group userAddGroupClass, userSubsGroupClass;
    ProgressBar progressBar;
    ImageButton btn_Back;
    StorageReference storageReference;


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
        setContentView(R.layout.activity_create_class);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        progressBar = findViewById(R.id.middleProgress);
        btn_Back = findViewById(R.id.btn_Back);


        if (getIntent().hasExtra("GroupName")) {
            groupName = getIntent().getStringExtra("GroupName");
        }


        EditText et_ClassName = findViewById(R.id.et_ClassName);
        Button btn_nextAddTopic = findViewById(R.id.btn_nextAddTopic);

        btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classGroupName = et_ClassName.getText().toString().trim();
                if (classGroupName.isEmpty()) {
//                    Toast.makeText(Create_Class.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    et_ClassName.setError("Enter Class Name");
                } else {

                    btn_nextAddTopic.setClickable(false);

                    progressBar.setVisibility(View.VISIBLE);

                    if (getIntent().hasExtra("groupPushId")) {
                        saveClassGroup(groupName, getIntent().getStringExtra("groupPushId"), classGroupName);
                    } else {
                        saveGroup(groupName, classGroupName);
                    }
                }
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void saveGroup(String GroupName, String classGroupName) {

        DatabaseReference referenceALLPBLCGroup = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("All_Universal_Group");

        DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        changeCOor.child("serverName").setValue(GroupName);

        referenceALLPBLCGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long noofGroupinCategory = snapshot.getChildrenCount();
                String push = "Uni_Group_No_" + noofGroupinCategory + "_" + GroupName;
                changeCOor.child("groupPushId").setValue(push);


                refAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push);
                refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push).child("User_Subscribed_Groups").child(userID);

                refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
                refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
                refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
                addedOrJoinedGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID).child(push);

                userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, GroupName, "Public", noofGroupinCategory);
                userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, GroupName, "Public", noofGroupinCategory);
                userSubsGroupClass = new Class_Group(dateTimeCC, userName, userID, userID, GroupName, push, "Admin", "Online");


//                DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId);
//                refSaveServerProfPic.child("serverProfilePic").setValue(uri.toString());

                ///Random DP/1.jpg
                String[] strArr = {"1", "2", "3", "4", "5"};
                Random rand = new Random();
                int res = rand.nextInt(strArr.length);

                storageReference = FirebaseStorage.getInstance().getReference();

                final StorageReference fileRef = storageReference.child("Random DP/" + strArr[res] + ".jpg");

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("RANDOMPIC", "onDataChange: " + uri);
                        DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push);
                        refSaveServerProfPic.child("serverProfilePic").setValue(uri.toString());
                    }
                });


                refuserPublicGroup.setValue(userAddGroupClass);
                refAllGroup.setValue(userAddGroupClass);
                refGroupSubsList.setValue(userSubsGroupClass);
                refuserAllGroup.setValue(userAddGroupClass);
                addedOrJoinedGroup.child("addedOrJoined").setValue("Added");
                addedOrJoinedGroup.child("dateTime").setValue(dateTimeCC);

                saveClassGroup(GroupName, push, classGroupName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //Toast.makeText(Create_Class.this, "Server Successfully Created", Toast.LENGTH_SHORT).show();
    }

    private void saveClassGroup(String serverName, String groupPushId, String sbChildGroupName) {

        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

        testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference getServerTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                getServerTemp.child("className").setValue(sbChildGroupName);

                DatabaseReference setAdmins = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins");

                getServerTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.d("SerT", "Temp Server name: " + serverName);
                        Log.d("SerT", "Temp Group Push Id: " + (snapshot.child("tpGroupPushId").getValue()));
                        Log.d("SerT", "Group Push Id: " + groupPushId);
                        Log.d("SerT", "Group name: " + sbChildGroupName);


                        testDatabaseReference.child(groupPushId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String[] push01 = String.valueOf(testDatabaseReference.child(groupPushId).push()).split("/");

                                testDatabaseReference.child(groupPushId).child(push01[6]).child("className").setValue(sbChildGroupName);
//                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classBio").setValue(" ");
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classUniPushId").setValue(push01[6]);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("groupPushId").setValue(groupPushId);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot01) {
                                        Class_Student_Details class_student_details = new Class_Student_Details(true, userID, userName);
                                        setAdmins.child(groupPushId).child("classAdminList")
                                                .child(userID).setValue(class_student_details);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Intent intent = new Intent(Create_Class.this, Create_Subject.class);
                                intent.putExtra("classUniPushId", push01[6]);
                                intent.putExtra("groupPushId", groupPushId);
                                if (getIntent().hasExtra("justBackServSetting")) {
                                    intent.putExtra("justBackServSetting", true);
                                }
                                startActivity(intent);

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
                /*

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}