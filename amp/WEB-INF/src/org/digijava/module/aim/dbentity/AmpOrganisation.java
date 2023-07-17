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
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@TranslatableClass (displayName = "Organisation")
@InterchangeableValue(OrganisationValueProvider.class)
@Entity
@Table(name = "AMP_ORGANISATION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpOrganisation implements Comparable<AmpOrganisation>, Identifiable, Serializable, ARDimensionable, HierarchyListable, NameableOrIdentifiable
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORGANISATION_seq")
    @SequenceGenerator(name = "AMP_ORGANISATION_seq", sequenceName = "AMP_ORGANISATION_seq", allocationSize = 1)
    @Column(name = "amp_org_id")
    @PossibleValueId
    private Long ampOrgId;

    @Column(name = "name")
    @TranslatableField
    private String name;

    @Column(name = "org_type")
    private String orgType;

    @Column(name = "dac_org_code")
    private String dacOrgCode;

    @Column(name = "org_iso_code")
    private String orgIsoCode;

    @Column(name = "description", columnDefinition = "text")
    @TranslatableField
    private String description;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "budget_org_code")
    private String budgetOrgCode;

    @Column(name = "org_group")
    private String orgGroup;

    @Column(name = "funding_org_id")
    private String fundingOrgId;

    @Column(name = "address")
    private String address;

    @Column(name = "org_url")
    private String orgUrl;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "segment_code")
    private String segmentCode;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name = "AMP_ORGANISATION_SECTOR",
            joinColumns = @JoinColumn(name = "amp_org_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_sector_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AmpSector> sectors;

    @ManyToOne
    @JoinColumn(name = "org_grp_id")
    private AmpOrgGroup orgGrpId;

    @ManyToOne
    @JoinColumn(name = "imp_level_id")
    private AmpCategoryValue implemLocationLevel;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country countryId;

    @ManyToOne
    @JoinColumn(name = "amp_fiscal_cal_id")
    private AmpFiscalCalendar ampFiscalCalId;

    @ManyToOne
    @JoinColumn(name = "amp_sec_scheme_id")
    private AmpSectorScheme ampSecSchemeId;

    @Column(name = "org_type_code")
    private String orgTypeCode;

    @OneToMany(mappedBy = "ampOrgId")
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<AmpAhsurvey> survey;

    @OneToMany(mappedBy = "pointOfDeliveryDonor")
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<AmpAhsurvey> surveyByPointOfDeliveryDonor;

    @ManyToMany
    @JoinTable(name = "AMP_CALENDAR_EVENT_ORGANISATIO",
            joinColumns = @JoinColumn(name = "amp_org_id"),
            inverseJoinColumns = @JoinColumn(name = "calendar_id"))
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<AmpCalendar> calendar;

    @OneToMany(mappedBy = "ampOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<AmpOrganisationDocument> documents;

    @OneToMany(mappedBy = "ampOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpOrgLocation> locations;

    @OneToMany(mappedBy = "ampOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpOrgStaffInformation> staffInfos;

    @OneToMany(mappedBy = "ampOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpOrganizationBudgetInformation> organizationBudgetInfos;

    @ManyToOne
    @JoinColumn(name = "amp_country_id")
    private AmpCategoryValueLocations country;

    @OneToMany(mappedBy = "parentOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpOrgRecipient> recipients;

    @ManyToMany
    @JoinTable(name = "AMP_ORGANISATION_BUDGETSECTOR",
            joinColumns = @JoinColumn(name = "amp_org_id"),
            inverseJoinColumns = @JoinColumn(name = "idsector"))
    private Set<AmpBudgetSector> budgetsectors;

    @ManyToMany
    @JoinTable(name = "AMP_ORGANISATION_DEPARTMENTS",
            joinColumns = @JoinColumn(name = "amp_org_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<AmpDepartments> departments;

    @ManyToOne
    @JoinColumn(name = "budget_sector_id")
    private AmpBudgetSector parentsector;

    @OneToMany(mappedBy = "ampOrgId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpOrganisationContact> organizationContacts;

    @Column(name = "address_abroad")
    private String addressAbroad;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "regist_num_min_plan")
    private String minPlanRegNumb;

    @Column(name = "legal_personality_number")
    private String legalPersonNum;

    @Column(name = "legal_personality_reg_date")
    private Date legalPersonRegDate;

    @Column(name = "min_plan_reg_date")
    private Date minPlanRegDate;

    @Column(name = "org_primary_purpose", columnDefinition = "text")
    private String primaryPurpose;

    @Column(name = "org_other_info", columnDefinition = "text")
    private String otherInformation;

    @Column(name = "org_line_ministry_reg_date")
    private Date lineMinRegDate;

    @Column(name = "org_oper_func_approval_date")
    private Date operFuncApprDate;

    @Column(name = "org_rec_leg_per_act")
    private String receiptLegPersonalityAct;

    @ManyToOne
    @JoinColumn(name = "cat_val_loc_region_id")
    private AmpCategoryValueLocations region;

    @Column(name = "org_background")
    private String orgBackground;

    @Column(name = "org_description")
    private String orgDescription;

    @Column(name = "org_key_areas")
    private String orgKeyAreas;

    @Column(name = "line_min_reg_numb")
    private String lineMinRegNumber;

    @ManyToMany
    @JoinTable(name = "DG_USER_ORGS",
            joinColumns = @JoinColumn(name = "org_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;
    

    @Transient
    private String fundingorgid;

    @Transient
    private String acronymAndName;

    @Transient
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

}   
