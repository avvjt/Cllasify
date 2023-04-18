package com.cllasify.cllasify.Activities.Routine;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Weekdays_Assign;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Priority_Subject extends AppCompatActivity {

    ArrayList<String> subjects = new ArrayList<>();
    TextInputLayout tvInputPrimary, tvInputSecondary;
    AutoCompleteTextView autoTvPrimary, autoTvSecondary;
    ArrayAdapter<String> adapterItems;
    String grpPushId;
    String itemPrimary = "null";
    String itemSecondary = "null";
    Button doneBtn;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userName;

    TextView tvPrimary, tvSecondary;

    RecyclerView rv_assignedMonday,rv_assignedTuesday,rv_assignedWednesday,rv_assignedThursday,rv_assignedFriday,rv_assignedSaturday;
    Adapter_Weekdays_Assign adapter_weekdays_assignMonday,adapter_weekdays_assignTuesday,adapter_weekdays_assignWednesday,adapter_weekdays_assignThursday,
            adapter_weekdays_assignFriday,adapter_weekdays_assignSaturday;
    List<Class_Routine> class_routinesMonday,class_routinesTuesday,class_routinesWednesday,class_routinesThursday,class_routinesFriday,class_routinesSaturday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_subject);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();


        if (getIntent().hasExtra("groupPushId")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
        }

        autoTvPrimary = findViewById(R.id.primaryACT);
        autoTvSecondary = findViewById(R.id.secondaryACT);

        tvInputPrimary = findViewById(R.id.textInputLayout);
        tvInputSecondary = findViewById(R.id.textInputLayoutSecondary);

        tvPrimary = findViewById(R.id.primarySub);
        tvSecondary = findViewById(R.id.secondarySub);

        doneBtn = findViewById(R.id.doneBtn);
        rv_assignedMonday = findViewById(R.id.rv_assigned_monday);
        class_routinesMonday = new ArrayList<>();

        rv_assignedTuesday = findViewById(R.id.rv_assigned_tuesday);
        class_routinesTuesday = new ArrayList<>();

        rv_assignedWednesday = findViewById(R.id.rv_assigned_wednesday);
        class_routinesWednesday = new ArrayList<>();

        rv_assignedThursday = findViewById(R.id.rv_assigned_thursday);
        class_routinesThursday = new ArrayList<>();

        rv_assignedFriday = findViewById(R.id.rv_assigned_friday);
        class_routinesFriday = new ArrayList<>();


        rv_assignedSaturday = findViewById(R.id.rv_assigned_saturday);
        class_routinesSaturday = new ArrayList<>();

        adapter_weekdays_assignMonday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedMonday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        adapter_weekdays_assignTuesday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedTuesday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        adapter_weekdays_assignWednesday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedWednesday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        adapter_weekdays_assignThursday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedThursday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        adapter_weekdays_assignFriday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedFriday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        adapter_weekdays_assignSaturday = new Adapter_Weekdays_Assign(Priority_Subject.this);
        rv_assignedSaturday.setLayoutManager(new LinearLayoutManager(Priority_Subject.this));

        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("schedule").child(userID);
        dbRoutineStructure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                class_routinesMonday.clear();

                for (DataSnapshot dataSnapshot : snapshot.child("Monday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesMonday.add(class_routine);

                }

                adapter_weekdays_assignMonday.setWeekdays(class_routinesMonday);
                rv_assignedMonday.setAdapter(adapter_weekdays_assignMonday);
                adapter_weekdays_assignMonday.notifyDataSetChanged();


                for (DataSnapshot dataSnapshot : snapshot.child("Tuesday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesTuesday.add(class_routine);

                }

                adapter_weekdays_assignTuesday.setWeekdays(class_routinesTuesday);
                rv_assignedTuesday.setAdapter(adapter_weekdays_assignTuesday);
                adapter_weekdays_assignTuesday.notifyDataSetChanged();



                for (DataSnapshot dataSnapshot : snapshot.child("Wednesday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesWednesday.add(class_routine);

                }

                adapter_weekdays_assignWednesday.setWeekdays(class_routinesWednesday);
                rv_assignedWednesday.setAdapter(adapter_weekdays_assignWednesday);
                adapter_weekdays_assignWednesday.notifyDataSetChanged();



                for (DataSnapshot dataSnapshot : snapshot.child("Thursday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesThursday.add(class_routine);

                }

                adapter_weekdays_assignThursday.setWeekdays(class_routinesThursday);
                rv_assignedThursday.setAdapter(adapter_weekdays_assignThursday);
                adapter_weekdays_assignThursday.notifyDataSetChanged();


                for (DataSnapshot dataSnapshot : snapshot.child("Friday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesFriday.add(class_routine);

                }

                adapter_weekdays_assignFriday.setWeekdays(class_routinesFriday);
                rv_assignedFriday.setAdapter(adapter_weekdays_assignFriday);
                adapter_weekdays_assignFriday.notifyDataSetChanged();


                for (DataSnapshot dataSnapshot : snapshot.child("Saturday").getChildren()) {

                    Log.d("DATAVAL", "onDataChange: " + dataSnapshot);

                    Class_Routine class_routine = dataSnapshot.getValue(Class_Routine.class);
                    class_routinesSaturday.add(class_routine);

                }

                adapter_weekdays_assignSaturday.setWeekdays(class_routinesSaturday);
                rv_assignedSaturday.setAdapter(adapter_weekdays_assignSaturday);
                adapter_weekdays_assignSaturday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        autoTvPrimary.setFocusable(false);
        autoTvPrimary.setClickable(true);

        autoTvSecondary.setFocusable(false);
        autoTvSecondary.setClickable(true);

        /*
        //After done button
        autoTvPrimary.setFocusable(false);
        autoTvPrimary.setClickable(false);
        autoTvPrimary.setEnabled(false);
        autoTvPrimary.setCursorVisible(false);
        autoTvPrimary.setKeyListener(null);
        autoTvPrimary.setBackgroundColor(Color.TRANSPARENT);
        */

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(grpPushId);
        DatabaseReference dbPriority = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("individualStructure").child(userID);

        dbPriority.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String primarySub = snapshot.child("primarySubject").getValue().toString();
                    String secondarySub = snapshot.child("secondarySubject").getValue().toString();


                    tvInputPrimary.setVisibility(View.GONE);
                    tvInputSecondary.setVisibility(View.GONE);

                    tvPrimary.setVisibility(View.VISIBLE);
                    tvSecondary.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.GONE);

                    tvPrimary.setText(primarySub);
                    tvSecondary.setText(secondarySub);

                    Toast.makeText(Priority_Subject.this, "Exists", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Priority_Subject.this, "Doesn't Exists", Toast.LENGTH_SHORT).show();

                    tvInputPrimary.setVisibility(View.VISIBLE);
                    tvInputSecondary.setVisibility(View.VISIBLE);

                    tvPrimary.setVisibility(View.GONE);
                    tvSecondary.setVisibility(View.GONE);
                    doneBtn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Log.d("QQSUBS", "onDataChange: " + dataSnapshot.getRef());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("classSubjectData").getChildren()) {

                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("QQSUBS", "onDataChange: " + object.getSubjectName());
                        subjects.add(object.getSubjectName());


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapterItems = new ArrayAdapter<String>(this, R.layout.priority_list_item, subjects);
        autoTvPrimary.setAdapter(adapterItems);

        autoTvPrimary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPrimary = adapterView.getItemAtPosition(i).toString();
                doneBtn.setEnabled(true);

            }
        });


        adapterItems = new ArrayAdapter<String>(this, R.layout.priority_list_item, subjects);
        autoTvSecondary.setAdapter(adapterItems);

        autoTvSecondary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSecondary = adapterView.getItemAtPosition(i).toString();

            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Priority_Subject.this, "Primary: " + itemPrimary + "\tSecondary: " + itemSecondary, Toast.LENGTH_SHORT).show();
                tvInputPrimary.setVisibility(View.GONE);
                tvInputSecondary.setVisibility(View.GONE);
                doneBtn.setVisibility(View.GONE);

                tvPrimary.setVisibility(View.VISIBLE);
                tvSecondary.setVisibility(View.VISIBLE);

                Class_Individual_Routine class_individual_routine = new Class_Individual_Routine(itemPrimary, itemSecondary, userID, userName);
                dbPriority.setValue(class_individual_routine);

            }
        });

    }
}