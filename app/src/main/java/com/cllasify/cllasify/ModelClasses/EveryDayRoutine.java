package com.cllasify.cllasify.ModelClasses;

import java.util.List;

public class EveryDayRoutine {

    private String userName, userId;
    private List<Class_Routine> routine;

    public EveryDayRoutine() {
    }

    public EveryDayRoutine(String userId) {
        this.userName = userName;
        this.userId = userId;
        this.routine = routine;
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

    public List<Class_Routine> getRoutine() {
        return routine;
    }

    public void setRoutine(List<Class_Routine> routine) {
        this.routine = routine;
    }


}
