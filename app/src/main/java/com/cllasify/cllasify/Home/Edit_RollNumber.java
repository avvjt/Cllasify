package com.cllasify.cllasify.Home;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMemberAttendanceRollNumberList;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMemberEditRollNumberList;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Service.NetworkBroadcast;
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
import java.util.Objects;

public class Edit_RollNumber extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;


    List<Class_Student_Details> listGrpMemberList, mDatalistNew, listGrpRoll;
    List<Class_Group> list_showAttend;

    DatabaseReference refGrpMemberList, refAttendance;
    RecyclerView rv_GrpMemberList, rv_SiderollNumberList;
    Adaptor_ShowGrpMemberEditRollNumberList showGrpMemberList;
    Adaptor_RollNumbers showGrpRoll;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String currUserId;

    String groupPushId, uniClassPush;
    Button done;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_edit_roll_number);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();

        done = findViewById(R.id.btn_done);

        if (getIntent().hasExtra("groupPushId") && getIntent().hasExtra("uniClassPush")) {
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
//                Toast.makeText(Edit_RollNumber.this, "From" + fromPosition + "gggto" + toPosition, Toast.LENGTH_SHORT).show();
                Log.d("POSS", "from position: "+listGrpMemberList.get(fromPosition).getUserId());
                Log.d("POSS", "to Position: "+toPosition);
                Collections.swap(listGrpMemberList, fromPosition, toPosition);
                showGrpMemberList.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        DatabaseReference databaseReferenceSaveRoll = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId)
                .child(uniClassPush).child("classStudentList");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i < listGrpMemberList.size();i++){
                    Log.d("POSS", "Members position: "+i+" = "+listGrpMemberList.get(i).getUserName());
                    databaseReferenceSaveRoll.child(listGrpMemberList.get(i).getUserId()).child("rollNumber").setValue(i);
                }
                finish();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);

        show_GrpRollList(groupPushId, uniClassPush);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void show_GrpMemberList(String groupPushId, String subGroupPushId) {
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(this));
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMemberEditRollNumberList(this, listGrpMemberList);


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