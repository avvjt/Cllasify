package com.cllasify.cllasify.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Result_Activity extends AppCompatActivity {


    RecyclerView rv_ShowSubject;
    TextView studentName;

    TextView school_name, marks_by, student_name, roll_numb, class_numb, result_date;

    Button generate_pdf;
    //Subjects
    Adapter_Result_Per_Subject adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Result_Info> class_results = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        if (getIntent().hasExtra("uniGroupPushId") && getIntent().hasExtra("uniClassPushId")) {

            String uniGrpPushId = getIntent().getStringExtra("uniGroupPushId");
            String uniClassPushId = getIntent().getStringExtra("uniClassPushId");
            String userID = getIntent().getStringExtra("userID");
            String userName = getIntent().getStringExtra("userName");
            String position = getIntent().getStringExtra("position");
            String allTotalMarks = getIntent().getStringExtra("allTotalMarks");
            String allTotalFullMarks = getIntent().getStringExtra("allTotalFullMarks");
            String rollNumb = getIntent().getStringExtra("rollNumber");


            rv_ShowSubject = findViewById(R.id.subjectResultList);
            subjectDetailsModelList = new ArrayList<>();
            studentName = findViewById(R.id.studentName);
            adapter_topicList = new Adapter_Result_Per_Subject(Result_Activity.this);
            adapter_topicList.setStudentUserId(userID);
            adapter_topicList.setSpecPos(Integer.parseInt(position));

            school_name = findViewById(R.id.school_name);
            marks_by = findViewById(R.id.marks_by);
            student_name = findViewById(R.id.student_name);
            roll_numb = findViewById(R.id.roll_numb);
            class_numb = findViewById(R.id.class_numb);
            result_date = findViewById(R.id.result_date);


            marks_by.setText("Marks: " + allTotalMarks + "/" + allTotalFullMarks);
            student_name.setText("Name: " + userName);

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            result_date.setText("Date: " + formattedDate);
            roll_numb.setText("Roll number: " + rollNumb);
            studentName.setText(userName + "'s Result");

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
                    class_numb.setText("Class: " + snapshot.child("className").getValue().toString());


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

        Result_Activity result_activity = Result_Activity.this;

        generate_pdf = findViewById(R.id.btn_generate_pdf);
        generate_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfGenerator.getBuilder()
                        .setContext(Result_Activity.this)
                        .fromViewIDSource()
                        .fromViewID(result_activity, R.id.student_result)
                        .setFileName("Allotment_PaperPDF")
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
        });

    }
}