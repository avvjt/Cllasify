package com.cllasify.cllasify.ModelClasses;

public class TeacherAttendanceModel {

    private String teacherId,teacherName,emailId,date;
    private boolean present;

    public TeacherAttendanceModel() {
    }

    public TeacherAttendanceModel(String teacherId, String teacherName, String emailId, String date, boolean present) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.emailId = emailId;
        this.date = date;
        this.present = present;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "TeacherAttendanceModel{" +
                "teacherId='" + teacherId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", date='" + date + '\'' +
                ", present=" + present +
                '}';
    }
}
