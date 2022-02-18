package com.cllasify.cllasify.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.cllasify.cllasify.Adaptor.Adapter_All_Friends;
import com.cllasify.cllasify.Adaptor.Adapter_ClassGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_Friends;
import com.cllasify.cllasify.Adaptor.Adaptor_FriendsList;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowDoubt;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Adaptor_Tab_ChatDiscussion;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Friend_Chat_Activity;
import com.cllasify.cllasify.FriendsListClass;
import com.cllasify.cllasify.Group_Students;
import com.cllasify.cllasify.MessageAdapter;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
import com.cllasify.cllasify.Server.Attendance_Activity;
import com.cllasify.cllasify.Server.Chat_List_Fragment;
import com.cllasify.cllasify.Server.Chat_New_Fragment;
import com.cllasify.cllasify.Server.DoubtFragment;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.discord.panels.OverlappingPanelsLayout;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Server_Activity extends AppCompatActivity implements Adapter_ClassGroup.onAddSubjectClickListener {


    TabLayout tabLayout, tabl_ChatView;
    ViewPager viewPager, view_Pager_ChatView;
    BottomNavigationView bottomNavigationView;
    TextView tv_GroupMember;
    RecyclerView rv_UserPublicGroupTitle, rv_OtherPublicGroupTitle, rv_GrpMemberList, recyclerViewClassList, topicNamesRecyclerView, endPanelAllFriendsRecyclerView;
    RelativeLayout rl_ChatView;
    FirebaseDatabase refFriendList;
    DatabaseReference refChatDashboard, refDoubtDashboard;
    DatabaseReference refuserPersonalGroup, refuserAllGroup, refGroupTopic,
            refuserPublicGroup, refAllGroup,
            refotheruserPublicGroup, refUserStatus,
            refSearchShowGroup, refShowUserPublicGroup, refShowUserPrivateGroup, refShowUserAllGroup,
            refteachStud,
            refChildGroup, refChildGroupSubsList,
            refGroupSubsList, refGrpMemberList, refClassStudentList;

    DatabaseReference checkFriends;

    List<Group_Students> group_studentsListDetails;
    List<Class_Group> list_GroupTitle, list_UserPrivateGroupTitle, list_UserPublicGroupTitle, list_OtherUserPublicGroupTitle,
            list_SubChild,
            list_ChatDashboard, list_DoubtDashboard, list_Friend, list_ChatListDashboard, list_NewChatDashboard;

    List<Class_Student_Details> listGrpMemberList;
    List<String> allFriendsList;

    Adaptor_QueryGroup showGroupadaptor, showUserPrivateGroupadaptor, showUserPublicGroupadaptor;
    Adaptor_ShowGrpMember showGrpMemberList;
    Adaptor_QueryGroup showOtherUserPublicGroupAdaptor;
    Adaptor_Friends show_FriendAdaptor, showChatListDashadaptor;
    Adapter_All_Friends adapter_all_friends;
    Adaptor_ShowGroup showChatDashadaptor;
    Adaptor_ShowDoubt showDoubtDashAdaptor;
    FloatingActionButton fab_addDoubtQ;
    String GroupCategory;
    GoogleSignInClient googleSignInClient;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    ProgressDialog notifyPB;
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
            textViewGroupName,
            left_pannel_upperTextView, classNameTextView,
            tv_FrndP_Title;

    ImageButton ib_cattach, ib_csubmit;
    ImageButton ImageViewRecentChat, ib_FrndP_csubmit, ib_servSettings;
    Button btn_caddgroup, btn_cjoingroup, addNewClassButton;
    Button btn_lteachresult, btn_lteachattend, btn_lteachexam;
    Button endPanelAllFriendsButton;
    LinearLayout groupSection, ll_AddJoinGrp;

    RecyclerView rvFriendsList;
    Adaptor_FriendsList adaptor_friendsList;
    List<FriendsListClass> friendsListClasses;
    TextView recChats;


    LinearLayout ll_bottom_send, ll_ChatDoubtDashboard,
            endPanelLinearLayout, ll_TabChatDoubt;
    TextView tv_ChatDashboard, tv_DoubtDashboard;
    RelativeLayout rl_ChatDashboard, rl_DoubtDashboard;
    RelativeLayout rl_FrndChatLayout;
    SearchView Rv_DoubtChat;
    RelativeLayout parentDoubtDashboard;

    public static RecyclerView rv_ChatDashboard, rv_DoubtDashboard;

    SwipeRefreshLayout swipeUpRefreshLayoutInClass, srl_ChatDashbaord;
    FragmentManager fragmentManager;
    DoubtFragment doubtFragment;

    LinearLayout friendSection;


    //Right Panel Class and Sub-class
    Adapter_ClassGroup adapter_classGroup;

    List<Class_Group_Names> parentItemArrayListClassName;
    List<Subject_Details_Model> childItemArrayListClassName;


    //Chat_Activity
    private static final String TAG = "ChatActivity";
    ValueEventListener seenListener, readLiveMessageListener;
    private DatabaseReference reference;
    private ArrayList<Class_Group> chats;
    private MessageAdapter messageAdapter;
    private boolean onScreen;

    /*
        Initialisalisation
         */
    void init() {


        bottomNavigationView = this.findViewById(R.id.bottom_nav);
        rv_UserPublicGroupTitle = findViewById(R.id.rv_UserPublicGroupTitle);
        rv_OtherPublicGroupTitle = findViewById(R.id.rv_OtherPublicGroupTitle);
        fragmentManager = getSupportFragmentManager();
        Rv_DoubtChat = findViewById(R.id.Rv_DoubtChat);
        doubtFragment = new DoubtFragment();

        tv_GroupMember = findViewById(R.id.tv_GroupMember);

        endPanelAllFriendsRecyclerView = findViewById(R.id.endPanelAllFriendsRecyclerView);

        rl_ChatView = findViewById(R.id.rl_ChatView);
        rl_FrndChatLayout = findViewById(R.id.rl_FrndChatLayout);

        ll_AddJoinGrp = findViewById(R.id.ll_AddJoinGrp);


        rv_ChatDashboard = findViewById(R.id.rv_ChatDashboard);
        rv_DoubtDashboard = findViewById(R.id.rv_DoubtDashboard);
        parentDoubtDashboard = findViewById(R.id.parentDoubtDashboard);

        rl_ChatDashboard = findViewById(R.id.rl_ChatDashboard);
        rl_DoubtDashboard = findViewById(R.id.rl_DoubtDashboard);


        tv_ChatDashboard = findViewById(R.id.tv_ChatDashboard);
        tv_DoubtDashboard = findViewById(R.id.tv_DoubtDashboard);
        ll_ChatDoubtDashboard = findViewById(R.id.ll_ChatDoubtDashboard);

        tv_FrndP_Title = findViewById(R.id.tv_FrndP_Title);
        tv_UserPublicTitle = findViewById(R.id.tv_UserPublicTitle);
        tv_UserPrivateTitle = findViewById(R.id.tv_UserPrivateTitle);
        tv_OtherTitle = findViewById(R.id.tv_OtherTitle);

        ib_FrndP_csubmit = findViewById(R.id.ib_FrndP_csubmit);

        btn_lteachattend = findViewById(R.id.btn_lteachattend);
        btn_lteachexam = findViewById(R.id.btn_lteachexam);
        btn_lteachresult = findViewById(R.id.btn_lteachresult);


        recChats = findViewById(R.id.FriendName);
        rvFriendsList = findViewById(R.id.recyclerViewFriendsNameList);
        refFriendList = FirebaseDatabase.getInstance();
        friendsListClasses = new ArrayList<>();

        friendSection = findViewById(R.id.friendsSection);
        ib_servSettings = findViewById(R.id.ib_servSettings);


        /*
        End Panel Initialisation
         */
        endPanelAllFriendsButton = findViewById(R.id.endPanelAllFriendsButton);
        endPanelLinearLayout = findViewById(R.id.endPanelLinearLayout);


        /*
            group section Initialisation
         */
        groupSection = findViewById(R.id.groupSection);
        textViewGroupName = findViewById(R.id.groupName);
        addNewClassButton = findViewById(R.id.addNewClassButton);
        recyclerViewClassList = findViewById(R.id.recyclerViewClassList);
        swipeUpRefreshLayoutInClass = findViewById(R.id.swipeUpRefreshLayoutInClass);
        classNameTextView = findViewById(R.id.classNameTextView);
        topicNamesRecyclerView = findViewById(R.id.topicNamesRecyclerView);

        ll_TabChatDoubt = findViewById(R.id.ll_TabChatDoubt);

        overlappingPanels = findViewById(R.id.overlapping_panels);
        tv_cpaneltitle = findViewById(R.id.tv_cpaneltitle);
        tv_cpanelbody = findViewById(R.id.tv_cpanelbody);
        left_pannel_upperTextView = findViewById(R.id.headerNameWelcome);


        ImageViewRecentChat = findViewById(R.id.ib_FrndsList);

        //start panel initialisation
        imageViewAddPanelAddGroup = findViewById(R.id.addNewTopicButton);
        srl_ChatDashbaord = findViewById(R.id.srl_ChatDashbaord);
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
        refteachStud = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
        refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");

        refShowUserPrivateGroup.keepSynced(true);
        refShowUserPublicGroup.keepSynced(true);

        rv_GrpMemberList = findViewById(R.id.rv_GrpMemberList);
        listGrpMemberList = new ArrayList<>();
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        allFriendsList = new ArrayList<>();


        endPanelAllFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Setting up layout manager
        rv_UserPublicGroupTitle.setLayoutManager(new LinearLayoutManager(this));
        rv_OtherPublicGroupTitle.setLayoutManager(new LinearLayoutManager(this));

        topicNamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //setting up Adapter
        showGroupadaptor = new Adaptor_QueryGroup(this, list_GroupTitle);
        showUserPrivateGroupadaptor = new Adaptor_QueryGroup(this, list_UserPrivateGroupTitle);
        showUserPublicGroupadaptor = new Adaptor_QueryGroup(this, list_UserPublicGroupTitle);
        showChatDashadaptor = new Adaptor_ShowGroup(this, list_ChatDashboard);
        showOtherUserPublicGroupAdaptor = new Adaptor_QueryGroup(this, list_OtherUserPublicGroupTitle);

        showGrpMemberList = new Adaptor_ShowGrpMember(Server_Activity.this, listGrpMemberList);

        adapter_all_friends = new Adapter_All_Friends(this, allFriendsList);
        endPanelAllFriendsRecyclerView.setAdapter(adapter_all_friends);

        show_FriendAdaptor = new Adaptor_Friends(this, list_Friend);
        showChatListDashadaptor = new Adaptor_Friends(this, list_ChatListDashboard);
        rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupadaptor);
        rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);

        topicNamesRecyclerView.setAdapter(showChatListDashadaptor);


        ib_csubmit = findViewById(R.id.ib_csubmit);


        adaptor_friendsList = new Adaptor_FriendsList(getApplicationContext(), friendsListClasses);


        //Class-Group
        adapter_classGroup = new Adapter_ClassGroup(getApplicationContext(), this);
        parentItemArrayListClassName = new ArrayList<>();
        childItemArrayListClassName = new ArrayList<>();
        adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
        recyclerViewClassList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewClassList.setAdapter(adapter_classGroup);

