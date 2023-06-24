package com.cllasify.cllasify.ModelClasses;

import java.util.List;

public class Class_Result {

    public String userName, totalGrade;
    public int totalMarks, totalFullMarks;

    public List<Class_Result_Info> subjectMarksInfo;

    public Class_Result(String userName, String totalGrade, int totalMarks, int totalFullMarks, List<Class_Result_Info> subjectMarksInfo) {
        this.userName = userName;
        this.totalGrade = totalGrade;
        this.totalMarks = totalMarks;
        this.totalFullMarks = totalFullMarks;
        this.subjectMarksInfo = subjectMarksInfo;
    }

    public Class_Result() {
    }
}
