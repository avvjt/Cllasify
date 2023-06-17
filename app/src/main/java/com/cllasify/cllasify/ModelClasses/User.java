package com.cllasify.cllasify.ModelClasses;

public class User {

    private String Name;
    private String dateTime;
    private String profilePic;
    private String token;
    private String userEmailId;
    private String userId;
    private String userStatus;

    public User() {
    }

    public User(String name, String dateTime, String profilePic, String token, String userEmailId, String userId, String userStatus) {
        Name = name;
        this.dateTime = dateTime;
        this.profilePic = profilePic;
        this.token = token;
        this.userEmailId = userEmailId;
        this.userId = userId;
        this.userStatus = userStatus;
    }

    public TeacherAttendanceModel toTeacherAttendanceModel(boolean present,String day){
        return new TeacherAttendanceModel(userId,Name,userEmailId,day,present);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", token='" + token + '\'' +
                ", userEmailId='" + userEmailId + '\'' +
                ", userId='" + userId + '\'' +
                ", userStatus='" + userStatus + '\'' +
                '}';
    }
}