/*
        //Topic Adapter
        adapter_topicList = new Adapter_TopicList(getApplicationContext());
        DatabaseReference refSaveCurrDataForSubj = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

        adapter_topicList.setOnItemClickListener(new Adapter_TopicList.OnItemClickListener() {

            @Override
            public void subjectChildClick(String groupName, String groupPushId, String groupSubjectPushId, String groupClassSubjects) {
                Toast.makeText(getApplicationContext(), "Testing the subject Server_Activity!!", Toast.LENGTH_SHORT).show();

                setReference(groupPushId,groupSubjectPushId,groupClassSubjects);
                refSaveCurrDataForSubj.child("groupName").setValue(groupName);
                refSaveCurrDataForSubj.child("SubGroupPushId").setValue(groupSubjectPushId);
                refSaveCurrDataForSubj.child("GroupPushId").setValue(groupPushId);
                refSaveCurrDataForSubj.child("groupClassSubjects").setValue(groupClassSubjects);
                overlappingPanels.closePanels();
            }
        });
*/

        final String[] serverName = new String[1];
        final String[] className = new String[1];
        final String[] subjectName = new String[1];
        final String[] groupPushId = new String[1];
        final String[] classPosition = new String[1];
        final String[] uniPushClassId = new String[1];


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
                        classPosition[0] = snapshot.child("classPosition").getValue().toString().trim();
                        className[0] = snapshot.child("clickedClassName").getValue().toString().trim();
                        subjectName[0] = snapshot.child("clickedSubjectName").getValue().toString().trim();
                        groupPushId[0] = snapshot.child("clickedGroupPushId").getValue().toString().trim();
                        uniPushClassId[0] = snapshot.child("uniPushClassId").getValue().toString().trim();

                        setReference(groupPushId[0], className[0], subjectName[0], classPosition[0], uniPushClassId[0]);


                        Log.d("SCSG", "init: serverName: " + serverName[0] + "\nclassName: " + className[0] + "\nsubjectName: " + subjectName[0] + "\ngroupPushId: " + groupPushId[0]);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ib_servSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_Activity.this, Server_Settings.class);
                intent.putExtra("currUserId", userID);
                startActivity(intent);
            }
        });


//        setReference("Uni_Group_No_0_Exp Group","Class 1","English","0");


        //Chat_Activity
        onScreen = false;
        chats = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext());
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setStackFromEnd(true);
        rv_ChatDashboard.setLayoutManager(linearLayout);
        rv_ChatDashboard.setAdapter(messageAdapter);

