package com.cllasify.cllasify.Activities;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Result_Per_Subject;
import com.cllasify.cllasify.ModelClasses.Class_Result;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Result_Activity extends AppCompatActivity {


    String currUserID;
    RecyclerView rv_ShowSubject;
    TextView tv_SubjectList, studentName;
    ImageButton btn_Back;
    //Subjects
    Adapter_Result_Per_Subject adapter_topicList;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<String> marks = new ArrayList<>();
    List<Integer> posss = new ArrayList<>();
    List<Class_Result> class_results = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;

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


            rv_ShowSubject = findViewById(R.id.subjectResultList);
            subjectDetailsModelList = new ArrayList<>();
            studentName = findViewById(R.id.studentName);
            adapter_topicList = new Adapter_Result_Per_Subject(Result_Activity.this);
            adapter_topicList.setStudentUserId(userID);
            adapter_topicList.setSpecPos(Integer.parseInt(position));

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

                            DatabaseReference resDb = FirebaseDatabase.getInstance().getReference().child("Groups").child("Result")
                                    .child(groupPushId).child(uniPushClassId).child(userID);

                            resDb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.exists()) {

                                            Log.d("RESCHKK", "onDataChange: " + dataSnapshot.getKey());


                                            Class_Result class_result = dataSnapshot.getValue(Class_Result.class);
                                            class_results.add(class_result);
                                            adapter_topicList.setClass_results(class_results);
                                            rv_ShowSubject.setAdapter(adapter_topicList);
                                            adapter_topicList.notifyDataSetChanged();
                                            /*
                                            int fullTheory = Integer.parseInt(dataSnapshot.child("theoryFullMarks").getValue().toString());
                                            int practicalFullMarks = Integer.parseInt(dataSnapshot.child("practicalFullMarks").getValue().toString());
                                            int theoryMarks = Integer.parseInt(dataSnapshot.child("theoryMarks").getValue().toString());
                                            int practicalMarks = Integer.parseInt(dataSnapshot.child("practicalMarks").getValue().toString());
                                            int poss = Integer.parseInt(dataSnapshot.child("position").getValue().toString());

                                            int fullTheoryPractical = fullTheory + practicalFullMarks;
                                            int theoryPractical = theoryMarks + practicalMarks;

                                            Log.d("RESCHKK", "onDataChange: " + theoryPractical + "/" + fullTheoryPractical + "pos: " + poss);

                                            marks.add(theoryPractical + "/" + fullTheoryPractical);
                                            posss.add(poss);
                                            adapter_topicList.setMarks(marks);

                                            rv_ShowSubject.setAdapter(adapter_topicList);
                                            adapter_topicList.notifyDataSetChanged();
*/
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            /*
                            resDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        int fullTheory = Integer.parseInt(snapshot.child("theoryFullMarks").getValue().toString());
                                        int practicalFullMarks = Integer.parseInt(snapshot.child("practicalFullMarks").getValue().toString());
                                        int theoryMarks = Integer.parseInt(snapshot.child("theoryMarks").getValue().toString());
                                        int practicalMarks = Integer.parseInt(snapshot.child("practicalMarks").getValue().toString());

                                        int fullTheoryPractical = fullTheory + practicalFullMarks;
                                        int theoryPractical = theoryMarks + practicalMarks;

                                        Log.d("RESCHKK", "onDataChange: " + theoryPractical + "/" + fullTheoryPractical + " Pos: " + Integer.parseInt(position));

                                        marks.add(Integer.parseInt(position), String.valueOf(fullTheoryPractical));

                                        adapter_topicList.setMarks(marks);

                                        rv_ShowSubject.setAdapter(adapter_topicList);
                                        adapter_topicList.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
*/

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
}