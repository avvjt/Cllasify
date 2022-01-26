
package com.cllasify.cllasify.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.cllasify.cllasify.Adaptor.Adaptor_Friends;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Adaptor_Tab_ChatDiscussion;
import com.cllasify.cllasify.Server.AttendanceFragment;
import com.cllasify.cllasify.Server.ChatFragment;
import com.cllasify.cllasify.Server.Chat_List_Fragment;
import com.cllasify.cllasify.Server.Chat_New_Fragment;
import com.cllasify.cllasify.Class.Class_Group;
//import com.cllasify.cllasify.Group_Join;
import com.cllasify.cllasify.Server.Discussion_Fragment;
import com.cllasify.cllasify.JoinGroupFragment;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    TabLayout tabLayout,tabl_ChatView;
    ViewPager viewPager,view_Pager_ChatView;

    BottomNavigationView bottom_nav;
    RecyclerView rv_GroupTitle,rv_GroupData;
    RecyclerView rv_UserPrivateGroupTitle,rv_UserPublicGroupTitle,rv_OtherPublicGroupTitle
            ,rv_GrpMemberList,rv_SubChild,rv_FriendList,rv_AllFriendList;
    RelativeLayout rl_FrndPanel,rl_ChatView;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    DatabaseReference refShowAllGroup,refTempGroupDB, refGroupChildUserDashboard, refFriendList,refChatDashboard;
    DatabaseReference refuserPersonalGroup,refuserAllGroup,refGroupTopic,
            refuserPublicGroup, refAllGroup,refAllPublicJGroup,
            refotheruserPublicGroup,refUserStatus,
            refAddSubGroup,
            refSearchShowGroup,refShowUserPublicGroup,refShowUserPrivateGroup,refShowUserAllGroup,
            refteachStud,
            refChildGroup, refChildGroupSubsList,
            refGroupSubsList,refGrpMemberList;

    List<Class_Group> list_GroupTitle, list_UserPrivateGroupTitle, list_UserPublicGroupTitle, list_OtherUserPublicGroupTitle,
            listGroupSTitle,listGrpMemberList,list_SubChild,list_Friend,list_ChatListDashboard,list_NewChatDashboard;

    Adaptor_QueryGroup showGroupadaptor,showUserPrivateGroupadaptor,showUserPublicGroupadaptor;
    Adaptor_ShowGrpMember showGrpMemberList;
    Adaptor_QueryGroup showOtherUserPublicGroupAdaptor;
//    Adaptor_lvl2QueryGroup showSubChild_Adaptor;
    Adaptor_Friends show_FriendAdaptor,showChatListDashadaptor;
    Adaptor_ShowGroup showChatDashadaptor;

    FloatingActionButton fab_addDoubtQ;

    String GroupCategory;
    GoogleSignInClient googleSignInClient;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    ProgressDialog notifyPB;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID,userEmailID,userName,userdbName;
    boolean searchShow=true;
    ChipNavigationBar chipNavigationBar;
    OverlappingPanelsLayout overlappingPanels;
//    Button openStartPanelButton, btn_AddGroup,btn_show;

    CircleImageView ib_AddGroup;
    Class_Group userAddGroupClass,addAnswer,userSubsGroupClass;
    SearchView esv_groupSearchView;
//    SearchView sv_textSearchView,sv_ChatSearch,sv_DoubtSearch;
    EditText addAnswer_et,et_FrndP_text;
    TextView postAnswer_tv, dispQues_tv,QuesAskedByTime_tv,
            tv_UserPublicTitle,tv_UserPrivateTitle,tv_OtherTitle,
            tv_cpaneltitle,tv_cpanelbody,tv_StartPanel,
            tv_GroupName,
            tv_ChatDashboard,tv_DoubtDashboard,
            tv_UserDetails,tv_addTopic,
            tv_FrndP_Title;

//    ImageButton ib_cattach, ib_csubmit,
            ImageButton ib_FrndsList,ib_FrndP_csubmit;
    Button btn_caddgroup, btn_cjoingroup, btn_AddSubChild;
    Button btn_lteachresult,btn_lteachattend,btn_lteachexam;
    Button btn_AllFriends;
    LinearLayout ll_SubChild,ll_AddJoinGrp,
            ll_childData,ll_TabChatDoubt;
    LinearLayout ll_bottom_send;
    RelativeLayout rl_ChatDoubt,rl_FrndChatLayout;

    SwipeRefreshLayout srl_rv_SubChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
            userEmailID = currentUser.getEmail();
            userPhoto = currentUser.getPhotoUrl();
            userName = currentUser.getDisplayName();
            notifyPB = new ProgressDialog(getContext());
            notifyPB.setTitle("Govt Jobs");
            notifyPB.setMessage("Loading All Jobs");
            notifyPB.setCanceledOnTouchOutside(true);

            GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1085537073642-dq2djhhvidcgmb4c3a5ushet55jk6hf5.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            googleSignInClient= GoogleSignIn.getClient(getContext(),googleSignInOptions);

            bottom_nav = getActivity().findViewById(R.id.bottom_nav);
//            chipNavigationBar = getActivity().findViewById(R.id.bottom_nav);
//            chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);

//            rv_GroupTitle =view.findViewById(R.id.rv_UserGroupTitle);
            rv_UserPrivateGroupTitle =view.findViewById(R.id.rv_UserPrivateGroupTitle);
            rv_UserPublicGroupTitle =view.findViewById(R.id.rv_UserPublicGroupTitle);
            rv_OtherPublicGroupTitle =view.findViewById(R.id.rv_OtherPublicGroupTitle);
            rv_SubChild=view.findViewById(R.id.recyclerViewClassList);
            rv_FriendList=view.findViewById(R.id.topicNamesRecyclerView);
            rv_AllFriendList=view.findViewById(R.id.endPanelAllFriendsRecyclerView);

            rl_ChatView=view.findViewById(R.id.rl_ChatView);
            rl_FrndChatLayout=view.findViewById(R.id.rl_FrndChatLayout);

            ll_AddJoinGrp=view.findViewById(R.id.ll_AddJoinGrp);
            srl_rv_SubChild=view.findViewById(R.id.swipeUpRefreshLayoutInClass);


//            rv_ChatDashData=view.findViewById(R.id.rv_ChatDashData);
//            rv_DoubtDashData=view.findViewById(R.id.rv_DoubtDashData);

//            rl_ChatDash=view.findViewById(R.id.rl_ChatDash);


//            tv_ChatDashboard=view.findViewById(R.id.tv_ChatDashboard);
//            tv_DoubtDashboard=view.findViewById(R.id.tv_DoubtDashboard);
//            sv_DoubtSearch=view.findViewById(R.id.sv_DoubtSearch);
//            sv_ChatSearch=view.findViewById(R.id.sv_ChatSearch);
            rl_ChatDoubt=view.findViewById(R.id.rl_ChatDoubt);

            tv_FrndP_Title=view.findViewById(R.id.tv_FrndP_Title);
            tv_UserPublicTitle =view.findViewById(R.id.tv_UserPublicTitle);
            tv_UserPrivateTitle =view.findViewById(R.id.tv_UserPrivateTitle);
            tv_OtherTitle =view.findViewById(R.id.tv_OtherTitle);
            tv_GroupName =view.findViewById(R.id.groupName);
            ib_FrndP_csubmit =view.findViewById(R.id.ib_FrndP_csubmit);


            btn_AddSubChild =view.findViewById(R.id.addNewClassButton);

            btn_lteachattend =view.findViewById(R.id.btn_lteachattend);
            btn_lteachexam =view.findViewById(R.id.btn_lteachexam);
            btn_lteachresult =view.findViewById(R.id.btn_lteachresult);
            btn_AllFriends =view.findViewById(R.id.endPanelAllFriendsButton);
//            listView =view.findViewById(R.id.listView);
            ll_SubChild =view.findViewById(R.id.groupSection);
            ll_childData =view.findViewById(R.id.endPanelLinearLayout);
//            ll_TabChatDoubt =view.findViewById(R.id.ll_TabChatDoubt);
//            ll_FriendList =view.findViewById(R.id.ll_FriendList);

            overlappingPanels = view.findViewById(R.id.overlapping_panels);
            tv_cpaneltitle = view.findViewById(R.id.tv_cpaneltitle);
            tv_cpanelbody = view.findViewById(R.id.tv_cpanelbody);
            tv_UserDetails = view.findViewById(R.id.headerNameWelcome);
            tv_addTopic = view.findViewById(R.id.classNameTextView);

            ib_FrndsList=view.findViewById(R.id.ib_FrndsList);

//            btn_AddGroup =view.findViewById(R.id.btn_AddGroup);
            ib_AddGroup =view.findViewById(R.id.addNewTopicButton);
            fab_addDoubtQ =view.findViewById(R.id.fab_addDoubtQ);
//            sv_textSearchView =view.findViewById(R.id.sv_textSearchView);

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID=currentUser.getUid();

//        openStartPanelButton = view.findViewById(R.id.open_start_panel_button);

            refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" ).child(userID);
            refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refShowUserAllGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_All_Group").child(userID);
            refteachStud = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups");
//            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");
            refUserStatus = FirebaseDatabase.getInstance().getReference().child("Registration");


            //            refShowUserGroup.keepSynced(true);
//            refShowUserPrivateGroup.keepSynced(true);
//            refShowUserPublicGroup.keepSynced(true);
//            refotheruserPublicGroup.keepSynced(true);


//        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(linearLayoutManager);

//            rv_GroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_UserPublicGroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_UserPrivateGroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_OtherPublicGroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_SubChild.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_FriendList.setLayoutManager(new LinearLayoutManager(getContext()));
//            erv_GroupSearchData.setLayoutManager(new LinearLayoutManager(getContext()));

            list_GroupTitle = new ArrayList<>();
            list_UserPrivateGroupTitle = new ArrayList<>();
            list_UserPublicGroupTitle = new ArrayList<>();
//            list_ChatDashboard = new ArrayList<>();
            list_OtherUserPublicGroupTitle = new ArrayList<>();
            list_SubChild = new ArrayList<>();
