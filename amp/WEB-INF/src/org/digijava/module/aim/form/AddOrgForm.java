package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrgStaffInformation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.aim.dbentity.AmpOrganizationBudgetInformation;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Pledge;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

public class AddOrgForm extends ActionForm {

    private String name = null;
    private String acronym = null;
    private String dacOrgCode = null;
    private String description = null;
    private String orgCode = null;
    private String orgIsoCode = null;       
    private String budgetOrgCode = null;
    private String fundingorgid;
        
    private String address = null;
    
    private String orgUrl = null;   
    private Long ampOrgId = null;
    private Long ampOrgTypeId = null;
    private Long fiscalCalId = null;
    private Long ampSecSchemeId = null;
    private Long levelId = null;        //defunct
    private Long regionId = null;
    private Long countryId = null;
    //private String countryId = "et";  //defunct
    private Long ampOrgGrpId = null;
    
    private Collection fiscalCal = null;
    private Collection sectorScheme = null;
    private Collection countries = null;
    //private Collection country = null;    //defunct
    private Collection region = null;
    private Collection orgType = null;
    private Collection level = null;    //defunct
    private Collection orgGroup = null;
    private Collection orgGroupColl = null;
    
    private String actionFlag = null;
    private String mode=null;
    private String flag = null;
    private String regionFlag = null;   //defunct
    private String orgTypeFlag = null;
    private String levelFlag = null;    //defunct
    private String saveFlag = "no";

    //Sector Preferences
    private Collection sectors;
    private Long selSectors[];
    

    private Collection currencies = null;
    
    
    private String orgPrimaryPurpose;
    private List<AmpOrgStaffInformation> staff = null;
    private List<AmpOrganizationBudgetInformation> orgInfos = null;
    private List<LabelValueBean> years;
    private Long typeOfStaff;
    private String numberOfStaff;
    private String selectedYear;
    private Long[] selectedStaff;
    private Long selectedStaffId;
    private List<AmpOrgRecipient> recipients;
    private Long[] selRecipients;
    private Long implemLocationLevel;
    private Long[] selLocs;
    private Long parentLocId;
    private String type;
    private String addressAbroad;
    private String taxNumber;
    private String regNumbMinPlan;
    private String legalPersonNum;
    private String legalPersonRegDate;
    private String minPlanRegDate;
    private List<AmpOrganisationDocument> documents;
    private String orgInfoSelectedYear;
    private Long orgInfoCurrId;
    private Long orgInfoType;
    private String orgInfoPercent;
    private Long[] selectedOrgInfoIds;
    private Long selectedOrgInfoId;
    private String orgInfoAmount;
    private String otherInformation;
    private String lineMinRegDate;
    private String operFuncApprDate;
    private String receiptLegPersonalityAct;
    private Set<AmpOrganisation> budgetOrgs;
    private Long[] selBudgetOrg;
    private Collection<Location> selectedLocs = null;
    private Collection<AmpDepartments> departments;
    private Long[] selecteddepartments;
    private Collection<AmpBudgetSector> budgetsectors;
    private Long[] selectedbudgetsectors;
    private String  lineMinRegNumber;
    
    private List<AmpOrganisationContact> orgContacts;
    
    private String[] selectedContactInfoIds;
    private String[] primaryOrgContIds;
    private Boolean resetPrimaryOrgContIds;    
    private String selContactId;
    
    private Boolean resetDepartments;
    private Boolean resetBudgetSectors;
    private int orgInfoIndex;
    private int staffInfoIndex;
    private String orgTypeName;
    private String orgGroupName;

    private Collection templates;
    private Long templateId;

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public String getOrgGroupName() {
        return orgGroupName;
    }

    public void setOrgGroupName(String orgGroupName) {
        this.orgGroupName = orgGroupName;
    }

    public int getOrgInfoIndex() {
        return orgInfoIndex;
    }

    public int getStaffInfoIndex() {
        return staffInfoIndex;
    }

    public void setStaffInfoIndex(int staffInfoIndex) {
        this.staffInfoIndex = staffInfoIndex;
    }