//        setReference("Uni_Group_No_1_T group", "T class", "T topic");


        ib_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subGroupMsg = et_ctext.getText().toString().trim();

                if (subGroupMsg.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
//                    et_ctext.setError("Enter text");
                } else {
                    et_ctext.getText().clear();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                            String push = "mszno_" + noofGroupinCategory + "_" + subGroupName[0];

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, groupPushId[0], classPosition[0], subGroupMsg);
                            reference.push().setValue(userAddGroupClass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
/*
                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Show_Group").child("User_Show_Group").child(userID).child(subGroupPushId[0]);
                    referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "mszno_" + noofGroupinCategory + "_" + subGroupPushId[0];

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, subGroupPushId[0], groupPushId[0], noofGroupinCategory);
                            referenceUser.child(push).setValue(userAddGroupClass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
*/

                }
            }
        });


        //Doubt Section
        showDoubtDashAdaptor = new Adaptor_ShowDoubt(this, list_DoubtDashboard);
        rv_DoubtDashboard.setLayoutManager(new LinearLayoutManager(this));
        showChatDashadaptor.scrollDownl();
        rv_DoubtDashboard.setAdapter(showDoubtDashAdaptor);

        showDoubtDashAdaptor.setOnItemClickListener(new Adaptor_ShowDoubt.OnItemClickListener() {
            @Override
            public void showDoubtChat(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush) {

                Toast.makeText(getApplicationContext(), "Test doubt", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("groupPushId", groupPush);
                bundle.putString("groupClassPushId", groupClassPush);
                bundle.putString("groupClassSubjectPushId", groupSubjectPush);
                bundle.putString("doubtQuestionPushId", doubtQuestionPush);

                doubtFragment.setArguments(bundle);
                Rv_DoubtChat.setVisibility(View.GONE);
                rv_DoubtDashboard.setVisibility(View.GONE);
                parentDoubtDashboard.setVisibility(View.GONE);
                if (fragmentManager.findFragmentByTag("zero") != null) {
                    getSupportFragmentManager().beginTransaction().show(doubtFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.rl_DoubtDashboard, doubtFragment, "zero").commit();
                }
            }
        });


/*
//Uni_Group_No_1_T group -> T class -> B topic ->
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final String[] groupPushId = new String[1];
//                groupPushId[0] = snapshot.child("groupClassSubjects").getValue().toString().trim();
                DatabaseReference databaseReference01 = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child("Uni_Group_No_1_T group").child("T class").child("B topic").child("groupStudentList");

                databaseReference01.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Group_Students> group_studentsList = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        group_studentsList.add(snapshot.child("0").getValue(Group_Students.class));
                            Log.d(TAG, "Snap" + snapshot);
//                        group_studentsListDetails.add((Group_Students) group_studentsList);
                        Log.d(TAG, "init: " + group_studentsList.get(0).isAdmin());
                        showGrpMemberList = new Adaptor_ShowGrpMember(Server_Activity.this, group_studentsList);
                        rv_GrpMemberList.setAdapter(showGrpMemberList);
                        }
//                        String subCrDate = snapshot.child("0").child("userName").getValue().toString().trim();
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
*/

    }


    private void saveClassGroup(String groupPushId, String sbChildGroupName) {
//        Class_Group_Names subGroup_Class = new Class_Group_Names(dateTimeCC, userName, SharePref.getDataFromPref(Constant.USER_ID), sbChildGroupName);
//        subGroup_Class.setAdmin(true);


        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

        testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference getServerTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                getServerTemp.child("className").setValue(sbChildGroupName);

                getServerTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("SerT", "Temp Server name: " + (snapshot.child("serverName").getValue()));
                        Log.d("SerT", "Temp Group Push Id: " + (snapshot.child("tpGroupPushId").getValue()));
                        Log.d("SerT", "Group Push Id: " + groupPushId);
                        Log.d("SerT", "Group name: " + sbChildGroupName);


                        testDatabaseReference.child(groupPushId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String[] push01 = String.valueOf(testDatabaseReference.child(groupPushId).push()).split("/");

                                testDatabaseReference.child(groupPushId).child(push01[6]).child("className").setValue(sbChildGroupName);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classUniPushId").setValue(push01[6]);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("groupPushId").setValue(groupPushId);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot01) {
                                        Class_Student_Details class_student_details = new Class_Student_Details(true, userID, userName);
                                        testDatabaseReference.child(groupPushId).child(push01[6]).child("classStudentList")
                                                .child(String.valueOf(snapshot01.getChildrenCount())).setValue(class_student_details);
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
                /*

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



/*
        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

        testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long noofGroupinCategory = snapshot.getChildrenCount();
                String push = "Uni_Group_No_" + noofGroupinCategory + "_" + sbChildGroupName;
                testDatabaseReference.child(push).child(String.valueOf(snapshot.getChildrenCount())).child("className").setValue(sbChildGroupName + " Testttt");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }

    private void setReference(String groupPushId, String subGroupPushId, String groupClassSubject, String classPosition, String classUniPushId) {

        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child("Chat_Message").child(groupPushId).child(subGroupPushId).child(groupClassSubject);

        if (reference != null) {
            readLiveMessageListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chats.clear();
                    Log.d(TAG, "onDataChange: " + snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Class_Group class_userDashBoard = postSnapshot.getValue(Class_Group.class);
                        chats.add(class_userDashBoard);
                    }
                    messageAdapter.setList(chats);
                    messageAdapter.notifyDataSetChanged();
                    rv_ChatDashboard.smoothScrollToPosition(rv_ChatDashboard.getAdapter().getItemCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            reference.addValueEventListener(readLiveMessageListener);
        }


        showDoubt(groupPushId, subGroupPushId, groupClassSubject);


        DatabaseReference allDoubtReference = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubject).child("All_Doubt");

        list_DoubtDashboard.clear();

        ChildEventListener doubtchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_DoubtDashboard.add(class_userDashBoard);
                    showDoubtDashAdaptor.notifyDataSetChanged();
                } else {
                    Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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

        allDoubtReference.addChildEventListener(doubtchildEventListener);


        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId).child("classStudentList");

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
        listGrpMemberList.clear();


        //Endpanel
        showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember.OnItemClickListener() {
            @Override
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "AddFrnd");
            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID, adminUserName, "FollowFrnd");
            }

            @Override
            public void MemberProfile(String memberUserId, String memberUserName) {
                showBtmDialogUserProfile(memberUserId, memberUserName);
            }

        });

        btn_lteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Server_Activity.this, Attendance_Activity.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", subGroupPushId);
                intent.putExtra("classPushId", groupClassSubject);
                startActivity(intent);
            }
        });






//        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("User_Subscribed_Groups");


/*
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Toast.makeText(Server_Activity.this, "From" + fromPosition + "gggto" + toPosition, Toast.LENGTH_SHORT).show();
                Collections.swap(listGrpMemberList, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);
*/

    }

    /*
        private void readMessage(String OrderId) {
            readLiveMessageListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chats.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals(OrderId)) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Chat chat = child.getValue(Chat.class);
                                chats.add(chat);
                            }
                        }
                    }
                    messageAdapter.setList(chats);
                    messageAdapter.notifyDataSetChanged();
                    chatBinding.shimmerFrame.stopShimmer();
                    chatBinding.shimmerFrame.setVisibility(View.GONE);
                    chatBinding.conversationList.smoothScrollToPosition(chatBinding.conversationList.getAdapter().getItemCount());
                    chatBinding.conversationList.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }
    */


    @Override
    protected void onResume() {
        super.onResume();
        if (onScreen) {
            reference.addValueEventListener(readLiveMessageListener);
            onScreen = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        SharePref sharePref = new SharePref(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();

            init();

            if (getIntent().hasExtra("closePanels")) {
                Log.d("CP", "onCreate: " + getIntent().getStringExtra("closePanels"));
//                overlappingPanels.closePanels();
            }


            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
            SharePref.setDataPref(Constant.USER_ID, userID);
            userEmailID = currentUser.getEmail();
            userPhoto = currentUser.getPhotoUrl();
            userName = currentUser.getDisplayName();
            notifyPB = new ProgressDialog(this);
            notifyPB.setTitle("Govt Jobs");
            notifyPB.setMessage("Loading All Jobs");
            notifyPB.setCanceledOnTouchOutside(true);


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


            refteachStud = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups");
            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
            refUserStatus = FirebaseDatabase.getInstance().getReference().child("Registration");


            checkFriends = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends");

            Log.d("TAG", "onCreate: " + checkFriends);


//            rv_ChatDashboard.setLayoutManager(new LinearLayoutManager(this));

//---------------------------------------------------------------------------------------------------------------------------------------------------
            /*

            Left Panel

             */
            left_pannel_upperTextView.setText("Welcome," + userName);

            left_pannel_upperTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupSection.setVisibility(View.GONE);
                    recyclerViewClassList.setVisibility(View.GONE);
                    textViewGroupName.setVisibility(View.GONE);
                    endPanelLinearLayout.setVisibility(View.GONE);
                    listGrpMemberList.clear();


                }
            });

            //add server option clicked(+)
            imageViewAddPanelAddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createGroupDialog();
                }
            });

            chatListDashboard();

            ImageViewRecentChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    groupSection.setVisibility(View.VISIBLE);
