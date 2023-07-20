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

    RecyclerView rv_assigned_monday;

    RecyclerView rv_assigned_tuesday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_routine);

        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child("Routine").child("Uni_Group_No_13_Experimental School").child("schedule");


        //Monday
        rv_assigned_monday = findViewById(R.id.rv_assigned_monday);

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

/*
        //Tuesday
        rv_assigned_monday = findViewById(R.id.rv_assigned_tuesday);

        AdapterAllTeacherSubjectMain adapterTuesday = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_tuesday.setAdapter(adapterTuesday);
        rv_assigned_tuesday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<String> classIDsTuesday = new ArrayList<>();


        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDsTuesday.add(dataSnapshot.getKey());

                }
                adapterTuesday.setDay("Tuesday");
                adapterTuesday.setRoutines(classIDsTuesday);
                adapterTuesday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }
}