package com.cllasify.cllasify.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adapter_All_Friends;
import com.cllasify.cllasify.Adaptor.Adapter_ClassGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_Friends;
import com.cllasify.cllasify.Adaptor.Adaptor_FriendsList;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Class.Blur_Report;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class.WebView_Fragment;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Friend_Chat_Activity;
import com.cllasify.cllasify.FriendsListClass;
import com.cllasify.cllasify.MessageAdapter;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
import com.cllasify.cllasify.Server.Attendance_Activity;
import com.cllasify.cllasify.Server.DoubtFragment;
import com.cllasify.cllasify.Service.NetworkBroadcast;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.discord.panels.OverlappingPanelsLayout;
import com.discord.panels.PanelState;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Server_Activity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;


    //Database References
    DatabaseReference refuserPersonalGroup, refuserAllGroup, refChatDashboard,
            refuserPublicGroup, refAllGroup, refOtherUserPublicGroup, refUserStatus,
            refSearchShowGroup, refShowUserPublicGroup, refShowUserPrivateGroup,
            refShowUserAllGroup, refShowUserJoinedGroup, refTeachStud, refChildGroup, checkFriends,
            refGroupSubsList, refGrpMemberList, refClassTeacherList, reference, allDoubtReference, addedOrJoinedGroup;

    //RecyclerViews
    RecyclerView rv_UserPublicGroupTitle, rv_OtherPublicGroupTitle, rv_GrpMemberList, rv_GrpTeacherList,
            recyclerViewClassList, endPanelAllFriendsRecyclerView, rvFriendsList;
    public static RecyclerView rv_ChatDashboard;

    ///Lists

    //List Class Group Model Class
    List<Class_Group> list_GroupTitle, list_UserPrivateGroupTitle, list_UserPublicGroupTitle,
            list_OtherUserPublicGroupTitle, list_SubChild, list_ChatDashboard, list_DoubtDashboard,
            list_Friend, list_ChatListDashboard, list_NewChatDashboard;

    List<Class_Student_Details> listGrpMemberList, listGrpTeacherList, listFriendList;
    List<String> allFriendsList;
    List<FriendsListClass> friendsListClasses;
    List<Class_Group_Names> parentItemArrayListClassName;
    List<Subject_Details_Model> childItemArrayListClassName;
    ArrayList<Class_Group> chats, doubts;


    //Adapters
    Adaptor_QueryGroup showGroupAdaptor, showUserPrivateGroupAdaptor, showUserPublicGroupAdaptor;
    Adaptor_ShowGrpMember showGrpMemberList, showGrpTeacherList;
    Adaptor_QueryGroup showOtherUserPublicGroupAdaptor;
    Adaptor_Friends show_FriendAdaptor, showChatListDashAdaptor;
    Adapter_All_Friends adapter_all_friends;
    Adaptor_ShowGroup showChatDashAdaptor;
    Adaptor_FriendsList adaptor_friendsList;


    //Relative Layouts
    LinearLayout ll_bottom_send;
    RelativeLayout friendToolBar;

    //Linear Layouts
    LinearLayout onlyAdminLayout, groupSection, ll_AddJoinGrp, ll_ChatDoubtDashboard,
            endPanelLinearLayout, friendSection, rightPanelMems, rightPanelMember,
            ib_servSettings, btn_lteachattend, FriendListText, btn_joinNotification;


    //Buttons
    Button btn_cAddGroup, btn_cJoinGroup, btn_lTeachResult, btn_lTeachExam;

    //Image Buttons
    ImageButton ib_cattach, ib_csubmit, ib_doubtSubmit, ImageViewRecentChat, ib_FrndP_csubmit, swipe_left, swipe_right, ib_pdf_btn;


    TabLayout tabLayout, tabL_ChatView;
    BottomNavigationView bottomNavigationView;
    TextView tv_GroupMember, adminListText, FriendListTextt;
    FirebaseDatabase refFriendList;

    String GroupCategory;
    GoogleSignInClient googleSignInClient;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userEmailID, userName;
    OverlappingPanelsLayout overlappingPanels;


    ImageView imageViewAddPanelAddGroup;
    Class_Group userAddGroupClass, userSubsGroupClass;
    EditText et_FrndP_text, et_ctext;
    TextView tv_UserPublicTitle, tv_OtherTitle,
            textViewGroupName,
            tv_FrndP_Title, textViewSubjectName;


    FragmentManager fragmentManager;
    DoubtFragment doubtFragment;
    Friend_Chat_Activity friendChatFragment;
    WebView_Fragment webView_fragment;


    //Right Panel Class and Sub-class
    Adapter_ClassGroup adapter_classGroup;

    //SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    String displayName;

    //Chat_Activity
    private static final String TAG = "ChatActivity";
    ValueEventListener seenListener, readLiveMessageListener, readLiveDoubtListener;
    private MessageAdapter messageAdapter;
    private boolean onScreen;
    boolean flag = false;
    boolean flagFriend = false;
    boolean pdf_flag = false;
    private Uri fileUri;

    void init() {

        rightPanelMems = findViewById(R.id.rightPanelMembs);
        rightPanelMember = findViewById(R.id.rightPanelMember);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);

        onlyAdminLayout = findViewById(R.id.onlyAdminLayout);
        btn_joinNotification = findViewById(R.id.btn_joinNotification);

        bottomNavigationView = this.findViewById(R.id.bottom_nav);
        rv_UserPublicGroupTitle = findViewById(R.id.rv_UserPublicGroupTitle);
        rv_OtherPublicGroupTitle = findViewById(R.id.rv_OtherPublicGroupTitle);
        fragmentManager = getSupportFragmentManager();
        doubtFragment = new DoubtFragment();

        tv_GroupMember = findViewById(R.id.tv_GroupMember);
        adminListText = findViewById(R.id.adminListText);
        FriendListText = findViewById(R.id.FriendListText);
        FriendListTextt = findViewById(R.id.FriendListTextt);
        friendToolBar = findViewById(R.id.friendToolBar);

        endPanelAllFriendsRecyclerView = findViewById(R.id.endPanelAllFriendsRecyclerView);

        ll_AddJoinGrp = findViewById(R.id.ll_AddJoinGrp);


        rv_ChatDashboard = findViewById(R.id.rv_ChatDashboard);
        ll_ChatDoubtDashboard = findViewById(R.id.ll_ChatDoubtDashboard);

        tv_FrndP_Title = findViewById(R.id.tv_FrndP_Title);
        tv_UserPublicTitle = findViewById(R.id.tv_UserPublicTitle);
        tv_OtherTitle = findViewById(R.id.tv_OtherTitle);

        ib_FrndP_csubmit = findViewById(R.id.ib_FrndP_csubmit);

        btn_lteachattend = findViewById(R.id.btn_lteachattend);
        btn_lTeachExam = findViewById(R.id.btn_lteachexam);
        btn_lTeachResult = findViewById(R.id.btn_lteachresult);


        rvFriendsList = findViewById(R.id.recyclerViewFriendsNameList);
        refFriendList = FirebaseDatabase.getInstance();
        friendsListClasses = new ArrayList<>();

        friendSection = findViewById(R.id.friendsSection);
        ib_servSettings = findViewById(R.id.ib_servSettings);


        /*
        End Panel Initialisation
         */
        endPanelLinearLayout = findViewById(R.id.endPanelLinearLayout);


        /*
            group section Initialisation
         */
        groupSection = findViewById(R.id.groupSection);
        textViewGroupName = findViewById(R.id.groupName);
        textViewSubjectName = findViewById(R.id.subjectName);
        recyclerViewClassList = findViewById(R.id.recyclerViewClassList);

//        ll_TabChatDoubt = findViewById(R.id.ll_TabChatDoubt);

        overlappingPanels = findViewById(R.id.overlapping_panels);


        ImageViewRecentChat = findViewById(R.id.ib_FrndsList);

        //start panel initialisation
        imageViewAddPanelAddGroup = findViewById(R.id.addNewTopicButton);

        list_GroupTitle = new ArrayList<>();
        list_UserPrivateGroupTitle = new ArrayList<>();
        list_UserPublicGroupTitle = new ArrayList<>();
        list_ChatDashboard = new ArrayList<>();
        list_OtherUserPublicGroupTitle = new ArrayList<>();
        list_SubChild = new ArrayList<>();
        list_DoubtDashboard = new ArrayList<>();
        list_Friend = new ArrayList<>();
        list_ChatListDashboard = new ArrayList<>();


        //Group Members
        refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID);
        refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
        refTeachStud = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
        refOtherUserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");

        refShowUserPrivateGroup.keepSynced(true);
        refShowUserPublicGroup.keepSynced(true);

        rv_GrpMemberList = findViewById(R.id.rv_GrpMemberList);
        listGrpMemberList = new ArrayList<>();
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rv_GrpTeacherList = findViewById(R.id.adminList);
        listGrpTeacherList = new ArrayList<>();
        rv_GrpTeacherList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        allFriendsList = new ArrayList<>();


        endPanelAllFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listFriendList = new ArrayList<>();

        //Setting up layout manager
        rv_UserPublicGroupTitle.setLayoutManager(new LinearLayoutManager(this));
        rv_OtherPublicGroupTitle.setLayoutManager(new LinearLayoutManager(this));


        //setting up Adapter
