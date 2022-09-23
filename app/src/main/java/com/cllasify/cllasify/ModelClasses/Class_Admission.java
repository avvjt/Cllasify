package com.cllasify.cllasify.ModelClasses;

public class Class_Admission {

    String name,dob,father,mother,address,phone,religion,cast,date,fileUrl;

    public Class_Admission() {

    }


    public Class_Admission(String name, String dob, String father, String mother, String address, String phone, String religion, String cast, String date, String fileUrl) {
        this.name = name;
        this.dob = dob;
        this.father = father;
        this.mother = mother;
        this.address = address;
        this.phone = phone;
        this.religion = religion;
        this.cast = cast;
        this.date = date;
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
