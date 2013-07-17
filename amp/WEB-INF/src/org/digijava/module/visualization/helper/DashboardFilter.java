package org.digijava.module.visualization.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class DashboardFilter {
    
    private int dashboardType;
    private Long startYear;
    private Long endYear;
    private Long defaultStartYear;
    private Long defaultEndYear;
    private Long startYearQuickFilter;
    private Long endYearQuickFilter;
    private Long startYearFilter;
    private Long endYearFilter;
    private Long currencyId;
    private Long currencyIdQuickFilter;
    private Boolean workspaceOnly;
    private Boolean workspaceOnlyQuickFilter;
    private Boolean showMonochrome;
    private TeamMember teamMember;
    private List<AmpCurrency>currencies;
    private List<AmpOrganisation>organizations;
    private Long[] selOrgIds;
    private List<AmpOrgGroup> orgGroups;
    private List<AmpSector>sectors;
    private List<AmpCategoryValueLocations> regions;
    private List<AmpCategoryValueLocations> zones;
    private List<AmpClassificationConfiguration> sectorConfigs;
    
    private Long[] orgGroupIds;
    private Long orgGroupId;
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
    
    private Long[] budgetCVIds;
    private Long[] selCVIds;
    private Long[] selProgramIds;
    private Long[] selSectorIds;
    private Long[] selLocationIds;
    private Long[] selOrgGroupIds;
    private Long selSectorConfigId;


	private List<AmpSector>sectorsSelected;
    
    private List<AmpCategoryValueLocations> locationsSelected;
   
    private Map<String, Integer> years;
    private int transactionType = 1;
    private int transactionTypeQuickFilter = 1;
    private int transactionTypeFilter = 1;
    private List<AmpFiscalCalendar> fiscalCalendars;
    private Long fiscalCalendarId;
    private Integer largestProjectNumber;
    private Boolean divideThousands;
    private Integer divideThousandsDecimalPlaces;
   
    private Long yearToCompare;
    private Boolean commitmentsVisible;
    private Boolean disbursementsVisible;
    private Boolean pledgeVisible;
    private Boolean expendituresVisible;
    private Boolean fromPublicView;
    private Boolean showOnlyApprovedActivities;
    private Boolean showOnlyNonDraftActivities;
    private Boolean showAmountsInThousands;
    private Boolean showProjectsRanking;
    private Boolean showOrganizationsRanking;
    private Boolean showSectorsRanking;
    private Boolean showRegionsRanking;
    private Boolean showNPORanking;
    private Boolean showProgramsRanking;
    private Boolean fromGenerator;
    private Boolean orgInfoEditable = false;
    private int agencyType;
    private int agencyTypeQuickFilter;
    private int agencyTypeFilter;
    
    private Long activityId;
    private int decimalsToShow;
    private String groupSeparator = FormatHelper.getGroupSymbol();
    private String decimalSeparator = FormatHelper.getDecimalSymbol();
    private List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupWithOrgsList;
    private List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones;
    //private List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorWithSubSectors;
    private List<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>> configWithSectorAndSubSectors;
    private ArrayList<AmpSector> allSectorList;
    private ArrayList<AmpCategoryValueLocations> allLocationsList;
    private int topLists = 5;

	public int getTopLists() {
		return topLists;
	}

	public void setTopLists(int topLists) {
		this.topLists = topLists;
	}

	public DashboardFilter getCopyFilterForFunding(){
    	DashboardFilter newFilter = new DashboardFilter();
    	
    	newFilter.setCurrencyId(this.getCurrencyId());
    	newFilter.setSelOrgIds(this.getSelOrgIds());
    	newFilter.setSelOrgGroupIds(this.getSelOrgGroupIds());
    	newFilter.setOrgGroupIds(this.getOrgGroupIds());
    	newFilter.setOrgGroupId(this.getOrgGroupId());
    	newFilter.setTeamMember(this.getTeamMember());
    	newFilter.setSelLocationIds(this.getSelLocationIds());
    	newFilter.setSelSectorIds(this.getSelSectorIds());
    	newFilter.setActivityId(this.getActivityId());
    	newFilter.setShowOnlyApprovedActivities(this.getShowOnlyApprovedActivities());
    	newFilter.setShowOnlyNonDraftActivities(this.getShowOnlyNonDraftActivities());
    	newFilter.setFromPublicView(this.getFromPublicView());
    	newFilter.setSelSectorConfigId(this.getSelSectorConfigId());
    	newFilter.setSectorIds(this.getSectorIds());
    	newFilter.setAllSectorList(this.getAllSectorList());
    	newFilter.setTransactionType(this.getTransactionType());
    	newFilter.setAgencyType(this.getAgencyType());
    	newFilter.setSelProgramIds(this.getSelProgramIds());
    	newFilter.setSelCVIds(this.getSelCVIds());
    	newFilter.setBudgetCVIds(this.getBudgetCVIds());
    	newFilter.setDivideThousands(this.getDivideThousands());
    	newFilter.setShowAmountsInThousands(this.getShowAmountsInThousands());
    	newFilter.setActivityComputedList(this.getActivityComputedList());
    	return newFilter;
    }
	
	
	

   /* public List<EntityRelatedListHelper<AmpSector, AmpSector>> getSectorWithSubSectors() {
		return sectorWithSubSectors;
	}



	public void setSectorWithSubSectors(
			List<EntityRelatedListHelper<AmpSector, AmpSector>> sectorWithSubSectors) {
		this.sectorWithSubSectors = sectorWithSubSectors;
	}*/



	public List<EntityRelatedListHelper<AmpOrgGroup, AmpOrganisation>> getOrgGroupWithOrgsList() {
		return orgGroupWithOrgsList;
	}


	public void setOrgGroupWithOrgsList(
			List<EntityRelatedListHelper<AmpOrgGroup, AmpOrganisation>> orgGroupWithOrgsList) {
		this.orgGroupWithOrgsList = orgGroupWithOrgsList;
	}
	
	


	public List<EntityRelatedListHelper<AmpClassificationConfiguration, EntityRelatedListHelper<AmpSector, AmpSector>>> getConfigWithSectorAndSubSectors() {
		return configWithSectorAndSubSectors;
	}


	public void setConfigWithSectorAndSubSectors(
			List<EntityRelatedListHelper<AmpClassificationConfiguration, EntityRelatedListHelper<AmpSector, AmpSector>>> configWithSectorAndSubSectors) {
		this.configWithSectorAndSubSectors = configWithSectorAndSubSectors;
	}


	public List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> getRegionWithZones() {
		return regionWithZones;
	}


	public void setRegionWithZones(
			List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> regionWithZones) {
		this.regionWithZones = regionWithZones;
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

    public Long getYearToCompare() {
		return yearToCompare;
	}

	public void setYearToCompare(Long yearToCompare) {
		this.yearToCompare = yearToCompare;
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
    
    public int getTransactionTypeQuickFilter() {
        return transactionTypeQuickFilter;
    }

    public void setTransactionTypeQuickFilter(int transactionTypeQuickFilter) {
        this.transactionTypeQuickFilter = transactionTypeQuickFilter;
    }
    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
        this.transactionTypeQuickFilter = transactionType;
    }
    
    public void setTransactionTypeFilter(int transactionTypeFilter) {
        this.transactionTypeFilter = transactionTypeFilter;
    }
    public int getTransactionTypeFilter() {
        return transactionTypeFilter;
    }

    public List<AmpCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<AmpCurrency> currencies) {
        this.currencies = currencies;
    }

    public Long getCurrencyId() {
    	if (currencyId == null) {
    		String defaultBaseCurrencyId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
    		if (defaultBaseCurrencyId != null && !defaultBaseCurrencyId.equals(""))
    			currencyId = CurrencyUtil.getCurrencyByCode(defaultBaseCurrencyId).getAmpCurrencyId();
    		else
    			currencyId = CurrencyUtil.getCurrencyByCode("USD").getAmpCurrencyId();
		}
        return currencyId;
    }

    public void setCurrencyId(Long currency) {
        this.currencyId = currency;
        this.currencyIdQuickFilter = currency;
    }

    public void setCurrencyIdQuickFilter(Long currency) {
        this.currencyIdQuickFilter = currency;
    }

    public Long getCurrencyIdQuickFilter() {
    	if (currencyIdQuickFilter == null) {
    		String defaultBaseCurrency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
    		if (defaultBaseCurrency != null && !defaultBaseCurrency.equals(""))
    			currencyIdQuickFilter = CurrencyUtil.getCurrencyByCode(defaultBaseCurrency).getAmpCurrencyId();
    		else
    			currencyIdQuickFilter = CurrencyUtil.getCurrencyByCode("USD").getAmpCurrencyId();
		}
        return currencyIdQuickFilter;
    }

    public List<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public Long getStartYear() {
        return startYear;
    }

    public void setStartYear(Long year) {
        this.startYear = year;
        this.startYearFilter = year;
        this.startYearQuickFilter = year;
    }

    public Long getEndYear() {
        return endYear;
    }

    public void setEndYear(Long year) {
        this.endYear = year;
        this.endYearFilter = year;
        this.endYearQuickFilter = year;
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

	public Map<String, Integer> getYears() {
        return years;
    }

    public void setYears(Map<String, Integer> years) {
        this.years = years;
    }
    
    public Boolean getWorkspaceOnly() {
        return workspaceOnly;
    }

    public void setWorkspaceOnly(Boolean workspaceOnly) {
        this.workspaceOnly = workspaceOnly;
        this.workspaceOnlyQuickFilter = workspaceOnly;
    }
    
    public Boolean getWorkspaceOnlyQuickFilter() {
        return workspaceOnlyQuickFilter;
    }

    public void setWorkspaceOnlyQuickFilter(Boolean workspaceOnlyQuickFilter) {
        this.workspaceOnlyQuickFilter = workspaceOnlyQuickFilter;
    }
    /*
    public String getOrgGroupName() {
        AmpOrgGroup group= DbUtil.getAmpOrgGroup(organizationGroupId);
        String name=null;
        if(group!=null){
            name=group.getOrgGrpName();
        }
        return name;
    }
    
    public String getOrgsName() {
        String name = "";
        if (orgIds != null) {
            for (Long id : orgIds) {
                AmpOrganisation organization = DbUtil.getOrganisation(id);
                name += organization.getName() + ", ";
            }
            if (name.length() > 0) {
                name = name.substring(0, name.length() - 1);
            }
        }
        return name;

    }
    public String getLocationsName() throws DgException {
        String name = "";
        if (selZoneIds != null && selZoneIds.length > 0 && selZoneIds[0] != -1) {
            for (Long zoneId : selZoneIds) {
                AmpCategoryValueLocations location = LocationUtil.getAmpCategoryValueLocationById(zoneId);
                name += location.getName() + ",";
            }
            if (name.length() > 0) {
                name = name.substring(0, name.length() - 1);
            }
        } else {
            if (selRegionIds != null && selRegionIds != -1) {
                AmpCategoryValueLocations location = LocationUtil.getAmpCategoryValueLocationById(selRegionId);
                name += location.getName();
            }
        }
        return name;

    }*/
      public String getCurrencyCode() {
  		String defaultBaseCurrency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
  		String name = "USD";
		if (defaultBaseCurrency != null && !defaultBaseCurrency.equals(""))
			name = defaultBaseCurrency;

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

    public void setDivideThousandsDecimalPlaces(Integer divideThousandsDecimalPlaces) {
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

	public void setDashboardType(int dashboardType) {
		this.dashboardType = dashboardType;
	}

	public int getDashboardType() {
		return dashboardType;
	}

	public Long[] getSelOrgIds() {
		return selOrgIds;
	}

	public void setSelOrgIds(Long[] selOrgIds) {
		this.selOrgIds = selOrgIds;
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
		if(decimalsToShow == 0) decimalsToShow = 2;
		return decimalsToShow;
	}

	public void setDecimalsToShow(int decimalsToShow) {
		this.decimalsToShow = decimalsToShow;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public String getGroupSeparator() {
		return groupSeparator;
	}

	public void setGroupSeparator(String groupSeparator) {
		this.groupSeparator = groupSeparator;
	}

	public Boolean getShowMonochrome() {
		return showMonochrome;
	}

	public void setShowMonochrome(Boolean showMonochrome) {
		this.showMonochrome = showMonochrome;
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


	public void setAllSectorList(ArrayList<AmpSector> allSectorList) {
		this.allSectorList = allSectorList;
	}
	public ArrayList<AmpSector> getAllSectorList() {
		return allSectorList;
	}

	public void setStartYearQuickFilter(Long startYearQuickFilter) {
		this.startYearQuickFilter = startYearQuickFilter;
	}

	public Long getStartYearQuickFilter() {
		return startYearQuickFilter;
	}

	public void setEndYearQuickFilter(Long endYearQuickFilter) {
		this.endYearQuickFilter = endYearQuickFilter;
	}

	public Long getEndYearQuickFilter() {
		return endYearQuickFilter;
	}

	public void setStartYearFilter(Long startYearFilter) {
		this.startYearFilter = startYearFilter;
	}

	public Long getStartYearFilter() {
		return startYearFilter;
	}

	public void setEndYearFilter(Long endYearFilter) {
		this.endYearFilter = endYearFilter;
	}

	public Long getEndYearFilter() {
		return endYearFilter;
	}

	public void setAllLocationsList(ArrayList<AmpCategoryValueLocations> allLocationsList) {
		this.allLocationsList = allLocationsList;
	}

	public ArrayList<AmpCategoryValueLocations> getAllLocationsList() {
		return allLocationsList;
	}

	public void setShowOnlyNonDraftActivities(Boolean showOnlyNonDraftActivities) {
		this.showOnlyNonDraftActivities = showOnlyNonDraftActivities;
	}

	public Boolean getShowOnlyNonDraftActivities() {
		return showOnlyNonDraftActivities;
	}

	public Boolean getShowAmountsInThousands() {
		return showAmountsInThousands;
	}

	public void setShowAmountsInThousands(Boolean showAmountsInThousands) {
		this.showAmountsInThousands = showAmountsInThousands;
	}
	public Boolean getShowProjectsRanking() {
		return showProjectsRanking;
	}

	public void setShowProjectsRanking(Boolean showProjectsRanking) {
		this.showProjectsRanking = showProjectsRanking;
	}

	public Boolean getShowOrganizationsRanking() {
		return showOrganizationsRanking;
	}

	public void setShowOrganizationsRanking(Boolean showOrganizationsRanking) {
		this.showOrganizationsRanking = showOrganizationsRanking;
	}

	public Boolean getShowSectorsRanking() {
		return showSectorsRanking;
	}

	public void setShowSectorsRanking(Boolean showSectorsRanking) {
		this.showSectorsRanking = showSectorsRanking;
	}

	public Boolean getShowRegionsRanking() {
		return showRegionsRanking;
	}

	public void setShowRegionsRanking(Boolean showRegionsRanking) {
		this.showRegionsRanking = showRegionsRanking;
	}

	public Boolean getFromGenerator() {
		return fromGenerator;
	}

	public void setFromGenerator(Boolean fromGenerator) {
		this.fromGenerator = fromGenerator;
	}

	public int getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(int agencyType) {
		this.agencyType = agencyType;
		this.agencyTypeFilter = agencyType;
		this.agencyTypeQuickFilter = agencyType;
	}

	public int getAgencyTypeQuickFilter() {
		return agencyTypeQuickFilter;
	}

	public void setAgencyTypeQuickFilter(int agencyTypeQuickFilter) {
		this.agencyTypeQuickFilter = agencyTypeQuickFilter;
	}

	public int getAgencyTypeFilter() {
		return agencyTypeFilter;
	}

	public void setAgencyTypeFilter(int agencyTypeFilter) {
		this.agencyTypeFilter = agencyTypeFilter;
	}

	public Long[] getSelProgramIds() {
		return selProgramIds;
	}

	public void setSelProgramIds(Long[] selProgramIds) {
		this.selProgramIds = selProgramIds;
	}

	public Boolean getShowNPORanking() {
		return showNPORanking;
	}

	public void setShowNPORanking(Boolean showNPORanking) {
		this.showNPORanking = showNPORanking;
	}

	public Boolean getShowProgramsRanking() {
		return showProgramsRanking;
	}

	public void setShowProgramsRanking(Boolean showProgramsRanking) {
		this.showProgramsRanking = showProgramsRanking;
	}

	public Long[] getSelCVIds() {
		return selCVIds;
	}

	public void setSelCVIds(Long[] selCVIds) {
		this.selCVIds = selCVIds;
	}

	public Long[] getBudgetCVIds() {
		return budgetCVIds;
	}

	public void setBudgetCVIds(Long[] budgetCVIds) {
		this.budgetCVIds = budgetCVIds;
	}
	private ArrayList<BigInteger> activityList;
	public void setActivityComputedList(ArrayList<BigInteger> activityList) {
		this.activityList = activityList;
	}
	public ArrayList<BigInteger> getActivityComputedList() {
		return this.activityList;
	}
	public Boolean getOrgInfoEditable() {
		return this.orgInfoEditable;
	}
	public void setOrgInfoEditable(Boolean orgInfoEditable) {
		this.orgInfoEditable = orgInfoEditable;
	}

}
