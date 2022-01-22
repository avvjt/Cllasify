package com.cllasify.cllasify.Class;

import android.net.Uri;

public class Class_Group {

    public String dateTime;
    public String userName;
    public String quesUserName;
    public String ansUserName;
    public String cmntAnsUserName;
    public String grpJoiningStatus;

    public String userId;
    public String adminUserId;
    public Uri userPhoto;
    public String userEmailId;
    public String userPhone;
    public String position;
    public long pos;
    public String quesCombined;
    public String groupName;
    public String groupCategory;
    public String groupPositionId;
    public String group1;
    public String group2;
    public String group3;
    public String group4;
    public String group5;
    public String group6;
    public String group7;
    public String group8;
    public String group9;

    public String subsStatus;
    public String userStatus;

    public String groupUserCmnt;
    public String groupOtherUserCmnt;
    public long groupno;

    public String subGroupName;
    public String groupSubGroupComb;

    public String notifyCategory;

    public boolean isChecked;

    private boolean expandable;

    private String imageUrl;

    public Class_Group(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Class_Group(String dateTime, String userId, String position, String groupName, String subGroupName, String groupSubGroupComb) {
        this.dateTime = dateTime;
        this.userId = userId;
        this.position = position;
        this.groupName = groupName;
        this.subGroupName = subGroupName;
        this.groupSubGroupComb = groupSubGroupComb;
    }

    public Class_Group(String dateTime, String userName, String userId, String position, String groupName, String groupCategory, long groupno) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.position = position;
        this.groupName = groupName;
        this.groupCategory = groupCategory;
        this.groupno = groupno;

        this.expandable = expandable;

        this.group1 = group1;
        this.group2 = group2;
        this.group3 = group3;
        this.group4 = group4;
        this.group5 = group5;
        this.group6 = group6;
        this.group7 = group7;
        this.group8 = group8;
        this.group9 = group9;
    }

    public Class_Group(String dateTime, String userName, String userId, String adminUserId, String groupName, String groupPositionId, String subsStatus, String userStatus) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.adminUserId = adminUserId;
        this.groupName = groupName;
        this.groupPositionId = groupPositionId;
        this.subsStatus = subsStatus;
        this.userStatus = userStatus;
    }

    public Class_Group(String dateTime, String userName, String userId, String adminUserId, long pos, String groupName, String groupPositionId, String subsStatus, String userStatus) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.adminUserId = adminUserId;
        this.pos = pos;
        this.groupName = groupName;
        this.groupPositionId = groupPositionId;
        this.subsStatus = subsStatus;
        this.userStatus = userStatus;
    }

    public Class_Group(String dateTime, String userName, String userId, String userEmailId, String position, String groupName, String groupCategory) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.userEmailId = userEmailId;
        this.position = position;
        this.groupName = groupName;
        this.groupCategory = groupCategory;
    }

    public Class_Group() {
    }


    public Class_Group(String dateTime, String userName, String userId, String adminUserId, String grpJoiningStatus, String userEmailId, String position, String groupName, String groupPositionId) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.userId = userId;
        this.adminUserId = adminUserId;
        this.grpJoiningStatus = grpJoiningStatus;
        this.userEmailId = userEmailId;
        this.position = position;
        this.groupName = groupName;
        this.groupPositionId = groupPositionId;
    }

    public Class_Group(String dateTime, String userName, String grpJoiningStatus, String userId, String adminUserId, String userEmailId, String position, String groupName, String groupPositionId, String notifyCategory) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.grpJoiningStatus = grpJoiningStatus;
        this.userId = userId;
        this.adminUserId = adminUserId;
        this.userEmailId = userEmailId;
        this.position = position;
        this.groupName = groupName;
        this.groupPositionId = groupPositionId;
        this.notifyCategory = notifyCategory;
    }

    public Class_Group(String dateTime, String userName, String grpJoiningStatus, String userId, String adminUserId, String userEmailId, String position, String groupName, String groupPositionId, String subGroupName, String notifyCategory) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.grpJoiningStatus = grpJoiningStatus;
        this.userId = userId;
        this.adminUserId = adminUserId;
        this.userEmailId = userEmailId;
        this.position = position;
        this.groupName = groupName;
        this.groupPositionId = groupPositionId;
        this.subGroupName = subGroupName;
        this.notifyCategory = notifyCategory;
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

    public String getQuesUserName() {
        return quesUserName;
    }

    public void setQuesUserName(String quesUserName) {
        this.quesUserName = quesUserName;
    }

    public String getAnsUserName() {
        return ansUserName;
    }

    public void setAnsUserName(String ansUserName) {
        this.ansUserName = ansUserName;
    }

    public String getCmntAnsUserName() {
        return cmntAnsUserName;
    }

    public void setCmntAnsUserName(String cmntAnsUserName) {
        this.cmntAnsUserName = cmntAnsUserName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Uri getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
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

    public String getQuesCombined() {
        return quesCombined;
    }

    public void setQuesCombined(String quesCombined) {
        this.quesCombined = quesCombined;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public String getGroupUserCmnt() {
        return groupUserCmnt;
    }

    public void setGroupUserCmnt(String groupUserCmnt) {
        this.groupUserCmnt = groupUserCmnt;
    }

    public String getGroupOtherUserCmnt() {
        return groupOtherUserCmnt;
    }

    public void setGroupOtherUserCmnt(String groupOtherUserCmnt) {
        this.groupOtherUserCmnt = groupOtherUserCmnt;
    }

    public long getGroupno() {
        return groupno;
    }

    public void setGroupno(long groupno) {
        this.groupno = groupno;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getGrpJoiningStatus() {
        return grpJoiningStatus;
    }

    public void setGrpJoiningStatus(String grpJoiningStatus) {
        this.grpJoiningStatus = grpJoiningStatus;
    }

    public String getGroupPositionId() {
        return groupPositionId;
    }

    public void setGroupPositionId(String groupPositionId) {
        this.groupPositionId = groupPositionId;
    }

    public String getGroup1() {
        return group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public String getGroup3() {
        return group3;
    }

    public void setGroup3(String group3) {
        this.group3 = group3;
    }

    public String getGroup4() {
        return group4;
    }

    public void setGroup4(String group4) {
        this.group4 = group4;
    }

    public String getGroup5() {
        return group5;
    }

    public void setGroup5(String group5) {
        this.group5 = group5;
    }

    public String getGroup6() {
        return group6;
    }

    public void setGroup6(String group6) {
        this.group6 = group6;
    }

    public String getGroup7() {
        return group7;
    }

    public void setGroup7(String group7) {
        this.group7 = group7;
    }

    public String getGroup8() {
        return group8;
    }

    public void setGroup8(String group8) {
        this.group8 = group8;
    }

    public String getGroup9() {
        return group9;
    }

    public void setGroup9(String group9) {
        this.group9 = group9;
    }

    public String getSubsStatus() {
        return subsStatus;
    }

    public void setSubsStatus(String subsStatus) {
        this.subsStatus = subsStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public String getGroupSubGroupComb() {
        return groupSubGroupComb;
    }

    public void setGroupSubGroupComb(String groupSubGroupComb) {
        this.groupSubGroupComb = groupSubGroupComb;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getNotifyCategory() {
        return notifyCategory;
    }

    public void setNotifyCategory(String notifyCategory) {
        this.notifyCategory = notifyCategory;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
