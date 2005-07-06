package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;


public class AmpOrgRole implements Serializable
{
	private AmpOrganisation organisation;
	private AmpRole role;
	
	public AmpOrganisation getOrganisation()
	{
		return organisation;
	}

	public AmpRole getRole()
	{
		return role;
	}
	
	public void setOrganisation(AmpOrganisation organisation)
	{
		this.organisation = organisation;
	}

	public void setRole(AmpRole role)
	{
		this.role = role;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();
		
		if (!(obj instanceof AmpOrgRole))
			throw new ClassCastException();
		
		AmpOrgRole role = (AmpOrgRole) obj;
		return (role.getOrganisation().getAmpOrgId().equals(
				organisation.getAmpOrgId()) && role.getRole().getAmpRoleId().equals(
						this.role.getAmpRoleId()));
	}
}	
