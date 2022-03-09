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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Students_Subjects extends AppCompatActivity {

    String currUserID;
    RecyclerView rv_ShowClass, rv_ShowSubject;

    Adaptor_ShowGrpMember showGrpMemberList;
    Adapter_TopicList adapter_topicList;
    List<Class_Student_Details> listGrpMemberList;
    List<Subject_Details_Model> subjectDetailsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_subjects);

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

        rv_ShowClass = findViewById(R.id.studentList);
        listGrpMemberList = new ArrayList<>();
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showGrpMemberList = new Adaptor_ShowGrpMember(getApplicationContext(), listGrpMemberList);


        rv_ShowSubject = findViewById(R.id.subjectList);
        subjectDetailsModelList = new ArrayList<>();
        rv_ShowSubject.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter_topicList = new Adapter_TopicList(getApplicationContext());
        adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);

        if(getIntent().hasExtra("uniGroupPushId")&&getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                        subjectDetailsModelList.add(object);

                    }


                    Adapter_TopicList_Serv adapter_topicList = new Adapter_TopicList_Serv(Students_Subjects.this);
                    rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));
                    adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);
                    rv_ShowSubject.setAdapter(adapter_topicList);
                    adapter_topicList.notifyDataSetChanged();





                    List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKSUB", "onDataChange: " + object.getUserId());
                        class_student_detailsList.add(object);

                    }


                    Adaptor_ShowGrpMember_Serv adaptor_showGrpMember_serv = new Adaptor_ShowGrpMember_Serv(Students_Subjects.this,class_student_detailsList);
                    rv_ShowClass.setLayoutManager(new LinearLayoutManager(Students_Subjects.this));
                    rv_ShowClass.setAdapter(adaptor_showGrpMember_serv);
//            Log.d("TOP", "onBindViewHolder: "+mDatalistNew.get(position).getChildItemList().get(position).getSubjectName());
                    showGrpMemberList.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }




    }
}