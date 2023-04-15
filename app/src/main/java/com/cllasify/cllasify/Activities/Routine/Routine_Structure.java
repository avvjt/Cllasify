package com.cllasify.cllasify.Activities.Routine;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Subject_Assign;
import com.cllasify.cllasify.Adapters.Adapter_Teacher_Assign;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
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


    Adapter_Teacher_Assign adapter_teacher_assignMonday, adapter_teacher_assignTuesday, adapter_teacher_assignWednesday,
            adapter_teacher_assignThursday, adapter_teacher_assignFriday, adapter_teacher_assignSaturday;

    Adapter_Subject_Assign adapter_subject_assignMonday, adapter_subject_assignTuesday, adapter_subject_assignWednesday,
            adapter_subject_assignThursday, adapter_subject_assignFriday, adapter_subject_assignSaturday;
    List<Subject_Details_Model> subjectDetailsModelListMonday, subjectDetailsModelListTuesday, subjectDetailsModelListWednesday, subjectDetailsModelListThursday, subjectDetailsModelListFriday, subjectDetailsModelListSaturday;

    List<Class_Student_Details> classStudentListMonday, classStudentListTuesday, classStudentListWednesday, classStudentListThursday, classStudentListFriday, classStudentListSaturday;

    RecyclerView rv_monday, rv_tuesday, rv_wednesday, rv_thursday, rv_friday, rv_saturday;
    RecyclerView rv_mondayTeacher, rv_tuesdayTeacher, rv_wednesdayTeacher, rv_thursdayTeacher, rv_fridayTeacher, rv_saturdayTeacher;

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

        //Recycler View Initialization
        rv_monday = findViewById(R.id.rv_monday);
        rv_tuesday = findViewById(R.id.rv_tuesday);
        rv_wednesday = findViewById(R.id.rv_wednesday);
        rv_thursday = findViewById(R.id.rv_thursday);
        rv_friday = findViewById(R.id.rv_friday);
        rv_saturday = findViewById(R.id.rv_saturday);
        tvClassName = findViewById(R.id.clname);

        rv_monday.setNestedScrollingEnabled(false);
        rv_tuesday.setNestedScrollingEnabled(false);
        rv_wednesday.setNestedScrollingEnabled(false);
        rv_thursday.setNestedScrollingEnabled(false);
        rv_friday.setNestedScrollingEnabled(false);
        rv_saturday.setNestedScrollingEnabled(false);


        rv_mondayTeacher = findViewById(R.id.rv_mondayTeacher);
        /*
        rv_tuesdayTeacher = findViewById(R.id.rv_tuesdayTeacher);
        rv_wednesdayTeacher = findViewById(R.id.rv_wednesdayTeacher);
        rv_thursdayTeacher = findViewById(R.id.rv_thursdayTeacher);
        rv_fridayTeacher = findViewById(R.id.rv_fridayTeacher);
        rv_saturdayTeacher = findViewById(R.id.rv_saturdayTeacher);

        rv_mondayTeacher.setNestedScrollingEnabled(false);
        rv_tuesdayTeacher.setNestedScrollingEnabled(false);
        rv_wednesdayTeacher.setNestedScrollingEnabled(false);
        rv_thursdayTeacher.setNestedScrollingEnabled(false);
        rv_fridayTeacher.setNestedScrollingEnabled(false);
        rv_saturdayTeacher.setNestedScrollingEnabled(false);
*/
        tvClassName.setText(className);


        DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(grpPushId).child(classPushId);
        DatabaseReference databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(grpPushId);


        //Monday
        subjectDetailsModelListMonday = new ArrayList<>();

        adapter_subject_assignMonday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_monday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListMonday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListMonday.add(object);

                }
                adapter_subject_assignMonday.setUniPush(classPushId);
                adapter_subject_assignMonday.setSubjectDetailsModelList(subjectDetailsModelListMonday);
                rv_monday.setAdapter(adapter_subject_assignMonday);
                adapter_subject_assignMonday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        classStudentListMonday = new ArrayList<>();

        adapter_teacher_assignMonday = new Adapter_Teacher_Assign(Routine_Structure.this);
        rv_mondayTeacher.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classStudentListMonday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                    Log.d("CHKINGTECH", "onClick: " + dataSnapshot1.getValue());
                    Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                    Log.d("CHKSUB27", "onDataChange: " + object.getUserId());
                    classStudentListMonday.add(object);

                }
                adapter_teacher_assignMonday.setUniPush(classPushId);
                adapter_teacher_assignMonday.setClassStudentList(classStudentListMonday);
                rv_mondayTeacher.setAdapter(adapter_teacher_assignMonday);
                adapter_teacher_assignMonday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
        //Tuesday
        subjectDetailsModelListTuesday = new ArrayList<>();
        adapter_teacher_assignTuesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_tuesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListTuesday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListTuesday.add(object);

                }
                adapter_teacher_assignTuesday.setUniPush(classPushId);
                adapter_teacher_assignTuesday.setSubjectDetailsModelList(subjectDetailsModelListTuesday);
                rv_tuesday.setAdapter(adapter_teacher_assignTuesday);
                adapter_teacher_assignTuesday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Wednesday
        subjectDetailsModelListWednesday = new ArrayList<>();
        adapter_teacher_assignWednesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_wednesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListWednesday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListWednesday.add(object);

                }
                adapter_teacher_assignWednesday.setUniPush(classPushId);
                adapter_teacher_assignWednesday.setSubjectDetailsModelList(subjectDetailsModelListWednesday);
                rv_wednesday.setAdapter(adapter_teacher_assignWednesday);
                adapter_teacher_assignWednesday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Thursday
        subjectDetailsModelListThursday = new ArrayList<>();
        adapter_teacher_assignThursday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_thursday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListThursday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListThursday.add(object);

                }
                adapter_teacher_assignThursday.setUniPush(classPushId);
                adapter_teacher_assignThursday.setSubjectDetailsModelList(subjectDetailsModelListThursday);
                rv_thursday.setAdapter(adapter_teacher_assignThursday);
                adapter_teacher_assignThursday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Friday
        subjectDetailsModelListFriday = new ArrayList<>();
        adapter_teacher_assignFriday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_friday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListFriday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListFriday.add(object);

                }
                adapter_teacher_assignFriday.setUniPush(classPushId);
                adapter_teacher_assignFriday.setSubjectDetailsModelList(subjectDetailsModelListFriday);
                rv_friday.setAdapter(adapter_teacher_assignFriday);
                adapter_teacher_assignFriday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Saturday
        subjectDetailsModelListSaturday = new ArrayList<>();
        adapter_teacher_assignSaturday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_saturday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListSaturday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListSaturday.add(object);

                }
                adapter_teacher_assignSaturday.setUniPush(classPushId);
                adapter_teacher_assignSaturday.setSubjectDetailsModelList(subjectDetailsModelListSaturday);
                rv_saturday.setAdapter(adapter_teacher_assignSaturday);
                adapter_teacher_assignSaturday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }
}