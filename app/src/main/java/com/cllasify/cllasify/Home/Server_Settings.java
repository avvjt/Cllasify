package com.cllasify.cllasify.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_Server_Setting_Items;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Server_Setting_Specifics;
import com.cllasify.cllasify.Subject_Details_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Server_Settings extends AppCompatActivity {

    DatabaseReference refGroupClassList, getTempData;
    String currUserId;
    RecyclerView rv_ShowClass;
    String groupPushId, serverName, serverBio;
    ImageButton serverSettingProfile, serverDelete;

    Adaptor_Server_Setting_Items showGrpClassList;
    List<Class_Group_Names> listGrpClassList;

    TextView tv_ServerName, schoolBio;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String userID;

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


        rv_ShowClass = findViewById(R.id.rv_ShowClass);
        tv_ServerName = findViewById(R.id.tv_ServerName);
        schoolBio = findViewById(R.id.schoolBio);
        listGrpClassList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Server_Settings.this));
        showGrpClassList = new Adaptor_Server_Setting_Items(Server_Settings.this, listGrpClassList);

        serverSettingProfile = findViewById(R.id.serverSettingProfile);
        serverDelete = findViewById(R.id.serverDelete);


        if (getIntent().hasExtra("currUserId")) {
            currUserId = getIntent().getStringExtra("currUserId");
            serverSettingProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Server_Settings.this, Server_Setting_Specifics.class);
                    intent.putExtra("currUserId", currUserId);
                    startActivity(intent);
                }
            });
        }

        getTempData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        getTempData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());
                serverName = String.valueOf(snapshot.child("clickedGroupName").getValue());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        serverBio = snapshot.child(groupPushId).child("ServerBio").getValue(String.class);
                        schoolBio.setText(serverBio);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                tv_ServerName.setText(serverName);

                serverDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSpecificServer(groupPushId, userID);
                    }
                });

                //GroupClassList..
                refGroupClassList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                        .child(groupPushId);
                listGrpClassList.clear();

                refGroupClassList.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Class_Group_Names class_userDashBoard = new Class_Group_Names();


                        class_userDashBoard.setClassName(String.valueOf(snapshot.child("className").getValue()));
                        class_userDashBoard.setUniPushClassId(String.valueOf(snapshot.child("classUniPushId").getValue()));
                        class_userDashBoard.setGroupPushId(String.valueOf(snapshot.child("groupPushId").getValue()));

                        GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                };

                        class_userDashBoard.setChildItemList(snapshot.child("classSubjectData").getValue(genericTypeIndicator));


                        GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
                                new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
                                };

                        class_userDashBoard.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void deleteSpecificServer(String groupPushId, String userID) {

        DatabaseReference databaseReferenceServDel = FirebaseDatabase.getInstance().getReference().child("Groups");

        databaseReferenceServDel.child("All_GRPs").child(groupPushId).removeValue();
        databaseReferenceServDel.child("All_Universal_Group").child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_All_Group").child(userID).child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_Public_Group").child(userID).child(groupPushId).removeValue();

        Intent intent = new Intent(Server_Settings.this, Server_Activity.class);
        startActivity(intent);


    }

    private void delServer() {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child("Uni_Group_No_2_NEWW").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

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
    }

    private void delSubject(String groupPushId, String classPos, int subjectPosition) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classPos).child("classSubjectData").child(String.valueOf(subjectPosition)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    private void delStudent() {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child("Uni_Group_No_2_NEWW").child("1").child("classStudentList").child("1").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

}