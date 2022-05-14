package com.cllasify.cllasify.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adapter_TopicList_Serv;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember_Serv;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Server.Attendance_Activity;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Students_Subjects extends AppCompatActivity {

    String currUserID;
    RecyclerView rv_showStudents, rv_ShowSubject, rv_showTeachers;
    TextView tv_studentList, tv_adminList, tv_SubjectList;
    Button addNewSubject;
    ImageButton btn_Back;

    //Students
    Adaptor_ShowGrpMember_Serv showGrpMemberList;
    List<Class_Student_Details> listGrpMemberList;

    //Teachers
    Adaptor_ShowGrpMember_Serv adaptor_showGrpAdmin;
    List<Class_Student_Details> class_admin_detailsList;

    //Subjects
    Adapter_TopicList_Serv adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;


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
        setContentView(R.layout.activity_students_subjects);

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

        tv_studentList = findViewById(R.id.studentListText);
        tv_adminList = findViewById(R.id.adminListText);
        tv_SubjectList = findViewById(R.id.subjectListText);
        addNewSubject = findViewById(R.id.addNewSubject);
        btn_Back = findViewById(R.id.btn_Back);


        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });


        //Set Students
        rv_showStudents = findViewById(R.id.studentList);
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMember_Serv(Students_Subjects.this);
        showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember_Serv.OnItemClickListener() {
            @Override
            public void removeStudent(String groupPushId, String classUniPushId, String studentUserId) {
                delStudent(groupPushId, classUniPushId, studentUserId);
            }

            @Override
            public void removeTeacher(String groupPushId, String studentUserId) {

            }
        });
        rv_showStudents.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            addNewSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Students_Subjects.this, Create_Subject.class);
                    intent.putExtra("classUniPushId", uniClassPushId);
                    intent.putExtra("groupPushId", uniGrpPushId);
                    intent.putExtra("justBack", true);
                    startActivity(intent);
                }
            });
        }

        //Set Subjects
        rv_ShowSubject = findViewById(R.id.subjectList);
        subjectDetailsModelList = new ArrayList<>();
        adapter_topicList = new Adapter_TopicList_Serv(Students_Subjects.this);
        adapter_topicList.setOnItemClickListener(new Adapter_TopicList_Serv.OnItemClickListener() {
            @Override
            public void renameSubject(String groupPushId, String classUniPushId, String classPosition, String subjectName) {
                renameSub(groupPushId, classUniPushId, classPosition, subjectName);
            }

            @Override
            public void deleteSubject(String groupPushId, String classPos, String subjectUniPush) {
                delSubject(groupPushId, classPos, subjectUniPush);
            }
        });
        rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));

        //Set Teachers
        rv_showTeachers = findViewById(R.id.adminList);
        class_admin_detailsList = new ArrayList<>();
        adaptor_showGrpAdmin = new Adaptor_ShowGrpMember_Serv(Students_Subjects.this);
        adaptor_showGrpAdmin.setOnItemClickListener(new Adaptor_ShowGrpMember_Serv.OnItemClickListener() {
            @Override
            public void removeStudent(String groupPushId, String classUniPushId, String studentUserId) {
            }

            @Override
            public void removeTeacher(String groupPushId, String studentUserId) {
                delTeacher(groupPushId, studentUserId);
            }
        });
        rv_showTeachers.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));

        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            Log.d("UNICLASS", "onCreate: " + uniClassPushId);

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild("classStudentList")) {
                        Log.d("STUCHK", "onSUBS: yes");
                        rv_showStudents.setVisibility(View.VISIBLE);
                        tv_studentList.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("STUCHK", "onSUBS: nooo");
                        rv_showStudents.setVisibility(View.GONE);
                        tv_studentList.setVisibility(View.GONE);
                    }

                    if (snapshot.hasChild("classSubjectData")) {
                        Log.d("STUCHK", "onSUBS: yes");
                        rv_ShowSubject.setVisibility(View.VISIBLE);
                        tv_SubjectList.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("STUCHK", "onSUBS: nooo");
                        rv_ShowSubject.setVisibility(View.GONE);
                        tv_SubjectList.setVisibility(View.GONE);
                    }

                    subjectDetailsModelList.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                        subjectDetailsModelList.add(object);

                    }
                    adapter_topicList.setUniPush(uniClassPushId);
                    adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);
                    rv_ShowSubject.setAdapter(adapter_topicList);
                    adapter_topicList.notifyDataSetChanged();


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


            DatabaseReference databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(uniGrpPushId);
            databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                        Log.d("CHKINGTECH", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getUserId());
                        class_admin_detailsList.add(object);

                    }

                    adaptor_showGrpAdmin.setClassStudents(class_admin_detailsList);
                    rv_showTeachers.setAdapter(adaptor_showGrpAdmin);
                    adaptor_showGrpAdmin.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    private void delSubject(String groupPushId, String classPos, String subjectUniPush) {

        DatabaseReference delSubjectRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        delSubjectRef.child("All_GRPs").child(groupPushId).child(classPos).child("classSubjectData").child(subjectUniPush).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });


        delSubjectRef.child("Chat_Message").child(groupPushId).child(classPos).child(subjectUniPush).removeValue();
        delSubjectRef.child("Doubt").child(groupPushId).child(classPos).child(subjectUniPush).removeValue();

    }

    private void renameSub(String groupPushId, String classUniPushId, String classPosition, String subjectName) {


        View customAlertDialog = LayoutInflater.from(Students_Subjects.this).inflate(R.layout.dialog_rename_subject, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(Students_Subjects.this);
        EditText et_SubjectName = customAlertDialog.findViewById(R.id.et_SubjectName);
        Button btn_nextAddTopic = customAlertDialog.findViewById(R.id.btn_nextAddTopic);
        builder.setView(customAlertDialog);
        AlertDialog dialog = builder.show();
        et_SubjectName.setText(subjectName);

        btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subjectName = et_SubjectName.getText().toString().trim();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

                databaseReference.child(groupPushId).child(classUniPushId).child("classSubjectData").child(classPosition).child("subjectName").setValue(subjectName);

                dialog.dismiss();

            }
        });


    }

    private void delTeacher(String groupPushId, String teacherUserId) {

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins")
                .child(groupPushId).child("classAdminList").child(teacherUserId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications")
                .child(teacherUserId).child(groupPushId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp")
                .child(teacherUserId).child(groupPushId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class")
                .child(groupPushId).child(teacherUserId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                .child(groupPushId).child("User_Subscribed_Groups").child(teacherUserId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(teacherUserId).child("clickedClassName").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(teacherUserId).child("clickedGroupName").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(teacherUserId).child("clickedGroupPushId").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(teacherUserId).child("clickedSubjectName").removeValue();

    }


    private void delStudent(String groupPushId, String classUniPushId, String studentUserId) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classUniPushId).child("classStudentList").child(studentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Notification").child("User_Notifications")
                .child(studentUserId).child(groupPushId).child(classUniPushId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp")
                .child(studentUserId).child(groupPushId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class")
                .child(groupPushId).child(studentUserId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group")
                .child(groupPushId).child("User_Subscribed_Groups").child(studentUserId).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(studentUserId).child("clickedClassName").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(studentUserId).child("clickedGroupName").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(studentUserId).child("clickedGroupPushId").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(studentUserId).child("clickedStudentUniPushClassId").removeValue();

        FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(studentUserId).child("clickedSubjectName").removeValue();

    }
}