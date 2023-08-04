package com.cllasify.cllasify.Activities.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cllasify.cllasify.Activities.attendance.Attendance_Activity_Teacher;
import com.cllasify.cllasify.Adapters.Adapter_Subject_Assign;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.Util;
import com.cllasify.cllasify.databinding.ActivityRoutineStructureBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routine_Structure extends AppCompatActivity {
    String grpPushId, classPushId, className;

    private ActivityRoutineStructureBinding binding;


    Adapter_Subject_Assign adapter_teacher_assignMonday, adapter_teacher_assignTuesday, adapter_teacher_assignWednesday, adapter_teacher_assignThursday, adapter_teacher_assignFriday, adapter_teacher_assignSaturday;
    List<String> subjectDetailsModelListMonday;

    List<String> classStudentListMonday;

    List<Class_Routine> classDataListMonday, classDataListTuesday, classDataListWednesday, classDataListThursday, classDataListFriday, classDataListSaturday;

    List<String> classStudentIDListMonday;

    private final List<Class_Student_Details> teachersList = new ArrayList<>();
    private final List<String> weekList = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    private final Map<String, List<Class_Routine>> classRoutines = new HashMap<>();
    List<Class_Routine> clRoutineMonday = new ArrayList<>();
    List<Class_Routine> clRoutineTuesday = new ArrayList<>();
    List<Class_Routine> clRoutineWednesday = new ArrayList<>();
    List<Class_Routine> clRoutineThursday = new ArrayList<>();
    List<Class_Routine> clRoutineFriday = new ArrayList<>();
    List<Class_Routine> clRoutineSaturday = new ArrayList<>();

    private DatabaseReference databaseReferenceGetTeachers;
    private DatabaseReference databaseAllRoutineStructure;

    private DatabaseReference databaseReferenceGetStudent;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineStructureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("groupPushId") && getIntent().hasExtra("classPushId") && getIntent().hasExtra("className")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
            classPushId = getIntent().getStringExtra("classPushId");
            className = getIntent().getStringExtra("className");

            binding.classNameTV.setText(className + "'s Routine");

        }
        initAdapters();
        initView();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(grpPushId);
        databaseReferenceGetStudent = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(grpPushId).child(classPushId);
        databaseAllRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("allSchedule").child(classPushId);
        fetchTeachers();

        binding.viewScroll.fullScroll(ScrollView.FOCUS_UP);

        binding.btnDone.setOnClickListener(view -> {
            startAddingNewRoutines();
        });
        binding.btnMonday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedMonday);

            binding.rvAssignedTuesday.setVisibility(View.GONE);
            binding.rvAssignedWednesday.setVisibility(View.GONE);
            binding.rvAssignedThursday.setVisibility(View.GONE);
            binding.rvAssignedFriday.setVisibility(View.GONE);
            binding.rvAssignedSaturday.setVisibility(View.GONE);


        });
        binding.btnTuesday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedTuesday);

            binding.rvAssignedMonday.setVisibility(View.GONE);
            binding.rvAssignedWednesday.setVisibility(View.GONE);
            binding.rvAssignedThursday.setVisibility(View.GONE);
            binding.rvAssignedFriday.setVisibility(View.GONE);
            binding.rvAssignedSaturday.setVisibility(View.GONE);

        });
        binding.btnWednesday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedWednesday);

            binding.rvAssignedMonday.setVisibility(View.GONE);
            binding.rvAssignedTuesday.setVisibility(View.GONE);
            binding.rvAssignedThursday.setVisibility(View.GONE);
            binding.rvAssignedFriday.setVisibility(View.GONE);
            binding.rvAssignedSaturday.setVisibility(View.GONE);

        });
        binding.btnThursday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedThursday);

            binding.rvAssignedMonday.setVisibility(View.GONE);
            binding.rvAssignedTuesday.setVisibility(View.GONE);
            binding.rvAssignedWednesday.setVisibility(View.GONE);
            binding.rvAssignedFriday.setVisibility(View.GONE);
            binding.rvAssignedSaturday.setVisibility(View.GONE);

        });
        binding.btnFriday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedFriday);

            binding.rvAssignedMonday.setVisibility(View.GONE);
            binding.rvAssignedTuesday.setVisibility(View.GONE);
            binding.rvAssignedWednesday.setVisibility(View.GONE);
            binding.rvAssignedThursday.setVisibility(View.GONE);
            binding.rvAssignedSaturday.setVisibility(View.GONE);

        });
        binding.btnSaturday.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvAssignedSaturday);


            binding.rvAssignedMonday.setVisibility(View.GONE);
            binding.rvAssignedTuesday.setVisibility(View.GONE);
            binding.rvAssignedWednesday.setVisibility(View.GONE);
            binding.rvAssignedThursday.setVisibility(View.GONE);
            binding.rvAssignedFriday.setVisibility(View.GONE);

        });
        fetchPreviousRoutines();
    }

    private void initView() {
        classDataListMonday = new ArrayList<>();
        classDataListTuesday = new ArrayList<>();
        classDataListWednesday = new ArrayList<>();
        classDataListThursday = new ArrayList<>();
        classDataListFriday = new ArrayList<>();
        classDataListSaturday = new ArrayList<>();
        //Monday
        subjectDetailsModelListMonday = new ArrayList<>();
        classStudentListMonday = new ArrayList<>();
        classStudentIDListMonday = new ArrayList<>();

//        initPopupMenu();
    }

    private void toggleViewVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            // Hide the content with animation
            view.animate().alpha(0.0f).translationY(-view.getHeight()).setDuration(150).withEndAction(() -> view.setVisibility(View.GONE));
        } else {
            // Show the content with animation
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.0f);
            view.animate().translationY(0).alpha(1.0f).setDuration(150);
        }
    }

    private void initAdapters() {
        adapter_teacher_assignMonday = new Adapter_Subject_Assign(Routine_Structure.this);
        adapter_teacher_assignTuesday = new Adapter_Subject_Assign(Routine_Structure.this);
        adapter_teacher_assignWednesday = new Adapter_Subject_Assign(Routine_Structure.this);
        adapter_teacher_assignThursday = new Adapter_Subject_Assign(Routine_Structure.this);
        adapter_teacher_assignFriday = new Adapter_Subject_Assign(Routine_Structure.this);
        adapter_teacher_assignSaturday = new Adapter_Subject_Assign(Routine_Structure.this);

        initRecyclerViews();
    }

    private void initRecyclerViews() {
        /*
        binding.rvAssignedMonday.setNestedScrollingEnabled(false);
        binding.rvAssignedTuesday.setNestedScrollingEnabled(false);
        binding.rvAssignedWednesday.setNestedScrollingEnabled(false);
        binding.rvAssignedThursday.setNestedScrollingEnabled(false);
        binding.rvAssignedFriday.setNestedScrollingEnabled(false);
        binding.rvAssignedSaturday.setNestedScrollingEnabled(false);
*/
        // Setting Adapter
        binding.rvAssignedMonday.setAdapter(adapter_teacher_assignMonday);
        binding.rvAssignedTuesday.setAdapter(adapter_teacher_assignTuesday);
        binding.rvAssignedWednesday.setAdapter(adapter_teacher_assignWednesday);
        binding.rvAssignedThursday.setAdapter(adapter_teacher_assignThursday);
        binding.rvAssignedFriday.setAdapter(adapter_teacher_assignFriday);
        binding.rvAssignedSaturday.setAdapter(adapter_teacher_assignSaturday);

        // Setting Layout Manager
        binding.rvAssignedMonday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
        binding.rvAssignedTuesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
        binding.rvAssignedWednesday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
        binding.rvAssignedThursday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
        binding.rvAssignedFriday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
        binding.rvAssignedSaturday.setLayoutManager(new LinearLayoutManager(Routine_Structure.this));
    }

    private void setupRecyclerViewSubjectsAndClass(List<String> teacherNames, List<String> teacherIds, List<String> subjects) {

        // Monday
        adapter_teacher_assignMonday.setUniPush(classPushId);
        adapter_teacher_assignMonday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignMonday.setClassStudentList(teacherNames);
        adapter_teacher_assignMonday.setSubjectDetailsModelList(subjects);

        // Tuesday
        adapter_teacher_assignTuesday.setUniPush(classPushId);
        adapter_teacher_assignTuesday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignTuesday.setClassStudentList(teacherNames);
        adapter_teacher_assignTuesday.setSubjectDetailsModelList(subjects);

        adapter_teacher_assignWednesday.setUniPush(classPushId);
        adapter_teacher_assignWednesday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignWednesday.setClassStudentList(teacherNames);
        adapter_teacher_assignWednesday.setSubjectDetailsModelList(subjects);

        adapter_teacher_assignThursday.setUniPush(classPushId);
        adapter_teacher_assignThursday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignThursday.setClassStudentList(teacherNames);
        adapter_teacher_assignThursday.setSubjectDetailsModelList(subjects);

        adapter_teacher_assignFriday.setUniPush(classPushId);
        adapter_teacher_assignFriday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignFriday.setClassStudentList(teacherNames);
        adapter_teacher_assignFriday.setSubjectDetailsModelList(subjects);

        adapter_teacher_assignSaturday.setUniPush(classPushId);
        adapter_teacher_assignSaturday.setClassStudentIDList(teacherIds);
        adapter_teacher_assignSaturday.setClassStudentList(teacherNames);
        adapter_teacher_assignSaturday.setSubjectDetailsModelList(subjects);
    }

    private void initPopupMenu() {
        final PopupMenu dropDownMenu = new PopupMenu(Routine_Structure.this, binding.routineMore);

        final Menu menu = dropDownMenu.getMenu();

        dropDownMenu.getMenuInflater().inflate(R.menu.routine_more, menu);

        dropDownMenu.setOnMenuItemClickListener(item -> {

            if ("Teacher Attendance".equals(item.toString())) {
                Intent intent = new Intent(Routine_Structure.this, Attendance_Activity_Teacher.class);
                intent.putExtra("groupPushId", grpPushId);
                intent.putExtra("classPushId", classPushId);
                startActivity(intent);

                    /*
                case "Maximum periods":

                    break;
                    */
            }

            return false;
        });

        binding.routineMore.setOnClickListener(view -> dropDownMenu.show());
    }


    private void fetchTeachers() {
        databaseReferenceGetTeachers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teachersList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                    Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                    if (class_student_details != null) {
                        teachersList.add(class_student_details);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchPreviousRoutines() {
        databaseReferenceGetStudent.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        setupRecyclerViewSubjectsAndClass(classStudentListMonday, classStudentIDListMonday, subjectDetailsModelListMonday);
                        databaseAllRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // Monday
                                classDataListMonday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Monday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListMonday.add(class_routineall);
                                }
                                if (classDataListMonday.isEmpty()) {
                                    classDataListMonday = Util.getDefaultRoutine(8);
                                }
                                adapter_teacher_assignMonday.setClass_routines(classDataListMonday);

                                // Tuesday
                                classDataListTuesday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Tuesday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListTuesday.add(class_routineall);
                                }
                                if (classDataListTuesday.isEmpty()) {
                                    classDataListTuesday = Util.getDefaultRoutine(8);
                                }
                                adapter_teacher_assignTuesday.setClass_routines(classDataListTuesday);

                                // Wednesday
                                classDataListWednesday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Wednesday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListWednesday.add(class_routineall);
                                }
                                if (classDataListWednesday.isEmpty()) {
                                    classDataListWednesday = Util.getDefaultRoutine(8);
                                }
                                adapter_teacher_assignWednesday.setClass_routines(classDataListWednesday);

                                // Thursday
                                classDataListThursday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Thursday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListThursday.add(class_routineall);
                                }
                                if (classDataListThursday.isEmpty()) {
                                    classDataListThursday = Util.getDefaultRoutine(8);
                                }
                                adapter_teacher_assignThursday.setClass_routines(classDataListThursday);

                                // Friday
                                classDataListFriday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Friday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListFriday.add(class_routineall);
                                }
                                if (classDataListFriday.isEmpty()) {
                                    classDataListFriday = Util.getDefaultRoutine(8);
                                }
                                adapter_teacher_assignFriday.setClass_routines(classDataListFriday);

                                // Saturday
                                classDataListSaturday.clear();
                                for (DataSnapshot dataSnapshot : snapshot.child("Saturday").getChildren()) {
                                    Class_Routine class_routineall = dataSnapshot.getValue(Class_Routine.class);
                                    classDataListSaturday.add(class_routineall);
                                }
                                if (classDataListSaturday.isEmpty()) {
                                    classDataListSaturday = Util.getDefaultRoutine(4);
                                }
                                adapter_teacher_assignSaturday.setClass_routines(classDataListSaturday);
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
    }


    private void startAddingNewRoutines() {
        // Monday
        clRoutineMonday = adapter_teacher_assignMonday.getClass_routines();
        if (Util.countRoutineData(clRoutineMonday) < 6) {
            Toast.makeText(this, getString(R.string.set_6_period_warn, "Monday"), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Monday", clRoutineMonday);

        // Tuesday
        clRoutineTuesday = adapter_teacher_assignTuesday.getClass_routines();
        if (Util.countRoutineData(clRoutineTuesday) < 6) {
            Toast.makeText(this, getString(R.string.set_6_period_warn, "Tuesday"), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Tuesday", clRoutineTuesday);

        // Wednesday
        clRoutineWednesday = adapter_teacher_assignWednesday.getClass_routines();
        if (Util.countRoutineData(clRoutineWednesday) < 6) {
            Toast.makeText(this, getString(R.string.set_6_period_warn, "Wednesday"), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Wednesday", clRoutineWednesday);

        // Thursday
        clRoutineThursday = adapter_teacher_assignThursday.getClass_routines();
        if (Util.countRoutineData(clRoutineThursday) < 6) {
            Toast.makeText(this, getString(R.string.set_6_period_warn, "Thursday"), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Thursday", clRoutineThursday);

        // Friday
        clRoutineFriday = adapter_teacher_assignFriday.getClass_routines();
        if (Util.countRoutineData(clRoutineFriday) < 6) {
            Toast.makeText(this, getString(R.string.set_6_period_warn, "Friday"), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Friday", clRoutineFriday);

        // Saturday
        clRoutineSaturday = adapter_teacher_assignSaturday.getClass_routines();
        if (Util.countRoutineData(clRoutineMonday) < 6) {
            Toast.makeText(this, getString(R.string.set_4_period_warn), Toast.LENGTH_SHORT).show();
            return;
        }
        classRoutines.put("Saturday", clRoutineSaturday);
        Toast.makeText(Routine_Structure.this, "Processing routine, this will may take some time so wait patiently", Toast.LENGTH_SHORT).show();
        setDailyRoutine(0, () -> {
            Toast.makeText(this, "Routine updated", Toast.LENGTH_SHORT).show();

            finish();
            Intent intent = new Intent(Routine_Structure.this, All_Routine.class);
            intent.putExtra("groupPushId", grpPushId);
            intent.putExtra("classPushId", classPushId);
            intent.putExtra("className", className);
            startActivity(intent);

            DatabaseReference databaseAllRoutineStructureDel = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("schedule");

            databaseAllRoutineStructureDel.child("Friday").removeValue();
        });
    }

    private void setDailyRoutine(int weekIndex, TaskAddRoutine taskAddRoutine) {
        if (weekList.isEmpty() || weekIndex >= weekList.size()) {
            taskAddRoutine.onTaskDone();
            return;
        }

        String day = weekList.get(weekIndex);
        List<Class_Routine> class_routines = classRoutines.get(day);
        Log.e(day, "" + weekIndex + " " + class_routines.toString());
        if (class_routines == null || class_routines.isEmpty()) {
            setDailyRoutine(weekIndex + 1, taskAddRoutine);
            return;
        }

        DatabaseReference databaseAllRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("allSchedule").child(classPushId);
        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("schedule");

        Map<String, Object> allScheduleUpdates = new HashMap<>();
        Map<String, Object> routineUpdates = new HashMap<>();

        removeTeacherPreviousSetSchedule(day, teachersList.size() - 1, () -> {
            for (Class_Routine class_routine : class_routines) {
                if (class_routine != null) {
                    class_routine.setClassPushId(classPushId);
                    allScheduleUpdates.put(day + "/" + class_routine.getPeriod(), class_routine);
                    if (class_routine.getId() != null) {
                        routineUpdates.put(class_routine.getId() + "/" + day + "/" + class_routine.getPeriod(), class_routine);
                    }
                }
            }

            if (!allScheduleUpdates.isEmpty()) {
                databaseAllRoutineStructure.updateChildren(allScheduleUpdates).addOnCompleteListener(task -> {
                    dbRoutineStructure.updateChildren(routineUpdates).addOnCompleteListener(task2 -> {
                        setDailyRoutine(weekIndex + 1, taskAddRoutine);
                    });
                });
            }
        });
    }

    private void removeTeacherPreviousSetSchedule(String day, int index, TaskDelete taskDelete) {
        if (index < 0) {
            taskDelete.onTaskDone();
            return;
        }

        Class_Student_Details teacher = teachersList.get(index);
        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups").child("Routine").child(grpPushId).child("schedule").child(teacher.getUserId()).child(day);
        dbRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Class_Routine class_routine = childSnapshot.getValue(Class_Routine.class);
                    if (class_routine != null) {
                        if (class_routine.getClassPushId().equals(classPushId)) {
                            childSnapshot.getRef().removeValue();
                        }
                    }
                }
                taskDelete.onTaskDone();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskDelete.onTaskDone();
            }
        });
    }

    private interface TaskDelete {
        void onTaskDone();
    }

    private interface TaskAddRoutine {
        void onTaskDone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}