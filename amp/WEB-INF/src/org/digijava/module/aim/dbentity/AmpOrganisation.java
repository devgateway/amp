package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.OrganisationValueProvider;
import org.digijava.kernel.dbentity.Country;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.donorReport.OrganizationReportColumn;
import org.digijava.module.aim.helper.donorReport.PropertyType;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@TranslatableClass (displayName = "Organisation")
@InterchangeableValue(OrganisationValueProvider.class)
public class AmpOrganisation implements Comparable<AmpOrganisation>, Identifiable, Serializable, ARDimensionable, HierarchyListable, NameableOrIdentifiable
{
    
    //IATI-check: not to be ignored. 
    @Interchangeable(fieldTitle="Organization ID", id=true)
    private Long ampOrgId;
    @TranslatableField
    @Interchangeable(fieldTitle="Name", value=true)
    private String name;
    /**
     * @deprecated
     */
    private String orgType;  // defunct
    @Interchangeable(fieldTitle="DAC Organization Code")
    private String dacOrgCode;
    @Interchangeable(fieldTitle="Organization ISO Code")
    private String orgIsoCode;
    @Interchangeable(fieldTitle="Description")
    @TranslatableField
    private String description;
    @Interchangeable(fieldTitle="Organization Code")
    private String orgCode;
    
    @Deprecated
    private String orgGroup;  // defunct
//  @Interchangeable(fieldTitle="AMP Fiscal Calendar")
    private AmpFiscalCalendar ampFiscalCalId;
//  @Interchangeable(fieldTitle="AMP Sector Scheme")
    private AmpSectorScheme ampSecSchemeId;
//  @Interchangeable(fieldTitle="Funding Organization ID")
    private String fundingorgid;
    @Interchangeable(fieldTitle="Deleted")
    private Boolean deleted;
    
    @Interchangeable(fieldTitle="Budget Organization Code")
    private String budgetOrgCode;
    
    @Interchangeable(fieldTitle="Acronym And Name")
    private String acronymAndName;
    /**
     * @deprecated
     */
    private String orgTypeCode; // defunct
    
//  @Interchangeable(fieldTitle="Organization Group")
    private AmpOrgGroup orgGrpId;
    @Interchangeable(fieldTitle="Address")
    private String address;
//  @Interchangeable(fieldTitle="Country ID")
    private Country countryId;
    @Interchangeable(fieldTitle="Organization URL")
    private String orgUrl;
    @Interchangeable(fieldTitle="Acronym")
    private String acronym;
    
//  @Interchangeable(fieldTitle="Region")
    private AmpCategoryValueLocations region;
//  @Interchangeable(fieldTitle="Implementation Location Level")
    private AmpCategoryValue implemLocationLevel;
//  @Interchangeable(fieldTitle="Locations")
    private Set<AmpOrgLocation> locations;
//  @Interchangeable(fieldTitle="Staff Infos")
    private Set<AmpOrgStaffInformation> staffInfos;
//  @Interchangeable(fieldTitle="Contry")
    private AmpCategoryValueLocations country;
//  @Interchangeable(fieldTitle="AMP Organization Recipients")
    private Set<AmpOrgRecipient> recipients;
    
    @Interchangeable(fieldTitle="Address Abroad")
    private String addressAbroad;
    @Interchangeable(fieldTitle="Tax Number")
    private String taxNumber;
    @Interchangeable(fieldTitle="Primary Purpose")
    private String primaryPurpose;
    @Interchangeable(fieldTitle="Ministry Plan Registration Number") 
    private String minPlanRegNumb;
    @Interchangeable(fieldTitle="Legal Person Number")
    private String legalPersonNum;
    @Interchangeable(fieldTitle="Legal Person Registration Date")
    private Date legalPersonRegDate;
    @Interchangeable(fieldTitle="Ministry Plan Registration Date") 
    private Date minPlanRegDate;
    @Interchangeable(fieldTitle="Organization Budget Infos")
    private Set<AmpOrganizationBudgetInformation> organizationBudgetInfos;
//  @Interchangeable(fieldTitle="Survey") there could be many items that are useless 
    private Set<AmpAhsurvey> survey;    // Collection of AmpAhsurvey dbentity objects
//  @Interchangeable(fieldTitle="Calendar") //really, this seems useless
    private Set<AmpCalendar> calendar;

    @Interchangeable(fieldTitle="Segment Code")
    private String segmentCode;
//  @Interchangeable(fieldTitle="Sectors", pickIdOnly=true)
    private Set<AmpSector> sectors;
    
//  @Interchangeable(fieldTitle="Documents")
    private Set<AmpOrganisationDocument> documents;

