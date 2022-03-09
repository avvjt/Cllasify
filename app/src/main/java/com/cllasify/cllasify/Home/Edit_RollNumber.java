package com.cllasify.cllasify.Home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_RollNumbers;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMemberRollNumberList;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edit_RollNumber extends AppCompatActivity {

    List<Class_Student_Details> listGrpMemberList, mDatalistNew, listGrpRoll;
    List<Class_Group> list_showAttend;

    DatabaseReference refGrpMemberList, refAttendance;
    RecyclerView rv_GrpMemberList, rv_SiderollNumberList;
    Adaptor_ShowGrpMemberRollNumberList showGrpMemberList;
    Adaptor_RollNumbers showGrpRoll;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String currUserId;

    String groupPushId, uniClassPush;
    Button btn_showStudentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roll_number);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();

        btn_showStudentList = findViewById(R.id.btn_showStudentList);

        if (getIntent().hasExtra("uniClassPush") && getIntent().hasExtra("uniClassPush")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
            uniClassPush = getIntent().getStringExtra("uniClassPush");
        }

        rv_GrpMemberList = findViewById(R.id.rv_rollNumberStudentList);
        rv_SiderollNumberList = findViewById(R.id.rv_SiderollNumberList);

        show_GrpMemberList(groupPushId, uniClassPush);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Toast.makeText(Edit_RollNumber.this, "From" + fromPosition + "gggto" + toPosition, Toast.LENGTH_SHORT).show();
                Collections.swap(listGrpMemberList, fromPosition, toPosition);
                showGrpMemberList.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);

        show_GrpRollList(groupPushId, uniClassPush);

        btn_showStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_showStudentList.setVisibility(View.GONE);
                showGrpRoll.removeItem(0);
                showGrpMemberList.removeItem(0);
            }
        });


    }

    private void show_GrpMemberList(String groupPushId, String subGroupPushId) {
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(this));
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMemberRollNumberList(this, listGrpMemberList);


        rv_GrpMemberList.setAdapter(showGrpMemberList);

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {

                    refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("classStudentList");

                    refGrpMemberList.orderByChild("rollNumber").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Class_Student_Details class_student_details = snapshot.getValue(Class_Student_Details.class);
                            listGrpMemberList.add(class_student_details);

                            rv_GrpMemberList.setAdapter(showGrpMemberList);
                            showGrpMemberList.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    listGrpMemberList.clear();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        refGrpMemberList.orderByChild("userName").addChildEventListener(childEventListener);
    }

    private void show_GrpRollList(String groupPushId, String subGroupPushId) {
        rv_SiderollNumberList.setLayoutManager(new LinearLayoutManager(this));
        listGrpRoll = new ArrayList<>();
        showGrpRoll = new Adaptor_RollNumbers(this, listGrpRoll);

        rv_SiderollNumberList.setAdapter(showGrpRoll);


        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {

                    refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("classStudentList");

                    refGrpMemberList.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Class_Student_Details class_student_details = snapshot.getValue(Class_Student_Details.class);
                            listGrpRoll.add(class_student_details);

                            rv_SiderollNumberList.setAdapter(showGrpRoll);
                            showGrpRoll.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    listGrpRoll.clear();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        refGrpMemberList.orderByChild("userName").addChildEventListener(childEventListener);
    }


}