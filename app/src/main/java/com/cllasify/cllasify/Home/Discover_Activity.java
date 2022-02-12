package com.cllasify.cllasify.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cllasify.cllasify.Adaptor.Adaptor_JoinGroupReq;
import com.cllasify.cllasify.Adaptor.Adaptor_SearchGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpClass;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.MySingleton;
import com.cllasify.cllasify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Discover_Activity extends AppCompatActivity {
    ProgressDialog notifyPB;
    Adaptor_SearchGroup showAllGroupAdaptor;
    Adaptor_ShowGrpMember showAllJoinUserAdaptor;
    Adaptor_JoinGroupReq showSubChild_Adaptor;
    List<Class_Group> listAllGroupStatus,list_SubChild,list_AllJoinUser;
    DatabaseReference refSearchShowGroup,refChildGroup;
    RecyclerView rv_AllJoinGroup,rv_AllJoinUser;
    Calendar calenderCC=Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    SearchView esv_groupSearchView;

    EditText edtTitle;
    EditText edtMessage;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA_L8NOeo:APA91bHC6j4xqoLT0XNXVYsCpY9ZQYkspIhpl3LkYlTkUW0Q80s8TzlVEpNq6Wgg6moVOWYbSdFz9qiK0BWBzu6L9CFoVgXZLojQQ_xEvHOfpGxgXgNwnWT1N2aB6peY2jsQq-qGbSnj";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    Button btn_Group,btn_School,btn_Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);


        //initialize the toolbar
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Join Group");
//        toolbar.setNavigationIcon(R.drawable.ic_left_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //open navigation drawer when click navigation back button
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new HomeFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        bottomMenu();
        esv_groupSearchView =findViewById(R.id.esv_groupSearchView);
        rv_AllJoinGroup =findViewById(R.id.rv_AllJoinGroup);
        rv_AllJoinUser =findViewById(R.id.rv_AllJoinUser);

        btn_Users =findViewById(R.id.btn_Users);
        btn_School =findViewById(R.id.btn_School);
        btn_Group =findViewById(R.id.btn_Group);

        notifyPB = new ProgressDialog(this);
        notifyPB.setTitle("Join Group");
        notifyPB.setMessage("Loading All Groups");
        notifyPB.setCanceledOnTouchOutside(true);
        notifyPB.show();

        rv_AllJoinGroup.setLayoutManager(new LinearLayoutManager(this));
        rv_AllJoinUser.setLayoutManager(new LinearLayoutManager(this));

        listAllGroupStatus =new ArrayList<>();
        list_AllJoinUser =new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_SearchGroup(this, listAllGroupStatus);
//        showAllJoinUserAdaptor = new Adaptor_ShowGrpMember(this, list_AllJoinUser);

        rv_AllJoinGroup.setAdapter(showAllGroupAdaptor);
        rv_AllJoinUser.setAdapter(showAllJoinUserAdaptor);


        refSearchShowGroup = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" );

        if (esv_groupSearchView != null) {
            esv_groupSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
//                    searchGroup(newTesxt);
                    return false;
                }
            });
        }


        btn_Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_AllJoinGroup.setVisibility(View.GONE);
                rv_AllJoinUser.setVisibility(View.VISIBLE);
                DatabaseReference refGroupReqCount = FirebaseDatabase.getInstance().getReference().
                        child( "Users" ).child( "Registration" );
                refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount()>0) {
                            showEAllGroupUser("Registration");
                        }else{
                            notifyPB.dismiss();
                            Toast.makeText(view.getContext(), "No Other Group Present", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


        btn_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_AllJoinUser.setVisibility(View.GONE);
                rv_AllJoinGroup.setVisibility(View.VISIBLE);
                DatabaseReference refGroupReqCount = FirebaseDatabase.getInstance().getReference().
                        child( "Groups" ).child( "All_Universal_Group" );
                refGroupReqCount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount()>0) {
                            showAllGroupSearch("All_Universal_Group");
                        }else{
                            notifyPB.dismiss();
                            Toast.makeText(view.getContext(), "No Other Group Present", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        showAllGroupSearch("All_Universal_Group");

    }

    private void showEAllGroupUser(String registration) {

        DatabaseReference refGroupRegistration = FirebaseDatabase.getInstance().getReference().
                child( "Users" ).child( "Registration" );



        showAllJoinUserAdaptor.setOnItemClickListener(new Adaptor_ShowGrpMember.OnItemClickListener() {
            @Override
            public void AddFrndDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentUserInvitation(adminGroupID,adminEmailID,adminUserName, "AddFrnd");
//            showBtmDialogClass();
            }

            @Override
            public void FollowFriendDialog(String adminGroupID, String adminEmailID, String adminUserName, String pushId) {
                sentUserInvitation(adminGroupID,adminEmailID,adminUserName, "FollowFrnd");
            }

            @Override
            public void MemberProfile(String memberUserId,String memberUserName) {
                showBtmDialogUserProfile(memberUserId,memberUserName);

            }

        });

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        String currUserId = mUser.getUid();

        list_AllJoinUser.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    String databaseUserId=class_userDashBoard.getUserId();
                    String groupCategory=class_userDashBoard.getGroupCategory();
//                        if (!currUserId.equals(databaseUserId) && groupCategory.equals("Public")){

                    list_AllJoinUser.add(class_userDashBoard);
                    notifyPB.dismiss();
                    showAllJoinUserAdaptor.notifyDataSetChanged();
//                        }

                } else {
                    Toast.makeText(Discover_Activity.this, "No group yet created", Toast.LENGTH_SHORT).show();
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

        refGroupRegistration.addChildEventListener(childEventListener);

    }

    private void showBtmDialogClass(String adminGroupID,String groupName,String groupPushId) {

        Intent intent = new Intent(getApplicationContext(), Discover_Item.class);
        intent.putExtra("groupName",groupName);
        intent.putExtra("groupPushId",groupPushId);
        Log.d("Grouup", "GroupName: "+groupName+"\n"+"GroupPushId: "+groupPushId);
        startActivity(intent);

/*
        Adaptor_ShowGrpClass showGrpClassList;
        List<Class_Group> listGrpClassList;
        DatabaseReference refGroupClassList;

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(Discover_Activity.this);
//        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_serverview);

        Button btn_Share=bottomSheetDialoglogin.findViewById(R.id.btn_Share);
        ImageView iv_ServerDP=bottomSheetDialoglogin.findViewById(R.id.iv_ServerDP);
        TextView tv_ServerName=bottomSheetDialoglogin.findViewById(R.id.tv_ServerName);
        TextView tv_ServerBio=bottomSheetDialoglogin.findViewById(R.id.tv_ServerBio);
        RecyclerView rv_ShowClass=bottomSheetDialoglogin.findViewById(R.id.rv_ShowClass);


        tv_ServerName.setText(groupName);
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Discover_Activity.this));

        listGrpClassList = new ArrayList<>();
        showGrpClassList = new Adaptor_ShowGrpClass(Discover_Activity.this, listGrpClassList);
        rv_ShowClass.setAdapter(showGrpClassList);

        refGroupClassList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("All_Sub_Group").child(groupPushId);
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
                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
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
*/
    }





    private void showBottmDialogClass(String adminGroupID,String groupName,String groupPushId) {
        Adaptor_ShowGrpClass showGrpClassList;
        List<Class_Group_Names> listGrpClassList;
        DatabaseReference refGroupClassList;

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(Discover_Activity.this);
//        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_serverview);

        Button btn_Share=bottomSheetDialoglogin.findViewById(R.id.btn_Share);
        ImageView iv_ServerDP=bottomSheetDialoglogin.findViewById(R.id.iv_ServerDP);
        TextView tv_ServerName=bottomSheetDialoglogin.findViewById(R.id.tv_ServerName);
        TextView tv_ServerBio=bottomSheetDialoglogin.findViewById(R.id.tv_ServerBio);
        RecyclerView rv_ShowClass=bottomSheetDialoglogin.findViewById(R.id.rv_ShowClass);


        tv_ServerName.setText(groupName);
        rv_ShowClass.setLayoutManager(new LinearLayoutManager(Discover_Activity.this));

        listGrpClassList = new ArrayList<>();
        showGrpClassList = new Adaptor_ShowGrpClass(Discover_Activity.this, listGrpClassList);
        rv_ShowClass.setAdapter(showGrpClassList);

        refGroupClassList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child("All_Sub_Group").child(groupPushId);
        listGrpClassList.clear();

        showGrpClassList.setOnItemClickListener(new Adaptor_ShowGrpClass.OnItemClickListener() {
            @Override
            public void JoinGroupClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String pushId) {
                sentGroupJoinInvitation(adminGroupID,adminUserName,groupName,groupPushId,subGroupName);

            }

            @Override
            public void admissionClass(String adminGroupID, String adminUserName, String groupName, String groupPushId, String subGroupName, String adminEmailId) {

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

    private void showAllGroupSearch(String GroupName) {
//        listGroupSTitle=new ArrayList<>();
//
//        if(GroupName.equals("Registration")){
//
//        } else if(GroupName.equals("All_Universal_Group")){
        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_SearchGroup.OnItemClickListener() {
            @Override
            public void createGroupDialog(String adminGroupID, String groupName,String groupPushId) {
                showBtmDialogClass(adminGroupID,groupName,groupPushId);
            }
        });
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        String currUserId = mUser.getUid();

        listAllGroupStatus.clear();
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
                    Toast.makeText(Discover_Activity.this, "No group yet created", Toast.LENGTH_SHORT).show();
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
    }
    //    }
    private void sentGroupJoinInvitation(String adminGroupID,String adminUserName, String groupName, String groupPushId,String subGroupName) {

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Discover_Activity.this);
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

    private void sentUserInvitation(String adminGroupID,String adminEmailID,String adminUserName, String Code) {

        String req = null,notifyStatus=null;
        if (Code.equals("AddFrnd")){
            req="Friend";
            notifyStatus="Friend_Request";
        }else if (Code.equals("FollowFrnd")){
            req="Follow";
            notifyStatus="Follow_Request";
        }

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Discover_Activity.this);
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
                                        String pushLong="Joining Reqno_"+noofQuesinCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                        Class_Group  userAdd= new Class_Group(dateTimeCC, userName, "req_sent",userID,adminGroupID, userEmail, pushLong, "groupName","groupPushId",finalNotifyStatus);
                                        refjoiningReq.child(pushLong).setValue(userAdd);
                                        refacceptingReq.child(pushLong).setValue(userAdd);
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

    private void sendPushNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        edtTitle.setText("");
                        edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Discover_Activity.this, "Request error", Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(Discover_Activity.this).addToRequestQueue(jsonObjectRequest);
    }


    private void sentInvitation1(String adminGroupID, String groupName,String groupPushId) {
        final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(Discover_Activity.this).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_creategroup1, null);
        TextView tapCancel=dialogView.findViewById(R.id.tv_Cancel);
        TextView tapSubmit=dialogView.findViewById(R.id.tv_Submit);
        RecyclerView rv_JoinGroupReq=dialogView.findViewById(R.id.rv_JoinGroupReq);
        rv_JoinGroupReq.setLayoutManager(new LinearLayoutManager(Discover_Activity.this));

        list_SubChild = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();

        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId);

        showSubChild_Adaptor = new Adaptor_JoinGroupReq(Discover_Activity.this, list_SubChild);
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

    private void showBtmDialogUserProfile(String memberUserId, String memberUserName) {

        BottomSheetDialog btmSheetUserProfile=new BottomSheetDialog(Discover_Activity.this);
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
    private void sentInvitation(String adminGroupID,String adminUserName, String Code) {
        String req = null,notifyStatus=null;
        if (Code.equals("AddFrnd")){
            req="Friend";
            notifyStatus="Friend_Request";
        }else if (Code.equals("FollowFrnd")){
            req="Follow";
            notifyStatus="Follow_Request";
        }
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Discover_Activity.this);
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

    private void bottomMenu() {
//        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int i) {
//                Fragment fragment = null;
//                switch (i) {
//                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag="home";
//                        break;
//                    case R.id.bottom_nav_feed:
////                        fragment = new FeedFragment();
//                        fragment = new FeedFragment();
//                        tag="feed";
//                        break;
//                    case R.id.bottom_nav_notification:
//                        fragment = new NotificationFragment();
//                        tag="notify";
//                        break;
//                    case R.id.bottom_nav_profile:
//                        fragment = new ProfileFragment();
//                        tag="profile";
//                        break;
//                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container,fragment,tag);
//                transaction.addToBackStack(tag);
//                transaction.commit();
//
//            }
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_discover);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag = "home";
                        startActivity(new Intent(Discover_Activity.this, Server_Activity.class));
                        Discover_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_discover:
//                          fragment = new FeedFragment();
//                        fragment = new JoinGroupFragment();
//                        tag = "joingroup";
                        startActivity(new Intent(Discover_Activity.this,Discover_Activity.class));
                        Discover_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_notification:
//                        fragment = new User_Notification_Frag();
//                        tag = "notify";
                        startActivity(new Intent(Discover_Activity.this, Notification_Activity.class));
                        Discover_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_profile:
//                        fragment = new ProfileFragment();
//                        tag = "profile";
                        startActivity(new Intent(Discover_Activity.this, Profile_Activity.class));
                        Discover_Activity.this.overridePendingTransition(0, 0);

                        break;
//                    case R.id.action_Share:
//                        Toast.makeText(landing_Page.this, "Refer and Earn", Toast.LENGTH_LONG).show();
//                        generateLink();
//                        break;
                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container,fragment,tag);
//                transaction.addToBackStack(tag);
//                transaction.commit();


                return true;
            }
        });
    }

}