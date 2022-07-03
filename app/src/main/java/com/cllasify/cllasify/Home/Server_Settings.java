package com.cllasify.cllasify.Home;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adaptor_Server_Setting_Items;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Server_Setting_Specifics;
import com.cllasify.cllasify.Service.NetworkBroadcast;
import com.cllasify.cllasify.Subject_Details_Model;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Server_Settings extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;


    DatabaseReference refGroupClassList, getTempData;
    String currUserId;
    RecyclerView rv_ShowClass;
    String groupPushId, serverBio, serverName, serverEmail;
    ImageButton serverSettingProfile, btn_back;

    Adaptor_Server_Setting_Items showGrpClassList;
    List<Class_Group_Names> listGrpClassList;

    TextView tv_ServerName, schoolBio, schoolEmail;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userName;


    ImageView schoolLogoImg;
    Button addNewClass;

    LinearLayout emailLayout;
    LinearLayout bioLayout;

    TextView studentCount, teacherCount;

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
        setContentView(R.layout.activity_server_settings);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
            userName = currentUser.getDisplayName();
        }

        emailLayout = findViewById(R.id.ll_email);
        bioLayout = findViewById(R.id.ll_bio);

        studentCount = findViewById(R.id.numbStudentsInt);
        teacherCount = findViewById(R.id.numbTeachersInt);

        schoolLogoImg = findViewById(R.id.schoolLogoImg);
        addNewClass = findViewById(R.id.addNewClass);
        btn_back = findViewById(R.id.btn_Back);

        rv_ShowClass = findViewById(R.id.rv_ShowClass);
        tv_ServerName = findViewById(R.id.tv_ServerName);
        schoolBio = findViewById(R.id.schoolBio);
        schoolEmail = findViewById(R.id.schoolEmail);
        listGrpClassList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Server_Settings.this));
        showGrpClassList = new Adaptor_Server_Setting_Items(Server_Settings.this, listGrpClassList);

        serverSettingProfile = findViewById(R.id.serverSettingProfile);

        groupPushId = getIntent().getStringExtra("groupPushId");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("serverProfilePic");
        refSaveServerProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!(Server_Settings.this).isFinishing()) {
                        Log.d("SERVIMG", "onDataChange: " + snapshot.getValue());
                        Glide.with(Server_Settings.this).load(snapshot.getValue()).into(schoolLogoImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (getIntent().hasExtra("currUserId")) {
            currUserId = getIntent().getStringExtra("currUserId");
            serverSettingProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Server_Settings.this,schoolLogoImg,"pic_shared");

                    Intent intent = new Intent(Server_Settings.this, Server_Setting_Specifics.class);
                    intent.putExtra("currUserId", currUserId);
                    intent.putExtra("groupPushId", groupPushId);

                    startActivity(intent, options.toBundle());
                }
            });
        }


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

        databaseReference.child(groupPushId).child("groupName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    serverName = snapshot.getValue(String.class);
                    tv_ServerName.setText(serverName);



                    addNewClass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Server_Settings.this,Create_Class.class);
                            intent.putExtra("GroupName",serverName);
                            intent.putExtra("groupPushId",groupPushId);
                            startActivity(intent);

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(groupPushId).child("ServerEmail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("SCHEMAIL", "onDataChange: "+snapshot.getValue());
                    emailLayout.setVisibility(View.VISIBLE);
                    serverEmail = snapshot.getValue(String.class);
                    schoolEmail.setText(serverEmail);
                }if(!(snapshot.exists()) || snapshot.getValue().toString().equals("")){
                    emailLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(groupPushId).child("ServerBio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bioLayout.setVisibility(View.VISIBLE);
                    serverBio = snapshot.getValue(String.class);
                    schoolBio.setText(serverBio);
                }if(!(snapshot.exists()) || snapshot.getValue().toString().equals("")){
                    bioLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //GroupClassList..
        refGroupClassList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId);

        refGroupClassList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGrpClassList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Class_Group_Names class_userDashBoard = new Class_Group_Names();

                    Log.d("REFCHK", "onDataChange: " + snapshot.getKey());

                    class_userDashBoard.setClassName(String.valueOf(snapshot.child("className").getValue()));
                    class_userDashBoard.setUniPushClassId(String.valueOf(snapshot.child("classUniPushId").getValue()));
                    class_userDashBoard.setGroupPushId(String.valueOf(snapshot.child("groupPushId").getValue()));

                    List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                        subjectDetailsModelList.add(object);

                    }

                    class_userDashBoard.setChildItemList(subjectDetailsModelList);


                    List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKSUB", "onDataChange: " + class_student_details.getUserName());
                        class_student_detailsList.add(class_student_details);

                    }

                    class_userDashBoard.setClass_student_detailsList(class_student_detailsList);

