package com.cllasify.cllasify.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_QueryGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Class_Group;
import com.cllasify.cllasify.Phone_Login;
import com.cllasify.cllasify.R;
import com.discord.panels.OverlappingPanelsLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import java.util.Objects;

public class HomeFragment extends Fragment {

    RecyclerView rv_GroupTitle,rv_GroupData,rv_GroupDashData,rv_UserPrivateGroupTitle,rv_UserPublicGroupTitle;
    DatabaseReference refShowAllGroup,refTempGroupDB,refGroupDashboard;
    List<Class_Group> listGroupTitle,listDashboard,listUserPrivateGroupTitle,listUserPublicGroupTitle;
    Adaptor_QueryGroup showGroupadaptor,showUserPrivateGroupadaptor,showUserPublicGroupadaptor;
    Adaptor_ShowGroup showDashadaptor;

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
     DatabaseReference refuserPersonalGroup,refuserAllGroup,
             refuserPublicGroup,refAllPublicGroup,refAddSubGroup,
             refShowUserGroup,refShowUserPublicGroup,refShowUserPrivateGroup,
    refteachStud;
    Class_Group userAddGroup,addAnswer;

    SearchView groupSearchView;
    EditText addAnswer_et, et_ctext;
    TextView postAnswer_tv, dispQues_tv,QuesAskedByTime_tv,
            tv_UserPublicTitle,tv_UserPrivateTitle,
            tv_cpaneltitle,tv_cpanelbody;

    ImageButton ib_cattach, ib_csubmit;
    Button btn_caddgroup, btn_csearchgroup;
    Button btn_lteachresult,btn_lteachattend,btn_lteachexam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

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
            notifyPB.show();

            chipNavigationBar = getActivity().findViewById(R.id.bottom_nav_menu);
            chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);

            rv_GroupTitle =view.findViewById(R.id.rv_UserGroupTitle);
            rv_UserPrivateGroupTitle =view.findViewById(R.id.rv_UserPrivateGroupTitle);
            rv_UserPublicGroupTitle =view.findViewById(R.id.rv_UserPublicGroupTitle);

            tv_UserPublicTitle =view.findViewById(R.id.tv_UserPublicTitle);
            tv_UserPrivateTitle =view.findViewById(R.id.tv_UserPrivateTitle);

            btn_lteachattend =view.findViewById(R.id.btn_lteachattend);
            btn_lteachexam =view.findViewById(R.id.btn_lteachexam);
            btn_lteachresult =view.findViewById(R.id.btn_lteachresult);


            overlappingPanels = view.findViewById(R.id.overlapping_panels);
            btn_AddGroup =view.findViewById(R.id.btn_AddGroup);
            groupSearchView=view.findViewById(R.id.cgroupSearchView);

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID=currentUser.getUid();

//        openStartPanelButton = view.findViewById(R.id.open_start_panel_button);

            refShowUserGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "User_All_Group" ).child(userID);
            refShowUserPrivateGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Private_Group").child(userID);
            refShowUserPublicGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);
            refteachStud = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("User_Public_Group").child(userID);

            //            refShowUserGroup.keepSynced(true);
            refShowUserPrivateGroup.keepSynced(true);
            refShowUserPublicGroup.keepSynced(true);

//        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(linearLayoutManager);

            rv_GroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_UserPublicGroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_UserPrivateGroupTitle.setLayoutManager(new LinearLayoutManager(getContext()));

            listGroupTitle = new ArrayList<>();
            listUserPrivateGroupTitle = new ArrayList<>();
            listUserPublicGroupTitle = new ArrayList<>();

            showGroupadaptor = new Adaptor_QueryGroup(getContext(), listGroupTitle);
            showUserPrivateGroupadaptor = new Adaptor_QueryGroup(getContext(), listUserPrivateGroupTitle);
            showUserPublicGroupadaptor = new Adaptor_QueryGroup(getContext(), listUserPublicGroupTitle);

            rv_GroupTitle.setAdapter(showGroupadaptor);
            rv_UserPrivateGroupTitle.setAdapter(showUserPrivateGroupadaptor);
            rv_UserPublicGroupTitle.setAdapter(showUserPublicGroupadaptor);
