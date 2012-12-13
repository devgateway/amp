package org.digijava.module.esrigis.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;

public class MapFilter {
	private static final long serialVersionUID = 1L;
	private boolean isinitialized;
	private Long currencyId;
	private Boolean workspaceOnly;
	private TeamMember teamMember;
	private List<AmpCurrency> currencies;
	private List<AmpOrganisation> organizations;
	private List<AmpOrganisation> organizationsSelected;
	private List<AmpOrgType> organizationsType;
	private List<AmpStructureType> structureTypes;
	private Long[] selStructureTypes;
	private Long organizationsTypeId;
	private BigDecimal fundingLimit;
	private Boolean fundingLimitAbove;
	private Long onBudget;
	private Long typeAssistanceId;
	private Long financingInstrumentId;
	private boolean modeexport;
	private String reportfilterquery;
	private Long startYearFilter;
	private Long endYearFilter;
	private Map<Integer, Integer> years;
    private Long startYear;
    private Long endYear;
    private Long defaultStartYear;
    private Long defaultEndYear; 
    private List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupWithOrgsList;
    private List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones;
    private List<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>> configWithSectorAndSubSectors;
    private Long selSectorConfigId;
    private List<AmpClassificationConfiguration> sectorConfigs;
    
	//	private List<AmpOrgType> projectStatuses;
	private Long projectStatusId;
	private List<AmpOrgType> organizationsTypeSelected;
	private List<AmpOrgGroup> orgGroups;
	private List<AmpSector> sectors;
	private List<AmpCategoryValueLocations> regions;
	private List<AmpCategoryValueLocations> zones;
	
	private Long[] orgtypeIds;
	private Long[] orgGroupIds;
	private Long orgGroupId;

	private Long[] implOrgGroupIds;
	private Long[] implOrgIds;
	
	private Long[] orgIds;
	private Long orgId;
	private Long[] sectorIds;
	private Long sectorId;
	private Long[] subSectorIds;
	private Long subSectorId;
	private Long[] regionIds;
	private Long regionId;
	private Long[] zoneIds;
	private Long zoneId;

	private Long[] selSectorIds;
	private Long[] selLocationIds;
	private Long[] selOrgGroupIds;
	private List<AmpSector> sectorsSelected;
	private List<AmpCategoryValueLocations> locationsSelected;

	private int transactionType;
	private List<AmpFiscalCalendar> fiscalCalendars;
	private Long fiscalCalendarId;
	private Integer largestProjectNumber;
	private Boolean divideThousands;
	private Integer divideThousandsDecimalPlaces;

	private int yearsInRange;
	private Boolean commitmentsVisible = true;
	private Boolean disbursementsVisible = true;
	private Boolean pledgeVisible = true;
	private Boolean expendituresVisible = true;
	private Boolean fromPublicView;
	private Boolean showOnlyApprovedActivities;
	
	private Long activityId;
	private int decimalsToShow;

	public MapFilter getCopyFilterForFunding() {
		MapFilter newFilter = new MapFilter();

		newFilter.setCurrencyId(this.getCurrencyId());
		newFilter.setOrgIds(this.getOrgIds());
		newFilter.setOrgGroupId(this.getOrgGroupId());
		newFilter.setTeamMember(this.getTeamMember());
		newFilter.setSelLocationIds(this.getSelLocationIds());
		newFilter.setSelSectorIds(this.getSelSectorIds());
		newFilter.setActivityId(this.getActivityId());
		newFilter.setShowOnlyApprovedActivities(this.getShowOnlyApprovedActivities());
		newFilter.setFromPublicView(this.getFromPublicView());
		return newFilter;
	}
	