    @Interchangeable(fieldTitle="Other Information")
    private String otherInformation;
    @Interchangeable(fieldTitle="Line Ministry Registration Date")
    private Date lineMinRegDate;
    @Interchangeable(fieldTitle="Oper Func Approval Date") //???????????????????????????
    private Date operFuncApprDate;
    @Interchangeable(fieldTitle="Receipt Leg Personality Act") //???????????????????????
    private String receiptLegPersonalityAct;
    
    //unused anywhere
//  @Interchangeable(fieldTitle="Survey By Point Of Delivery Donor")  
    private transient Set<AmpAhsurvey> surveyByPointOfDeliveryDonor;

//  @Interchangeable(fieldTitle="Organization Contacts")
    private Set<AmpOrganisationContact> organizationContacts;
    
    // this field is saved in Organization Dashboard and not from organization manager in admin
    @Interchangeable(fieldTitle="Organization Background")
    private String orgBackground;
    /*
     *this field is saved in Organization Dashboard and not from organization manager in admin
     * don't confuse it with Description field
     */
    @Interchangeable(fieldTitle="Organization Description Dashboard")
    private String orgDescription;
    // this field is saved in Organization Dashboard and not from organization manager in admin
    @Interchangeable(fieldTitle="Organization Key Areas Dashboard")
    private String orgKeyAreas;
    
    //Budget fields
//  @Interchangeable(fieldTitle="Parent Sector", pickIdOnly = true)
    private AmpBudgetSector parentsector;
//  @Interchangeable(fieldTitle="Departments", pickIdOnly = true)
    private Set<AmpDepartments> departments;
//  @Interchangeable(fieldTitle="Budget Sectors")
    private Set<AmpBudgetSector> budgetsectors;
//  @Interchangeable(fieldTitle="Line Ministry Registration Number")
    private String  lineMinRegNumber;
    
    private boolean translateable   = true;
    
    @OrganizationReportColumn(columnName="Line Ministry Registration Number",propertyType=PropertyType.NGO)
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

    
    
    @OrganizationReportColumn(columnName="Sector Preferences", propertyType=PropertyType.BOTH)
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
    @OrganizationReportColumn(columnName="DAC Code")
    public String getDacOrgCode() {
        return dacOrgCode;
    }

    /**
     * @return
     */
    @OrganizationReportColumn(columnName="Description")
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    @OrganizationReportColumn(columnName="Organization Name", propertyType=PropertyType.BOTH)
    public String getName() {
        return name;
    }
    @OrganizationReportColumn(columnName="Organization Acronym",propertyType=PropertyType.BOTH)
    public String getAcronym() {
        return acronym;
    }

    /**
     * @return
     */
    @OrganizationReportColumn(columnName="Organization Code")
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * @return
     */
    @OrganizationReportColumn(columnName="ISO Code")
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
    @OrganizationReportColumn(columnName="Fiscal Calendar",propertyType=PropertyType.BOTH)
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
    
    @Override
    public int compareTo(AmpOrganisation org)
    {
        if (this.name == null)
        {
            if (org.name == null)
                return 0; // null == null
            return -1; // null < [anything]
        }
        
        if (org.name == null)
            return 1; // [anything] > null
        
        return this.name.trim().compareTo(org.name.trim());
    }
    
    //do not implement equals, implement compareTo and use treeset!
    
