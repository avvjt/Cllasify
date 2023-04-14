package com.cllasify.cllasify.Activities.Routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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

import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_subject);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();

        autoTvPrimary = findViewById(R.id.primaryACT);
        autoTvSecondary = findViewById(R.id.secondaryACT);

        tvInputPrimary = findViewById(R.id.textInputLayout);
        tvInputSecondary = findViewById(R.id.textInputLayoutSecondary);

        tvPrimary = findViewById(R.id.primarySub);
        tvSecondary = findViewById(R.id.secondarySub);

        doneBtn = findViewById(R.id.doneBtn);


        if (getIntent().hasExtra("groupPushId")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
        }


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
        DatabaseReference dbPriority = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child("individualStructure").child(userID);

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