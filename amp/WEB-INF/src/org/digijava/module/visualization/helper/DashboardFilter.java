package org.digijava.module.visualization.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

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
    private Long currencyIdDefault;
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
    
    private Integer showAmountsInThousands;
    private Integer showAmountsInThousandsDefault;
    private Boolean showProjectsRanking;
    private Boolean showOrganizationsRanking;
    private Boolean showSectorsRanking;
    private Boolean showRegionsRanking;
    private Boolean showNPORanking;
    private Boolean showProgramsRanking;
    private Boolean showSecondaryProgramsRanking;
    private Boolean fromGenerator;
    private int agencyType;
    private int agencyTypeDefault;
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
    private String flashSliderLabels = "";
    private String adjustmentType = "Actual";
    private String adjustmentTypeFilter = "Actual";
    private String adjustmentTypeQuickFilter = "Actual";
    private List<CategoryConstants.HardCodedCategoryValue>adjustmentTypeList;
    private Long[] selStatusIds;
    private List<AmpCategoryValue>statusList;
    
    //fields for Deal Dashboard
    
    private List<AmpOrganisation> donorAgencyList;
    private List<AmpOrganisation> implementingAgencyList;
    private List<AmpOrganisation> beneficiaryAgencyList;
    private List<AmpCategoryValue> peacebuilderMarkerList;
    private List<AmpCategoryValue> peacebuildingList;
    private List<AmpSector>secondarySectors;
    private List<AmpTheme>secondaryProgramsList;
    private Long[] donorAgencyIds;
    private Long donorAgencyId;
    private Long[] implementingAgencyIds;
    private Long[] selImplementingAgencyIds;
    private Long implementingAgencyId;
    private Long[] beneficiaryAgencyIds;
    private Long[] selBeneficiaryAgencyIds;
    private Long beneficiaryAgencyId;
    private Long[] secondarySectorsIds;
    private Long secondarySectorsId;
    private Long[] peacebuilderMarkerIds;
    private Long peacebuilderMarkerId;
    private Long[] selPeacebuilderMarkerIds;
    private Long[] peacebuildingIds;
    private Long peacebuildingId;
    private Long[] secondaryProgramIds;
    private Long secondaryProgramId;
    private Long[] selSecondaryProgramIds;
    
     
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
    	newFilter.setSectorIds(this.getSectorIds());
    	newFilter.setActivityId(this.getActivityId());
    	newFilter.setShowOnlyApprovedActivities(this.getShowOnlyApprovedActivities());
    	newFilter.setFromPublicView(this.getFromPublicView());
    	newFilter.setSelSectorConfigId(this.getSelSectorConfigId());
    	newFilter.setAllSectorList(this.getAllSectorList());
    	newFilter.setTransactionType(this.getTransactionType());
    	newFilter.setAgencyType(this.getAgencyType());
    	newFilter.setSelProgramIds(this.getSelProgramIds());
    	newFilter.setSelCVIds(this.getSelCVIds());
    	newFilter.setBudgetCVIds(this.getBudgetCVIds());
    	newFilter.setDivideThousands(this.getDivideThousands());
    	newFilter.setShowAmountsInThousands(this.getShowAmountsInThousands());
    	newFilter.setActivityComputedList(this.getActivityComputedList());
    	newFilter.setAdjustmentType(this.getAdjustmentType());
    	newFilter.setDonorAgencyId(this.getDonorAgencyId());
    	newFilter.setBeneficiaryAgencyId(this.getBeneficiaryAgencyId());
    	newFilter.setImplementingAgencyId(this.getImplementingAgencyId());
    	newFilter.setSelBeneficiaryAgencyIds(this.getSelBeneficiaryAgencyIds());
    	newFilter.setSelImplementingAgencyIds(this.getSelImplementingAgencyIds());
    	newFilter.setPeacebuilderMarkerId(this.getPeacebuilderMarkerId());
    	newFilter.setSelPeacebuilderMarkerIds(this.getSelPeacebuilderMarkerIds());
    	newFilter.setPeacebuildingId(this.getPeacebuildingId());
    	newFilter.setEndYear(this.getEndYear());
    	newFilter.setStartYear(this.getStartYear());
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
    	this.transactionTypeFilter = transactionType;
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

	/**
	 * returns whether to show amounts in thousands or millins
	 * @return
	 */
	public Integer getShowAmountsInThousands() {
		return showAmountsInThousands;
	}

	/**
	 * false = show in millions, true = show in thousands
	 * @param showAmountsInThousands
	 */
	public void setShowAmountsInThousands(Integer showAmountsInThousands) {
		this.showAmountsInThousands = showAmountsInThousands;
	}
	
	public boolean shouldShowAmountsInThousands()
	{
		if (showAmountsInThousands == null)
			return true;
		return showAmountsInThousands == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS;
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

	public Boolean getShowSecondaryProgramsRanking() {
		return showSecondaryProgramsRanking;
	}

	public void setShowSecondaryProgramsRanking(Boolean showSecondaryProgramsRanking) {
		this.showSecondaryProgramsRanking = showSecondaryProgramsRanking;
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

	public String getFlashSliderLabels() {
		return flashSliderLabels;
	}

	public void setFlashSliderLabels(String flashSliderLabels) {
		this.flashSliderLabels = flashSliderLabels;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
		this.adjustmentTypeQuickFilter = adjustmentType;
	}

	public String getAdjustmentTypeQuickFilter() {
		return adjustmentTypeQuickFilter;
	}

	public void setAdjustmentTypeQuickFilter(String adjustmentTypeQuickFilter) {
		this.adjustmentTypeQuickFilter = adjustmentTypeQuickFilter;
	}

	public String getAdjustmentTypeFilter() {
		return adjustmentTypeFilter;
	}

	public void setAdjustmentTypeFilter(String adjustmentTypeFilter) {
		this.adjustmentTypeFilter = adjustmentTypeFilter;
	}

	public List<CategoryConstants.HardCodedCategoryValue> getAdjustmentTypeList() {
		return adjustmentTypeList;
	}

	public void setAdjustmentTypeList(
			List<CategoryConstants.HardCodedCategoryValue> adjustmentTypeList) {
		this.adjustmentTypeList = adjustmentTypeList;
	}

	public Long[] getSelStatusIds() {
		return selStatusIds;
	}

	public void setSelStatusIds(Long[] selStatusIds) {
		this.selStatusIds = selStatusIds;
	}

	public List<AmpCategoryValue> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<AmpCategoryValue> statusList) {
		this.statusList = statusList;
	}

	public Boolean getShowOnlyNonDraftActivities() {
		return showOnlyNonDraftActivities;
	}

	public void setShowOnlyNonDraftActivities(Boolean showOnlyNonDraftActivities) {
		this.showOnlyNonDraftActivities = showOnlyNonDraftActivities;
	}
	
	//fields for Deal Dashboard
    
	public List<AmpOrganisation> getDonorAgencyList() {
		return donorAgencyList;
	}

	public void setDonorAgencyList(List<AmpOrganisation> donorAgencyList) {
		this.donorAgencyList = donorAgencyList;
	}

	public List<AmpOrganisation> getImplementingAgencyList() {
		return implementingAgencyList;
	}

	public void setImplementingAgencyList(
			List<AmpOrganisation> implementingAgencyList) {
		this.implementingAgencyList = implementingAgencyList;
	}

	public List<AmpOrganisation> getBeneficiaryAgencyList() {
		return beneficiaryAgencyList;
	}

	public void setBeneficiaryAgencyList(List<AmpOrganisation> beneficiaryAgencyList) {
		this.beneficiaryAgencyList = beneficiaryAgencyList;
	}

	public List<AmpCategoryValue> getPeacebuilderMarkerList() {
		return peacebuilderMarkerList;
	}

	public void setPeacebuilderMarkerList(
			List<AmpCategoryValue> peacebuilderMarkerList) {
		this.peacebuilderMarkerList = peacebuilderMarkerList;
	}

	public List<AmpCategoryValue> getPeacebuildingList() {
		return peacebuildingList;
	}

	public void setPeacebuildingList(List<AmpCategoryValue> peacebuildingList) {
		this.peacebuildingList = peacebuildingList;
	}

	public List<AmpSector> getSecondarySectors() {
		return secondarySectors;
	}

	public void setSecondarySectors(List<AmpSector> secondarySectors) {
		this.secondarySectors = secondarySectors;
	}

	public Long[] getDonorAgencyIds() {
		return donorAgencyIds;
	}

	public void setDonorAgencyIds(Long[] donorAgencyIds) {
		this.donorAgencyIds = donorAgencyIds;
	}

	public Long getDonorAgencyId() {
		return donorAgencyId;
	}

	public void setDonorAgencyId(Long donorAgencyId) {
		this.donorAgencyId = donorAgencyId;
	}

	public Long[] getImplementingAgencyIds() {
		return implementingAgencyIds;
	}

	public void setImplementingAgencyIds(Long[] implementingAgencyIds) {
		this.implementingAgencyIds = implementingAgencyIds;
	}

	public Long getImplementingAgencyId() {
		return implementingAgencyId;
	}

	public void setImplementingAgencyId(Long implementingAgencyId) {
		this.implementingAgencyId = implementingAgencyId;
	}

	public Long[] getBeneficiaryAgencyIds() {
		return beneficiaryAgencyIds;
	}

	public void setBeneficiaryAgencyIds(Long[] beneficiaryAgencyIds) {
		this.beneficiaryAgencyIds = beneficiaryAgencyIds;
	}

	public Long[] getSelImplementingAgencyIds() {
		return selImplementingAgencyIds;
	}

	public void setSelImplementingAgencyIds(Long[] selImplementingAgencyIds) {
		this.selImplementingAgencyIds = selImplementingAgencyIds;
	}

	public Long[] getSelBeneficiaryAgencyIds() {
		return selBeneficiaryAgencyIds;
	}

	public void setSelBeneficiaryAgencyIds(Long[] selBeneficiaryAgencyIds) {
		this.selBeneficiaryAgencyIds = selBeneficiaryAgencyIds;
	}

	public Long getBeneficiaryAgencyId() {
		return beneficiaryAgencyId;
	}

	public void setBeneficiaryAgencyId(Long beneficiaryAgencyId) {
		this.beneficiaryAgencyId = beneficiaryAgencyId;
	}

	public Long[] getSecondarySectorsIds() {
		return secondarySectorsIds;
	}

	public void setSecondarySectorsIds(Long[] secondarySectorsIds) {
		this.secondarySectorsIds = secondarySectorsIds;
	}

	public Long getSecondarySectorsId() {
		return secondarySectorsId;
	}

	public void setSecondarySectorsId(Long secondarySectorsId) {
		this.secondarySectorsId = secondarySectorsId;
	}

	public Long[] getPeacebuilderMarkerIds() {
		return peacebuilderMarkerIds;
	}

	public void setPeacebuilderMarkerIds(Long[] peacebuilderMarkerIds) {
		this.peacebuilderMarkerIds = peacebuilderMarkerIds;
	}

	public Long getPeacebuilderMarkerId() {
		return peacebuilderMarkerId;
	}

	public void setPeacebuilderMarkerId(Long peacebuilderMarkerId) {
		this.peacebuilderMarkerId = peacebuilderMarkerId;
	}

	public Long[] getPeacebuildingIds() {
		return peacebuildingIds;
	}

	public void setPeacebuildingIds(Long[] peacebuildingIds) {
		this.peacebuildingIds = peacebuildingIds;
	}

	public Long getPeacebuildingId() {
		return peacebuildingId;
	}

	public void setPeacebuildingId(Long peacebuildingId) {
		this.peacebuildingId = peacebuildingId;
	}

	public List<AmpTheme> getSecondaryProgramsList() {
		return secondaryProgramsList;
	}

	public void setSecondaryProgramsList(List<AmpTheme> secondaryProgramsList) {
		this.secondaryProgramsList = secondaryProgramsList;
	}

	public Long[] getSecondaryProgramIds() {
		return secondaryProgramIds;
	}

	public void setSecondaryProgramIds(Long[] secondaryProgramIds) {
		this.secondaryProgramIds = secondaryProgramIds;
	}

	public Long getSecondaryProgramId() {
		return secondaryProgramId;
	}

	public void setSecondaryProgramId(Long secondaryProgramId) {
		this.secondaryProgramId = secondaryProgramId;
	}

	public Long[] getSelSecondaryProgramIds() {
		return selSecondaryProgramIds;
	}

	public void setSelSecondaryProgramIds(Long[] selSecondaryProgramIds) {
		this.selSecondaryProgramIds = selSecondaryProgramIds;
	}

	public Long[] getSelPeacebuilderMarkerIds() {
		return selPeacebuilderMarkerIds;
	}

	public void setSelPeacebuilderMarkerIds(Long[] selPeacebuilderMarkerIds) {
		this.selPeacebuilderMarkerIds = selPeacebuilderMarkerIds;
	}

	public Long getCurrencyIdDefault() {
		return currencyIdDefault;
	}

	public void setCurrencyIdDefault(Long currencyIdDefault) {
		this.currencyIdDefault = currencyIdDefault;
	}

	public Integer getShowAmountsInThousandsDefault() {
		return showAmountsInThousandsDefault;
	}

	public void setShowAmountsInThousandsDefault(
			Integer showAmountsInThousandsDefault) {
		this.showAmountsInThousandsDefault = showAmountsInThousandsDefault;
	}

	public int getAgencyTypeDefault() {
		return agencyTypeDefault;
	}

	public void setAgencyTypeDefault(int agencyTypeDefault) {
		this.agencyTypeDefault = agencyTypeDefault;
	}
	
}