	public JSONArray toJson(){
		JSONArray result = new JSONArray();
		SimpleFilter selectedfilter = new SimpleFilter();
		selectedfilter.setCurrency(this.getCurrencyCode());
		selectedfilter.setStartyear(this.getStartYear().toString());
		selectedfilter.setEndyear(this.getEndYear().toString());
		
		Collection<AmpCategoryValue> categoryValues = null;
		categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		for (Iterator<AmpCategoryValue> iterator = categoryValues.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			if (ampCategoryValue.getId() == financingInstrumentId.longValue()){
				selectedfilter.setFinancinginstrument(ampCategoryValue.getValue());
			}
		}
		
		if (orgGroupIds!=null && orgGroupIds.length>0){
			for (Iterator<AmpOrgGroup> iterator = orgGroups.iterator(); iterator.hasNext();) {
				AmpOrgGroup group = (AmpOrgGroup) iterator.next();
				for (int i = 0; i < orgGroupIds.length; i++) {
					if (group.getIdentifier() == orgGroupIds[i]){
						selectedfilter.setOrganizationgroup(group.getOrgGrpName());
					}
				}
			}
		}
		ArrayList<SimpleDonor> donorslist = new ArrayList<SimpleDonor>();
		if (organizationsSelected != null && !organizationsSelected.isEmpty()){
			for (Iterator<AmpOrganisation> iterator = organizationsSelected.iterator(); iterator.hasNext();) {
				AmpOrganisation org = (AmpOrganisation) iterator.next();
				if (org!=null){
					SimpleDonor donor = new SimpleDonor();
					donor.setDonorname(org.getName());
					donorslist.add(donor);
				}
			}
			selectedfilter.setSelecteddonors(donorslist);
		}
		
		if (implOrgGroupIds!=null && implOrgGroupIds.length>0 ){
			for (Iterator<AmpOrgGroup> iterator = orgGroups.iterator(); iterator.hasNext();) {
				AmpOrgGroup group = (AmpOrgGroup) iterator.next();
				for (int i = 0; i < implOrgGroupIds.length; i++) {
					if (group.getIdentifier() == implOrgGroupIds[i]){
						selectedfilter.setImplementingagency(group.getOrgGrpName());
					}
				}
			}
		}
		
		boolean exit = false;
		ArrayList<SimpleDonor> impdonorslist = new ArrayList<SimpleDonor>();
		if (organizationsSelected != null && !organizationsSelected.isEmpty()){
			for (Iterator<AmpOrganisation> iterator = organizationsSelected.iterator(); iterator.hasNext();) {
				AmpOrganisation org = (AmpOrganisation) iterator.next();
				if (org!=null){
					SimpleDonor donor = new SimpleDonor();
					donor.setDonorname(org.getName());
					donorslist.add(donor);
				}else{
					exit = true;
				}
			}
			if (exit){
				selectedfilter.setImpselecteddonors(impdonorslist);
			}
		}
		
		if (projectStatusId!=null){
			Collection<AmpCategoryValue> categoryvaluesstatus = null;
			categoryvaluesstatus = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
			for (Iterator<AmpCategoryValue> iterator = categoryvaluesstatus.iterator(); iterator.hasNext();) {
				AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
				if (ampCategoryValue.getId() == projectStatusId.longValue()){
					selectedfilter.setProjectstatus(ampCategoryValue.getValue());
				}
			}
		}
		Collection<AmpCategoryValue> categoryvaluesfinanceintrument = null;
		categoryvaluesfinanceintrument = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		for (Iterator<AmpCategoryValue> iterator = categoryvaluesfinanceintrument.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			if (ampCategoryValue.getId() == financingInstrumentId.longValue()){
				selectedfilter.setFinancinginstrument(ampCategoryValue.getValue());
			}
		}
		
		Collection<AmpCategoryValue> categoryvaluestypeofassis = null;
		categoryvaluestypeofassis = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
		for (Iterator<AmpCategoryValue> iterator = categoryvaluestypeofassis.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			if (ampCategoryValue.getId() == typeAssistanceId.longValue()){
				selectedfilter.setTypeofassistance(ampCategoryValue.getValue());
			}
		}
		
		if (selStructureTypes!=null){
			ArrayList<String> selstructurestr = new ArrayList<String>();
			for (Iterator iterator = DbHelper.getAllStructureTypes().iterator(); iterator.hasNext();) {
				AmpStructureType type = (AmpStructureType) iterator.next();
				for (int i = 0; i < selStructureTypes.length; i++) {
					if (type.getTypeId() == selStructureTypes[i].longValue()){
						selstructurestr.add(type.getName());
					}
				}
				
			}
			selectedfilter.setStructuretypes(selstructurestr);
		}
		
		
		if (selSectorIds!=null && selSectorIds.length>0 ){
			for (Iterator<AmpSector> iterator = sectors.iterator(); iterator.hasNext();) {
				AmpSector sector = (AmpSector) iterator.next();
				for (int i = 0; i < selSectorIds.length; i++) {
					if (sector.getAmpSectorId() == selSectorIds[i].longValue()){
						selectedfilter.setSector(sector.getName());
					}
				}
			}
		}
		
		ArrayList<String> selregions = new ArrayList<String>();
		if (regionIds != null && regionIds.length>0){
			for (Iterator<AmpCategoryValueLocations> iterator = regions.iterator(); iterator.hasNext();) {
				AmpCategoryValueLocations region = (AmpCategoryValueLocations) iterator.next();
				for (int i = 0; i < regionIds.length; i++) {
					if(region.getId() == regionIds [i].longValue()){
						selregions.add(region.getName());
					}
				}
			}
			
			selectedfilter.setRegions(selregions);
		}
		
		if (organizationsTypeId !=null && organizationsTypeId!=-1){
			AmpOrgType orgtype = (AmpOrgType) DbUtil.getAmpOrgType(organizationsTypeId);
			selectedfilter.setOrganizationtype(orgtype.getOrgType());
		}
		result.add(selectedfilter);
		return result;
	}
	
	
	public List<EntityRelatedListHelper<AmpClassificationConfiguration, EntityRelatedListHelper<AmpSector, AmpSector>>> getConfigWithSectorAndSubSectors() {
		return configWithSectorAndSubSectors;
	}