//                    textViewGroupName.setVisibility(View.VISIBLE);
//                    topicNamesRecyclerView.setVisibility(View.GONE);
//                    recyclerViewGroupNameList.setVisibility(View.VISIBLE);
//                    textViewGroupName.setText("Recent Chats");
//                    addNewClassButton.setVisibility(View.GONE);
                    ll_AddJoinGrp.setVisibility(View.GONE);
                    tv_GroupMember.setVisibility(View.GONE);
                    rv_GrpMemberList.setVisibility(View.GONE);
                    imageViewAddPanelAddGroup.setVisibility(View.GONE);
                    endPanelAllFriendsRecyclerView.setVisibility(View.VISIBLE);
                    recChats.setText("Recent Chats");
                    groupSection.setVisibility(View.GONE);
                    friendSection.setVisibility(View.VISIBLE);

//                    rvFriendsList.setVisibility(View.VISIBLE);

                    rvFriendsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvFriendsList.setAdapter(adaptor_friendsList);

                    adaptor_friendsList.setFriendListClick(new Adaptor_FriendsList.OnItemClickListener() {
                        @Override
                        public void onFriendClick(String friendName, String friendUserId) {

                            overlappingPanels.closePanels();

                            ll_AddJoinGrp.setVisibility(View.GONE);

                            Fragment fragment = new Friend_Chat_Activity();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.center_panel, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();

                            Bundle bundle = new Bundle();
                            bundle.putString("name", friendName);
                            bundle.putString("receiverUid", friendUserId);
                            fragment.setArguments(bundle);
                        }
                    });

                }
            });


            refShowUserAllGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {
                        notifyPB.dismiss();
                        ll_AddJoinGrp.setVisibility(View.GONE);
                        overlappingPanels.openStartPanel();

                    } else {
                        Toast.makeText(Server_Activity.this, "Please create Group using left swipe", Toast.LENGTH_SHORT).show();
                        ll_AddJoinGrp.setVisibility(View.VISIBLE);
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

            btn_caddgroup = findViewById(R.id.btn_caddgroup);
            btn_cjoingroup = findViewById(R.id.btn_cjoingroup);

            ll_bottom_send = findViewById(R.id.ll_bottom_send);


            tabLayout = findViewById(R.id.tabl_chatdis);
            viewPager = findViewById(R.id.view_Pager);

            tabl_ChatView = findViewById(R.id.tabl_ChatView);
            view_Pager_ChatView = findViewById(R.id.view_Pager_ChatView);

            btn_caddgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createGroupDialog();
                }
            });

            //fragment for doubt chat

            tv_ChatDashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rl_ChatDashboard.setVisibility(View.VISIBLE);
                    rl_DoubtDashboard.setVisibility(View.GONE);
                }
            });
            tv_DoubtDashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rl_DoubtDashboard.setVisibility(View.VISIBLE);
                    rl_ChatDashboard.setVisibility(View.GONE);

                    Rv_DoubtChat.setVisibility(View.VISIBLE);
                    rv_DoubtDashboard.setVisibility(View.VISIBLE);
                    parentDoubtDashboard.setVisibility(View.VISIBLE);
                    if (fragmentManager.findFragmentByTag("zero") != null) {
                        getSupportFragmentManager().beginTransaction().hide(doubtFragment).commit();
                    }


                }
            });


            srl_ChatDashbaord.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // cancel the Visual indication of a refresh
                    srl_ChatDashbaord.setRefreshing(false);
//                    list_ChatDashboard.clear();
//                    rv_ChatDashboard.removeAllViews();
                    showChatDashadaptor.notifyDataSetChanged();
                    showChatDashadaptor.scrollDownl();


                }
            });

            //??
            tabl_ChatView.setupWithViewPager(view_Pager_ChatView);
            tabl_ChatView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            setupViewPager1(view_Pager_ChatView);

//---------------------------------------------------------------------------------------------------------------------------------------------------



            /*
                Right Panel
             */


            endPanelAllFriendsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateFriendList();
                }
            });


            showOtherUserGroupRV();
            showUserPublicGroupRV();

        } else {
            startActivity(new Intent(this, getStarted.class));
            this.finish();
        }


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

        bottomSheetDialog.show();

    }


    /*
    Dialog box when click on + symbol in left slider where it have option to create Public server and Private Server
     */
    private void createGroupDialog() {
        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(this).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_creategroup, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        Button btn_Public = dialogView.findViewById(R.id.btn_Public);
        Button btn_Private = dialogView.findViewById(R.id.btn_Private);
        Button btn_CreateGroup = dialogView.findViewById(R.id.btn_CreateGroup);
        LinearLayout ll_CreatingServer = dialogView.findViewById(R.id.ll_groupFamFrnds);
        LinearLayout ll_creategroup = dialogView.findViewById(R.id.ll_creategroup);
        EditText et_GroupName = dialogView.findViewById(R.id.et_GroupName);

        ll_CreatingServer.setVisibility(View.VISIBLE);
        btn_Public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupCategory = "Public";
                ll_CreatingServer.setVisibility(View.GONE);
                ll_creategroup.setVisibility(View.VISIBLE);
            }
        });
        btn_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_CreatingServer.setVisibility(View.GONE);
                ll_creategroup.setVisibility(View.VISIBLE);
                GroupCategory = "Private";
            }
        });
        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupName = et_GroupName.getText().toString().trim();

                DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                changeCOor.child("serverName").setValue(GroupName);

                if (GroupName.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                } else {

                    DatabaseReference referenceALLPBLCGroup = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("All_Universal_Group");

                    referenceALLPBLCGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount();
                            String push = "Uni_Group_No_" + noofGroupinCategory + "_" + GroupName;
                            changeCOor.child("groupPushId").setValue(push);

                            refAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push);
                            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push).child("User_Subscribed_Groups").child(userID);

                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);

                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, GroupName, GroupCategory, noofGroupinCategory);
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, GroupName, GroupCategory, noofGroupinCategory);
                            userSubsGroupClass = new Class_Group(dateTimeCC, userName, userID, userID, GroupName, push, "Admin", "Online");

                            refAllGroup.setValue(userAddGroupClass);
                            refGroupSubsList.setValue(userSubsGroupClass);
                            refuserAllGroup.setValue(userAddGroupClass);

  /*

                            refClassStudentList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(push);
                            Log.d(TAG, "onDataChange: St" + push);

                            refClassStudentList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                                    .child("0").child("classStudentList");
                            refClassStudentList.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d(TAG, "onDataChange: St" + dataSnapshot.child("studentName"));
                                        Class_Student_Details class_student_details = new Class_Student_Details(true,userID,userName);
                                        refClassStudentList.setValue(class_student_details);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
*/
                            if (GroupCategory.equals("Public")) {
                                refuserPublicGroup.setValue(userAddGroupClass);
                            }
                            if (GroupCategory.equals("Private")) {
                                refuserPersonalGroup.setValue(userAddGroupClass);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                    Toast.makeText(Server_Activity.this, "Group Successfully Created", Toast.LENGTH_SHORT).show();
                    dialogBuilder.dismiss();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    /*
    Public group part
     */

    public void showUserPublicGroupRV() {

        showUserPublicGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {


            @Override
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID, String groupCategory) {
                Log.d(TAG, "showChildGroupAdaptor: Clicked");

                ib_servSettings.setVisibility(View.VISIBLE);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "showChildGroupAdaptor: Clicked" + snapshot.getKey());
                        parentItemArrayListClassName.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d(TAG, "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());

                            Class_Group_Names class_group_names = new Class_Group_Names();
                            class_group_names.setGroupPushId(groupPushId);
                            class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));
                            class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));

                            Log.d("JOIN", "onClick: " + groupPushId);

                            GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                    new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                    };

                            class_group_names.setChildItemList(dataSnapshot.child("classSubjectData").getValue(genericTypeIndicator));

                            GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
                                    new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
                                    };

                            class_group_names.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));


                            parentItemArrayListClassName.add(class_group_names);
                        }
                        adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                        adapter_classGroup.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                addNewClassButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                addSubChild0(position, groupName, groupPushId, groupUserID, Category, "newClass");
                        openDialog();

                    }

                    private void openDialog() {

                        View customAlertDialog = LayoutInflater.from(Server_Activity.this).inflate(R.layout.dialog_add_class_group, null, false);


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Server_Activity.this);
                        EditText et_ClassName = customAlertDialog.findViewById(R.id.et_ClassName);
                        Button btn_nextAddTopic = customAlertDialog.findViewById(R.id.btn_nextAddTopic);


                        dialogBuilder.setView(customAlertDialog);
                        AlertDialog dialog = dialogBuilder.show();

                        btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String classGroupName = et_ClassName.getText().toString().trim();
                                if (classGroupName.isEmpty()) {
                                    Toast.makeText(Server_Activity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                                    et_ClassName.setError("Enter Class Name");
                                } else {
                                    dialog.cancel();
                                    saveClassGroup(groupPushId, classGroupName);
                                }
                            }
                        });


                        //dialogBuilder.setCancelable(false);

                        firebaseAuth = FirebaseAuth.getInstance();
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        userID = currentUser.getUid();
                        userEmailID = currentUser.getEmail();
                        userPhoto = currentUser.getPhotoUrl();
                        userName = currentUser.getDisplayName();


                    }


                });
