package com.cllasify.cllasify;

import android.net.Uri;

public class class_Answer {

    public String quesCategory;
    public String question;
    public String ans;
    public String dateTime;
    public String userName;
    public String userId;
    public Uri userPhoto;
    public String examfQues;
    public String userEmailId;
    public String userPhone;
    public String position;
    public String quesCombined;
    private boolean expandable;

    public class_Answer(String quesCategory, String question, String dateTime, String userName, String userId, String userEmailId, String quesCombined) {
        this.quesCategory = quesCategory;
        this.question = question;
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.userEmailId = userEmailId;
        this.quesCombined = quesCombined;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getQuesCombined() {
        return quesCombined;
    }

    public void setQuesCombined(String quesCombined) {
        this.quesCombined = quesCombined;
    }


    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public class_Answer(String position, String ans, String dateTime, String userName, String userId) {
        this.position = position;
        this.ans = ans;
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
    }

//    public class_Answer(String quesCategory, String question, String dateTime, String userName, String userId, String examfQues, String userPhone) {
//        this.quesCategory = quesCategory;
//        this.question = question;
//        this.dateTime = dateTime;
//        this.userName = userName;
//        this.userId = userId;
//        this.examfQues = examfQues;
//        this.userPhone = userPhone;
////        this.expandable=false;
//    }


    public class_Answer() {
    }

    public String getQuesCategory() {
        return quesCategory;
    }

    public void setQuesCategory(String quesCategory) {
        this.quesCategory = quesCategory;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
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

    public String getExamfQues() {
        return examfQues;
    }

    public void setExamfQues(String examfQues) {
        this.examfQues = examfQues;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
