package com.cllasify.cllasify.Activities.Server;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.Result_Activity;
import com.cllasify.cllasify.Adapters.Adapter_Result_Subject;
import com.cllasify.cllasify.ModelClasses.Class_Result_Info;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result_Subjects extends AppCompatActivity {

    RecyclerView rv_ShowSubject;
    TextView tv_SubjectList, studentName;
    ImageButton btn_Back;
    //Subjects
    Adapter_Result_Subject adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;
    private BroadcastReceiver broadcastReceiver;

    List<String> marks = new ArrayList<>();
    List<Integer> posss = new ArrayList<>();
    List<Class_Result_Info> class_results = new ArrayList<>();

    Button done;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    private void checkOnlineStatus(String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        setStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userStatus").exists()) {
                    setStatus.child("userStatus").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_subject_result);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        tv_SubjectList = findViewById(R.id.subjectListText);
        btn_Back = findViewById(R.id.btn_Back);
        studentName = findViewById(R.id.studentName);
        done = findViewById(R.id.btn_done);


        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");
            String userID = getIntent().getStringExtra("userID");
            String userName = getIntent().getStringExtra("userName");
            String position = getIntent().getStringExtra("position");


            rv_ShowSubject = findViewById(R.id.subjectList);
            subjectDetailsModelList = new ArrayList<>();
            adapter_topicList = new Adapter_Result_Subject(Result_Subjects.this);
            adapter_topicList.setStudentUserId(userID);
            adapter_topicList.setSpecPos(Integer.parseInt(position));


            DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

            refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {
                        if (snapshot.child("subjectUniPushId").exists() && snapshot.child("uniPushClassId").exists() && snapshot.child("clickedGroupPushId").exists()) {


                            String subjectUniPushId = snapshot.child("subjectUniPushId").getValue().toString().trim();
                            String uniPushClassId = snapshot.child("uniPushClassId").getValue().toString().trim();
                            String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                            DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result").child(groupPushId).child(uniPushClassId).child(userID).child("subjectMarksInfo");

                            resDb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {

                                            Log.d("RESCHKK", "onDataChange: " + dataSnapshot.getKey());


                                            Class_Result_Info class_result = dataSnapshot.getValue(Class_Result_Info.class);
                                            class_results.add(class_result);
                                            adapter_topicList.setClass_results(class_results);
                                            rv_ShowSubject.setAdapter(adapter_topicList);
                                            adapter_topicList.notifyDataSetChanged();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            studentName.setText(userName + "'s Result");


            adapter_topicList.setOnItemClickListener(new Adapter_Result_Subject.OnItemClickListener() {
                @Override
                public void clickedSub(String subUniPushId, String subjectName, String position) {
                    setResult(uniGrpPushId, uniClassPushId, userID, userName, subUniPushId, subjectName, position);
                }

            });
            rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Result_Subjects.this));


            Log.d("UNICLASS", "onCreate: " + uniClassPushId);

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    subjectDetailsModelList.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                        Log.d("CHKSUB25", "onClick: " + dataSnapshot1.getValue());
                        Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                        Log.d("CHKSUB26", "onDataChange: " + object.getSubjectName());
                        subjectDetailsModelList.add(object);

                    }
                    adapter_topicList.setUniPush(uniClassPushId);
                    adapter_topicList.setSubjectDetailsModelList(subjectDetailsModelList);
                    rv_ShowSubject.setAdapter(adapter_topicList);
                    adapter_topicList.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final int[] allTotalMarks = {0};
            final int[] allTotalFullMarks = {0};
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference resDbsetDef = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result").child(uniGrpPushId).child(uniClassPushId).child(userID).child("subjectMarksInfo");


                    resDbsetDef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (snapshot.exists()) {

                                    int total = Integer.parseInt(dataSnapshot.child("totalSubjectMarks").getValue().toString());
                                    allTotalMarks[0] += total;

                                    int fullTotal = Integer.parseInt(dataSnapshot.child("totalFullMarks").getValue().toString());
                                    allTotalFullMarks[0] += fullTotal;

                                }
                            }

                            Toast.makeText(Result_Subjects.this, allTotalFullMarks[0] + "\t" + allTotalMarks[0], Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Intent intent = new Intent(Result_Subjects.this, Result_Activity.class);
                    intent.putExtra("uniGroupPushId", uniGrpPushId);
                    intent.putExtra("uniClassPushId", uniClassPushId);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);
                    intent.putExtra("position", position);
                    startActivity(intent);

                }
            });

        }


    }

    private void setResult(String uniGrpPushId, String uniClassPushId, String userID, String userName, String subUniPushId, String subjectName, String position) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.set_result);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        EditText et_theory_full = dialog.findViewById(R.id.et_theory_full);
        EditText et_practical_full = dialog.findViewById(R.id.et_practical_full);
        EditText et_theory = dialog.findViewById(R.id.et_theory);
        EditText et_practical = dialog.findViewById(R.id.et_practical);
        Button btn_done = dialog.findViewById(R.id.btn_done);

        DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result").child(uniGrpPushId).child(uniClassPushId).child(userID).child("subjectMarksInfo").child(subUniPushId);


        resDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Log.d("SUBCHK", "onDataChange: " + snapshot.getKey());

                    String fullTheory = snapshot.child("theoryFullMarks").getValue().toString();
                    String practicalFullMarks = snapshot.child("practicalFullMarks").getValue().toString();
                    String theoryMarks = snapshot.child("theoryMarks").getValue().toString();
                    String practicalMarks = snapshot.child("practicalMarks").getValue().toString();

                    et_theory_full.setText(fullTheory);
                    et_practical_full.setText(practicalFullMarks);
                    et_theory.setText(theoryMarks);
                    et_practical.setText(practicalMarks);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String theoryFull = et_theory_full.getText().toString();
                String practicalFull = et_practical_full.getText().toString();
                String theory = et_theory.getText().toString();
                String practical = et_practical.getText().toString();


                if (TextUtils.isEmpty(theoryFull)) {
                    et_theory_full.setError("Theory full marks cannot be empty");
                } else if (TextUtils.isEmpty(practicalFull)) {
                    et_practical_full.setError("Practical full marks cannot be empty");
                } else if (TextUtils.isEmpty(theory)) {
                    et_theory.setError("Practical full marks cannot be empty");
                } else if (TextUtils.isEmpty(practical)) {
                    et_practical.setError("Practical full marks cannot be empty");
                } else {

                    int theoryFullInt = Integer.parseInt(theoryFull);
                    int practicalFullInt = Integer.parseInt(practicalFull);
                    int theoryInt = Integer.parseInt(theory);
                    int practicalInt = Integer.parseInt(practical);

                    int totalSubMarks = theoryInt + practicalInt;
                    int totalFullMarks = theoryFullInt + practicalFullInt;

                    DatabaseReference resDbsetDef = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result").child(uniGrpPushId).child(uniClassPushId).child(userID).child("subjectMarksInfo");

                    resDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            int th = Integer.parseInt(snapshot.child("theoryFullMarks").getValue().toString());


                            if (th == 0) {

                                resDbsetDef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (snapshot.exists()) {

                                                if (theoryFullInt > 0) {
                                                    Class_Result_Info class_result_info = new Class_Result_Info(theoryFullInt, practicalFullInt, 0, 0, 0, 0, subjectName, "");
                                                    resDbsetDef.child(Objects.requireNonNull(dataSnapshot.getKey())).setValue(class_result_info);

                                                    Class_Result_Info class_result_infos = new Class_Result_Info(theoryFullInt, practicalFullInt, theoryInt, practicalInt, totalSubMarks, totalFullMarks, subjectName, "");
                                                    resDb.setValue(class_result_infos);

                                                    class_results.add(class_result_info);
                                                    adapter_topicList.setClass_results(class_results);
                                                    rv_ShowSubject.setAdapter(adapter_topicList);
                                                    adapter_topicList.notifyDataSetChanged();

                                                    recreate();
                                                    dialog.dismiss();

                                                } else {
                                                    Toast.makeText(Result_Subjects.this, "Set a valid mark!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {


                                Class_Result_Info class_result_info = new Class_Result_Info(theoryFullInt, practicalFullInt, theoryInt, practicalInt, totalSubMarks, totalFullMarks, subjectName, "");
                                resDbsetDef.child(subUniPushId).setValue(class_result_info);
                                class_results.add(class_result_info);
                                adapter_topicList.setClass_results(class_results);
                                rv_ShowSubject.setAdapter(adapter_topicList);
                                adapter_topicList.notifyDataSetChanged();

                                recreate();
                                dialog.dismiss();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}