//                showchildGroupRV(position, groupName, groupPushId, groupUserID, groupCategory);


            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {

            }


        });
        int count = 0;

        Toast.makeText(getApplicationContext(), "Clicked on Server", Toast.LENGTH_SHORT).show();

        friendSection.setVisibility(View.GONE);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);

                Log.i("SBO", dataSnapshot.getRef().toString());
                list_UserPublicGroupTitle.add(userQuestions);
                showUserPublicGroupadaptor.notifyDataSetChanged();
                notifyPB.dismiss();


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
        list_UserPublicGroupTitle.clear();
        refShowUserPublicGroup.orderByChild("groupno").addChildEventListener(childEventListener);


        refShowUserPublicGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    tv_UserPublicTitle.setText("Public Server");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//
//    }

    }


    /*
    Other group part
     */
    public void showOtherUserGroupRV() {


        showOtherUserPublicGroupAdaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
            @Override
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {

            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID, String groupCategory) {


                DatabaseReference databaseReferenceTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("classPos");

                databaseReferenceTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String classPos = String.valueOf(snapshot.getValue());


                        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPos).child("classStudentList");

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
                        listGrpMemberList.clear();


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPos);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                parentItemArrayListClassName.clear();
                                Log.d("OTH", "class Name: " + snapshot.child("className").getValue());

                                Class_Group_Names class_group_names = new Class_Group_Names();
                                class_group_names.setGroupPushId(groupPushId);
                                class_group_names.setClassName(snapshot.child("className").getValue(String.class));
                                class_group_names.setUniPushClassId(snapshot.child("classUniPushId").getValue(String.class));

                                Log.d("JOIN", "onClick: " + groupPushId);

                                GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                        new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                        };

                                class_group_names.setChildItemList(snapshot.child("classSubjectData").getValue(genericTypeIndicator));


                                GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
                                        new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
                                        };

                                class_group_names.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));


                                parentItemArrayListClassName.add(class_group_names);

                                adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                                adapter_classGroup.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                                        /*
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "showChildGroupAdaptor: Clicked" + snapshot.getKey());
                        parentItemArrayListClassName.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d(TAG, "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());

                            Class_Group_Names class_group_names = new Class_Group_Names();
                            class_group_names.setGroupPushId(groupPushId);
                            class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));

                            Log.d("JOIN", "onClick: " + groupPushId);

                            GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                    new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                    };

                            class_group_names.setChildItemList(dataSnapshot.child("classSubjectData").getValue(genericTypeIndicator));
                            parentItemArrayListClassName.add(class_group_names);

                        }

                        adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                        adapter_classGroup.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                showchildGroupRV(position, groupName, groupPushId, groupUserID, groupCategory);
                */


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
        int count = 0;

        friendSection.setVisibility(View.GONE);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("User_Subscribed_Groups").child(userID).exists()) {
                    String check = dataSnapshot.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
                    if (check.equals("Class Member")) {
                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);

                        Log.i("SBO1", dataSnapshot.getRef().toString());
                        list_OtherUserPublicGroupTitle.add(userQuestions);
                        showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
                        notifyPB.dismiss();
                        tv_OtherTitle.setText("Other Server");

                    }
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
        refotheruserPublicGroup.orderByChild("groupno").addChildEventListener(childEventListener);


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
//                    Log.d("TAG", "onDataChange: FRNDS: "+postSnapshot.getKey());
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


/*
        List<Class_Group> friends = new ArrayList<Class_Group>();
        refFriendList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    friends.clear();
                    friends.add(postSnapshot.getValue(Class_Group.class));
                    Log.d("TAG", "chatListDashboard: \n" + friends);
                }

//                String p = snapshot.getValue().toString();
//                Log.d("TAG", "chatListDashboard: \n" + p);
//                checkChatDashboard("b8cEWxBEoxOmZBWgwWZq8uk2UKs1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fetchFriendList(friends);
*/
//        Toast.makeText(getApplicationContext(), "refFriendList"+refFriendList, Toast.LENGTH_SHORT).show();
/*
        showChatListDashadaptor.setOnItemClickListener(new Adaptor_Friends.OnItemClickListener() {
            @Override
            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID, String frndUserId) {
                tv_FrndP_Title.setText(subGroupName);
                checkChatDashboard(frndUserId);
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot.child("Chat_Message").hasChildren()) {
                        Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                        list_ChatListDashboard.add(class_userDashBoard);
                    }
                    showChatListDashadaptor.notifyDataSetChanged();
                } else {
                    Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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
        refFriendList.addChildEventListener(childEventListener);
*/

//        list_ChatListDashboard.clear();
    }

    private void fetchFriendList(List<Class_Group> friends) {
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        for (Class_Group friend : friends) {
            FriendsListClass friendsList = new FriendsListClass(friend.getUserId(), friend.getUserName());
//            friendsListClasses.add(friendsList);
            Toast.makeText(getApplicationContext(), "" + friendsList.getName(), Toast.LENGTH_SHORT).show();
//            Log.d("TAGN", "chatListDashboard: \n" + friend.getUserName());

        }
    }

    private void showchildGroupRV(int position, String groupName, String groupPushId, String groupUserID, String Category) {


        groupSection.setVisibility(View.VISIBLE);
        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
        refChildGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    recyclerViewClassList.setVisibility(View.VISIBLE);
                    textViewGroupName.setVisibility(View.VISIBLE);
                    classNameTextView.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(Server_Activity.this, "Please Create Sub Group using (+) Sign", Toast.LENGTH_LONG).show();
                    recyclerViewClassList.setVisibility(View.GONE);
                    textViewGroupName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

    }


    private void showDoubt(String groupPushId, String subGroupPushId, String groupClassSubjects) {
        fab_addDoubtQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDoubtBtmDialog(groupPushId, subGroupPushId, groupClassSubjects);
            }
        });
    }


    private void upDateDashboard(int position, String groupName, String subGroupName, String groupPushId,
                                 String subGroupPushId, String groupClassSubjects) {

        topicNamesRecyclerView.setVisibility(View.GONE);

        /*
        Why I'm  here i don't know
         */
        DatabaseReference refUserCategory = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("Category").exists()) {
                        String getTeach = snapshot.child("Category").getValue().toString().trim();
                        if (getTeach.equals("School")) {
                            btn_lteachattend.setVisibility(View.VISIBLE);
                            btn_lteachexam.setVisibility(View.VISIBLE);
                            btn_lteachresult.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        rl_FrndChatLayout.setVisibility(View.GONE);
        ll_TabChatDoubt.setVisibility(View.VISIBLE);
        btn_caddgroup.setVisibility(View.GONE);
        btn_cjoingroup.setVisibility(View.GONE);
        rl_ChatDashboard.setVisibility(View.VISIBLE);
        ll_bottom_send.setVisibility(View.VISIBLE);
        endPanelLinearLayout.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        tv_cpaneltitle.setVisibility(View.VISIBLE);
        tv_cpanelbody.setVisibility(View.VISIBLE);

        tv_cpaneltitle.setText(groupName);
        tv_cpanelbody.setText(subGroupName);

        //Attendance button clicked
        btn_lteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Server_Activity.this, Attendance_Activity.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", subGroupPushId);
                intent.putExtra("classPushId", groupClassSubjects);
                startActivity(intent);
            }
        });

        //fetching data from firebase into listView for chatDashboard
        refChatDashboard = FirebaseDatabase.getInstance().getReference()
                .child("Groups").child("Chat_Message").child(groupPushId).child(subGroupPushId).child(groupClassSubjects);


