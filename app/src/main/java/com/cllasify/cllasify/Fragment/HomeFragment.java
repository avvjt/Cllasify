
package com.cllasify.cllasify.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cllasify.cllasify.Adaptor.Adaptor_ChildGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_DashboardTab;
//import com.cllasify.cllasify.Adaptor.Adaptor_ProfileTab;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup1;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Attendance_Activity;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Fragment.DashboardTab.ChatTab;
import com.cllasify.cllasify.Group_Join;
import com.cllasify.cllasify.Register.Phone_Login;
import com.cllasify.cllasify.R;
import com.discord.panels.OverlappingPanelsLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabItem;
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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    TabItem tabChatItem, tabDoubtItem;
    ViewPager viewPager;
    Adaptor_DashboardTab adaptor_dashboardTab;

//    RecyclerView rv_GroupTitle,rv_GroupData,rv_GroupDashData
            RecyclerView rv_UserPrivateGroupTitle,rv_UserPublicGroupTitle,rv_OtherPublicGroupTitle
            ,rv_GrpMemberList,rv_SubChild;

    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    ListView listView;
    DatabaseReference refShowAllGroup,refTempGroupDB, refGroupChildUserDashboard,refGrpChildAllDashboard;
    DatabaseReference refuserPersonalGroup,refuserAllGroup,
            refuserPublicGroup, refAllGroup,refAllPublicJGroup,
            refotheruserPublicGroup,
            refAddSubGroup,
            refSearchShowGroup,refShowUserPublicGroup,refShowUserPrivateGroup,
            refteachStud,
            refChildGroup, refChildGroupSubsList,
            refGroupSubsList,refGrpMemberList;

    List<Class_Group> list_GroupTitle, list_UserPrivateGroupTitle, list_UserPublicGroupTitle, list_OtherUserPublicGroupTitle,
            list_Dashboard,listGroupSTitle,listGrpMemberList,list_SubChild;
    Adaptor_QueryGroup showGroupadaptor,showUserPrivateGroupadaptor,showUserPublicGroupadaptor;
    Adaptor_ShowGrpMember showGrpMemberList;
    Adaptor_QueryGroup1 showOtherUserPublicGroupAdaptor;
    Adaptor_ShowGroup showDashadaptor;
    Adaptor_ChildGroup showSubChild_Adaptor;


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
     Button openStartPanelButton, btn_AddGroup,btn_show;
     //
    Class_Group userAddGroupClass,addAnswer,userSubsGroupClass;

    SearchView esv_groupSearchView;
//    SearchView sv_textSearchView,esv_groupSearchView;
    EditText addAnswer_et, et_ctext;
    TextView postAnswer_tv, dispQues_tv,QuesAskedByTime_tv,
            tv_UserPublicTitle,tv_UserPrivateTitle,tv_OtherTitle,
            tv_cpaneltitle,tv_cpanelbody,tv_StartPanel;

    ImageButton ib_cattach, ib_csubmit;
    Button btn_caddgroup, btn_cjoingroup;
    Button btn_lteachresult,btn_lteachattend,btn_lteachexam;
    LinearLayout ll_SubChild,ll_childData;
//    LinearLayout ll_bottom_send;

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


            chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);
            chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);

//            rv_GroupTitle =view.findViewById(R.id.rv_UserGroupTitle);
            rv_UserPrivateGroupTitle =view.findViewById(R.id.rv_UserPrivateGroupTitle);
            rv_UserPublicGroupTitle =view.findViewById(R.id.rv_UserPublicGroupTitle);
            rv_OtherPublicGroupTitle =view.findViewById(R.id.rv_OtherPublicGroupTitle);
            rv_SubChild=view.findViewById(R.id.rv_SubChild);

            tv_UserPublicTitle =view.findViewById(R.id.tv_UserPublicTitle);
            tv_UserPrivateTitle =view.findViewById(R.id.tv_UserPrivateTitle);
            tv_OtherTitle =view.findViewById(R.id.tv_OtherTitle);

            btn_lteachattend =view.findViewById(R.id.btn_lteachattend);
            btn_lteachexam =view.findViewById(R.id.btn_lteachexam);
            btn_lteachresult =view.findViewById(R.id.btn_lteachresult);