//                        GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
//                                new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
//                                };
//
//                        class_userDashBoard.setChildItemList(snapshot.child("classSubjectData").getValue(genericTypeIndicator));

//
//                        GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
//                                new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
//                                };
//
//                        class_userDashBoard.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));

                    listGrpClassList.add(class_userDashBoard);


                    showGrpClassList.setOnItemClickListener(new Adaptor_Server_Setting_Items.OnItemClickListener() {
                        @Override
                        public void onClassDeleteBtn(String classPosition) {

                            delClass(groupPushId, classPosition);

                        }

                        @Override
                        public void onClassRenameBtn(String className, String groupPushId, String uniClassPushId) {

                            View customAlertDialog = LayoutInflater.from(Server_Settings.this).inflate(R.layout.popup_rename_class, null, false);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Server_Settings.this);
                            EditText et_ClassName = customAlertDialog.findViewById(R.id.et_ClassName);
                            Button btn_nextAddTopic = customAlertDialog.findViewById(R.id.btn_nextAddTopic);
                            builder.setView(customAlertDialog);
                            AlertDialog dialog = builder.show();
                            et_ClassName.setText(className);

                            btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String className = et_ClassName.getText().toString().trim();
                                    refGroupClassList.child(uniClassPushId).child("className").setValue(className);
                                    dialog.dismiss();

                                }
                            });

                        }

                    });

                    rv_ShowClass.setAdapter(showGrpClassList);
                    showGrpClassList.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }



    private void delClass(String groupPushId, String classPos) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classPos).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showGrpClassList.notifyDataSetChanged();
            }
        });

        DatabaseReference delSubjectRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        delSubjectRef.child("Chat_Message").child(groupPushId).child(classPos).removeValue();
        delSubjectRef.child("Doubt").child(groupPushId).child(classPos).removeValue();

        DatabaseReference databaseReferenceClassDelStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp");
        databaseReferenceClassDelStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String classUniJoinedPushId = dataSnapshot.getKey();

                    if(dataSnapshot.child(groupPushId).child("addedOrJoined").exists()){

                        String stuJoin =dataSnapshot.child(groupPushId).child("addedOrJoined").getValue().toString();
                        if (stuJoin.equals("StudentJoin")) {

                            Log.d("DELCHKST", "classUniJoinedPushId: " + dataSnapshot.child(groupPushId).child("addedOrJoined").getValue());
                            Log.d("DELCHKST1", "classUniJoinedPushId: " + databaseReferenceClassDelStudent.child(classUniJoinedPushId).child(groupPushId).child("addedOrJoined"));

                            databaseReferenceClassDelStudent.child(classUniJoinedPushId).child(groupPushId).child("addedOrJoined").setValue(null);

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReferenceClassDel = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(groupPushId);
        databaseReferenceClassDel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String classUniJoinedPushId = dataSnapshot.child("classUniPushId").getValue().toString();

                    Log.d("DELCHK", "classPos: " + classPos + "\n classUniJoinedPushId: " + classUniJoinedPushId);

                    if (classPos.equals(classUniJoinedPushId)) {

                        databaseReferenceClassDel.child(dataSnapshot.getKey()).child("classUniPushId").setValue(null);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void saveClassGroup(String groupPushId, String sbChildGroupName) {

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

                        String serverName = (Objects.requireNonNull(snapshot.child("serverName").getValue())).toString();

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