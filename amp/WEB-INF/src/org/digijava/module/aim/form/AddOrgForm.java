package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Pledge;

public class AddOrgForm extends ActionForm {

	private String name = null;
	private String acronym = null;
	private String dacOrgCode = null;
	private String description = null;
	private String orgCode = null;
	private String orgIsoCode = null;
		
	private String budgetOrgCode = null;
	
	private String contactPersonName = null;
	private String contactPersonTitle = null;
	private String address = null;
	private String email = null;
	private String phone = null;
	private String fax = null;
	private String orgUrl = null;
	
	private Long ampOrgId = null;
	private Long ampOrgTypeId = null;
	private Long fiscalCalId = null;
	private Long ampSecSchemeId = null;
	private Long levelId = null;		//defunct
	private Long regionId = null;
	private String countryId = "et";	//defunct
	private Long ampOrgGrpId = null;
	
	private Collection fiscalCal = null;
	private Collection sectorScheme = null;
	private Collection country = null;	//defunct
	private Collection region = null;
	private Collection orgType = null;
	private Collection level = null;	//defunct
	private Collection orgGroup = null;
	private Collection orgGroupColl = null;
	
	private String actionFlag = null;
	private String mode=null;
	private String flag = null;
	private String regionFlag = null;	//defunct
	private String orgTypeFlag = null;
	private String levelFlag = null;	//defunct
	private String saveFlag = "no";

	//Sector Preferences
	private Collection sectors;
	private Long selSectors[];
	//
	//Pledges
	private ArrayList fundingDetails;
	private Collection currencies = null;
	private long transIndexId;
	public Pledge getFundingDetail(int index) {
		int currentSize = fundingDetails.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				fundingDetails.add(new Pledge());
			}
		}
		return (Pledge)fundingDetails.get(index);
	}
	public void setFundingDetail(int index, Object test){
		////System.out.println("NU merge!");
	}
	//
	public Collection getCurrencies() {
		return currencies;
	}


	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}


	//
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
			
		  contactPersonName = null;
		  contactPersonTitle = null;
		  address = null;
		  email = null;
		  phone = null;
		  fax = null;
		  orgUrl = null;
		
		  ampOrgId = null;
		  ampOrgTypeId = null;
		  fiscalCalId = null;
		  ampSecSchemeId = null;
		  levelId = null;		//defunct
		  regionId = null;
		  countryId = "et";	//defunct
		  ampOrgGrpId = null;
		
		  fiscalCal = null;
		  sectorScheme = null;
		  country = null;	//defunct
		  region = null;
		  orgType = null;
		  level = null;	//defunct
		  orgGroup = null;
		  orgGroupColl = null;
		
		 // actionFlag = null;
		  flag = null;
		  regionFlag = null;	//defunct
		  orgTypeFlag = null;
		  levelFlag = null;	//defunct
		  saveFlag = "no";
		  mode=null;
		  sectors = null;
		  fundingDetails = null;
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
	
	public String getCountryId() {
		return countryId;
	}
	
	public void setCountryId(String countryId) {
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
	
	public Collection getCountry() {
		return country;
	}
	
	public void setCountry(Collection country) {
		this.country = country;
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
	
	public String getContactPersonName() {
		return contactPersonName;
	}
	
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	
	public String getContactPersonTitle() {
		return contactPersonTitle;
	}
	
	public void setContactPersonTitle(String contactPersonTitle) {
		this.contactPersonTitle = contactPersonTitle;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getOrgUrl() {
		return orgUrl;
	}
	
	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
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


	public ArrayList getFundingDetails() {
		return fundingDetails;
	}


	public void setFundingDetails(ArrayList fundingDetails) {
		this.fundingDetails = fundingDetails;
	}


	public long getTransIndexId() {
		return transIndexId;
	}


	public void setTransIndexId(long transIndexId) {
		this.transIndexId = transIndexId;
	}
	public String getBudgetOrgCode() {
		return budgetOrgCode;
	}
	public void setBudgetOrgCode(String budgetOrgCode) {
		this.budgetOrgCode = budgetOrgCode;
	}
}
