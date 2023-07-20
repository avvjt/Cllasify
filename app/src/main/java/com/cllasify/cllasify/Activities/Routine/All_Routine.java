package com.cllasify.cllasify.Activities.Routine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.AdapterAllTeacherSubjectMain;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
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
    List<Class_Routine> classDataListMonday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_routine);

        rv_assigned_monday = findViewById(R.id.rv_assigned_monday);

        AdapterAllTeacherSubjectMain adapter = new AdapterAllTeacherSubjectMain(getApplicationContext());
        rv_assigned_monday.setAdapter(adapter);
        rv_assigned_monday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        rv_assigned_monday.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        classDataListMonday = new ArrayList<>();
//        classDataList = new ArrayList<>();

        List<String> classIDs = new ArrayList<>();

        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child("Routine").child("Uni_Group_No_13_Experimental School").child("schedule");

        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    classIDs.add(dataSnapshot.getKey());

/*
                    for (int i = 1; i <= 8; i++) {

                        Class_Routine class_routine = dataSnapshot.child("Monday").child(String.valueOf(i)).getValue(Class_Routine.class);
                        Log.d("ROUTCHK", "onDataChange: " + class_routine + i);

                        classDataListMonday.add(class_routine);


                    }
                    EveryDayRoutine everyDayRoutine = new EveryDayRoutine("Roronoa Sk", "g5vmLAdL8NaT2kzpO4f4wg2crO62", classDataListMonday);

//                        adapter.setDay("Monday");
//                        adapter.setWeekdays(classDataListMonday);
                    classDataList.add(everyDayRoutine);

                    */
                }
                adapter.setRoutines(classIDs);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}