//        showGroupAdaptor = new Adaptor_QueryGroup(this, list_GroupTitle);
//        showUserPrivateGroupAdaptor = new Adaptor_QueryGroup(this, list_UserPrivateGroupTitle);
//        showUserPublicGroupAdaptor = new Adaptor_QueryGroup(this, list_UserPublicGroupTitle);
        showChatDashAdaptor = new Adaptor_ShowGroup(this, list_ChatDashboard);
        showOtherUserPublicGroupAdaptor = new Adaptor_QueryGroup(this, list_OtherUserPublicGroupTitle);

        showGrpMemberList = new Adaptor_ShowGrpMember(Server_Activity.this, listGrpMemberList);
        showGrpTeacherList = new Adaptor_ShowGrpMember(Server_Activity.this, listGrpTeacherList);
        showGrpTeacherList.setOnItemClickListener(new Adaptor_ShowGrpMember.OnItemClickListener() {
            @Override
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {

            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {

            }

            @Override
            public void MemberProfile(String memberUserId, String memberUserName) {
                showBtmDialogUserProfile(memberUserId, memberUserName);
            }
        });

        adapter_all_friends = new Adapter_All_Friends(this, allFriendsList);
        endPanelAllFriendsRecyclerView.setAdapter(adapter_all_friends);

        show_FriendAdaptor = new Adaptor_Friends(this, list_Friend);
        showChatListDashAdaptor = new Adaptor_Friends(this, list_ChatListDashboard);
//        rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupAdaptor);
        rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);


        ib_csubmit = findViewById(R.id.ib_csubmit);
        ib_doubtSubmit = findViewById(R.id.ib_doubtSubmit);
        ib_pdf_btn = findViewById(R.id.ib_pdf_btn);

        swipe_left = findViewById(R.id.swipe_left);
        swipe_right = findViewById(R.id.swipe_right);


        adaptor_friendsList = new Adaptor_FriendsList(getApplicationContext(), friendsListClasses);


        //Class-Group
        adapter_classGroup = new Adapter_ClassGroup(getApplicationContext());
        adapter_classGroup.setOnItemClickListener(new Adapter_ClassGroup.onAddSubjectClickListener() {
            @Override
            public void onAddSubjectClickListener(String groupName, String uniPushClassId) {

            }

            @Override
            public void onClassClickListener(int position, String classGroupName, String uniPushClassId) {

                DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                posTemp.child("classPosition").setValue(position);
                posTemp.child("clickedClassName").setValue(classGroupName);
                posTemp.child("uniPushClassId").setValue(uniPushClassId);

            }


            @Override
            public void onSubClick(int classUniPosition, String clickedClassName, String uniClassPushId) {

                final String[] serverName = new String[1];
                final String[] className = new String[1];
                final String[] subjectName = new String[1];
                final String[] groupPushId = new String[1];
                final String[] classPosition = new String[1];
                final String[] uniPushClassId = new String[1];
                final String[] subjectUniPushId = new String[1];
                final String[] clickedGroupName = new String[1];

                friendToolBar.setVisibility(View.GONE);
                textViewSubjectName.setVisibility(View.VISIBLE);

                if (flag == true) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    manager.getBackStackEntryCount();
                    transaction.remove(doubtFragment);
                    transaction.commit();
                    flag = false;
                }

                if (flagFriend == true) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    manager.getBackStackEntryCount();
                    transaction.remove(friendChatFragment);
                    transaction.commit();
                    flagFriend = false;
                }

                if (pdf_flag == true) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    manager.getBackStackEntryCount();
                    transaction.remove(webView_fragment);
                    transaction.commit();
                    pdf_flag = false;
                }

                DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            if (snapshot.child("clickedClassName").exists() && snapshot.child("clickedSubjectName").exists() && snapshot.child("clickedGroupPushId").exists()) {
                                Log.d(TAG, "onDataChange: Clicked");
                                if (snapshot.child("serverName").exists()) {
                                    serverName[0] = snapshot.child("serverName").getValue().toString().trim();
                                }
                                subjectUniPushId[0] = snapshot.child("subjectUniPushId").getValue().toString().trim();
                                uniPushClassId[0] = snapshot.child("uniPushClassId").getValue().toString().trim();
                                groupPushId[0] = snapshot.child("clickedGroupPushId").getValue().toString().trim();
                                classPosition[0] = snapshot.child("classPosition").getValue().toString().trim();
                                className[0] = snapshot.child("clickedClassName").getValue().toString().trim();
                                subjectName[0] = snapshot.child("clickedSubjectName").getValue().toString().trim();
                                clickedGroupName[0] = snapshot.child("clickedGroupName").getValue().toString().trim();

                                ib_servSettings.setEnabled(true);

                                memVis(true);

                                setReference(groupPushId[0], clickedClassName, subjectName[0], String.valueOf(classUniPosition), uniClassPushId, subjectUniPushId[0], clickedGroupName[0]);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                overlappingPanels.closePanels();
            }

        });
        parentItemArrayListClassName = new ArrayList<>();
        childItemArrayListClassName = new ArrayList<>();
        adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
        recyclerViewClassList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewClassList.setAdapter(adapter_classGroup);

        final String[] serverName = new String[1];
        final String[] className = new String[1];
        final String[] subjectName = new String[1];
        final String[] groupPushId = new String[1];
        final String[] classPosition = new String[1];
        final String[] uniPushClassId = new String[1];
        final String[] subjectUniPushId = new String[1];
        final String[] clickedGroupName = new String[1];


        DatabaseReference chkJoinedORAddGRP = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID);
        chkJoinedORAddGRP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("CHKJOINEDORADDGRPNN", "onDataChange: available");
                    DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

                    refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0) {
                                if (snapshot.child("clickedClassName").exists() && snapshot.child("clickedSubjectName").exists() && snapshot.child("clickedGroupPushId").exists()) {
                                    Log.d(TAG, "onDataChange: Clicked");
                                    if (snapshot.child("serverName").exists()) {
                                        serverName[0] = Objects.requireNonNull(snapshot.child("serverName").getValue()).toString().trim();
                                    }
                                    subjectUniPushId[0] = Objects.requireNonNull(snapshot.child("subjectUniPushId").getValue()).toString().trim();
                                    classPosition[0] = Objects.requireNonNull(snapshot.child("classPosition").getValue()).toString().trim();
                                    className[0] = Objects.requireNonNull(snapshot.child("clickedClassName").getValue()).toString().trim();
                                    subjectName[0] = Objects.requireNonNull(snapshot.child("clickedSubjectName").getValue()).toString().trim();
                                    groupPushId[0] = Objects.requireNonNull(snapshot.child("clickedGroupPushId").getValue()).toString().trim();
                                    uniPushClassId[0] = Objects.requireNonNull(snapshot.child("uniPushClassId").getValue()).toString().trim();
                                    clickedGroupName[0] = Objects.requireNonNull(snapshot.child("clickedGroupName").getValue()).toString().trim();


                                    setReference(groupPushId[0], className[0], subjectName[0], classPosition[0], uniPushClassId[0], subjectUniPushId[0], clickedGroupName[0]);


                                    Log.d("SCSG", "init: serverName: " + serverName[0] + "\nsubjectUniPush: " + subjectUniPushId[0] + "\nclassName: " + className[0] + "\nsubjectName: " + subjectName[0] + "\ngroupPushId: " + groupPushId[0]);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Log.d("CHKJOINEDORADDGRPNN", "onDataChange: Not available");
                    rv_ChatDashboard.setVisibility(View.GONE);
                    ll_bottom_send.setVisibility(View.GONE);
                    textViewSubjectName.setVisibility(View.GONE);
                    textViewGroupName.setVisibility(View.GONE);
                    rv_GrpMemberList.setVisibility(View.GONE);
                    rv_GrpTeacherList.setVisibility(View.GONE);
                    groupSection.setVisibility(View.GONE);
                    tv_GroupMember.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        setReference("Uni_Group_No_0_Exp Group","Class 1","English","0");


        //Chat_Activity
        onScreen = false;
        chats = new ArrayList<>();
        doubts = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext());
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setStackFromEnd(true);
        rv_ChatDashboard.setLayoutManager(linearLayout);
        rv_ChatDashboard.setAdapter(messageAdapter);

        messageAdapter.setOnDownloadClickListener(new MessageAdapter.onDownloadClickListener() {
            @Override
            public void onDownloadClick(String path, String doc_name) {
                webView_fragment = new WebView_Fragment();
                if (!pdf_flag) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("path", path);
                    bundle.putString("docName", doc_name);
                    webView_fragment.setArguments(bundle);
                    getFragmentManager().getBackStackEntryCount();
                    transaction.replace(R.id.below_toolbar, webView_fragment, "FirstFragment");
                    transaction.commit();
                    pdf_flag = true;
                }
            }
        });

        messageAdapter.setOnDoubtClickListener((doubtQuestion, groupPush, groupClassPush, groupSubjectPush, doubtQuestionPush, userId, userName) -> {
            DatabaseReference putTempDoubt = FirebaseDatabase.getInstance().getReference().child("Groups");
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupPushId").setValue(groupPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassPushId").setValue(groupClassPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassSubjectPushId").setValue(groupSubjectPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("doubtQuestionPushId").setValue(doubtQuestionPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("doubtCreatorName").setValue(userName);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("doubtCreatorId").setValue(userId);


            if (!flag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                getFragmentManager().getBackStackEntryCount();
                transaction.replace(R.id.rl_ChatDashboard, doubtFragment, "FirstFragment");
                transaction.commit();
                flag = true;
            }
        });


        ib_csubmit.setOnClickListener(view -> {

            String subGroupMsg = et_ctext.getText().toString().trim();

            if (subGroupMsg.isEmpty()) {
//                Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
            } else {
                et_ctext.getText().clear();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                        String push = "mszno_" + noofGroupinCategory + "_" + snapshot.getChildrenCount();

                        String messagePush = reference.push().toString();
                        String[] f = messagePush.split("/");
                        String messagePushId = f[f.length - 1];
                        Log.d("messagePushId", "onDataChange: " + messagePush);
                        Log.d("messagePushId01", "onDataChange: " + f[f.length - 1]);

                        Calendar calenderCC = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                        String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                        userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId[0], classPosition[0], subGroupMsg, "chat", subjectUniPushId[0], messagePushId, "", "");
                        reference.child(messagePushId).setValue(userAddGroupClass);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

    }

    private void memVis(boolean b) {
        if (b == true) {
            rightPanelMems.setVisibility(View.VISIBLE);
            rightPanelMember.setVisibility(View.VISIBLE);
        } else {
            rightPanelMems.setVisibility(View.GONE);
            rightPanelMember.setVisibility(View.GONE);

        }

    }

    public void checkAdmmmmin(boolean checking) {
        Log.d("ADMMMM", "is admin : " + checking);
        if (checking) {
            onlyAdminLayout.setVisibility(View.VISIBLE);
        } else {
            onlyAdminLayout.setVisibility(View.GONE);
        }
    }

    private void setReference(String groupPushId, String subGroupPushId, String groupClassSubject, String classPosition, String classUniPushId, String subjectUniPushId, String serverName) {

        if (groupPushId != null && classUniPushId != null && subjectUniPushId != null) {

            btn_joinNotification.setEnabled(true);
            btn_lteachattend.setEnabled(true);

            btn_lteachattend.setOnClickListener(view -> {

                Intent intent = new Intent(Server_Activity.this, Attendance_Activity.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", classUniPushId);
                intent.putExtra("classPushId", subjectUniPushId);
                startActivity(intent);
            });

            btn_joinNotification.setOnClickListener(v -> {
                Intent intent = new Intent(Server_Activity.this, GRPJoinReqs.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", classUniPushId);
                intent.putExtra("classPushId", subjectUniPushId);
                startActivity(intent);
            });
        }


        messageAdapter.setOnMessageClickListener(new MessageAdapter.onMessageClickListener() {
            @Override
            public void onMessageClick(int position, Class_Group chat) {

                final Dialog dialog = new Dialog(Server_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.more_chat_options);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.create();

                DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference()
                        .child("Groups").child("Chat_Message").child(groupPushId).child(classUniPushId).child(subjectUniPushId);

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    if (chat.getPosition().equals(userID)) {

                        dialog.show();


                        Log.d("USERID", "getPosition: " + chat.getPosition());
                        Log.d("USERID", "messageId: " + chat.getSubGroupPushId());


                        Button copyBtn = dialog.findViewById(R.id.copy_button);
                        copyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData data = (ClipData) ClipData.newPlainText("text", chat.getGroupSubGroupComb().toString());
                                clipboardManager.setPrimaryClip(data);

                            }
                        });

                        Button unsend = dialog.findViewById(R.id.unsend_button);
                        unsend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                firebaseDatabaseUnsendMSG.child(chat.getDoubtUniPushId()).removeValue();
                                messageAdapter.removeItem(position);

/*
                                        DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference().child("Groups").child("Chat_Message").child(chat.get(getAdapterPosition()).getGroupName());

                                        firebaseDatabaseUnsendMSG.child(chat.get(getAdapterPosition()).getMessageIdSender()).removeValue();
                                        removeItem(getAdapterPosition());
*/
                            }
                        });

                        Button reply = dialog.findViewById(R.id.reply_button);
                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    } else {


                        boolean isUser = false;
                        Log.d("REPOCHK", "onDataChange: " + chat.getReportUsers());
                        String[] ru = chat.getReportUsers().split("&");
                        Log.d("REPOCHK", "onDataChange: " + ru.length);
                        for (int i = 0; i < ru.length; i++) {
                            Log.d("REPOCHK", "onDataChange: " + ru[i].equals(userID));
                            if (ru[i].equals(userID)) {
                                isUser = true;
                            }
                        }
                        if (ru.length == 5) {
                            firebaseDatabaseUnsendMSG.child(chat.getDoubtUniPushId()).child("groupSubGroupComb").setValue("This message is reported");
                        }
                        Log.d("REPOCHK", "onDataChange: REPORT CHECK" + ru.length);

                        if (chat.getGroupSubGroupComb().trim().equals("This message is reported") || isUser == true || ru.length == 5) {


                            Toast.makeText(getApplicationContext(), "This message is reported", Toast.LENGTH_SHORT).show();

                        } else {

                            dialog.show();

                            Log.d("NOTREPORTED", "getPosition: " + chat.getGroupSubGroupComb());

                            dialog.setContentView(R.layout.more_chat_options_others);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            dialog.getWindow().setGravity(Gravity.BOTTOM);


                            Button copyBtn = dialog.findViewById(R.id.copy_button);
                            copyBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData data = (ClipData) ClipData.newPlainText("text", chat.getGroupSubGroupComb().toString());
                                    clipboardManager.setPrimaryClip(data);

                                }
                            });

                            Button report = dialog.findViewById(R.id.report_button);
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            String repoUsers = snapshot.child(chat.getDoubtUniPushId()).child("reportUsers").getValue().toString().trim();
                                            Log.d("REPOCHK", "onDataChange: " + repoUsers);


                                            String[] ru = repoUsers.split("&");
                                            Log.d("REPOCHK", "onDataChange: " + ru.length);
                                            for (int i = 0; i < ru.length; i++) {
                                                Log.d("REPOCHK", "onDataChange: " + ru[i]);
                                            }

                                            if (repoUsers.equals("")) {
                                                firebaseDatabaseUnsendMSG.child(chat.getDoubtUniPushId()).child("reportUsers").setValue(userID + "&");
                                            } else {
                                                firebaseDatabaseUnsendMSG.child(chat.getDoubtUniPushId()).child("reportUsers").setValue(repoUsers + userID + "&");
                                            }
                                            messageAdapter.notifyItemChanged(position);
                                            messageAdapter.notifyDataSetChanged();