/* for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            // TODO: handle the post
        }*/

        FirebaseDatabase.getInstance().getReference()
                .child("Groups").child("Chat_Message").child(groupPushId).child(subGroupPushId).child(groupClassSubjects).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_ChatDashboard.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Class_Group class_userDashBoard = postSnapshot.getValue(Class_Group.class);
                    list_ChatDashboard.add(class_userDashBoard);
                }
                showChatDashadaptor.notifyDataSetChanged();
                showChatDashadaptor.scrollDownl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (dataSnapshot.getChildrenCount() > 0) {
////                    list_ChatDashboard.clear();
//                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
//                    if (class_userDashBoard.get)
//                        list_ChatDashboard.add(class_userDashBoard);
//
//                    showChatDashadaptor.notifyDataSetChanged();
//                    showChatDashadaptor.scrollDownl();
//                } else {
//                    Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };

        //on item of doubt clicked
//        refChatDashboard.addChildEventListener(childEventListener);

        /*
        fetching douch dashboard
         */
        DatabaseReference allDoubtReference = FirebaseDatabase.getInstance().getReference().
                child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubjects).child("All_Doubt");

        list_DoubtDashboard.clear();

        ChildEventListener doubtchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_DoubtDashboard.add(class_userDashBoard);
                    showDoubtDashAdaptor.notifyDataSetChanged();
                } else {
                    Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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

        allDoubtReference.addChildEventListener(doubtchildEventListener);

        /*
        On doubt list clicked this listener is running
         */
        showDoubtDashAdaptor.setOnItemClickListener(new Adaptor_ShowDoubt.OnItemClickListener() {
            @Override
            public void showDoubtChat(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush) {
                Bundle bundle = new Bundle();
                bundle.putString("groupPushId", groupPush);
                bundle.putString("groupClassPushId", groupClassPush);
                bundle.putString("groupClassSubjectPushId", groupSubjectPush);
                bundle.putString("doubtQuestionPushId", doubtQuestionPush);

                doubtFragment.setArguments(bundle);
                Rv_DoubtChat.setVisibility(View.GONE);
                rv_DoubtDashboard.setVisibility(View.GONE);
                parentDoubtDashboard.setVisibility(View.GONE);
                if (fragmentManager.findFragmentByTag("zero") != null) {
                    getSupportFragmentManager().beginTransaction().show(doubtFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.rl_DoubtDashboard, doubtFragment, "zero").commit();
                }


            }


        });


        //chat message updation in firebase realtime database


        DatabaseReference refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
//                        Class_Group class_group=new Class_Group();

                    if (snapshot.child("Category").exists()) {
                        String getTeach = snapshot.child("Category").getValue().toString().trim();
                        if (getTeach.equals("Teacher")) {
                            btn_lteachattend.setVisibility(View.VISIBLE);
                            btn_lteachexam.setVisibility(View.VISIBLE);
                            btn_lteachresult.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    private void sentInvitation(String adminGroupID, String adminUserName, String Code) {
        String req = null, notifyStatus = null;
        if (Code.equals("AddFrnd")) {
            req = "Friend";
            notifyStatus = "Friend_Request";
        } else if (Code.equals("FollowFrnd")) {
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

    //end panel friends clicked
    private void updateFriendList() {
        show_FriendAdaptor.setOnItemClickListener(new Adaptor_Friends.OnItemClickListener() {
            @Override
            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID, String frndUserId) {
                checkChatDashboard(frndUserId);
            }
        });
        list_Friend.clear();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                list_Friend.add(userQuestions);
                show_FriendAdaptor.notifyDataSetChanged();
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
//        refFriendList.addChildEventListener(childEventListener);
    }


    private void showBtmDialogUserProfile(String memberUserId, String memberUserName) {

        BottomSheetDialog btmSheetUserProfile = new BottomSheetDialog(this);
        btmSheetUserProfile.setCancelable(true);
        btmSheetUserProfile.setContentView(R.layout.btmdialog_profileothers);
        DatabaseReference refUserStatus, refUserFollowers, refUserFollowing;


        Button btn_SentFrndReq = btmSheetUserProfile.findViewById(R.id.btn_SentFrndReq);
        Button btn_FollowFrnd = btmSheetUserProfile.findViewById(R.id.btn_FollowFrnd);
        Button btn_messageFrnd = btmSheetUserProfile.findViewById(R.id.btn_message);

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
        LinearLayout ll_AddCategory = btmSheetUserProfile.findViewById(R.id.ll_AddCategory);

        TextView tv_CountFollowers = btmSheetUserProfile.findViewById(R.id.tv_CountFollowers);
        TextView tv_CountFollowing = btmSheetUserProfile.findViewById(R.id.tv_CountFollowing);


        tv_Name.setText(memberUserName);

        btn_messageFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btmSheetUserProfile.dismiss();

                ll_AddJoinGrp.setVisibility(View.GONE);
                overlappingPanels.closePanels();
                Fragment fragment = new Friend_Chat_Activity();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.center_panel, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();

//                Intent intent = new Intent(getApplicationContext(), Friend_Chat_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", memberUserName);
                bundle.putString("receiverUid", memberUserId);
                fragment.setArguments(bundle);

//                intent.putExtra("name", memberUserName);
//                intent.putExtra("receiverUid", memberUserId);
//                startActivity(intent);
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

        refUserFollowers = FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(memberUserId);
        refUserFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    tv_CountFollowers.setText((int) count + " Followers");
                } else {
                    tv_CountFollowers.setText("No Followers");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowing = FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(memberUserId);
        refUserFollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    tv_CountFollowing.setText((int) count + " Following");
                } else {
                    tv_CountFollowing.setText("No Following");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btn_FollowFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentInvitation(memberUserId, memberUserName, "AddFrnd");
                Log.d("FRIEND", "friend's name: " + memberUserName + "\n friend's userId: " + memberUserId);

            }
        });
        btn_SentFrndReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sentInvitation(memberUserId, memberUserName, "FollowFrnd");
            }
        });

        btmSheetUserProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btmSheetUserProfile.show();
    }


    private void checkChatDashboard(String frndUserId) {
        rl_FrndChatLayout.setVisibility(View.VISIBLE);
        overlappingPanels.closePanels();
        refChatDashboard = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID).child(frndUserId).child("Chat_Message");
