package com.cllasify.cllasify.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Server_Activity extends AppCompatActivity implements Adapter_ClassGroup.onAddSubjectClickListener {


    TabLayout tabLayout, tabl_ChatView;
    ViewPager viewPager, view_Pager_ChatView;
    BottomNavigationView bottomNavigationView;
    TextView tv_GroupMember, adminListText, FriendListText;
    RecyclerView rv_UserPublicGroupTitle, rv_OtherPublicGroupTitle, rv_GrpMemberList, rv_GrpTeacherList, recyclerViewClassList, endPanelAllFriendsRecyclerView;
    RelativeLayout rl_ChatView;
    FirebaseDatabase refFriendList;
    DatabaseReference refChatDashboard, refDoubtDashboard;
    DatabaseReference refuserPersonalGroup, refuserAllGroup, refGroupTopic,
            refuserPublicGroup, refAllGroup,
            refotheruserPublicGroup, refUserStatus,
            refSearchShowGroup, refShowUserPublicGroup, refShowUserPrivateGroup, refShowUserAllGroup, refShowUserJoinedGroup,
            refteachStud,
            refChildGroup, refChildGroupSubsList,
            refGroupSubsList, refGrpMemberList, refClassTeacherList, refFriendLists;

    DatabaseReference checkFriends;
    ImageButton btn_joinNotification;
    LinearLayout onlyAdminLayout;

    List<Group_Students> group_studentsListDetails;
    List<Class_Group> list_GroupTitle, list_UserPrivateGroupTitle, list_UserPublicGroupTitle, list_OtherUserPublicGroupTitle,
            list_SubChild,
            list_ChatDashboard, list_DoubtDashboard, list_Friend, list_ChatListDashboard, list_NewChatDashboard;

    List<Class_Student_Details> listGrpMemberList, listGrpTeacherList, listFriendList;
    List<String> allFriendsList;

    Adaptor_QueryGroup showGroupadaptor, showUserPrivateGroupadaptor, showUserPublicGroupadaptor;
    Adaptor_ShowGrpMember showGrpMemberList, showGrpTeacherList;
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

    ImageButton ib_cattach, ib_csubmit, ib_doubtSubmit;
    ImageButton ImageViewRecentChat, ib_FrndP_csubmit, ib_servSettings;
    Button btn_caddgroup, btn_cjoingroup, addNewClassButton;
    Button btn_lteachresult, btn_lteachexam;
    ImageButton btn_lteachattend;
    LinearLayout groupSection, ll_AddJoinGrp;

    RecyclerView rvFriendsList;
    Adaptor_FriendsList adaptor_friendsList;
    List<FriendsListClass> friendsListClasses;


    LinearLayout ll_bottom_send, ll_ChatDoubtDashboard,
            endPanelLinearLayout, ll_TabChatDoubt;
    TextView tv_ChatDashboard, tv_DoubtDashboard;
    RelativeLayout rl_ChatDashboard, rl_DoubtDashboard;
    RelativeLayout rl_FrndChatLayout;
    SearchView Rv_DoubtChat;
    RelativeLayout parentDoubtDashboard;

    public static RecyclerView rv_ChatDashboard, rv_DoubtDashboard;

    FragmentManager fragmentManager;
    DoubtFragment doubtFragment;
    Friend_Chat_Activity friendChatFragment;

    LinearLayout friendSection;


    //Right Panel Class and Sub-class
    Adapter_ClassGroup adapter_classGroup;

    List<Class_Group_Names> parentItemArrayListClassName;
    List<Subject_Details_Model> childItemArrayListClassName;


    //Chat_Activity
    private static final String TAG = "ChatActivity";
    ValueEventListener seenListener, readLiveMessageListener, readLiveDoubtListener;
    private DatabaseReference reference;
    private DatabaseReference allDoubtReference;
    private ArrayList<Class_Group> chats, doubts;
    private MessageAdapter messageAdapter;
    private boolean onScreen;
    boolean flag = false;
    boolean flagFriend = false;

    void init() {

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
        rl_FrndChatLayout = findViewById(R.id.rl_FrndChatLayout);

        ll_AddJoinGrp = findViewById(R.id.ll_AddJoinGrp);


        rv_ChatDashboard = findViewById(R.id.rv_ChatDashboard);
//        rv_DoubtDashboard = findViewById(R.id.rv_DoubtDashboard);
//        parentDoubtDashboard = findViewById(R.id.parentDoubtDashboard);
//
//        rl_ChatDashboard = findViewById(R.id.rl_ChatDashboard);
//        rl_DoubtDashboard = findViewById(R.id.rl_DoubtDashboard);
//
//
//        tv_ChatDashboard = findViewById(R.id.tv_ChatDashboard);
//        tv_DoubtDashboard = findViewById(R.id.tv_DoubtDashboard);
        ll_ChatDoubtDashboard = findViewById(R.id.ll_ChatDoubtDashboard);

        tv_FrndP_Title = findViewById(R.id.tv_FrndP_Title);
        tv_UserPublicTitle = findViewById(R.id.tv_UserPublicTitle);
        tv_UserPrivateTitle = findViewById(R.id.tv_UserPrivateTitle);
        tv_OtherTitle = findViewById(R.id.tv_OtherTitle);

        ib_FrndP_csubmit = findViewById(R.id.ib_FrndP_csubmit);

        btn_lteachattend = findViewById(R.id.btn_lteachattend);
        btn_lteachexam = findViewById(R.id.btn_lteachexam);
        btn_lteachresult = findViewById(R.id.btn_lteachresult);


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
        addNewClassButton = findViewById(R.id.addNewClassButton);
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
        refteachStud = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID);
        refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");

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
        showGroupadaptor = new Adaptor_QueryGroup(this, list_GroupTitle);
        showUserPrivateGroupadaptor = new Adaptor_QueryGroup(this, list_UserPrivateGroupTitle);
        showUserPublicGroupadaptor = new Adaptor_QueryGroup(this, list_UserPublicGroupTitle);
        showChatDashadaptor = new Adaptor_ShowGroup(this, list_ChatDashboard);
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
        showChatListDashadaptor = new Adaptor_Friends(this, list_ChatListDashboard);
        rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupadaptor);
        rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);


        ib_csubmit = findViewById(R.id.ib_csubmit);
        ib_doubtSubmit = findViewById(R.id.ib_doubtSubmit);


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


                        setReference(groupPushId[0], className[0], subjectName[0], classPosition[0], uniPushClassId[0], subjectUniPushId[0], clickedGroupName[0]);


                        Log.d("SCSG", "init: serverName: " + serverName[0] + "\nsubjectUniPush: " + subjectUniPushId[0] + "\nclassName: " + className[0] + "\nsubjectName: " + subjectName[0] + "\ngroupPushId: " + groupPushId[0]);

                    }
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


        messageAdapter.setOnDoubtClickListener(new MessageAdapter.onDoubtClickListener() {
            @Override
            public void onDoubtClick(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush) {

                Toast.makeText(getApplicationContext(), "Test doubt", Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putString("groupPushId", groupPush);
//                bundle.putString("groupClassPushId", groupClassPush);
//                bundle.putString("groupClassSubjectPushId", groupSubjectPush);
//                bundle.putString("doubtQuestionPushId", doubtQuestionPush);

                DatabaseReference putTempDoubt = FirebaseDatabase.getInstance().getReference().child("Groups");
                putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupPushId").setValue(groupPush);
                putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassPushId").setValue(groupClassPush);
                putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("groupClassSubjectPushId").setValue(groupSubjectPush);
                putTempDoubt.child("Temp").child(userID).child("DoubtTemps").child("doubtQuestionPushId").setValue(doubtQuestionPush);

                if (flag == false) {
//                    FragmentTransaction transaction =  getParentFragmentManager().beginTransaction();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    getFragmentManager().getBackStackEntryCount();
//                    transaction.setCustomAnimations(R.anim.transition_anim0, R.anim.transition_anim1);
                    transaction.replace(R.id.rl_ChatDashboard, doubtFragment, "FirstFragment");
                    transaction.commit();
                    flag = true;
                }
            }
        });

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

        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs");

        testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference getServerTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                getServerTemp.child("className").setValue(sbChildGroupName);

                DatabaseReference setAdmins = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins");

                getServerTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String serverName = (snapshot.child("serverName").getValue()).toString();

                        Log.d("SerT", "Temp Server name: " + serverName);
                        Log.d("SerT", "Temp Group Push Id: " + (snapshot.child("tpGroupPushId").getValue()));
                        Log.d("SerT", "Group Push Id: " + groupPushId);
                        Log.d("SerT", "Group name: " + sbChildGroupName);


                        testDatabaseReference.child(groupPushId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String[] push01 = String.valueOf(testDatabaseReference.child(groupPushId).push()).split("/");

                                testDatabaseReference.child(groupPushId).child(push01[6]).child("className").setValue(sbChildGroupName);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classBio").setValue(" ");
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classUniPushId").setValue(push01[6]);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("groupPushId").setValue(groupPushId);
                                testDatabaseReference.child(groupPushId).child(push01[6]).child("classStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot01) {
                                        Class_Student_Details class_student_details = new Class_Student_Details(true, userID, userName);
                                        setAdmins.child(groupPushId).child("classAdminList")
                                                .child(userID).setValue(class_student_details);
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

    public void checkAdmmmmin(boolean checking) {
        Log.d("ADMMMM", "is admin : " + checking);
        if (checking) {
            onlyAdminLayout.setVisibility(View.VISIBLE);
            addNewClassButton.setVisibility(View.VISIBLE);
        } else {
            onlyAdminLayout.setVisibility(View.GONE);
            addNewClassButton.setVisibility(View.GONE);
        }
    }

    private void setReference(String groupPushId, String subGroupPushId, String groupClassSubject, String classPosition, String classUniPushId, String subjectUniPushId, String serverName) {

        if (subjectUniPushId != null && classUniPushId != null) {
            DatabaseReference databaseReferenceCHKSUBJECT = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId)
                    .child("classSubjectData").child(subjectUniPushId);
            databaseReferenceCHKSUBJECT.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ll_bottom_send.setVisibility(View.VISIBLE);
                    }else{
                        ll_bottom_send.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        DatabaseReference databaseReferenceTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("clickedGroupPushId");

        databaseReferenceTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentItemArrayListClassName.clear();
                String groupPushId = String.valueOf(snapshot.getValue());

                DatabaseReference databaseReferenceGrpClass = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(groupPushId).child(userID).child("classUniPushId");

                databaseReferenceGrpClass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String classUniPush = String.valueOf(snapshot.getValue());

                        DatabaseReference databaseReferenceStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPush);

                        databaseReferenceStudent.child("classStudentList").child(userID).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Log.d("CHKADMIN", "onDataChange: " + snapshot.getValue());

                                if (Objects.equals(snapshot.getValue(), false)) {
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
//                                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//
//                                        Log.i("SBO1", dataSnapshot.getRef().toString());
//                                        list_OtherUserPublicGroupTitle.add(userQuestions);
//                                        showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
//                                        notifyPB.dismiss();
//                                        tv_OtherTitle.setText("Other Server");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
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

//                            GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
//                                    new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
//                                    };
//
//                            class_group_names.setChildItemList(dataSnapshot.child("classSubjectData").getValue(genericTypeIndicator));

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


        DatabaseReference checkAdminMe = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList").child(userID).child("admin");
        checkAdminMe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().equals(true)) {
                        checkAdmmmmin(true);
                    }
                } else {
                    checkAdmmmmin(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        DatabaseReference checkAdmin = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(groupPushId).child("classAdminList");
//        checkAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("ADMINCHK", "onDataChange: " + snapshot.getValue());
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    if (dataSnapshot.getKey().equals(userID)) {
//                        Log.d("MEADMINYES", "onDataChange: " + dataSnapshot.getKey());
//                        onlyAdminLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        Log.d("MEADMINYES", "onDataChange: nooo");
//                        onlyAdminLayout.setVisibility(View.GONE);
//                    }
//                    Log.d("MEADMIN", "onDataChange: " + dataSnapshot.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


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
                    rv_ChatDashboard.smoothScrollToPosition(rv_ChatDashboard.getAdapter().getItemCount());
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
                            rv_ChatDashboard.smoothScrollToPosition(rv_ChatDashboard.getAdapter().getItemCount());
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


        ChildEventListener doubtchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

        ib_servSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_Activity.this, Server_Settings.class);
                intent.putExtra("currUserId", userID);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("serverName", serverName);
                startActivity(intent);
            }
        });

        btn_lteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Server_Activity.this, Attendance_Activity.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", classUniPushId);
                intent.putExtra("classPushId", subjectUniPushId);
                startActivity(intent);
            }
        });

        btn_joinNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Server_Activity.this, GRPJoinReqs.class);
                intent.putExtra("groupPushId", groupPushId);
                intent.putExtra("subGroupPushId", classUniPushId);
                intent.putExtra("classPushId", subjectUniPushId);
                startActivity(intent);
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
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

            DatabaseReference chkJoinedORAddGRP = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
            chkJoinedORAddGRP.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if(dataSnapshot.child("User_Subscribed_Groups").child(userID).exists()){
                            ll_AddJoinGrp.setVisibility(View.GONE);
                        }
                        else{
                            ll_AddJoinGrp.setVisibility(View.VISIBLE);
                            Log.d("CHKJOINEDORADDGRP", "onDataChange: Nothing");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference firebaseDatabaseDARKLIGHT = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID).child("darkORlight");

            firebaseDatabaseDARKLIGHT.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.getValue().toString().equals(true)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            editor.putBoolean("isDarkModeOn", true);
                            editor.apply();
                        }
                        if (snapshot.getValue().toString().equals(false)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            editor.putBoolean("isDarkModeOn", false);
                            editor.apply();
                        }
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean("isDarkModeOn", true);
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            init();

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
//                    recyclerViewGroupNameList.setVisibility(View.VISIBLE);
//                    textViewGroupName.setText("Recent Chats");
//                    addNewClassButton.setVisibility(View.GONE);

