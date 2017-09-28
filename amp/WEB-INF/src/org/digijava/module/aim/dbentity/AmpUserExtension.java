package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * AMP extension for digi User.
 * Let's use this for additional field we need users to have in AMP.
 * @author Irakli Kobiashvili
 *
 */
public class AmpUserExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    private AmpUserExtensionPK ampUserExtId;
    private AmpOrgType orgType;
    private AmpOrgGroup orgGroup;
    private AmpOrganisation organization;
    
    public AmpUserExtension(){
        
    }
    
    public AmpUserExtension(AmpUserExtensionPK key){
        this.ampUserExtId=key;
    }
    public AmpOrgType getOrgType() {
        return orgType;
    }
    public void setOrgType(AmpOrgType orgType) {
        this.orgType = orgType;
    }
    public AmpOrgGroup getOrgGroup() {
        return orgGroup;
    }
    public void setOrgGroup(AmpOrgGroup orgGroup) {
        this.orgGroup = orgGroup;
    }
    public AmpOrganisation getOrganization() {
        return organization;
    }
    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }
    public AmpUserExtensionPK getAmpUserExtId() {
        return ampUserExtId;
    }
    public void setAmpUserExtId(AmpUserExtensionPK ampUserExtId) {
        this.ampUserExtId = ampUserExtId;
    }
}
