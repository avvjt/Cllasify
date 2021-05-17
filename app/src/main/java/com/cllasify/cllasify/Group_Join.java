package com.cllasify.cllasify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_JoinGroupReq;
import com.cllasify.cllasify.Adaptor.Adaptor_SearchGroup;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Group_Join extends AppCompatActivity {
    ProgressDialog notifyPB;
    Adaptor_SearchGroup showAllGroupAdaptor;
    Adaptor_JoinGroupReq showSubChild_Adaptor;
    List<Class_Group> listAllGroupStatus,list_SubChild;
    DatabaseReference refSearchShowGroup,refChildGroup;
    RecyclerView rv_AllGroupStatus;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    SearchView esv_groupSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_join_activity);

        esv_groupSearchView =findViewById(R.id.esv_groupSearchView);
        rv_AllGroupStatus =findViewById(R.id.rv_AllGroupStatus);

        notifyPB = new ProgressDialog(Group_Join.this);
        notifyPB.setTitle("Join Group");
        notifyPB.setMessage("Loading All Groups");
        notifyPB.setCanceledOnTouchOutside(true);
        notifyPB.show();

        rv_AllGroupStatus.setLayoutManager(new LinearLayoutManager(this));

        listAllGroupStatus =new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_SearchGroup(this, listAllGroupStatus);
        rv_AllGroupStatus.setAdapter(showAllGroupAdaptor);


        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" );

        if (esv_groupSearchView != null) {
            esv_groupSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                child( "Groups" ).child( "All_Universal_Group" );
        refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    showEAllGroupSearchRV();
                }else{
                    notifyPB.dismiss();
                    Toast.makeText(Group_Join.this, "No Other Group Present", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
    private void showEAllGroupSearchRV() {
//        listGroupSTitle=new ArrayList<>();

        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_SearchGroup.OnItemClickListener() {
            @Override
            public void createGroupDialog(String adminGroupID, String groupName,String groupPushId) {
                sentInvitation(adminGroupID,groupName,groupPushId);
            }
        });
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        String currUserId = mUser.getUid();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    String databaseUserId=class_userDashBoard.getUserId();
                    String groupCategory=class_userDashBoard.getGroupCategory();
                    if (!currUserId.equals(databaseUserId) && groupCategory.equals("Public")){

                        listAllGroupStatus.add(class_userDashBoard);
                        notifyPB.dismiss();
                        showAllGroupAdaptor.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(Group_Join.this, "No group yet created", Toast.LENGTH_SHORT).show();
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


    private void sentInvitation1(String adminGroupID, String groupName,String groupPushId) {

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Group_Join.this);
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Do you want to send Group Joining request to Admin?")
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
                                DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("User").child("GetReq").child(adminGroupID);
                                DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("User").child("SubmitReq").child(userID);

                                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                                        String push="Joining Reqno_"+noofQuesinCategory;


                                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID,adminGroupID, "req_sent",userEmail, push, groupName,groupPushId);
                                        refjoiningReq.child(push).setValue(userAddComment);
                                        refacceptingReq.child(push).setValue(userAddComment);
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
    private void sentInvitation(String adminGroupID, String groupName,String groupPushId) {
    final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(Group_Join.this).create();
    dialogBuilder.setCanceledOnTouchOutside(true);
    //dialogBuilder.setCancelable(false);
    LayoutInflater inflater = this.getLayoutInflater();

    final View dialogView = inflater.inflate(R.layout.dialog_creategroup1, null);
    TextView tapCancel=dialogView.findViewById(R.id.tv_Cancel);
    TextView tapSubmit=dialogView.findViewById(R.id.tv_Submit);
    RecyclerView rv_JoinGroupReq=dialogView.findViewById(R.id.rv_JoinGroupReq);
     rv_JoinGroupReq.setLayoutManager(new LinearLayoutManager(Group_Join.this));

        list_SubChild = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();

        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);

        showSubChild_Adaptor = new Adaptor_JoinGroupReq(Group_Join.this, list_SubChild);
        rv_JoinGroupReq.setAdapter(showSubChild_Adaptor);



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

//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

    tapCancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogBuilder.dismiss();
        }

    });
    tapSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogBuilder.dismiss();
        }
    });
    dialogBuilder.setView(dialogView);
    dialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentTransaction ft;
        HomeFragment fragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, fragment,"home");
        ft.commit();
    }
}