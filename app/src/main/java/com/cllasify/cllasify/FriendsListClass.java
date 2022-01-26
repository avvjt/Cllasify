package com.cllasify.cllasify;

public class FriendsListClass {

    String UserId;
    String name;

    public FriendsListClass(String userId, String name) {
        UserId = userId;
        this.name = name;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