	public void setConfigWithSectorAndSubSectors(
			List<EntityRelatedListHelper<AmpClassificationConfiguration, EntityRelatedListHelper<AmpSector, AmpSector>>> configWithSectorAndSubSectors) {
		this.configWithSectorAndSubSectors = configWithSectorAndSubSectors;
	}
	
	
	public List<AmpClassificationConfiguration> getSectorConfigs() {
		return sectorConfigs;
	}

	public void setSectorConfigs(List<AmpClassificationConfiguration> sectorConfigs) {
		this.sectorConfigs = sectorConfigs;
	}

	
	public Long getSelSectorConfigId() {
		return selSectorConfigId;
	}

	public void setSelSectorConfigId(Long selSectorConfigId) {
		this.selSectorConfigId= selSectorConfigId;
	}
	
	public List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> getRegionWithZones() {
		return regionWithZones;
	}

	public void setRegionWithZones(
			List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> regionWithZones) {
		this.regionWithZones = regionWithZones;
	}

	
	public List<EntityRelatedListHelper<AmpOrgGroup, AmpOrganisation>> getOrgGroupWithOrgsList() {
		return orgGroupWithOrgsList;
	}


	public void setOrgGroupWithOrgsList(
			List<EntityRelatedListHelper<AmpOrgGroup, AmpOrganisation>> orgGroupWithOrgsList) {
		this.orgGroupWithOrgsList = orgGroupWithOrgsList;
	}
	
    public Long getDefaultStartYear() {
		return defaultStartYear;
	}

	public void setDefaultStartYear(Long defaultStartYear) {
		this.defaultStartYear = defaultStartYear;
	}

	public Long getDefaultEndYear() {
		return defaultEndYear;
	}

	public void setDefaultEndYear(Long defaultEndYear) {
		this.defaultEndYear = defaultEndYear;
	}
    public Long getEndYear() {
        return endYear;
    }

    public void setEndYear(Long year) {
    	this.endYearFilter = year;
        this.endYear = year;
    }
    
