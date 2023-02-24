/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Alex Gartner
 *
 */
public class DocToOrgForm extends ActionForm {
    
    boolean hasAddParticipatingOrgRights    = false;
    
    String uuidForOrgsShown;
    Set<AmpOrganisation> orgs;
    Set<AmpOrganisation> addedOrgs;
    
    String removingUuid;
    Long removingOrgId;
    
    private List <String> messages;
    
    /**
     * @return the hasAddParticipatingOrgRights
     */
    public boolean getHasAddParticipatingOrgRights() {
        return hasAddParticipatingOrgRights;
    }

    /**
     * @param hasAddParticipatingOrgRights the hasAddParticipatingOrgRights to set
     */
    public void setHasAddParticipatingOrgRights(boolean hasAddParticipatingOrgRights) {
        this.hasAddParticipatingOrgRights = hasAddParticipatingOrgRights;
    }
    
    

    /**
     * @return the uuidForOrgsShown
     */
    public String getUuidForOrgsShown() {
        return uuidForOrgsShown;
    }

    /**
     * @param uuidForOrgsShown the uuidForOrgsShown to set
     */
    public void setUuidForOrgsShown(String uuidForOrgsShown) {
        this.uuidForOrgsShown = uuidForOrgsShown;
    }


    public Set<AmpOrganisation> getOrgs() {
        return orgs;
    }

    public void setOrgs(Set<AmpOrganisation> orgs) {
        this.orgs = orgs;
    }

    public Set<AmpOrganisation> getAddedOrgs() {
        return addedOrgs;
    }

    public void setAddedOrgs(Set<AmpOrganisation> addedOrgs) {
        this.addedOrgs = addedOrgs;
    }

    /**
     * @return the removingUuid
     */
    public String getRemovingUuid() {
        return removingUuid;
    }

    /**
     * @param removingUuid the removingUuid to set
     */
    public void setRemovingUuid(String removingUuid) {
        this.removingUuid = removingUuid;
    }

    /**
     * @return the removingOrgId
     */
    public Long getRemovingOrgId() {
        return removingOrgId;
    }

    /**
     * @param removingOrgId the removingOrgId to set
     */
    public void setRemovingOrgId(Long removingOrgId) {
        this.removingOrgId = removingOrgId;
    }


    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }   
    
    
}
