package com.cllasify.cllasify.Activities.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.cllasify.cllasify.Activities.Server.Server_Activity;
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
import java.util.Arrays;
import java.util.List;

public class Routine_Structure extends AppCompatActivity {
    String grpPushId, classPushId, className;


    Adapter_Subject_Assign adapter_teacher_assignMonday, adapter_teacher_assignTuesday, adapter_teacher_assignWednesday, adapter_teacher_assignThursday, adapter_teacher_assignFriday, adapter_teacher_assignSaturday;

    Adapter_Subject_Assign adapter_all_teacher_assignMonday, adapter_all_teacher_assignTuesday, adapter_all_teacher_assignWednesday, adapter_all_teacher_assignThursday, adapter_all_teacher_assignFriday, adapter_all_teacher_assignSaturday;

    List<String> subjectDetailsModelListMonday, subjectDetailsModelListTuesday, subjectDetailsModelListWednesday, subjectDetailsModelListThursday, subjectDetailsModelListFriday, subjectDetailsModelListSaturday;

    List<String> classStudentListMonday, classStudentListTuesday, classStudentListWednesday, classStudentListThursday, classStudentListFriday, classStudentListSaturday;

    List<Class_Routine> classDataListMonday, classDataListTuesday, classDataListWednesday, classDataListThursday, classDataListFriday, classDataListSaturday;

    List<String> classStudentIDListMonday, classStudentIDListTuesday, classStudentIDListWednesday, classStudentIDListThursday, classStudentIDListFriday, classStudentIDListSaturday;

    List<String> weekdays;

    RecyclerView rv_monday, rv_tuesday, rv_wednesday, rv_thursday, rv_friday, rv_saturday;
    RecyclerView rv_all_monday, rv_all_tuesday, rv_all_wednesday, rv_all_thursday, rv_all_friday, rv_all_saturday;
    RecyclerView rv_mondayTeacher, rv_tuesdayTeacher, rv_wednesdayTeacher, rv_thursdayTeacher, rv_fridayTeacher, rv_saturdayTeacher;

    TextView tvClassName;
    Button done;
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

        done = findViewById(R.id.btn_done);
        routine_more = findViewById(R.id.routine_more);

        List<String> weekList = Arrays.asList(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        DatabaseReference databaseAllRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child("Routine").child(grpPushId).child("allSchedule").child(classPushId);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Routine_Structure.this, "Processing routine", Toast.LENGTH_SHORT).show();

                List<Class_Routine> clRoutineMonday = adapter_teacher_assignMonday.getClass_routines();
//                List<Class_Routine> clRoutineTuesday = adapter_teacher_assignTuesday.getClass_routines();
//                List<Class_Routine> clRoutineWednesday = adapter_teacher_assignWednesday.getClass_routines();
//                List<Class_Routine> clRoutineThursday = adapter_teacher_assignThursday.getClass_routines();
//                List<Class_Routine> clRoutineFriday = adapter_teacher_assignFriday.getClass_routines();
//                List<Class_Routine> clRoutineSaturday = adapter_teacher_assignSaturday.getClass_routines();

                DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                        .child("Routine").child(grpPushId).child("schedule");

