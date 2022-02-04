package com.cllasify.cllasify.NestedRecyclerview.Model;

import java.util.List;

public class ParentItem {

    private String className;
    private List<ChildItem> childItemList;


    public ParentItem(){}

    public ParentItem(String className, List<ChildItem> childItemList) {
        this.className = className;
        this.childItemList = childItemList;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ChildItem> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<ChildItem> childItemList) {
        this.childItemList = childItemList;
    }
}
