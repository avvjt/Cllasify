package com.cllasify.cllasify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cllasify.cllasify.Activities.Server.Create_Class;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpMember_Fees;
import com.cllasify.cllasify.Adapters.NotesAdapter;
import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Class_Notice;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Notice extends AppCompatActivity {

    FloatingActionButton newNoticeBtn;
    DatabaseReference firebaseDBNotice;
    String groupPushId, classUniPushId, subjectUniPushId, userID;
    RecyclerView noticeRv;
    RelativeLayout emptll;
    FirebaseUser currentUser;

    NotesAdapter notesAdapter;
    List<Class_Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        groupPushId = getIntent().getStringExtra("groupPushId");
        classUniPushId = getIntent().getStringExtra("classUniPushId");
        subjectUniPushId = getIntent().getStringExtra("subjectUniPushId");

        firebaseDBNotice = FirebaseDatabase.getInstance().getReference()
                .child("Groups").child("Notice").child(groupPushId).child(classUniPushId).child(subjectUniPushId).child("All_Notice");

        newNoticeBtn = findViewById(R.id.newNoticeBtn);
        noticeRv = findViewById(R.id.noticeRv);
        emptll = findViewById(R.id.empty_views);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();

        DatabaseReference databaseReferenceStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId);

        databaseReferenceStudent.child("classStudentList").child(userID).child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CHKADMIN", "onDataChange: " + snapshot.getRef());

                if (Objects.equals(snapshot.getValue(), false)) {
                    newNoticeBtn.setVisibility(View.GONE);

                } else {
                    newNoticeBtn.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        noticeList = new ArrayList<>();
        notesAdapter = new NotesAdapter(Notice.this);
        noticeRv.setLayoutManager(new LinearLayoutManager(Notice.this));


        firebaseDBNotice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    noticeRv.setVisibility(View.VISIBLE);
                    emptll.setVisibility(View.GONE);
                } else {
                    noticeRv.setVisibility(View.GONE);
                    emptll.setVisibility(View.VISIBLE);
                }

                noticeList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Log.d("CHKINGSTUD", "onClick: " + dataSnapshot1.getValue());
                    Class_Notice object = dataSnapshot1.getValue(Class_Notice.class);
                    noticeList.add(object);

                }

                notesAdapter.setNotices(noticeList);
                noticeRv.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        newNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notice.this, Create_Notice.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("classUniPushId", classUniPushId);
                intent.putExtra("subjectUniPushId", subjectUniPushId);
                startActivity(intent);
            }
        });


    }
}