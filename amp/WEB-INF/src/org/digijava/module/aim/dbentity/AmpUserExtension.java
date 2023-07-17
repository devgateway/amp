package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * AMP extension for digi User.
 * Let's use this for additional field we need users to have in AMP.
 * @author Irakli Kobiashvili
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_USER_EXT")
public class AmpUserExtension implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private AmpUserExtensionPK ampUserExtId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_org_type_id", nullable = false)
    private AmpOrgType orgType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_org_group__id", nullable = false)
    private AmpOrgGroup orgGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_org_id", nullable = false)
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
