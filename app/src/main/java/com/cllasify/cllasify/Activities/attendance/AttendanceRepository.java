package com.cllasify.cllasify.Activities.attendance;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.TeacherAttendanceModel;
import com.cllasify.cllasify.ModelClasses.User;
import com.cllasify.cllasify.Utility.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceRepository {
    private final String groupPushId;
    private final String subGroupPushId;
    private final String classPushId;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference globalGroupReference = database.getReference().child("Groups");
    private DatabaseReference usersReference = database.getReference().child("Users").child("Registration");
    private DatabaseReference teachersReference;
    private DatabaseReference allScheduleReference;
    private DatabaseReference singleScheduleReference;
    private DatabaseReference individualPriorityReference;

    private DatabaseReference groupRoutineReference;

    private DatabaseReference attendanceReference;


    public AttendanceRepository(String groupPushId, String subGroupPushId, String classPushId) {
        this.groupPushId = groupPushId;
        this.subGroupPushId = subGroupPushId;
        this.classPushId = classPushId;
        groupRoutineReference = globalGroupReference.child("Routine").child(groupPushId);
        teachersReference = globalGroupReference
                .child("Check_Group_Admins").child(groupPushId).child("classAdminList");
        singleScheduleReference = groupRoutineReference.child("schedule");
        individualPriorityReference = groupRoutineReference.child("individualStructure");
        allScheduleReference = groupRoutineReference.child("allSchedule");
        attendanceReference = globalGroupReference.child("Attendance")
                .child(groupPushId);
    }

    public void getAttendanceStatus(DataFetchListener<Integer> listener) {
        listener.onLoading();
        String day = Util.getDayOfWeek();
//        String day = "Monday";
        if (day == null) {
            listener.onError("Today is sunday");
            return;
        }
        attendanceReference.child("status").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer status = snapshot.getValue(Integer.class);
                if (status == null) {
                    listener.onDataLoad(0);
                    return;
                }
                listener.onDataLoad(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public void getTeachers(DataFetchListener<List<Class_Student_Details>> listener) {
        listener.onLoading();
        ArrayList<Class_Student_Details> teachers = new ArrayList<>();
        teachersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teachers.clear();
                for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {
                    Class_Student_Details class_student_details = teacherSnapshot.getValue(Class_Student_Details.class);
                    if (class_student_details != null) {
                        teachers.add(class_student_details);
                    }
                }
                listener.onDataLoad(teachers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public void getTeacherUser(List<Class_Student_Details> adminTeacher, DataFetchListener<List<User>> listener) {
        listener.onLoading();
        ArrayList<User> teachers = new ArrayList<>();
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teachers.clear();
                for (Class_Student_Details s : adminTeacher) {
                    DataSnapshot dataSnapshot = snapshot.child(s.getUserId());
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        teachers.add(user);
                    }
                }
                listener.onDataLoad(teachers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public void startAttendance(Map<User, Boolean> teachers, DataFetchListener<?> listener) {
        listener.onLoading();
        String day = Util.getDayOfWeek();
//        String day = "Monday";
        if (day == null) {
            listener.onError("Today is sunday");
            return;
        }
        ArrayList<User> presentTeacher = new ArrayList<>();
        for (Map.Entry<User, Boolean> entry : teachers.entrySet()) {
            if (entry.getValue()) { // check if the value is true
                presentTeacher.add(entry.getKey()); // add the object to the list
            }
        }
        attendanceReference.child("status").child(day).setValue(1);
        getRoutineClasses(day, presentTeacher, result -> {
            List<Class_Routine> presentRoutines = result.first;
            List<Class_Routine> vacantClassPeriods = result.second;
            markPresentTeacherDefaultRoutine(presentRoutines, 0, new HashMap<>(), newMarked -> {
                getPresentTeacherPriorities(presentTeacher, (priorityTeachers) -> {
                    markVacantPeriod(vacantClassPeriods, priorityTeachers, newMarked, 0, (vacantMarked) -> {
                        storeAttendance(teachers, day, (v) -> {
                            attendanceReference.child("status").child(day).setValue(2);
                            listener.onDataLoad(null);
                        });
                    });
                });
            });
        });
    }

    private void storeAttendance(Map<User, Boolean> teachers, String day, TaskFillAttendance taskFillAttendance) {
        Map<String, TeacherAttendanceModel> teacherAttendance = new HashMap<>();
        for (Map.Entry<User, Boolean> entry : teachers.entrySet()) {
            User value = entry.getKey();
            boolean present = entry.getValue();
            teacherAttendance.put(value.getUserId(), value.toTeacherAttendanceModel(present, day));
        }
        attendanceReference.child(day)
                .setValue(teacherAttendance).addOnCompleteListener(task -> {
                    taskFillAttendance.onTaskDone(new HashMap<>());
                });
    }


    private void getRoutineClasses(
            String day, List<User> presentTeacher,
            OnDataFetch<Pair<List<Class_Routine>,
                    List<Class_Routine>>> onDataFetch
    ) {
        ArrayList<Class_Routine> vacantClassPeriods = new ArrayList<>();
        ArrayList<Class_Routine> presentRoutines = new ArrayList<>();
        allScheduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    vacantClassPeriods.clear();
                    presentRoutines.clear();
                    for (DataSnapshot classId : snapshot.getChildren()) {
                        for (DataSnapshot routine : classId.child(day).getChildren()) {
                            Class_Routine class_routine = routine.getValue(Class_Routine.class);
                            if (class_routine != null) {
                                if (Util.isCorrespondingTeacherPresent(class_routine.getId(), presentTeacher)) {
                                    presentRoutines.add(class_routine);
                                } else {
                                    vacantClassPeriods.add(class_routine);
                                }
                            }
                        }
                    }
                    onDataFetch.onDataLoad(new Pair<>(presentRoutines, vacantClassPeriods));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void markVacantPeriod(
            List<Class_Routine> vacantPeriods,
            List<Class_Individual_Routine> presentTeacher,
            Map<String, Integer> marked,
            int index,
            TaskFillAttendance taskFillAttendance
    ) {
        if (vacantPeriods.isEmpty()) {
            taskFillAttendance.onTaskDone(marked);
            return;
        }
        if (index >= vacantPeriods.size()) {
            taskFillAttendance.onTaskDone(marked);
            return;
        }
        Class_Routine period = vacantPeriods.get(index);
        Collections.shuffle(presentTeacher);
        Class_Routine pairRoutine = Util.pairVacantPeriods(period, presentTeacher, marked);
        if (pairRoutine == null) {
            markVacantPeriod(vacantPeriods, presentTeacher, marked, index + 1, taskFillAttendance);
            return;
        }
        addRoutineAttendance(pairRoutine, marked, (newMarked) -> {
            markVacantPeriod(vacantPeriods, presentTeacher, newMarked, index + 1, taskFillAttendance);
        });
    }

    private void getPresentTeacherPriorities(List<User> presentTeacher, OnDataFetch<List<Class_Individual_Routine>> onDataFetch) {
        ArrayList<Class_Individual_Routine> class_individual_routines = new ArrayList<>();
        individualPriorityReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                class_individual_routines.clear();
                for (User teacher : presentTeacher) {
                    Class_Individual_Routine routine =
                            snapshot.child(teacher.getUserId()).getValue(Class_Individual_Routine.class);
                    if (routine != null) {
                        if (routine.getPrimarySubject() != null && routine.getSecondarySubject() != null)
                            class_individual_routines.add(routine);
                    }
                }
                onDataFetch.onDataLoad(class_individual_routines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataFetch.onDataLoad(class_individual_routines);
            }
        });
    }

    private void markPresentTeacherDefaultRoutine(
            List<Class_Routine> presentPeriods,
            int index,
            Map<String, Integer> marked,
            TaskFillAttendance taskFillAttendance
    ) {
        if (index >= presentPeriods.size()) {
            taskFillAttendance.onTaskDone(marked);
            return;
        }
        if (presentPeriods.isEmpty()) {
            taskFillAttendance.onTaskDone(marked);
            return;
        }
        Class_Routine class_routine = presentPeriods.get(index);
        addRoutineAttendance(class_routine, marked, (newMarked) -> {
            markPresentTeacherDefaultRoutine(presentPeriods, index + 1, newMarked, taskFillAttendance);
        });
    }

    private void addRoutineAttendance(
            Class_Routine class_routine,
            Map<String, Integer> marked,
            TaskFillAttendance taskFillAttendance
    ) {
        DatabaseReference dbRoutineStructure = singleScheduleReference.child(class_routine.getId())
                .child(Util.getCurrentDateString()).child(String.valueOf(class_routine.getPeriod()));
        dbRoutineStructure.setValue(class_routine).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (marked != null) {
                    int current = marked.getOrDefault(class_routine.getId(), 0);
                    marked.put(class_routine.getId(), current + 1);
                }
            }
            taskFillAttendance.onTaskDone(marked);
        });
    }

    public void removePreviousTeacherSchedule(String userId) {
        singleScheduleReference.child(userId).child(Util.getCurrentDateString()).removeValue();
    }

    private interface OnDataFetch<T> {
        void onDataLoad(T data);
    }


    private interface TaskFillAttendance {
        void onTaskDone(Map<String, Integer> marked);
    }
}