//            list_DoubtDashboard = new ArrayList<>();
            list_Friend=new ArrayList<>();
            list_ChatListDashboard=new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();


            showGroupadaptor = new Adaptor_QueryGroup(getContext(), list_GroupTitle);
            showUserPrivateGroupadaptor = new Adaptor_QueryGroup(getContext(), list_UserPrivateGroupTitle);
            showUserPublicGroupadaptor = new Adaptor_QueryGroup(getContext(), list_UserPublicGroupTitle);
//            showChatDashadaptor = new Adaptor_ShowGroup(getContext(), list_ChatDashboard);
            showOtherUserPublicGroupAdaptor = new Adaptor_QueryGroup(getContext(), list_OtherUserPublicGroupTitle);
//            showSubChild_Adaptor = new Adaptor_lvl2QueryGroup(getContext(), list_SubChild);
//            showDoubtDashAdaptor = new Adaptor_ShowDoubt(getContext(), list_DoubtDashboard);
//            showDoubtDashAdaptor = new Adaptor_ShowDoubt(getContext(), list_DoubtDashboard);
            show_FriendAdaptor = new Adaptor_Friends(getContext(), list_Friend);
            showChatListDashadaptor = new Adaptor_Friends(getContext(), list_ChatListDashboard);
//            showAllGroupAdaptor = new Adaptor_SearchGroup(getContext(), listGroupSTitle);

//            rv_GroupTitle.setAdapter(showGroupadaptor);
            rv_UserPrivateGroupTitle.setAdapter(showUserPrivateGroupadaptor);
            rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupadaptor);
            rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);
//            rv_SubChild.setAdapter(showSubChild_Adaptor);
//            rv_FriendList.setAdapter(show_FriendAdaptor);
            rv_FriendList.setAdapter(showChatListDashadaptor);

//            rv_ChatDashData.setLayoutManager(new LinearLayoutManager(getContext()));
//            rv_DoubtDashData.setLayoutManager(new LinearLayoutManager(getContext()));
//            rv_ChatDashData.setAdapter(showChatDashadaptor);
//            rv_DoubtDashData.setAdapter(showDoubtDashAdaptor);

//Left Panel
//            tv_UserDetails.findViewById(tv_UserDetails);

            tv_UserDetails.setText("Welcome,"+userName);

            tv_UserDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ll_SubChild.setVisibility(View.GONE);
                    rv_SubChild.setVisibility(View.GONE);
                    tv_GroupName.setVisibility(View.GONE);
//                    list_SubChild.clear();

//                    ll_bottom_send.setVisibility(View.GONE);
//                    sv_textSearchView.setVisibility(View.GONE);
//                    rv_GroupDashData.setVisibility(View.GONE);
//                    tabLayout.setVisibility(View.GONE);
//                    tabChatItem.setVisibility(View.GONE);
//                    tabDoubtItem.setVisibility(View.GONE);

//
//                    tv_cpaneltitle.setText(R.string.center_panel_name);
//                    tv_cpanelbody.setText(R.string.swipe_gesture_instructions);
//
//                    btn_caddgroup.setVisibility(View.VISIBLE);
//                    btn_cjoingroup.setVisibility(View.VISIBLE);
//                    list_Dashboard.clear();


                    ll_childData.setVisibility(View.GONE);
                    listGrpMemberList.clear();


                }
            });
            ib_AddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    btn_show.setVisibility(View.VISIBLE);
//                    addGroupBtmDialog();
                    createGroupDialog();
                }
            });

            ib_FrndsList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_SubChild.setVisibility(View.VISIBLE);
                    tv_GroupName.setVisibility(View.VISIBLE);
                    rv_FriendList.setVisibility(View.VISIBLE);
                    btn_AddSubChild.setVisibility(View.GONE);
                    tv_GroupName.setText("Recent Chats");

                    rv_SubChild.setVisibility(View.GONE);
//                    updateFriendList();
                    chatListDashboard();
                }
            });

            srl_rv_SubChild.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    srl_rv_SubChild.setRefreshing(false);
//                    list_SubChild.clear();
//                    showSubChild_Adaptor.notifyDataSetChanged();

                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    if (Build.VERSION.SDK_INT>=26){
                    }
                    ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                }
            });
            refShowUserAllGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        notifyPB.dismiss();
                        ll_AddJoinGrp.setVisibility(View.GONE);
                        overlappingPanels.openStartPanel();

                    }else{
                        Toast.makeText(view.getContext(), "Please create Group using left swipe", Toast.LENGTH_SHORT).show();
                        ll_AddJoinGrp.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//            refShowUserPrivateGroup.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount()>0){
//                        notifyPB.show();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//            refotheruserPublicGroup.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount()>0){
//
//                        notifyPB.show();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });

//Center Panel
//            ib_cattach =view.findViewById(R.id.ib_cattach);
//            ib_csubmit =view.findViewById(R.id.ib_csubmit);
//            et_ctext =view.findViewById(R.id.et_ctext);
            btn_caddgroup =view.findViewById(R.id.btn_caddgroup);
            btn_cjoingroup =view.findViewById(R.id.btn_cjoingroup);
            ll_bottom_send =view.findViewById(R.id.ll_bottom_send);
//            rv_ChatDashData =view.findViewById(R.id.rv_ChatDashData);


            tabLayout=view.findViewById(R.id.tabl_chatdis);
            viewPager=view.findViewById(R.id.view_Pager);

            tabl_ChatView=view.findViewById(R.id.tabl_ChatView);
            view_Pager_ChatView=view.findViewById(R.id.view_Pager_ChatView);

            btn_caddgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    addGroupBtmDialog();
                    createGroupDialog();
                }
            });

            btn_cjoingroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startActivity(new Intent(getContext(), Group_Join.class));
                    Fragment fragment = null;
                    FragmentTransaction transaction;
                    fragment = new JoinGroupFragment();
                    transaction=getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container,fragment,"JoinGroup");
                    transaction.addToBackStack("JoinGroup");
                    transaction.commit();
                }

            });

//            tv_ChatDashboard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    rl_ChatDash.setVisibility(View.VISIBLE);
//                    rl_DoubtDash.setVisibility(View.GONE);
//                }
//            });
//            tv_DoubtDashboard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    rl_DoubtDash.setVisibility(View.VISIBLE);
//                    rl_ChatDash.setVisibility(View.GONE);
//                }
//            });

//
////


//        openStartPanelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                overlappingPanels.openStartPanel();
//            }
//        });
//btn_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                startActivity(new Intent(getContext(), GroupsTest.class));
//            }
//        });

//
//            addAnswer_et=view.findViewById(R.id.add_Answer_et);
//            postAnswer_tv=view.findViewById(R.id.post_Answer_tv);
//
//            postAnswer_tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (addAnswer_et.getText().toString().equals("")){
//                        addAnswer_et.setError("Enter Answer");
//                        Toast.makeText(getContext(),"Enter Answer",Toast.LENGTH_SHORT).show();
//                    } else{
//                        refTempGroupDB = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "TempCmntGroup" );
//                        refTempGroupDB.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                firebaseAuth = FirebaseAuth.getInstance();
//                                currentUser = firebaseAuth.getCurrentUser();
//                                assert currentUser != null;
//                                userID=currentUser.getUid();
//                                userEmailID= currentUser.getEmail();
//                                userPhoto=currentUser.getPhotoUrl();
//                                userName=currentUser.getDisplayName();
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//                                String groupTitle=snapshot.child(userID).getValue().toString();
//                                addComment(groupTitle);
////                            Toast.makeText(getContext(), "gp"+groupTitle, Toast.LENGTH_SHORT).show();
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
////                    addAnswer(positionQues, quesUserName,question,push,questionCategory,Answer);
//                    }
//                }
//            });

//            if (sv_textSearchView != null) {
//                sv_textSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        return false;
//                    }
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        searchText(newText);
//                        return false;
//                    }
//                });
//            }



            //END Discord


//            Adaptor_Tab_ChatDiscussion adaptorTabChatDiscussion=new Adaptor_Tab_ChatDiscussion(getChildFragmentManager());
//            adaptorTabChatDiscussion.addFragment(new ChatFragment(),"Chat");
//            adaptorTabChatDiscussion.addFragment(new Discussion_Fragment(),"Discussion");
//
//            viewPager.setAdapter(adaptorTabChatDiscussion);

            refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refteachStud = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
//            refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child("User_Subscribed_Groups");
//            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");

            //            refShowUserGroup.keepSynced(true);
            refShowUserPrivateGroup.keepSynced(true);
            refShowUserPublicGroup.keepSynced(true);
//            refotheruserPublicGroup.keepSynced(true);


//        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(linearLayoutManager);

            rv_GrpMemberList=view.findViewById(R.id.rv_GrpMemberList);
            rv_AllFriendList=view.findViewById(R.id.endPanelAllFriendsRecyclerView);
            rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_AllFriendList.setLayoutManager(new LinearLayoutManager(getContext()));

            listGrpMemberList = new ArrayList<>();

            showGrpMemberList = new Adaptor_ShowGrpMember(getContext(), listGrpMemberList);
            rv_GrpMemberList.setAdapter(showGrpMemberList);
            rv_AllFriendList.setAdapter(show_FriendAdaptor);



            btn_AllFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateFriendList();
                }
            });

//            DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
//            refSaveCurrentData.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount()>0){
////                        Class_Group class_group=new Class_Group();
//                        if(snapshot.child("GroupPushId").exists() && snapshot.child("GroupPushId").exists() && snapshot.child("subGroupName").exists() ) {
//                            String GroupPushId = snapshot.child("GroupPushId").getValue().toString().trim();
//                            String SubGroupPushId = snapshot.child("SubGroupPushId").getValue().toString().trim();
//                            String groupClassSubjects = snapshot.child("groupClassSubjects").getValue().toString().trim();
//                            String GroupName = snapshot.child("groupName").getValue().toString().trim();
//                            String subGroupName = snapshot.child("subGroupName").getValue().toString().trim();
//                            int position=1;
////                            checkChatDashboard(GroupPushId,SubGroupPushId,subGroupName);
//
//                            overlappingPanels.openStartPanel();
//                            upDateDashboard(position, GroupName, subGroupName, GroupPushId, SubGroupPushId,groupClassSubjects);
////                            upDateDashboard(int position, String groupName, String subGroupName, String SubGroupPushId,String subGroupPushId, String groupClassSubjects)
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });



            showOtherUserGroupRV();
            showUserPublicGroupRV();
            showUserPrivateGroupRV();
        }else{
            startActivity(new Intent(getContext(), getStarted.class));
            getActivity().finish();
//            showLoginBtmDialog();
        }
        return view;
    }