//        list_NewChatDashboard.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_NewChatDashboard.add(class_userDashBoard);
                    showChatDashadaptor.notifyDataSetChanged();
                    showChatDashadaptor.scrollDownl();
                } else {
                    Toast.makeText(Server_Activity.this, "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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
        refChatDashboard.addChildEventListener(childEventListener);


        ib_FrndP_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subGroupMsg = et_FrndP_text.getText().toString().trim();
                if (subGroupMsg.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter text", Toast.LENGTH_SHORT).show();
                    et_FrndP_text.setError("Enter text");
                } else {
                    refChatDashboard = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID).child(frndUserId).child("Chat_Message");
                    refChatDashboard.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "mszno_" + noofGroupinCategory;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, "groupPushId", "subGroupPushId", subGroupMsg);
                            refChatDashboard.child(push).setValue(userAddGroupClass);
                            et_FrndP_text.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
        });

    }

    /*
    Create new class functionality - new alert box come for creating new class and thier new topic
     */
    /*
    private void addSubChild0(int position, String groupTitle, String groupPushId,
                              String groupUserID, String Category, String subChildGroupName) {

        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(this).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_createclass, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        Button btn_nextAddTopic = dialogView.findViewById(R.id.btn_nextAddTopic);
        Button btn_CreateTopic = dialogView.findViewById(R.id.btn_CreateTopic);

        LinearLayout ll_addTopic = dialogView.findViewById(R.id.ll_addTopic);
        LinearLayout ll_creategroup = dialogView.findViewById(R.id.ll_creategroup);

        EditText et_ClassName = dialogView.findViewById(R.id.et_ClassName);
        EditText et_TopicName = dialogView.findViewById(R.id.et_TopicName);
        TextView tv_ClassName = dialogView.findViewById(R.id.tv_ClassName);
        TextView tv_AddClassTitle = dialogView.findViewById(R.id.tv_AddClassTitle);

        ll_addTopic.setVisibility(View.GONE);

        if (subChildGroupName.equals("newClass")) {
            et_ClassName.setVisibility(View.VISIBLE);
            tv_ClassName.setVisibility(View.GONE);
            ll_creategroup.setVisibility(View.VISIBLE);
        } else {
            ll_creategroup.setVisibility(View.GONE);
            ll_addTopic.setVisibility(View.VISIBLE);
            tv_ClassName.setVisibility(View.VISIBLE);
            tv_AddClassTitle.setVisibility(View.GONE);
            tv_ClassName.setText("Please create a new Topic under " + subChildGroupName);
        }


        btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sbChildGroupName = et_ClassName.getText().toString().trim();
                if (sbChildGroupName.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter All Details", Toast.LENGTH_SHORT).show();
                    et_ClassName.setError("Enter Class Name");
                } else {
                    refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups")
                            .child("All_Sub_Group").child(groupPushId);
                    refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups")
                            .child("All_Sub_Group").child(groupPushId).child(sbChildGroupName).child("SubGroup_SubsList");
                    refChildGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);
                            String position = Integer.toString(noofQuesinCategory);
                            Class_Group subGroup_Class = new Class_Group(dateTimeCC, userName, userID, sbChildGroupName, position, groupTitle, groupPushId);
                            Class_Group subGroupSubsList_Class = new Class_Group(dateTimeCC, userName, userID, userID, groupTitle, groupPushId, "Admin", sbChildGroupName);

                            refChildGroup.child(sbChildGroupName).setValue(subGroup_Class);
                            refChildGroupSubsList.child(userID).setValue(subGroupSubsList_Class);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    ll_addTopic.setVisibility(View.VISIBLE);
                    ll_creategroup.setVisibility(View.GONE);
                    tv_ClassName.setVisibility(View.VISIBLE);

                    tv_ClassName.setText("you have successfully created " + sbChildGroupName + " under Group" + groupTitle);

                }
            }
        });

        btn_CreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String subChildGroupName=et_GroupName.getText().toString().trim();
                String subTopicName = et_TopicName.getText().toString().trim();
                if (subTopicName.isEmpty()) {
                    Toast.makeText(Server_Activity.this, "Enter Topic Name", Toast.LENGTH_SHORT).show();
                    et_TopicName.setError("Enter Topic Name");
                } else {

//

                    if (subChildGroupName.equals("newClass")) {
                        String sbChildGroupName = et_ClassName.getText().toString().trim();
                        refGroupTopic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group")
                                .child(groupPushId).child(sbChildGroupName);
                        refGroupTopic.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);
                                switch (noofQuesinCategory) {
                                    case 13:
                                        refChildGroup.child(sbChildGroupName).child("group1").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 14:
                                        refChildGroup.child(sbChildGroupName).child("group2").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 15:
                                        refChildGroup.child(sbChildGroupName).child("group3").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 16:
                                        refChildGroup.child(sbChildGroupName).child("group4").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 17:
                                        refChildGroup.child(sbChildGroupName).child("group5").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 18:
                                        refChildGroup.child(sbChildGroupName).child("group6").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 19:
                                        refChildGroup.child(sbChildGroupName).child("group7").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 20:
                                        refChildGroup.child(sbChildGroupName).child("group8").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 21:
                                        refChildGroup.child(sbChildGroupName).child("group9").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(Server_Activity.this, "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                                }
                                startActivity(new Intent(Server_Activity.this, Server_Activity.class));
                                Server_Activity.this.overridePendingTransition(0, 0);

                                dialogBuilder.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        refGroupTopic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group")
                                .child(groupPushId).child(subChildGroupName);
                        refGroupTopic.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);

                                switch (noofQuesinCategory) {
                                    case 13:
                                        refChildGroup.child(subChildGroupName).child("group1").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 14:
                                        refChildGroup.child(subChildGroupName).child("group2").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 15:
                                        refChildGroup.child(subChildGroupName).child("group3").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 16:
                                        refChildGroup.child(subChildGroupName).child("group4").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 17:
                                        refChildGroup.child(subChildGroupName).child("group5").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 18:
                                        refChildGroup.child(subChildGroupName).child("group6").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 19:
                                        refChildGroup.child(subChildGroupName).child("group7").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 20:
                                        refChildGroup.child(subChildGroupName).child("group8").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 21:
                                        refChildGroup.child(subChildGroupName).child("group9").setValue(subTopicName);
                                        Toast.makeText(Server_Activity.this, "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(Server_Activity.this, "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                                }
                                showGroupadaptor.notifyDataSetChanged();
                                startActivity(new Intent(Server_Activity.this, Server_Activity.class));
                                Server_Activity.this.overridePendingTransition(0, 0);


                                dialogBuilder.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }

                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

*/
    /*
        Google Signin Result
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
                String s = "Google Signin is sucessful";
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

    private void setupViewPager1(ViewPager view_pager_chatView) {
        Adaptor_Tab_ChatDiscussion adaptorTabFrndChat = new Adaptor_Tab_ChatDiscussion(getSupportFragmentManager());

        adaptorTabFrndChat.addFragment(new Chat_New_Fragment(), "New Chat");
        adaptorTabFrndChat.addFragment(new Chat_List_Fragment(), "Friends List");

        view_pager_chatView.setAdapter(adaptorTabFrndChat);
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
        final String udateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

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
                        refUserRegister.child("DateTime").setValue(udateTimeCC);
                        refUserRegister.child("Category").setValue("Student");
                        refUserRegister.child("userStatus").setValue("Online");
                        refUserRegister.child("token").setValue(token);
                        notifyPB.dismiss();
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
        exitApp();

    }


    /*
    Alert box show when user want to exit the app
     */
    private void exitApp() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Server_Activity.this);
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Are you want to close this application?")
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    Server_Activity.this.finishAffinity();
                                    System.exit(0);
                                } else {
                                    Server_Activity.this.finish();
                                    System.exit(0);
                                }
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

    @Override
    public void onClassClickListener(int position, String classGroupName, String uniPushClassId) {

        DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        posTemp.child("classPosition").setValue(position);
        posTemp.child("clickedClassName").setValue(classGroupName);
        posTemp.child("uniPushClassId").setValue(uniPushClassId);


//        classPosition = String.valueOf(position);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("")
    }

    @Override
    public void onAddSubjectClickListener(String groupName, String uniPushClassId) {

        Log.d("SNAAPYKey", "onDataChange: " + uniPushClassId);

        View customAlertDialog = LayoutInflater.from(Server_Activity.this).inflate(R.layout.dialog_add_new_subject_topic, null, false);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Server_Activity.this);


        Button btn_CreateTopic = customAlertDialog.findViewById(R.id.btn_CreateTopic);

        LinearLayout ll_addTopic = customAlertDialog.findViewById(R.id.ll_addTopic);
        LinearLayout ll_creategroup = customAlertDialog.findViewById(R.id.ll_creategroup);

        EditText et_TopicName = customAlertDialog.findViewById(R.id.et_TopicName);
        TextView tv_ClassName = customAlertDialog.findViewById(R.id.tv_ClassName);
        dialogBuilder.setView(customAlertDialog);

        AlertDialog dialog = dialogBuilder.show();


        btn_CreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_TopicName.getText().toString().isEmpty()) {

                    DatabaseReference getServerTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                    getServerTemp.child("subjectName").setValue(et_TopicName.getText().toString());

                    getServerTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("SerT", "Temp Server name: " + (snapshot.child("serverName").getValue()));
                            Log.d("SerT", "Temp Group Push Id: " + (snapshot.child("tpGroupPushId").getValue()));

                            String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString();

                            DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);

                            testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


