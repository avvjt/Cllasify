package com.cllasify.cllasify.Server;

import android.content.Context;
import android.content.SharedPreferences;
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
//import android.widget.SearchView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
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

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView rv_ChatDashData;
    List<Class_Group> list_ChatDashboard;
    Adaptor_ShowGroup showChatDashadaptor;
    DatabaseReference refChatDashboard;
    SearchView sv_ChatSearch;
    ImageButton ib_cattach, ib_csubmit;
    EditText et_ctext;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userName,userEmailID;
    DatabaseReference refSaveCurrentData;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userName=currentUser.getDisplayName();
        userEmailID=currentUser.getEmail();

//        bundle=this.getArguments();
//        list_ChatDashboard=new ArrayList<>();

        rv_ChatDashData=view.findViewById(R.id.rv_ChatDashData);
        ib_cattach=view.findViewById(R.id.ib_cattach);
        ib_csubmit=view.findViewById(R.id.ib_csubmit);
        et_ctext=view.findViewById(R.id.et_ctext);
        sv_ChatSearch=view.findViewById(R.id.sv_ChatSearch);

        rv_ChatDashData.setLayoutManager(new LinearLayoutManager(getContext()));
        list_ChatDashboard = new ArrayList<>();
        showChatDashadaptor = new Adaptor_ShowGroup(getContext(), list_ChatDashboard);
        rv_ChatDashData.setAdapter(showChatDashadaptor);

        list_ChatDashboard.clear();
        showChatDashadaptor.notifyDataSetChanged();
//        String groupName=bundle.getString("groupName","");
//        String subGroupName=bundle.getString("subGroupName","");
//        String GroupPushId=bundle.getString("GroupPushId","");
//        String SubGroupPushId=bundle.getString("SubGroupPushId","");
//        String groupClassSubjects=bundle.getString("groupClassSubjects","");

//        Toast.makeText(getContext(), "v"+groupName+GroupPushId+subGroupName+groupClassSubjects, Toast.LENGTH_SHORT).show();
//         checkChatDashboard(GroupPushId,SubGroupPushId,subGroupName,groupClassSubjects);

//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        String groupName=sharedPref.getString("groupName","");
//        String subGroupName=sharedPref.getString("subGroupName","");
//        String groupPushId=sharedPref.getString("groupPushId","");
//        String subGroupPushId=sharedPref.getString("subGroupPushId","");
//        String groupClassSubjects=sharedPref.getString("groupClassSubjects","");
        refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();

                    if(snapshot.child("GroupPushId").exists() && snapshot.child("GroupPushId").exists() && snapshot.child("subGroupName").exists() ) {
                        String GroupPushId = snapshot.child("GroupPushId").getValue().toString().trim();
                        String SubGroupPushId = snapshot.child("SubGroupPushId").getValue().toString().trim();
                        String subGroupName = snapshot.child("subGroupName").getValue().toString().trim();
                        String groupName = snapshot.child("groupName").getValue().toString().trim();
                        String groupClassSubjects = snapshot.child("groupClassSubjects").getValue().toString().trim();
//                        Toast.makeText(getContext(), "Grouppushid"+ GroupPushId+"=Subgrouppushid"+SubGroupPushId+"/n"+subGroupName+"/n"+groupClassSubjects, Toast.LENGTH_SHORT).show();
//                        list_ChatDashboard.clear();
                        Toast.makeText(getContext(), "v"+groupName+GroupPushId+subGroupName+groupClassSubjects, Toast.LENGTH_SHORT).show();
//                        checkChatDashboard(GroupPushId,SubGroupPushId,subGroupName,groupClassSubjects);

                        checkChatDashboard(GroupPushId,SubGroupPushId,subGroupName,groupClassSubjects);
                    }
//                    else{
////                        refSaveCurrentData.removeEventListener(this);
//                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        Toast.makeText(getContext(), "1"+groupPushId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "2"+subGroupPushId, Toast.LENGTH_SHORT).show();


        return view;
    }

    private void checkChatDashboard(String groupPushId, String subGroupPushId, String subGroupName, String groupClassSub) {

        refChatDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("Chat_Message").child(groupPushId).child(subGroupPushId).child(groupClassSub);
//        list_ChatDashboard=new ArrayList<>();
        list_ChatDashboard.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_ChatDashboard.add(class_userDashBoard);
                    showChatDashadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
                } else {
                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                showChatDashadaptor.notifyDataSetChanged();
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
        refChatDashboard.addChildEventListener(childEventListener);
        list_ChatDashboard.clear();

        ib_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subGroupMsg = et_ctext.getText().toString().trim();
                if (subGroupMsg.isEmpty()) {
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
                    et_ctext.setError("Enter text");
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("Chat_Message").child(groupPushId).child(subGroupPushId).child(groupClassSub);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "mszno_" + noofGroupinCategory + "_" +subGroupName;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, subGroupPushId, subGroupMsg);
                            reference.child(push).setValue(userAddGroupClass);
                            et_ctext.setText("");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Show_Group").child("User_Show_Group").child(userID).child(subGroupPushId).child(groupClassSub);
                    referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "mszno_" + noofGroupinCategory + "_" + subGroupPushId;
                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, subGroupPushId,groupPushId, noofGroupinCategory);
                            referenceUser.child(push).setValue(userAddGroupClass);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
    }
}