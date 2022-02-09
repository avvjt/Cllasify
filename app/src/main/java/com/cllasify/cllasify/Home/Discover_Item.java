package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cllasify.cllasify.Adaptor.Adapter_Discover_Item;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpClass;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.R;
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

public class Discover_Item extends AppCompatActivity {

    /*
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Adapter_Discover_Item adapter_discover_item;
    */

    Adaptor_ShowGrpClass showGrpClassList;
    List<Class_Group_Names> listGrpClassList;
    DatabaseReference refGroupClassList;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_item);
/*
        recyclerView = findViewById(R.id.class_items_rv);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter_discover_item = new Adapter_Discover_Item(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_discover_item);
*/


//        ImageView iv_ServerDP= findViewById(R.id.iv_ServerDP);
//        TextView tv_ServerBio= findViewById(R.id.tv_ServerBio);
        Button btn_Share= findViewById(R.id.btn_Share);
        TextView tv_ServerName= findViewById(R.id.tv_ServerName);
        RecyclerView rv_ShowClass= findViewById(R.id.rv_ShowClass);

        String groupName = getIntent().getStringExtra("groupName");
        String groupPushId = getIntent().getStringExtra("groupPushId");

        tv_ServerName.setText(groupName);
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Discover_Item.this));

        listGrpClassList = new ArrayList<>();
        showGrpClassList = new Adaptor_ShowGrpClass(Discover_Item.this, listGrpClassList);
        rv_ShowClass.setAdapter(showGrpClassList);

        refGroupClassList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("All_GRPs").child(groupPushId);
        listGrpClassList.clear();

        showGrpClassList.setOnItemClickListener(new Adaptor_ShowGrpClass.OnItemClickListener() {
            @Override
            public void JoinGroupClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId) {
                sentGroupJoinInvitation(adminGroupID,adminUserName,groupName,groupPushId,subGroupName);

            }
        });

        refGroupClassList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);fatten
////                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
////                listView.setAdapter(arrayAdapter);
                Class_Group_Names class_userDashBoard = snapshot.getValue(Class_Group_Names.class);
                Log.d("CLLGRP", "onChildAdded: "+snapshot.getValue());
                listGrpClassList.add(class_userDashBoard);

                showGrpClassList.notifyDataSetChanged();
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


//        btn_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialoglogin.dismiss();
//            }
//        });
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String body = "Cllasify is the best App to Discuss Study Material with Classmates using Servers," +
                        "\nPlease Click on Below Link to Install:";
                String subject = "Install Classify App";
                String app_url = " https://play.google.com/store/apps/details?id=in.dreamworld.fillformonline";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body + app_url);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });





    }


    private void sentGroupJoinInvitation(String adminGroupID,String adminUserName, String groupName, String groupPushId,String subGroupName) {

        Log.d("JOINTT", "adminGroupID: "+adminGroupID+"\nsubGroupName: "+subGroupName+
                "\nadminUserName: "+adminUserName+"\ngroupName: "+groupName+"\ngroupPushId: "+groupPushId);

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Discover_Item.this);
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
                                DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("Received_Req").child(adminGroupID);
                                DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child( "Notification" ).child("Submit_Req").child(userID);

                                refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long noofQuesinCategory=snapshot.getChildrenCount()+1;
                                        String pushLong="Joining Reqno_"+noofQuesinCategory;

                                        Class_Group  userAddComment= new Class_Group(dateTimeCC, userName, "req_sent",userID,adminGroupID, userEmail, pushLong, groupName,groupPushId,subGroupName,"Group_JoiningReq");
                                        refjoiningReq.child(pushLong).setValue(userAddComment);
                                        refacceptingReq.child(pushLong).setValue(userAddComment);
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

}