//
//                                            firebaseDatabaseUnsendMSG.child(chat.getDoubtUniPushId()).child("reportUsers").setValue("2aBFaV4H7XOGwg8JnOBiSXF5zvf2&");


/*
                                            if ((snapshot.child(chat.getDoubtUniPushId()).child("reportUser1").getValue().toString().equals(""))
                                                    && (snapshot.child(chat.getDoubtUniPushId()).child("reportUser2").getValue().toString() != "")
                                                    && (snapshot.child(chat.getDoubtUniPushId()).child("reportUser2").getValue().toString() != "")) {
                                                reference.child(chat.getDoubtUniPushId()).child("reportUser1").setValue(userID);
                                            }
                                            if (snapshot.child(chat.getDoubtUniPushId()).child("reportUser2").getValue().toString().equals("")) {
                                                reference.child(chat.getDoubtUniPushId()).child("reportUser2").setValue(userID);
                                            }
                                            if (snapshot.child(chat.getDoubtUniPushId()).child("reportUser3").getValue().toString().equals("")) {
                                                reference.child(chat.getDoubtUniPushId()).child("reportUser3").setValue(userID);
                                            }
*/

                                            /*
                                            chats.get(position).setBlur_report(blur_report);
                                            messageAdapter.setList(chats);
                                            messageAdapter.notifyItemChanged(position);
                                            messageAdapter.notifyDataSetChanged();
                                            */

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