//                    ll_AddJoinGrp.setVisibility(View.GONE);

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

                }
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

            btn_caddgroup = findViewById(R.id.btn_caddgroup);
            btn_cjoingroup = findViewById(R.id.btn_cjoingroup);

            ll_bottom_send = findViewById(R.id.ll_bottom_send);


            tabLayout = findViewById(R.id.tabl_chatdis);
            viewPager = findViewById(R.id.view_Pager);

            tabl_ChatView = findViewById(R.id.tabl_ChatView);
            view_Pager_ChatView = findViewById(R.id.view_Pager_ChatView);

            btn_cjoingroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Server_Activity.this, Discover_Activity.class);
                    startActivity(intent);
                }
            });

            btn_caddgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createGroupDialog();
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

                addNewClassButton.setVisibility(View.VISIBLE);

                Log.d("CHKFLAG", "showChildGroupAdaptor: " + flagFriend);

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

                Log.d(TAG, "showChildGroupAdaptor: Clicked");

                textViewGroupName.setText(groupName.trim());


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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

//                            GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
//                                    new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
//                                    };
//
//                            class_group_names.setChildItemList(dataSnapshot.child("classSubjectData").getValue(genericTypeIndicator));

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
//                                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//
//                                        Log.i("SBO1", dataSnapshot.getRef().toString());
//                                        list_OtherUserPublicGroupTitle.add(userQuestions);
//                                        showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
//                                        notifyPB.dismiss();
//                                        tv_OtherTitle.setText("Other Server");
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        } else {
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

//                            GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
//                                    new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
//                                    };
//
//                            class_group_names.setChildItemList(dataSnapshot.child("classSubjectData").getValue(genericTypeIndicator));

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
        int count = 0;

        friendSection.setVisibility(View.GONE);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.i("pushid", "" + dataSnapshot.getKey());

                String groupPushIdJoined = dataSnapshot.getKey();

                DatabaseReference databaseReferenceGetUserJoinedClass = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_User_Group_Class").child(groupPushIdJoined);

                databaseReferenceGetUserJoinedClass.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot01) {

                        if (snapshot01.child(userID).child("classUniPushId").exists()) {
                            if (dataSnapshot.child("User_Subscribed_Groups").child(userID).exists()) {
                                String check = dataSnapshot.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
                                if (check.equals("Class Member")) {
                                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);

                                    Log.i("SBO1", "" + dataSnapshot.getValue());
                                    list_OtherUserPublicGroupTitle.add(userQuestions);
                                    showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
                                    tv_OtherTitle.setText("Other Server");

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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
        ib_doubtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDoubtBtmDialog(groupPushId, subGroupPushId, groupClassSubjects);
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


                                                long noofQuesinCategory = snapshot.getChildrenCount() + 1;
                                                String push = "Joining Reqno_" + noofQuesinCategory;
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


    /*
    Alert box show when user want to exit the app
     */

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


                        setReference(groupPushId[0], clickedClassName, subjectName[0], String.valueOf(classUniPosition), uniClassPushId, subjectUniPushId[0], clickedGroupName[0]);

                        showDoubt(groupPushId[0], uniClassPushId, subjectUniPushId[0]);

                        Log.d("SCSG", "init: serverName: " + serverName[0] + "\nsubjectUniPush: " + subjectUniPushId[0] + "\nclassName: " + className[0] + "\nsubjectName: " + subjectName[0] + "\ngroupPushId: " + groupPushId[0]);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(getApplicationContext(), "CLICKKKKKEDDDD on SUBBBJJEECCTT", Toast.LENGTH_SHORT).show();
        overlappingPanels.closePanels();
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

                            String[] pushSubject = String.valueOf(testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(String.valueOf(snapshot.getChildrenCount())).push()).split("/");

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
                                        class_group_names.setClassBio(dataSnapshot.child("classBio").getValue(String.class));
                                        class_group_names.setUniPushClassId(dataSnapshot.child("classUniPushId").getValue(String.class));

//                                        Log.d("SNAAPYKey", "onDataChange: " + );

                                        testDatabaseReference.child(uniPushClassId).child("classSubjectData").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(pushSubject[9]).child("subjectName").setValue(et_TopicName.getText().toString());
                                                testDatabaseReference.child(uniPushClassId).child("classSubjectData").child(pushSubject[9]).child("subjectUniPushId").setValue(pushSubject[9]);
//                                                testDatabaseReference.child(uniPushClassId).child("classSubjectData").push().child("subjectName").setValue(et_TopicName.getText().toString());


                                                GenericTypeIndicator<ArrayList<Subject_Details_Model>> genericTypeIndicator =
                                                        new GenericTypeIndicator<ArrayList<Subject_Details_Model>>() {
                                                        };

//                                                testDatabaseReference.child(String.valueOf(dataSnapshot.getKey())).child("classSubjectData").setValue(class_group_names);

                                                if (snapshot.hasChild("classSubjectData")) {
                                                    childItemArrayListClassName = snapshot.child("classSubjectData").getValue(genericTypeIndicator);
                                                }
                                                Subject_Details_Model subject_details_model = new Subject_Details_Model();
                                                subject_details_model.setSubjectName(et_TopicName.getText().toString());
                                                subject_details_model.setSubjectUniPushId(pushSubject[9]);
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