    public void setOrgInfoIndex(int orgInfoIndex) {
        this.orgInfoIndex = orgInfoIndex;
    }

    public String getLineMinRegNumber() {
        return lineMinRegNumber;
    }

    public void setLineMinRegNumber(String lineMinRegNumber) {
        this.lineMinRegNumber = lineMinRegNumber;
    }


    public Collection<AmpBudgetSector> getBudgetsectors() {
        return budgetsectors;
    }

    public void setBudgetsectors(Collection<AmpBudgetSector> budgetsectors) {
        this.budgetsectors = budgetsectors;
    }

    public Long[] getSelectedbudgetsectors() {
        return selectedbudgetsectors;
    }

    public void setSelectedbudgetsectors(Long[] selectedbudgetsectors) {
        this.selectedbudgetsectors = selectedbudgetsectors;
    }

    public Collection<AmpDepartments> getDepartments() {
        return departments;
    }

    public void setDepartments(Collection<AmpDepartments> departments) {
        this.departments = departments;
    }

    public Long[] getSelecteddepartments() {
        return selecteddepartments;
    }

    public void setSelecteddepartments(Long[] selecteddepartments) {
        this.selecteddepartments = selecteddepartments;
    }    
    
    
    public Collection getCurrencies() {
        return currencies;
    }


    public void setCurrencies(Collection currencies) {
        this.currencies = currencies;
    }