//                                    DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference().child("Groups").child("Chat_Message").child(groupPushId);
//
//                                    firebaseDatabaseUnsendMSG.child(classUniPushId).child(subjectUniPushId).child(chat.getDoubtUniPushId()).child("reportUserCheck")
//                                            .child(userID).setValue(blur_report);

                                }
                            });

                            Button reply = dialog.findViewById(R.id.reply_button);
                            reply.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    }

                }


                Log.d("USERIDADAPTER", "messageId: " + chat.getSubGroupPushId());


            }
        });


        if (subjectUniPushId != null && classUniPushId != null && groupPushId != null) {
            showDoubt(groupPushId, classUniPushId, subjectUniPushId);
        }
        if (subjectUniPushId != null && classUniPushId != null) {
            DatabaseReference databaseReferenceCHKSUBJECT = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId)
                    .child("classSubjectData").child(subjectUniPushId);
            databaseReferenceCHKSUBJECT.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ll_bottom_send.setVisibility(View.VISIBLE);
                    } else {
                        ll_bottom_send.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        DatabaseReference databaseReferenceTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("clickedGroupPushId");

        databaseReferenceTemp.addValueEventListener(new

                                                            ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    parentItemArrayListClassName.clear();
                                                                    String groupPushId = String.valueOf(snapshot.getValue());

                                                                    DatabaseReference databaseReferenceAdminShow = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList").child(userID);
                                                                    databaseReferenceAdminShow.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            Log.d("CHKADMINCLASS", "onDataChange: " + snapshot.child("admin").getValue());
                                                                            if (Objects.equals(snapshot.child("admin").getValue(), true)) {
                                                                                checkAdmmmmin(true);
                                                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                                                                                databaseReference.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        Log.d(TAG, "showChildGroupAdaptor: Clicked" + snapshot.getKey());
                                                                                        parentItemArrayListClassName.clear();
                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                        Log.d(TAG, "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());

                                                                                            if (dataSnapshot.getChildrenCount() > 0) {
                                                                                                ib_servSettings.setVisibility(View.VISIBLE);
                                                                                            }

                                                                                            Class_Group_Names class_group_names = new Class_Group_Names();
                                                                                            class_group_names.setGroupPushId(groupPushId);
                                                                                            class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));
                                                                                            class_group_names.setClassBio(dataSnapshot.child("classBio").getValue(String.class));
                                                                                            class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));

                                                                                            Log.d("JOIN", "onClick: " + groupPushId);

                                                                                            List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("classSubjectData").getChildren()) {
                                                                                                Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                                                                Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                                                                                                Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                                                                                                subjectDetailsModelList.add(object);

                                                                                            }

                                                                                            class_group_names.setChildItemList(subjectDetailsModelList);


                                                                                            List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("classStudentList").getChildren()) {
                                                                                                Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                                                                Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                                                                                                Log.d("CHKSUB", "onDataChange: " + class_student_details.getUserName());
                                                                                                class_student_detailsList.add(class_student_details);

                                                                                            }

                                                                                            class_group_names.setClass_student_detailsList(class_student_detailsList);


                                                                                            parentItemArrayListClassName.add(class_group_names);
                                                                                        }
                                                                                        adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                                                                                        adapter_classGroup.notifyDataSetChanged();
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });
                                                                            } else {
//                            Toast.makeText(Server_Activity.this, "You are an admin", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                                    DatabaseReference databaseReferenceGrpClass = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(groupPushId).child(userID).child("classUniPushId");

                                                                    databaseReferenceGrpClass.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                            String classUniPush = String.valueOf(snapshot.getValue());

                                                                            DatabaseReference databaseReferenceStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPush);

                                                                            databaseReferenceStudent.child("classStudentList").child(userID).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                    Log.d("CHKADMIN", "onDataChange: " + snapshot.exists());
                                                                                    if (Objects.equals(snapshot.getValue(), false)) {
                                                                                        checkAdmmmmin(false);
                                                                                        databaseReferenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                parentItemArrayListClassName.clear();
                                                                                                Log.d("OTH", "class Name: " + snapshot.child("className").getValue());

                                                                                                Class_Group_Names class_group_names = new Class_Group_Names();
                                                                                                class_group_names.setGroupPushId(groupPushId);
                                                                                                class_group_names.setClassName(snapshot.child("className").getValue(String.class));
                                                                                                class_group_names.setClassBio(snapshot.child("classBio").getValue(String.class));
                                                                                                class_group_names.setUniPushClassId(snapshot.child("classUniPushId").getValue(String.class));

                                                                                                Log.d("JOIN", "onClick: " + groupPushId);

                                                                                                List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                                                                                                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                                                                                                    Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                                                                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                                                                                                    Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                                                                                                    subjectDetailsModelList.add(object);
                                                                                                }

                                                                                                class_group_names.setChildItemList(subjectDetailsModelList);


                                                                                                List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                                                                                                for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                                                                                                    Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                                                                    Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                                                                                                    Log.d("CHKSUB", "onDataChange: " + class_student_details.getUserName());
                                                                                                    class_student_detailsList.add(class_student_details);

                                                                                                }

                                                                                                class_group_names.setClass_student_detailsList(class_student_detailsList);


                                                                                                parentItemArrayListClassName.add(class_group_names);

                                                                                                adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                                                                                                adapter_classGroup.notifyDataSetChanged();
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });

                                                                                    } else {
//                                    Toast.makeText(Server_Activity.this, "You are not admin", Toast.LENGTH_SHORT).show();
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


                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });


        if (subjectUniPushId != null) {
            DatabaseReference refGetSubjectN = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId)
                    .child(classUniPushId).child("classSubjectData").child(subjectUniPushId);
            refGetSubjectN.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        textViewSubjectName.setText(snapshot.child("subjectName").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        assert groupPushId != null;
        DatabaseReference refGetGRPName = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId);
        refGetGRPName.addValueEventListener(new

                                                    ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.child("groupName").exists()) {
                                                                textViewGroupName.setText(snapshot.child("groupName").getValue().toString());
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


        reference = FirebaseDatabase.getInstance().

                getReference().

                child("Groups").

                child("Chat_Message").

                child(groupPushId).

                child(classUniPushId).

                child(subjectUniPushId);

        readLiveMessageListener = new

                ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chats.clear();
                        Log.d(TAG, "onDataChange: " + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Log.d("DOUBTCHK", "onDataChange: " + postSnapshot.getValue());

                            Class_Group class_userDashBoard = postSnapshot.getValue(Class_Group.class);
                            chats.add(class_userDashBoard);
                        }
                        messageAdapter.setList(chats);
                        messageAdapter.notifyDataSetChanged();
                        rv_ChatDashboard.smoothScrollToPosition(Objects.requireNonNull(rv_ChatDashboard.getAdapter()).getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }

        ;

        reference.addValueEventListener(readLiveMessageListener);

        allDoubtReference = FirebaseDatabase.getInstance().

                getReference().

                child("Groups").

                child("Doubt").

                child(groupPushId).

                child(classUniPushId).

                child(subjectUniPushId).

                child("All_Doubt");

        if (allDoubtReference != null) {
            readLiveDoubtListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    doubts.clear();
                    if (snapshot.getChildrenCount() > 0) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                            doubts.add(class_userDashBoard);

                            Log.d("SETDOUBT", "onClick: " + dataSnapshot.getValue());

                            messageAdapter.setDoubt(doubts);
                            messageAdapter.notifyDataSetChanged();
                            rv_ChatDashboard.smoothScrollToPosition(Objects.requireNonNull(rv_ChatDashboard.getAdapter()).getItemCount());
                        }
                    } else {
//                        Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            allDoubtReference.addValueEventListener(readLiveDoubtListener);

        }

        refClassTeacherList = FirebaseDatabase.getInstance().

                getReference().

                child("Groups").

                child("Check_Group_Admins").

                child(groupPushId).

                child("classAdminList");
        listGrpTeacherList.clear();
        refClassTeacherList.addChildEventListener(new

                                                          ChildEventListener() {
                                                              @Override
                                                              public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                  Class_Student_Details class_teacher_details = snapshot.getValue(Class_Student_Details.class);
                                                                  listGrpTeacherList.add(class_teacher_details);


                                                                  rv_GrpTeacherList.setAdapter(showGrpTeacherList);
                                                                  showGrpTeacherList.notifyDataSetChanged();
                                                              }

                                                              @Override
                                                              public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                                                                      previousChildName) {

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

        DatabaseReference refGrpTeacherList = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId);
        refGrpTeacherList.addListenerForSingleValueEvent(new

                                                                 ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                         if (snapshot.hasChild("classAdminList")) {
                                                                             Log.d("STUCHK", "onSUBS: yes");
                                                                             rv_GrpTeacherList.setVisibility(View.VISIBLE);
                                                                             adminListText.setVisibility(View.VISIBLE);
                                                                         } else {
                                                                             Log.d("STUCHK", "onSUBS: nooo");
                                                                             rv_GrpTeacherList.setVisibility(View.GONE);
                                                                             adminListText.setVisibility(View.GONE);
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(@NonNull DatabaseError error) {

                                                                     }
                                                                 });


        Log.d(TAG, "onSUBS: " + classUniPushId);

        DatabaseReference refGrpMembers = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId);
        refGrpMembers.addListenerForSingleValueEvent(new

                                                             ValueEventListener() {
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                     if (snapshot.hasChild("classStudentList")) {
                                                                         Log.d("STUCHK", "onSUBS: yes");
                                                                         rv_GrpMemberList.setVisibility(View.VISIBLE);
                                                                         tv_GroupMember.setVisibility(View.VISIBLE);
                                                                     } else {
                                                                         Log.d("STUCHK", "onSUBS: nooo");
                                                                         rv_GrpMemberList.setVisibility(View.GONE);
                                                                         tv_GroupMember.setVisibility(View.GONE);
                                                                     }
                                                                 }

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                                 }
                                                             });


        refGrpMemberList = FirebaseDatabase.getInstance().

                getReference().

                child("Groups").

                child("All_GRPs").

                child(groupPushId).

                child(classUniPushId).

                child("classStudentList");

        refGrpMemberList.addValueEventListener(new

                                                       ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                               listGrpMemberList.clear();
                                                               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                   Class_Student_Details class_student_details = snapshot.getValue(Class_Student_Details.class);
                                                                   listGrpMemberList.add(class_student_details);

                                                                   rv_GrpMemberList.setAdapter(showGrpMemberList);
                                                                   showGrpMemberList.notifyDataSetChanged();
                                                               }
                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                           }
                                                       });

        adapter_all_friends.setOnItemClickListener(new Adapter_All_Friends.OnItemClickListener() {
            @Override
            public void MemberProfile(String memberUserId, String memberUserName) {
                showBtmDialogUserProfile(memberUserId, memberUserName);
            }
        });


        //Endpanel
        showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember.OnItemClickListener() {
            @Override
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String
                    adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "AddFriend");
            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String
                    adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "FollowFriend");
            }

            @Override
            public void MemberProfile(String memberUserId, String memberUserName) {
                showBtmDialogUserProfile(memberUserId, memberUserName);
            }

        });


    }

    private void sendPdf() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 438);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onScreen) {
            reference.addValueEventListener(readLiveMessageListener);
            allDoubtReference.addValueEventListener(readLiveDoubtListener);

            onScreen = false;
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_server);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        SharePref sharePref = new SharePref(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();


            DatabaseReference chkJoinedORAddGRP = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID);
            chkJoinedORAddGRP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d("CHKJOINEDORADDGRP", "onDataChange: available");
                        ll_AddJoinGrp.setVisibility(View.GONE);
                    } else {
                        Log.d("CHKJOINEDORADDGRP", "onDataChange: Not available");
                        ll_AddJoinGrp.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            init();

            ib_pdf_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPdf();
                }
            });

            DatabaseReference dbSaveGroupPosition = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID);

            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, 0) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    int fromPosition = viewHolder.getAdapterPosition();
                    int toPosition = target.getAdapterPosition();
