package org.digijava.module.aim.dbentity;

import java.io.Serializable;


public class AmpOrgRecipient implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long ampOrgRecipientId;
    private AmpOrganisation organization;
    private AmpOrganisation parentOrganization;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmpOrgRecipientId() {
        return ampOrgRecipientId;
    }

    public void setAmpOrgRecipientId(Long id) {
        this.ampOrgRecipientId = id;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }
 

    public AmpOrganisation getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(AmpOrganisation parentOrganization) {
        this.parentOrganization = parentOrganization;
    }


}
