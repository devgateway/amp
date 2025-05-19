package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

public class AddOrgTypeForm extends ActionForm {
    
    private Long ampOrgTypeId = null;
    private String orgType = null;
    //private Boolean orgTypeIsGovernmental = Boolean.FALSE;
    private String orgTypeCode = null;
    private String action = null;
    private Boolean reset = Boolean.FALSE;
    private String deleteFlag = "delete";
    private String classification;
    
    public void reset(ActionMapping mapping, HttpServletRequest req) {
        if (reset.booleanValue()) {
            //////System.out.println("inside reset: clearing properties");
            ampOrgTypeId = null;
            orgType = null;
            orgTypeCode = null;
            deleteFlag = "delete";
            reset = Boolean.FALSE;
            classification=null;
        }
    }
    
    public Long getAmpOrgTypeId() {
        return ampOrgTypeId;
    }
    public void setAmpOrgTypeId(Long ampOrgTypeId) {
        this.ampOrgTypeId = ampOrgTypeId;
    }
    public String getOrgType() {
        return orgType;
    }
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
    public String getOrgTypeCode() {
        return orgTypeCode;
    }
    public void setOrgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Boolean getReset() {
        return reset;
    }
    public void setReset(Boolean reset) {
        this.reset = reset;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }   
}
