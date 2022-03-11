package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Notify;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GRPJoinReqs extends AppCompatActivity {

    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_AllNotifications;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    Class_Group userSubsGroupClass;

    String currUserId,groupPushId,subGroupPushId,classPushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grpjoin_reqs);

        groupPushId = getIntent().getStringExtra("groupPushId");
        subGroupPushId = getIntent().getStringExtra("subGroupPushId");
        classPushId = getIntent().getStringExtra("classPushId");

        rv_AllNotifications =findViewById(R.id.rv_AllNotifications);
        rv_AllNotifications.setLayoutManager(new LinearLayoutManager(this));
        listGroupSTitle=new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_Notify(this, listGroupSTitle);
        rv_AllNotifications.setAdapter(showAllGroupAdaptor);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();

        Log.d("GRPPUSH", "onCreate: "+subGroupPushId);

        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().
                child( "Groups" ).child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs");

        DatabaseReference refGroupReqCount = FirebaseDatabase.getInstance().getReference().
                child( "Groups" ).child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs");
        refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    showEAllGroupSearchRV();
                }else{
                    Toast.makeText(GRPJoinReqs.this, "No group Notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private void showEAllGroupSearchRV() {
        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_Notify.OnItemClickListener() {
            @Override
            public void createGroupDialog(String adminGroupID, String groupName) {

            }

            @Override
            public void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId) {
                if (notifyCategory.equals("Group_JoiningReq")){
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId, groupName,classPushId,"false","Off");
                    refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs").child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    Toast.makeText(GRPJoinReqs.this, "Group request from "+userName+"has been Rejected", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId,String classUni) {

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
                                    child( "Groups" ).child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupJoiningReqs").child(notPushId);
                            DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);

                            refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                            refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");

                            Toast.makeText(GRPJoinReqs.this, "Group request from "+userName+"has been Approved", Toast.LENGTH_SHORT).show();
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
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    assert class_userDashBoard != null;
                    String groupjoinStatus=class_userDashBoard.getGrpJoiningStatus();
//                    Toast.makeText(getContext(), "status"+groupjoinStatus, Toast.LENGTH_SHORT).show();
//                    if ((!groupjoinStatus.equals("Approve")) && (!groupjoinStatus.equals("Reject"))) {
                    listGroupSTitle.add(class_userDashBoard);
                    showAllGroupAdaptor.notifyDataSetChanged();
//                    }

                } else {
                    Toast.makeText(GRPJoinReqs.this, "No Group request Pending", Toast.LENGTH_SHORT).show();
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