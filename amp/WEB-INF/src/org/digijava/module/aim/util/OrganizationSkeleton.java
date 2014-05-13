package org.digijava.module.aim.util;

import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * lightweight alternative to AmpOrganisation, usable in the (vast) majority of places in AMP where a full Org is not needed, but just a name and id
 * @author Dolghier Constantin
 *
 */
public class OrganizationSkeleton implements Comparable<OrganizationSkeleton>
{
	private Long ampOrgId;
	private String name;
	private String acronym;
	
	public Long getAmpOrgId() {
		return ampOrgId;
	}
	public void setAmpOrgId(Long ampOrgId) {
		this.ampOrgId = ampOrgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * mirroring {@link AmpOrganisation#compareTo(AmpOrganisation)}, hence the shameless copypaste
	 */
	@Override
	public int compareTo(OrganizationSkeleton org)
	{
		if (this.name == null)
		{
			if (org.name == null)
				return 0; // null == null
			return -1; // null < [anything]
		}
		
		if (org.name == null)
			return 1; // [anything] > null
		
		return this.name.toLowerCase().trim().compareTo(org.name.toLowerCase().trim());
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (id: %d)", this.getName(), this.getAmpOrgId());
	}
}
