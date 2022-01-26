package com.cllasify.cllasify;

import java.util.List;

public class Subject_Details_Model {

    String groupName,subjectTitle,subjectCreationDate;
    String groupSubjectPushId,groupPushId;

    List<Group_Students> groupStudentList;


    public String getGroupSubjectPushId() {
        return groupSubjectPushId;
    }

    public void setGroupSubjectPushId(String groupSubjectPushId) {
        this.groupSubjectPushId = groupSubjectPushId;
    }

    public String getGroupPushId() {
        return groupPushId;
    }

    public void setGroupPushId(String groupPushId) {
        this.groupPushId = groupPushId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectCreationDate() {
        return subjectCreationDate;
    }

    public void setSubjectCreationDate(String subjectCreationDate) {
        this.subjectCreationDate = subjectCreationDate;
    }

    public List<Group_Students> getGroupStudentList() {
        return groupStudentList;
    }

    public void setGroupStudentList(List<Group_Students> groupStudentList) {
        this.groupStudentList = groupStudentList;
    }

    public Subject_Details_Model(){

    }
}
