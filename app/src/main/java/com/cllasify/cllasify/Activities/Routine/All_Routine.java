package com.cllasify.cllasify.Activities.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.AdapterAllTeacherSubjectMain;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.databinding.ActivityAllRoutineBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class All_Routine extends AppCompatActivity {

    RecyclerView rv_assigned_monday, rv_assigned_tuesday, rv_assigned_wednesday, rv_assigned_thursday, rv_assigned_friday, rv_assigned_saturday;


    private ActivityAllRoutineBinding binding;

    private String groupPushId = "";
    private String classUniPushId = "";
    private String subGroupPushId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        List<String> weekList = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");


        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
            classUniPushId = getIntent().getStringExtra("classPushId");
            subGroupPushId = getIntent().getStringExtra("className");


            DatabaseReference dbRoutineStructureDB = FirebaseDatabase.getInstance().getReference().child("Groups")
                    .child("Routine").child(groupPushId).child("allSchedule").child(classUniPushId);
            binding.editRoutine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbRoutineStructureDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                Intent intent = new Intent(All_Routine.this, Routine_Structure.class);
                                intent.putExtra("groupPushId", groupPushId);
                                intent.putExtra("classPushId", classUniPushId);
                                intent.putExtra("className", subGroupPushId);

                                startActivity(intent);
                            } else {
                                for (int pos = 1; pos < 9; pos++) {

                                    Log.d("DEMOCHK", "onClick: " + pos);


                                    int finalPos = pos;


                                    Class_Routine class_routineMon = new Class_Routine(finalPos, "", "", "", classUniPushId, subGroupPushId);

                                    for (int week = 0; week < weekList.size(); week++) {
                                        dbRoutineStructureDB.child(weekList.get(week)).child(String.valueOf(finalPos)).setValue(class_routineMon);


                                    }


                                }
                                for (int pos = 1; pos < 5; pos++) {

                                    Log.d("DEMOCHK", "onClick: " + pos);


                                    int finalPos = pos;


                                    Class_Routine class_routineMon = new Class_Routine(finalPos, "", "", "", classUniPushId, subGroupPushId);

                                    dbRoutineStructureDB.child("Saturday").child(String.valueOf(finalPos)).setValue(class_routineMon);


                                }
/*
                                Intent intent = new Intent(Server_Activity.this, RoutineStructureActivity.class);
                                intent.putExtra("groupPushId", groupPushId);
                                intent.putExtra("classPushId", classUniPushId);
                                intent.putExtra("className", subGroupPushId);

                                startActivity(intent);
*/

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

            DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                    .child("Routine").child(groupPushId).child("schedule");

            rv_assigned_monday = findViewById(R.id.rv_assigned_monday);
            rv_assigned_tuesday = findViewById(R.id.rv_assigned_tuesday);
            rv_assigned_wednesday = findViewById(R.id.rv_assigned_wednesday);
            rv_assigned_thursday = findViewById(R.id.rv_assigned_thursday);
            rv_assigned_friday = findViewById(R.id.rv_assigned_friday);
            rv_assigned_saturday = findViewById(R.id.rv_assigned_saturday);


            binding.scrollView.fullScroll(ScrollView.FOCUS_UP);

            binding.btnMonday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedMonday);

                binding.rvAssignedTuesday.setVisibility(View.GONE);
                binding.rvAssignedWednesday.setVisibility(View.GONE);
                binding.rvAssignedThursday.setVisibility(View.GONE);
                binding.rvAssignedFriday.setVisibility(View.GONE);
                binding.rvAssignedSaturday.setVisibility(View.GONE);


            });
            binding.btnTuesday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedTuesday);

                binding.rvAssignedMonday.setVisibility(View.GONE);
                binding.rvAssignedWednesday.setVisibility(View.GONE);
                binding.rvAssignedThursday.setVisibility(View.GONE);
                binding.rvAssignedFriday.setVisibility(View.GONE);
                binding.rvAssignedSaturday.setVisibility(View.GONE);

            });
            binding.btnWednesday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedWednesday);

                binding.rvAssignedMonday.setVisibility(View.GONE);
                binding.rvAssignedTuesday.setVisibility(View.GONE);
                binding.rvAssignedThursday.setVisibility(View.GONE);
                binding.rvAssignedFriday.setVisibility(View.GONE);
                binding.rvAssignedSaturday.setVisibility(View.GONE);

            });
            binding.btnThursday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedThursday);

                binding.rvAssignedMonday.setVisibility(View.GONE);
                binding.rvAssignedTuesday.setVisibility(View.GONE);
                binding.rvAssignedWednesday.setVisibility(View.GONE);
                binding.rvAssignedFriday.setVisibility(View.GONE);
                binding.rvAssignedSaturday.setVisibility(View.GONE);

            });
            binding.btnFriday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedFriday);

                binding.rvAssignedMonday.setVisibility(View.GONE);
                binding.rvAssignedTuesday.setVisibility(View.GONE);
                binding.rvAssignedWednesday.setVisibility(View.GONE);
                binding.rvAssignedThursday.setVisibility(View.GONE);
                binding.rvAssignedSaturday.setVisibility(View.GONE);

            });
            binding.btnSaturday.setOnClickListener(v -> {
                toggleViewVisibility(binding.rvAssignedSaturday);


                binding.rvAssignedMonday.setVisibility(View.GONE);
                binding.rvAssignedTuesday.setVisibility(View.GONE);
                binding.rvAssignedWednesday.setVisibility(View.GONE);
                binding.rvAssignedThursday.setVisibility(View.GONE);
                binding.rvAssignedFriday.setVisibility(View.GONE);

            });


            //Monday
            AdapterAllTeacherSubjectMain adapterMonday = new AdapterAllTeacherSubjectMain(getApplicationContext());
            rv_assigned_monday.setAdapter(adapterMonday);
            rv_assigned_monday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapterMonday.setGroupPushId(groupPushId);

            List<String> classIDs = new ArrayList<>();


            dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                        classIDs.add(dataSnapshot.getKey());

                    }
                    adapterMonday.setDay("Monday");
                    adapterMonday.setRoutines(classIDs);
                    adapterMonday.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            //Tuesday
            AdapterAllTeacherSubjectMain adapter_tuesday = new AdapterAllTeacherSubjectMain(getApplicationContext());
            rv_assigned_tuesday.setAdapter(adapter_tuesday);
            rv_assigned_tuesday.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter_tuesday.setGroupPushId(groupPushId);

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
            adapter_wednesday.setGroupPushId(groupPushId);

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
            adapter_thursday.setGroupPushId(groupPushId);

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
            adapter_friday.setGroupPushId(groupPushId);

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
            adapter_saturday.setGroupPushId(groupPushId);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(All_Routine.this, Server_Activity.class);
        finish();
        startActivity(intent);

    }

    private void toggleViewVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            // Hide the content with animation
            view.animate()
                    .alpha(0.0f)
                    .translationY(-view.getHeight())
                    .setDuration(150)
                    .withEndAction(() -> view.setVisibility(View.GONE));
        } else {
            // Show the content with animation
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.0f);
            view.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(150);
        }
    }
}