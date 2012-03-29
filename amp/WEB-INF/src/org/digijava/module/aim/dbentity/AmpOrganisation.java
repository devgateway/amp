package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorDimension;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpOrganisation implements Comparable, Serializable, Identifiable, ARDimensionable, HierarchyListable
{
	private Long ampOrgId;
	private String name ;
	/**
	 * @deprecated
	 */
	private String orgType;  // defunct
	private String dacOrgCode ;
	private String orgIsoCode;
	private String description ;
	private String orgCode;
	private String orgGroup;  // defunct
	private AmpFiscalCalendar ampFiscalCalId;
	private AmpSectorScheme ampSecSchemeId;
	private String fundingorgid;
	
	private String budgetOrgCode;
	
	private String acronymAndName;
	/**
	 * @deprecated
	 */
	private String orgTypeCode; // defunct
	
	private AmpOrgGroup orgGrpId;
	private String address;
	private Country countryId;
	private String orgUrl;
	private String acronym;
	private AmpLevel levelId;
	
	private AmpCategoryValueLocations region;
    private AmpCategoryValue implemLocationLevel;
    private Set<AmpOrgLocation> locations;
    private Set<AmpOrgStaffInformation> staffInfos;
    private AmpCategoryValueLocations country;
    private Set<AmpOrgRecipient> recipients;
    
    private String addressAbroad;
    private String taxNumber;
    private String primaryPurpose;
    private String minPlanRegNumb;
    private String legalPersonNum;
    private Date legalPersonRegDate;
    private Date minPlanRegDate;
    private Set<AmpOrganizationBudgetInformation> organizationBudgetInfos;
	
	private Set survey;	// Collection of AmpAhsurvey dbentity objects
	private Set calendar;
	
	private String segmentCode;
	private Set sectors;
	
	private Set<AmpOrganisationDocument> documents;

	//Pledges
	private Set fundingDetails;
	
	private String otherInformation;
    private Date lineMinRegDate;
    private Date operFuncApprDate;
    private String receiptLegPersonalityAct;
	
	private transient Set surveyByPointOfDeliveryDonor;

	private Set<AmpOrganisationContact> organizationContacts;
	
    // this field is saved in org. profile and not from organization manager in admin
    private String orgBackground;
    /*
     *this field is saved in org. profile and not from organization manager in admin
     * don't confuse it with Description field
     */
    private String orgDescription;
    
    //Budget fields
    private AmpBudgetSector parentsector;
    private Set<AmpDepartments> departments;
    private Set<AmpBudgetSector> budgetsectors;
    private String  lineMinRegNumber;
    
    private boolean translateable	= true;

	public String getLineMinRegNumber() {
        return lineMinRegNumber;
    }

    public void setLineMinRegNumber(String lineMinRegNumber) {
        this.lineMinRegNumber = lineMinRegNumber;
    }

       
    public Set<AmpBudgetSector> getBudgetsectors() {
		return budgetsectors;
	}

	public void setBudgetsectors(Set<AmpBudgetSector> budgetsectors) {
		this.budgetsectors = budgetsectors;
	}

	public Set<AmpDepartments> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<AmpDepartments> departments) {
		this.departments = departments;
	}

	public AmpBudgetSector getParentsector() {
		return parentsector;
	}

	public void setParentsector(AmpBudgetSector parentsector) {
		this.parentsector = parentsector;
	}

	public String getOrgBackground() {
        return orgBackground;
    }

    public void setOrgBackground(String orgBackground) {
        this.orgBackground = orgBackground;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }
	
	public Set getSurveyByPointOfDeliveryDonor() {
		return surveyByPointOfDeliveryDonor;
	}

	public void setSurveyByPointOfDeliveryDonor(Set surveyByPointOfDeliveryDonor) {
		this.surveyByPointOfDeliveryDonor = surveyByPointOfDeliveryDonor;
	}

	
	
	//
	public Set getSectors() {
		return sectors;
	}

	public void setSectors(Set sectors) {
		this.sectors = sectors;
	}

	public String getSegmentCode() {
		return segmentCode;
	}

	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}

	/**
	 * @return
	 */
	public Long getAmpOrgId() {
		return ampOrgId;
	}

	/**
	 * @return
	 */
	public String getDacOrgCode() {
		return dacOrgCode;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getAcronym() {
		return acronym;
	}

	/**
	 * @return
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @return
	 */
	public String getOrgIsoCode() {
		return orgIsoCode;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getOrgType() {
		return orgType;
	}

	/**
	 * @deprecated
	 * @param long1
	 */
	public void setAmpOrgId(Long long1) {
		ampOrgId = long1;
	}

	/**
	 * @param string
	 */
	public void setDacOrgCode(String string) {
		dacOrgCode = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	public void setAcronym(String string) {
		acronym = string;
	}

	/**
	 * @param string
	 */
	public void setOrgCode(String string) {
		orgCode = string;
	}

	/**
	 * @param string
	 */
	public void setOrgIsoCode(String string) {
		orgIsoCode = string;
	}

	/**
	 * @param string
	 */
	public void setOrgType(String string) {
		orgType = string;
	}

	public AmpFiscalCalendar getAmpFiscalCalId() {
		return ampFiscalCalId;
	}

	public void setAmpFiscalCalId(AmpFiscalCalendar Cal) {
		this.ampFiscalCalId = Cal;
	}

	/**
	 * @return
	 */
	public String getOrgGroup() {
		return orgGroup;
	}

	/**
	 * @param string
	 */
	public void setOrgGroup(String string) {
		orgGroup = string;
	}

	/**
	 * @return
	 */
	public AmpSectorScheme getAmpSecSchemeId() {
		return ampSecSchemeId;
	}

	/**
	 * @param scheme
	 */
	public void setAmpSecSchemeId(AmpSectorScheme scheme) {
		ampSecSchemeId = scheme;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getOrgTypeCode() {
		return orgTypeCode;
	} 

	/**
	 * @deprecated
	 * @return
	 */
	public void setOrgTypeCode(String string) {
		orgTypeCode = string;
	}
	
	public int compareTo(Object obj) {
		
		if (!(obj instanceof AmpOrganisation)) 
			throw new ClassCastException();
		
		AmpOrganisation org = (AmpOrganisation) obj;
		if (this.name != null) {
			if (org.name != null) {
				return (this.name.trim().toLowerCase().
						compareTo(org.name.trim().toLowerCase()));
			} else {
				return (this.name.trim().toLowerCase().
						compareTo(""));
			}
		} else {
			if (org.name != null) {
				return ("".compareTo(org.name.trim().toLowerCase()));
			} else {
				return 0;
			}			
		}
	}
	
	//do not implement equals, implement compareTo and use treeset!
	
	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * @return Returns the countryId.
	 */
	public Country getCountryId() {
		return countryId;
	}
	/**
	 * @param countryId The countryId to set.
	 */
	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}
	
	/**
	 * @return Returns the levelId.
	 */
	public AmpLevel getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId The levelId to set.
	 */
	public void setLevelId(AmpLevel levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return Returns the orgGrpId.
	 */
	public AmpOrgGroup getOrgGrpId() {
		return orgGrpId;
	}
	/**
	 * @param orgGrpId The orgGrpId to set.
	 */
	public void setOrgGrpId(AmpOrgGroup orgGrpId) {
		this.orgGrpId = orgGrpId;
	}
	/**
	 * @return Returns the orgUrl.
	 */
	public String getOrgUrl() {
		return orgUrl;
	}
	/**
	 * @param orgUrl The orgUrl to set.
	 */
	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}
	/**
	 * @return Returns the survey.
	 */
	public Set getSurvey() {
		return survey;
	}
	/**
	 * @param survey The survey to set.
	 */
	public void setSurvey(Set survey) {
		this.survey = survey;
	}

	public Set getCalendar() {
		return calendar;
	}

	public void setCalendar(Set calendar) {
		this.calendar = calendar;
	}


	
	public String toString() {
		return this.getName();
	}

	public Object getIdentifier() {
		return this.getAmpOrgId();
	}

	public Set getFundingDetails() {
		return fundingDetails;
	}

	public void setFundingDetails(Set fundingDetails) {
		this.fundingDetails = fundingDetails;
	}

	public Class getDimensionClass() {
	    return DonorDimension.class;
	}

	public Set<AmpOrganisationDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<AmpOrganisationDocument> documents) {
		this.documents = documents;
	}
	
	/**
	 * 
	 * @return If this organization has a SINGLE document attached then it returns the uuid 
	 * of that document. In any other case it returns null.
	 * 
	 */
	public String getUniqueDocumentId() {
		if ( ampOrgId!=null && documents!=null && documents.size()==1 ) {
			Iterator<AmpOrganisationDocument> iter	= documents.iterator();
			return iter.next().getUuid();
		}
		
			
		return null;
	}

	public String getAcronymAndName() {
	acronymAndName = (acronym != null) ? acronym + " - " + name
		: name;
	return acronymAndName;
    }

    public void setAcronymAndName(String acronymAndName) {
	this.acronymAndName = acronymAndName;
    }

	public String getBudgetOrgCode() {
		return budgetOrgCode;
	}

	public void setBudgetOrgCode(String budgetOrgCode) {
		this.budgetOrgCode = budgetOrgCode;
	}

	public AmpCategoryValueLocations getRegion() {
		return region;
	}

	public void setRegion(AmpCategoryValueLocations region) {
		this.region = region;
	}

	public AmpCategoryValue getImplemLocationLevel() {
		return implemLocationLevel;
	}

	public void setImplemLocationLevel(AmpCategoryValue implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
	}

	public Set<AmpOrgLocation> getLocations() {
		return locations;
	}

	public void setLocations(Set<AmpOrgLocation> locations) {
		this.locations = locations;
	}

	public Set<AmpOrgStaffInformation> getStaffInfos() {
		return staffInfos;
	}

	public void setStaffInfos(Set<AmpOrgStaffInformation> staffInfos) {
		this.staffInfos = staffInfos;
	}

	public AmpCategoryValueLocations getCountry() {
		return country;
	}

	public void setCountry(AmpCategoryValueLocations country) {
		this.country = country;
	}

	public Set<AmpOrgRecipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<AmpOrgRecipient> recipients) {
		this.recipients = recipients;
	}

	public String getAddressAbroad() {
		return addressAbroad;
	}

	public void setAddressAbroad(String addressAbroad) {
		this.addressAbroad = addressAbroad;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getPrimaryPurpose() {
		return primaryPurpose;
	}

	public void setPrimaryPurpose(String primaryPurpose) {
		this.primaryPurpose = primaryPurpose;
	}

	public String getMinPlanRegNumb() {
		return minPlanRegNumb;
	}

	public void setMinPlanRegNumb(String minPlanRegNumb) {
		this.minPlanRegNumb = minPlanRegNumb;
	}

	public String getLegalPersonNum() {
		return legalPersonNum;
	}

	public void setLegalPersonNum(String legalPersonNum) {
		this.legalPersonNum = legalPersonNum;
	}

	public Date getLegalPersonRegDate() {
		return legalPersonRegDate;
	}

	public void setLegalPersonRegDate(Date legalPersonRegDate) {
		this.legalPersonRegDate = legalPersonRegDate;
	}

	public Date getMinPlanRegDate() {
		return minPlanRegDate;
	}

	public void setMinPlanRegDate(Date minPlanRegDate) {
		this.minPlanRegDate = minPlanRegDate;
	}

	public Set<AmpOrganizationBudgetInformation> getOrganizationBudgetInfos() {
		return organizationBudgetInfos;
	}

	public void setOrganizationBudgetInfos(
			Set<AmpOrganizationBudgetInformation> organizationBudgetInfos) {
		this.organizationBudgetInfos = organizationBudgetInfos;
	}

	public String getOtherInformation() {
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}

	public Date getLineMinRegDate() {
		return lineMinRegDate;
	}

	public void setLineMinRegDate(Date lineMinRegDate) {
		this.lineMinRegDate = lineMinRegDate;
	}

	public Date getOperFuncApprDate() {
		return operFuncApprDate;
	}

	public void setOperFuncApprDate(Date operFuncApprDate) {
		this.operFuncApprDate = operFuncApprDate;
	}

	public String getReceiptLegPersonalityAct() {
		return receiptLegPersonalityAct;
	}

	public void setReceiptLegPersonalityAct(String receiptLegPersonalityAct) {
		this.receiptLegPersonalityAct = receiptLegPersonalityAct;
	}

	public void setOrganizationContacts(Set<AmpOrganisationContact> organizationContacts) {
		this.organizationContacts = organizationContacts;
	}

	public Set<AmpOrganisationContact> getOrganizationContacts() {
		return organizationContacts;
	}
	
	public String getFundingorgid() {
		return fundingorgid;
	}

	public void setFundingorgid(String fundingorgid) {
		this.fundingorgid = fundingorgid;
	}
	
	@Override
	public Collection<AmpOrganisation> getChildren() {
		return null;
	}

	@Override
	public int getCountDescendants() {
		return 1;
	}

	@Override
	public String getLabel() {
		return this.name;
	}

	@Override
	public String getUniqueId() {
        return String.valueOf(this.ampOrgId.longValue());
	}

	@Override
	public boolean getTranslateable() {
		
		return this.translateable;
	}

	@Override
	public void setTranslateable(boolean translateable) {
		this.translateable	= translateable;
		
	}

    public String getAdditionalSearchString() {
        return this.acronym;
    }
	
	
}	
