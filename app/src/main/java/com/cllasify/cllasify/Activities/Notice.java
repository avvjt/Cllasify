package com.cllasify.cllasify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cllasify.cllasify.Activities.Server.Create_Class;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpMember_Fees;
import com.cllasify.cllasify.Adapters.NotesAdapter;
import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Class_Notice;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
    ImageButton btn_Back;

    NotesAdapter notesAdapter;
    List<Class_Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        btn_Back = findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

        SwipeHelper swipeHelper = new SwipeHelper(this, noticeRv) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF6200EE"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                firebaseDBNotice.child(noticeList.get(pos).getKey()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Notice.this, "Item successfully removed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#333333"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

//                                Toast.makeText(Notice.this, "" + noticeList.get(pos).getKey(), Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(Notice.this, Update_Notice.class);
                                intent.putExtra("groupPushId", groupPushId);
                                intent.putExtra("classUniPushId", classUniPushId);
                                intent.putExtra("subjectUniPushId", subjectUniPushId);
                                intent.putExtra("title", noticeList.get(pos).title);
                                intent.putExtra("notes", noticeList.get(pos).description);
                                intent.putExtra("date", noticeList.get(pos).date);
                                intent.putExtra("docs", noticeList.get(pos).docs);
                                intent.putExtra("key", noticeList.get(pos).key);
                                startActivity(intent);
                            }
                        }
                ));
            }
        };

        DatabaseReference databaseReferenceStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId);

        databaseReferenceStudent.child("classStudentList").child(userID).child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CHKADMIN", "onDataChange: " + snapshot.getValue());

                if (Objects.equals(snapshot.getValue(), false)) {
                    newNoticeBtn.setVisibility(View.GONE);
                    swipeHelper.dettachSwipe();

                } else {
                    newNoticeBtn.setVisibility(View.VISIBLE);
                    swipeHelper.attachSwipe();
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