//            rv_GroupTitle.keepSynced(true);
//        recyclerView1.setAdapter(showQuesadaptor);

            rv_GroupDashData=view.findViewById(R.id.rv_cGroupDashData);
            rv_GroupDashData.setLayoutManager(new LinearLayoutManager(getContext()));
            listDashboard = new ArrayList<>();
            showDashadaptor = new Adaptor_ShowGroup(getContext(), listDashboard);
            rv_GroupDashData.setAdapter(showDashadaptor);

//Left Panel
            btn_AddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    btn_show.setVisibility(View.VISIBLE);
                    addGroupBtmDialog();
                }
            });

//            showUserGroupRV();
            showUserPrivateGroupRV();

            DatabaseReference refUserStatus = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
            refUserStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();
                        String getTeach=snapshot.child("Category").getValue().toString();
                        if (getTeach.equals("Teacher")){
                            btn_lteachattend.setVisibility(View.VISIBLE);
                            btn_lteachexam.setVisibility(View.VISIBLE);
                            btn_lteachresult.setVisibility(View.VISIBLE);

                        }else{
                            Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
                        }

                    }
//                    else {
//                        Toast.makeText(getContext(), "You Registered as Student", Toast.LENGTH_SHORT).show();
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            refShowUserPublicGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        showUserPublicGroupRV();
                    }else {
                        Toast.makeText(getContext(), "No groups created yet", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            refShowUserPublicGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        showUserPublicGroupRV();
                    }else {
                        Toast.makeText(getContext(), "No groups created yet", Toast.LENGTH_SHORT).show();
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
                        showUserPrivateGroupRV();
                    }else {
                        Toast.makeText(getContext(), "No groups created yet", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

//Center Panel
            ib_cattach =view.findViewById(R.id.ib_cattach);
            ib_csubmit =view.findViewById(R.id.ib_csubmit);
            et_ctext =view.findViewById(R.id.et_ctext);
            btn_caddgroup =view.findViewById(R.id.btn_caddgroup);
            btn_csearchgroup =view.findViewById(R.id.btn_csearchgroup);
            tv_cpaneltitle =view.findViewById(R.id.tv_cpaneltitle);
            tv_cpanelbody =view.findViewById(R.id.tv_cpanelbody);


            btn_caddgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addGroupBtmDialog();
                }
            });

            btn_csearchgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overlappingPanels.openEndPanel();
                }
            });

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

            if (groupSearchView != null) {
                groupSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        search(newText);
                        return false;
                    }
                });
            }
        }else{
            showLoginBtmDialog();
        }




        return view;

    }

    public void search(String newText) {
        ArrayList<Class_Group> listSearchQues=new ArrayList<>();
        for (Class_Group classUserSearch:listGroupTitle){
            if (classUserSearch.getGroupName().toLowerCase().contains(newText.toLowerCase())){
                listSearchQues.add(classUserSearch);
            }
        }
        Adaptor_QueryGroup adapSearchJob= new Adaptor_QueryGroup(getContext(),listSearchQues);
        rv_GroupDashData.setAdapter(adapSearchJob);
    }

    private void showLoginBtmDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(true);

        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_login);


        Button btn_phonelogin=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
        Button btn_Cancel=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);
        Button btn_googlelogin=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialoglogin.dismiss();
            }
        });
        btn_googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        student_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmail );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Student");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(getContext(), "Login Sucessful as Student", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

            }
        });
        teacher_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmail );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Teacher");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(getContext(), "Login Sucessful as Teacher", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

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
                                    chipNavigationBar.setVisibility(View.VISIBLE);

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

    private void addComment(String groupTitle) {

        refAddSubGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "Users_Show_Group" ).child( userID ).child(groupTitle);
        refAddSubGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long noofQuesinCategory=snapshot.getChildrenCount()+1;
                String push=groupTitle+"_Cmntno_"+noofQuesinCategory;
                String Comment=addAnswer_et.getText().toString();