//            listView =view.findViewById(R.id.listView);
            ll_SubChild =view.findViewById(R.id.ll_SubChild);
            ll_childData =view.findViewById(R.id.ll_childData);

            overlappingPanels = view.findViewById(R.id.overlapping_panels);
            tv_cpaneltitle = view.findViewById(R.id.tv_cpaneltitle);
            tv_cpanelbody = view.findViewById(R.id.tv_cpanelbody);

            btn_AddGroup =view.findViewById(R.id.btn_AddGroup);
//            sv_textSearchView =view.findViewById(R.id.sv_textSearchView);

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID=currentUser.getUid();

//        openStartPanelButton = view.findViewById(R.id.open_start_panel_button);
        tv_StartPanel = view.findViewById(R.id.tv_StartPanel);

            refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" ).child(userID);
            refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refteachStud = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Subscribed_Groups");
//            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refotheruserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group");



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
//            erv_GroupSearchData.setLayoutManager(new LinearLayoutManager(getContext()));

            list_GroupTitle = new ArrayList<>();
            list_UserPrivateGroupTitle = new ArrayList<>();
            list_UserPublicGroupTitle = new ArrayList<>();
            list_Dashboard = new ArrayList<>();
            list_OtherUserPublicGroupTitle = new ArrayList<>();
            list_SubChild = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();


            showGroupadaptor = new Adaptor_QueryGroup(getContext(), list_GroupTitle);
            showUserPrivateGroupadaptor = new Adaptor_QueryGroup(getContext(), list_UserPrivateGroupTitle);
            showUserPublicGroupadaptor = new Adaptor_QueryGroup(getContext(), list_UserPublicGroupTitle);
            showDashadaptor = new Adaptor_ShowGroup(getContext(), list_Dashboard);
            showOtherUserPublicGroupAdaptor = new Adaptor_QueryGroup1(getContext(), list_OtherUserPublicGroupTitle);
            showSubChild_Adaptor = new Adaptor_ChildGroup(getContext(), list_SubChild);
//            showAllGroupAdaptor = new Adaptor_SearchGroup(getContext(), listGroupSTitle);

//            rv_GroupTitle.setAdapter(showGroupadaptor);
            rv_UserPrivateGroupTitle.setAdapter(showUserPrivateGroupadaptor);
            rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupadaptor);
            rv_OtherPublicGroupTitle.setAdapter(showOtherUserPublicGroupAdaptor);
            rv_SubChild.setAdapter(showSubChild_Adaptor);

//            rv_GroupDashData=view.findViewById(R.id.rv_GroupDashData);
//            rv_GroupDashData.setLayoutManager(new LinearLayoutManager(getContext()));
//            rv_GroupDashData.setAdapter(showDashadaptor);

            //TabLayout
            tabLayout= view.findViewById(R.id.tabLayout);
            tabChatItem = view.findViewById(R.id.tabChatItem);
            tabDoubtItem = view.findViewById(R.id.tabDoubtItem);
            viewPager= view.findViewById(R.id.viewPager);

            adaptor_dashboardTab = new Adaptor_DashboardTab(getChildFragmentManager(),tabLayout.getTabCount());
            viewPager.setAdapter(adaptor_dashboardTab);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());

                    if(tab.getPosition()==0 || tab.getPosition()==1)
                        adaptor_dashboardTab.notifyDataSetChanged();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            //listen for scroll or page change



//Left Panel

            tv_StartPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ll_SubChild.setVisibility(View.GONE);
                    list_SubChild.clear();

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
                    list_Dashboard.clear();


                    ll_childData.setVisibility(View.GONE);
                    listGrpMemberList.clear();


                }
            });
            btn_AddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    btn_show.setVisibility(View.VISIBLE);
//                    addGroupBtmDialog();
                    createGroupDialog();
                }
            });

//            showUserGroupRV();
//            showUserPrivateGroupRV();


            refShowUserPublicGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        notifyPB.show();
                        showUserPublicGroupRV();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            refShowUserPrivateGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        notifyPB.show();
                        showUserPrivateGroupRV();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            refotheruserPublicGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        showOtherUserGroupRV();
                        notifyPB.show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//




//Center Panel
            ib_cattach =view.findViewById(R.id.ib_cattach);
            ib_csubmit =view.findViewById(R.id.ib_csubmit);
            et_ctext =view.findViewById(R.id.et_ctext);
            btn_caddgroup =view.findViewById(R.id.btn_caddgroup);
            btn_cjoingroup =view.findViewById(R.id.btn_cjoingroup);
