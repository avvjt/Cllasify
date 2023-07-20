package com.cllasify.cllasify.Activities.Routine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.AdapterAllTeacherSubjectMain;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class All_Routine extends AppCompatActivity {

    RecyclerView rv_assigned_monday, rv_assigned_tuesday, rv_assigned_wednesday, rv_assigned_thursday, rv_assigned_friday, rv_assigned_saturday;

    private String groupPushId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_routine);


        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
        }

        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child("Routine").child(groupPushId).child("schedule");

        rv_assigned_monday = findViewById(R.id.rv_assigned_monday);
        rv_assigned_tuesday = findViewById(R.id.rv_assigned_tuesday);
        rv_assigned_wednesday = findViewById(R.id.rv_assigned_wednesday);
        rv_assigned_thursday = findViewById(R.id.rv_assigned_thursday);
        rv_assigned_friday = findViewById(R.id.rv_assigned_friday);
        rv_assigned_saturday = findViewById(R.id.rv_assigned_saturday);


        //Monday
        AdapterAllTeacherSubjectMain adapter = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_monday.setAdapter(adapter);
        rv_assigned_monday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDs = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDs.add(dataSnapshot.getKey());

                }
                adapter.setDay("Monday");
                adapter.setRoutines(classIDs);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Tuesday
        AdapterAllTeacherSubjectMain adapter_tuesday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_tuesday.setAdapter(adapter_tuesday);
        rv_assigned_tuesday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsTuesday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsTuesday.add(dataSnapshot.getKey());

                }
                adapter_tuesday.setDay("Tuesday");
                adapter_tuesday.setRoutines(classIDsTuesday);
                adapter_tuesday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Wednesday
        AdapterAllTeacherSubjectMain adapter_wednesday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_wednesday.setAdapter(adapter_wednesday);
        rv_assigned_wednesday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsWednesday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsWednesday.add(dataSnapshot.getKey());

                }
                adapter_wednesday.setDay("Wednesday");
                adapter_wednesday.setRoutines(classIDsWednesday);
                adapter_wednesday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Thursday
        AdapterAllTeacherSubjectMain adapter_thursday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_thursday.setAdapter(adapter_thursday);
        rv_assigned_thursday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsThursday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsThursday.add(dataSnapshot.getKey());

                }
                adapter_thursday.setDay("Thursday");
                adapter_thursday.setRoutines(classIDsThursday);
                adapter_thursday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Friday
        AdapterAllTeacherSubjectMain adapter_friday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_friday.setAdapter(adapter_friday);
        rv_assigned_friday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsFriday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsFriday.add(dataSnapshot.getKey());

                }
                adapter_friday.setDay("Friday");
                adapter_friday.setRoutines(classIDsFriday);
                adapter_friday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Saturday
        AdapterAllTeacherSubjectMain adapter_saturday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_saturday.setAdapter(adapter_saturday);
        rv_assigned_saturday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsSaturday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsSaturday.add(dataSnapshot.getKey());

                }
                adapter_saturday.setDay("Saturday");
                adapter_saturday.setRoutines(classIDsSaturday);
                adapter_saturday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}