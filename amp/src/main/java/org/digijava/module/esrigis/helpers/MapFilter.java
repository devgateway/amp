package org.digijava.module.esrigis.helpers;

import net.sf.json.JSONArray;
import org.apache.commons.lang.ArrayUtils;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.math.BigDecimal;
import java.util.*;

public class MapFilter {
    private static final long serialVersionUID = 1L;
    private boolean isinitialized;
    private Long currencyId;
    private Long defaultCurrencyId;
    private Boolean workspaceOnly;
    private TeamMember teamMember;
    private List<AmpCurrency> currencies;
    private List<OrganizationSkeleton> organizations;
    private List<AmpOrganisation> organizationsSelected;
    private List<AmpOrgType> organizationsType;
    private List<AmpStructureType> structureTypes;
    private Long[] selStructureTypes;
    private Long[] selfinancingInstruments;
    private Long[] seltypeofassistence;
    private Long[] selprojectstatus;
    
    private Long [] selorganizationsTypes;
    
    private BigDecimal fundingLimit;
    private Boolean fundingLimitAbove;
    private Long selectedBudget;
    private List<AmpCategoryValue> financingInstruments;
    private List<AmpCategoryValue> typeofassistences;
    private List<AmpCategoryValue> projectstatus;
    
    private boolean modeexport;
    private String reportfilterquery;
    private Long startYearFilter;
    private Long endYearFilter;
    private Map<Integer, Integer> years;
    private Long startYear;
    private Long endYear;
    private Long defaultStartYear;
    private Long defaultEndYear;
    private List<EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>> orgGroupWithOrgsList;
    private List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> regionWithZones;
    private List<EntityRelatedListHelper<AmpClassificationConfiguration, EntityRelatedListHelper<AmpSector, AmpSector>>> configWithSectorAndSubSectors;
    private Long selSectorConfigId;
    private List<AmpClassificationConfiguration> sectorConfigs;
    private List <AmpCategoryValue> budgets;

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
    private Long defaultFiscalCalendarId;
    private Integer largestProjectNumber;
    private Boolean divideThousands;
    private Integer divideThousandsDecimalPlaces;

    private int yearsInRange;
    private Boolean commitmentsVisible = true;
    private Boolean disbursementsVisible = true;
    private Boolean pledgeVisible = true;
    private Boolean expendituresVisible = true;
    private Boolean fromPublicView = false;
    private Boolean showOnlyApprovedActivities;

    private Long activityId;
    private int decimalsToShow;

    private List <AmpCategoryValue> peacebuildingMarkers;
    private Long selectedPeacebuildingMarkerId;
    private boolean filterByPeacebuildingMarker;
    private Map<String,AmpTheme> programElements;
    private Long[] selectedNatPlanObj;
    private Long[] selectedPrimaryPrograms;
    private Long[] selectedSecondaryPrograms;

    
    public MapFilter getCopyFilterForFunding() {
        MapFilter newFilter = new MapFilter();

        newFilter.setCurrencyId(this.getCurrencyId());
        newFilter.setOrgIds(this.getOrgIds());
        newFilter.setOrgGroupId(this.getOrgGroupId());
        newFilter.setTeamMember(this.getTeamMember());
        newFilter.setSelLocationIds(this.getSelLocationIds());
        newFilter.setSelSectorIds(this.getSelSectorIds());
        newFilter.setActivityId(this.getActivityId());
        newFilter.setShowOnlyApprovedActivities(this
                .getShowOnlyApprovedActivities());
        newFilter.setFromPublicView(this.getFromPublicView());
        return newFilter;
    }

