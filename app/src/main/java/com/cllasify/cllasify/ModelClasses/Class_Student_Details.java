package com.cllasify.cllasify.ModelClasses;

public class Class_Student_Details {

    boolean isAdmin;
    public String userId,userName,annualFees,monthlyFees;

    public Class_Student_Details() {
    }

    public Class_Student_Details(boolean isAdmin, String userId, String userName) {
        this.isAdmin = isAdmin;
        this.userId = userId;
        this.userName = userName;
    }

    public Class_Student_Details(boolean isAdmin, String userId, String userName, String annualFees, String monthlyFees) {
        this.isAdmin = isAdmin;
        this.userId = userId;
        this.userName = userName;
        this.annualFees = annualFees;
        this.monthlyFees = monthlyFees;
    }

    public String getAnnualFees() {
        return annualFees;
    }

    public void setAnnualFees(String annualFees) {
        this.annualFees = annualFees;
    }

    public String getMonthlyFees() {
        return monthlyFees;
    }

    public void setMonthlyFees(String monthlyFees) {
        this.monthlyFees = monthlyFees;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
