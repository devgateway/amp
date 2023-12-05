package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class AddOrgGroupForm extends ActionForm {
    
    private Long ampOrgGrpId;
    private String orgGrpName;
    private String orgGrpCode;
    private Long levelId;               // defunct
    private String action = null;
    private String flag = null;
    private Collection level = null;    // defunct
    private Long ampOrgId = null;
    
    private Collection orgTypeColl = null;
    private Long orgTypeId = null;

    public Long getAmpOrgGrpId() {
        return ampOrgGrpId;
    }
    
    public void setAmpOrgGrpId(Long ampOrgGrpId) {
        this.ampOrgGrpId = ampOrgGrpId;
    }
    
    public Long getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
    
    public String getOrgGrpCode() {
        return orgGrpCode;
    }
    
    public void setOrgGrpCode(String orgGrpCode) {
        this.orgGrpCode = orgGrpCode;
    }
    
    public String getOrgGrpName() {
        return orgGrpName;
    }
    
    public void setOrgGrpName(String orgGrpName) {
        this.orgGrpName = orgGrpName;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    public Collection getLevel() {
        return level;
    }
    
    public void setLevel(Collection level) {
        this.level = level;
    }
    
    public Long getAmpOrgId() {
        return ampOrgId;
    }
    
    public void setAmpOrgId(Long ampOrgId) {
        this.ampOrgId = ampOrgId;
    }
    
    /**
     * @return Returns the orgTypeColl.
     */
    public Collection getOrgTypeColl() {
        return orgTypeColl;
    }
    /**
     * @param orgTypeColl The orgTypeColl to set.
     */
    public void setOrgTypeColl(Collection orgTypeColl) {
        this.orgTypeColl = orgTypeColl;
    }
    /**
     * @return Returns the orgTypeId.
     */
    public Long getOrgTypeId() {
        return orgTypeId;
    }
    /**
     * @param orgTypeId The orgTypeId to set.
     */
    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
}
