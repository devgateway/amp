package org.digijava.module.aim.util;

import org.digijava.module.aim.annotations.translation.TranslatableField;

/**
 * lightweight alternative to AmpOrgGroup, usable in the (vast) majority of places in AMP where a full OrgGroup is not needed, but just a name and id
 * @author Dolghier Constantin
 *
 */
public class OrgGroupSkeleton implements Comparable<OrgGroupSkeleton>
{
	private Long ampOrgGrpId;	
	private String orgGrpName;	
	private String orgGrpCode;
	
	public Long getAmpOrgGrpId() {
		return ampOrgGrpId;
	}

	public void setAmpOrgGrpId(Long ampOrgGrpId) {
		this.ampOrgGrpId = ampOrgGrpId;
	}

	public String getOrgGrpName() {
		return orgGrpName;
	}

	public void setOrgGrpName(String orgGrpName) {
		this.orgGrpName = orgGrpName;
	}

	public String getOrgGrpCode() {
		return orgGrpCode;
	}

	public void setOrgGrpCode(String orgGrpCode) {
		this.orgGrpCode = orgGrpCode;
	}

	@Override
	public int compareTo(OrgGroupSkeleton org)
	{
		if (this.orgGrpName == null)
		{
			if (org.orgGrpName == null)
				return 0; // null == null
			return -1; // null < [anything]
		}
		
		if (org.orgGrpName == null)
			return 1; // [anything] > null
		
		return this.orgGrpName.trim().compareTo(org.orgGrpName.trim());
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (id: %d)", this.orgGrpName, this.ampOrgGrpId);
	}
}