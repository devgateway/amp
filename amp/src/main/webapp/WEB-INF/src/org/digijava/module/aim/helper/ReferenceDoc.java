package org.digijava.module.aim.helper;

import java.io.Serializable;

public class ReferenceDoc implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long refDocId;
    private Long categoryValueId;
    private String categoryValue;
    private String comment;
    private boolean checked;
    
    public boolean getChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public Long getCategoryValueId() {
        return categoryValueId;
    }
    public void setCategoryValueId(Long categoryValueId) {
        this.categoryValueId = categoryValueId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Long getRefDocId() {
        return refDocId;
    }
    public void setRefDocId(Long refDocId) {
        this.refDocId = refDocId;
    }
    public String getCategoryValue() {
        return categoryValue;
    }
    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }
}
