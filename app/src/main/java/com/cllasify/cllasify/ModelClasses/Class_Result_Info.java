package com.cllasify.cllasify.ModelClasses;

public class Class_Result_Info {

    public int theoryFullMarks, practicalFullMarks, theoryMarks, practicalMarks, totalSubjectMarks, totalFullMarks;
    public String subjectName, grade;

    public Class_Result_Info(int theoryFullMarks, int practicalFullMarks, int theoryMarks, int practicalMarks, int totalSubjectMarks, int totalFullMarks, String subjectName, String grade) {
        this.theoryFullMarks = theoryFullMarks;
        this.practicalFullMarks = practicalFullMarks;
        this.theoryMarks = theoryMarks;
        this.practicalMarks = practicalMarks;
        this.totalSubjectMarks = totalSubjectMarks;
        this.totalFullMarks = totalFullMarks;
        this.subjectName = subjectName;
        this.grade = grade;
    }

    public Class_Result_Info() {
    }
}