    public JSONArray toJson() {
        JSONArray result = new JSONArray();
        SimpleFilter selectedfilter = new SimpleFilter();
        selectedfilter.setCurrency(this.getCurrencyCode());
        selectedfilter.setStartyear(this.getStartYear().toString());
        selectedfilter.setEndyear(this.getEndYear().toString());

        if (orgGroupIds != null && orgGroupIds.length > 0) {
            for (Iterator<AmpOrgGroup> iterator = orgGroups.iterator(); iterator
                    .hasNext();) {
                AmpOrgGroup group = (AmpOrgGroup) iterator.next();
                for (int i = 0; i < orgGroupIds.length; i++) {
                    if (group.getIdentifier() == orgGroupIds[i]) {
                        selectedfilter.setOrganizationgroup(group
                                .getOrgGrpName());
                    }
                }
            }
        }
        ArrayList<SimpleDonor> donorslist = new ArrayList<SimpleDonor>();
        if (organizationsSelected != null && !organizationsSelected.isEmpty()) {
            for (Iterator<AmpOrganisation> iterator = organizationsSelected
                    .iterator(); iterator.hasNext();) {
                AmpOrganisation org = (AmpOrganisation) iterator.next();
                if (org != null) {
                    SimpleDonor donor = new SimpleDonor();
                    donor.setDonorname(org.getName());
                    donorslist.add(donor);
                }
            }
            selectedfilter.setSelecteddonors(donorslist);
        }

        if (implOrgGroupIds != null && implOrgGroupIds.length > 0) {
            for (Iterator<AmpOrgGroup> iterator = orgGroups.iterator(); iterator
                    .hasNext();) {
                AmpOrgGroup group = (AmpOrgGroup) iterator.next();
                for (int i = 0; i < implOrgGroupIds.length; i++) {
                    if (group.getIdentifier() == implOrgGroupIds[i]) {
                        selectedfilter.setImplementingagency(group
                                .getOrgGrpName());
                    }
                }
            }
        }

        boolean exit = false;
        ArrayList<SimpleDonor> impdonorslist = new ArrayList<SimpleDonor>();
        if (organizationsSelected != null && !organizationsSelected.isEmpty()) {
            for (Iterator<AmpOrganisation> iterator = organizationsSelected
                    .iterator(); iterator.hasNext();) {
                AmpOrganisation org = (AmpOrganisation) iterator.next();
                if (org != null) {
                    SimpleDonor donor = new SimpleDonor();
                    donor.setDonorname(org.getName());
                    donorslist.add(donor);
                } else {
                    exit = true;
                }
            }
            if (exit) {
                selectedfilter.setImpselecteddonors(impdonorslist);
            }
        }
        
        ArrayList<String> selporjectsst = new ArrayList<String>();
        Collection<AmpCategoryValue> categoryvaluesstatus = null;
        categoryvaluesstatus = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
            for (Iterator<AmpCategoryValue> iterator = categoryvaluesstatus.iterator(); iterator.hasNext();) {
                AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
                if (ArrayUtils.contains(selprojectstatus, ampCategoryValue.getIdentifier())) {
                    selporjectsst.add(ampCategoryValue.getValue());
                }
            }
        selectedfilter.setProjectstatus(selporjectsst);
        
        ArrayList<String> selfinancinginstruments = new ArrayList<String>();
        Collection<AmpCategoryValue> categoryvaluesfinanceintrument = null;
        categoryvaluesfinanceintrument = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        for (Iterator<AmpCategoryValue> iterator = categoryvaluesfinanceintrument.iterator(); iterator.hasNext();) {
            AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
            if (ArrayUtils.contains(selfinancingInstruments, ampCategoryValue.getIdentifier())) {
                selfinancinginstruments.add(ampCategoryValue.getValue());
            }
        }
        selectedfilter.setFinancinginstrument(selfinancinginstruments);
        
        ArrayList<String> seltypesofassiss = new ArrayList<String>();
        Collection<AmpCategoryValue> categoryvaluestypeofassis = null;
        categoryvaluestypeofassis = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        for (Iterator<AmpCategoryValue> iterator = categoryvaluestypeofassis.iterator(); iterator.hasNext();) {
            AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
            if (ArrayUtils.contains(seltypeofassistence, ampCategoryValue.getIdentifier())) {
                seltypesofassiss.add(ampCategoryValue.getValue());
            }
        }
        selectedfilter.setTypeofassistance(seltypesofassiss);
    
        if (selectedBudget != null && CategoryManagerUtil.getAmpCategoryValueFromDb(selectedBudget) != null) {
            selectedfilter.setSelectedBudget(CategoryManagerUtil.getAmpCategoryValueFromDb(selectedBudget).getValue());
        }
        if (selStructureTypes != null) {
            ArrayList<String> selstructurestr = new ArrayList<String>();
            for (Iterator iterator = DbHelper.getAllStructureTypes().iterator(); iterator
                    .hasNext();) {
                AmpStructureType type = (AmpStructureType) iterator.next();
                for (int i = 0; i < selStructureTypes.length; i++) {
                    if (type.getTypeId() == selStructureTypes[i].longValue()) {
                        selstructurestr.add(type.getName());
                    }
                }

            }
            selectedfilter.setStructuretypes(selstructurestr);
        }

        selectedfilter.setSector(new ArrayList<String>());
        if (selSectorIds != null && selSectorIds.length > 0) {
            for (Iterator<AmpSector> iterator = sectors.iterator(); iterator
                    .hasNext();) {
                AmpSector sector = (AmpSector) iterator.next();
                if (ArrayUtils.contains(selSectorIds, sector.getIdentifier())) {
                    selectedfilter.getSector().add(sector.getName());
                }
            }
        }
        
        selectedfilter.setNatplanobj(new ArrayList<String>());
        if (selectedNatPlanObj != null && selectedNatPlanObj.length > 0) {
            AmpTheme parent = programElements.get("selectedNatPlanObj");
            if (ArrayUtils.contains(selectedNatPlanObj, parent.getAmpThemeId())) {
                    selectedfilter.getNatplanobj().add(parent.getName());
                }
            
        }
        selectedfilter.setPrimaryprograms(new ArrayList<String>());
        if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.length > 0) {
            AmpTheme parent = programElements.get("selectedPrimaryPrograms");
            if (ArrayUtils.contains(selectedPrimaryPrograms, parent.getAmpThemeId())) {
                    selectedfilter.getPrimaryprograms().add(parent.getName());
                }
            
        }
        selectedfilter.setSecondaryprograms(new ArrayList<String>());
        if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.length > 0) {
            AmpTheme parent = programElements.get("selectedSecondaryPrograms");
            if (ArrayUtils.contains(selectedSecondaryPrograms, parent.getAmpThemeId())) {
                    selectedfilter.getSecondaryprograms().add(parent.getName());
                }
            
        }       
        if (this.getSelLocationIds()[0] != -1) {
            selectedfilter.setLocationfiltered("True");
        } else {
            selectedfilter.setLocationfiltered("False");
        }
        
        ArrayList<String> selorgtypes = new ArrayList<String>();
        List<AmpOrgType> orgtypes = DbUtil.getAmpOrgTypes();
        for (Iterator iterator = orgtypes.iterator(); iterator.hasNext();) {
            AmpOrgType ampOrgType = (AmpOrgType) iterator.next();
            if (ArrayUtils.contains(selorganizationsTypes, ampOrgType.getIdentifier())){
                selorgtypes.add(ampOrgType.getOrgType());
            }
        }
        selectedfilter.setOrganizationtype(selorgtypes);

        //sel PeaceBuilding marker
        Long selPeaceBuildingMarkerId = getSelectedPeacebuildingMarkerId();
        if (selPeaceBuildingMarkerId == null &&
                getPeacebuildingMarkers() != null &&
                !getPeacebuildingMarkers().isEmpty()) {
            selPeaceBuildingMarkerId = getPeacebuildingMarkers().get(0).getId();
        }
        selectedfilter.setSelectedPeacebuildingMarkerId(selPeaceBuildingMarkerId);

        result.add(selectedfilter);
        return result;
    }

    public Long[] getSelorganizationsTypes() {
        return selorganizationsTypes;
    }

    public void setSelorganizationsTypes(Long[] selorganizationsTypes) {
        this.selorganizationsTypes = selorganizationsTypes;
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

    public void setSectorConfigs(
            List<AmpClassificationConfiguration> sectorConfigs) {
        this.sectorConfigs = sectorConfigs;
    }

    public Long getSelSectorConfigId() {
        return selSectorConfigId;
    }

    public void setSelSectorConfigId(Long selSectorConfigId) {
        this.selSectorConfigId = selSectorConfigId;
    }

    public List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> getRegionWithZones() {
        return regionWithZones;
    }

    public void setRegionWithZones(
            List<EntityRelatedListHelper<AmpCategoryValueLocations, AmpCategoryValueLocations>> regionWithZones) {
        this.regionWithZones = regionWithZones;
    }

    public List<EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>> getOrgGroupWithOrgsList() {
        return orgGroupWithOrgsList;
    }

    public void setOrgGroupWithOrgsList(List<EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>> orgGroupWithOrgsList) {
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

    public List<OrganizationSkeleton> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationSkeleton> organizations) {
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

    public void setSelectedBudget(Long onBudget) {
        this.selectedBudget = onBudget;
    }

    public Long getSelectedBudget() {
        return selectedBudget;
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

    public Long[] getSelfinancingInstruments() {
        return selfinancingInstruments;
    }

    public void setSelfinancingInstruments(Long[] selfinancingInstruments) {
        this.selfinancingInstruments = selfinancingInstruments;
    }

    public List<AmpCategoryValue> getFinancingInstruments() {
        return financingInstruments;
    }

    public void setFinancingInstruments(List<AmpCategoryValue> financingInstruments) {
        this.financingInstruments = financingInstruments;
    }
    
    public Long[] getSeltypeofassistence() {
        return seltypeofassistence;
    }

    public void setSeltypeofassistence(Long[] seltypeofassistence) {
        this.seltypeofassistence = seltypeofassistence;
    }

    public List<AmpCategoryValue> getTypeofassistences() {
        return typeofassistences;
    }

    public void setTypeofassistences(List<AmpCategoryValue> typeofassistences) {
        this.typeofassistences = typeofassistences;
    }
    
    public Long[] getSelprojectstatus() {
        return selprojectstatus;
    }

    public void setSelprojectstatus(Long[] selprojectstatus) {
        this.selprojectstatus = selprojectstatus;
    }

    public List<AmpCategoryValue> getProjectstatus() {
        return projectstatus;
    }

    public void setProjectstatus(List<AmpCategoryValue> projectstatus) {
        this.projectstatus = projectstatus;
    }
    
    /**
     * per-MapFilter cached list of activity Ids which pass the workspace filters. It is very expensive to compute - over 1 second and does not change on logout / login.
     * we cache it here at least per-request
     * TODO: Dolghier Constantin - cache this per-session in the future
     */
    private transient List<Long> filteredActivities;
    
    /**
     * computes the list of activity Id's which should be displayed by a map which uses this filter's workspace setting.<br />
     * <b>DOES NOT TAKE INTO ACCOUNT the MapFilter settings per se</b> 
     * @return
     */
    public List<Long> buildFilteredActivitiesList()
    {
        if (filteredActivities != null)
            return filteredActivities;
                
        try
        {
            String workSpaceQuery;
            if (this.getFromPublicView() != null && this.getFromPublicView() == true) {
                workSpaceQuery = WorkspaceFilter.generateWorkspaceFilterQuery(null);
            } else if(!this.isModeexport()){
                workSpaceQuery = WorkspaceFilter.getWorkspaceFilterQuery(TLSUtils.getRequest().getSession());
            }
            else
                workSpaceQuery = "SELECT amp_activity_id FROM amp_activity";
            
            List<Long> workspaceActivityList = DbHelper.getInActivitiesLong(workSpaceQuery);
            filteredActivities = Collections.unmodifiableList(workspaceActivityList);
            return filteredActivities;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * fills {@link #orgGroupWithOrgsList} corresponding to the database and filter state
     */
    public void buildOrganizationsByOrgGroup()
    {
        Map<Long, java.util.Set<OrganizationSkeleton>> orgsByGroupId = DbUtil.getOrgSkeletonGroupedByGroupId();
        List<OrgGroupSkeleton> orgGroups = new ArrayList<OrgGroupSkeleton>(DbUtil.getAllOrgGroupSkeletons());
        List<EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>> orgGroupsWithOrgsList = new ArrayList<EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>>();
        for(OrgGroupSkeleton orgGroup:orgGroups)
        {
            java.util.Set<OrganizationSkeleton> orgs = orgsByGroupId.get(orgGroup.getAmpOrgGrpId());
            if (orgs != null)
            {
                orgGroupsWithOrgsList.add(new EntityRelatedListHelper<OrgGroupSkeleton, OrganizationSkeleton>(orgGroup, new ArrayList<OrganizationSkeleton>(orgs)));
            }
        }
        this.setOrgGroupWithOrgsList(orgGroupsWithOrgsList);
    }
    
    /**
     * builds a list of locations which should filter the activities.
     * returns "null" if no filtering should be performed
     * @return
     */
    public List<Long> buildFilteredLocationIds()
    {
        if ((getSelLocationIds() == null) || (getSelLocationIds().length == 0))
            return null;
        java.util.Set<Long> selectedIds = new java.util.HashSet<Long>(Arrays.asList(getSelLocationIds()));
        if (selectedIds.contains(-1L))
            return null;
        
        return new ArrayList<Long>(DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(selectedIds, false));
    }
    
    public List<AmpCategoryValue> getPeacebuildingMarkers() {
        return peacebuildingMarkers;
    }

    public void setPeacebuildingMarkers(List<AmpCategoryValue> peacebuildingMarkers) {
        this.peacebuildingMarkers = peacebuildingMarkers;
    }

    public Long getSelectedPeacebuildingMarkerId() {
        return selectedPeacebuildingMarkerId;
    }

    public void setSelectedPeacebuildingMarkerId(Long selectedPeacebuildingMarkerId) {
        this.selectedPeacebuildingMarkerId = selectedPeacebuildingMarkerId;
    }

    public boolean isFilterByPeacebuildingMarker() {
        return filterByPeacebuildingMarker;
    }

    public void setFilterByPeacebuildingMarker(boolean filterByPeacebuildingMarker) {
        this.filterByPeacebuildingMarker = filterByPeacebuildingMarker;
    }
    
    public boolean hasSectorCondition()
    {
        Long[] sectorIds = this.getSelSectorIds();
        return sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
    }

    public Map<String,AmpTheme> getProgramElements() {
        return programElements;
    }

    public void setProgramElements(Map<String,AmpTheme> programElements) {
        this.programElements = programElements;
    }

    public Long[] getSelectedNatPlanObj() {
        return selectedNatPlanObj;
    }

    public void setSelectedNatPlanObj(Long[] selectedNatPlanObj) {
        this.selectedNatPlanObj = selectedNatPlanObj;
    }

    public Long[] getSelectedPrimaryPrograms() {
        return selectedPrimaryPrograms;
    }

    public void setSelectedPrimaryPrograms(Long[] selectedPrimaryPrograms) {
        this.selectedPrimaryPrograms = selectedPrimaryPrograms;
    }

    public Long[] getSelectedSecondaryPrograms() {
        return selectedSecondaryPrograms;
    }

    public void setSelectedSecondaryPrograms(Long[] selectedSecondaryPrograms) {
        this.selectedSecondaryPrograms = selectedSecondaryPrograms;
    }

    public Long getDefaultFiscalCalendarId() {
        return defaultFiscalCalendarId;
    }

    public void setDefaultFiscalCalendarId(Long defaultFiscalCalendarId) {
        this.defaultFiscalCalendarId = defaultFiscalCalendarId;
    }

    public Long getDefaultCurrencyId() {
        return CurrencyUtil.getCurrencyByCode("USD").getAmpCurrencyId();
    }

    public void setDefaultCurrencyId(Long defaultCurrencyId) {
        this.defaultCurrencyId = defaultCurrencyId;
    }

    public List<AmpCategoryValue> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<AmpCategoryValue> budgets) {
        this.budgets = budgets;
    }
}
