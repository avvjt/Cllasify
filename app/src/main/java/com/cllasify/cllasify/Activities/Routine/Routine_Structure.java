package com.cllasify.cllasify.Activities.Routine;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Teacher_Assign;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Routine_Structure extends AppCompatActivity {
    String grpPushId, classPushId, className;


    Adapter_Teacher_Assign adapter_teacher_assign;
    List<Subject_Details_Model> subjectDetailsModelList;
    RecyclerView rv_monday, rv_tuesday, rv_wednesday, rv_thursday, rv_friday, rv_saturday;

    TextView tvClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_structure);

        if (getIntent().hasExtra("groupPushId") && getIntent().hasExtra("classPushId") && getIntent().hasExtra("className")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
            classPushId = getIntent().getStringExtra("classPushId");
            className = getIntent().getStringExtra("className");

        }

        rv_monday = findViewById(R.id.rv_monday);
        rv_tuesday = findViewById(R.id.rv_tuesday);
        rv_wednesday = findViewById(R.id.rv_wednesday);
        rv_thursday = findViewById(R.id.rv_thursday);
        rv_friday = findViewById(R.id.rv_friday);
        rv_saturday = findViewById(R.id.rv_saturday);
        tvClassName = findViewById(R.id.clname);

        tvClassName.setText(className);

        DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(grpPushId).child(classPushId);


        //Monday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_monday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_monday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Tuesday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_tuesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_tuesday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Wednesday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_wednesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_wednesday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Thursday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_thursday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_thursday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Friday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_friday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_friday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Saturday
        subjectDetailsModelList = new ArrayList<>();
        adapter_teacher_assign = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_saturday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelList.add(object);

                }
                adapter_teacher_assign.setUniPush(classPushId);
                adapter_teacher_assign.setSubjectDetailsModelList(subjectDetailsModelList);
                rv_saturday.setAdapter(adapter_teacher_assign);
                adapter_teacher_assign.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}