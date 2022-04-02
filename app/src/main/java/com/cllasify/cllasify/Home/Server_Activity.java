package com.cllasify.cllasify.Home;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adapter_All_Friends;
import com.cllasify.cllasify.Adaptor.Adapter_ClassGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_Friends;
import com.cllasify.cllasify.Adaptor.Adaptor_FriendsList;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Class.Class_Group;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Server_Activity extends AppCompatActivity {


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
    RelativeLayout rl_ChatView, rl_FriendChatLayout, ll_bottom_send;

    //Linear Layouts
    LinearLayout onlyAdminLayout, groupSection, ll_AddJoinGrp, ll_ChatDoubtDashboard,
            endPanelLinearLayout, friendSection,rightPanelMems;


    //Buttons
    Button btn_cAddGroup, btn_cJoinGroup, btn_lTeachResult, btn_lTeachExam;

    //Image Buttons
    ImageButton ib_cattach, ib_csubmit, ib_doubtSubmit, ImageViewRecentChat, ib_FrndP_csubmit,
            ib_servSettings, btn_lteachattend, btn_joinNotification;


    TabLayout tabLayout, tabL_ChatView;
    BottomNavigationView bottomNavigationView;
    TextView tv_GroupMember, adminListText, FriendListText;
    FirebaseDatabase refFriendList;







    FloatingActionButton fab_addDoubtQ;
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
    TextView tv_UserPublicTitle, tv_UserPrivateTitle, tv_OtherTitle,
            tv_cpaneltitle, tv_cpanelbody,
            textViewGroupName, classNameTextView,
            tv_FrndP_Title, textViewSubjectName;





    SearchView Rv_DoubtChat;

    FragmentManager fragmentManager;
    DoubtFragment doubtFragment;
    Friend_Chat_Activity friendChatFragment;



    //Right Panel Class and Sub-class
    Adapter_ClassGroup adapter_classGroup;




    //Chat_Activity
    private static final String TAG = "ChatActivity";
    ValueEventListener seenListener, readLiveMessageListener, readLiveDoubtListener;
    private MessageAdapter messageAdapter;
    private boolean onScreen;
    boolean flag = false;
    boolean flagFriend = false;

    void init() {

        rightPanelMems = findViewById(R.id.rightPanelMembs);

        onlyAdminLayout = findViewById(R.id.onlyAdminLayout);
        btn_joinNotification = findViewById(R.id.btn_joinNotification);

        bottomNavigationView = this.findViewById(R.id.bottom_nav);
        rv_UserPublicGroupTitle = findViewById(R.id.rv_UserPublicGroupTitle);
        rv_OtherPublicGroupTitle = findViewById(R.id.rv_OtherPublicGroupTitle);
        fragmentManager = getSupportFragmentManager();
        Rv_DoubtChat = findViewById(R.id.Rv_DoubtChat);
        doubtFragment = new DoubtFragment();

        tv_GroupMember = findViewById(R.id.tv_GroupMember);
        adminListText = findViewById(R.id.adminListText);
        FriendListText = findViewById(R.id.FriendListText);

        endPanelAllFriendsRecyclerView = findViewById(R.id.endPanelAllFriendsRecyclerView);

        rl_ChatView = findViewById(R.id.rl_ChatView);
        rl_FriendChatLayout = findViewById(R.id.rl_FrndChatLayout);

        ll_AddJoinGrp = findViewById(R.id.ll_AddJoinGrp);


        rv_ChatDashboard = findViewById(R.id.rv_ChatDashboard);
        ll_ChatDoubtDashboard = findViewById(R.id.ll_ChatDoubtDashboard);

        tv_FrndP_Title = findViewById(R.id.tv_FrndP_Title);
        tv_UserPublicTitle = findViewById(R.id.tv_UserPublicTitle);
        tv_UserPrivateTitle = findViewById(R.id.tv_UserPrivateTitle);
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
        classNameTextView = findViewById(R.id.classNameTextView);

//        ll_TabChatDoubt = findViewById(R.id.ll_TabChatDoubt);

        overlappingPanels = findViewById(R.id.overlapping_panels);
        tv_cpaneltitle = findViewById(R.id.tv_cpaneltitle);
        tv_cpanelbody = findViewById(R.id.tv_cpanelbody);


        ImageViewRecentChat = findViewById(R.id.ib_FrndsList);

        //start panel initialisation
        imageViewAddPanelAddGroup = findViewById(R.id.addNewTopicButton);
//        srl_ChatDashbaord = findViewById(R.id.srl_ChatDashbaord);
        fab_addDoubtQ = findViewById(R.id.fab_addDoubtQ);

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
        showGroupAdaptor = new Adaptor_QueryGroup(this, list_GroupTitle);
        showUserPrivateGroupAdaptor = new Adaptor_QueryGroup(this, list_UserPrivateGroupTitle);
        showUserPublicGroupAdaptor = new Adaptor_QueryGroup(this, list_UserPublicGroupTitle);
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
        rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupAdaptor);
        rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);


        ib_csubmit = findViewById(R.id.ib_csubmit);
        ib_doubtSubmit = findViewById(R.id.ib_doubtSubmit);


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
                                classPosition[0] = snapshot.child("classPosition").getValue().toString().trim();
                                className[0] = snapshot.child("clickedClassName").getValue().toString().trim();
                                subjectName[0] = snapshot.child("clickedSubjectName").getValue().toString().trim();
                                groupPushId[0] = snapshot.child("clickedGroupPushId").getValue().toString().trim();
                                uniPushClassId[0] = snapshot.child("uniPushClassId").getValue().toString().trim();
                                clickedGroupName[0] = snapshot.child("clickedGroupName").getValue().toString().trim();


                                //Agar middle panel stuck ho kar aaye then yaha se lekar

                                btn_joinNotification.setEnabled(true);
                                btn_lteachattend.setEnabled(true);
                                ib_servSettings.setEnabled(true);

                                btn_lteachattend.setOnClickListener(view -> {

                                    Intent intent = new Intent(Server_Activity.this, Attendance_Activity.class);
                                    intent.putExtra("groupPushId", groupPushId[0]);
                                    intent.putExtra("subGroupPushId", uniPushClassId[0]);
                                    intent.putExtra("classPushId", subjectUniPushId[0]);
                                    startActivity(intent);
                                });

                                btn_joinNotification.setOnClickListener(v -> {
                                    Intent intent = new Intent(Server_Activity.this, GRPJoinReqs.class);
                                    intent.putExtra("groupPushId", groupPushId[0]);
                                    intent.putExtra("subGroupPushId", uniPushClassId[0]);
                                    intent.putExtra("classPushId", subjectUniPushId[0]);
                                    startActivity(intent);
                                });

                                memVis(true);

                                //yaha tak comment kardena

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

                }else{
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


        messageAdapter.setOnDoubtClickListener((doubtQuestion, groupPush, groupClassPush, groupSubjectPush, doubtQuestionPush) -> {

            Toast.makeText(getApplicationContext(), "Test doubt", Toast.LENGTH_SHORT).show();

            DatabaseReference putTempDoubt = FirebaseDatabase.getInstance().getReference().child("Groups");
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupPushId").setValue(groupPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassPushId").setValue(groupClassPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassSubjectPushId").setValue(groupSubjectPush);
            putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("doubtQuestionPushId").setValue(doubtQuestionPush);

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
                Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
            } else {
                et_ctext.getText().clear();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                        String push = "mszno_" + noofGroupinCategory + "_" + snapshot.getChildrenCount();

                        Calendar calenderCC = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                        String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                        userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId[0], classPosition[0], subGroupMsg, "chat", subjectUniPushId[0], push, 0);
                        reference.push().setValue(userAddGroupClass);
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
        } else {
            rightPanelMems.setVisibility(View.GONE);
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

        databaseReferenceTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentItemArrayListClassName.clear();
                String groupPushId = String.valueOf(snapshot.getValue());

                DatabaseReference databaseReferenceAdminShow = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList").child(userID);
                databaseReferenceAdminShow.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("CHKADMINCLASS", "onDataChange: "+snapshot.child("admin").getValue());
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
                        }else{
                            Toast.makeText(Server_Activity.this, "You are an admin", Toast.LENGTH_SHORT).show();
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

                                }
                                else{
                                    Toast.makeText(Server_Activity.this, "You are not admin", Toast.LENGTH_SHORT).show();
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


        DatabaseReference refGetGRPName = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId);
        refGetGRPName.addValueEventListener(new ValueEventListener() {
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


        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child("Chat_Message").child(groupPushId).child(classUniPushId).child(subjectUniPushId);

        if (reference != null) {
            readLiveMessageListener = new ValueEventListener() {
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
            };

            reference.addValueEventListener(readLiveMessageListener);
        }

        allDoubtReference = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("Doubt").child(groupPushId).child(classUniPushId).child(subjectUniPushId).child("All_Doubt");

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
                        Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            allDoubtReference.addValueEventListener(readLiveDoubtListener);

        }

        refClassTeacherList = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList");
        listGrpTeacherList.clear();
        refClassTeacherList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Class_Student_Details class_teacher_details = snapshot.getValue(Class_Student_Details.class);
                listGrpTeacherList.add(class_teacher_details);


                rv_GrpTeacherList.setAdapter(showGrpTeacherList);
                showGrpTeacherList.notifyDataSetChanged();
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

        Log.d(TAG, "onSUBS: " + classUniPushId);

        DatabaseReference refGrpMembers = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId);
        refGrpMembers.addListenerForSingleValueEvent(new ValueEventListener() {
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


        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId).child("classStudentList");

        refGrpMemberList.addValueEventListener(new ValueEventListener() {
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
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "AddFriend");
            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "FollowFriend");
            }

            @Override
            public void MemberProfile(String memberUserId, String memberUserName) {
                showBtmDialogUserProfile(memberUserId, memberUserName);
            }

        });






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

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_server);

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
                    }else{
                        Log.d("CHKJOINEDORADDGRP", "onDataChange: Not available");
                        ll_AddJoinGrp.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            init();

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
            imageViewAddPanelAddGroup.setOnClickListener(view -> {
                Intent intent = new Intent(Server_Activity.this, Create_Server.class);
                startActivity(intent);
            });

            chatListDashboard();

            ImageViewRecentChat.setOnClickListener(view -> {

                tv_GroupMember.setVisibility(View.GONE);
                adminListText.setVisibility(View.GONE);
                FriendListText.setVisibility(View.VISIBLE);
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

                            friendChatFragment = new Friend_Chat_Activity();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.center_panel, friendChatFragment).addToBackStack(friendChatFragment.getClass().getSimpleName()).commit();

                            Bundle bundle = new Bundle();
                            bundle.putString("name", friendName);
                            bundle.putString("receiverUid", friendUserId);
                            friendChatFragment.setArguments(bundle);
                            flagFriend = true;
                        }
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
                                overlappingPanels.openStartPanel();
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
                        overlappingPanels.openStartPanel();

                    }
                    if (snapshot.getChildrenCount() < 0) {
                        Toast.makeText(Server_Activity.this, "Please create Group using left swipe", Toast.LENGTH_SHORT).show();
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
            ib_cattach = findViewById(R.id.ib_cattach);
            ib_csubmit = findViewById(R.id.ib_csubmit);
            et_ctext = findViewById(R.id.et_ctext);

            btn_cAddGroup = findViewById(R.id.btn_caddgroup);
            btn_cJoinGroup = findViewById(R.id.btn_cjoingroup);

            ll_bottom_send = findViewById(R.id.ll_bottom_send);


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


    //Doubt
    private void showAddDoubtBtmDialog(String groupPushId, String subGroupPushId, String groupClassSubjects) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(R.layout.btmdialog_adddoubt);

        Button btn_Cancel = bottomSheetDialog.findViewById(R.id.btn_Cancel);
        Button btn_Submit = bottomSheetDialog.findViewById(R.id.btn_Submit);
        EditText et_AddTopic = bottomSheetDialog.findViewById(R.id.et_AddTopic);
        EditText et_AddDoubt = bottomSheetDialog.findViewById(R.id.et_AddDoubt);

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
                if (addTopic.isEmpty() && addDoubt.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
                } else {
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

                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId, subGroupPushId, addDoubt, "doubt", groupClassSubjects, push, 0);
                            reference.push().setValue(userAddGroupClass);
                            Toast.makeText(Server_Activity.this, "Doubt Successfully Added", Toast.LENGTH_SHORT).show();
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

        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        bottomSheetDialog.show();

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
                    intent.putExtra("serverName", snapshot.child("serverName").getValue().toString());
                    startActivity(intent);
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_joinNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Server_Activity.this, "Please select a subject to continue,", Toast.LENGTH_SHORT).show();
                btn_joinNotification.setEnabled(false);
            }
        });

        btn_lteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Server_Activity.this, "Please select a subject to continue,", Toast.LENGTH_SHORT).show();
                btn_lteachattend.setEnabled(false);
            }
        });

        DatabaseReference userJoinedOrAddServer = FirebaseDatabase.getInstance().getReference().child("Groups").child("UserAddedOrJoinedGrp").child(userID);
        userJoinedOrAddServer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_OtherUserPublicGroupTitle.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("UserAddedOrJoinedGrpPUSHIDS", "" + dataSnapshot.getKey());

                    String UserAddedOrJoinedGrpPUSHIDS = dataSnapshot.getKey();
                    if (dataSnapshot.exists() && UserAddedOrJoinedGrpPUSHIDS != null) {

                        if (Objects.equals(dataSnapshot.child("addedOrJoined").getValue(), "Added")
                                || Objects.equals(dataSnapshot.child("addedOrJoined").getValue(), "TeacherJoin")) {

                            DatabaseReference databaseReferenceCHKADMIN = FirebaseDatabase.getInstance().getReference()
                                    .child("Groups").child("Check_Group_Admins").child(UserAddedOrJoinedGrpPUSHIDS).child("classAdminList");

                            databaseReferenceCHKADMIN.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String adminUserId = dataSnapshot1.child("userId").getValue().toString();
                                        refOtherUserPublicGroup.child(UserAddedOrJoinedGrpPUSHIDS).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot newSnap) {

                                                Class_Group userQuestionsAdded = newSnap.getValue(Class_Group.class);

                                                if (newSnap.exists() && adminUserId.equals(currentUser.getUid())) {
                                                    Log.i("SBO", dataSnapshot1.child("userId").getValue().toString());
                                                    list_OtherUserPublicGroupTitle.add(userQuestionsAdded);
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

                            databaseReferenceGetUserJoinedClass.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot01) {

                                    if (snapshot01.child(userID).child("classUniPushId").exists()) {

                                        refOtherUserPublicGroup.child(UserAddedOrJoinedGrpPUSHIDS).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot otherSnap) {

                                                if (otherSnap.child("User_Subscribed_Groups").child(userID).exists()) {
                                                    String check = otherSnap.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
                                                    if (check.equals("Class Member")) {
                                                        Class_Group userQuestions = otherSnap.getValue(Class_Group.class);
                                                        list_OtherUserPublicGroupTitle.add(userQuestions);
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
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {

            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID, String groupCategory) {

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


            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {

            }


        });

        friendSection.setVisibility(View.GONE);


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

        BottomSheetDialog btmSheetUserProfile = new BottomSheetDialog(this);
        btmSheetUserProfile.setCancelable(true);
        btmSheetUserProfile.setContentView(R.layout.btmdialog_profileothers);
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
                if (snapshot.child("profilePic").exists()){
                    String profilePic=snapshot.child("profilePic").getValue().toString();
                    if (!(Server_Activity.this).isFinishing()) {
                        Glide.with(getApplicationContext()).load(profilePic).into(prof_pic_BtmSheet);
                    }
                }else{
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

        tv_Name.setText(memberUserName);

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

                    friendChatFragment = new Friend_Chat_Activity();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.center_panel, friendChatFragment).addToBackStack(friendChatFragment.getClass().getSimpleName()).commit();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", memberUserName);
                    bundle.putString("receiverUid", memberUserId);
                    friendChatFragment.setArguments(bundle);
                    flagFriend = true;
                }

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
                    if(count < 2) {
                        tv_CountFollowing.setText((int) count + " Following");
                    }
                    else{
                        tv_CountFollowing.setText((int) count + " Followings");
                    }
                } else {
                    tv_CountFollowing.setText("0 Followings");

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


        btmSheetUserProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btmSheetUserProfile.show();
    }


    /*
        Google SignIn Result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "on activity result called", Toast.LENGTH_SHORT).show();

        if (requestCode == 100) {
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
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Server_Activity.this.finishAffinity();
            } else {
                Server_Activity.this.finish();
            }
        }
    }

}