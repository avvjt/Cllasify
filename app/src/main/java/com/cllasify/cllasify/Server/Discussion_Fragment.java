package com.cllasify.cllasify.Server;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_ShowDoubt;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class Discussion_Fragment extends Fragment {


    RecyclerView rv_DoubtDashData;
    List<Class_Group> list_DoubtDashboard;
    Adaptor_ShowDoubt showDoubtDashAdaptor;
    DatabaseReference refDoubtDashboard;

    FloatingActionButton fab_addDoubtQ;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userName,userEmailID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_discussion, container, false);



        rv_DoubtDashData=view.findViewById(R.id.rv_DoubtDashData);
        fab_addDoubtQ=view.findViewById(R.id.fab_addDoubtQ);
        rv_DoubtDashData.setLayoutManager(new LinearLayoutManager(getContext()));

        list_DoubtDashboard = new ArrayList<>();

        showDoubtDashAdaptor = new Adaptor_ShowDoubt(getContext(), list_DoubtDashboard);
        rv_DoubtDashData.setAdapter(showDoubtDashAdaptor);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userName=currentUser.getDisplayName();
        userEmailID=currentUser.getEmail();

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
        refSaveCurrentData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();

                    if(snapshot.child("GroupPushId").exists() && snapshot.child("GroupPushId").exists() && snapshot.child("subGroupName").exists() ) {
                        String GroupPushId = snapshot.child("GroupPushId").getValue().toString().trim();
                        String SubGroupPushId = snapshot.child("SubGroupPushId").getValue().toString().trim();
                        String subGroupName = snapshot.child("subGroupName").getValue().toString().trim();
                        String groupClassSubjects = snapshot.child("groupClassSubjects").getValue().toString().trim();
                        checkChatDashboard(GroupPushId,SubGroupPushId,subGroupName,groupClassSubjects);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    private void checkChatDashboard(String groupPushId, String subGroupPushId, String subGroupName, String groupClassSubjects) {


        showDoubtDashAdaptor.setOnItemClickListener(new Adaptor_ShowDoubt.OnItemClickListener() {
            @Override
            public void showDoubtChat(String doubtQuestion, String groupPush, String groupClassPush, String groupSubjectPush, String doubtQuestionPush) {

                Fragment fragment=new DoubtFragment();
                Bundle bundle=new Bundle();
                bundle.putString("groupPushId", groupPush);
                bundle.putString("groupClassPushId", groupClassPush);
                bundle.putString("groupClassSubjectPushId", groupSubjectPush);
                bundle.putString("doubtQuestionPushId", doubtQuestionPush);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        refDoubtDashboard = FirebaseDatabase.getInstance().getReference().child("Groups").child("Doubt").child(groupPushId).child(subGroupPushId).child(groupClassSubjects).child("All_Doubt");
//        refDoubtDashboard = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Sub_Group" ).child(groupPushId).child(subGroupPushId);
        ChildEventListener doubtchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_DoubtDashboard.add(class_userDashBoard);
                    showDoubtDashAdaptor.notifyDataSetChanged();

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
        refDoubtDashboard.addChildEventListener(doubtchildEventListener);


        fab_addDoubtQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDoubtBtmDialog(groupPushId,subGroupPushId,groupClassSubjects);
            }
        });
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
//        userPhoto = currentUser.getPhotoUrl();
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
                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId, subGroupPushId, groupClassSubject, addTopic, addDoubt);
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
                            Class_Group userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, groupPushId,subGroupPushId,groupClassSubject, addTopic,addDoubt);
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

}