                //Monday
                for (int i = 0; i < clRoutineMonday.size(); i++) {

                    Class_Routine class_routineMonday = new Class_Routine(clRoutineMonday.get(i).getPeriod(), clRoutineMonday.get(i).getId(), clRoutineMonday.get(i).getTeacher(), clRoutineMonday.get(i).getSubject(), classPushId, className);
//                    Class_Routine class_routineTuesday = new Class_Routine(clRoutineTuesday.get(i).getPeriod(), clRoutineTuesday.get(i).getId(), clRoutineTuesday.get(i).getTeacher(), clRoutineTuesday.get(i).getSubject(), classPushId, className);
//                    Class_Routine class_routineWednesday = new Class_Routine(clRoutineWednesday.get(i).getPeriod(), clRoutineWednesday.get(i).getId(), clRoutineWednesday.get(i).getTeacher(), clRoutineWednesday.get(i).getSubject(), classPushId, className);
//                    Class_Routine class_routineThursday = new Class_Routine(clRoutineThursday.get(i).getPeriod(), clRoutineThursday.get(i).getId(), clRoutineThursday.get(i).getTeacher(), clRoutineThursday.get(i).getSubject(), classPushId, className);
//                    Class_Routine class_routineFriday = new Class_Routine(clRoutineFriday.get(i).getPeriod(), clRoutineFriday.get(i).getId(), clRoutineFriday.get(i).getTeacher(), clRoutineFriday.get(i).getSubject(), classPushId, className);


                    Log.d("ROUTDATAMonday", "period: " + class_routineMonday.getPeriod());
                    Log.d("ROUTDATAMonday", "id: " + class_routineMonday.getId());
                    Log.d("ROUTDATAMonday", "subject: " + class_routineMonday.getSubject());
                    Log.d("ROUTDATAMonday", "teacher: " + class_routineMonday.getTeacher());


                    String teachUserId = class_routineMonday.getId();
                    int period = class_routineMonday.getPeriod();


                    if (teachUserId != null) {
                        dbRoutineStructure.child(teachUserId).child("Monday").child(String.valueOf(period)).setValue(class_routineMonday);
//                        dbRoutineStructure.child(teachUserId).child("Tuesday").child(String.valueOf(period)).setValue(class_routineTuesday);
//                        dbRoutineStructure.child(teachUserId).child("Wednesday").child(String.valueOf(period)).setValue(class_routineWednesday);
//                        dbRoutineStructure.child(teachUserId).child("Thursday").child(String.valueOf(period)).setValue(class_routineThursday);
//                        dbRoutineStructure.child(teachUserId).child("Friday").child(String.valueOf(period)).setValue(class_routineFriday);

                        databaseAllRoutineStructure.child("Monday").child(String.valueOf(period)).setValue(class_routineMonday);
//                        databaseAllRoutineStructure.child("Tuesday").child(String.valueOf(period)).setValue(class_routineTuesday);
//                        databaseAllRoutineStructure.child("Wednesday").child(String.valueOf(period)).setValue(class_routineWednesday);
//                        databaseAllRoutineStructure.child("Thursday").child(String.valueOf(period)).setValue(class_routineThursday);
//                        databaseAllRoutineStructure.child("Friday").child(String.valueOf(period)).setValue(class_routineFriday);


                    }


                }

/*
                //Saturday
                for (int i = 0; i < clRoutineSaturday.size(); i++) {

                    Class_Routine class_routineSaturday = new Class_Routine(clRoutineSaturday.get(i).getPeriod(), clRoutineSaturday.get(i).getId(), clRoutineSaturday.get(i).getTeacher(), clRoutineSaturday.get(i).getSubject(), classPushId, className);

                    String teachUserId = class_routineSaturday.getId();
                    int period = class_routineSaturday.getPeriod();


                    if (teachUserId != null) {
                        dbRoutineStructure.child(teachUserId).child("Saturday").child(String.valueOf(period)).setValue(class_routineSaturday);
                        databaseAllRoutineStructure.child("Saturday").child(String.valueOf(period)).setValue(class_routineSaturday);

                    }

                }
*/
                Intent intent = new Intent(Routine_Structure.this, Server_Activity.class);
                startActivity(intent);

            }
        });

        final PopupMenu dropDownMenu = new PopupMenu(Routine_Structure.this, routine_more);

        final Menu menu = dropDownMenu.getMenu();

        dropDownMenu.getMenuInflater().inflate(R.menu.routine_more, menu);

        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {
                    case "Teacher Attendance":
                        Intent intent = new Intent(Routine_Structure.this, Attendance_Activity_Teacher.class);
                        startActivity(intent);
                        break;

                    case "Maximum periods":

                        break;
                }

                return false;
            }
        });


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


        classDataListMonday = new ArrayList<>();
        classDataListTuesday = new ArrayList<>();
        classDataListWednesday = new ArrayList<>();
        classDataListThursday = new ArrayList<>();
        classDataListFriday = new ArrayList<>();
        classDataListSaturday = new ArrayList<>();

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


                        databaseAllRoutineStructure.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                classDataListMonday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Monday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListMonday.add(class_routineall);
                                }

                                adapter_teacher_assignMonday.setClass_routines(classDataListMonday);
                                adapter_teacher_assignMonday.setUniPush(classPushId);
                                adapter_teacher_assignMonday.setClassStudentIDList(classStudentIDListMonday);
                                adapter_teacher_assignMonday.setClassStudentList(classStudentListMonday);
                                adapter_teacher_assignMonday.setSubjectDetailsModelList(subjectDetailsModelListMonday);
                                rv_monday.setAdapter(adapter_teacher_assignMonday);
                                adapter_teacher_assignMonday.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        //Put this while opening routine structure button


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

/*
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


        */
/*




        adapter_all_teacher_assignMonday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_monday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));


        adapter_all_teacher_assignTuesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_tuesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));


        adapter_all_teacher_assignWednesday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_wednesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));


        adapter_all_teacher_assignThursday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_thursday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));


        adapter_all_teacher_assignFriday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_friday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        adapter_all_teacher_assignSaturday = new Adapter_Subject_Assign(Routine_Structure.this);
        rv_all_saturday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));

        databaseAllRoutineStructure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Monday
                classDataListMonday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Monday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListMonday.add(class_routineall);
                }

                rv_all_monday.setAdapter(adapter_all_teacher_assignMonday);
                adapter_all_teacher_assignMonday.setClass_routines(classDataListMonday);
                adapter_all_teacher_assignMonday.notifyDataSetChanged();


                //Tuesday
                classDataListTuesday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Tuesday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListTuesday.add(class_routineall);
                }
                rv_all_tuesday.setAdapter(adapter_all_teacher_assignTuesday);
                adapter_all_teacher_assignTuesday.setClass_routines(classDataListTuesday);
                adapter_all_teacher_assignTuesday.notifyDataSetChanged();


                //Wednesday
                classDataListWednesday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Wednesday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListWednesday.add(class_routineall);
                }
                rv_all_wednesday.setAdapter(adapter_all_teacher_assignWednesday);
                adapter_all_teacher_assignWednesday.setClass_routines(classDataListWednesday);
                adapter_all_teacher_assignWednesday.notifyDataSetChanged();

                //Thursday
                classDataListThursday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Thursday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListThursday.add(class_routineall);
                }
                rv_all_thursday.setAdapter(adapter_all_teacher_assignThursday);
                adapter_all_teacher_assignThursday.setClass_routines(classDataListThursday);
                adapter_all_teacher_assignThursday.notifyDataSetChanged();

                //Friday
                classDataListFriday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Friday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListFriday.add(class_routineall);
                }
                rv_all_friday.setAdapter(adapter_all_teacher_assignFriday);
                adapter_all_teacher_assignFriday.setClass_routines(classDataListFriday);
                adapter_all_teacher_assignFriday.notifyDataSetChanged();

                //Saturday
                classDataListSaturday.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("Saturday").getChildren()) {
                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                    classDataListSaturday.add(class_routineall);
                }
                rv_all_saturday.setAdapter(adapter_all_teacher_assignSaturday);
                adapter_all_teacher_assignSaturday.setClass_routines(classDataListSaturday);
                adapter_all_teacher_assignSaturday.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }
}