//                        Log.d("SNAAP", "onDataChange: "+snapshot.getChildrenCount());
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Log.d("SNAAP", "onDataChange: " + dataSnapshot.child("classSubjectData").child("1").child("subjectName").getValue());
//                            Log.d("SNAAPY", "onDataChange: " + dataSnapshot.getKey());
                                        Log.d(TAG, "showChildGroupAdaptor: ClickedDDS" + dataSnapshot.getKey());
                                        Log.d(TAG, "showChildGroupAdaptor: ClickedDS" + dataSnapshot.child("className").getValue().toString());
                                        Class_Group_Names class_group_names = new Class_Group_Names();
                                        class_group_names.setGroupPushId(groupPushId);
                                        class_group_names.setClassName(dataSnapshot.child("className").getValue(String.class));
                                        class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));

//                                        Log.d("SNAAPYKey", "onDataChange: " + );

                                        testDatabaseReference.child(uniPushClassId).child("classSubjectData").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                testDatabaseReference.child(uniPushClassId).child("classSubjectData").child("al;kjsfdn").child("subjectName").setValue(et_TopicName.getText().toString());


                                                GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                                        new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                                        };

//                                                testDatabaseReference.child(String.valueOf(dataSnapshot.getKey())).child("classSubjectData").setValue(class_group_names);

                                                if (snapshot.hasChild("classSubjectData")) {
                                                    childItemArrayListClassName = snapshot.child("classSubjectData").getValue(genericTypeIndicator);
                                                }


                                                Subject_Details_Model subject_details_model = new Subject_Details_Model();
                                                subject_details_model.setSubjectName(et_TopicName.getText().toString());


                                                childItemArrayListClassName.add(subject_details_model);

                                                class_group_names.setChildItemList(childItemArrayListClassName);

                                                GenericTypeIndicator<ArrayList<Class_Student_Details>> genericTypeIndicatorStudent =
                                                        new GenericTypeIndicator<ArrayList<Class_Student_Details>>() {
                                                        };

                                                class_group_names.setClass_student_detailsList(snapshot.child("classStudentList").getValue(genericTypeIndicatorStudent));


                                                parentItemArrayListClassName.add(class_group_names);


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


/*
                            DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                            testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d(TAG, "onDataChange: "+snapshot.getKey());
                                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                                        Log.d(TAG, "onDataChange: Children: "+snapshot1.child(""));
                                    }
//                                    testDatabaseReference.child(String.valueOf(snapshot.getChildrenCount())).child("className").setValue(et_TopicName + "Testttt");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
*/
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
/*
                    DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");
                    testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            parentItemArrayListClassName.clear();
                            childItemArrayListClassName.clear();
                            Log.d(TAG, "onDataChange: " + snapshot.getKey() + " VALUE" + snapshot.getValue());
                            for (DataSnapshot snapshot2 : snapshot.getChildren()) {

                                Log.d(TAG, "onDataChange:2 " + snapshot2.getKey() + " VALUE" + snapshot2.getValue());
                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                    if (groupName == snapshot3.child("className").getValue(String.class)) {
                                        Log.d(TAG, "onDataChange:3 " + snapshot3.getKey() + " VALUE" + snapshot3.getValue());

                                        Class_Group_Names class_group_names = new Class_Group_Names();
                                        class_group_names.setClassName(snapshot2.child("className").getValue(String.class));

                                        GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                                new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                                };
                                        testDatabaseReference.child(snapshot2.getKey()).child(snapshot3.getKey()).child("classSubjectData").setValue(class_group_names);


                                        if (snapshot2.hasChild("classSubjectData")) {
                                            childItemArrayListClassName = snapshot2.child("classSubjectData").getValue(genericTypeIndicator);
                                        }


                                        Subject_Details_Model subject_details_model = new Subject_Details_Model();
                                        subject_details_model.setSubjectName(et_TopicName.getText().toString());


                                        childItemArrayListClassName.add(subject_details_model);

                                        class_group_names.setChildItemList(childItemArrayListClassName);

                                        parentItemArrayListClassName.add(class_group_names);
                                    }
//

                                }

                            }

                            adapter_classGroup.setParentItemArrayListClassName(parentItemArrayListClassName);
                            adapter_classGroup.notifyDataSetChanged();

//                            testDatabaseReference.child(push).child(String.valueOf(snapshot.getChildrenCount())).child("className").setValue(et_TopicName+"Testttt");
//                            Subject_Details_Model subject_details_model = new Subject_Details_Model();
//                            subject_details_model.setSubjectName(et_TopicName.getText().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
*/
                    /*
                    Subject_Details_Model subject_details_model = new Subject_Details_Model();
                    subject_details_model.setSubjectTitle(et_TopicName.getText().toString());
                    subject_details_model.setGroupName(groupName);
//                    List<Group_Students> groupStudentList = new ArrayList<>();
//                    Group_Students group_student = new Group_Students();
//                    group_student.setUserName(userName);
//                    group_student.setAdmin(true);
//                    group_student.setUserId(SharePref.getDataFromPref(Constant.USER_ID));
//                    groupStudentList.add(group_student);
//                    subject_details_model.setGroupStudentList(groupStudentList);
                    subject_details_model.setSubjectCreationDate(dateTimeCC);
                    refChildGroup.child(groupName).child(subject_details_model.getSubjectTitle()).setValue(subject_details_model);
//                    readChildData(groupName, subject_details_model.getSubjectTitle());
                    dialog.dismiss();
                    */

                }
            }
        });


    }





//    private void readChildData(String groupName, String subjectTitle) {
//        refChildGroup.child(groupName).child(subjectTitle)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot childSnap : snapshot.getChildren()) {
//                            Subject_Details_Model subject_details_model = childSnap.getValue(Subject_Details_Model.class);
//                            childItemArrayListClassName.add(subject_details_model);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }
}