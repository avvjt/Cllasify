package com.cllasify.cllasify.Activities.RightPanel;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpMember_Fees;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.Constant;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fees_Structure extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    String currUserID;
    RecyclerView rv_showStudents;
    TextView tv_studentList;
    Button btn_setFees, btn_setAdmissionFees;
    ImageButton btn_Back;
    DatabaseReference databaseReference;

    //Students
    Adaptor_ShowGrpMember_Fees showGrpMemberList;
    List<Class_Student_Details> listGrpMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_structure);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

//        btn_Back = findViewById(R.id.btn_Back);

/*
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */

        btn_setFees = findViewById(R.id.btn_setFees);
        btn_setAdmissionFees = findViewById(R.id.btn_setAdmissionFees);

        tv_studentList = findViewById(R.id.studentListText);
        rv_showStudents = findViewById(R.id.studentList);
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMember_Fees(Fees_Structure.this);
        rv_showStudents.setLayoutManager(new LinearLayoutManager(Fees_Structure.this));


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("classFees").exists()) {
                        if (!(snapshot.child("classFees").getValue().toString().isEmpty())) {
                            btn_setFees.setText(snapshot.child("classFees").getValue().toString());

                            btn_setFees.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog dialog = new Dialog(Fees_Structure.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.bottomsheet_fees);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
                                    EditText et_NewFees = dialog.findViewById(R.id.et_NewFees);
                                    Button btn_Submit = dialog.findViewById(R.id.btn_submit);
                                    et_NewFees.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    et_NewFees.setHint(snapshot.child("classFees").getValue().toString());

                                    btn_Submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String newFees = et_NewFees.getText().toString().trim();
                                            if (newFees.isEmpty() || newFees.equals("0")) {
                                                et_NewFees.setError("Please enter a valid value");

                                            } else {
                                                et_NewFees.getText().toString().trim();
                                                databaseReference.child("classFees").setValue("₹" + newFees);
                                                dialog.dismiss();
                                            }

                                        }
                                    });
                                    btn_Cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                                    moveTaskToBack(false);
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("admissionFees").exists()) {
                        if (!(snapshot.child("admissionFees").getValue().toString().isEmpty())) {
                            btn_setAdmissionFees.setText(snapshot.child("admissionFees").getValue().toString());

                            btn_setAdmissionFees.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog dialog = new Dialog(Fees_Structure.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(true);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.bottomsheet_admission_fees);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
                                    EditText et_NewFees = dialog.findViewById(R.id.et_NewFees);
                                    Button btn_Submit = dialog.findViewById(R.id.btn_submit);
                                    et_NewFees.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    et_NewFees.setHint(snapshot.child("admissionFees").getValue().toString());

                                    btn_Submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String newFees = et_NewFees.getText().toString().trim();
                                            if (newFees.isEmpty() || newFees.equals("0")) {
                                                et_NewFees.setError("Please enter a valid value");

                                            } else {
                                                et_NewFees.getText().toString().trim();
                                                databaseReference.child("admissionFees").setValue("₹" + newFees);
                                                dialog.dismiss();
                                            }

                                        }
                                    });
                                    btn_Cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                                    moveTaskToBack(false);
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Log.d("UNICLASS", "onCreate: " + uniClassPushId);

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    listGrpMemberList.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child("classStudentList").getChildren()) {
                        Log.d("CHKINGSTUD", "onClick: " + dataSnapshot1.getValue());
                        Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                        Log.d("CHKINGSTUD", "onDataChange: " + object.getUserId());
                        listGrpMemberList.add(object);

                    }

                    showGrpMemberList.setClassStudents(listGrpMemberList);
                    rv_showStudents.setAdapter(showGrpMemberList);
                    showGrpMemberList.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            showGrpMemberList.setOnItemClickListener(new Adaptor_ShowGrpMember_Fees.OnItemClickListener() {
                @Override
                public void clickedStatus(String studentUniPush) {

                    final Dialog dialog = new Dialog(Fees_Structure.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.bottomsheet_offline_fees);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                    Button btn_paidOffline = dialog.findViewById(R.id.btn_paidOffline);

                    btn_paidOffline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child("classStudentList").child(studentUniPush).child("annualFees").setValue("Paid Offline");
                            dialog.dismiss();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                    moveTaskToBack(false);
                }

            });


        }


    }
}