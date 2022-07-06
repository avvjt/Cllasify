package com.cllasify.cllasify.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cllasify.cllasify.Adapters.Adaptor_Notify;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
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

public class Student_Joining_Reqs extends Fragment {

    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_StudentJoiningNotification;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    Class_Group userSubsGroupClass;

    final String[] groupPushId = new String[1];
    final String[] uniPushClassId = new String[1];
    final String[] subjectUniPushId = new String[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student__joining__reqs, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();

        DatabaseReference chkJoinedORAddGRP = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        chkJoinedORAddGRP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("subjectUniPushId").exists()) {

                    subjectUniPushId[0] = snapshot.child("subjectUniPushId").getValue().toString().trim();
                }
                if (snapshot.child("clickedGroupPushId").exists()) {
                    groupPushId[0] = snapshot.child("clickedGroupPushId").getValue().toString().trim();
                }
                if (snapshot.child("uniPushClassId").exists()) {

                    uniPushClassId[0] = snapshot.child("uniPushClassId").getValue().toString().trim();
                }
                refSearchShowGroup = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("All_GRPs").child(groupPushId[0]).child(uniPushClassId[0]).child("groupJoiningReqs");

                refSearchShowGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            showEAllGroupSearchRV(uniPushClassId[0]);
                        } else {
                            //  Toast.makeText(getActivity(), "No group Notifications", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv_StudentJoiningNotification = view.findViewById(R.id.rv_StudentJoiningNotification);
        rv_StudentJoiningNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        listGroupSTitle = new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_Notify(getActivity(), listGroupSTitle);
        rv_StudentJoiningNotification.setAdapter(showAllGroupAdaptor);


        return view;

    }

    private void showEAllGroupSearchRV(String subGroupPushId) {
        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_Notify.OnItemClickListener() {

            @Override
            public void createGroupDialog(String adminGroupID, String groupName) {

            }

            @Override
            public void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId) {
                if (notifyCategory.equals("Group_JoiningReq")) {
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, groupName, classPushId, "false", "Off");
                    refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs").child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    Toast.makeText(getActivity(), "Group request from " + userName + "has been Rejected", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUni) {

                if (notifyCategory.equals("Group_JoiningReq")) {
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    DatabaseReference refAll_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("User_Subscribed_Groups");


                    DatabaseReference refAllGRPs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                    refAllGRPs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            refAllGRPs.child(classUni).child("classStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Class_Student_Details class_student_details = new Class_Student_Details(false, reqUserID, userName);
                                    refAllGRPs.child(classUni).child("classStudentList").child(reqUserID).setValue(class_student_details);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    refSubs_J_Group.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "joining_No" + noofGroupinCategory;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, noofGroupinCategory, groupName, classPushId, "Class Member", "On");
                            refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);
                            refAll_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                            DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().
                                    child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs").child(notPushId);
                            DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);

                            refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                            refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");

                            Toast.makeText(getActivity(), "Group request from " + userName + "has been Approved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }


            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    assert class_userDashBoard != null;
                    String groupjoinStatus = class_userDashBoard.getGrpJoiningStatus();
//                    Toast.makeText(getActivity(), "status"+groupjoinStatus, Toast.LENGTH_SHORT).show();
//                    if ((!groupjoinStatus.equals("Approve")) && (!groupjoinStatus.equals("Reject"))) {
                    listGroupSTitle.add(class_userDashBoard);
                    showAllGroupAdaptor.notifyDataSetChanged();
//                    }

                } else {
//                    Toast.makeText(getActivity(), "No Group request Pending", Toast.LENGTH_SHORT).show();
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

        refSearchShowGroup.addChildEventListener(childEventListener);

    }

}