//    private void showGroupMembers() {
//
//
//
//
//        refGrpMemberList.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                String value=snapshot.getValue(String.class);
//                arrayList.add(value);
//                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
//                listView.setAdapter(arrayAdapter);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
////        ChildEventListener childEventListener = new ChildEventListener() {
////            @Override
////            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                String value=dataSnapshot.getValue(String.class);
////                Toast.makeText(getContext(), "value", Toast.LENGTH_SHORT).show();
//////                listGrpMemberList = new ArrayList<>();
//////                showGrpMemberList = new Adaptor_QueryGroup(getContext(), listGrpMemberList);
//////                rv_GrpMemberList.setAdapter(showGrpMemberList);
//////                notifyPB.dismiss();
//////                } else {
//////                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//////                    notifyPB.dismiss();
////////                }
////
//////                }
////
////            }
////            @Override
////            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////            }
////            @Override
////            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////            }
////            @Override
////            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////            }
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////            }
////        };
//        //refAdmin.addChildEventListener(childEventListener);
////        refShowUserPublicGroup.orderByChild("groupno").addChildEventListener(childEventListener);
//
//
//
//    }
    //    private void addComment(String groupTitle) {
//
//        refAddSubGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "Show_Group" ).child( userID ).child(groupTitle);
//        refAddSubGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                long noofQuesinCategory=snapshot.getChildrenCount()+1;
//                String push=groupTitle+"_Cmntno_"+noofQuesinCategory;
//                String Comment=addAnswer_et.getText().toString().trim();
//
////                String groupTitle=snapshot.child(userID).getValue().toString();
//                refAddSubGroup = FirebaseDatabase.getInstance().getReference().
//                        child( "Groups" ).child( "Show_Group" ).child( userID ).child(groupTitle);
//                Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID, userEmailID, push, groupTitle,Comment);
//                refAddSubGroup.child(push).setValue(userAddComment);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }
//    public void addAnswer(int position, String groupCombined) {
////        updateUser();
//        refShowAllQues = FirebaseDatabase.getInstance().getReference().child( "Feed" ).child( "Questions_List" ).child( "Feed_All_Question" );
//
//        if (FirebaseAuth.getInstance().getCurrentUser() == null)
//        {
////            loginDialogPopUp();
//        }
//        else {
//            firebaseAuth = FirebaseAuth.getInstance();
//            currentUser = firebaseAuth.getCurrentUser();
//            assert currentUser != null;
//            userID = currentUser.getUid();
//            userEmailID = currentUser.getEmail();
//            userdbName = currentUser.getDisplayName();
//
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
//                    child( "Feed" ).child( "Answer_List" ).child( "Feed_All_Answer").child(pushid);
//            reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//                    long noofQuesinCategory=snapshot.getChildrenCount()+1;
////                    String push=quesCategory+"_Qno_"+noofQuesinCategory;
//
//                    String anspush=pushid+"_Ansno_"+noofQuesinCategory;
//
//                    String Privacy="public";
//                    addAnsCategoryRef= FirebaseDatabase.getInstance().getReference().child("Feed").child("Answer_List").child("Feed_Answer_Category").child(quesCategory).child(pushid);
//                    addAllAnsRef = FirebaseDatabase.getInstance().getReference().child( "Feed" ).child( "Answer_List" ).child( "Feed_All_Answer").child(pushid);
//                    userAllAnsRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_All_Answer").child(userID).child(pushid);
//                    userAnsCategoryRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_Answer_Category").child(quesCategory).child(userID).child(pushid);
//                    userAnsPrivacyRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_Answer_Privacy").child(Privacy).child(userID).child(pushid);
//
//                    Calendar calenderCC = Calendar.getInstance();
//                    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                    final String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//
//                    String positionQ = String.valueOf(position);
//
////                    addAnswer = new Class_Answer(positionQ, Answer, dateTimeCC, userdbName, userID);
////                    addAllAnsRef.push().setValue(addAnswer);
//
//                    userAddQues = new Class_Answer(quesCategory,question,Answer,dateTimeCC,quesaskUserName,userdbName,userID,userEmailID,push,anspush);
////                    addQuesCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
////                    addQuesUsers = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
////                    addQuesUsersCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
////                    addQuesUsersPrivacy = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//
//                    addAllAnsRef.child(anspush).setValue(userAddQues);
//                    addAnsCategoryRef.child(anspush).setValue(userAddQues);
//                    userAllAnsRef.child(anspush).setValue(userAddQues);
//                    userAnsPrivacyRef.child(anspush).setValue(userAddQues);
//                    userAnsCategoryRef.child(anspush).setValue(userAddQues);
//
//
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//
//
////        SharedPreferences sp = getSharedPreferences("register", MODE_PRIVATE);
////        if (sp.contains("Name")) {
////            rName = sp.getString("Name", "");
////            rPhoneno = sp.getString("Mobile", "");
////        }
////        HashMap<String,Object> hashMap=new HashMap<>();
////        hashMap.put("Answer",addAnswer_et.getText().toString());
////        hashMap.put("Postid",postid);
////        hashMap.put("Publisherid",userId);
////        hashMap.put("Publisher",rName);
////        hashMap.put("DateTime",dateTimeCC);
////        reference.push().setValue(hashMap);
//
//
//        }
//        addAnswer_et.setText("");
//    }
//
//    private void addGroupBtmDialog() {
//
//        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
//        bottomSheetDialoglogin.setCancelable(true);
//        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_addgroup);
//        Button btn_CreateGroup=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);
//        Button btn_JoinGroup=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
//        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getContext(), Group_AddPrivPublic.class));
//                createGroupDialog();
//                bottomSheetDialoglogin.dismiss();
//
//            }
//        });
//        btn_JoinGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getContext(), Group_AddPrivPublic.class));
//
//            }
//        });
//
//
//        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        bottomSheetDialoglogin.show();
//
//    }
    //    public void showUserGroupRV() {
//
//        showGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
//            @Override
//            public void addSubGroup(int position, String groupTitle, String groupUserID) {
//                addSubChild0(position,groupTitle,groupUserID);
//            }
//
//            @Override
//            public void addSubChild1(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//                }
//
//            @Override
//            public void addSubChild2(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild3(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild4(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild5(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild6(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild7(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild8(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//
//            @Override
//            public void addSubChild9(int position, String groupName, String subgroupName) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName);
//
//            }
//        });
//                int count=0;
//
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//                    listGroupTitle.add(userQuestions);
//                    showGroupadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
////                } else {
////                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
////                    notifyPB.dismiss();
//////                }
//
////                }
//
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };
//        //refAdmin.addChildEventListener(childEventListener);
//        refSearchShowGroup.addChildEventListener(childEventListener);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//
////
////    }
//
//    }
    //    public void showDashBoardRV(int position, String groupCombined, String groupName,String subgroupName, String groupPushId) {
