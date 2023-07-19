package com.cllasify.cllasify.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.Profile.Profile_Activity;
import com.cllasify.cllasify.Activities.RightPanel.Discover_Activity;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adaptor_Notify;
import com.cllasify.cllasify.ModelClasses.Class_Admission;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
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

public class Notification_Activity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    BottomNavigationView bottom_nav;
    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_AllNotifications;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

    SearchView sv_notifySearchView;

    Class_Group userSubsGroupClass;
    ChipNavigationBar chipNavigationBar;

    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Notification_Activity.this, Server_Activity.class);
        startActivity(intent);
    }

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    private void checkOnlineStatus(String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        setStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userStatus").exists()) {
                    setStatus.child("userStatus").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_notification2);


        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.bottom_nav_notification);
        bottomMenu();

        progressBar = findViewById(R.id.progBar);

        rv_AllNotifications = findViewById(R.id.rv_AllNotifications);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        rv_AllNotifications.setLayoutManager(linearLayoutManager);

        listGroupSTitle = new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_Notify(this, listGroupSTitle);
        rv_AllNotifications.setAdapter(showAllGroupAdaptor);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();

        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(userID);

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
                child("Notification").child("Received_Req").child(userID);
        refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    showEAllGroupSearchRV();
                } else {
                    progressBar.setVisibility(View.GONE);
                    // Toast.makeText(Notification_Activity.this, "No group Notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    private void showEAllGroupSearchRV() {
//        listGroupSTitle=new ArrayList<>();
        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_Notify.OnItemClickListener() {
            public void createGroupDialog(String adminGroupID, String groupName) {

            }

            @Override
            public void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId) {

                if (notifyCategory.equals("Friend_Request")) {

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(reqUserID).child(currUserId);
                    checkFRNDReq.child("reqStatus").setValue("Rejected");

                    //Toast.makeText(Notification_Activity.this, "Friend request from " + userName + "has been Rejected", Toast.LENGTH_SHORT).show();

                } else if (notifyCategory.equals("Follow_Request")) {

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                    //Toast.makeText(Notification_Activity.this, "Follow request from " + userName + "has been Rejected", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUni) {

                if (notifyCategory.equals("Friend_Request")) {

                    DatabaseReference refSubs_Frnd_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(reqUserID);
                    userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, groupName, notPushId, "true", "On");

                    refSubs_Frnd_Group.child(currUserId).setValue(userSubsGroupClass);

                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                    refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");


                    DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(reqUserID).child(currUserId);
                    checkFRNDReq.child("reqStatus").setValue("Accepted");


                } else if (notifyCategory.equals("Follow_Request")) {
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Follow").child(reqUserID);
                    userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, groupName, notPushId, "true", "On");

                    refSubs_J_Group.child(currUserId).setValue(userSubsGroupClass);

                    DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                    DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                    refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");

                }


            }

            @Override
            public void reqClick(String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid, String grpJoiningStatus) {
                if (notifyReq.equals("AdmissionAccepted")) {
                    Dialog bottomSheetDialog = new Dialog(Notification_Activity.this);
                    bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialog.setCancelable(true);
                    bottomSheetDialog.setCanceledOnTouchOutside(true);
                    bottomSheetDialog.setContentView(R.layout.bottomsheet_admission_response);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView response_tv = bottomSheetDialog.findViewById(R.id.response_tv);

                    response_tv.setText("Your admission request has been approved please visit the school for further details.");


                    bottomSheetDialog.show();
                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

                } else if (notifyReq.equals("AdmissionRejected")) {
                    Dialog bottomSheetDialog = new Dialog(Notification_Activity.this);
                    bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialog.setCancelable(true);
                    bottomSheetDialog.setCanceledOnTouchOutside(true);
                    bottomSheetDialog.setContentView(R.layout.bottomsheet_admission_response);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView response_tv = bottomSheetDialog.findViewById(R.id.response_tv);


                    response_tv.setText("Sorry your admission request has been rejected.");


                    bottomSheetDialog.show();
                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

                } else {
                    Dialog bottomSheetDialog = new Dialog(Notification_Activity.this);
                    bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    bottomSheetDialog.setCancelable(true);
                    bottomSheetDialog.setCanceledOnTouchOutside(true);
                    bottomSheetDialog.setContentView(R.layout.bottomsheet_accept_reject);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button btn_accept = bottomSheetDialog.findViewById(R.id.btn_accept);
                    Button btn_reject = bottomSheetDialog.findViewById(R.id.btn_reject);


                    btn_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            DatabaseReference refSubs_Frnd_Group = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(reqUserID);
                            userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, groupName, notPushId, "true", "On");

                            refSubs_Frnd_Group.child(currUserId).setValue(userSubsGroupClass);

                            DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                            DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                            refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                            refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");


                            DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(reqUserID).child(currUserId);
                            checkFRNDReq.child("reqStatus").setValue("Accepted");

                            bottomSheetDialog.dismiss();

                        }
                    });

                    btn_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(currUserId).child(notPushId);
                            DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                            refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                            refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

                            DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(reqUserID).child(currUserId);
                            checkFRNDReq.child("reqStatus").setValue("Rejected");

                            bottomSheetDialog.dismiss();


                        }
                    });

                    bottomSheetDialog.show();
                    bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                }
            }

            @Override
            public void reqAdmissionClick(Class_Admission class_admission, String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid) {

            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    assert class_userDashBoard != null;
                    String groupjoinStatus = class_userDashBoard.getGrpJoiningStatus();
                    listGroupSTitle.add(class_userDashBoard);
                    progressBar.setVisibility(View.GONE);
                    showAllGroupAdaptor.notifyDataSetChanged();

                } else {
                    progressBar.setVisibility(View.GONE);
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

    private void bottomMenu() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag = "home";
                        Intent intent = new Intent(Notification_Activity.this, Server_Activity.class);
                        intent.putExtra("panelState", "close");
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav_discover:
//                          fragment = new FeedFragment();
//                        fragment = new JoinGroupFragment();
//                        tag = "joingroup";
                        startActivity(new Intent(Notification_Activity.this, Discover_Activity.class));
                        Notification_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_notification:
//                        fragment = new User_Notification_Frag();
//                        tag = "notify";
                        startActivity(new Intent(Notification_Activity.this, Notification_Activity.class));
                        Notification_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_profile:
                        startActivity(new Intent(Notification_Activity.this, Profile_Activity.class));
                        Notification_Activity.this.overridePendingTransition(0, 0);

                        break;
                }

                return true;
            }
        });
    }

}