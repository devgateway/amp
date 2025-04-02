package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpComponentType;

public class UpdateComponentsForm extends ActionForm implements Serializable {

    Long Id = null;
    String compTitle = null;
    String compDes = null;
    Long compType = null;
    String compCode = null;
    String check;
    ArrayList<AmpComponentType> typeList = null;

    public ArrayList<AmpComponentType> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<AmpComponentType> typeList) {
        this.typeList = typeList;
    }

    /**
     * @return the check
     */
    public String getCheck() {
        return check;
    }

    /**
     * @param check
     *            the check to set
     */
    public void setCheck(String check) {
        this.check = check;
    }

    /**
     * @return Returns the compCode.
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode
     *            The compCode to set.
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    /**
     * @return Returns the compDes.
     */
    public String getCompDes() {
        return compDes;
    }

    /**
     * @param compDes
     *            The compDes to set.
     */
    public void setCompDes(String compDes) {
        this.compDes = compDes;
    }

    /**
     * @return Returns the compTitle.
     */
    public String getCompTitle() {
        return compTitle;
    }

    /**
     * @param compTitle
     *            The compTitle to set.
     */
    public void setCompTitle(String compTitle) {
        this.compTitle = compTitle;
    }

    /**
     * @return Returns the compType.
     */
    public Long getCompType() {
        return compType;
    }

    /**
     * @param compType
     *            The compType to set.
     */
    public void setCompType(Long compType) {
        this.compType = compType;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return Id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        Id = id;
    }

}
