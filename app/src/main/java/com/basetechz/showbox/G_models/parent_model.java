package com.basetechz.showbox.G_models;

import java.util.List;

public class parent_model {
    private String parentId,acId,ref;
    private String parentTitle;
    private List<child_model> childItemList;
    public parent_model() {
    }

    public parent_model(List<child_model> childItemList){
        this.childItemList = childItemList;
    }

    public parent_model(String parentId, String title,List<child_model> childItemList) {
        this.parentId = parentId;
        this.parentTitle = title;
        this.childItemList = childItemList;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public List<child_model> getChildItemList() {
        return childItemList;
    }

    public void setChildItemList(List<child_model> childItemList) {
        this.childItemList = childItemList;
    }


    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
