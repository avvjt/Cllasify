package com.cllasify.cllasify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_SearchGroup;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Fragment.FeedFragment;
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
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_AllGroup;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    SearchView esv_groupSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_join_activity);

        esv_groupSearchView =findViewById(R.id.esv_groupSearchView);
        rv_AllGroup =findViewById(R.id.rv_AllGroup);

        notifyPB = new ProgressDialog(Group_Join.this);
        notifyPB.setTitle("Join Group");
        notifyPB.setMessage("Loading All Groups");
        notifyPB.setCanceledOnTouchOutside(true);
        notifyPB.show();

        rv_AllGroup.setLayoutManager(new LinearLayoutManager(this));

        listGroupSTitle=new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_SearchGroup(this, listGroupSTitle);
        rv_AllGroup.setAdapter(showAllGroupAdaptor);


        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Public_Group" );

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
        showEAllGroupSearchRV();
    }
    private void showEAllGroupSearchRV() {
//        listGroupSTitle=new ArrayList<>();

        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_SearchGroup.OnItemClickListener() {
            @Override
            public void createGroupDialog(String adminGroupID, String groupName) {
                sentInvitation(adminGroupID,groupName);
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    listGroupSTitle.add(class_userDashBoard);
                    notifyPB.dismiss();
                    showAllGroupAdaptor.notifyDataSetChanged();

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

    private void sentInvitation(String adminGroupID, String groupName) {

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


                                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID,adminGroupID, "req_sent",userEmail, push, groupName);
                                        refjoiningReq.child(push).setValue(userAddComment);
                                        refacceptingReq.child(push).setValue(userAddComment);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                                refacceptingReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                                        String push="Accepting Reqno_"+noofQuesinCategory;

                                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, userID,adminGroupID, "req_pending",userEmail, push, groupName);
//                                        refjoiningReq.child(push).setValue(userAddComment);
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

    @Override
    public void onBackPressed() {

        Fragment fragment=null;
        FragmentTransaction ft;
        fragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, fragment,"home");
        ft.commit();

//        super.onBackPressed();
    }

//    public void searchGroup(String newText) {
//
//        ArrayList<Class_Group> listGroupSearch=new ArrayList<>();
//        for (Class_Group classUserSearch:listGroupSTitle){
//            if (classUserSearch.getGroupName().toLowerCase().contains(newText.toLowerCase())){
//                listGroupSearch.add(classUserSearch);
//            }
//        }
//        Adaptor_QueryGroup adapSearchJob= new Adaptor_QueryGroup(getContext(),listGroupSearch);
////        erv_GroupSearchData.setAdapter(adapSearchJob);
//
//    }
}