//
//        refGroupChildUserDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "Show_Group" ).child("User_Show_Group").child( userID ).child(groupCombined);
//        refGrpChildAllDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "Show_Group" ).child("Show_All_Group").child(groupCombined);
//
//        ll_bottom_send.setVisibility(View.VISIBLE);
//        sv_textSearchView.setVisibility(View.VISIBLE);
//
//        switch(position){
//            case 0 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//
//                    break;
//            case 1 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//                break;
//            case 2 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//
//                break;
//            case 3 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//                break;
//
//            case 4 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//                break;
//            case 5 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//
//                break;
//            case 6 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//
//                break;
//            case 7 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//
//                break;
//            case 8 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//                break;
//            case 9 :
//                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                upDateDashboard(position,groupCombined,subgroupName,groupName,groupPushId);
//                break;
//        }
//
////
////        showQuesadaptor.setOnItemClickListener(new Adaptor_QueryQuestions.OnItemClickListener() {
////            @Override
////            public void shareQues(int position,String Title) {
//////                generateLink(Title);
////                Toast.makeText(getContext(), "share", Toast.LENGTH_SHORT).show();
////            }
////        });
////        FragmentTransaction ft = getFragmentManager().beginTransaction();
////        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//
////        Bundle intent=getIntent().getExtras();
////        if (intent!=null){
////            String publisher=intent.getString("publisherid");
////            SharedPreferences.Editor editor=getSharedPreferences("PUBLISH",MODE_PRIVATE).edit();
////            editor.putString("publisherid",publisher);
////            editor.apply();
////
////        }
//
//    }
//
//    private void updateFriendList(){
////        list_SubChild.clear();
//        refFriendList = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID);
////
////        refFriendList.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                if (snapshot.getChildrenCount() > 0) {
////                    ll_FriendList.setVisibility(View.VISIBLE);
////                    rv_FriendList.setVisibility(View.VISIBLE);
////                } else {
////                    Toast.makeText(getContext(), "Please Add Friend", Toast.LENGTH_LONG).show();
////                    rv_FriendList.setVisibility(View.GONE);
////                    ll_FriendList.setVisibility(View.GONE);
////                    ll_SubChild.setVisibility(View.GONE);
////                    rv_SubChild.setVisibility(View.GONE);
////                    tv_GroupName.setVisibility(View.GONE);
////                }
////            }
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
//
//
//
////        showSubChild_Adaptor.setOnItemClickListener(new Adaptor_ChildGroup.OnItemClickListener() {
////            @Override
////            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID) {
////                overlappingPanels.closePanels();
////                upDateDashboard(position, groupName, subGroupName, groupPushId, subGroupPushID);
////            }
////
////
////        });
//        show_FriendAdaptor.setOnItemClickListener(new Adaptor_Friends.OnItemClickListener() {
//            @Override
//            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID,String frndUserId) {
////                btn_caddgroup.setVisibility(View.GONE);
////                btn_cjoingroup.setVisibility(View.GONE);
////                overlappingPanels.closePanels();
//                rl_ChatView.setVisibility(View.VISIBLE);
//                DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child("Temp").child(userID);
//                refSaveCurrentData.child("TempChatUserId").setValue(frndUserId);
//                refSaveCurrentData.child("TempChatUserName").setValue(subGroupName);
//            }
//        });
//        list_Friend.clear();
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                if (dataSnapshot.child(userID).exists()){
////                    String check=dataSnapshot.child(userID).child("grpJoiningStatus").getValue().toString();
////                    if(check.equals("true")) {
//                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//                        list_Friend.add(userQuestions);
//                        show_FriendAdaptor.notifyDataSetChanged();
//                        notifyPB.dismiss();
////                    }
////                }
//
////                notifyPB.dismiss();
////                } else {
////                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
////                    notifyPB.dismiss();
//////                }
//
////                }
//
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };
//        //refAdmin.addChildEventListener(childEventListener);
//        refFriendList.addChildEventListener(childEventListener);
//    }
//    private void addChildGroup(int position, String groupName,String groupPushId, String groupUserID, String category) {
//
//        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
//        dialogBuilder.setCanceledOnTouchOutside(true);
//        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //dialogBuilder.setCancelable(false);
//        LayoutInflater inflater = this.getLayoutInflater();
//
//        View dialogView = inflater.inflate(R.layout.dialog_creategroup, null);
//        firebaseAuth = FirebaseAuth.getInstance();
//        currentUser = firebaseAuth.getCurrentUser();
//        assert currentUser != null;
//        userID=currentUser.getUid();
//        userEmailID= currentUser.getEmail();
//        userPhoto=currentUser.getPhotoUrl();
//        userName=currentUser.getDisplayName();
//
//        Button btn_CreateGroup=dialogView.findViewById(R.id.btn_CreateGroup);
//        LinearLayout ll_groupFamFrnds=dialogView.findViewById(R.id.ll_groupFamFrnds);
//        LinearLayout ll_creategroup=dialogView.findViewById(R.id.ll_creategroup);
//        EditText et_GroupName=dialogView.findViewById(R.id.et_GroupName);
//
////        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
////        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//        ll_creategroup.setVisibility(View.VISIBLE);
//        ll_groupFamFrnds.setVisibility(View.GONE);
//
//        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String subChildGroupName = et_GroupName.getText().toString().trim();
//                if (subChildGroupName.isEmpty()) {
//                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
//                    et_GroupName.setError("Enter Group Name");
//                } else {
//                    String groupSubGroup=groupName+"_"+subChildGroupName;
//                    refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
//                    refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(groupSubGroup).child("SubGroup_SubsList");
//
//                    refChildGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);
//                                String position=Integer.toString(noofQuesinCategory);
//                                Class_Group subGroup_Class = new Class_Group(dateTimeCC,subChildGroupName,userID,groupSubGroup,position,groupName,groupPushId);
//                                Class_Group subGroupSubsList_Class = new Class_Group(dateTimeCC,userName,userID,userID,"Admin",subChildGroupName,groupSubGroup,groupName,groupPushId);
//                                refChildGroup.child(groupSubGroup).setValue(subGroup_Class);
//                                refChildGroupSubsList.child(userID).setValue(subGroupSubsList_Class);
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                }
//                Toast.makeText(getContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
//                dialogBuilder.dismiss();
//
//            }
//
//        });
//
//
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//
////        FragmentTransaction ft = getFragmentManager().beginTransaction();
////        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
////        dialogBuilder.dismiss();
//
//    }

    public void searchText(String newText) {
    ArrayList<Class_Group> listSearchQues=new ArrayList<>();
    for (Class_Group classUserSearch: list_GroupTitle){
        if (classUserSearch.getGroupName().toLowerCase().contains(newText.toLowerCase())){
            listSearchQues.add(classUserSearch);
        }
    }
    Adaptor_QueryGroup adapSearchJob= new Adaptor_QueryGroup(getContext(),listSearchQues);
//        rv_GroupDashData.setAdapter(adapSearchJob);
}
    private void showLoginBtmDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_login);

//        Button btn_phonelogin=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
//        Button btn_Cancel=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);
        Button btn_googlelogin=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);


//        btn_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialoglogin.dismiss();
//            }
//        });
        btn_googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1085537073642-dq2djhhvidcgmb4c3a5ushet55jk6hf5.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                googleSignInClient= GoogleSignIn.getClient(getContext(),googleSignInOptions);
                notifyPB = new ProgressDialog(getContext());
                notifyPB.setTitle("Govt Jobs");
                notifyPB.setMessage("Loading All Jobs");
                notifyPB.setCanceledOnTouchOutside(true);
                notifyPB.show();


                Intent intent=googleSignInClient.getSignInIntent();

                startActivityForResult(intent,100);
                bottomSheetDialoglogin.dismiss();

            }
        });