//            ll_bottom_send =view.findViewById(R.id.ll_bottom_send);


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
                    startActivity(new Intent(getContext(), Group_Join.class));
                }
            });
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
            rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getContext()));


            listGrpMemberList = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();


            showGrpMemberList = new Adaptor_ShowGrpMember(getContext(), listGrpMemberList);
            rv_GrpMemberList.setAdapter(showGrpMemberList);



        }else{
            showLoginBtmDialog();
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


    private void showLoginBtmDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(true);

        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_login);


        Button btn_phonelogin=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
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


                Intent intent=googleSignInClient.getSignInIntent();

                startActivityForResult(intent,100);
                bottomSheetDialoglogin.dismiss();

            }
        });

        btn_phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Phone_Login.class));
                bottomSheetDialoglogin.dismiss();
            }
        });

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

        student_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmailID );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Student");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(getContext(), "Login Sucessful as Student", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();

            }
        });
        teacher_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmailID );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Teacher");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(getContext(), "Login Sucessful as Teacher", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
            }
        });

        bottomSheetDialog.show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), "on activity result called", Toast.LENGTH_SHORT).show();

        if (requestCode==100){
            Toast.makeText(getContext(), "100", Toast.LENGTH_SHORT).show();

            Task<GoogleSignInAccount> signInAccountTask= GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            Toast.makeText(getContext(), "checking", Toast.LENGTH_SHORT).show();


            if (signInAccountTask.isSuccessful()){

                String s= "Google Signin is sucessful";

                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    showStudTeachBtmDialog();
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
                addChildGroup(position,groupName,groupPushId,groupUserID,"public");
            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {

                showchildGroupRV(position,groupName,groupPushId,groupUserID);
            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {
//                Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
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
                addChildGroup(position,groupName,groupPushId,groupUserID,"public");
            }

            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
                showchildGroupRV(position,groupName,groupPushId,groupUserID);
            }

            @Override
            public void showll_Group(int position, String groupName, String groupPushId, String groupUserID) {
//                showchildGroupRV(groupTitle);
            }

        });
                int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                    list_UserPublicGroupTitle.add(userQuestions);
                    showUserPublicGroupadaptor.notifyDataSetChanged();
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

        showOtherUserPublicGroupAdaptor.setOnItemClickListener(new Adaptor_QueryGroup1.OnItemClickListener() {
            @Override
            public void showChildGroupAdaptor(int position, String groupName, String groupPushId, String groupUserID) {
                showchildGroupRV(position,groupName,groupPushId,groupUserID);
            }
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
//                    String check1=dataSnapshot.child("User_Subscribed_Groups").child(userID).getKey();

                    if(check.equals("true")) {
                        Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                        list_OtherUserPublicGroupTitle.add(userQuestions);
                        showOtherUserPublicGroupAdaptor.notifyDataSetChanged();
//                        Toast.makeText(getContext(),"chch"+check,Toast.LENGTH_SHORT).show();

                    }
//                    Toast.makeText(getContext(),"chch",Toast.LENGTH_SHORT).show();

                }

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
        refotheruserPublicGroup.orderByChild("groupno").addChildEventListener(childEventListener);


        refotheruserPublicGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    tv_OtherTitle.setText("Other Groups");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        refotheruserPublicGroup.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            if (snapshot.getChildrenCount()>0){
//                tv_OtherPublicGroupTitle.setText("Private Server");
//            }
//
//        }

//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//        }
//    });

    }

    private void addChildGroup(int position, String groupName,String groupPushId, String groupUserID, String category) {

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

                String subChildGroupName = et_GroupName.getText().toString().trim();
                if (subChildGroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                } else {
                    String groupSubGroup=groupName+"_"+subChildGroupName;
                    refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);
                    refChildGroupSubsList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(groupSubGroup).child("SubGroup_SubsList");

                    refChildGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int noofQuesinCategory = (int) (snapshot.getChildrenCount() + 1);
                                String position=Integer.toString(noofQuesinCategory);
                                Class_Group subGroup_Class = new Class_Group(dateTimeCC,subChildGroupName,userID,groupSubGroup,position,groupName,groupPushId);
                                Class_Group subGroupSubsList_Class = new Class_Group(dateTimeCC,userName,userID,userID,"Admin",subChildGroupName,groupSubGroup,groupName,groupPushId);
                                refChildGroup.child(groupSubGroup).setValue(subGroup_Class);
                                refChildGroupSubsList.child(userID).setValue(subGroupSubsList_Class);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                }
                Toast.makeText(getContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
                dialogBuilder.dismiss();

            }

        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//        dialogBuilder.dismiss();

    }

    private void showchildGroupRV(int position, String groupName, String groupPushId, String groupUserID) {


        list_SubChild.clear();
        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);

        refChildGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    ll_SubChild.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Please Create Sub Group using (+) Sign", Toast.LENGTH_LONG).show();
                    ll_SubChild.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        showSubChild_Adaptor.setOnItemClickListener(new Adaptor_ChildGroup.OnItemClickListener() {
            @Override
            public void showChildChatAdaptor(int position, String groupName, String subGroupName, String groupPushId, String subGroupPushID) {
                overlappingPanels.closePanels();
                upDateDashboard(position, groupName, subGroupName, groupPushId, subGroupPushID);
            }


        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                list_SubChild.add(userQuestions);
                showSubChild_Adaptor.notifyDataSetChanged();
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
        refChildGroup.orderByChild("position").addChildEventListener(childEventListener);

    }
    private void upDateDashboard(int position, String groupName, String subGroupName, String groupPushId,String subGroupPushId) {



        btn_caddgroup.setVisibility(View.GONE);
        btn_cjoingroup.setVisibility(View.GONE);


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
//        refTempGroupDB = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child( userID );
////        refGroupChildUserDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId);
//        refGrpChildAllDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId).child("Chat_Message");
//        refGroupChildUserDashboard.keepSynced(true);
//        refGrpChildAllDashboard.keepSynced(true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();


//        refTempGroupDB.setValue(subGroupPushId);

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
                Intent intent=new Intent(getContext(), Attendance_Activity.class);
                intent.putExtra("groupPushId", groupPushId);
                startActivity(intent);

//Inflate the fragment
//                getFragmentManager().beginTransaction().add(R.id.fragment_container, attendance).commit();
            }
        });


        DatabaseReference refUserStatus = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();
                    String getTeach=snapshot.child("Category").getValue().toString().trim();
                    if (getTeach.equals("Teacher")){
                        btn_lteachattend.setVisibility(View.VISIBLE);
                        btn_lteachexam.setVisibility(View.VISIBLE);
                        btn_lteachresult.setVisibility(View.VISIBLE);

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

        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId).child("SubGroup_SubsList");

        refGrpMemberList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);
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

