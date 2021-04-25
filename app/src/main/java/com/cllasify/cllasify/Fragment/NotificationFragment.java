package com.cllasify.cllasify.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_Notify;
import com.cllasify.cllasify.Adaptor.Adaptor_SearchGroup;
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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class NotificationFragment extends Fragment {
    ProgressDialog notifyPB;
    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_AllNotifications;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

    SearchView sv_notifySearchView;

    ChipNavigationBar chipNavigationBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);

        chipNavigationBar.setItemSelected(R.id.bottom_nav_notification,true);


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

        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child( "User" ).child("GetReq").child(userID);

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
                child( "Notification" ).child( "User" ).child("GetReq").child(userID);
        refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    showEAllGroupSearchRV();
                }else{
                    notifyPB.dismiss();
                    Toast.makeText(getContext(), "No group Notifications", Toast.LENGTH_SHORT).show();
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
            public void rejectNotify(String reqUserID, String currUserId, String groupName, String userName,String pushId,String groupPushId) {

                DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupPushId);
                refSubsGroup.child(reqUserID).setValue(false);
                DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_J_Group").child(groupPushId).child("User_Subscribed_Groups");
                refSubs_J_Group.child(reqUserID).setValue(false);
                DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("User").child("GetReq").child(currUserId).child(pushId);
                DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("User").child("SubmitReq").child(reqUserID).child(pushId);
                refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                //mListener.dislikeAns();
                Toast.makeText(getContext(), "Group request from "+userName+"has been Reject", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String pushId,String groupPushId) {
                        DatabaseReference refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(groupPushId);
                        DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_J_Group").child(groupPushId).child("User_Subscribed_Groups");
                        refSubsGroup.child(reqUserID).setValue(true);
                refSubs_J_Group.child(reqUserID).setValue(true);

                        DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("User").child("GetReq").child(currUserId).child(pushId);
                        DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("User").child("SubmitReq").child(reqUserID).child(pushId);
                        refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                        refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");
                        Toast.makeText(getContext(), "Group request from "+userName+"has been Approve", Toast.LENGTH_SHORT).show();



            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    assert class_userDashBoard != null;
                    String groupjoinStatus=class_userDashBoard.getGrpJoiningStatus();
                    Toast.makeText(getContext(), "status"+groupjoinStatus, Toast.LENGTH_SHORT).show();
//                    if ((!groupjoinStatus.equals("Approve")) && (!groupjoinStatus.equals("Reject"))) {
                        listGroupSTitle.add(class_userDashBoard);
                        notifyPB.dismiss();
                        showAllGroupAdaptor.notifyDataSetChanged();
//                    }

                } else {
                    Toast.makeText(getContext(), "No Group request Pending", Toast.LENGTH_SHORT).show();
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