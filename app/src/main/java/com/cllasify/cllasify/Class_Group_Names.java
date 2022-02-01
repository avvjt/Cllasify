package com.cllasify.cllasify;

import android.util.Log;

import java.util.List;

public class Class_Group_Names {

    String dateTime;
    String userName;
    String userId;
    String groupName;
//    boolean isAdmin;

//    List<Subject_Details_Model> subjectDetailsModelList;

//    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
//        this.subjectDetailsModelList = subjectDetailsModelList;
//        if(        subjectDetailsModelList.get(0).getGroupStudentList()!=null){
//            Log.d("DATA", "setSubjectDetailsModelList: "+subjectDetailsModelList.get(0).getGroupStudentList().size());
//
//        }
//
//    }
//
//    public List<Subject_Details_Model> getSubjectDetailsModelList() {
//        return subjectDetailsModelList;
//    }

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
}