    public Long getStartYear() {
        return startYear;
    }

    public void setStartYear(Long year) {
        this.startYear = year;
        this.startYearFilter = year;
    }
	
    
	public void setEndYearFilter(Long endYearFilter) {
		this.endYearFilter = endYearFilter;
	}

	public Long getEndYearFilter() {
		return endYearFilter;
	}
    
	public void setStartYearFilter(Long startYearFilter) {
		this.startYearFilter = startYearFilter;
	}

	public Long getStartYearFilter() {
		return startYearFilter;
	}
	
	public boolean isIsinitialized() {
		return isinitialized;
	}

	public void setIsinitialized(boolean isinitialized) {
		this.isinitialized = isinitialized;
	}
	
	public Boolean getCommitmentsVisible() {
		return commitmentsVisible;
	}

	public void setCommitmentsVisible(Boolean commitmentsVisible) {
		this.commitmentsVisible = commitmentsVisible;
	}

	public Boolean getDisbursementsVisible() {
		return disbursementsVisible;
	}

	public void setDisbursementsVisible(Boolean disbursementsVisible) {
		this.disbursementsVisible = disbursementsVisible;
	}

	public Boolean getExpendituresVisible() {
		return expendituresVisible;
	}

	public void setExpendituresVisible(Boolean expendituresVisible) {
		this.expendituresVisible = expendituresVisible;
	}

	public Boolean getPledgeVisible() {
		return pledgeVisible;
	}

	public void setPledgeVisible(Boolean pledgeVisible) {
		this.pledgeVisible = pledgeVisible;
	}

	public int getYearsInRange() {
		if (yearsInRange == 0)
			yearsInRange = 5;
		return yearsInRange;
	}

	public void setYearsInRange(int yearsInRange) {
		this.yearsInRange = yearsInRange;
	}

	public List<AmpCategoryValueLocations> getRegions() {
		return regions;
	}

	public void setRegions(List<AmpCategoryValueLocations> regions) {
		this.regions = regions;
	}

	public Long[] getRegionIds() {
		return regionIds;
	}

	public void setRegionIds(Long[] regionIds) {
		this.regionIds = regionIds;
	}

	public Long[] getZoneIds() {
		return zoneIds;
	}

	public void setZoneIds(Long[] zoneIds) {
		this.zoneIds = zoneIds;
	}

	public List<AmpCategoryValueLocations> getZones() {
		return zones;
	}

	public void setZones(List<AmpCategoryValueLocations> zones) {
		this.zones = zones;
	}

	public Integer getLargestProjectNumber() {
		return largestProjectNumber;
	}

	public void setLargestProjectNumber(Integer largestProjectNumb) {
		this.largestProjectNumber = largestProjectNumb;
	}

	public List<AmpFiscalCalendar> getFiscalCalendars() {
		return fiscalCalendars;
	}

	public void setFiscalCalendars(List<AmpFiscalCalendar> fiscalCalendars) {
		this.fiscalCalendars = fiscalCalendars;
	}

	public Long getFiscalCalendarId() {
		return fiscalCalendarId;
	}

	public void setFiscalCalendarId(Long fiscalCalendarId) {
		this.fiscalCalendarId = fiscalCalendarId;
	}

	public Long getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(Long orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public Long[] getOrgGroupIds() {
		return orgGroupIds;
	}

	public void setOrgGroupIds(Long[] orgGroupIds) {
		this.orgGroupIds = orgGroupIds;
	}

	public List<AmpOrgGroup> getOrgGroups() {
		return orgGroups;
	}

	public void setOrgGroups(List<AmpOrgGroup> orgGroups) {
		this.orgGroups = orgGroups;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public List<AmpCurrency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<AmpCurrency> currencies) {
		this.currencies = currencies;
	}

	public Long getCurrencyId() {
		if (currencyId == null) {
			currencyId = CurrencyUtil.getCurrencyByCode("USD")
					.getAmpCurrencyId();
		}
		return currencyId;
	}

	public void setCurrencyId(Long currency) {
		this.currencyId = currency;
	}

	public List<AmpOrganisation> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<AmpOrganisation> organizations) {
		this.organizations = organizations;
	}

