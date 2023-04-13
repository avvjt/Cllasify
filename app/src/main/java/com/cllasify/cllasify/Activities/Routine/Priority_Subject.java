package com.cllasify.cllasify.Activities.Routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Priority_Subject extends AppCompatActivity {

    ArrayList<String> subjects = new ArrayList<>();


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_subject);


//        String groupPushId = getIntent().getStringExtra("groupPushId");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child("Uni_Group_No_4_Just testing");


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


        autoCompleteTextView = findViewById(R.id.primaryACT);

        adapterItems = new ArrayAdapter<String>(this, R.layout.priority_list_item, subjects);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(Priority_Subject.this, "item: " + item, Toast.LENGTH_SHORT).show();

            }
        });


    }
}