//
//        btn_phonelogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Phone_Login.class));
//                bottomSheetDialoglogin.dismiss();
//            }
//        });

        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialoglogin.show();

    }
    private void showStudTeachBtmDialog() {
//        rl_feed.setBackgroundColor(Color.GRAY);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getContext());
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);

        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());


        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    if (!snapshot.child(userID).exists()){
                        student_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                                refUserRegister.child( "Name" ).setValue( userName );
                                refUserRegister.child( "Email" ).setValue( userEmailID );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                                refUserRegister.child( "UserId" ).setValue( userID );
                                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                                refUserRegister.child( "Category" ).setValue("Student");
                                refUserRegister.child( "userStatus" ).setValue("Online");
                                Toast.makeText(getContext(), "Login Successful as Student", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                            }
                        });
                        teacher_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                                refUserRegister.child( "Name" ).setValue( userName );
                                refUserRegister.child( "Email" ).setValue( userEmailID );
                                refUserRegister.child( "UserId" ).setValue( userID );
                                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                                refUserRegister.child( "Category" ).setValue("Teacher");
                                refUserRegister.child( "userStatus" ).setValue("Online");
                                Toast.makeText(getContext(), "Login Successful as Teacher", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                            }
                        });
                    }

                    notifyPB.dismiss();
                }else{
                    Toast.makeText(getContext(), "Please create Group using left swipe", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        bottomSheetDialog.show();
    }
    private void showAddDoubtBtmDialog(String groupPushId,String subGroupPushId,String groupClassSubject) {
//        rl_feed.setBackgroundColor(Color.GRAY);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getContext());
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(R.layout.btmdialog_adddoubt);

        Button btn_Cancel=bottomSheetDialog.findViewById(R.id.btn_Cancel);
        Button btn_Submit=bottomSheetDialog.findViewById(R.id.btn_Submit);
        EditText et_AddTopic=bottomSheetDialog.findViewById(R.id.et_AddTopic);
        EditText et_AddDoubt=bottomSheetDialog.findViewById(R.id.et_AddDoubt);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addTopic = et_AddTopic.getText().toString().trim();
                String addDoubt = et_AddDoubt.getText().toString().trim();
                if (addTopic.isEmpty() && addDoubt.isEmpty() ) {
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference allDoubtReference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubject).child("All_Doubt");
                    allDoubtReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "Doubtno_" + noofGroupinCategory + "_" +addTopic;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId,subGroupPushId,groupClassSubject,addTopic,addDoubt);
                            allDoubtReference.child(push).setValue(userAddGroupClass);
                            Toast.makeText(getContext(),"Doubt Successfully Added",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubject).child("Topic");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "Doubtno_" + noofGroupinCategory + "_" +addTopic;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId,subGroupPushId,groupClassSubject, addTopic,addDoubt);
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

    private void addSubPublicChild0(int position,String groupTitle, String groupUserID) {

        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_creategroup, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        Button btn_CreateGroup=dialogView.findViewById(R.id.btn_CreateGroup);
        LinearLayout ll_groupFamFrnds=dialogView.findViewById(R.id.ll_groupFamFrnds);
        LinearLayout ll_creategroup=dialogView.findViewById(R.id.ll_creategroup);
        EditText et_GroupName=dialogView.findViewById(R.id.et_GroupName);


//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        ll_creategroup.setVisibility(View.VISIBLE);

        ll_groupFamFrnds.setVisibility(View.GONE);


        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subChildGroupName=et_GroupName.getText().toString().trim();
                if (subChildGroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "User_Public_Group" ).child(userID).child(groupTitle);
//                    DatabaseReference referenceJoinOther= FirebaseDatabase.getInstance().getReference().
//                            child("Groups").child("All_Universal_Group").child(groupTitle);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                            int noofQuesinCategory= (int) (snapshot.getChildrenCount()+1);

//                            if (noofQuesinCategory==8){
//                                reference.child( "group1" ).setValue( subChildGroupName );
//                            }else{
//                                Toast.makeText(getContext(), "Wrong data", Toast.LENGTH_SHORT).show();
//                            }

                            Class_Group userQuestions = snapshot.getValue(Class_Group.class);

                            String groupAdmin=userQuestions.getUserId();
                            String groupName=userQuestions.getGroupName();
                            String groupCategory=userQuestions.getGroupCategory();

                            switch(noofQuesinCategory){
                                case 8:
                                    reference.child( "group1" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group1",subChildGroupName);
                                    break;
                                case 9:
                                    reference.child( "group2" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group2",subChildGroupName);
                                    break;
                                case 10:
                                    reference.child( "group3" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group3",subChildGroupName);
                                    break;
                                case 11:
                                    reference.child( "group4" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup2" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup2" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group4",subChildGroupName);
                                    break;
                                case 12:
                                    reference.child( "group5" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup3" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup3" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group5",subChildGroupName);
                                    break;
                                case 13:
                                    reference.child( "group6" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup4" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup4" ).setValue( subChildGroupName );
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group6",subChildGroupName);
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 14:
                                    reference.child( "group7" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup5" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup5" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group7",subChildGroupName);
                                    break;
                                case 15:
                                    reference.child( "group8" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup6" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup6" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group8",subChildGroupName);
                                    break;
                                case 16:
                                    reference.child( "group9" ).setValue( subChildGroupName );
//                                    referenceAllGroup.child( "SubGroup7" ).setValue( subChildGroupName );
//                                    referenceJoinOther.child( "SubGroup7" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group9",subChildGroupName);
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                            }
                            showGroupadaptor.notifyDataSetChanged();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                            dialogBuilder.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();



    }
    private void addSubPrivateChild0(int position,String groupTitle, String groupUserID) {

        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_creategroup, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        Button btn_CreateGroup=dialogView.findViewById(R.id.btn_CreateGroup);
        LinearLayout ll_groupFamFrnds=dialogView.findViewById(R.id.ll_groupFamFrnds);
        LinearLayout ll_creategroup=dialogView.findViewById(R.id.ll_creategroup);
        EditText et_GroupName=dialogView.findViewById(R.id.et_GroupName);


//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        ll_creategroup.setVisibility(View.VISIBLE);

        ll_groupFamFrnds.setVisibility(View.GONE);

        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subChildGroupName=et_GroupName.getText().toString().trim();
                if (subChildGroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "User_Private_Group" ).child(userID).child(groupTitle);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                            int noofQuesinCategory= (int) (snapshot.getChildrenCount()+1);

//                            if (noofQuesinCategory==8){
//                                reference.child( "group1" ).setValue( subChildGroupName );
//                            }else{
//                                Toast.makeText(getContext(), "Wrong data", Toast.LENGTH_SHORT).show();
//                            }

                            switch(noofQuesinCategory){
                                case 9:
                                    reference.child( "group1" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();

                                    break;
                                case 10:
                                    reference.child( "group2" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 11:
                                    reference.child( "group3" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 12:
                                    reference.child( "group4" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 13:
                                    reference.child( "group5" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 14:
                                    reference.child( "group6" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 15:
                                    reference.child( "group7" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 16:
                                    reference.child( "group8" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case 17:
                                    reference.child( "group9" ).setValue( subChildGroupName );
                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                            }
                            showGroupadaptor.notifyDataSetChanged();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                            dialogBuilder.dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                }

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();



    }

    private void createGroupDialog() {
        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_creategroup, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        Button btn_Public=dialogView.findViewById(R.id.btn_Public);
        Button btn_Private=dialogView.findViewById(R.id.btn_Private);
        Button btn_CreateGroup=dialogView.findViewById(R.id.btn_CreateGroup);
        LinearLayout ll_groupFamFrnds=dialogView.findViewById(R.id.ll_groupFamFrnds);
        LinearLayout ll_creategroup=dialogView.findViewById(R.id.ll_creategroup);
        EditText et_GroupName=dialogView.findViewById(R.id.et_GroupName);

        ll_groupFamFrnds.setVisibility(View.VISIBLE);

//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);


        btn_Public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupCategory ="Public";
                ll_groupFamFrnds.setVisibility(View.GONE);
                ll_creategroup.setVisibility(View.VISIBLE);
            }
        });
        btn_Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_groupFamFrnds.setVisibility(View.GONE);
                ll_creategroup.setVisibility(View.VISIBLE);
                GroupCategory ="Private";
            }
        });
        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupName=et_GroupName.getText().toString().trim();
                if (GroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {
//                    DatabaseReference referenceALLGroup= FirebaseDatabase.getInstance().getReference().
//                            child( "Groups" ).child( "User_All_Group" ).child(userID);
//                    referenceALLGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            long noofGroupinCategory=snapshot.getChildrenCount()+1;
//                            String push="Group_UAllG_No_"+noofGroupinCategory+"_"+GroupName;
//                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
//                            refSubsGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups").child(push);
//                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
//                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
//
//                            if (GroupCategory.equals("Private")) {
//                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
//                                refuserAllGroup.setValue(userAddGroup);
//                                refSubsGroup.child(userID).setValue(true);
//                            } else if (GroupCategory.equals("Public")) {
//                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
//                                refuserAllGroup.setValue(userAddGroup);
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//
//                    if (GroupCategory.equals("Private")) {
//
//                        DatabaseReference referencePVTGroup = FirebaseDatabase.getInstance().getReference().
//                                child("Groups").child("User_Private_Group").child(userID);
//                        referencePVTGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//                                long noofGroupinCategory = snapshot.getChildrenCount() + 1;
////                            String position=getString((int) noofQuesinCategory);
//                                String push = "Group_UPriG_No_" + noofGroupinCategory + "_" + GroupName;
//                                Calendar calenderCC = Calendar.getInstance();
//                                SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                                String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//
//                                refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
//                                refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
//
////                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
////                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
////                            refAllPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_Group").child(push);
//
////                            if (GroupCategory.equals("Private")) {
//                                userAddGroup = new Class_Group(dateTimeCC, userName, userID, userEmailID, push, GroupName, GroupCategory, noofGroupinCategory);
//                                refuserPersonalGroup.setValue(userAddGroup);
////                                refuserAllGroup.setValue(userAddGroup);
////                            } else if (GroupCategory.equals("Public")) {
////                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
////                                refuserPublicGroup.setValue(userAddGroup);
////                                refAllPublicGroup.setValue(userAddGroup);
////                                refuserAllGroup.setValue(userAddGroup);
////                            }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//
//                    }
//                    else if(GroupCategory.equals("Public")) {
//
//                        DatabaseReference referencePBLCGroup = FirebaseDatabase.getInstance().getReference().
//                                child("Groups").child("User_Public_Group").child(userID);
//                        referencePBLCGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                                String push = "Group_UPubG_No_" + noofGroupinCategory + "_" + GroupName;
//
//                                refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
//
//                                userAddGroup = new Class_Group(dateTimeCC, userName, userID, userEmailID, push, GroupName, GroupCategory, noofGroupinCategory);
//                                refuserPublicGroup.setValue(userAddGroup);
//
////                            }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }


                    DatabaseReference referenceALLPBLCGroup= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "All_Universal_Group" );

                    referenceALLPBLCGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory=snapshot.getChildrenCount()+1;
                            String push="Uni_Group_No_"+noofGroupinCategory+"_"+GroupName;

                            refAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push);
                            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(push).child("User_Subscribed_Groups").child(userID);

                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);

                            userAddGroupClass = new Class_Group(dateTimeCC,userName, userID, push, GroupName,GroupCategory,noofGroupinCategory);
                            userAddGroupClass = new Class_Group(dateTimeCC,userName, userID, push, GroupName,GroupCategory,noofGroupinCategory);
                            userSubsGroupClass = new Class_Group(dateTimeCC,userName, userID, userID, GroupName,push,"Admin","Online");

                            refAllGroup.setValue(userAddGroupClass);
                            refGroupSubsList.setValue(userSubsGroupClass);
                            refuserAllGroup.setValue(userAddGroupClass);

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

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();

                    Toast.makeText(getContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
                    dialogBuilder.dismiss();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showUserPrivateGroupRV() {

        showUserPrivateGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {


            @Override
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
//                addChildGroup(position,groupName,groupPushId,groupUserID,"public");
//                addSubChild0(position, groupName,groupPushId, groupUserID,Category);
            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID,String groupCategory) {

//                showchildGroupRV(position,groupName,groupPushId,groupUserID,groupCategory);
            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {

            }


//
//            @Override
//            public void addSubChild1(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild2(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild3(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild4(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild5(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild6(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild7(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild8(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild9(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
        });
        int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                list_UserPrivateGroupTitle.add(userQuestions);
                showUserPrivateGroupadaptor.notifyDataSetChanged();
                notifyPB.dismiss();
//                } else {
//                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
////                }

//                }

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
        list_UserPrivateGroupTitle.clear();
        refShowUserPrivateGroup.orderByChild("groupno").addChildEventListener(childEventListener);

        refShowUserPrivateGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    tv_UserPrivateTitle.setText("Private Server");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public void showUserPublicGroupRV() {

        showUserPublicGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
            @Override
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
//                addChildGroup(position,groupName,groupPushId,groupUserID,"public");
//                addSubChild0( position, groupName,groupPushId, groupUserID,Category);
            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID,String groupCategory) {
//                showchildGroupRV(position,groupName,groupPushId,groupUserID,groupCategory);

            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {

            }


        });
                int count=0;

//                list_UserPublicGroupTitle.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                    list_UserPublicGroupTitle.add(userQuestions);
                    showUserPublicGroupadaptor.notifyDataSetChanged();
//                showUserPublicGroupadaptor.notifyDataSetChanged();

                notifyPB.dismiss();
//                } else {
//                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
////                }

//                }

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
                if (snapshot.getChildrenCount()>0){
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
    public void showOtherUserGroupRV() {

        showOtherUserPublicGroupAdaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
            @Override
            public void addChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {

            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID, String groupCategory) {
//                showchildGroupRV(position,groupName,groupPushId,groupUserID,"Others_Group");
            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {
//                showchildGroupRV(position,groupName,groupPushId,groupUserID,"Others_Group");
            }
//
//            @Override
//   0]pi          public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
// yi
//            }

//            @Override
//            public void addSubGroup(int position, String groupTitle, String groupUserID) {
//                addSubPrivateChild0(position,groupTitle,groupUserID);
//            }
//
//            @Override
//            public void addSubChild1(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild2(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild3(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild4(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild5(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName, groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild6(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild7(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild8(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
//
//            @Override
//            public void addSubChild9(int position, String groupName, String subgroupName, String groupPushId) {
//                overlappingPanels.closePanels();
//                showDashBoardRV(position,groupName+"_"+subgroupName,groupName,subgroupName,  groupPushId);
//
//            }
        });
        int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("User_Subscribed_Groups").child(userID).exists()){
                    String check=dataSnapshot.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
                    if(check.equals("true")) {
                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
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

//        refotheruserPublicGroup.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//                    if (snapshot.child("User_Subscribed_Groups").child(userID).exists()){
//                        String check=snapshot.child("User_Subscribed_Groups").child(userID).child("subsStatus").getValue().toString();
//                        if(check.equals("true")) {
//                            tv_OtherTitle.setText("Other Server");
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
    }
    private void chatListDashboard() {

        refFriendList = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID);

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
//                    String chatStatus=dataSnapshot.child("Chat_Message").getValue().toString();
                    if (dataSnapshot.child("Chat_Message").hasChildren()){
                        Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                        list_ChatListDashboard.add(class_userDashBoard);
                    }
                    showChatListDashadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
                } else {
                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
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
        list_ChatListDashboard.clear();
    }

//    private void showchildGroupRV(int position, String groupName, String groupPushId, String groupUserID, String Category) {
//
////        Toast.makeText(getContext(),groupPushId,Toast.LENGTH_SHORT).show();
////
////        if (Category.equals("Public") || Category.equals("Private")){
////            btn_AddSubChild.setVisibility(View.VISIBLE);
////        }else{
////            btn_AddSubChild.setVisibility(View.GONE);
////        }
//        btn_AddSubChild.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                addChildGroup( position, groupName,groupPushId, groupUserID,Category );
//                addSubChild0( position, groupName,groupPushId, groupUserID,Category,"newClass");
//
//            }
//        });
//
//        ll_SubChild.setVisibility(View.VISIBLE);
//        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
//        refChildGroup.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() > 0) {
//                    rv_SubChild.setVisibility(View.VISIBLE);
//                    tv_GroupName.setVisibility(View.VISIBLE);
//                    tv_addTopic.setVisibility(View.VISIBLE);
//
//                } else {
//                    Toast.makeText(getContext(), "Please Create Sub Group using (+) Sign", Toast.LENGTH_LONG).show();
//                    rv_SubChild.setVisibility(View.GONE);
//                    tv_GroupName.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
//
//        showSubChild_Adaptor.setOnItemClickListener(new Adaptor_lvl2QueryGroup.OnItemClickListener() {
//            @Override
//            public void addSubGroup(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID) {
//                addSubChild0( position, groupName,groupPushId, groupUserID,Category,groupClassName);
//
//            }
//
//            @Override
//            public void viewChildGroup1(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                overlappingPanels.closePanels();
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//            }
//
//            @Override
//            public void viewChildGroup2(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//            }
//            @Override
//            public void viewChildGroup3(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//            }
//            @Override
//            public void viewChildGroup4(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//            }
//            @Override
//            public void viewChildGroup5(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//            }
//            @Override
//            public void viewChildGroup6(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//            }
//
//            @Override
//            public void viewChildGroup7(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//
//            }
//
//            @Override
//            public void viewChildGroup8(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//
//            }
//
//            @Override
//            public void viewChildGroup9(int position, String groupName, String groupPushId, String groupClassName, String groupCreatorName, String groupUserID, String groupClassSubjects) {
//                upDateDashboard(position, groupName, groupClassName, groupPushId, groupClassName,groupClassSubjects);
//                refSaveCurrentData.child("GroupPushId").setValue(groupPushId);
//                refSaveCurrentData.child("SubGroupPushId").setValue(groupClassName);
//                refSaveCurrentData.child("subGroupName").setValue(groupClassName);
//                refSaveCurrentData.child("groupName").setValue(groupName);
//                refSaveCurrentData.child("groupClassSubjects").setValue(groupClassSubjects);
//
//                overlappingPanels.closePanels();
//
//            }
//
////            @Override
////            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID) {
////                overlappingPanels.closePanels();
////                upDateDashboard(position, groupName, subGroupName, groupPushId, subGroupPushID);
////            }
//
//
//        });
//
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//                list_SubChild.add(userQuestions);
//                showSubChild_Adaptor.notifyDataSetChanged();
//                tv_GroupName.setText(userQuestions.getGroupName());
//
////                notifyPB.dismiss();
////                } else {
////                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
////                    notifyPB.dismiss();
//////                }
//
////                }
//
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                showSubChild_Adaptor.notifyDataSetChanged();
//
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };
//        //refAdmin.addChildEventListener(childEventListener);
//        list_SubChild.clear();
//        refChildGroup.orderByChild("position").addChildEventListener(childEventListener);
//    }

    private void upDateDashboard(int position, String groupName, String subGroupName, String groupPushId,String subGroupPushId, String groupClassSubjects) {

//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(groupName, groupName);
//        editor.putString(subGroupName, subGroupName);
//        editor.putString(groupPushId, groupPushId);
//        editor.putString(subGroupPushId, subGroupPushId);
//        editor.putString(groupClassSubjects, groupClassSubjects);
//        editor.apply();



        rv_FriendList.setVisibility(View.GONE);
        DatabaseReference refUserCategory = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();
                    if(snapshot.child("Category").exists()) {
                        String getTeach = snapshot.child("Category").getValue().toString().trim();
                        if (getTeach.equals("School")) {
                            btn_lteachattend.setVisibility(View.VISIBLE);
                            btn_lteachexam.setVisibility(View.VISIBLE);
                            btn_lteachresult.setVisibility(View.VISIBLE);
                        }
                    }
//                        else{
////                            Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
//                        }
                }
//                    else {
//                        Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
//                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        rl_ChatDoubt.setVisibility(View.VISIBLE);
        rl_FrndChatLayout.setVisibility(View.GONE);
//        ll_TabChatDoubt.setVisibility(View.VISIBLE);
        btn_caddgroup.setVisibility(View.GONE);
        btn_cjoingroup.setVisibility(View.GONE);
//        rl_ChatDash.setVisibility(View.VISIBLE);
//        ll_bottom_send.setVisibility(View.VISIBLE);
        ll_childData.setVisibility(View.VISIBLE);
//
//        tabLayout.setVisibility(View.VISIBLE);
//        tabChatItem.setVisibility(View.VISIBLE);
//        tabDoubtItem.setVisibility(View.VISIBLE);
//
//        tv_cpanelbody.setVisibility(View.VISIBLE);
//        tv_cpaneltitle.setVisibility(View.VISIBLE);
//
//        refTempGroupDB = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child("Temp").child( userID );
//        refGroupChildUserDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId);
//        refGroupChildUserDashboard.keepSynced(true);
//        refGrpChildAllDashboard.keepSynced(true);
//
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();
//        refTempGroupDB.setValue(subGroupPushId);

        tv_cpaneltitle.setVisibility(View.VISIBLE);
        tv_cpanelbody.setVisibility(View.VISIBLE);

        tv_cpaneltitle.setText(groupName);
        tv_cpanelbody.setText(subGroupName);

//        refGrpMemberList.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//
//                    notifyPB.show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        btn_lteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new AttendanceFragment();
                Bundle bundle=new Bundle();
                bundle.putString("groupPushId", groupPushId);
                bundle.putString("subGroupPushId", subGroupPushId);
                bundle.putString("classPushId", groupClassSubjects);
                fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


//                Intent intent=new Intent(getContext(), Attendance_Activity.class);
//                intent.putExtra("groupPushId", groupPushId);
//                intent.putExtra("subGroupPushId", subGroupPushId);
//                startActivity(intent);

//Inflate the fragment
//                getFragmentManager().beginTransaction().add(R.id.fragment_container, attendance).commit();
            }
        });


//        Fragment fragment=new tasks();

//        Bundle bundle =new Bundle();
//        bundle.putString("groupName",groupName);
//        bundle.putString("subGroupName",subGroupName);
//        bundle.putString("groupPushId",groupPushId);
//        bundle.putString("subGroupPushId",subGroupPushId);
//
//        Fragment fragment=new ChatTab();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container,fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

//        showGroupMembers();
//        list_Dashboard.clear();
//        list_ChatDashboard=new ArrayList<>();


//
//        ib_csubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String subGroupMsg = et_ctext.getText().toString().trim();
//                if (subGroupMsg.isEmpty()) {
//                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
//                    et_ctext.setError("Enter text");
//                } else {
//
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
//                            child("Groups").child("All_Sub_Group").child(groupPushId).child(subGroupPushId).child("Chat_Message");
//                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                            String push = "mszno_" + noofGroupinCategory + "_" +subGroupName;
//
//                            Calendar calenderCC = Calendar.getInstance();
//                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId,subGroupPushId,subGroupMsg);
//                            reference.child(push).setValue(userAddGroupClass);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//
//                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference().
//                            child("Groups").child("Show_Group").child("User_Show_Group").child(userID).child(subGroupPushId);
//                    referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                            String push = "mszno_" + noofGroupinCategory + "_" + subGroupPushId;
//
//                            Calendar calenderCC = Calendar.getInstance();
//                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//                            userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, subGroupPushId,groupPushId, noofGroupinCategory);
//                            referenceUser.child(push).setValue(userAddGroupClass);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//
//                }
//            }
//        });
//
//        fab_addDoubtQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAddDoubtBtmDialog(groupPushId,subGroupPushId,groupClassSubjects);
//            }
//        });


        DatabaseReference refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//        refUserStatus.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
////                        Class_Group class_group=new Class_Group();
//                    String getTeach=snapshot.child("Category").getValue().toString().trim();
//                    if (getTeach.equals("Teacher")){
//                        btn_lteachattend.setVisibility(View.VISIBLE);
//                        btn_lteachexam.setVisibility(View.VISIBLE);
//                        btn_lteachresult.setVisibility(View.VISIBLE);
//                    }
////                        else{
//////                            Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
////                        }
//                }
////                    else {
////                        Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
////                    }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember.OnItemClickListener() {
            @Override
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID,adminUserName, "AddFrnd");
            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentInvitation(adminGroupID,adminUserName, "FollowFrnd");
            }

            @Override
            public void MemberProfile(String memberUserId,String memberUserName) {
                showBtmDialogUserProfile(memberUserId,memberUserName);
            }

        });


        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" ).child(groupPushId).child("User_Subscribed_Groups");

        refGrpMemberList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);fatten
