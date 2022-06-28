package com.cllasify.cllasify.Class;

public class Blur_Report {

    public int position;
    public String userId;

    public Blur_Report(int position, String userId) {
        this.position = position;
        this.userId = userId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