     public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }


    public void reset(ActionMapping mapping, HttpServletRequest request){
        String rMode = (String) request.getParameter("mode");
        if(("resetMode".equals(rMode))&&(request.getParameter("addSector")==null)&&(request.getParameter("remSector")==null)&&(request.getParameter("saveFlag")==null)&&(request.getParameter("orgGroupAdded")==null))
        {   
          name = null;
          acronym = null;
          dacOrgCode = null;
          description = null;
          orgCode = null;
          orgIsoCode = null;
          
          budgetOrgCode=null;
          
          orgUrl = null;
        
          ampOrgId = null;
          ampOrgTypeId = null;
          fiscalCalId = null;
          ampSecSchemeId = null;
          levelId = null;       //defunct
          regionId = null;
          countryId = null; //defunct
          ampOrgGrpId = null;
        
          fiscalCal = null;
          sectorScheme = null;
          countries = null; //defunct
          region = null;
          orgType = null;
          level = null; //defunct
          orgGroup = null;
          orgGroupColl = null;
        
         // actionFlag = null;
          flag = null;
          regionFlag = null;    //defunct
          orgTypeFlag = null;
          levelFlag = null; //defunct
          saveFlag = "no";
          mode=null;
          sectors = null;
          templates = null;
          templateId = null;
          if ("resetMode".equals(request.getParameter("mode"))){
              request.removeAttribute("mode");
          }
          }
    
    }
    public String getName() {
        return name;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgIsoCode() {
        return orgIsoCode;
    }
    
    public String getDacOrgCode() {
        return dacOrgCode;
    }

    public String getDescription() {
        return description;
    }
    
    public void setName(String string) {
        name = string;
    }

    public void setOrgCode(String string) {
        orgCode = string;
    }

    public void setOrgIsoCode(String string) {
        orgIsoCode = string;
    }
    
    public void setDacOrgCode(String string) {
        dacOrgCode = string;
    }

    public void setDescription(String string) {
        description = string;
    }
    
    public Long getAmpOrgId() {
        return ampOrgId;
    }
    
    public void setAmpOrgId(Long orgId) {
        ampOrgId = orgId;
    }
    
    public Long getFiscalCalId() {
        return fiscalCalId;
    }
    
    public void setFiscalCalId(Long long1) {
        fiscalCalId = long1;
    }
    
    public Long getAmpSecSchemeId() {
        return ampSecSchemeId;
    }
    
    public void setAmpSecSchemeId(Long  long1) {
        ampSecSchemeId = long1;
    }
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public Long getRegionId() {
        return regionId;
    }
    
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    
    public Long getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
    
    public Long getAmpOrgGrpId() {
        return ampOrgGrpId;
    }
    
    public void setAmpOrgGrpId(Long ampOrgGrpId) {
        this.ampOrgGrpId = ampOrgGrpId;
    }

    public Long getAmpOrgTypeId() {
        return ampOrgTypeId;
    }
    
    public void setAmpOrgTypeId(Long ampOrgTypeId) {
        this.ampOrgTypeId = ampOrgTypeId;
    }
    
    public Collection getFiscalCal() {
        return fiscalCal;
    }
    
    public void setFiscalCal(Collection fiscalCal) {
        this.fiscalCal = fiscalCal;
    }
    
    public Collection getSectorScheme() {
        return sectorScheme;
    }
    
    public void setSectorScheme(Collection sector) {
        sectorScheme = sector;
    }

    
    public Collection getCountries() {
        return countries;
    }

    public void setCountries(Collection countries) {
        this.countries = countries;
    }

    public Collection getOrgType() {
        return orgType;
    }
    
    public void setOrgType(Collection orgType) {
        this.orgType = orgType;
    }
    
    public Collection getLevel() {
        return level;
    }
    
    public void setLevel(Collection level) {
        this.level = level;
    }
    
    public Collection getOrgGroup() {
        return orgGroup;
    }
    
    public void setOrgGroup(Collection orgGroup) {
        this.orgGroup = orgGroup;
    }
    
    /**
     * @return Returns the orgGroupColl.
     */
    public Collection getOrgGroupColl() {
        return orgGroupColl;
    }
    /**
     * @param orgGroupColl The orgGroupColl to set.
     */
    public void setOrgGroupColl(Collection orgGroupColl) {
        this.orgGroupColl = orgGroupColl;
    }
    
    public Collection getRegion() {
        return region;
    }
    
    public void setRegion(Collection region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getOrgUrl() {
        return orgUrl;
    }
    
    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }
    public String getActionFlag() {
        return actionFlag;
    }
    
    public String getFlag() {
        return flag;
    }
    
    public String getRegionFlag() {
        return regionFlag;
    }
    
    public String getOrgTypeFlag() {
        return orgTypeFlag;
    }
    
    public String getLevelFlag() {
        return levelFlag;
    }
    
    public String getSaveFlag() {
        return saveFlag;
    }
    
    public void setActionFlag(String action) {
        this.actionFlag = action;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    public void setRegionFlag(String regionFlag) {
        this.regionFlag = regionFlag;
    }
    
    public void setOrgTypeFlag(String orgTypeFlag) {
        this.orgTypeFlag = orgTypeFlag;
    }
    
    public void setLevelFlag(String levelFlag) {
        this.levelFlag = levelFlag;
    }
    
    public void setSaveFlag(String saveFlag) {
        this.saveFlag = saveFlag;
    }
    
    /**
     * @return Returns the acronym.
     */
    public String getAcronym() {
        return acronym;
    }
    /**
     * @param acronym The acronym to set.
     */
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }


    public Collection getSectors() {
        return sectors;
    }


    public void setSectors(Collection sectors) {
        this.sectors = sectors;
    }


    public Long[] getSelSectors() {
        return selSectors;
    }


    public void setSelSectors(Long[] selSectors) {
        this.selSectors = selSectors;
    }

    public String getBudgetOrgCode() {
        return budgetOrgCode;
    }
    public void setBudgetOrgCode(String budgetOrgCode) {
        this.budgetOrgCode = budgetOrgCode;
    }

    public String getOrgPrimaryPurpose() {
        return orgPrimaryPurpose;
    }

    public void setOrgPrimaryPurpose(String orgPrimaryPurpose) {
        this.orgPrimaryPurpose = orgPrimaryPurpose;
    }

    public List<AmpOrgStaffInformation> getStaff() {
        return staff;
    }

    public void setStaff(List<AmpOrgStaffInformation> staff) {
        this.staff = staff;
    }

    public List<AmpOrganizationBudgetInformation> getOrgInfos() {
        return orgInfos;
    }

    public void setOrgInfos(List<AmpOrganizationBudgetInformation> orgInfos) {
        this.orgInfos = orgInfos;
    }

    public List<LabelValueBean> getYears() {
        return years;
    }

    public void setYears(List<LabelValueBean> years) {
        this.years = years;
    }

    public Long getTypeOfStaff() {
        return typeOfStaff;
    }

    public void setTypeOfStaff(Long typeOfStaff) {
        this.typeOfStaff = typeOfStaff;
    }

    public String getNumberOfStaff() {
        return numberOfStaff;
    }

    public void setNumberOfStaff(String numberOfStaff) {
        this.numberOfStaff = numberOfStaff;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public Long[] getSelectedStaff() {
        return selectedStaff;
    }

    public void setSelectedStaff(Long[] selectedStaff) {
        this.selectedStaff = selectedStaff;
    }

    public Long getSelectedStaffId() {
        return selectedStaffId;
    }

    public void setSelectedStaffId(Long selectedStaffId) {
        this.selectedStaffId = selectedStaffId;
    }

    public List<AmpOrgRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<AmpOrgRecipient> recipients) {
        this.recipients = recipients;
    }

    public Long[] getSelRecipients() {
        return selRecipients;
    }

    public void setSelRecipients(Long[] selRecipients) {
        this.selRecipients = selRecipients;
    }

    public Long getImplemLocationLevel() {
        return implemLocationLevel;
    }

    public void setImplemLocationLevel(Long implemLocationLevel) {
        this.implemLocationLevel = implemLocationLevel;
    }

    public Long[] getSelLocs() {
        return selLocs;
    }

    public void setSelLocs(Long[] selLocs) {
        this.selLocs = selLocs;
    }

    public Long getParentLocId() {
        return parentLocId;
    }

    public void setParentLocId(Long parentLocId) {
        this.parentLocId = parentLocId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getRegNumbMinPlan() {
        return regNumbMinPlan;
    }

    public void setRegNumbMinPlan(String regNumbMinPlan) {
        this.regNumbMinPlan = regNumbMinPlan;
    }

    public String getLegalPersonNum() {
        return legalPersonNum;
    }

    public void setLegalPersonNum(String legalPersonNum) {
        this.legalPersonNum = legalPersonNum;
    }

    public String getLegalPersonRegDate() {
        return legalPersonRegDate;
    }

    public void setLegalPersonRegDate(String legalPersonRegDate) {
        this.legalPersonRegDate = legalPersonRegDate;
    }

    public String getMinPlanRegDate() {
        return minPlanRegDate;
    }

    public void setMinPlanRegDate(String minPlanRegDate) {
        this.minPlanRegDate = minPlanRegDate;
    }

    public List<AmpOrganisationDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<AmpOrganisationDocument> documents) {
        this.documents = documents;
    }

    public String getOrgInfoSelectedYear() {
        return orgInfoSelectedYear;
    }

    public void setOrgInfoSelectedYear(String orgInfoSelectedYear) {
        this.orgInfoSelectedYear = orgInfoSelectedYear;
    }

    public Long getOrgInfoCurrId() {
        return orgInfoCurrId;
    }

    public void setOrgInfoCurrId(Long orgInfoCurrId) {
        this.orgInfoCurrId = orgInfoCurrId;
    }

    public Long getOrgInfoType() {
        return orgInfoType;
    }

    public void setOrgInfoType(Long orgInfoType) {
        this.orgInfoType = orgInfoType;
    }

    public String getOrgInfoPercent() {
        return orgInfoPercent;
    }

    public void setOrgInfoPercent(String orgInfoPercent) {
        this.orgInfoPercent = orgInfoPercent;
    }

    public Long[] getSelectedOrgInfoIds() {
        return selectedOrgInfoIds;
    }

    public void setSelectedOrgInfoIds(Long[] selectedOrgInfoIds) {
        this.selectedOrgInfoIds = selectedOrgInfoIds;
    }

    public Long getSelectedOrgInfoId() {
        return selectedOrgInfoId;
    }

    public void setSelectedOrgInfoId(Long selectedOrgInfoId) {
        this.selectedOrgInfoId = selectedOrgInfoId;
    }

    public String getOrgInfoAmount() {
        return orgInfoAmount;
    }

    public void setOrgInfoAmount(String orgInfoAmount) {
        this.orgInfoAmount = orgInfoAmount;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public String getLineMinRegDate() {
        return lineMinRegDate;
    }

    public void setLineMinRegDate(String lineMinRegDate) {
        this.lineMinRegDate = lineMinRegDate;
    }

    public String getOperFuncApprDate() {
        return operFuncApprDate;
    }

    public void setOperFuncApprDate(String operFuncApprDate) {
        this.operFuncApprDate = operFuncApprDate;
    }

    public String getReceiptLegPersonalityAct() {
        return receiptLegPersonalityAct;
    }

    public void setReceiptLegPersonalityAct(String receiptLegPersonalityAct) {
        this.receiptLegPersonalityAct = receiptLegPersonalityAct;
    }

    public Long[] getSelBudgetOrg() {
        return selBudgetOrg;
    }

    public void setSelBudgetOrg(Long[] selBudgetOrg) {
        this.selBudgetOrg = selBudgetOrg;
    }

    public Collection<Location> getSelectedLocs() {
        return selectedLocs;
    }

    public void setSelectedLocs(Collection<Location> selectedLocs) {
        this.selectedLocs = selectedLocs;
    }

    public void setOrgContacts(List<AmpOrganisationContact> orgContacts) {
        this.orgContacts = orgContacts;
    }

    public List<AmpOrganisationContact> getOrgContacts() {
        return orgContacts;
    }

    public void setSelectedContactInfoIds(String[] selectedContactInfoIds) {
        this.selectedContactInfoIds = selectedContactInfoIds;
    }

    public String[] getSelectedContactInfoIds() {
        return selectedContactInfoIds;
    }

    public void setPrimaryOrgContIds(String[] primaryOrgContIds) {
        this.primaryOrgContIds = primaryOrgContIds;
    }

    public String[] getPrimaryOrgContIds() {
        return primaryOrgContIds;
    }

    public void setSelContactId(String selContactId) {
        this.selContactId = selContactId;
    }

    public String getSelContactId() {
        return selContactId;
    }

    public Boolean getResetPrimaryOrgContIds() {
        return resetPrimaryOrgContIds;
    }

    public void setResetPrimaryOrgContIds(Boolean resetPrimaryOrgContIds) {
        this.resetPrimaryOrgContIds = resetPrimaryOrgContIds;
    }

    public Boolean getResetDepartments() {
        return resetDepartments;
    }

    public void setResetDepartments(Boolean resetDepartments) {
        this.resetDepartments = resetDepartments;
    }

    public Boolean getResetBudgetSectors() {
        return resetBudgetSectors;
    }

    public void setResetBudgetSectors(Boolean resetBudgetSectors) {
        this.resetBudgetSectors = resetBudgetSectors;
    }   

    public String getFundingorgid() {
        return fundingorgid;
    }

    public void setFundingorgid(String fundingorgid) {
        this.fundingorgid = fundingorgid;
    }

    public Set<AmpOrganisation> getBudgetOrgs() {
        return budgetOrgs;
    }

    public void setBudgetOrgs(Set<AmpOrganisation> budgetOrgs) {
        this.budgetOrgs = budgetOrgs;
    }

    public Collection getTemplates() {
        return templates;
    }

    public void setTemplates(Collection templates) {
        this.templates = templates;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public MultilingualInputFieldValues buildMultilingualNameInputInstance()
    {
        return new MultilingualInputFieldValues(AmpOrganisation.class, this.getAmpOrgId(), "name", null, null);
    }
    
    public MultilingualInputFieldValues restoreMultilingualNameInputInstance(HttpServletRequest request)
    {
        MultilingualInputFieldValues result = buildMultilingualNameInputInstance();
        result.populateTranslations(request);
        return result;
    }
    
}
