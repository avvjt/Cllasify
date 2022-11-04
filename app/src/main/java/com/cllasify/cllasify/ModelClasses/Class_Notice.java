package com.cllasify.cllasify.ModelClasses;

public class Class_Notice {

    public String title,description,date,key,docs;

    public Class_Notice() {
    }

    public Class_Notice(String title, String description, String date, String key, String docs) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.key = key;
        this.docs = docs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }
}
