package com.cllasify.cllasify.ModelClasses;

public class Class_Result {

    public String userName, totalGrade;
    public int totalMarks;

    public Class_Result_Info class_result_infos;

    public Class_Result(String userName, String totalGrade, int totalMarks) {
        this.userName = userName;
        this.totalGrade = totalGrade;
        this.totalMarks = totalMarks;
        this.class_result_infos = class_result_infos;
    }

    public Class_Result() {
    }
}
