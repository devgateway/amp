package org.digijava.module.admin.helper;

public class FieldInfo {
    private String subclass;
    private String fieldName;
    private String fieldType;

    public FieldInfo(String subclass, String fieldName, String fieldType) {
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    // Constructor, getter, and setter methods
}