//        Fragment fragment=new tasks();

        Bundle bundle =new Bundle();
        bundle.putString("groupName",groupName);
        bundle.putString("subGroupName",subGroupName);
        bundle.putString("groupPushId",groupPushId);
        bundle.putString("subGroupPushId",subGroupPushId);

        Fragment fragment=new ChatTab();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


//        showGroupMembers();
//        list_Dashboard.clear();
//        listDashboard=new ArrayList<>();
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (dataSnapshot.getChildrenCount()>0) {
//                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
//                    list_Dashboard.add(class_userDashBoard);
//                    showDashadaptor.notifyDataSetChanged();
//
//                    notifyPB.dismiss();
//                } else {
//                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
//                }
//
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
//        //refAdmin.addChildEventListener(childEventListener);
//        refGrpChildAllDashboard.addChildEventListener(childEventListener);


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

//                }
//            }
//        });
    }

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
//    private void addSubChild0(int position,String groupTitle, String groupUserID) {
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
//
////        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
////        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//                ll_creategroup.setVisibility(View.VISIBLE);
//
//                ll_groupFamFrnds.setVisibility(View.GONE);
//
//
//        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String subChildGroupName=et_GroupName.getText().toString().trim();
//                if (subChildGroupName.isEmpty()) {
//                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
//                    et_GroupName.setError("Enter Group Name");
//                }
//                else {
//
//                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
//                            child( "Groups" ).child( "User_All_Group" ).child(userID).child(groupTitle);
//
//                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
//                                case 9:
//                                    reference.child( "group1" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//
//                                    break;
//                                case 10:
//                                    reference.child( "group2" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 11:
//                                    reference.child( "group3" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 12:
//                                    reference.child( "group4" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 13:
//                                    reference.child( "group5" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 14:
//                                    reference.child( "group6" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 15:
//                                    reference.child( "group7" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 16:
//                                    reference.child( "group8" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 17:
//                                    reference.child( "group9" ).setValue( subChildGroupName );
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
//
//                }
//
//            }
//        });
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//
//
//
//    }
//    private void addSubPublicChild0(int position,String groupTitle, String groupUserID) {
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
//
////        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
////        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//                ll_creategroup.setVisibility(View.VISIBLE);
//
//                ll_groupFamFrnds.setVisibility(View.GONE);
//
//
//        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String subChildGroupName=et_GroupName.getText().toString().trim();
//                if (subChildGroupName.isEmpty()) {
//                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
//                    et_GroupName.setError("Enter Group Name");
//                }
//                else {
//
//                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
//                            child( "Groups" ).child( "User_Public_Group" ).child(userID).child(groupTitle);
////                    DatabaseReference referenceJoinOther= FirebaseDatabase.getInstance().getReference().
////                            child("Groups").child("All_Universal_Group").child(groupTitle);
//
//                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
//                            Class_Group userQuestions = snapshot.getValue(Class_Group.class);
//
//                            String groupAdmin=userQuestions.getUserId();
//                            String groupName=userQuestions.getGroupName();
//                            String groupCategory=userQuestions.getGroupCategory();
//
//                            switch(noofQuesinCategory){
//                                case 8:
//                                    reference.child( "group1" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group1",subChildGroupName);
//                                    break;
//                                case 9:
//                                    reference.child( "group2" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group2",subChildGroupName);
//                                    break;
//                                case 10:
//                                    reference.child( "group3" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup1" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup1" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group3",subChildGroupName);
//                                    break;
//                                case 11:
//                                    reference.child( "group4" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup2" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup2" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group4",subChildGroupName);
//                                    break;
//                                case 12:
//                                    reference.child( "group5" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup3" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup3" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group5",subChildGroupName);
//                                    break;
//                                case 13:
//                                    reference.child( "group6" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup4" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup4" ).setValue( subChildGroupName );
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group6",subChildGroupName);
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 14:
//                                    reference.child( "group7" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup5" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup5" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group7",subChildGroupName);
//                                    break;
//                                case 15:
//                                    reference.child( "group8" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup6" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup6" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group8",subChildGroupName);
//                                    break;
//                                case 16:
//                                    reference.child( "group9" ).setValue( subChildGroupName );
////                                    referenceAllGroup.child( "SubGroup7" ).setValue( subChildGroupName );
////                                    referenceJoinOther.child( "SubGroup7" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    AllPublicJGroup(groupAdmin,groupName,groupCategory,"group9",subChildGroupName);
//                                    break;
//                                default:
//                                    Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();
//
//                            }
//                            showGroupadaptor.notifyDataSetChanged();
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                            dialogBuilder.dismiss();
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//                }
//            }
//        });
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//
//
//
//    }
//
//    private void addSubPrivateChild0(int position,String groupTitle, String groupUserID) {
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
//
////        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
////        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//                ll_creategroup.setVisibility(View.VISIBLE);
//
//                ll_groupFamFrnds.setVisibility(View.GONE);
//
//
//        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String subChildGroupName=et_GroupName.getText().toString().trim();
//                if (subChildGroupName.isEmpty()) {
//                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
//                    et_GroupName.setError("Enter Group Name");
//                }
//                else {
//
//                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
//                            child( "Groups" ).child( "User_Private_Group" ).child(userID).child(groupTitle);
//
//                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
//                                case 9:
//                                    reference.child( "group1" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//
//                                    break;
//                                case 10:
//                                    reference.child( "group2" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 11:
//                                    reference.child( "group3" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 12:
//                                    reference.child( "group4" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 13:
//                                    reference.child( "group5" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 14:
//                                    reference.child( "group6" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 15:
//                                    reference.child( "group7" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 16:
//                                    reference.child( "group8" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 17:
//                                    reference.child( "group9" ).setValue( subChildGroupName );
//                                    Toast.makeText(getContext(), "Sub Group Created Sucessfully", Toast.LENGTH_SHORT).show();
//                                    break;
//                                default:
//                                    Toast.makeText(getContext(), "Sub Group limit Exceeded", Toast.LENGTH_SHORT).show();
//
//                            }
//                            showGroupadaptor.notifyDataSetChanged();
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                            dialogBuilder.dismiss();
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                }
//
//            }
//        });
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//
//
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




}
