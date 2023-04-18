package com.cllasify.cllasify.ModelClasses;

public class Class_Routine {

    int period;
    String id, teacher, subject, classID, className;

    public Class_Routine() {
    }


    public Class_Routine(int period, String id, String teacher, String subject, String classID, String className) {
        this.period = period;
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
        this.classID = classID;
        this.className = className;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
