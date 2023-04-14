package com.cllasify.cllasify.ModelClasses;

public class Class_Individual_Routine {

    private String primarySubject, secondarySubject, teacherId, teacherName;

    public Class_Individual_Routine(String primarySubject, String secondarySubject, String teacherId, String teacherName) {
        this.primarySubject = primarySubject;
        this.secondarySubject = secondarySubject;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    public Class_Individual_Routine() {
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPrimarySubject() {
        return primarySubject;
    }

    public void setPrimarySubject(String primarySubject) {
        this.primarySubject = primarySubject;
    }

    public String getSecondarySubject() {
        return secondarySubject;
    }

    public void setSecondarySubject(String secondarySubject) {
        this.secondarySubject = secondarySubject;
    }

}
