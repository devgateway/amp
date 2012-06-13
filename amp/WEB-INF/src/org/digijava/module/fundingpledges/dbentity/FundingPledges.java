package org.digijava.module.fundingpledges.dbentity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class FundingPledges implements Comparable<FundingPledges>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private AmpCategoryValue title;
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;
	
	private AmpOrganisation organization;
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
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String name) {
		contactName = name;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String address) {
		contactAddress = address;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String email) {
		contactEmail = email;
	}
	public String getContactMinistry() {
		return contactMinistry;
	}
	public void setContactMinistry(String ministry) {
		contactMinistry = ministry;
	}
	public String getContactTelephone() {
		return contactTelephone;
	}
	public void setContactTelephone(String telephone) {
		contactTelephone = telephone;
	}
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String fax) {
		contactFax = fax;
	}
	
	public String getContactName_1() {
		return contactName_1;
	}
	public void setContactName_1(String name_1) {
		contactName_1 = name_1;
	}
	public String getContactAddress_1() {
		return contactAddress_1;
	}
	public void setContactAddress_1(String address_1) {
		contactAddress_1 = address_1;
	}
	public String getContactEmail_1() {
		return contactEmail_1;
	}
	public void setContactEmail_1(String email_1) {
		contactEmail_1 = email_1;
	}
	public String getContactTitle_1() {
		return contactTitle_1;
	}
	public void setContactTitle_1(String title_1) {
		contactTitle_1 = title_1;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String title) {
		contactTitle = title;
	}
	public String getContactMinistry_1() {
		return contactMinistry_1;
	}
	public void setContactMinistry_1(String ministry_1) {
		contactMinistry_1 = ministry_1;
	}
	public String getContactTelephone_1() {
		return contactTelephone_1;
	}
	public void setContactTelephone_1(String telephone_1) {
		contactTelephone_1 = telephone_1;
	}
	public String getContactFax_1() {
		return contactFax_1;
	}
	public void setContactFax_1(String fax_1) {
		contactFax_1 = fax_1;
	}
	public AmpOrganisation getContactOrganization() {
		return contactOrganization;
	}
	public void setContactOrganization(AmpOrganisation contactOrganization) {
		this.contactOrganization = contactOrganization;
	}
	public AmpOrganisation getContactOrganization_1() {
		return contactOrganization_1;
	}
	public void setContactOrganization_1(AmpOrganisation contactOrganization_1) {
		this.contactOrganization_1 = contactOrganization_1;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpCategoryValue getTitle() {
		return title;
	}
	public void setTitle(AmpCategoryValue title) {
		this.title = title;
	}
	public AmpOrganisation getOrganization() {
		return organization;
	}
	public void setOrganization(AmpOrganisation organization) {
		this.organization = organization;
	}
	public Set<FundingPledgesSector> getSectorlist() {
		return sectorlist;
	}
	public void setSectorlist(Set<FundingPledgesSector> sectorlist) {
		this.sectorlist = sectorlist;
	}
	public Set<FundingPledgesLocation> getLocationlist() {
		return locationlist;
	}
	public void setLocationlist(Set<FundingPledgesLocation> locationlist) {
		this.locationlist = locationlist;
	}
	/**
	 * @return the contactAlternativeName
	 */
	public String getContactAlternativeName() {
		return contactAlternativeName;
	}
	/**
	 * @param contactAlternativeName the contactAlternativeName to set
	 */
	public void setContactAlternativeName(String contactAlternativeName) {
		this.contactAlternativeName = contactAlternativeName;
	}
	/**
	 * @return the contactAlternativeTelephone
	 */
	public String getContactAlternativeTelephone() {
		return contactAlternativeTelephone;
	}
	/**
	 * @param contactAlternativeTelephone the contactAlternativeTelephone to set
	 */
	public void setContactAlternativeTelephone(String contactAlternativeTelephone) {
		this.contactAlternativeTelephone = contactAlternativeTelephone;
	}
	/**
	 * @return the contactAlternativeEmail
	 */
	public String getContactAlternativeEmail() {
		return contactAlternativeEmail;
	}
	/**
	 * @param contactAlternativeEmail the contactAlternativeEmail to set
	 */
	public void setContactAlternativeEmail(String contactAlternativeEmail) {
		this.contactAlternativeEmail = contactAlternativeEmail;
	}
	/**
	 * @return the contactAlternativeName_1
	 */
	public String getContactAlternativeName_1() {
		return contactAlternativeName_1;
	}
	/**
	 * @param contactAlternativeName_1 the contactAlternativeName_1 to set
	 */
	public void setContactAlternativeName_1(String contactAlternativeName_1) {
		this.contactAlternativeName_1 = contactAlternativeName_1;
	}
	/**
	 * @return the contactAlternativeTelephone_1
	 */
	public String getContactAlternativeTelephone_1() {
		return contactAlternativeTelephone_1;
	}
	/**
	 * @param contactAlternativeTelephone_1 the contactAlternativeTelephone_1 to set
	 */
	public void setContactAlternativeTelephone_1(
			String contactAlternativeTelephone_1) {
		this.contactAlternativeTelephone_1 = contactAlternativeTelephone_1;
	}
	/**
	 * @return the contactAlternativeEmail_1
	 */
	public String getContactAlternativeEmail_1() {
		return contactAlternativeEmail_1;
	}
	/**
	 * @param contactAlternativeEmail_1 the contactAlternativeEmail_1 to set
	 */
	public void setContactAlternativeEmail_1(String contactAlternativeEmail_1) {
		this.contactAlternativeEmail_1 = contactAlternativeEmail_1;
	}
	/**
	 * @return the additionalInformation
	 */
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	/**
	 * @param additionalInformation the additionalInformation to set
	 */
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	/**
	 * @return the fundingPledgesDetails
	 */
	public Set<FundingPledgesDetails> getFundingPledgesDetails() {
		return fundingPledgesDetails;
	}
	/**
	 * @param fundingPledgesDetails the fundingPledgesDetails to set
	 */
	public void setFundingPledgesDetails(Set<FundingPledgesDetails> fundingPledgesDetails) {
		this.fundingPledgesDetails = fundingPledgesDetails;
	}
	/**
	 * @return the whoAuthorizedPledge
	 */
	public String getWhoAuthorizedPledge() {
		return whoAuthorizedPledge;
	}
	/**
	 * @param whoAuthorizedPledge the whoAuthorizedPledge to set
	 */
	public void setWhoAuthorizedPledge(String whoAuthorizedPledge) {
		this.whoAuthorizedPledge = whoAuthorizedPledge;
	}
	/**
	 * @return the furtherApprovalNedded
	 */
	public String getFurtherApprovalNedded() {
		return furtherApprovalNedded;
	}
	/**
	 * @param furtherApprovalNedded the furtherApprovalNedded to set
	 */
	public void setFurtherApprovalNedded(String furtherApprovalNedded) {
		this.furtherApprovalNedded = furtherApprovalNedded;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public TreeSet<String> getYearsList() {
		return yearsList;
	}
	public void setYearsList(TreeSet<String> yearsList) {
		this.yearsList = yearsList;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof FundingPledges))
			return false;
		FundingPledges p = (FundingPledges) o; 
		if (p == null)
			return false;
		return this.getId().equals(p.getId());
	}
	
	@Override
	public int compareTo(FundingPledges o) {
		return (int) (this.getId() - o.getId());
	}
}