//                Toast.makeText(Edit_RollNumber.this, "From" + fromPosition + "gggto" + toPosition, Toast.LENGTH_SHORT).show();
                    //  Log.d("POSS", "from position: "+listGrpMemberList.get(fromPosition).getUserId());
                    Log.d("POSS", "to Position: " + toPosition);
                    Collections.swap(list_OtherUserPublicGroupTitle, fromPosition, toPosition);
                    showOtherUserPublicGroupAdaptor.notifyItemMoved(fromPosition, toPosition);

                    for (int i = 0; i < list_OtherUserPublicGroupTitle.size(); i++) {
                        Log.d("POSS", "Members position: " + i + " = " + list_OtherUserPublicGroupTitle.get(i).getPosition());
                        dbSaveGroupPosition.child(list_OtherUserPublicGroupTitle.get(i).getPosition()).child("grpPosition").setValue(i);
                    }

                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                }
            };


            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(rv_OtherPublicGroupTitle);


            swipe_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    overlappingPanels.openStartPanel();
                }
            });


            swipe_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    overlappingPanels.openEndPanel();
                }
            });

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    Intent intent = new Intent(Server_Activity.this, Server_Activity.class);
                    intent.putExtra("notOpen", "notOpen");
                    startActivity(intent);

                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            et_ctext = findViewById(R.id.et_ctext);

            et_ctext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String c = String.valueOf(s);

                    if (c.trim().isEmpty()) {
                        ib_doubtSubmit.setVisibility(View.VISIBLE);
                        ib_csubmit.setVisibility(View.GONE);
                    } else {
                        ib_doubtSubmit.setVisibility(View.GONE);
                        ib_csubmit.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            overlappingPanels.registerStartPanelStateListeners(new OverlappingPanelsLayout.PanelStateListener() {
                @Override
                public void onPanelStateChange(@NonNull PanelState panelState) {
                    String store = panelState.toString();
                    Log.d("SCROLLCHANGE", "onScrollChange: " + store);
                    String[] words = store.split("\\.");
                    if (words[3].startsWith("PanelState$Opened")) {
                        Log.d("SCROLLCHANGESPLIT", "onScrollChange: " + (words[3]));
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                    if (words[3].startsWith("PanelState$Closed")) {
                        Log.d("SCROLLCHANGESPLIT", "onScrollChange: " + (words[3]));
                        bottomNavigationView.setVisibility(View.GONE);
                    }
                }
            });

            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
            SharePref.setDataPref(Constant.USER_ID, userID);
            userEmailID = currentUser.getEmail();
            userPhoto = currentUser.getPhotoUrl();
            userName = currentUser.getDisplayName();


            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("61526858520-djs6utqbssvf5p5iqnki3buievn9ufhg.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


            bottomMenu();


            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();


            refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(userID);
            refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID);
            refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
            refShowUserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID);
            refShowUserJoinedGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class");

            refTeachStud = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups");
            refOtherUserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
            refUserStatus = FirebaseDatabase.getInstance().getReference().child("Registration");


            checkFriends = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends");

            Log.d("TAG", "onCreate: " + checkFriends);


//            rv_ChatDashboard.setLayoutManager(new LinearLayoutManager(this));

//---------------------------------------------------------------------------------------------------------------------------------------------------
            /*

            Left Panel

             */

            //add server option clicked(+)
            imageViewAddPanelAddGroup.setOnClickListener(view ->

            {
                Intent intent = new Intent(Server_Activity.this, Create_Server.class);
                startActivity(intent);
            });

            chatListDashboard();

            ImageViewRecentChat.setOnClickListener(view ->

            {

                tv_GroupMember.setVisibility(View.GONE);
                adminListText.setVisibility(View.GONE);
                FriendListText.setVisibility(View.VISIBLE);
                FriendListTextt.setVisibility(View.VISIBLE);
                rv_GrpTeacherList.setVisibility(View.GONE);
                rv_GrpMemberList.setVisibility(View.GONE);
                friendSection.setVisibility(View.VISIBLE);
                onlyAdminLayout.setVisibility(View.GONE);

                imageViewAddPanelAddGroup.setVisibility(View.GONE);
                endPanelAllFriendsRecyclerView.setVisibility(View.VISIBLE);
                textViewGroupName.setText("Recent Chats");

                groupSection.setVisibility(View.GONE);

//                    rvFriendsList.setVisibility(View.VISIBLE);

                rvFriendsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvFriendsList.setAdapter(adaptor_friendsList);

                adaptor_friendsList.setFriendListClick(new Adaptor_FriendsList.OnItemClickListener() {
                    @Override
                    public void onFriendClick(String friendName, String friendUserId) {

                        overlappingPanels.closePanels();

//                            ll_AddJoinGrp.setVisibility(View.GONE);
                        if (flagFriend == false) {

                            flagFriend = true;
                        }
                        friendChatFragment = new Friend_Chat_Activity();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.below_toolbar, friendChatFragment).addToBackStack(friendChatFragment.getClass().getSimpleName()).commit();

                        friendToolBar.setVisibility(View.VISIBLE);
                        textViewSubjectName.setVisibility(View.GONE);

                        Bundle bundle = new Bundle();
                        bundle.putString("name", friendName);
                        bundle.putString("receiverUid", friendUserId);
                        friendChatFragment.setArguments(bundle);

                        TextView friendNameTv = findViewById(R.id.tv_friend_name);
                        friendNameTv.setText(friendName);

                        CircleImageView friendImg = findViewById(R.id.friendImg);

                        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(friendUserId);
                        refUserProfPic.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("Name").exists()) {
                                    String userName = snapshot.child("Name").getValue().toString();
                                    friendNameTv.setText(userName);
                                }
                                if (snapshot.child("profilePic").exists()) {
                                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                                    Glide.with(Server_Activity.this).load(profilePicUrl).into(friendImg);
                                } else {
                                    Glide.with(Server_Activity.this).load(R.drawable.maharaji).into(friendImg);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

            });

            refShowUserJoinedGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("JOINEDGRP", "onDataChange: " + dataSnapshot1.getKey());
                            String chkUserID = dataSnapshot1.getKey();
                            if (chkUserID.equals(userID)) {
//                                ll_AddJoinGrp.setVisibility(View.GONE);
                                if (getIntent().hasExtra("notOpen")) {
                                    Log.d("JOINEDGRP", "onDataChange: No panel");
                                } else {
                                    overlappingPanels.openStartPanel();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            refShowUserAllGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {
//                        ll_AddJoinGrp.setVisibility(View.GONE);
                        if (getIntent().hasExtra("notOpen")) {
                            Log.d("JOINEDGRP", "onDataChange: No panel");
                        } else {
                            overlappingPanels.openStartPanel();
                        }

                    }
                    if (snapshot.getChildrenCount() < 0) {
//                        Toast.makeText(Server_Activity.this, "Please create Group using left swipe", Toast.LENGTH_SHORT).show();
//                        ll_AddJoinGrp.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


//---------------------------------------------------------------------------------------------------------------------------------------------------

            /*
                Center Panel
             */
            ib_cattach =

                    findViewById(R.id.ib_cattach);

            ib_csubmit =

                    findViewById(R.id.ib_csubmit);

            et_ctext =

                    findViewById(R.id.et_ctext);

            btn_cAddGroup =

                    findViewById(R.id.btn_caddgroup);

            btn_cJoinGroup =

                    findViewById(R.id.btn_cjoingroup);

            ll_bottom_send =

                    findViewById(R.id.ll_bottom_send);


            btn_cJoinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Server_Activity.this, Discover_Activity.class);
                    startActivity(intent);
                }
            });

            btn_cAddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Server_Activity.this, Create_Server.class);
                    startActivity(intent);

//                    createGroupDialog();
                }
            });

