package com.cllasify.cllasify.Utility;

import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {

    public static String getCurrentDateString() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Convert the day of the week to the corresponding day name
        String dayName = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                break;
            case Calendar.MONDAY:
                dayName = "Monday";
                break;
            case Calendar.TUESDAY:
                dayName = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayName = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayName = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayName = "Friday";
                break;
            case Calendar.SATURDAY:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    public static boolean isCorrespondingTeacherPresent(String teacherID, List<User> presentTeachers) {
        for (User cTeacher : presentTeachers) {
            if (cTeacher.getUserId().equals(teacherID)) {
                return true;
            }
        }
        return false;
    }

    public static Class_Individual_Routine getLeastMarkedTeacher(Set<Class_Individual_Routine> routines,
                                                                 Map<String, Integer> marked) {
        int maxMarked = 6;
        Class_Individual_Routine assignedTeacher = null;
        for (Class_Individual_Routine routine : routines) {
            int markedCount = marked.getOrDefault(routine.getTeacherId(), 0);
            if (markedCount < maxMarked) {
                maxMarked = markedCount;
                assignedTeacher = routine;
            }
        }
        return assignedTeacher;
    }

    public static Set<Class_Individual_Routine> findRelevantPriorities(String subject, List<Class_Individual_Routine> priorities) {
        Set<Class_Individual_Routine> relevantPriorities = new HashSet<>();
        for (Class_Individual_Routine priority : priorities) {
            if (subject.equals(priority.getPrimarySubject()) || subject.equals(priority.getSecondarySubject())) {
                relevantPriorities.add(priority);
            }
        }
        return relevantPriorities;
    }

    public static Class_Routine pairVacantPeriods(Class_Routine vacantPeriods, List<Class_Individual_Routine> priorities,
                                                  Map<String, Integer> marked) {
        Set<Class_Individual_Routine> relevantPriorities = findRelevantPriorities(vacantPeriods.getSubject(), priorities);
        if (relevantPriorities.isEmpty()) {
            return null;
        }
        Class_Individual_Routine assignedTeacher = getLeastMarkedTeacher(relevantPriorities, marked);
        if (assignedTeacher == null) {
            return null;
        }
        return new Class_Routine(vacantPeriods.getPeriod(),
                assignedTeacher.getTeacherId(),
                assignedTeacher.getTeacherName(),
                vacantPeriods.getSubject(),
                vacantPeriods.getClassID(),
                vacantPeriods.getClassName(),
                vacantPeriods.getClassPushId());
    }

    public static int countRoutineData(List<Class_Routine> routines) {
        if (routines == null) return 0;
        int count = 0;
        for (Class_Routine routine : routines) {
            if (routine.getSubject() == null || routine.getSubject().equals("")) continue;
            if (routine.getId() == null || routine.getId().equals("")) continue;
            count++;
        }
        return count;
    }
    public static ArrayList<Class_Routine> getDefaultRoutine(int capacity) {
        ArrayList<Class_Routine> routines = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            routines.add(new Class_Routine());
        }
        return routines;
    }
}
