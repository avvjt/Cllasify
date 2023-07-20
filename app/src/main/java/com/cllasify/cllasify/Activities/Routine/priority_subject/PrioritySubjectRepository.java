package com.cllasify.cllasify.Activities.Routine.priority_subject;

import android.os.Build;

import androidx.annotation.NonNull;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.SingleDayRoutine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.Utility.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrioritySubjectRepository {

    private final String groupPushId;
    private final List<String> weekList = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    private final String userId;
    private final String username;

    private final DatabaseReference individualRoutineReference;
    private final DatabaseReference subjectReference;

    private final DatabaseReference priorityReference;

    public PrioritySubjectRepository(String groupPushId) {
        this.groupPushId = groupPushId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        DatabaseReference groupReference = database.getReference().child("Groups");
        individualRoutineReference = groupReference.child("Routine").child(groupPushId).child("schedule").child(userId);
        subjectReference = groupReference.child("All_GRPs").child(groupPushId);
        priorityReference = groupReference.child("Routine").child(groupPushId)
                .child("individualStructure").child(userId);
    }

    public void getAllSubjects(DataFetchListener<List<Subject_Details_Model>> listener) {
        listener.onLoading();
        ArrayList<Subject_Details_Model> subjects = new ArrayList<>();
        subjectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.hasChildren()) {

                        for (DataSnapshot dataSnapshot1 : child.child("classSubjectData").getChildren()) {
                            Subject_Details_Model object = dataSnapshot1.getValue(Subject_Details_Model.class);
                            if (object != null)
                                subjects.add(object);
                        }
                    }
                }
                listener.onDataLoad(subjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public void getPrioritySubject(DataFetchListener<Class_Individual_Routine> listener) {
        listener.onLoading();
        priorityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listener.onDataLoad(snapshot.getValue(Class_Individual_Routine.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public void setPrioritySubject(String primarySubject, String secondarySubject, DataFetchListener<Boolean> listener) {
        Class_Individual_Routine class_individual_routine = new Class_Individual_Routine(primarySubject,
                secondarySubject, userId, username);
        priorityReference.setValue(class_individual_routine).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                listener.onDataLoad(true);
                return;
            }
            listener.onDataLoad(false);
        });
    }

    public void getAllRoutine(DataFetchListener<List<SingleDayRoutine>> listener) {
        listener.onLoading();
        individualRoutineReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SingleDayRoutine> routines = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    routines = weekList.stream().map(tempDay -> {
                        String day = (tempDay.equals("Today")) ? Util.getCurrentDateString() : tempDay;
                        return new SingleDayRoutine(tempDay, getClassRoutine(snapshot, day));
                    }).collect(Collectors.toList());
                }
                listener.onDataLoad(routines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listener.onDataLoad(weekList.stream().map(day -> new SingleDayRoutine(day, Collections.emptyList())).collect(Collectors.toList()));
                }
            }
        });
    }

    private List<Class_Routine> getClassRoutine(DataSnapshot snapshot, String day) {
        ArrayList<Class_Routine> classRoutines = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.child(day).getChildren()) {
            Class_Routine routine = dataSnapshot.getValue(Class_Routine.class);
            if (routine != null) classRoutines.add(routine);
        }
        return classRoutines;
    }
}