//---------------------------------------------------------------------------------------------------------------------------------------------------



            /*
                Right Panel
             */


            showOtherUserGroupRV();

        } else {
            startActivity(new Intent(this, getStarted.class));
            this.finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void checkDarkLightDefault() {

        DatabaseReference setDarkLightDefault = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);

        setDarkLightDefault.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("DarkLightDefault").exists()) {

//                    Log.d("DARKEXISTS", "onDataChange: " + snapshot.child("DarkLightDefault").exists());

                    String darkLightDefaultVal = snapshot.child("DarkLightDefault").getValue().toString();

                    Log.d("TAG", "onCreate: " + darkLightDefaultVal);
                    if (darkLightDefaultVal.equals("Dark")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    if (darkLightDefaultVal.equals("Light")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    if (darkLightDefaultVal.equals("Default")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                } else {
                    Log.d("DARKEXISTS", "onDataChange: " + snapshot.child("DarkLightDefault").exists());
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Doubt bottom sheet layout
    private void showAddDoubtBtmDialog(String groupPushId, String subGroupPushId, String groupClassSubjects) {
        final Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_doubt);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Cancel = bottomSheetDialog.findViewById(R.id.btn_Cancel);
        Button btn_Submit = bottomSheetDialog.findViewById(R.id.btn_Submit);
        EditText et_AddTopic = bottomSheetDialog.findViewById(R.id.et_AddTopic);
        EditText et_AddDoubt = bottomSheetDialog.findViewById(R.id.et_AddDoubt);

        et_AddDoubt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String c = String.valueOf(s);

                if (c.trim().isEmpty() && et_AddTopic != null && et_AddDoubt != null) {
                    btn_Submit.setEnabled(false);
                    btn_Submit.setTextColor(getResources().getColor(R.color.iconColor));
                } else {
                    btn_Submit.setEnabled(true);
                    btn_Submit.setTextColor(getResources().getColor(R.color.iconPrimaryColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());


        //Adding doubt
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addTopic = et_AddTopic.getText().toString().trim();
                String addDoubt = et_AddDoubt.getText().toString().trim();
                if (addDoubt.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
                }
                if (addTopic.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
                }
                if (!addDoubt.isEmpty() && !addTopic.isEmpty()) {
                    DatabaseReference allDoubtReference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubjects).child("All_Doubt");
                    allDoubtReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "Doubtno_" + noofGroupinCategory + "_" + addTopic;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, subGroupPushId, groupClassSubjects, addTopic, addDoubt);

                            allDoubtReference.child(push).setValue(userAddGroupClass);

                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId, subGroupPushId, addDoubt, "doubt", groupClassSubjects, push, "", "");
                            reference.push().setValue(userAddGroupClass);
//                            Toast.makeText(Server_Activity.this, "Doubt Successfully Added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubjects).child("Topic");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "Doubtno_" + noofGroupinCategory + "_" + addTopic;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, subGroupPushId, groupClassSubjects, addTopic, addDoubt);
                            reference.child(push).setValue(userAddGroupClass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                bottomSheetDialog.dismiss();

            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        moveTaskToBack(false);

    }

    /*
    Other group part
     */
    public void showOtherUserGroupRV() {

        DatabaseReference clickedGroupPush = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        clickedGroupPush.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ib_servSettings.setOnClickListener(view -> {

                    Log.d("REFGRPP", "onDataChange: " + snapshot.child("clickedGroupPushId").getValue().toString());

                    Intent intent = new Intent(Server_Activity.this, Server_Settings.class);
                    intent.putExtra("currUserId", userID);
                    intent.putExtra("groupPushId", snapshot.child("clickedGroupPushId").getValue().toString());
//                    intent.putExtra("serverName", snapshot.child("serverName").getValue().toString());
                    startActivity(intent);
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference userJoinedOrAddServer = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID);
        userJoinedOrAddServer.orderByChild("grpPosition").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("UserAddedOrJoinedGrpPUSHIDS", "" + dataSnapshot.getKey());

                    String UserAddedOrJoinedGrpPUSHIDS = dataSnapshot.getKey();
                    if (dataSnapshot.exists() && UserAddedOrJoinedGrpPUSHIDS != null) {

                        if (Objects.equals(dataSnapshot.child("addedOrJoined").getValue(), "Added")
                                || Objects.equals(dataSnapshot.child("addedOrJoined").getValue(), "TeacherJoin")) {

                            DatabaseReference databaseReferenceCHKADMIN = FirebaseDatabase.getInstance().getReference()
                                    .child("Groups").child("Check_Group_Admins").child(UserAddedOrJoinedGrpPUSHIDS).child("classAdminList");

                            databaseReferenceCHKADMIN.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String adminUserId = dataSnapshot1.child("userId").getValue().toString();
                                        refOtherUserPublicGroup.child(UserAddedOrJoinedGrpPUSHIDS).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot newSnap) {

                                                Class_Group userQuestionsAdded = newSnap.getValue(Class_Group.class);

                                                if (newSnap.exists() && adminUserId.equals(currentUser.getUid())) {
                                                    Log.i("SBO", dataSnapshot1.child("userId").getValue().toString());
                                                    list_OtherUserPublicGroupTitle.add(userQuestionsAdded);
//                                                    showOtherUserPublicGroupAdaptor.setmDatalistNew(list_OtherUserPublicGroupTitle);
                                                    showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            DatabaseReference databaseReferenceGetUserJoinedClass = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(UserAddedOrJoinedGrpPUSHIDS);
                            databaseReferenceGetUserJoinedClass.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot01) {
                                    if (snapshot01.child(userID).child("classUniPushId").exists()) {

                                        refOtherUserPublicGroup.child(UserAddedOrJoinedGrpPUSHIDS).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot otherSnap) {

                                                if (otherSnap.child("User_Subscribed_Groups").child(userID).exists()) {
                                                    String check = otherSnap.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
                                                    if (check.equals("Class Member")) {
                                                        Class_Group userQuestions = otherSnap.getValue(Class_Group.class);
                                                        list_OtherUserPublicGroupTitle.add(userQuestions);
//                                                        showOtherUserPublicGroupAdaptor.setmDatalistNew(list_OtherUserPublicGroupTitle);
                                                        showOtherUserPublicGroupAdaptor.notifyDataSetChanged();

                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showOtherUserPublicGroupAdaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID, String groupCategory) {


                btn_joinNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast();
                        btn_joinNotification.setEnabled(false);
                    }
                });

                btn_lteachattend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast();
                        btn_lteachattend.setEnabled(false);
                    }
                });

                memVis(false);

                if (flagFriend == true) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    manager.getBackStackEntryCount();
                    transaction.remove(friendChatFragment);
                    transaction.commit();
                    flagFriend = false;
                }

                tv_GroupMember.setVisibility(View.VISIBLE);
                adminListText.setVisibility(View.VISIBLE);
                rv_GrpTeacherList.setVisibility(View.VISIBLE);
                if (listGrpMemberList != null) {
                    rv_GrpMemberList.setVisibility(View.VISIBLE);
                }
                friendSection.setVisibility(View.GONE);
                FriendListText.setVisibility(View.GONE);
                FriendListTextt.setVisibility(View.GONE);

//                ll_AddJoinGrp.setVisibility(View.VISIBLE);
                imageViewAddPanelAddGroup.setVisibility(View.VISIBLE);
                endPanelAllFriendsRecyclerView.setVisibility(View.GONE);
                groupSection.setVisibility(View.VISIBLE);

                textViewGroupName.setText(groupName.trim());


                DatabaseReference databaseReferenceTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("clickedGroupPushId");

                databaseReferenceTemp.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        parentItemArrayListClassName.clear();
                        String groupPushId = String.valueOf(snapshot.getValue());

                        DatabaseReference databaseReferenceGrpClass = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(groupPushId).child(userID).child("classUniPushId");

                        databaseReferenceGrpClass.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String classUniPush = String.valueOf(snapshot.getValue());

                                refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList");

                                refGrpMemberList.addChildEventListener(new ChildEventListener() {
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

                                DatabaseReference databaseReferenceStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPush);

                                databaseReferenceStudent.child("classStudentList").child(userID).child("admin").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        listGrpMemberList.clear();
                                        Log.d("CHKADMIN", "onDataChange: " + snapshot.getValue());

                                        if (Objects.equals(snapshot.getValue(), false)) {
                                            checkAdmmmmin(false);
                                            databaseReferenceStudent.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    parentItemArrayListClassName.clear();
                                                    Log.d("OTH", "class Name: " + snapshot.child("className").getValue());

                                                    Class_Group_Names class_group_names = new Class_Group_Names();
                                                    class_group_names.setGroupPushId(groupPushId);
                                                    class_group_names.setClassName(snapshot.child("className").getValue(String.class));
                                                    class_group_names.setClassBio(snapshot.child("classBio").getValue(String.class));
                                                    class_group_names.setUniPushClassId(snapshot.child("classUniPushId").getValue(String.class));

                                                    Log.d("JOIN", "onClick: " + groupPushId);

                                                    List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                                                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                                                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                                                        Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                                                        subjectDetailsModelList.add(object);

                                                    }

                                                    class_group_names.setChildItemList(subjectDetailsModelList);


                                                    List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                                                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                                                        Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                        Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                                                        Log.d("CHKSUB", "onDataChange: " + class_student_details.getUserName());
                                                        class_student_detailsList.add(class_student_details);

                                                    }

                                                    class_group_names.setClass_student_detailsList(class_student_detailsList);


                                                    parentItemArrayListClassName.add(class_group_names);

                                                    adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                                                    adapter_classGroup.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        } else {
                                            checkAdmmmmin(true);
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    listGrpMemberList.clear();
                                                    Log.d(TAG, "showChildGroupAdaptor: Clicked" + snapshot.getKey());
                                                    parentItemArrayListClassName.clear();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Log.d(TAG, "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());

                                                        if (dataSnapshot.getChildrenCount() > 0) {
                                                            ib_servSettings.setVisibility(View.VISIBLE);
                                                        }

                                                        Class_Group_Names class_group_names = new Class_Group_Names();
                                                        class_group_names.setGroupPushId(groupPushId);
                                                        class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));
                                                        class_group_names.setClassBio(dataSnapshot.child("classBio").getValue(String.class));
                                                        class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));

                                                        Log.d("JOIN", "onClick: " + groupPushId);

                                                        List<Subject_Details_Model> subjectDetailsModelList = new ArrayList<>();

                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("classSubjectData").getChildren()) {
                                                            Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                            Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                                                            Log.d("CHKSUB", "onDataChange: " + object.getSubjectName());
                                                            subjectDetailsModelList.add(object);

                                                        }

                                                        class_group_names.setChildItemList(subjectDetailsModelList);


                                                        List<Class_Student_Details> class_student_detailsList = new ArrayList<>();

                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("classStudentList").getChildren()) {
                                                            Log.d("CHKSUB", "onClick: " + dataSnapshot1.getValue());
                                                            Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                                                            Log.d("CHKSUB", "onDataChange: " + class_student_details.getUserName());
                                                            class_student_detailsList.add(class_student_details);

                                                        }

                                                        class_group_names.setClass_student_detailsList(class_student_detailsList);


                                                        parentItemArrayListClassName.add(class_group_names);
                                                    }
                                                    adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                                                    adapter_classGroup.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
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


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


        });

        friendSection.setVisibility(View.GONE);


    }

    //Toast for subject select warning
    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_subject_select, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    //personal dom chat dashboard
    private void chatListDashboard() {
        FirebaseDatabase.getInstance().getReference().child("chats").child("RecentChats").child(userID).child("recentChatUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsListClasses.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FriendsListClass friendsListClass = dataSnapshot.getValue(FriendsListClass.class);
                    friendsListClasses.add(friendsListClass);
                    adaptor_friendsList.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refFriendList.getReference().child("Users").child("Friends").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFriendsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String friendsList = postSnapshot.getKey();
                    allFriendsList.add(friendsList);
//                    Toast.makeText(getApplicationContext(), "........" + friendsList.getUserName(), Toast.LENGTH_SHORT).show();
                }
                adapter_all_friends.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void showDoubt(String groupPushId, String subGroupPushId, String groupClassSubjects) {
        ib_doubtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDoubtBtmDialog(groupPushId, subGroupPushId, groupClassSubjects);
            }
        });
    }


    private void sentInvitation(String adminGroupID, String adminUserName, String Code) {
        String req = null, notifyStatus = null;
        if (Code.equals("AddFriend")) {
            req = "Friend";
            notifyStatus = "Friend_Request";
        } else if (Code.equals("FollowFriend")) {
            req = "Follow";
            notifyStatus = "Follow_Request";
        }
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        String finalNotifyStatus = notifyStatus;
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Do you want to send " + req + " request to" + adminUserName + "?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                final String userID = currentUser.getUid();
                                final String userName = currentUser.getDisplayName();
                                final String userEmail = currentUser.getEmail();
                                final Uri userPhoto = currentUser.getPhotoUrl();
                                DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(adminGroupID);
                                DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(userID);

                                Log.d("Friend", "Admin Id: \t" + adminUserName + "\nUser Id: \t" + userName);

                                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                        String push = "Joining Reqno_" + noofQuesinCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                        Class_Group userAdd = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, push, "groupName", "groupPushId", finalNotifyStatus);
                                        refjoiningReq.child(push).setValue(userAdd);
                                        refacceptingReq.child(push).setValue(userAdd);

                                        DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(userID).child(adminGroupID);
                                        checkFRNDReq.child("reqStatus").setValue("Requested");

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });


                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertdialogbuilder.create();
        alert.show();
    }


    private void showBtmDialogUserProfile(String memberUserId, String memberUserName) {

        Dialog btmSheetUserProfile = new Dialog(this);
        btmSheetUserProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        btmSheetUserProfile.setCancelable(true);
        btmSheetUserProfile.setCanceledOnTouchOutside(true);
        btmSheetUserProfile.setContentView(R.layout.bottomsheet_profileothers);
        btmSheetUserProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DatabaseReference refUserStatus, refUserFollowers, refUserFollowing;

        Button btn_SentFrndReq = btmSheetUserProfile.findViewById(R.id.btn_SentFrndReq);
        Button btn_FollowFrnd = btmSheetUserProfile.findViewById(R.id.btn_FollowFrnd);
        Button btn_messageFrnd = btmSheetUserProfile.findViewById(R.id.btn_message);
        Button btn_unfollow = btmSheetUserProfile.findViewById(R.id.btn_unfollow);

        TextView tv_UserName = btmSheetUserProfile.findViewById(R.id.tv_UserName);
        TextView tv_Name = btmSheetUserProfile.findViewById(R.id.tv_Name);

        TextView tv_UserBio = btmSheetUserProfile.findViewById(R.id.tv_UserBio);
        TextView tv_UserInstitute = btmSheetUserProfile.findViewById(R.id.tv_UserInstitute);
        TextView tv_UserUserName = btmSheetUserProfile.findViewById(R.id.tv_UserUserName);
        TextView tv_UserLocation = btmSheetUserProfile.findViewById(R.id.tv_userLocation);

        LinearLayout ll_bio = btmSheetUserProfile.findViewById(R.id.ll_bio);
        LinearLayout ll_Institution = btmSheetUserProfile.findViewById(R.id.ll_Institution);
        LinearLayout ll_location = btmSheetUserProfile.findViewById(R.id.ll_location);
        LinearLayout ll_UserName = btmSheetUserProfile.findViewById(R.id.ll_UserName);


        TextView tv_UserCategory = btmSheetUserProfile.findViewById(R.id.tv_userCategory);
        TextView tv_addCategory = btmSheetUserProfile.findViewById(R.id.tv_addCategory);
        LinearLayout unfollowAndMessage = btmSheetUserProfile.findViewById(R.id.unfollowAndMessage);

        TextView tv_CountFollowers = btmSheetUserProfile.findViewById(R.id.tv_CountFollowers);
        TextView tv_CountFollowing = btmSheetUserProfile.findViewById(R.id.tv_CountFollowing);


        CircleImageView prof_pic_BtmSheet = btmSheetUserProfile.findViewById(R.id.prof_pic);

        DatabaseReference refUserStatusProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(memberUserId);
        refUserStatusProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()) {
                    String userName = snapshot.child("Name").getValue().toString();
                    tv_Name.setText(userName);
                }
                if (snapshot.child("profilePic").exists()) {
                    String profilePic = snapshot.child("profilePic").getValue().toString();
                    if (!(Server_Activity.this).isFinishing()) {
                        Glide.with(getApplicationContext()).load(profilePic).into(prof_pic_BtmSheet);
                    }
                } else {
                    if (!(Server_Activity.this).isFinishing()) {
                        Glide.with(getApplicationContext()).load(R.drawable.maharaji).into(prof_pic_BtmSheet);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();


        DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(userID).child(memberUserId).child("reqStatus");

        checkFRNDReq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Log.d("FRNDCHKK", "onDataChange: " + snapshot.getValue());

                    if (snapshot.getValue().equals("Accepted")) {
                        unfollowAndMessage.setVisibility(View.VISIBLE);
                        btn_FollowFrnd.setVisibility(View.GONE);
                    }

                    if (snapshot.getValue().equals("Requested")) {
                        btn_FollowFrnd.setVisibility(View.VISIBLE);
                        btn_FollowFrnd.setText("Requested");
                        btn_FollowFrnd.setEnabled(false);
                    }

                    if (snapshot.getValue().equals("Rejected")) {
                        btn_FollowFrnd.setVisibility(View.VISIBLE);
                    }

                } else {
                    btn_FollowFrnd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_messageFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btmSheetUserProfile.dismiss();

//                ll_AddJoinGrp.setVisibility(View.GONE);
                overlappingPanels.closePanels();
                if (flagFriend == false) {

                    flagFriend = true;
                }

                friendChatFragment = new Friend_Chat_Activity();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.below_toolbar, friendChatFragment).addToBackStack(friendChatFragment.getClass().getSimpleName()).commit();

                Bundle bundle = new Bundle();
                bundle.putString("name", memberUserName);
                bundle.putString("receiverUid", memberUserId);
                friendChatFragment.setArguments(bundle);

            }
        });

        refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(memberUserId);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("Bio").exists()) {
                        String bio = snapshot.child("Bio").getValue().toString();
                        ll_bio.setVisibility(View.VISIBLE);
                        tv_UserBio.setText(bio);
                    } else {
                        ll_bio.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Insitution").exists()) {
                        String bio = snapshot.child("Insitution").getValue().toString();
                        ll_Institution.setVisibility(View.VISIBLE);
                        tv_UserInstitute.setText(bio);
                    } else {
                        ll_Institution.setVisibility(View.GONE);
                    }

                    if (snapshot.child("NickName").exists()) {
                        String UserName = snapshot.child("NickName").getValue().toString();
                        tv_UserUserName.setVisibility(View.VISIBLE);
                        tv_UserUserName.setText(UserName);
                        ll_UserName.setVisibility(View.VISIBLE);
                    } else {
                        ll_UserName.setVisibility(View.GONE);
                    }
                    if (snapshot.child("Location").exists()) {
                        ll_location.setVisibility(View.VISIBLE);
                        String Location = snapshot.child("Location").getValue().toString();
                        tv_UserLocation.setText(Location);
                    } else {
                        ll_location.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Category").exists()) {
                        tv_addCategory.setVisibility(View.GONE);
                        String Category = snapshot.child("Category").getValue().toString();
                        tv_UserCategory.setVisibility(View.VISIBLE);
                        tv_UserCategory.setText(Category);
                    } else {
                        tv_addCategory.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowers = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(memberUserId);
        refUserFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    if (count < 2) {
                        tv_CountFollowing.setText(" Following - " + (int) count);
                    } else {
                        tv_CountFollowing.setText(" Following - " + (int) count);
                    }
                } else {
                    tv_CountFollowing.setText("Following - 0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btn_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Server_Activity.this);
                alertdialogbuilder.setTitle("Please confirm !!!")
                        .setMessage("Do you really want to unfollow " + memberUserName + " ? \nThis will clear the chats of You and " + memberUserName + " and will be removed from your friend list!!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference delFriends = FirebaseDatabase.getInstance().getReference().child("Users");
                                delFriends.child("Friends").child(userID).child(memberUserId).removeValue();
                                delFriends.child("Friends").child(memberUserId).child(userID).removeValue();

                                delFriends.child("checkUserFriendReq").child(userID).child(memberUserId).removeValue();

                                DatabaseReference delFriendsRecChats = FirebaseDatabase.getInstance().getReference().child("chats");
                                delFriendsRecChats.child("RecentChats").child(userID).child("recentChatUser").child(memberUserId).removeValue();
                                delFriendsRecChats.child("RecentChats").child(memberUserId).child("recentChatUser").child(userID).removeValue();

                                String myChatRoom = userID + memberUserId;
                                String friendChatRoom = memberUserId + userID;

                                delFriendsRecChats.child(myChatRoom).removeValue();
                                delFriendsRecChats.child(friendChatRoom).removeValue();

                                unfollowAndMessage.setVisibility(View.GONE);
                                btn_FollowFrnd.setVisibility(View.VISIBLE);


                            }
                        }).setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = alertdialogbuilder.create();
                alert.show();
            }
        });


        btn_FollowFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                sentInvitation(memberUserId, memberUserName, "AddFrnd");

                String adminGroupID, adminUserName, Code;

                adminGroupID = memberUserId;
                adminUserName = memberUserName;
                Code = "AddFrnd";


                String req = null, notifyStatus = null;
                if (Code.equals("AddFrnd")) {
                    req = "Friend";
                    notifyStatus = "Friend_Request";
                } else if (Code.equals("FollowFrnd")) {
                    req = "Follow";
                    notifyStatus = "Follow_Request";
                }
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Server_Activity.this);
                String finalNotifyStatus = notifyStatus;
                alertdialogbuilder.setTitle("Please confirm !!!")
                        .setMessage("Do you want to send " + req + " request to" + adminUserName + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(adminGroupID);
                                        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(userID);

                                        Log.d("Friend", "Admin Id: \t" + adminUserName + "\nUser Id: \t" + userName);

                                        refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                long noOfQuesInCategory = snapshot.getChildrenCount() + 1;
                                                String push = "Joining ReqNo_" + noOfQuesInCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                                Class_Group userAdd = new Class_Group(dateTimeCC, userName, "req_sent", userID, adminGroupID, userEmail, push, "groupName", "groupPushId", finalNotifyStatus);
                                                refjoiningReq.child(push).setValue(userAdd);
                                                refacceptingReq.child(push).setValue(userAdd);

                                                DatabaseReference checkFRNDReq = FirebaseDatabase.getInstance().getReference().child("Users").child("checkUserFriendReq").child(userID).child(adminGroupID);
                                                checkFRNDReq.child("reqStatus").setValue("Requested");

                                                btn_FollowFrnd.setText("Requested");
                                                btn_FollowFrnd.setEnabled(false);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });


                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = alertdialogbuilder.create();
                alert.show();

                Log.d("FRIEND", "friend's name: " + memberUserName + "\n friend's userId: " + memberUserId);

            }
        });
        btn_SentFrndReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sentInvitation(memberUserId, memberUserName, "FollowFriend");
            }
        });


        btmSheetUserProfile.show();
        btmSheetUserProfile.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btmSheetUserProfile.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btmSheetUserProfile.getWindow().setGravity(Gravity.BOTTOM);
        moveTaskToBack(false);
    }


    /*
        Google SignIn Result
     */
    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT).show();


        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            setDocumentInFB(fileUri, displayName);

            Log.d("BUNDLESTRING", "onDownloadClick: " + displayName);


        } else if (requestCode == 100) {
//            Toast.makeText(this, "100", Toast.LENGTH_SHORT).show();

            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
//            Toast.makeText(this, "checking", Toast.LENGTH_SHORT).show();
            if (signInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(Server_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    submitLoginData();

                                } else {
                                    Toast.makeText(Server_Activity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "google account null", Toast.LENGTH_SHORT).show();
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(this, "wrong request code", Toast.LENGTH_SHORT).show();

        }


    }

    private void setDocumentInFB(Uri fileUri, String fileName) {


        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectUniPushId = snapshot.child("subjectUniPushId").getValue().toString().trim();
                String uniPushClassId = snapshot.child("uniPushClassId").getValue().toString().trim();
                String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                DatabaseReference allDocumentReference = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("Documents").child(groupPushId).child(uniPushClassId).child(subjectUniPushId).child("All_Document");

                String fileUriPath = fileUri.toString();

                String onlyPath = fileUriPath.substring(0, fileUriPath.indexOf("/"));

                Log.d("ONLYPATH", "onDataChange: " + onlyPath);


                String pushValue[] = allDocumentReference.push().toString().split("/");

                String push = pushValue[9];

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Document Files");
                String userMsgKeyRef = allDocumentReference.child(onlyPath).push().getKey();
                StorageReference filePath = storageReference.child(userMsgKeyRef + "." + "pdf");

                filePath.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

//                        messageAdapter.setProgVal(1);

                        if (task.isComplete()) {

                            storageReference.child(userMsgKeyRef + "." + "pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "onClick: " + uri);
                                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, uniPushClassId, subjectUniPushId, uri.toString(), fileName);
                                    allDocumentReference.child(push).setValue(userAddGroupClass);
                                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId, uniPushClassId, uri.toString(), "pdf", subjectUniPushId, push, fileName, "");
                                    reference.push().setValue(userAddGroupClass);

                                    Toast.makeText(Server_Activity.this, "Document sending successful", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Server_Activity.this, "Document sending failed", Toast.LENGTH_SHORT).show();
//                        messageAdapter.setProgVal(2);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

//                        messageAdapter.setProgVal(3);

                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                        Toast.makeText(Server_Activity.this, "Document sending: " + progress, Toast.LENGTH_SHORT).show();


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*
    Chat box part..........................
     */
    private void submitLoginData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String updateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Server_Activity.this, "Token Failed" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                        refUserRegister.child("Name").setValue(userName);
                        refUserRegister.child("Email").setValue(userEmailID);
                        refUserRegister.child("UserId").setValue(userID);
                        refUserRegister.child("DateTime").setValue(updateTimeCC);
                        refUserRegister.child("Category").setValue("Student");
                        refUserRegister.child("userStatus").setValue("Online");
                        refUserRegister.child("token").setValue(token);
                        Toast.makeText(Server_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    /*
    Bottom navigation method  when it's pressed
     */
    private void bottomMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        startActivity(new Intent(Server_Activity.this, Server_Activity.class));
                        Server_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_discover:

                        startActivity(new Intent(Server_Activity.this, Discover_Activity.class));
                        Server_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_notification:

                        startActivity(new Intent(Server_Activity.this, Notification_Activity.class));
                        Server_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_profile:

                        startActivity(new Intent(Server_Activity.this, Profile_Activity.class));
                        Server_Activity.this.overridePendingTransition(0, 0);

                        break;

                }


                return true;
            }
        });
    }

    /*
    On back press
     */
    @Override
    public void onBackPressed() {

        if (flag == true) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            manager.getBackStackEntryCount();
            transaction.remove(doubtFragment);
            transaction.commit();
            flag = false;
        } else if (pdf_flag == true) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            manager.getBackStackEntryCount();
            transaction.remove(webView_fragment);
            transaction.commit();
            pdf_flag = false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Server_Activity.this.finishAffinity();
            } else {
                Server_Activity.this.finish();
            }
        }
    }

}