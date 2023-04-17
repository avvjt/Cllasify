package com.cllasify.cllasify.Activities.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.Attendance_Activity_Teacher;
import com.cllasify.cllasify.Adapters.Adapter_Subject_Assign;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Routine_Structure extends AppCompatActivity {
    String grpPushId, classPushId, className;


    Adapter_Subject_Assign adapter_teacher_assignMonday, adapter_teacher_assignTuesday, adapter_teacher_assignWednesday,
            adapter_teacher_assignThursday, adapter_teacher_assignFriday, adapter_teacher_assignSaturday;

    List<String> subjectDetailsModelListMonday, subjectDetailsModelListTuesday,
            subjectDetailsModelListWednesday, subjectDetailsModelListThursday, subjectDetailsModelListFriday, subjectDetailsModelListSaturday;

    List<String> classStudentListMonday, classStudentListTuesday, classStudentListWednesday,
            classStudentListThursday, classStudentListFriday, classStudentListSaturday;

    List<String> classStudentIDListMonday, classStudentIDListTuesday, classStudentIDListWednesday,
            classStudentIDListThursday, classStudentIDListFriday, classStudentIDListSaturday;


    RecyclerView rv_monday, rv_tuesday, rv_wednesday, rv_thursday, rv_friday, rv_saturday;
    RecyclerView rv_mondayTeacher, rv_tuesdayTeacher, rv_wednesdayTeacher, rv_thursdayTeacher, rv_fridayTeacher, rv_saturdayTeacher;

    TextView tvClassName;
    Button attAP, done;
    ImageButton routine_more;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_structure);

        if (getIntent().hasExtra("groupPushId") && getIntent().hasExtra("classPushId") && getIntent().hasExtra("className")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
            classPushId = getIntent().getStringExtra("classPushId");
            className = getIntent().getStringExtra("className");

        }

        attAP = findViewById(R.id.teachAP);
        done = findViewById(R.id.btn_done);
        routine_more = findViewById(R.id.routine_more);

        attAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Routine_Structure.this, Attendance_Activity_Teacher.class);
                startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Routine_Structure.this, "Processing routine", Toast.LENGTH_SHORT).show();

                List<Class_Routine> clRoutineMonday = adapter_teacher_assignMonday.getClass_routines();
                List<Class_Routine> clRoutineTuesday = adapter_teacher_assignTuesday.getClass_routines();
                List<Class_Routine> clRoutineWednesday = adapter_teacher_assignWednesday.getClass_routines();
                List<Class_Routine> clRoutineThursday = adapter_teacher_assignThursday.getClass_routines();
                List<Class_Routine> clRoutineFriday = adapter_teacher_assignFriday.getClass_routines();
                List<Class_Routine> clRoutineSaturday = adapter_teacher_assignSaturday.getClass_routines();

                //Monday
                for (int i = 0; i < clRoutineMonday.size(); i++) {

                    Class_Routine class_routineMonday = new Class_Routine(clRoutineMonday.get(i).getPeriod(),
                            clRoutineMonday.get(i).getId(), clRoutineMonday.get(i).getSubject(), clRoutineMonday.get(i).getTeacher());

                    String teachUserId = class_routineMonday.getId();
                    int period = class_routineMonday.getPeriod();

                    Log.d("ROUTDATAMonday", "period: " + class_routineMonday.getPeriod());
                    Log.d("ROUTDATAMonday", "id: " + class_routineMonday.getId());
                    Log.d("ROUTDATAMonday", "subject: " + class_routineMonday.getSubject());
                    Log.d("ROUTDATAMonday", "teacher: " + class_routineMonday.getTeacher());

                    DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                            .child("Routine").child(grpPushId).child("schedule");

                    if (teachUserId != null) {
                        dbRoutineStructure.child(teachUserId).child("Monday").child(String.valueOf(period)).setValue(class_routineMonday);
                    }

                }


                //Tuesday
                for (int i = 0; i < clRoutineTuesday.size(); i++) {


                    Class_Routine class_routineTues = new Class_Routine(clRoutineTuesday.get(i).getPeriod(),
                            clRoutineTuesday.get(i).getId(), clRoutineTuesday.get(i).getSubject(), clRoutineTuesday.get(i).getTeacher());

                    Log.d("ROUTDATATuesday", "period: " + class_routineTues.getPeriod());
                    Log.d("ROUTDATATuesday", "id: " + class_routineTues.getId());
                    Log.d("ROUTDATATuesday", "subject: " + class_routineTues.getSubject());
                    Log.d("ROUTDATATuesday", "teacher: " + class_routineTues.getTeacher());

                }

            }
        });

        final PopupMenu dropDownMenu = new PopupMenu(Routine_Structure.this, routine_more);

        final Menu menu = dropDownMenu.getMenu();

        dropDownMenu.getMenuInflater().inflate(R.menu.routine_more, menu);
