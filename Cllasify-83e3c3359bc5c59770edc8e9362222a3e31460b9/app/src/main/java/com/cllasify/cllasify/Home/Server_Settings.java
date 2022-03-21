package com.cllasify.cllasify.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Server_Settings extends AppCompatActivity {

    DatabaseReference refGroupClassList, getTempData;
    String currUserId;
    RecyclerView rv_ShowClass;
    String groupPushId, serverBio, serverName;
    ImageButton serverSettingProfile;

    Adaptor_Server_Setting_Items showGrpClassList;
    List<Class_Group_Names> listGrpClassList;

    TextView tv_ServerName, schoolBio;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String userID;

    ImageView schoolLogoImg;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Server_Settings.this, Server_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_settings);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
        }


        schoolLogoImg = findViewById(R.id.schoolLogoImg);

        rv_ShowClass = findViewById(R.id.rv_ShowClass);
        tv_ServerName = findViewById(R.id.tv_ServerName);
        schoolBio = findViewById(R.id.schoolBio);
        listGrpClassList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Server_Settings.this));
        showGrpClassList = new Adaptor_Server_Setting_Items(Server_Settings.this, listGrpClassList);

        serverSettingProfile = findViewById(R.id.serverSettingProfile);

        groupPushId = getIntent().getStringExtra("groupPushId");


        DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("serverProfilePic");
        refSaveServerProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!(Server_Settings.this).isFinishing()) {
                        Log.d("SERVIMG", "onDataChange: "+snapshot.getValue());
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
                    Intent intent = new Intent(Server_Settings.this, Server_Setting_Specifics.class);
                    intent.putExtra("currUserId", currUserId);
                    intent.putExtra("groupPushId", groupPushId);

                    startActivity(intent);
                }
            });
        }


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");

        databaseReference.child(groupPushId).child("groupName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    serverName = snapshot.getValue(String.class);
                    tv_ServerName.setText(serverName);

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
                    serverBio = snapshot.getValue(String.class);
                    schoolBio.setText(serverBio);
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

                            View customAlertDialog = LayoutInflater.from(Server_Settings.this).inflate(R.layout.dialog_rename_class, null, false);
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





}