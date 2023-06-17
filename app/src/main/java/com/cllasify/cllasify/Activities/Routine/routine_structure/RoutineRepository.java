package com.cllasify.cllasify.Activities.Routine.routine_structure;

import androidx.annotation.NonNull;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.Activities.interfaces.TaskCompleteListener;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.RoutineStructureModel;
import com.cllasify.cllasify.ModelClasses.SingleDayRoutine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.Utility.Util;
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
import java.util.stream.Collectors;

public class RoutineRepository {
    private final String groupPushId;
    private final String classPushId;

    private final List<String> weekList = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    private final DatabaseReference allRoutineReference;
    private final DatabaseReference subjectReference;
    private final DatabaseReference teachersReference;
    private final DatabaseReference individualScheduleReference;

    private List<Class_Student_Details> teachers;

    public RoutineRepository(String groupPushId, String classPushId) {
        this.groupPushId = groupPushId;
        this.classPushId = classPushId;
        teachers = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference groupReference = database.getReference().child("Groups");
        subjectReference = groupReference.child("All_GRPs").child(groupPushId).child(classPushId)
                .child("classSubjectData");
        teachersReference = groupReference.child("Check_Group_Admins").child(groupPushId)
                .child("classAdminList");
        individualScheduleReference = groupReference.child("Routine").child(groupPushId).child("schedule");

        allRoutineReference = groupReference.child("Routine").child(groupPushId).child("allSchedule")
                .child(classPushId);
    }

    public void startSettingRoutines(
            List<Class_Student_Details> teachers,
            Map<String, List<Class_Routine>> mappedRoutines,
            DataFetchListener<?> listener
    ) {
        listener.onLoading();
        this.teachers = teachers;
        for (Map.Entry<String, List<Class_Routine>> entry : mappedRoutines.entrySet()) {
            int max = (entry.getKey().equals(weekList.get(5))) ? 4 : 6;
            if (Util.countRoutineData(entry.getValue()) < max) {
                listener.onError(String.format("You have to select at least %s periods for class %s", max, entry.getKey()));
                return;
            }
        }
        setDailyRoutine(0, mappedRoutines, () -> {
            listener.onDataLoad(null);
        });
    }

    public void getRoutineStructureModel(DataFetchListener<RoutineStructureModel> listener) {
        listener.onLoading();
        final RoutineStructureModel routineStructureModel = new RoutineStructureModel();
        getAdminTeachers(new TaskCompleteListener<List<Class_Student_Details>>() {
            @Override
            public void onSuccess(List<Class_Student_Details> teachers) {
                routineStructureModel.setTeachers(teachers);
                getGroupSubjects(new TaskCompleteListener<List<Subject_Details_Model>>() {
                    @Override
                    public void onSuccess(List<Subject_Details_Model> data) {
                        routineStructureModel.setSubjects(data);
                        getDailyRoutine(new TaskCompleteListener<List<SingleDayRoutine>>() {
                            @Override
                            public void onSuccess(List<SingleDayRoutine> data) {
                                routineStructureModel.setSingleDayRoutines(data);
                                listener.onDataLoad(routineStructureModel);
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        listener.onError(message);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                listener.onError(message);
            }
        });
    }


    private void getAdminTeachers(TaskCompleteListener<List<Class_Student_Details>> listener) {
        ArrayList<Class_Student_Details> teachers = new ArrayList<>();
        teachersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teachers.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Class_Student_Details teacher = child.getValue(Class_Student_Details.class);
                    if (teacher != null) {
                        teachers.add(teacher);
                    }
                }
                listener.onSuccess(teachers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }

    private void getGroupSubjects(TaskCompleteListener<List<Subject_Details_Model>> listener) {
        ArrayList<Subject_Details_Model> subjects = new ArrayList<>();
        subjectReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjects.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Subject_Details_Model subject = child.getValue(Subject_Details_Model.class);
                    if (subject != null) {
                        subjects.add(subject);
                    }
                }
                listener.onSuccess(subjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }


    private void getDailyRoutine(TaskCompleteListener<List<SingleDayRoutine>> listener) {
        allRoutineReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SingleDayRoutine> singleDayRoutines = weekList.stream().map(day -> new SingleDayRoutine(day, getClassRoutine(snapshot, day))).collect(Collectors.toList());
                listener.onSuccess(singleDayRoutines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ArrayList<SingleDayRoutine> singleDayRoutines = new ArrayList<>();
                singleDayRoutines.add(new SingleDayRoutine("Monday", Util.getDefaultRoutine(8)));
                singleDayRoutines.add(new SingleDayRoutine("Tuesday", Util.getDefaultRoutine(8)));
                singleDayRoutines.add(new SingleDayRoutine("Wednesday", Util.getDefaultRoutine(8)));
                singleDayRoutines.add(new SingleDayRoutine("Thursday", Util.getDefaultRoutine(8)));
                singleDayRoutines.add(new SingleDayRoutine("Friday", Util.getDefaultRoutine(8)));
                singleDayRoutines.add(new SingleDayRoutine("Saturday", Util.getDefaultRoutine(4)));
                listener.onSuccess(singleDayRoutines);
            }
        });
    }

    private List<Class_Routine> getClassRoutine(DataSnapshot snapshot, String day) {
        ArrayList<Class_Routine> classRoutines = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.child(day).getChildren()) {
            Class_Routine routine = dataSnapshot.getValue(Class_Routine.class);
            if (routine != null)
                classRoutines.add(routine);
        }
        if (classRoutines.isEmpty()) {
            boolean isSaturday = day.equals("Saturday");
            classRoutines = Util.getDefaultRoutine((isSaturday) ? 4 : 8);
        }
        return classRoutines;
    }

    private void setDailyRoutine(int weekIndex, Map<String, List<Class_Routine>> classRoutines, TaskAddRoutine taskAddRoutine) {
        if (weekList.isEmpty() || weekIndex >= weekList.size()) {
            taskAddRoutine.onTaskDone();
            return;
        }

        String day = weekList.get(weekIndex);
        List<Class_Routine> class_routines = classRoutines.get(day);
        if (class_routines == null || class_routines.isEmpty()) {
            setDailyRoutine(weekIndex + 1, classRoutines, taskAddRoutine);
            return;
        }

        Map<String, Object> allScheduleUpdates = new HashMap<>();
        Map<String, Object> routineUpdates = new HashMap<>();

        removeTeacherPreviousSetSchedule(day, teachers.size() - 1, () -> {
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
                allRoutineReference.updateChildren(allScheduleUpdates).addOnCompleteListener(task -> {
                    individualScheduleReference.updateChildren(routineUpdates).addOnCompleteListener(task2 -> {
                        setDailyRoutine(weekIndex + 1, classRoutines, taskAddRoutine);
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

        Class_Student_Details teacher = teachers.get(index);
        DatabaseReference dbRoutineStructure = individualScheduleReference
                .child(teacher.getUserId())
                .child(day);
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
}
