/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Alex Gartner
 *
 */
public class DocToOrgForm extends ActionForm {
	
	boolean hasAddParticipatingOrgRights	= false;
	
	String uuidForOrgsShown;
	List<AmpOrganisation> orgs;
	List<AmpOrganisation> addedOrgs;
	
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

	/**
	 * @return the orgs
	 */
	public List<AmpOrganisation> getOrgs() {
		return orgs;
	}

	/**
	 * @param orgs the orgs to set
	 */
	public void setOrgs(List<AmpOrganisation> orgs) {
		this.orgs = orgs;
	}

	/**
	 * @return the addedOrgs
	 */
	public List<AmpOrganisation> getAddedOrgs() {
		return addedOrgs;
	}

	/**
	 * @param addedOrgs the addedOrgs to set
	 */
	public void setAddedOrgs(List<AmpOrganisation> addedOrgs) {
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
