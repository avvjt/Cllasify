package com.cllasify.cllasify.ModelClasses;

import java.util.List;

public class RoutineStructureModel {

    private List<Class_Student_Details> teachers;
    private List<Subject_Details_Model> subjects;
    private List<SingleDayRoutine>  singleDayRoutines;

    public RoutineStructureModel() {
    }

    public RoutineStructureModel(List<Class_Student_Details> teachers, List<Subject_Details_Model> subjects, List<SingleDayRoutine> singleDayRoutines) {
        this.teachers = teachers;
        this.subjects = subjects;
        this.singleDayRoutines = singleDayRoutines;
    }

    public List<Class_Student_Details> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Class_Student_Details> teachers) {
        this.teachers = teachers;
    }

    public List<Subject_Details_Model> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject_Details_Model> subjects) {
        this.subjects = subjects;
    }

    public List<SingleDayRoutine> getSingleDayRoutines() {
        return singleDayRoutines;
    }

    public void setSingleDayRoutines(List<SingleDayRoutine> singleDayRoutines) {
        this.singleDayRoutines = singleDayRoutines;
    }

    @Override
    public String toString() {
        return "RoutineStructureModel{" +
                "teachers=" + teachers +
                ", subjects=" + subjects +
                ", singleDayRoutines=" + singleDayRoutines +
                '}';
    }
}
