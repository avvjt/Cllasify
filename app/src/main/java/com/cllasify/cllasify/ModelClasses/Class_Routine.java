package com.cllasify.cllasify.ModelClasses;

public class Class_Routine {

    int period;
    String id,teacher,subject;

    public Class_Routine() {
    }

    public Class_Routine(int period, String id, String teacher, String subject) {
        this.period = period;
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
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