////                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
////                listView.setAdapter(arrayAdapter);
                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
                listGrpMemberList.add(class_userDashBoard);
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

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition=viewHolder.getAdapterPosition();
                int toPosition=target.getAdapterPosition();

                Toast.makeText(getContext(),"From"+fromPosition+"gggto"+toPosition,Toast.LENGTH_SHORT).show();
                Collections.swap(listGrpMemberList,fromPosition,toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };


        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);

    }

    private void sentInvitation(String adminGroupID,String adminUserName, String Code) {
        String req = null,notifyStatus=null;
        if (Code.equals("AddFrnd")){
            req="Friend";
            notifyStatus="Friend_Request";
        }else if (Code.equals("FollowFrnd")){
            req="Follow";
            notifyStatus="Follow_Request";
        }
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getContext());
        String finalNotifyStatus = notifyStatus;
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Do you want to send "+req+" request to"+adminUserName+"?")
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
                                DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("Received_Req").child(adminGroupID);
                                DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("Submit_Req").child(userID);

                                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                                        String push="Joining Reqno_"+noofQuesinCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                        Class_Group  userAdd= new Class_Group(dateTimeCC, userName, "req_sent",userID,adminGroupID, userEmail, push, "groupName","groupPushId",finalNotifyStatus);
                                        refjoiningReq.child(push).setValue(userAdd);
                                        refacceptingReq.child(push).setValue(userAdd);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                //
