package com.cllasify.cllasify.ModelClasses;

import java.util.List;

public class SingleDayRoutine {

    private String day;
    private List<Class_Routine> routine;

    public SingleDayRoutine() {
    }

    public SingleDayRoutine(String day, List<Class_Routine> routine) {
        this.day = day;
        this.routine = routine;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Class_Routine> getRoutine() {
        return routine;
    }

    public void setRoutine(List<Class_Routine> routine) {
        this.routine = routine;
    }

    @Override
    public String toString() {
        return "SingleDayRoutine{" +
                "day='" + day + '\'' +
                ", routine=" + routine +
                '}';
    }
}
