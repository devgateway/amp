package org.digijava.module.fundingpledges.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.springframework.beans.BeanWrapperImpl;

public class PledgeForm extends ActionForm implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long pledgeId;
	/**
	 * @return the pledgeId
	 */
	public Long getPledgeId() {
		return pledgeId;
	}

	/**
	 * @param pledgeId the pledgeId to set
	 */
	public void setPledgeId(Long pledgeId) {
		this.pledgeId = pledgeId;
	}

	private FundingPledges fundingPledges;
	private String selectedOrgId;
	private String selectedOrgName;
	private AmpCategoryValue pledgeTitle;
	private Collection<AmpCurrency> validcurrencies;
	private String currencyCode;
	private String contact1Name;
	private String contact1Title;
	private String contact1OrgName;
	private String contact1OrgId;
	private String contact1Ministry;
	private String contact1Address;
	private String contact1Telephone;
	private String contact1Email;
	private String contact1Fax;
	private String contactAlternate1Name;
	private String contactAlternate1Email;
	private String contactAlternate1Telephone;
	
	private String contact2Name;
	private String Contact2Title;
	private String contact2OrgName;
	private String contact2OrgId;
	private String contact2Ministry;
	private String contact2Address;
	private String contact2Telephone;
	private String contact2Email;
	private String contact2Fax;
	
	private String contactAlternate2Name;
	private String contactAlternate2Email;
	private String contactAlternate2Telephone;
	
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;
	private Collection<ActivitySector> pledgeSectors;
	private Collection<FundingPledgesDetails> fundingPledgesDetails;
	private Collection<AmpCategoryValue> pledgeTypeCategory = null;
	private Collection<AmpCategoryValue> assistanceTypeCategory = null;
	private Collection<AmpCategoryValue> aidModalityCategory = null;
	private String defaultCurrency;
	private Collection<AmpCategoryValue> pledgeNames;
	private Long pledgeTitleId;
	private Collection<String> years;
	private String year;
	
	/*Fields for Location*/
	private boolean noMoreRecords=false;
	private Long implemLocationLevel = null;
	private Integer impLevelValue; // Implementation Level value
	private Long levelId = null;
	private Long parentLocId;
	private boolean defaultCountryIsSet;
	private Collection<FundingPledgesLocation> selectedLocs = null;
	private Long [] userSelectedLocs;
	private TreeMap<Integer, Collection<KeyValue>> locationByLayers;
	private TreeMap<Integer, Long> selectedLayers ;
	
	/*Fields for program*/
	private int programType;
	private List programLevels;
	private Long selPrograms[];
	private Collection<FundingPledgesProgram> selectedProgs = null;
	private AmpActivityProgramSettings nationalSetting;
	
	public Collection<String> getYears() {
		return years;
	}

	public void setYears(Collection<String> years) {
		this.years = years;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getPledgeTitleId() {
		return pledgeTitleId;
	}

	public void setPledgeTitleId(Long pledgeTitleId) {
		this.pledgeTitleId = pledgeTitleId;
		this.pledgeTitle = CategoryManagerUtil.getAmpCategoryValueFromDb(this.pledgeTitleId);
	}
	
	/**
	 * @return the pledgeNames
	 */
	public Collection<AmpCategoryValue> getPledgeNames() {
		return pledgeNames;
	}

	/**
	 * @param pledgeNames the pledgeNames to set
	 */
	public void setPledgeNames(Collection<AmpCategoryValue> pledgeNames) {
		this.pledgeNames = pledgeNames;
	}

	/**
	 * @return the defaultCurrency
	 */
	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	/**
	 * @param defaultCurrency the defaultCurrency to set
	 */
	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	/**
	 * @return the fundingPledges
	 */
	public FundingPledges getFundingPledges() {
		return fundingPledges;
	}

	/**
	 * @param fundingPledges the fundingPledges to set
	 */
	public void setFundingPledges(FundingPledges fundingPledges) {
		this.fundingPledges = fundingPledges;
	}

	/**
	 * @return the pledgeTypeCategory
	 */
	public Collection<AmpCategoryValue> getPledgeTypeCategory() {
		return pledgeTypeCategory;
	}

	/**
	 * @param pledgeTypeCategory the pledgeTypeCategory to set
	 */
	public void setPledgeTypeCategory(Collection<AmpCategoryValue> pledgeTypeCategory) {
		this.pledgeTypeCategory = pledgeTypeCategory;
	}

	/**
	 * @return the assistanceTypeCategory
	 */
	public Collection<AmpCategoryValue> getAssistanceTypeCategory() {
		return assistanceTypeCategory;
	}

	/**
	 * @param assistanceTypeCategory the assistanceTypeCategory to set
	 */
	public void setAssistanceTypeCategory(
			Collection<AmpCategoryValue> assistanceTypeCategory) {
		this.assistanceTypeCategory = assistanceTypeCategory;
	}

	/**
	 * @return the aidModalityCategory
	 */
	public Collection<AmpCategoryValue> getAidModalityCategory() {
		return aidModalityCategory;
	}

	/**
	 * @param aidModalityCategory the aidModalityCategory to set
	 */
	public void setAidModalityCategory(Collection<AmpCategoryValue> aidModalityCategory) {
		this.aidModalityCategory = aidModalityCategory;
	}

	private String fundingEvent;
	private Long selectedFunding[] = null;
	private Collection<AmpCategoryValue> pledgestypes;
	
	public Collection<AmpCategoryValue> getPledgestypes() {
		return pledgestypes;
	}

	public void setPledgestypes(Collection<AmpCategoryValue> pledgestypes) {
		this.pledgestypes = pledgestypes;
	}


	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Collection<AmpCurrency> getValidcurrencies() {
		return validcurrencies;
	}

	public void setValidcurrencies(Collection<AmpCurrency> validcurrencies) {
		this.validcurrencies = validcurrencies;
	}
	
	/**
	 * @return the selectedFunding
	 */
	public Long[] getSelectedFunding() {
		return selectedFunding;
	}

	/**
	 * @param selectedFunding the selectedFunding to set
	 */
	public void setSelectedFunding(Long[] selectedFunding) {
		this.selectedFunding = selectedFunding;
	}

	/**
	 * @return the selectedOrgId
	 */
	public String getSelectedOrgId() {
		return selectedOrgId;
	}
	/**
	 * @param selectedOrgId the selectedOrgId to set
	 */
	public void setSelectedOrgId(String selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}
	/**
	 * @return the selectedOrgName
	 */
	public String getSelectedOrgName() {
		return selectedOrgName;
	}
	/**
	 * @param selectedOrgName the selectedOrgName to set
	 */
	public void setSelectedOrgName(String selectedOrgName) {
		this.selectedOrgName = selectedOrgName;
	}
	/**
	 * @return the pledgeTitle
	 */
	public AmpCategoryValue getPledgeTitle() {
		return pledgeTitle;
	}
	/**
	 * @param pledgeTitle the pledgeTitle to set
	 */
	public void setPledgeTitle(AmpCategoryValue pledgeTitle) {
		this.pledgeTitle = pledgeTitle;
	}
	/**
	 * @return the contact1Name
	 */
	public String getContact1Name() {
		return contact1Name;
	}
	/**
	 * @param contact1Name the contact1Name to set
	 */
	public void setContact1Name(String contact1Name) {
		this.contact1Name = contact1Name;
	}
	/**
	 * @return the contact1Title
	 */
	public String getContact1Title() {
		return contact1Title;
	}
	/**
	 * @param contact1Title the contact1Title to set
	 */
	public void setContact1Title(String contact1Title) {
		this.contact1Title = contact1Title;
	}
	/**
	 * @return the contact1OrgName
	 */
	public String getContact1OrgName() {
		return contact1OrgName;
	}
	/**
	 * @param contact1OrgName the contact1OrgName to set
	 */
	public void setContact1OrgName(String contact1OrgName) {
		this.contact1OrgName = contact1OrgName;
	}
	/**
	 * @return the contact1OrgId
	 */
	public String getContact1OrgId() {
		return contact1OrgId;
	}
	/**
	 * @param contact1OrgId the contact1OrgId to set
	 */
	public void setContact1OrgId(String contact1OrgId) {
		this.contact1OrgId = contact1OrgId;
	}
	/**
	 * @return the contact1Ministry
	 */
	public String getContact1Ministry() {
		return contact1Ministry;
	}
	/**
	 * @param contact1Ministry the contact1Ministry to set
	 */
	public void setContact1Ministry(String contact1Ministry) {
		this.contact1Ministry = contact1Ministry;
	}
	/**
	 * @return the contact1Address
	 */
	public String getContact1Address() {
		return contact1Address;
	}
	/**
	 * @param contact1Address the contact1Address to set
	 */
	public void setContact1Address(String contact1Address) {
		this.contact1Address = contact1Address;
	}
	/**
	 * @return the contact1Telephone
	 */
	public String getContact1Telephone() {
		return contact1Telephone;
	}
	/**
	 * @param contact1Telephone the contact1Telephone to set
	 */
	public void setContact1Telephone(String contact1Telephone) {
		this.contact1Telephone = contact1Telephone;
	}
	/**
	 * @return the contact1Email
	 */
	public String getContact1Email() {
		return contact1Email;
	}
	/**
	 * @param contact1Email the contact1Email to set
	 */
	public void setContact1Email(String contact1Email) {
		this.contact1Email = contact1Email;
	}
	/**
	 * @return the contact1Fax
	 */
	public String getContact1Fax() {
		return contact1Fax;
	}
	/**
	 * @param contact1Fax the contact1Fax to set
	 */
	public void setContact1Fax(String contact1Fax) {
		this.contact1Fax = contact1Fax;
	}
	/**
	 * @return the contactAlternate1Name
	 */
	public String getContactAlternate1Name() {
		return contactAlternate1Name;
	}
	/**
	 * @param contactAlternate1Name the contactAlternate1Name to set
	 */
	public void setContactAlternate1Name(String contactAlternate1Name) {
		this.contactAlternate1Name = contactAlternate1Name;
	}
	/**
	 * @return the contactAlternate1Email
	 */
	public String getContactAlternate1Email() {
		return contactAlternate1Email;
	}
	/**
	 * @param contactAlternate1Email the contactAlternate1Email to set
	 */
	public void setContactAlternate1Email(String contactAlternate1Email) {
		this.contactAlternate1Email = contactAlternate1Email;
	}
	/**
	 * @return the contactAlternate1Telephone
	 */
	public String getContactAlternate1Telephone() {
		return contactAlternate1Telephone;
	}
	/**
	 * @param contactAlternate1Telephone the contactAlternate1Telephone to set
	 */
	public void setContactAlternate1Telephone(String contactAlternate1Telephone) {
		this.contactAlternate1Telephone = contactAlternate1Telephone;
	}
	/**
	 * @return the contact2Name
	 */
	public String getContact2Name() {
		return contact2Name;
	}
	/**
	 * @param contact2Name the contact2Name to set
	 */
	public void setContact2Name(String contact2Name) {
		this.contact2Name = contact2Name;
	}
	/**
	 * @return the contact2Title
	 */
	public String getContact2Title() {
		return Contact2Title;
	}
	/**
	 * @param contact2Title the contact2Title to set
	 */
	public void setContact2Title(String contact2Title) {
		Contact2Title = contact2Title;
	}
	/**
	 * @return the contact2OrgName
	 */
	public String getContact2OrgName() {
		return contact2OrgName;
	}
	/**
	 * @param contact2OrgName the contact2OrgName to set
	 */
	public void setContact2OrgName(String contact2OrgName) {
		this.contact2OrgName = contact2OrgName;
	}
	/**
	 * @return the contact2OrgId
	 */
	public String getContact2OrgId() {
		return contact2OrgId;
	}
	/**
	 * @param contact2OrgId the contact2OrgId to set
	 */
	public void setContact2OrgId(String contact2OrgId) {
		this.contact2OrgId = contact2OrgId;
	}
	/**
	 * @return the contact2Ministry
	 */
	public String getContact2Ministry() {
		return contact2Ministry;
	}
	/**
	 * @param contact2Ministry the contact2Ministry to set
	 */
	public void setContact2Ministry(String contact2Ministry) {
		this.contact2Ministry = contact2Ministry;
	}
	/**
	 * @return the contact2Address
	 */
	public String getContact2Address() {
		return contact2Address;
	}
	/**
	 * @param contact2Address the contact2Address to set
	 */
	public void setContact2Address(String contact2Address) {
		this.contact2Address = contact2Address;
	}
	/**
	 * @return the contact2Telephone
	 */
	public String getContact2Telephone() {
		return contact2Telephone;
	}
	/**
	 * @param contact2Telephone the contact2Telephone to set
	 */
	public void setContact2Telephone(String contact2Telephone) {
		this.contact2Telephone = contact2Telephone;
	}
	/**
	 * @return the contact2Email
	 */
	public String getContact2Email() {
		return contact2Email;
	}
	/**
	 * @param contact2Email the contact2Email to set
	 */
	public void setContact2Email(String contact2Email) {
		this.contact2Email = contact2Email;
	}
	/**
	 * @return the contact2Fax
	 */
	public String getContact2Fax() {
		return contact2Fax;
	}
	/**
	 * @param contact2Fax the contact2Fax to set
	 */
	public void setContact2Fax(String contact2Fax) {
		this.contact2Fax = contact2Fax;
	}
	/**
	 * @return the contactAlternate2Name
	 */
	public String getContactAlternate2Name() {
		return contactAlternate2Name;
	}
	/**
	 * @param contactAlternate2Name the contactAlternate2Name to set
	 */
	public void setContactAlternate2Name(String contactAlternate2Name) {
		this.contactAlternate2Name = contactAlternate2Name;
	}
	/**
	 * @return the contactAlternate2Email
	 */
	public String getContactAlternate2Email() {
		return contactAlternate2Email;
	}
	/**
	 * @param contactAlternate2Email the contactAlternate2Email to set
	 */
	public void setContactAlternate2Email(String contactAlternate2Email) {
		this.contactAlternate2Email = contactAlternate2Email;
	}
	/**
	 * @return the contactAlternate2Telephone
	 */
	public String getContactAlternate2Telephone() {
		return contactAlternate2Telephone;
	}
	/**
	 * @param contactAlternate2Telephone the contactAlternate2Telephone to set
	 */
	public void setContactAlternate2Telephone(String contactAlternate2Telephone) {
		this.contactAlternate2Telephone = contactAlternate2Telephone;
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

	public ActivitySector getPledgeSectors(int index) {
		return (ActivitySector) (this.pledgeSectors.toArray()[index]);
	}
	
	/**
	 * @return the activitySectors
	 */
	public Collection<ActivitySector> getPledgeSectors() {
		return pledgeSectors;
	}

	/**
	 * @param activitySectors the activitySectors to set
	 */
	public void setPledgeSectors(Collection<ActivitySector> selectedSectors) {
		this.pledgeSectors = selectedSectors;
	}
	/*
	public FundingPledgesLocation getFundingPledgesDetails(int index) {
		return (FundingPledgesLocation) (this.fundingPledgesDetails.toArray()[index]);
	}
	*/
	/**
	 * @return the fundingPledgesDetails
	 */
	public Collection<FundingPledgesDetails> getFundingPledgesDetails() {
		return fundingPledgesDetails;
	}

	/**
	 * @param fundingDetails the fundingPledgesDetails to set
	 */
	public void setFundingPledgesDetails(Collection<FundingPledgesDetails> fundingDetails) {
		this.fundingPledgesDetails = fundingDetails;
	}

	/**
	 * @return the fundingEvent
	 */
	public String getFundingEvent() {
		return fundingEvent;
	}

	/**
	 * @param fundingEvent the fundingEvent to set
	 */
	public void setFundingEvent(String fundingEvent) {
		this.fundingEvent = fundingEvent;
	}
	
	public TreeMap<Integer, Long> getSelectedLayers() {
		if (selectedLayers == null)
			selectedLayers		= new TreeMap<Integer, Long>();
		return selectedLayers;
	}
	
	public void setSelectedLayer(String key, Object value) {
		selectedLayers.put(Integer.parseInt(key), (Long)value);
	}
	public Long getSelectedLayer(String key) {
		return selectedLayers.get( Integer.parseInt(key) );
	}
	
	public TreeMap<Integer, Collection<KeyValue>> getLocationByLayers() {
		if ( locationByLayers == null )
			locationByLayers	= new TreeMap<Integer, Collection<KeyValue>>();
		return locationByLayers;
	}

	/**
	 * @return the noMoreRecords
	 */
	public boolean isNoMoreRecords() {
		return noMoreRecords;
	}

	/**
	 * @param noMoreRecords the noMoreRecords to set
	 */
	public void setNoMoreRecords(boolean noMoreRecords) {
		this.noMoreRecords = noMoreRecords;
	}

	/**
	 * @return the implemLocationLevel
	 */
	public Long getImplemLocationLevel() {
		return implemLocationLevel;
	}

	/**
	 * @param implemLocationLevel the implemLocationLevel to set
	 */
	public void setImplemLocationLevel(Long implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
	}

	/**
	 * @return the impLevelValue
	 */
	public Integer getImpLevelValue() {
		return impLevelValue;
	}

	/**
	 * @param impLevelValue the impLevelValue to set
	 */
	public void setImpLevelValue(Integer impLevelValue) {
		this.impLevelValue = impLevelValue;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	/**
	 * @return the parentLocId
	 */
	public Long getParentLocId() {
		return parentLocId;
	}

	/**
	 * @param parentLocId the parentLocId to set
	 */
	public void setParentLocId(Long parentLocId) {
		this.parentLocId = parentLocId;
	}

	/**
	 * @return the defaultCountryIsSet
	 */
	public boolean isDefaultCountryIsSet() {
		return defaultCountryIsSet;
	}

	/**
	 * @param defaultCountryIsSet the defaultCountryIsSet to set
	 */
	public void setDefaultCountryIsSet(boolean defaultCountryIsSet) {
		this.defaultCountryIsSet = defaultCountryIsSet;
	}

	/**
	 * @return the selectedLocs
	 */
	public Collection<FundingPledgesLocation> getSelectedLocs() {
		return selectedLocs;
	}
	
	public FundingPledgesLocation getSelectedLocs(int index) {
		return (FundingPledgesLocation) (this.selectedLocs.toArray()[index]);
	}
	
	/**
	 * @param selectedLocs2 the selectedLocs to set
	 */
	public void setSelectedLocs(Collection<FundingPledgesLocation> selectedLocs2) {
		this.selectedLocs = selectedLocs2;
	}

	/**
	 * @return the userSelectedLocs
	 */
	public Long[] getUserSelectedLocs() {
		return userSelectedLocs;
	}

	/**
	 * @param userSelectedLocs the userSelectedLocs to set
	 */
	public void setUserSelectedLocs(Long[] userSelectedLocs) {
		this.userSelectedLocs = userSelectedLocs;
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

	public int getProgramType() {
		return programType;
	}

	public void setProgramType(int programType) {
		this.programType = programType;
	}

	public List getProgramLevels() {
		return programLevels;
	}

	public void setProgramLevels(List programLevels) {
		this.programLevels = programLevels;
	}

	public Long[] getSelPrograms() {
		return selPrograms;
	}

	public void setSelPrograms(Long[] selPrograms) {
		this.selPrograms = selPrograms;
	}

	public Collection<FundingPledgesProgram> getSelectedProgs() {
		return selectedProgs;
	}

	public void setSelectedProgs(Collection<FundingPledgesProgram> selectedProgs) {
		this.selectedProgs = selectedProgs;
	}
	
	public AmpActivityProgramSettings getNationalSetting() {
		return nationalSetting;
	}

	public void setNationalSetting(AmpActivityProgramSettings nationalSetting) {
		this.nationalSetting = nationalSetting;
	}

	
}
