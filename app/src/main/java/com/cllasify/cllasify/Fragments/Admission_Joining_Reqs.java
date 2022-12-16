package com.cllasify.cllasify.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.WebViewActivity;
import com.cllasify.cllasify.Adapters.Adaptor_Notify;
import com.cllasify.cllasify.ModelClasses.Class_Admission;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Admission_Joining_Reqs extends Fragment {

    Adaptor_Notify showAllGroupAdaptor;
    List<Class_Group> listGroupSTitle;
    DatabaseReference refSearchShowGroup;
    RecyclerView rv_StudentJoiningNotification;
    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
    Class_Group userSubsGroupClass;

    final String[] groupPushId = new String[1];
    final String[] uniPushClassId = new String[1];
    final String[] subjectUniPushId = new String[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student__joining__reqs, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();

        DatabaseReference chkJoinedORAddGRP = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        chkJoinedORAddGRP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("subjectUniPushId").exists()) {

                    subjectUniPushId[0] = snapshot.child("subjectUniPushId").getValue().toString().trim();
                }
                if (snapshot.child("clickedGroupPushId").exists()) {
                    groupPushId[0] = snapshot.child("clickedGroupPushId").getValue().toString().trim();
                }
                if (snapshot.child("uniPushClassId").exists()) {

                    uniPushClassId[0] = snapshot.child("uniPushClassId").getValue().toString().trim();
                }
                refSearchShowGroup = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("All_GRPs").child(groupPushId[0]).child(uniPushClassId[0]).child("groupAdmissionReqs");

                refSearchShowGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            showEAllGroupSearchRV(uniPushClassId[0]);
                        } else {
                            //  Toast.makeText(getActivity(), "No group Notifications", Toast.LENGTH_SHORT).show();
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

        rv_StudentJoiningNotification = view.findViewById(R.id.rv_StudentJoiningNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        rv_StudentJoiningNotification.setLayoutManager(linearLayoutManager);
        listGroupSTitle = new ArrayList<>();
        showAllGroupAdaptor = new Adaptor_Notify(getActivity(), listGroupSTitle);
        rv_StudentJoiningNotification.setAdapter(showAllGroupAdaptor);


        return view;

    }

    private void showEAllGroupSearchRV(String subGroupPushId) {
        showAllGroupAdaptor.setOnItemClickListener(new Adaptor_Notify.OnItemClickListener() {

            @Override
            public void createGroupDialog(String adminGroupID, String groupName) {

            }

            @Override
            public void rejectNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId) {
                if (notifyCategory.equals("Group_JoiningReq")) {
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, groupName, classPushId, "false", "Off");
                    refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                    DatabaseReference refrejuserNotify = FirebaseDatabase.getInstance().getReference().
                            child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupAdmissionReqs").child(notPushId);
                    DatabaseReference refrejadminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);
                    refrejuserNotify.child("grpJoiningStatus").setValue("Reject");
                    refrejadminNotify.child("grpJoiningStatus").setValue("Reject");

//                    Toast.makeText(getActivity(), "Group request from " + userName + "has been Rejected", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void acceptNotify(String reqUserID, String currUserId, String groupName, String userName, String classPushId, String groupPushId, String notifyCategory, String notPushId, String classUni) {

                if (notifyCategory.equals("Group_JoiningReq")) {
                    DatabaseReference refSubs_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Sub_Group").child(groupPushId).child(classPushId).child("SubGroup_SubsList");
                    DatabaseReference refAll_J_Group = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("User_Subscribed_Groups");


                    DatabaseReference refAllGRPs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId);
                    refAllGRPs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            refAllGRPs.child(classUni).child("classStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Class_Student_Details class_student_details = new Class_Student_Details(false, reqUserID, userName, "unpaid", "unpaid");
                                    refAllGRPs.child(classUni).child("classStudentList").child(reqUserID).setValue(class_student_details);
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


                    refSubs_J_Group.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long noofGroupinCategory = snapshot.getChildrenCount() + 1;
                            String push = "joining_No" + noofGroupinCategory;

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());
                            userSubsGroupClass = new Class_Group(dateTimeCC, userName, reqUserID, currUserId, noofGroupinCategory, groupName, classPushId, "Class Member", "On");
                            refSubs_J_Group.child(reqUserID).setValue(userSubsGroupClass);
                            refAll_J_Group.child(reqUserID).setValue(userSubsGroupClass);

                            DatabaseReference refAccUserNotify = FirebaseDatabase.getInstance().getReference().
                                    child("Groups").child("All_GRPs").child(groupPushId).child(subGroupPushId).child("groupAdmissionReqs").child(notPushId);
                            DatabaseReference refAccAdminNotify = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(reqUserID).child(notPushId);

                            refAccUserNotify.child("grpJoiningStatus").setValue("Approve");
                            refAccAdminNotify.child("grpJoiningStatus").setValue("Approve");

//                            Toast.makeText(getActivity(), "Group request from " + userName + "has been Approved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }


            }

            @Override
            public void reqClick(String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid, String grpJoiningStatus) {

            }


            @Override
            public void reqAdmissionClick(Class_Admission class_admission, String reqUserID, String currUserId, String groupName, String userName, String notPushId, String classUniPushId, String groupPushId, String notifyReq, String classPushid, String pushid) {

                Dialog bottomSheetDialog = new Dialog(getActivity());
                bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.setContentView(R.layout.bottomsheet_admission_accept_reject);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btn_accept = bottomSheetDialog.findViewById(R.id.btn_accept);
                Button btn_reject = bottomSheetDialog.findViewById(R.id.btn_reject);
                LinearLayout btn_pdf = bottomSheetDialog.findViewById(R.id.document);


                TextView name, dob, father_name, mother_name, address, phone, religion, cast, date;

                date = bottomSheetDialog.findViewById(R.id.date);
                name = bottomSheetDialog.findViewById(R.id.name);
                dob = bottomSheetDialog.findViewById(R.id.dob);
                father_name = bottomSheetDialog.findViewById(R.id.father_name);
                mother_name = bottomSheetDialog.findViewById(R.id.mother_name);
                address = bottomSheetDialog.findViewById(R.id.address);
                phone = bottomSheetDialog.findViewById(R.id.phone);
                religion = bottomSheetDialog.findViewById(R.id.religion);
                cast = bottomSheetDialog.findViewById(R.id.cast);

                date.setText(class_admission.getDate());
                name.setText(class_admission.getName());
                dob.setText(class_admission.getDob());
                father_name.setText(class_admission.getFather());
                mother_name.setText(class_admission.getMother());
                address.setText(class_admission.getAddress());
                phone.setText(class_admission.getPhone());
                religion.setText(class_admission.getReligion());
                cast.setText(class_admission.getCast());

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (reqUserID != null && groupPushId != null && classUniPushId != null) {

                            DatabaseReference grpJoiningReqs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId).child("groupAdmissionReqs").child(notPushId);
                            grpJoiningReqs.child("grpJoiningStatus").setValue("Approve");
                        }

                        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(reqUserID);
                        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(currUserId);

                        refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                long noOfQuesInCategory = snapshot.getChildrenCount() + 1;
                                String push = "Admission_Response_" + noOfQuesInCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                Class_Group userAdd = new Class_Group(dateTimeCC, userName, "req_sent", currUserId, reqUserID, "", "AdmissionResponse", "", "", "AdmissionAccepted");
                                refjoiningReq.child(push).setValue(userAdd);
                                refacceptingReq.child(push).setValue(userAdd);

                                btn_accept.setText("Sent");
                                btn_accept.setEnabled(false);

                                bottomSheetDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                    }
                });

                btn_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (reqUserID != null && groupPushId != null && classUniPushId != null) {
                            DatabaseReference grpJoiningReqs = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classUniPushId).child("groupAdmissionReqs").child(notPushId);
                            grpJoiningReqs.child("grpJoiningStatus").setValue("Reject");
                        }


                        DatabaseReference refjoiningReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Received_Req").child(reqUserID);
                        DatabaseReference refacceptingReq = FirebaseDatabase.getInstance().getReference().child("Notification").child("Submit_Req").child(currUserId);

                        refjoiningReq.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                long noOfQuesInCategory = snapshot.getChildrenCount() + 1;
                                String push = "Admission_Response_" + noOfQuesInCategory;
