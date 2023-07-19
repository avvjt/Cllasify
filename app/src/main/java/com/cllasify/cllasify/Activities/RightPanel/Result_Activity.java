package com.cllasify.cllasify.Activities.RightPanel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adapter_Result_Per_Subject;
import com.cllasify.cllasify.ModelClasses.Class_Result_Info;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Result_Activity extends AppCompatActivity {


    RecyclerView rv_ShowSubject;

    TextView school_name, marks_by, student_name, roll_numb, class_numb, result_date, percentage, gradeTV;

    Button generate_pdf;
    //Subjects
    Adapter_Result_Per_Subject adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Result_Info> class_results = new ArrayList<>();
    private PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;
    CircleImageView userProfilePic;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver(this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);

        Result_Activity result_activity = Result_Activity.this;

        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");
            String userID = getIntent().getStringExtra("userID");
            String userName = getIntent().getStringExtra("userName");
            String position = getIntent().getStringExtra("position");
            String allTotalMarks = getIntent().getStringExtra("allTotalMarks");
            String allTotalFullMarks = getIntent().getStringExtra("allTotalFullMarks");
            String rollNumb = getIntent().getStringExtra("rollNumber");

            int percentageMarks = (Integer.parseInt(allTotalMarks) * 100) / Integer.parseInt(allTotalFullMarks);


//            Log.d("RESPERC", "onCreate: " + percentageMarks));

            rv_ShowSubject = findViewById(R.id.subjectResultList);
            subjectDetailsModelList = new ArrayList<>();
            adapter_topicList = new Adapter_Result_Per_Subject(Result_Activity.this);
            adapter_topicList.setStudentUserId(userID);
            adapter_topicList.setSpecPos(Integer.parseInt(position));

            userProfilePic = findViewById(R.id.userProfilePic);
            school_name = findViewById(R.id.school_name);
            marks_by = findViewById(R.id.marks_by);
            student_name = findViewById(R.id.student_name);
            roll_numb = findViewById(R.id.roll_numb);
            class_numb = findViewById(R.id.class_numb);
            result_date = findViewById(R.id.result_date);
            percentage = findViewById(R.id.percentage);
            gradeTV = findViewById(R.id.grade);
            generate_pdf = findViewById(R.id.btn_generate_pdf);

            marks_by.setText(allTotalMarks + "/" + allTotalFullMarks);
            student_name.setText(userName);

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            DatabaseReference refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
            refUserStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (snapshot.child("profilePic").exists()) {
                        String profilePic = snapshot.child("profilePic").getValue().toString();

                        if (profilePic != null) {
                            if (!(Result_Activity.this).isFinishing()) {
                                Glide.with(Result_Activity.this).load(profilePic).into(userProfilePic);
                            }
                        }

                    } else {
                        if (!(Result_Activity.this).isFinishing()) {
                            Glide.with(Result_Activity.this).load(R.drawable.maharaji).into(userProfilePic);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            result_date.setText(formattedDate);
            roll_numb.setText(rollNumb);
            percentage.setText(percentageMarks + "%");

            String grade = "";


            if (percentageMarks < 30) {
                grade = "F";
            } else if (percentageMarks >= 30 && percentageMarks <= 39) {
                grade = "P";
            } else if (percentageMarks >= 40 && percentageMarks <= 49) {
                grade = "C";
            } else if (percentageMarks >= 50 && percentageMarks <= 59) {
                grade = "B";
            } else if (percentageMarks >= 60 && percentageMarks <= 69) {
                grade = "B+";
            } else if (percentageMarks >= 70 && percentageMarks <= 79) {
                grade = "A";
            } else if (percentageMarks >= 80 && percentageMarks <= 89) {
                grade = "A+";
            } else if (percentageMarks >= 90 && percentageMarks <= 100) {
                grade = "O";
            }

            if (gradeTV.equals("F")) {
                gradeTV.setTextColor(Color.RED);
            }

            gradeTV.setText(" | " + grade);

            DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

            DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result").child(uniGrpPushId).child(uniClassPushId).child(userID).child("subjectMarksInfo");

            resDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {

                            Log.d("RESCHKK", "onDataChange: " + dataSnapshot.getKey());


                            Class_Result_Info class_result = dataSnapshot.getValue(Class_Result_Info.class);
                            class_results.add(class_result);

                        }

                    }
                    adapter_topicList.setClass_results(class_results);
                    rv_ShowSubject.setAdapter(adapter_topicList);
                    adapter_topicList.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            rv_ShowSubject.setLayoutManager(new LinearLayoutManager(Result_Activity.this));


            Log.d("UNICLASS", "onCreate: " + uniClassPushId);

            DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(uniGrpPushId).child(uniClassPushId);
            databaseReferenceGetStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String groupPushID = snapshot.child("groupPushId").getValue().toString();

                    String[] separated = groupPushID.split("_");


                    String schoolName = separated[separated.length - 1];

                    school_name.setText(schoolName);
                    class_numb.setText(snapshot.child("className").getValue().toString());


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

            Toast.makeText(result_activity, "Generating result please wait...", Toast.LENGTH_SHORT).show();

            if (getIntent().getStringExtra("generateResult").equals("true")) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (android.os.Build.VERSION.SDK_INT > 31) {

                            PdfGenerator.getBuilder().setContext(Result_Activity.this)
                                    .fromViewIDSource()
                                    .fromViewID(result_activity, R.id.student_result)
                                    .setFileName(userName + "'s Result")
                                    .savePDFSharedStorage(xmlToPDFLifecycleObserver)
                                    .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                                    .build(new PdfGeneratorListener() {
                                        @Override
                                        public void onFailure(FailureResponse failureResponse) {
                                            super.onFailure(failureResponse);

                                        }

                                        @Override
                                        public void showLog(String log) {
                                            super.showLog(log);

                                        }

                                        @Override
                                        public void onStartPDFGeneration() {

                                        }

                                        @Override
                                        public void onFinishPDFGeneration() {

                                        }

                                        @Override
                                        public void onSuccess(SuccessResponse response) {
                                            super.onSuccess(response);
                                        }
                                    });

                        } else {

                            PdfGenerator.getBuilder().setContext(Result_Activity.this)
                                    .fromViewIDSource()
                                    .fromViewID(result_activity, R.id.student_result)
                                    .setFileName(userName + "'s Result")
                                    .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                                    .build(new PdfGeneratorListener() {
                                        @Override
                                        public void onFailure(FailureResponse failureResponse) {
                                            super.onFailure(failureResponse);

                                        }

                                        @Override
                                        public void showLog(String log) {
                                            super.showLog(log);

                                        }

                                        @Override
                                        public void onStartPDFGeneration() {

                                        }

                                        @Override
                                        public void onFinishPDFGeneration() {

                                        }

                                        @Override
                                        public void onSuccess(SuccessResponse response) {
                                            super.onSuccess(response);
                                        }
                                    });

                        }


                    }
                }, 1000);


            }


        }


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Result_Activity.this, Server_Activity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}