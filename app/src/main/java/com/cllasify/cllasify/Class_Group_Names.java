package com.cllasify.cllasify;

import android.util.Log;

import com.cllasify.cllasify.NestedRecyclerview.Model.ChildItem;

import java.util.List;

public class Class_Group_Names {


    private String className,groupPushId;
    private List<Subject_Details_Model> childItemList;
    private String uniPushClassId;
    private List<Class_Student_Details> class_student_detailsList;

    public Class_Group_Names(){}


    public Class_Group_Names(String className, String groupPushId, List<Subject_Details_Model> childItemList, String uniPushClassId, List<Class_Student_Details> class_student_detailsList) {
        this.className = className;
        this.groupPushId = groupPushId;
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

    /*
    String dateTime;
    String userName;
    String userId;
    String groupName;

    boolean isAdmin;

    List<Subject_Details_Model> subjectDetailsModelList;

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
        if(        subjectDetailsModelList.get(0).getGroupStudentList()!=null){
            Log.d("DATA", "setSubjectDetailsModelList: "+subjectDetailsModelList.get(0).getGroupStudentList().size());

        }

    }

    public List<Subject_Details_Model> getSubjectDetailsModelList() {
        return subjectDetailsModelList;
    }

    public Class_Group_Names(String dateTime, String userName, String userId, String groupName) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.groupName = groupName;
    }

//    public boolean isAdmin() {
//        return isAdmin;
//    }
//
//    public void setAdmin(boolean admin) {
//        isAdmin = admin;
//    }

    Class_Group_Names(){
        //Alt tab daba kr tabs change karna bro.....
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    */
}
