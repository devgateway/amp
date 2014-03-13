package org.digijava.module.fundingpledges.dbentity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
@Getter @Setter
public class FundingPledges implements Comparable<FundingPledges>, Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private AmpCategoryValue title;
	private String titleFreeText;
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;
	
	@Deprecated
	private AmpOrganisation organization;
	
	private AmpOrgGroup organizationGroup;
	private Set<FundingPledgesSector> sectorlist;
	private Set<FundingPledgesLocation> locationlist;
	private Set<FundingPledgesDetails> fundingPledgesDetails;
	
	// "Point of Contact at Donors Conference on March 31st"
	private String contactName; 
	private String contactAddress; 
	private String contactEmail; 
	private String contactTitle; 
	private String contactMinistry; 
	private String contactTelephone; 
	private String contactFax;
	private AmpOrganisation contactOrganization;
	private String contactAlternativeName; 
	private String contactAlternativeTelephone; 
	private String contactAlternativeEmail; 
	
	//"is Point of Contact for Follow Up"
	 
	private String contactName_1; 
	private String contactAddress_1; 
	private String contactEmail_1; 
	private String contactTitle_1; 
	private String contactMinistry_1; 
	private String contactTelephone_1; 
	private String contactFax_1;
 	private AmpOrganisation contactOrganization_1;
 	private String contactAlternativeName_1; 
	private String contactAlternativeTelephone_1; 
	private String contactAlternativeEmail_1; 
	
	private Double totalAmount;
	private TreeSet<String> yearsList;
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if(!(o instanceof FundingPledges))
			return false;
		FundingPledges p = (FundingPledges) o; 
		return this.getId().equals(p.getId());
	}
	
	@Override
	public int compareTo(FundingPledges o) {
		return (int) (this.getId() - o.getId());
	}
}