//                                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
//                                NOTIFICATION_TITLE = edtTitle.getText().toString();
//                                NOTIFICATION_MESSAGE = edtMessage.getText().toString();
//
//                                JSONObject notification = new JSONObject();
//                                JSONObject notifcationBody = new JSONObject();
//                                try {
//                                    notifcationBody.put("title", NOTIFICATION_TITLE);
//                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
//
//                                    notification.put("to", TOPIC);
//                                    notification.put("data", notifcationBody);
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "onCreate: " + e.getMessage() );
//                                }
//                                sendPushNotification(notification);
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


    private void updateFriendList(){
//        list_SubChild.clear();
        refFriendList = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID);
//
//        refFriendList.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() > 0) {
//                    ll_FriendList.setVisibility(View.VISIBLE);
//                    rv_FriendList.setVisibility(View.VISIBLE);
//                } else {
//                    Toast.makeText(getContext(), "Please Add Friend", Toast.LENGTH_LONG).show();
//                    rv_FriendList.setVisibility(View.GONE);
//                    ll_FriendList.setVisibility(View.GONE);
//                    ll_SubChild.setVisibility(View.GONE);
//                    rv_SubChild.setVisibility(View.GONE);
//                    tv_GroupName.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });



//        showSubChild_Adaptor.setOnItemClickListener(new Adaptor_ChildGroup.OnItemClickListener() {
//            @Override
//            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID) {
//                overlappingPanels.closePanels();
//                upDateDashboard(position, groupName, subGroupName, groupPushId, subGroupPushID);
//            }
//
//
//        });
        show_FriendAdaptor.setOnItemClickListener(new Adaptor_Friends.OnItemClickListener() {
            @Override
            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID,String frndUserId) {
//                btn_caddgroup.setVisibility(View.GONE);
//                btn_cjoingroup.setVisibility(View.GONE);
//                overlappingPanels.closePanels();
//                rl_ChatView.setVisibility(View.VISIBLE);
//                DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child("Temp").child(userID);
//                refSaveCurrentData.child("TempChatUserId").setValue(frndUserId);
//                refSaveCurrentData.child("TempChatUserName").setValue(subGroupName);

//                tv_FrndP_Title.setText(subGroupName);
                checkChatDashboard(frndUserId);
            }
        });
        list_Friend.clear();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (dataSnapshot.child(userID).exists()){
//                    String check=dataSnapshot.child(userID).child("grpJoiningStatus").getValue().toString();
//                    if(check.equals("true")) {
                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                list_Friend.add(userQuestions);
                show_FriendAdaptor.notifyDataSetChanged();
//                notifyPB.dismiss();
//                    }
//                }

//                notifyPB.dismiss();
//                } else {
//                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
////                }

//                }

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
    }
    private void showBtmDialogUserProfile(String memberUserId, String memberUserName) {

        BottomSheetDialog btmSheetUserProfile=new BottomSheetDialog(getContext());
        btmSheetUserProfile.setCancelable(true);
        btmSheetUserProfile.setContentView(R.layout.btmdialog_profileothers);
        DatabaseReference refUserStatus,refUserFollowers,refUserFollowing;


        Button btn_SentFrndReq=btmSheetUserProfile.findViewById(R.id.btn_SentFrndReq);
        Button btn_FollowFrnd=btmSheetUserProfile.findViewById(R.id.btn_FollowFrnd);

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

        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(memberUserId);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    if (snapshot.child("Bio").exists()){
                        String bio=snapshot.child("Bio").getValue().toString();
                        ll_bio.setVisibility(View.VISIBLE);
                        tv_UserBio.setText(bio);
                    }else{
                        ll_bio.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Insitution").exists()){
                        String bio=snapshot.child("Insitution").getValue().toString();
                        ll_Institution.setVisibility(View.VISIBLE);
                        tv_UserInstitute.setText(bio);
                    }else{
                        ll_Institution.setVisibility(View.GONE);
                    }

                    if (snapshot.child("NickName").exists()){
                        String UserName=snapshot.child("NickName").getValue().toString();
                        tv_UserUserName.setVisibility(View.VISIBLE);
                        tv_UserUserName.setText(UserName);
                        ll_UserName.setVisibility(View.VISIBLE);
                    }else{
                        ll_UserName.setVisibility(View.GONE);
                    }
                    if (snapshot.child("Location").exists()){
                        ll_location.setVisibility(View.VISIBLE);
                        String Location=snapshot.child("Location").getValue().toString();
                        tv_UserLocation.setText(Location);
                    }else{
                        ll_location.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Category").exists()){
                        tv_addCategory.setVisibility(View.GONE);
                        String Category=snapshot.child("Category").getValue().toString();
                        tv_UserCategory.setVisibility(View.VISIBLE);
                        tv_UserCategory.setText(Category);
                    }else{
                        tv_addCategory.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowers= FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(memberUserId);
        refUserFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    long count=snapshot.getChildrenCount();
                    tv_CountFollowers.setText((int) count+" Followers");
                }
                else{
                    tv_CountFollowers.setText("No Followers");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(memberUserId);
        refUserFollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    long count=snapshot.getChildrenCount();
                    tv_CountFollowing.setText((int) count+" Following");
                }else {
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
                sentInvitation(memberUserId,memberUserName,"AddFrnd");

            }
        });
        btn_SentFrndReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sentInvitation(memberUserId,memberUserName, "FollowFrnd");
            }
        });

        btmSheetUserProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btmSheetUserProfile.show();
    }

    private void checkChatDashboard(String frndUserId) {

        rl_FrndChatLayout.setVisibility(View.VISIBLE);
        rl_ChatDoubt.setVisibility(View.GONE);

        overlappingPanels.closePanels();
        refChatDashboard = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID).child(frndUserId).child("Chat_Message");
        list_NewChatDashboard.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_NewChatDashboard.add(class_userDashBoard);
                    showChatDashadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
                } else {
                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
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
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
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

//                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference().
//                            child("Groups").child("Show_Group").child("User_Show_Group").child(userID).child(subGroupPushId);
//                    referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                            String push = "mszno_" + noofGroupinCategory;
//
//                            Calendar calenderCC = Calendar.getInstance();
//                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, subGroupPushId, groupPushId, noofGroupinCategory);
//                            referenceUser.child(push).setValue(userAddGroupClass);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });

                }
            }
        });

    }

    private void addSubChild0(int position,String groupTitle,String groupPushId, String groupUserID,String Category,String subChildGroupName) {

        android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_createclass, null);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        Button btn_nextAddTopic=dialogView.findViewById(R.id.btn_nextAddTopic);
        Button btn_CreateTopic=dialogView.findViewById(R.id.btn_CreateTopic);

        LinearLayout ll_addTopic=dialogView.findViewById(R.id.ll_addTopic);
        LinearLayout ll_creategroup=dialogView.findViewById(R.id.ll_creategroup);

        EditText et_ClassName=dialogView.findViewById(R.id.et_ClassName);
        EditText et_TopicName=dialogView.findViewById(R.id.et_TopicName);
        TextView tv_ClassName=dialogView.findViewById(R.id.tv_ClassName);
        TextView tv_AddClassTitle=dialogView.findViewById(R.id.tv_AddClassTitle);

            ll_addTopic.setVisibility(View.GONE);
//            Toast.makeText(getContext(), "check"+subChildGroupName, Toast.LENGTH_SHORT).show();

            if (subChildGroupName.equals("newClass")){
                et_ClassName.setVisibility(View.VISIBLE);
                tv_ClassName.setVisibility(View.GONE);
                ll_creategroup.setVisibility(View.VISIBLE);
            }else{
                ll_creategroup.setVisibility(View.GONE);
                ll_addTopic.setVisibility(View.VISIBLE);
                tv_ClassName.setVisibility(View.VISIBLE);
                tv_AddClassTitle.setVisibility(View.GONE);
                tv_ClassName.setText("Please create a new Topic under "+subChildGroupName);
            }

//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

//            et_TopicName.setVisibility(View.VISIBLE);
//                ll_groupFamFrnds.setVisibility(View.GONE);

            btn_nextAddTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sbChildGroupName=et_ClassName.getText().toString().trim();