    /**
     * @return Returns the address.
     */
    @OrganizationReportColumn(columnName="Address",propertyType=PropertyType.BOTH)
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
     * @return Returns the orgGrpId.
     */
    @OrganizationReportColumn(columnName="Organization Group", propertyType=PropertyType.BOTH)
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
    @OrganizationReportColumn(columnName="Organization website", propertyType=PropertyType.BOTH)
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
            Iterator<AmpOrganisationDocument> iter  = documents.iterator();
            return iter.next().getUuid();
        }
        
            
        return null;
    }

    public String getAcronymAndName() {
        acronymAndName = (acronym != null) ? acronym + " - " + name : name;
        return acronymAndName;
    }

    public void setAcronymAndName(String acronymAndName) {
    this.acronymAndName = acronymAndName;
    }
    @OrganizationReportColumn(columnName="Budget Organization Code")
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
    @OrganizationReportColumn(columnName="Organization Intervention Location",propertyType=PropertyType.NGO,returnedClass=AmpOrgLocation.class)
    public Set<AmpOrgLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<AmpOrgLocation> locations) {
        this.locations = locations;
    }
    @OrganizationReportColumn(columnName="Staff Information",propertyType=PropertyType.NGO,returnedClass=AmpOrgStaffInformation.class)
    public Set<AmpOrgStaffInformation> getStaffInfos() {
        return staffInfos;
    }

    public void setStaffInfos(Set<AmpOrgStaffInformation> staffInfos) {
        this.staffInfos = staffInfos;
    }
    @OrganizationReportColumn(columnName="Country Of Origin",propertyType=PropertyType.NGO)
    public AmpCategoryValueLocations getCountry() {
        return country;
    }

    public void setCountry(AmpCategoryValueLocations country) {
        this.country = country;
    }
    
    @OrganizationReportColumn(columnName="Recipients",propertyType=PropertyType.NGO,returnedClass=AmpOrgRecipient.class)
    public Set<AmpOrgRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<AmpOrgRecipient> recipients) {
        this.recipients = recipients;
    }
    @OrganizationReportColumn(columnName="Organization Address Abroad(Internation NGO)",propertyType=PropertyType.NGO)
    public String getAddressAbroad() {
        return addressAbroad;
    }

    public void setAddressAbroad(String addressAbroad) {
        this.addressAbroad = addressAbroad;
    }
    @OrganizationReportColumn(columnName="Tax Number",propertyType=PropertyType.NGO)
    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
    @OrganizationReportColumn(columnName="Organization Primary Purpose",propertyType=PropertyType.NGO)
    public String getPrimaryPurpose() {
        return primaryPurpose;
    }

    public void setPrimaryPurpose(String primaryPurpose) {
        this.primaryPurpose = primaryPurpose;
    }
    @OrganizationReportColumn(columnName="Registration Number in MinPlan",propertyType=PropertyType.NGO)
    public String getMinPlanRegNumb() {
        return minPlanRegNumb;
    }

    public void setMinPlanRegNumb(String minPlanRegNumb) {
        this.minPlanRegNumb = minPlanRegNumb;
    }
    @OrganizationReportColumn(columnName="Legal Personality Number",propertyType=PropertyType.NGO)
    public String getLegalPersonNum() {
        return legalPersonNum;
    }

    public void setLegalPersonNum(String legalPersonNum) {
        this.legalPersonNum = legalPersonNum;
    }
    @OrganizationReportColumn(columnName="Legal personality registration date in the country of origin",propertyType=PropertyType.NGO)
    public Date getLegalPersonRegDate() {
        return legalPersonRegDate;
    }

    public void setLegalPersonRegDate(Date legalPersonRegDate) {
        this.legalPersonRegDate = legalPersonRegDate;
    }
    @OrganizationReportColumn(columnName="Registration Date in MinPlan",propertyType=PropertyType.NGO)
    public Date getMinPlanRegDate() {
        return minPlanRegDate;
    }

    public void setMinPlanRegDate(Date minPlanRegDate) {
        this.minPlanRegDate = minPlanRegDate;
    }
    @OrganizationReportColumn(columnName="Budget Information",propertyType=PropertyType.NGO,returnedClass=AmpOrganizationBudgetInformation.class)
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
    @OrganizationReportColumn(columnName="Registration date of Line Ministry",propertyType=PropertyType.NGO)
    public Date getLineMinRegDate() {
        return lineMinRegDate;
    }

    public void setLineMinRegDate(Date lineMinRegDate) {
        this.lineMinRegDate = lineMinRegDate;
    }
    @OrganizationReportColumn(columnName="Operation approval date in the country of origin",propertyType=PropertyType.NGO)
    public Date getOperFuncApprDate() {
        return operFuncApprDate;
    }

    public void setOperFuncApprDate(Date operFuncApprDate) {
        this.operFuncApprDate = operFuncApprDate;
    }
    @OrganizationReportColumn(columnName="Receipt of legal personality act in DRC",propertyType=PropertyType.NGO)
    public String getReceiptLegPersonalityAct() {
        return receiptLegPersonalityAct;
    }

    public void setReceiptLegPersonalityAct(String receiptLegPersonalityAct) {
        this.receiptLegPersonalityAct = receiptLegPersonalityAct;
    }

    public void setOrganizationContacts(Set<AmpOrganisationContact> organizationContacts) {
        this.organizationContacts = organizationContacts;
    }
    @OrganizationReportColumn(columnName="Contact Information",propertyType=PropertyType.BOTH,returnedClass=AmpOrganisationContact.class)
    public Set<AmpOrganisationContact> getOrganizationContacts() {
        return organizationContacts;
    }
    @OrganizationReportColumn(columnName="Funding Org Id",propertyType=PropertyType.BOTH)
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
        this.translateable  = translateable;
        
    }

    public String getAdditionalSearchString() {
        return this.acronym;
    }
    @OrganizationReportColumn(columnName="Organization Type",propertyType=PropertyType.BOTH)
    public String getOrgTypeName() {
        return this.orgGrpId.getOrgType().getOrgType();
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOrgKeyAreas() {
        return orgKeyAreas;
    }

    public void setOrgKeyAreas(String orgKeyAreas) {
        this.orgKeyAreas = orgKeyAreas;
    }
    
    public static String sqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall(idSource + ".ampOrgId");
    }

}   
