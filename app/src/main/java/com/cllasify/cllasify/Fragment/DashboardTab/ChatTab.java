package com.cllasify.cllasify.Fragment.DashboardTab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Attendance_Activity;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatTab extends Fragment {


    SearchView sv_ChatSearch;
    RecyclerView rv_ChatDashData;
    ImageButton ib_cattach,ib_csubmit;
    EditText et_ctext;
    String groupPushId,subGroupPushId,groupName,subGroupName;

    DatabaseReference refGrpChildAllDashboard,refTempGroupDB;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userEmailID,userName;
    Class_Group userAddGroupClass;
    Adaptor_ShowGroup showDashadaptor;
    List<Class_Group> list_Dashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat_tab, container, false);


        sv_ChatSearch = view.findViewById(R.id.sv_ChatSearch);
        rv_ChatDashData = view.findViewById(R.id.rv_ChatDashData);
        ib_csubmit = view.findViewById(R.id.ib_csubmit);
        ib_cattach = view.findViewById(R.id.ib_cattach);
        et_ctext = view.findViewById(R.id.et_ctext);
        rv_ChatDashData=view.findViewById(R.id.rv_ChatDashData);

        Bundle bundle=this.getArguments();

        if (bundle!=null) {
            groupName = getArguments().getString("groupName");
            groupPushId = getArguments().getString("groupPushId");
            subGroupName = getArguments().getString("subGroupName");
            subGroupPushId = getArguments().getString("subGroupPushId");


            rv_ChatDashData.setLayoutManager(new LinearLayoutManager(getContext()));
            list_Dashboard = new ArrayList<>();
            showDashadaptor = new Adaptor_ShowGroup(getContext(), list_Dashboard);
            rv_ChatDashData.setAdapter(showDashadaptor);



            upDateDashboard(groupName, subGroupName, groupPushId, subGroupPushId);
        }
        return view;
    }

    private void upDateDashboard(String groupName, String subGroupName, String groupPushId,String subGroupPushId) {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
//        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        refTempGroupDB = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child( userID );
//        refGroupChildUserDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId);
        refGrpChildAllDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId).child("Chat_Message");
//        refGroupChildUserDashboard.keepSynced(true);
//        refGrpChildAllDashboard.keepSynced(true);

        refTempGroupDB.setValue(subGroupPushId);
//        list_Dashboard.clear();
//        listDashboard=new ArrayList<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_Dashboard.add(class_userDashBoard);
                    showDashadaptor.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        //refAdmin.addChildEventListener(childEventListener);
        refGrpChildAllDashboard.addChildEventListener(childEventListener);

        ib_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subGroupMsg = et_ctext.getText().toString().trim();
                if (subGroupMsg.isEmpty()) {
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
                    et_ctext.setError("Enter text");
                } else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("All_Sub_Group").child(groupPushId).child(subGroupPushId).child("Chat_Message");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "mszno_" + noofGroupinCategory + "_" +subGroupName;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId,subGroupPushId,subGroupMsg);
                            reference.child(push).setValue(userAddGroupClass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
        });
    }

}