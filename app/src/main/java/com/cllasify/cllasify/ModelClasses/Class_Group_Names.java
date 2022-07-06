package com.cllasify.cllasify.ModelClasses;

import java.util.List;

public class Class_Group_Names {

    private String className, groupPushId, classBio;
    private List<Subject_Details_Model> childItemList;
    private String uniPushClassId;
    private List<Class_Student_Details> class_student_detailsList;

    public Class_Group_Names() {
    }


    public Class_Group_Names(String className, String groupPushId, String classBio, List<Subject_Details_Model> childItemList, String uniPushClassId, List<Class_Student_Details> class_student_detailsList) {
        this.className = className;
        this.groupPushId = groupPushId;
        this.classBio = classBio;
        this.childItemList = childItemList;
        this.uniPushClassId = uniPushClassId;
        this.class_student_detailsList = class_student_detailsList;
    }

    public List<Class_Student_Details> getClass_student_detailsList() {
        return class_student_detailsList;
    }

    public void setClass_student_detailsList(List<Class_Student_Details> class_student_detailsList) {
        this.class_student_detailsList = class_student_detailsList;
    }

    public String getUniPushClassId() {
        return uniPushClassId;
    }

    public void setUniPushClassId(String uniPushClassId) {
        this.uniPushClassId = uniPushClassId;
    }

    public String getGroupPushId() {
        return groupPushId;
    }

    public void setGroupPushId(String groupPushId) {
        this.groupPushId = groupPushId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Subject_Details_Model> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<Subject_Details_Model> childItemList) {
        this.childItemList = childItemList;
    }

    public String getClassBio() {
        return classBio;
    }

    public void setClassBio(String classBio) {
        this.classBio = classBio;
    }

}
