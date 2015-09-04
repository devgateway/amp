/**
 * 
 */
package org.dgfoundation.amp.onepager.helper;

import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * Transient storage for AmpOrgRole
 * @author Nadejda Mandrescu
 */
public class OrgRole implements IOrgRole {
	private AmpOrganisation organisation;
	private AmpRole role;
	
	public OrgRole(AmpOrganisation organisation, AmpRole role) {
		this.organisation = organisation;
		this.role = role;
	}
	
	public OrgRole(AmpOrgRole ampOrgRole) {
		this.organisation = ampOrgRole.getOrganisation();
		this.role = ampOrgRole.getRole();
	}

	@Override
	public AmpOrganisation getOrganisation() {
		return organisation;
	}
	
	/**
	 * @param organisation the organisation to set
	 */
	public void setOrganisation(AmpOrganisation organisation) {
		this.organisation = organisation;
	}

	@Override
	public AmpRole getRole() {
		return role;
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRole(AmpRole role) {
		this.role = role;
	}

}