	public Map<Integer, Integer> getYears() {
        return years;
    }

    public void setYears(Map<Integer, Integer> years) {
        this.years = years;
    }
    
	public Boolean getWorkspaceOnly() {
		return workspaceOnly;
	}

	public void setWorkspaceOnly(Boolean workspaceOnly) {
		this.workspaceOnly = workspaceOnly;
	}

	public String getCurrencyCode() {
		String name = "USD";
		if (currencyId != null && currencyId != -1) {
			AmpCurrency curr = CurrencyUtil.getAmpcurrency(currencyId);
			name = curr.getCurrencyCode();
		}
		return name;

	}

	public Long[] getSelSectorIds() {
		return selSectorIds;
	}

	public void setSelSectorIds(Long[] selSectorIds) {
		this.selSectorIds = selSectorIds;
	}

	public Long[] getSubSectorIds() {
		return subSectorIds;
	}

	public void setSubSectorIds(Long[] subSectorIds) {
		this.subSectorIds = subSectorIds;
	}

	public Long[] getSelLocationIds() {
		return selLocationIds;
	}

	public Boolean getDivideThousands() {
		return divideThousands;
	}

	public void setDivideThousands(Boolean divideThousands) {
		this.divideThousands = divideThousands;
	}

	public Integer getDivideThousandsDecimalPlaces() {
		return divideThousandsDecimalPlaces;
	}

	public void setDivideThousandsDecimalPlaces(
			Integer divideThousandsDecimalPlaces) {
		this.divideThousandsDecimalPlaces = divideThousandsDecimalPlaces;
	}

	public Boolean getFromPublicView() {
		return fromPublicView;
	}

	public void setFromPublicView(Boolean fromPublicView) {
		this.fromPublicView = fromPublicView;
	}

	public Boolean getShowOnlyApprovedActivities() {
		return showOnlyApprovedActivities;
	}

	public void setShowOnlyApprovedActivities(Boolean showOnlyApprovedActivities) {
		this.showOnlyApprovedActivities = showOnlyApprovedActivities;
	}

	public List<AmpOrganisation> getOrganizationsSelected() {
		return organizationsSelected;
	}

	public void setOrganizationsSelected(
			List<AmpOrganisation> organizationsSelected) {
		this.organizationsSelected = organizationsSelected;
	}

	public List<AmpSector> getSectors() {
		return sectors;
	}

	public void setSectors(List<AmpSector> sectors) {
		this.sectors = sectors;
	}

	public List<AmpSector> getSectorsSelected() {
		return sectorsSelected;
	}

	public void setSectorsSelected(List<AmpSector> sectorsSelected) {
		this.sectorsSelected = sectorsSelected;
	}

	public Long getSectorId() {
		return sectorId;
	}

	public void setSectorId(Long sectorId) {
		this.sectorId = sectorId;
	}

	public List<AmpCategoryValueLocations> getLocationsSelected() {
		return locationsSelected;
	}

	public void setLocationsSelected(
			List<AmpCategoryValueLocations> locationsSelected) {
		this.locationsSelected = locationsSelected;
	}

	public Long[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(Long[] orgIds) {
		this.orgIds = orgIds;
	}

	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}

	public TeamMember getTeamMember() {
		return teamMember;
	}

	public Long[] getSetLocationIds() {
		return selLocationIds;
	}

	public void setSelLocationIds(Long[] locationIds) {
		this.selLocationIds = locationIds;
	}

	public boolean isCommitmentsVisible() {
		return commitmentsVisible;
	}

	public boolean isDisbursementsVisible() {
		return disbursementsVisible;
	}

	public boolean isExpendituresVisible() {
		return expendituresVisible;
	}

	public boolean isPledgeVisible() {
		return pledgeVisible;
	}

	public void setSectorIds(Long[] sectorIds) {
		this.sectorIds = sectorIds;
	}