/*
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });

*/
        routine_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropDownMenu.show();

            }
        });

        //Recycler View Initialization
        rv_monday = findViewById(R.id.rv_monday);
        rv_tuesday = findViewById(R.id.rv_tuesday);
        rv_wednesday = findViewById(R.id.rv_wednesday);
        rv_thursday = findViewById(R.id.rv_thursday);
        rv_friday = findViewById(R.id.rv_friday);
        rv_saturday = findViewById(R.id.rv_saturday);
        tvClassName = findViewById(R.id.clname);

        rv_monday.setNestedScrollingEnabled(false);
        rv_tuesday.setNestedScrollingEnabled(false);
        rv_wednesday.setNestedScrollingEnabled(false);
        rv_thursday.setNestedScrollingEnabled(false);
        rv_friday.setNestedScrollingEnabled(false);
        rv_saturday.setNestedScrollingEnabled(false);


        rv_mondayTeacher = findViewById(R.id.rv_mondayTeacher);
        /*
        rv_tuesdayTeacher = findViewById(R.id.rv_tuesdayTeacher);
        rv_wednesdayTeacher = findViewById(R.id.rv_wednesdayTeacher);
        rv_thursdayTeacher = findViewById(R.id.rv_thursdayTeacher);
        rv_fridayTeacher = findViewById(R.id.rv_fridayTeacher);
        rv_saturdayTeacher = findViewById(R.id.rv_saturdayTeacher);

        rv_mondayTeacher.setNestedScrollingEnabled(false);
        rv_tuesdayTeacher.setNestedScrollingEnabled(false);
        rv_wednesdayTeacher.setNestedScrollingEnabled(false);
        rv_thursdayTeacher.setNestedScrollingEnabled(false);
        rv_fridayTeacher.setNestedScrollingEnabled(false);
        rv_saturdayTeacher.setNestedScrollingEnabled(false);
*/
        tvClassName.setText(className);


        DatabaseReference databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(grpPushId).child(classPushId);
        DatabaseReference databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(grpPushId);


        //Monday
        subjectDetailsModelListMonday = new ArrayList<>();
        classStudentListMonday = new ArrayList<>();
        classStudentIDListMonday = new ArrayList<>();

        adapter_teacher_assignMonday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_monday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListMonday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListMonday.add(object.getSubjectName());

                }


                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListMonday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListMonday.add(object.getUserName());
                            classStudentIDListMonday.add(object.getUserId());
                        }


                        adapter_teacher_assignMonday.setUniPush(classPushId);
                        adapter_teacher_assignMonday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignMonday.setClassStudentList(classStudentListMonday);
                        adapter_teacher_assignMonday.setSubjectDetailsModelList(subjectDetailsModelListMonday);
                        rv_monday.setAdapter(adapter_teacher_assignMonday);
                        adapter_teacher_assignMonday.notifyDataSetChanged();


                        //Put this while opening routine structure button

