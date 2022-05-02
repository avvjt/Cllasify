package com.cllasify.cllasify;

import android.app.ProgressDialog;
import android.net.Uri;
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
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Notify;
import com.cllasify.cllasify.Class.Class_Group;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User_Notification_Frag extends Fragment {
    BottomNavigationView bottom_nav;
    ProgressDialog notifyPB;
    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_AllNotifications;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

    SearchView sv_notifySearchView;

    Class_Group userSubsGroupClass;
    ChipNavigationBar chipNavigationBar;



    public User_Notification_Frag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.user_notification_fragment, container, false);

        bottom_nav = getActivity().findViewById(R.id.bottom_nav);
//        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav);
//        chipNavigationBar.setItemSelected(R.id.bottom_nav_notification,true);

        //initialize the toolbar
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Notification");
//        toolbar.setNavigationIcon(R.drawable.ic_left_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //open navigation drawer when click navigation back button
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new HomeFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        sv_notifySearchView =view.findViewById(R.id.sv_notifySearchView);
        rv_AllNotifications =view.findViewById(R.id.rv_AllNotifications);

        notifyPB = new ProgressDialog(getContext());
        notifyPB.setTitle("Notification");
        notifyPB.setMessage("Loading All Notifications");
        notifyPB.setCanceledOnTouchOutside(true);
        notifyPB.show();

        rv_AllNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        listGroupSTitle=new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_Notify(getContext(), listGroupSTitle);
        rv_AllNotifications.setAdapter(showAllGroupAdaptor);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();

        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("Received_Req").child(userID);

        if (sv_notifySearchView != null) {
            sv_notifySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
//                    searchGroup(newText);
                    return false;
                }
            });
        }

        DatabaseReference refGroupReqCount = FirebaseDatabase.getInstance().getReference().
                child( "Notification" ).child("Received_Req").child(userID);
        refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    showEAllGroupSearchRV();
                }else{
                    notifyPB.dismiss();
                  //  Toast.makeText(getContext(), "No group Notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    private void showEAllGroupSearchRV() {
//        listGroupSTitle=new ArrayList<>();

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

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    Toast.makeText(getContext(), "Group request from "+userName+"has been Rejected", Toast.LENGTH_SHORT).show();
                }
                else
                if (notifyCategory.equals("Friend_Request")){
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(reqUserID);
                    userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId, groupName,notPushId,"false","Off");
                    refSubs_J_Group.child(currUserId).setValue(userSubsGroupClass);

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    Toast.makeText(getContext(), "Friend request from "+userName+"has been Rejected", Toast.LENGTH_SHORT).show();

                }
                else
                if (notifyCategory.equals("Follow_Request")){
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Follow").child(reqUserID);
                    userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId, groupName,notPushId,"false","Off");
                    refSubs_J_Group.child(currUserId).setValue(userSubsGroupClass);

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    Toast.makeText(getContext(), "Follow request from "+userName+"has been Rejected", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUniPush) {
//                        DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupPushId);
//                        DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("User_Subscribed_Groups");
//                        refSubsGroup.child(reqUserID).setValue(true);
                if (notifyCategory.equals("Group_JoiningReq")){
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    DatabaseReference refAll_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("User_Subscribed_Groups");

                    refSubs_J_Group.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "joining_No" + noofGroupinCategory;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId,noofGroupinCategory, groupName,classPushId,"true","On");
                            refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);
                            refAll_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                            DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                            DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);

                            refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                            refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");

                            Toast.makeText(getContext(), "Group request from "+userName+"has been Approved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });



                }else if (notifyCategory.equals("Friend_Request")){
                    DatabaseReference refSubs_Frnd_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(reqUserID);
                    DatabaseReference refAdmin_Frnd_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(currUserId);
                    userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId, groupName,notPushId,"true","On");

                    refSubs_Frnd_Group.child(currUserId).setValue(userSubsGroupClass);
                    refAdmin_Frnd_Group.child(reqUserID).setValue(userSubsGroupClass);

                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                    refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");
                    Toast.makeText(getContext(), "Friend request from "+userName+"has been Approved", Toast.LENGTH_SHORT).show();

                }else if (notifyCategory.equals("Follow_Request")){
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Follow").child(reqUserID);
                    userSubsGroupClass = new Class_Group(dateTimeCC,userName, reqUserID, currUserId, groupName,notPushId,"true","On");

                    refSubs_J_Group.child(currUserId).setValue(userSubsGroupClass);

                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                    refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");
                    Toast.makeText(getContext(), "Follow request from "+userName+"has been Approved", Toast.LENGTH_SHORT).show();

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
                    notifyPB.dismiss();
                    showAllGroupAdaptor.notifyDataSetChanged();
//                    }

                } else {
                 //   Toast.makeText(getContext(), "No Group request Pending", Toast.LENGTH_SHORT).show();
                    notifyPB.dismiss();
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

        refSearchShowGroup.addChildEventListener(childEventListener);
//
//        refSearchShowGroup.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

    }



}