	public Long[] getSectorIds() {
		return sectorIds;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSubSectorId() {
		return subSectorId;
	}

	public void setSubSectorId(Long subSectorId) {
		this.subSectorId = subSectorId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long[] getSelOrgGroupIds() {
		return selOrgGroupIds;
	}

	public void setSelOrgGroupIds(Long[] selOrgGroupIds) {
		this.selOrgGroupIds = selOrgGroupIds;
	}

	public int getDecimalsToShow() {
		if (decimalsToShow == 0)
			decimalsToShow = 2;
		return decimalsToShow;
	}

	public void setDecimalsToShow(int decimalsToShow) {
		this.decimalsToShow = decimalsToShow;
	}


	public List<AmpOrgType> getOrganizationsType() {
		return organizationsType;
	}


	public void setOrganizationsType(List<AmpOrgType> organizationstype) {
		this.organizationsType = organizationstype;
	}


	public List<AmpOrgType> getOrganizationsTypeSelected() {
		return organizationsTypeSelected;
	}


	public void setOrganizationsTypeSelected(
			List<AmpOrgType> organizationstypeselected) {
		this.organizationsTypeSelected = organizationstypeselected;
	}


	public Long[] getOrgtypeIds() {
		return orgtypeIds;
	}


	public void setOrgtypeIds(Long[] orgtypeIds) {
		this.orgtypeIds = orgtypeIds;
	}


	public void setImplOrgGroupIds(Long[] implOrgGroupIds) {
		this.implOrgGroupIds = implOrgGroupIds;
	}


	public Long[] getImplOrgGroupIds() {
		return implOrgGroupIds;
	}


	public void setImplOrgIds(Long[] implOrgIds) {
		this.implOrgIds = implOrgIds;
	}


	public Long[] getImplOrgIds() {
		return implOrgIds;
	}


	public void setOrganizationsTypeId(Long organizationsTypeId) {
		this.organizationsTypeId = organizationsTypeId;
	}


	public Long getOrganizationsTypeId() {
		return organizationsTypeId;
	}


	public void setProjectStatusId(Long projectStatusId) {
		this.projectStatusId = projectStatusId;
	}


	public Long getProjectStatusId() {
		return projectStatusId;
	}


	public void setFundingLimit(BigDecimal fundingLimit) {
		this.fundingLimit = fundingLimit;
	}


	public BigDecimal getFundingLimit() {
		return fundingLimit;
	}


	public void setFundingLimitAbove(Boolean fundingLimitAbove) {
		this.fundingLimitAbove = fundingLimitAbove;
	}


	public Boolean getFundingLimitAbove() {
		return fundingLimitAbove;
	}


	public void setOnBudget(Long onBudget) {
		this.onBudget = onBudget;
	}


	public Long getOnBudget() {
		return onBudget;
	}


	public void setTypeAssistanceId(Long typeAssistanceId) {
		this.typeAssistanceId = typeAssistanceId;
	}


	public Long getTypeAssistanceId() {
		return typeAssistanceId;
	}


	public void setFinancingInstrumentId(Long financingInstrumentId) {
		this.financingInstrumentId = financingInstrumentId;
	}


	public Long getFinancingInstrumentId() {
		return financingInstrumentId;
	}


	public void setStructureTypes(List<AmpStructureType> structureTypes) {
		this.structureTypes = structureTypes;
	}

	public List<AmpStructureType> getStructureTypes() {
		return this.structureTypes;
	}


	public void setSelStructureTypes(Long[] selStructureTypes) {
		this.selStructureTypes = selStructureTypes;
	}


	public Long[] getSelStructureTypes() {
		return selStructureTypes;
	}

	public boolean isModeexport() {
		return modeexport;
	}

	public void setModeexport(boolean modeexport) {
		this.modeexport = modeexport;
	}

	public String getReportfilterquery() {
		return reportfilterquery;
	}

	public void setReportfilterquery(String reportfilterquery) {
		this.reportfilterquery = reportfilterquery;
	}


}
