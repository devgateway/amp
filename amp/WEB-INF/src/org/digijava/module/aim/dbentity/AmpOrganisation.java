package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorDimension;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpOrganisation implements Comparable, Serializable, Identifiable, ARDimensionable
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
        @Deprecated
	private AmpSectorScheme ampSecSchemeId;
	
	private String budgetOrgCode;
	
	private String acronymAndName;
	/**
	 * @deprecated
	 */
	private String orgTypeCode; // defunct
	
	private AmpOrgType orgTypeId;
	private AmpOrgGroup orgGrpId;
	private String address;
        @Deprecated
	private Country countryId;
	private String contactPersonName;
	private String contactPersonTitle;
	private String email;
	private String phone;
	private String fax;
	private String orgUrl;
	private String acronym;
	private AmpLevel levelId;
	private AmpCategoryValueLocations region;
        private AmpCategoryValue implemLocationLevel;
        private Set<AmpOrgLocation> locations;
        private Set<AmpOrgStaffInformation> staffInfos;
        private AmpCategoryValueLocations country;
        private Set<AmpOrganisation> recipients;
        private Set<AmpContact> contacts;
        private String addressAbroad;
        private Double programAnnualBudget;
        private AmpCurrency programAnnualBudgetCurr;
        private Double adminAnnualBudget;
        private AmpCurrency adminAnnualBudgetCurr;
        private Double programAnnualPercent;
        private Long taxNumber;
        private String primaryPurpose;
        private String minPlanRegNumb;
        private String legalPersonNum;
        private Date legalPersonRegDate;
        private Date minPlanRegDate;

        public AmpCategoryValue getImplemLocationLevel() {
            return implemLocationLevel;
        }

        public void setImplemLocationLevel(AmpCategoryValue implemLocationLevel) {
            this.implemLocationLevel = implemLocationLevel;
        }

        public String getPrimaryPurpose() {
            return primaryPurpose;
        }

        public void setPrimaryPurpose(String primaryPurpose) {
            this.primaryPurpose = primaryPurpose;
        }

        public String getAddressAbroad() {
            return addressAbroad;
        }

        public void setAddressAbroad(String addressAbroad) {
            this.addressAbroad = addressAbroad;
        }

        public Double getAdminAnnualBudget() {
            return adminAnnualBudget;
        }

        public void setAdminAnnualBudget(Double adminAnnualBudget) {
            this.adminAnnualBudget = adminAnnualBudget;
        }

        public AmpCurrency getAdminAnnualBudgetCurr() {
            return adminAnnualBudgetCurr;
        }

        public void setAdminAnnualBudgetCurr(AmpCurrency adminAnnualBudgetCurr) {
            this.adminAnnualBudgetCurr = adminAnnualBudgetCurr;
        }

        public Set<AmpContact> getContacts() {
            return contacts;
        }

        public void setContacts(Set<AmpContact> contacts) {
            this.contacts = contacts;
        }

        public AmpCategoryValueLocations getCountry() {
            return country;
        }

        public void setCountry(AmpCategoryValueLocations country) {
            this.country = country;
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

        public Set<AmpOrgLocation> getLocations() {
            return locations;
        }

        public void setLocations(Set<AmpOrgLocation> locations) {
            this.locations = locations;
        }

        public Date getMinPlanRegDate() {
            return minPlanRegDate;
        }

        public void setMinPlanRegDate(Date minPlanRegDate) {
            this.minPlanRegDate = minPlanRegDate;
        }

        public Double getProgramAnnualBudget() {
            return programAnnualBudget;
        }

        public void setProgramAnnualBudget(Double programAnnualBudget) {
            this.programAnnualBudget = programAnnualBudget;
        }

        public AmpCurrency getProgramAnnualBudgetCurr() {
            return programAnnualBudgetCurr;
        }

        public void setProgramAnnualBudgetCurr(AmpCurrency programAnnualBudgetCurr) {
            this.programAnnualBudgetCurr = programAnnualBudgetCurr;
        }

        public Double getProgramAnnualPercent() {
            return programAnnualPercent;
        }

        public void setProgramAnnualPercent(Double programAnnualPercent) {
            this.programAnnualPercent = programAnnualPercent;
        }

        public Set<AmpOrganisation> getRecipients() {
            return recipients;
        }

        public void setRecipients(Set<AmpOrganisation> recipients) {
            this.recipients = recipients;
        }

        public String getMinPlanRegNumb() {
            return minPlanRegNumb;
        }

        public void setMinPlanRegNumb(String regNumbMinPlan) {
            this.minPlanRegNumb = regNumbMinPlan;
        }

        public Set<AmpOrgStaffInformation> getStaffInfos() {
            return staffInfos;
        }

        public void setStaffInfos(Set<AmpOrgStaffInformation> staffInfos) {
            this.staffInfos = staffInfos;
        }

        public Long getTaxNumber() {
            return taxNumber;
        }

        public void setTaxNumber(Long taxNumber) {
            this.taxNumber = taxNumber;
        }
       
	
	private Set survey;	// Collection of AmpAhsurvey dbentity objects 
	
	private transient Set surveyByPointOfDeliveryDonor;
	
	public Set getSurveyByPointOfDeliveryDonor() {
		return surveyByPointOfDeliveryDonor;
	}

	public void setSurveyByPointOfDeliveryDonor(Set surveyByPointOfDeliveryDonor) {
		this.surveyByPointOfDeliveryDonor = surveyByPointOfDeliveryDonor;
	}

	private Set calendar;
	
	private String segmentCode;
	private Set sectors;
	
	private Set<AmpOrganisationDocument> documents;

	//Pledges
	private Set fundingDetails;
	
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
	
	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		
		if (!(obj instanceof AmpOrganisation)) throw new ClassCastException();
		
		AmpOrganisation temp = (AmpOrganisation) obj;
		if (temp.getAmpOrgId() == null)
			return false;
		return (temp.getAmpOrgId().equals(this.getAmpOrgId()));
	}
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
	 * @return Returns the contactPersonName.
	 */
	public String getContactPersonName() {
		return contactPersonName;
	}
	/**
	 * @param contactPersonName The contactPersonName to set.
	 */
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	/**
	 * @return Returns the contactPersonTitle.
	 */
	public String getContactPersonTitle() {
		return contactPersonTitle;
	}
	/**
	 * @param contactPersonTitle The contactPersonTitle to set.
	 */
	public void setContactPersonTitle(String contactPersonTitle) {
		this.contactPersonTitle = contactPersonTitle;
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
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
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
	 * @return Returns the orgTypeId.
	 */
	public AmpOrgType getOrgTypeId() {
		return orgTypeId;
	}
	/**
	 * @param orgTypeId The orgTypeId to set.
	 */
	public void setOrgTypeId(AmpOrgType orgTypeId) {
		this.orgTypeId = orgTypeId;
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
	 * @return Returns the phone.
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone The phone to set.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return Returns the regionId.
	 */
	public AmpCategoryValueLocations getRegion() {
		return region;
	}
	/**
	 * @param regionId The regionId to set.
	 */
	public void setRegion(AmpCategoryValueLocations region) {
		this.region = region;
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

	
	
	
}	