//                    String subTopicName=et_TopicName.getText().toString().trim();
                    if (sbChildGroupName.isEmpty()) {
                        Toast.makeText(getContext(), "Enter All Details", Toast.LENGTH_SHORT).show();
                        et_ClassName.setError("Enter Class Name");
                    }

                    else {
                        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
                        refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(sbChildGroupName).child("SubGroup_SubsList");
//
                    refChildGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                if (snapshot.exists()){
//
//                                }

                                int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);
                                String position=Integer.toString(noofQuesinCategory);
                                Class_Group subGroup_Class = new Class_Group(dateTimeCC,userName,userID,sbChildGroupName,position,groupTitle,groupPushId);
                                Class_Group subGroupSubsList_Class = new Class_Group(dateTimeCC,userName,userID,userID,groupTitle,groupPushId,"Admin",sbChildGroupName);

                                refChildGroup.child(sbChildGroupName).setValue(subGroup_Class);
                                refChildGroupSubsList.child(userID).setValue(subGroupSubsList_Class);

//                                showSubChild_Adaptor.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

//                Toast.makeText(getContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();


//                        Class_Group class_ClassData=new Class_Group()
                        ll_addTopic.setVisibility(View.VISIBLE);
                        ll_creategroup.setVisibility(View.GONE);
                        tv_ClassName.setVisibility(View.VISIBLE);
//                        et_ClassName.setVisibility(View.GONE);
//                        tv_AddClassTitle.setVisibility(View.GONE);

                        tv_ClassName.setText("you have successfully created "+sbChildGroupName +" under Group"+groupTitle );

//                        ll_creategroup.setVisibility(View.GONE);
                    }
                }
            });

            btn_CreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String subChildGroupName=et_GroupName.getText().toString().trim();
                String subTopicName=et_TopicName.getText().toString().trim();
                if (subTopicName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Topic Name", Toast.LENGTH_SHORT).show();
                    et_TopicName.setError("Enter Topic Name");
                }
                else {

//

                    if (subChildGroupName.equals("newClass")){
                        String sbChildGroupName=et_ClassName.getText().toString().trim();
                        refGroupTopic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(sbChildGroupName);
                        refGroupTopic.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                                int noofQuesinCategory= (int) (snapshot.getChildrenCount()+1);

//                            if (noofQuesinCategory==8){
//                                reference.child( "group1" ).setValue( subChildGroupName );
//                            }else{
//                                Toast.makeText(getContext(), "Wrong data", Toast.LENGTH_SHORT).show();
//                            }

                                switch(noofQuesinCategory){
                                    case 13:
                                        refChildGroup.child(sbChildGroupName).child( "group1" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 14:
                                        refChildGroup.child(sbChildGroupName).child( "group2" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 15:
                                        refChildGroup.child(sbChildGroupName).child( "group3" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 16:
                                        refChildGroup.child(sbChildGroupName).child( "group4" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 17:
                                        refChildGroup.child(sbChildGroupName).child( "group5" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 18:
                                        refChildGroup.child(sbChildGroupName).child( "group6" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 19:
                                        refChildGroup.child(sbChildGroupName).child( "group7" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 20:
                                        refChildGroup.child(sbChildGroupName).child( "group8" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 21:
                                        refChildGroup.child(sbChildGroupName).child( "group9" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                                }
                                HomeFragment hm=new HomeFragment();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_container,hm);
                                ft.addToBackStack(null);
//                                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                                showGroupadaptor.notifyDataSetChanged();
                                ft.commit();

                                dialogBuilder.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }else{
                        refGroupTopic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(subChildGroupName);
                        refGroupTopic.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                                int noofQuesinCategory= (int) (snapshot.getChildrenCount()+1);

//                            if (noofQuesinCategory==8){
//                                reference.child( "group1" ).setValue( subChildGroupName );
//                            }else{
//                                Toast.makeText(getContext(), "Wrong data", Toast.LENGTH_SHORT).show();
//                            }

                                switch(noofQuesinCategory){
                                    case 13:
                                        refChildGroup.child(subChildGroupName).child( "group1" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 14:
                                        refChildGroup.child(subChildGroupName).child( "group2" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 15:
                                        refChildGroup.child(subChildGroupName).child( "group3" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 16:
                                        refChildGroup.child(subChildGroupName).child( "group4" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 17:
                                        refChildGroup.child(subChildGroupName).child( "group5" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 18:
                                        refChildGroup.child(subChildGroupName).child( "group6" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 19:
                                        refChildGroup.child(subChildGroupName).child( "group7" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 20:
                                        refChildGroup.child(subChildGroupName).child( "group8" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 21:
                                        refChildGroup.child(subChildGroupName).child( "group9" ).setValue( subTopicName );
                                        Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();

                                }
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                                showGroupadaptor.notifyDataSetChanged();

                                dialogBuilder.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }

//                    refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(subTopicName).child("SubGroup_SubsList");
//
//                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
//                            child( "Groups" ).child( "User_All_Group" ).child(userID).child(groupTitle);

//                    refGroupTopic.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
//                            int noofQuesinCategory= (int) (snapshot.getChildrenCount()+1);
//
////                            if (noofQuesinCategory==8){
////                                reference.child( "group1" ).setValue( subChildGroupName );
////                            }else{
////                                Toast.makeText(getContext(), "Wrong data", Toast.LENGTH_SHORT).show();
////                            }
//
//                            switch(noofQuesinCategory){
//                                case 13:
//                                    refChildGroup.child(subChildGroupName).child( "group1" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 14:
//                                    refChildGroup.child(subChildGroupName).child( "group2" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 15:
//                                    refChildGroup.child(subChildGroupName).child( "group3" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 16:
//                                    refChildGroup.child(subChildGroupName).child( "group4" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 17:
//                                    refChildGroup.child(subChildGroupName).child( "group5" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 18:
//                                    refChildGroup.child(subChildGroupName).child( "group6" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 19:
//                                    refChildGroup.child(subChildGroupName).child( "group7" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 20:
//                                    refChildGroup.child(subChildGroupName).child( "group8" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 21:
//                                    refChildGroup.child(subChildGroupName).child( "group9" ).setValue( subTopicName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                default:
//                                    Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();
//
//                            }
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                            showGroupadaptor.notifyDataSetChanged();
//
//                            dialogBuilder.dismiss();
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
                }
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void AllPublicJGroup(String groupAdmin, String groupName, String groupCategory, String subChild,String subChildGroupName) {

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
            child( "Groups" ).child( "All_Universal_Group" );
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                if (ds.child("userId").exists() || ds.child("groupName").exists() ||
                        ds.child("groupCategory").exists()) {
                    if (ds.child("userId").getValue().equals(groupAdmin) &&
                            ds.child("groupName").getValue().equals(groupName) &&
                            ds.child("groupCategory").getValue().equals(groupCategory)) {
                        String grouppushid=ds.getKey();
//                        Toast.makeText(getContext(), "g"+grouppushid, Toast.LENGTH_SHORT).show();
                        reference.child(grouppushid).child(subChild).setValue(subChildGroupName);
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    DatabaseReference referenceAllGroup= FirebaseDatabase.getInstance().getReference().
            child("Groups").child("User_All_Group").child(userID);

    referenceAllGroup.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                if (ds.child("userId").exists() || ds.child("groupName").exists() ||
                        ds.child("groupCategory").exists()) {
                    if (ds.child("userId").getValue().equals(groupAdmin) &&
                            ds.child("groupName").getValue().equals(groupName) &&
                            ds.child("groupCategory").getValue().equals(groupCategory)) {
                        String grouppushid=ds.getKey();
//                        Toast.makeText(getContext(), "g"+grouppushid, Toast.LENGTH_SHORT).show();
                        referenceAllGroup.child(grouppushid).child(subChild).setValue(subChildGroupName);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), "on activity result called", Toast.LENGTH_SHORT).show();

        if (requestCode==100){
//            Toast.makeText(getContext(), "100", Toast.LENGTH_SHORT).show();

            Task<GoogleSignInAccount> signInAccountTask= GoogleSignIn
                    .getSignedInAccountFromIntent(data);
//            Toast.makeText(getContext(), "checking", Toast.LENGTH_SHORT).show();
            if (signInAccountTask.isSuccessful()){
                String s= "Google Signin is sucessful";
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    submitLoginData();
//                                    showStudTeachBtmDialog();
//                                    chipNavigationBar.setVisibility(View.VISIBLE);

                                }else{
                                    Toast.makeText(getContext(), "Authentication Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getContext(), "google account null", Toast.LENGTH_SHORT).show();
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            Toast.makeText(getContext(), "wrong request code", Toast.LENGTH_SHORT).show();

        }
    }
    private void setupViewPager1(ViewPager view_pager_chatView) {
        Adaptor_Tab_ChatDiscussion adaptorTabFrndChat=new Adaptor_Tab_ChatDiscussion(getChildFragmentManager());

        adaptorTabFrndChat.addFragment(new Chat_New_Fragment(),"New Chat");
        adaptorTabFrndChat.addFragment(new Chat_List_Fragment(),"Friends List");

        view_pager_chatView.setAdapter(adaptorTabFrndChat);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adaptor_Tab_ChatDiscussion adaptorTabChatDiscussion=new Adaptor_Tab_ChatDiscussion(getChildFragmentManager());

        adaptorTabChatDiscussion.addFragment(new ChatFragment(),"Chat");
        adaptorTabChatDiscussion.addFragment(new Discussion_Fragment(),"Discussion");

        viewPager.setAdapter(adaptorTabChatDiscussion);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        setupViewPager1(view_Pager_ChatView);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }
    private void submitLoginData() {
//        rl_feed.setBackgroundColor(Color.GRAY);
//        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getContext());
//        bottomSheetDialog.setCancelable(false);
//        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);
//
//        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
//        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Token Failed"+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                        refUserRegister.child( "Name" ).setValue( userName );
                        refUserRegister.child( "Email" ).setValue( userEmailID );
                        refUserRegister.child( "UserId" ).setValue( userID );
                        refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                        refUserRegister.child( "Category" ).setValue("Student");
                        refUserRegister.child( "userStatus" ).setValue("Online");
                        refUserRegister.child( "token" ).setValue(token);
                        notifyPB.dismiss();
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                });

    }

//    private void bottomMenu() {
////        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(int i) {
////                Fragment fragment = null;
////                switch (i) {
////                    case R.id.bottom_nav_home:
////                        fragment = new HomeFragment();
////                        tag="home";
////                        break;
////                    case R.id.bottom_nav_feed:
//////                        fragment = new FeedFragment();
////                        fragment = new FeedFragment();
////                        tag="feed";
////                        break;
////                    case R.id.bottom_nav_notification:
////                        fragment = new NotificationFragment();
////                        tag="notify";
////                        break;
////                    case R.id.bottom_nav_profile:
////                        fragment = new ProfileFragment();
////                        tag="profile";
////                        break;
////                }
////                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
////                transaction.replace(R.id.fragment_container,fragment,tag);
////                transaction.addToBackStack(tag);
////                transaction.commit();
////
////            }
////        });
//
////        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
//        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//
//                switch (item.getItemId()) {
//                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag = "home";
//                        break;
//                    case R.id.bottom_nav_discover:
////                          fragment = new FeedFragment();
//                        fragment = new JoinGroupFragment();
//                        tag = "joingroup";
//                        break;
//                    case R.id.bottom_nav_notification:
//                        fragment = new User_Notification_Frag();
//                        tag = "notify";
//                        break;
//                    case R.id.bottom_nav_profile:
//                        fragment = new ProfileFragment();
//                        tag = "profile";
//                        break;
////                    case R.id.action_Share:
////                        Toast.makeText(landing_Page.this, "Refer and Earn", Toast.LENGTH_LONG).show();
////                        generateLink();
////                        break;
//                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container,fragment,tag);
//                transaction.addToBackStack(tag);
//                transaction.commit();
//
//
//                return true;
//            }
//        });
//    }


}
