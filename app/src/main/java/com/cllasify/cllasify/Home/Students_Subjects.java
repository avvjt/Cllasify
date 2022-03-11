package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.cllasify.cllasify.Adaptor.Adapter_TopicList;
import com.cllasify.cllasify.Adaptor.Adapter_TopicList_Serv;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember_Serv;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
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
    RecyclerView rv_ShowClass, rv_ShowSubject, rv_showTeachers;

    //Students
    Adaptor_ShowGrpMember_Serv showGrpMemberList;
    List<Class_Student_Details> listGrpMemberList;

    //Teachers
    Adaptor_ShowGrpMember_Serv adaptor_showGrpAdmin;
    List<Class_Student_Details> class_admin_detailsList;

    //Subjects
    Adapter_TopicList_Serv adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_subjects);

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

        //Set Students
        rv_ShowClass = findViewById(R.id.studentList);
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMember_Serv(Students_Subjects.this);
        showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember_Serv.OnItemClickListener() {
            @Override
            public void removeStudent(String groupPushId, String classUniPushId, String studentUserId) {
                delStudent(groupPushId,classUniPushId,studentUserId);
            }
        });
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));


        //Set Subjects
        rv_ShowSubject = findViewById(R.id.subjectList);
        subjectDetailsModelList = new ArrayList<>();
        adapter_topicList = new Adapter_TopicList_Serv(Students_Subjects.this);
        rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));

        //Set Teachers
        rv_showTeachers = findViewById(R.id.adminList);
        class_admin_detailsList = new ArrayList<>();
        adaptor_showGrpAdmin = new Adaptor_ShowGrpMember_Serv(Students_Subjects.this);
        rv_showTeachers.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));

        if(getIntent().hasExtra("uniGroupPushId")&&getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                        subjectDetailsModelList.add(object);

                    }

                    adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);
                    rv_ShowSubject.setAdapter(adapter_topicList);
                    adapter_topicList.notifyDataSetChanged();



                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                        Log.d("CHKINGSTUD", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKINGSTUD", "onDataChange: " + object.getUserId());
                        listGrpMemberList.add(object);

                    }


                    showGrpMemberList.setClassStudents(listGrpMemberList);
                    rv_ShowClass.setAdapter(showGrpMemberList);
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


    private void delStudent(String groupPushId,String classUniPushId,String studentUserId) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs")
                .child(groupPushId).child(classUniPushId).child("classStudentList").child(studentUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }
}