//                String groupTitle=snapshot.child(userID).getValue().toString();
                refAddSubGroup = FirebaseDatabase.getInstance().getReference().
                        child( "Groups" ).child( "Users_Show_Group" ).child( userID ).child(groupTitle);
                Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID, userEmailID, push, groupTitle,Comment);
                refAddSubGroup.child(push).setValue(userAddComment);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

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

    private void addGroupBtmDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(true);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_addgroup);
        Button btn_CreateGroup=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);
        Button btn_JoinGroup=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Group_AddPrivPublic.class));
                createGroupDialog();
                bottomSheetDialoglogin.dismiss();

            }
        });
        btn_JoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Group_AddPrivPublic.class));

            }
        });


        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialoglogin.show();

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

                String GroupName=et_GroupName.getText().toString();
                if (GroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "User_All_Group" ).child(userID);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                            long noofGroupinCategory=snapshot.getChildrenCount()+1;
//                            String position=getString((int) noofQuesinCategory);
                            String push="GroupNo_"+noofGroupinCategory+"_"+GroupName;
                            Calendar calenderCC= Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
                            refAllPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_Group").child(push);

                            if (GroupCategory.equals("Private")) {
                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
                                refuserPersonalGroup.setValue(userAddGroup);
                                refuserAllGroup.setValue(userAddGroup);
                            } else if (GroupCategory.equals("Public")) {
                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
                                refuserPublicGroup.setValue(userAddGroup);
                                refAllPublicGroup.setValue(userAddGroup);
                                refuserAllGroup.setValue(userAddGroup);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Toast.makeText(getContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
                    dialogBuilder.dismiss();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showUserGroupRV() {

        showGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
            @Override
            public void addSubGroup(int position, String groupTitle, String groupUserID) {
                addSubChild0(position,groupTitle,groupUserID);
            }

            @Override
            public void addSubChild1(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                }

            @Override
            public void addSubChild2(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild3(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild4(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild5(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild6(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild7(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild8(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }

            @Override
            public void addSubChild9(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
                
            }
        });
                int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                    listGroupTitle.add(userQuestions);
                    showGroupadaptor.notifyDataSetChanged();
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
        refShowUserGroup.addChildEventListener(childEventListener);

        refShowUserGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//
//    }

    }
    public void showUserPrivateGroupRV() {

        showUserPrivateGroupadaptor.setOnItemClickListener(new Adaptor_QueryGroup.OnItemClickListener() {
            @Override
            public void addSubGroup(int position, String groupTitle, String groupUserID) {
                addSubPrivateChild0(position,groupTitle,groupUserID);
            }

            @Override
            public void addSubChild1(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);
            }

            @Override
            public void addSubChild2(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild3(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild4(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild5(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild6(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild7(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild8(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild9(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }
        });
                int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                    listUserPrivateGroupTitle.add(userQuestions);
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
            public void addSubGroup(int position, String groupTitle, String groupUserID) {
                addSubPublicChild0(position,groupTitle,groupUserID);
            }

            @Override
            public void addSubChild1(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);


            }

            @Override
            public void addSubChild2(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild3(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild4(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild5(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild6(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild7(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild8(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }

            @Override
            public void addSubChild9(int position, String groupName, String subgroupName) {
                overlappingPanels.closePanels();
                showDashBoardRV(position,groupName+subgroupName,groupName,subgroupName);

            }
        });
                int count=0;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
                    listUserPublicGroupTitle.add(userQuestions);
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

    private void addSubChild0(int position,String groupTitle, String groupUserID) {

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

                String subChildGroupName=et_GroupName.getText().toString();
                if (subChildGroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "User_All_Group" ).child(userID).child(groupTitle);

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

                String subChildGroupName=et_GroupName.getText().toString();
                if (subChildGroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                            child( "Groups" ).child( "User_Public_Group" ).child(userID).child(groupTitle);

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

                String subChildGroupName=et_GroupName.getText().toString();
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

    public void showDashBoardRV(int position, String groupCombined, String groupName,String subgroupName) {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//


        switch(position){
            case 0 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
//                addAnswer(position,groupCombined);

                upDateDashboard(position,groupCombined,subgroupName,groupName);

                    break;
            case 1 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();

                upDateDashboard(position,groupCombined,subgroupName,groupName);
                break;
            case 2 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);

                break;
            case 3 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);
                break;

            case 4 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);
                break;
            case 5 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);

                break;
            case 6 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);

                break;
            case 7 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);

                break;
            case 8 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);
                break;
            case 9 :
                Toast.makeText(getContext(), "Loading : "+groupCombined, Toast.LENGTH_SHORT).show();
                upDateDashboard(position,groupCombined,subgroupName,groupName);
                break;
        }

//
//        showQuesadaptor.setOnItemClickListener(new Adaptor_QueryQuestions.OnItemClickListener() {
//            @Override
//            public void shareQues(int position,String Title) {
////                generateLink(Title);
//                Toast.makeText(getContext(), "share", Toast.LENGTH_SHORT).show();
//            }
//        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    listDashboard.add(class_userDashBoard);
                    showDashadaptor.notifyDataSetChanged();
                    notifyPB.dismiss();
                } else {
                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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
        refGroupDashboard.addChildEventListener(childEventListener);
//        Bundle intent=getIntent().getExtras();
//        if (intent!=null){
//            String publisher=intent.getString("publisherid");
//            SharedPreferences.Editor editor=getSharedPreferences("PUBLISH",MODE_PRIVATE).edit();
//            editor.putString("publisherid",publisher);
//            editor.apply();
//
//        }

    }

    private void upDateDashboard(int position, String groupCombined, String subgroupName, String groupName) {

        refTempGroupDB = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "TempCmntGroup" ).child( userID );
        refGroupDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "Users_Show_Group" ).child( userID ).child(groupCombined);
        refGroupDashboard.keepSynced(true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userEmailID= currentUser.getEmail();
        userPhoto=currentUser.getPhotoUrl();
        userName=currentUser.getDisplayName();

        refTempGroupDB.setValue(groupCombined);
        tv_cpaneltitle.setText(groupName);
        tv_cpanelbody.setText(subgroupName);
        btn_caddgroup.setVisibility(View.GONE);
        btn_csearchgroup.setVisibility(View.GONE);

        ib_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupName = et_ctext.getText().toString();
                if (GroupName.isEmpty()) {
                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
                    et_ctext.setError("Enter text");
                } else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("Users_Show_Group").child(userID).child(groupCombined);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" Answers");
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
//                            String position=getString((int) noofQuesinCategory);
                            String push = "mszno_" + noofGroupinCategory + "_" + groupCombined;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
//
//                            refuserPersonalGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Private_Group").child(userID).child(push);
//                            refuserPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_Public_Group").child(userID).child(push);
//                            refuserAllGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("User_All_Group").child(userID).child(push);
//                            refAllPublicGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Public_Group").child(push);

//                            if (GroupCategory.equals("Private")) {
//                                userAddGroup = new Class_Group(dateTimeCC,userName, userID, userEmailID,push, GroupName,GroupCategory,noofGroupinCategory);
//                                refuserPersonalGroup.setValue(userAddGroup);
//                                refuserAllGroup.setValue(userAddGroup);
//                            } else if (GroupCategory.equals("Public")) {
                            userAddGroup = new Class_Group(dateTimeCC, userName, userID, userEmailID, push, groupCombined,GroupName, noofGroupinCategory);
//                                refuserPublicGroup.setValue(userAddGroup);
//                                refAllPublicGroup.setValue(userAddGroup);
                            refGroupDashboard.child(push).setValue(userAddGroup);
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        }
    }