//                                        Class_Group  userAddComment= new Class_Group(dateTimeCC,userName,userID,adminGroupID, userEmail,adminEmailID,"req_sent", finalNotifyStatus);
                                Class_Group userAdd = new Class_Group(dateTimeCC, userName, "req_sent", currUserId, reqUserID, "", "AdmissionResponse", "", "", "AdmissionRejected");
                                refjoiningReq.child(push).setValue(userAdd);
                                refacceptingReq.child(push).setValue(userAdd);

                                btn_reject.setText("Sent");
                                btn_reject.setEnabled(false);

                                bottomSheetDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                });

                btn_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("pdfUrl", class_admission.getFileUrl());
                        startActivity(intent);

/*
                        WebView_Fragment webView_fragment = new WebView_Fragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("path", class_admission.getFileUrl());
                        bundle.putString("docName", "Result.pdf");
                        bundle.putString("type", "activity");
                        webView_fragment.setArguments(bundle);
                        getFragmentManager().getBackStackEntryCount();
                        transaction.replace(R.id.friendNoti, webView_fragment, "FirstFragment");
                        transaction.commit();
                        */
                    }
                });


                bottomSheetDialog.show();
                bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);


            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    assert class_userDashBoard != null;
                    String groupjoinStatus = class_userDashBoard.getGrpJoiningStatus();
//                    Toast.makeText(getActivity(), "status"+groupjoinStatus, Toast.LENGTH_SHORT).show();
//                    if ((!groupjoinStatus.equals("Approve")) && (!groupjoinStatus.equals("Reject"))) {
                    listGroupSTitle.add(class_userDashBoard);
                    showAllGroupAdaptor.notifyDataSetChanged();
//                    }

                } else {
//                    Toast.makeText(getActivity(), "No Group request Pending", Toast.LENGTH_SHORT).show();
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

        refSearchShowGroup.addChildEventListener(childEventListener);

    }

}