/*
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int pos = 1; pos < 9; pos++) {

                                    Log.d("DEMOCHK", "onClick: " + pos);

                                    DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                                            .child("Routine").child(grpPushId).child("schedule");

                                    for (int teachList = 0; teachList < classStudentIDListMonday.size(); teachList++) {

                                        Class_Routine class_routineMon = new Class_Routine(pos, classStudentIDListMonday.get(teachList), "", "");


                                        dbRoutineStructure.child(classStudentIDListMonday.get(teachList)).child("Monday").child(String.valueOf(pos)).setValue(class_routineMon);
                                    }

                                }
                            }
                        });
*/

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


        //Tuesday
        subjectDetailsModelListTuesday = new ArrayList<>();
        classStudentListTuesday = new ArrayList<>();
        classStudentIDListTuesday = new ArrayList<>();

        adapter_teacher_assignTuesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_tuesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListTuesday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListTuesday.add(object.getSubjectName());

                }


                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListTuesday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListTuesday.add(object.getUserName());
                            classStudentIDListTuesday.add(object.getUserId());

                        }


                        adapter_teacher_assignTuesday.setClassStudentList(classStudentListTuesday);
                        adapter_teacher_assignTuesday.setUniPush(classPushId);
                        adapter_teacher_assignTuesday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignTuesday.setSubjectDetailsModelList(subjectDetailsModelListTuesday);
                        rv_tuesday.setAdapter(adapter_teacher_assignTuesday);
                        adapter_teacher_assignTuesday.notifyDataSetChanged();
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


        //Wednesday
        subjectDetailsModelListWednesday = new ArrayList<>();
        classStudentListWednesday = new ArrayList<>();
        classStudentIDListWednesday = new ArrayList<>();

        adapter_teacher_assignWednesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_wednesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListWednesday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListWednesday.add(object.getSubjectName());

                }

                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListWednesday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListWednesday.add(object.getUserName());
                            classStudentIDListWednesday.add(object.getUserId());
                        }


                        adapter_teacher_assignWednesday.setClassStudentList(classStudentListWednesday);
                        adapter_teacher_assignWednesday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignWednesday.setUniPush(classPushId);
                        adapter_teacher_assignWednesday.setSubjectDetailsModelList(subjectDetailsModelListWednesday);
                        rv_wednesday.setAdapter(adapter_teacher_assignWednesday);
                        adapter_teacher_assignWednesday.notifyDataSetChanged();
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


        //Thursday
        subjectDetailsModelListThursday = new ArrayList<>();
        classStudentListThursday = new ArrayList<>();
        classStudentIDListThursday = new ArrayList<>();

        adapter_teacher_assignThursday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_thursday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListThursday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListThursday.add(object.getSubjectName());

                }

                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListThursday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListThursday.add(object.getUserName());
                            classStudentIDListThursday.add(object.getUserId());
                        }


                        adapter_teacher_assignThursday.setClassStudentList(classStudentListThursday);
                        adapter_teacher_assignThursday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignThursday.setUniPush(classPushId);
                        adapter_teacher_assignThursday.setSubjectDetailsModelList(subjectDetailsModelListThursday);
                        rv_thursday.setAdapter(adapter_teacher_assignThursday);
                        adapter_teacher_assignThursday.notifyDataSetChanged();
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


        //Friday
        subjectDetailsModelListFriday = new ArrayList<>();
        classStudentListFriday = new ArrayList<>();
        classStudentIDListFriday = new ArrayList<>();

        adapter_teacher_assignFriday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_friday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListFriday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListFriday.add(object.getSubjectName());

                }


                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListFriday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListFriday.add(object.getUserName());
                            classStudentIDListFriday.add(object.getUserId());
                        }


                        adapter_teacher_assignFriday.setClassStudentList(classStudentListFriday);
                        adapter_teacher_assignFriday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignFriday.setUniPush(classPushId);
                        adapter_teacher_assignFriday.setSubjectDetailsModelList(subjectDetailsModelListFriday);
                        rv_friday.setAdapter(adapter_teacher_assignFriday);
                        adapter_teacher_assignFriday.notifyDataSetChanged();
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


        //Saturday
        subjectDetailsModelListSaturday = new ArrayList<>();
        classStudentListSaturday = new ArrayList<>();
        classStudentIDListSaturday = new ArrayList<>();

        adapter_teacher_assignSaturday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_saturday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseReferenceGetStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectDetailsModelListSaturday.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classSubjectData").getChildren()) {
                    Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                    subjectDetailsModelListSaturday.add(object.getSubjectName());

                }


                databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classStudentListSaturday.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                            Class_Student_Details object = dataSnapshot1.getValue(Class_Student_Details.class);
                            classStudentListSaturday.add(object.getUserName());
                            classStudentIDListSaturday.add(object.getUserId());
                        }


                        adapter_teacher_assignSaturday.setClassStudentList(classStudentListSaturday);
                        adapter_teacher_assignSaturday.setClassStudentIDList(classStudentIDListMonday);
                        adapter_teacher_assignSaturday.setUniPush(classPushId);
                        adapter_teacher_assignSaturday.setSaturday(true);
                        adapter_teacher_assignSaturday.setSubjectDetailsModelList(subjectDetailsModelListSaturday);
                        rv_saturday.setAdapter(adapter_teacher_assignSaturday);
                        adapter_teacher_assignSaturday.notifyDataSetChanged();
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

    }
}