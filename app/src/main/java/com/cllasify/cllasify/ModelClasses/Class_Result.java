package com.cllasify.cllasify.ModelClasses;

public class Class_Result {

    public String subjectName, userName;
    public int theoryFullMarks, practicalFullMarks, theoryMarks, practicalMarks;

    public Class_Result(String subjectName, String userName, int theoryFullMarks, int practicalFullMarks, int theoryMarks, int practicalMarks) {
        this.subjectName = subjectName;
        this.userName = userName;
        this.theoryFullMarks = theoryFullMarks;
        this.practicalFullMarks = practicalFullMarks;
        this.theoryMarks = theoryMarks;
        this.practicalMarks = practicalMarks;
    }


    public Class_Result() {
    }
}
