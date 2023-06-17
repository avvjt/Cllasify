package com.cllasify.cllasify.ModelClasses;

public class Subject_Details_Model {

    private String subjectName,subjectUniPushId;
    public Subject_Details_Model(){}

    public Subject_Details_Model(String subjectName, String subjectUniPushId) {
        this.subjectName = subjectName;
        this.subjectUniPushId = subjectUniPushId;
    }


    public String getSubjectUniPushId() {
        return subjectUniPushId;
    }

    public void setSubjectUniPushId(String subjectUniPushId) {
        this.subjectUniPushId = subjectUniPushId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
