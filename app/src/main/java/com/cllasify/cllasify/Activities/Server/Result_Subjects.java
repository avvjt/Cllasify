
package com.cllasify.cllasify.Activities.Server;

import android.app.Dialog;
import android.content.BroadcastReceiver;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Result_Subject;
import com.cllasify.cllasify.ModelClasses.Class_Result;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.Constant;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Result_Subjects extends AppCompatActivity {

    String currUserID;
    RecyclerView rv_ShowSubject;
    TextView tv_SubjectList, studentName;
    ImageButton btn_Back;
    //Subjects
    Adapter_Result_Subject adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;
    private BroadcastReceiver broadcastReceiver;

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

        currUserID = SharePref.getDataFromPref(Constant.USER_ID);

        tv_SubjectList = findViewById(R.id.subjectListText);
        btn_Back = findViewById(R.id.btn_Back);
        studentName = findViewById(R.id.studentName);


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

            studentName.setText(userName + "'s Result");

            //Set Subjects
            rv_ShowSubject = findViewById(R.id.subjectList);
            subjectDetailsModelList = new ArrayList<>();
            adapter_topicList = new Adapter_Result_Subject(Result_Subjects.this);
            adapter_topicList.setStudentUserId(userID);
            adapter_topicList.setSpecPos(Integer.parseInt(position));
            adapter_topicList.setOnItemClickListener(new Adapter_Result_Subject.OnItemClickListener() {
                @Override
                public void clickedSub(String subUniPushId, String subjectName, String position) {
                    setResult(uniGrpPushId, uniClassPushId, userID, userName, subUniPushId, subjectName);
                }

            });
            rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Result_Subjects.this));


        }


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");

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


        }


    }

    private void setResult(String uniGrpPushId, String uniClassPushId, String userID, String userName, String subUniPushId, String subjectName) {

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                String theoryFull = et_theory_full.getText().toString();
                String practicalFull = et_practical_full.getText().toString();
                String theory = et_theory.getText().toString();
                String practical = et_practical.getText().toString();

                int theoryFullInt = Integer.parseInt(theoryFull);
                int practicalFullInt = Integer.parseInt(practicalFull);
                int theoryInt = Integer.parseInt(theory);
                int practicalInt = Integer.parseInt(practical);


                if (TextUtils.isEmpty(theoryFull)) {
                    et_theory_full.setError("Theory full marks cannot be empty");
                } else if (TextUtils.isEmpty(practicalFull)) {
                    et_practical_full.setError("Practical full marks cannot be empty");
                } else if (TextUtils.isEmpty(theory)) {
                    et_theory.setError("Practical full marks cannot be empty");
                } else if (TextUtils.isEmpty(practical)) {
                    et_practical.setError("Practical full marks cannot be empty");
                } else {

                    DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result")
                            .child(uniGrpPushId).child(uniClassPushId).child(userID).child(subUniPushId);

                    Class_Result class_result = new Class_Result(subjectName, userName, theoryFullInt, practicalFullInt,
                            theoryInt, practicalInt);


                    resDb.setValue(class_result);


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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}