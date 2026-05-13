package org.digijava.module.translation.entity;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 9/30/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentTrnClassFieldPair {
    private String objClass;
    private String objField;


    public ContentTrnClassFieldPair (String objClass, String objField) {
        this.objClass = objClass;
        this.objField = objField;
    }

    public String getObjClass() {
        return objClass;
    }

    public void setObjClass(String objClass) {
        this.objClass = objClass;
    }

    public String getObjField() {
        return objField;
    }

    public void setObjField(String objField) {
        this.objField = objField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentTrnClassFieldPair that = (ContentTrnClassFieldPair) o;

        if (!Objects.equals(objClass, that.objClass)) return false;
        return Objects.equals(objField, that.objField);
    }

    @Override
    public int hashCode() {
        int result = objClass != null ? objClass.hashCode() : 0;
        result = 31 * result + (objField != null ? objField.hashCode() : 0);
        return result;
    }
}
