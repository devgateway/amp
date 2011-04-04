package org.digijava.module.visualization.helper;

import java.util.Collection;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.springframework.beans.BeanWrapperImpl;

public class DashboardFilter {
    
    private static final long serialVersionUID = 1L;
    private int dashboardType;
    private Long year;
    private Long currencyId;
    private Boolean workspaceOnly;
    private TeamMember teamMember;
    private List<AmpCurrency>currencies;
    private List<AmpOrganisation>organizations;

    private List<AmpOrganisation>organizationsSelected;
    private Long[] orgIds;

    private List<AmpSector>sectors;
    private List<AmpSector>sectorsSelected;
    private Long[] selSectorIds;
    private Long[] sectorIds;
    private Long[] subSectorIds;
    private Long sectorId; //used to fill subsectors list

    private List<AmpCategoryValueLocations> locationsSelected;
    private Long[] selLocationIds;

    private Collection<BeanWrapperImpl> years;
    private int transactionType;
    private List<AmpOrgGroup> orgGroups;
    private Long organizationGroupId;
    private List<AmpFiscalCalendar> fiscalCalendars;
    private Long fiscalCalendarId;
    private Integer largestProjectNumber;
    private Boolean divideThousands;
    private Integer divideThousandsDecimalPlaces;
    private Long[] selRegionIds;
    private Long[] selZoneIds;
    private List<AmpCategoryValueLocations> regions;
    private List<AmpCategoryValueLocations> zones;

    private Long[] orgGroupIds;
    private Long orgGroupId;
    private Long orgId;
    private Long subSectorId;
    private Long[] regionIds;
    private Long regionId;
    private Long[] zoneIds;
    private Long zoneId;

    private int yearsInRange;
    private Boolean pledgeVisible;
    private Boolean expendituresVisible;
    private Boolean fromPublicView;
    private Boolean showOnlyApprovedActivities;

    private Long activityId;
    
    public DashboardFilter getCopyFilterForFunding(){
    	DashboardFilter newFilter = new DashboardFilter();
    	
    	newFilter.setCurrencyId(this.getCurrencyId());
    	newFilter.setOrgIds(this.getOrgIds());
    	newFilter.setOrganizationGroupId(this.getOrganizationGroupId());
    	newFilter.setTeamMember(this.getTeamMember());
    	newFilter.setSelLocationIds(this.getSelLocationIds());
    	newFilter.setSelSectorIds(this.getSelSectorIds());
    	newFilter.setActivityId(this.getActivityId());
    	newFilter.setShowOnlyApprovedActivities(this.getShowOnlyApprovedActivities());
    	newFilter.setFromPublicView(this.getFromPublicView());
    	return newFilter;
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
    	if(yearsInRange == 0) yearsInRange = 5;
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

    public Long[] getSelRegionIds() {
        return selRegionIds;
    }

    public void setSelRegionIds(Long[] selRegionIds) {
        this.selRegionIds = selRegionIds;
    }

     public Long[] getSelZoneIds() {
        return selZoneIds;
    }

    public void setSelZoneIds(Long[] selZoneIds) {
        this.selZoneIds = selZoneIds;
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

    public Long getOrganizationGroupId() {
        return organizationGroupId;
    }

    public void setOrganizationGroupId(Long orgGroupId) {
        this.organizationGroupId = orgGroupId;
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

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Collection<BeanWrapperImpl> getYears() {
        return years;
    }

    public void setYears(Collection<BeanWrapperImpl> years) {
        this.years = years;
    }
    
     public Boolean getWorkspaceOnly() {
        return workspaceOnly;
    }

    public void setWorkspaceOnly(Boolean workspaceOnly) {
        this.workspaceOnly = workspaceOnly;
    }

    public String getOrgGroupName() {
        AmpOrgGroup group= DbUtil.getAmpOrgGroup(organizationGroupId);
        String name=null;
        if(group!=null){
            name=group.getOrgGrpName();
        }
        return name;
    }
    /*
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

	public List<AmpOrganisation> getOrganizationsSelected() {
		return organizationsSelected;
	}

	public void setOrganizationsSelected(List<AmpOrganisation> organizationsSelected) {
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

	public boolean isPledgeVisible() {
		// TODO CHANGE THIS!
		return true;
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

	public boolean isExpendituresVisible() {
		// TODO CHANGE THIS!
		return true;
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

	public void setOrgGroupIds(Long[] orgGroupIds) {
		this.orgGroupIds = orgGroupIds;
	}

	public Long[] getOrgGroupIds() {
		return orgGroupIds;
	}

	public void setOrgGroupId(Long orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	public Long getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setSubSectorId(Long subSectorId) {
		this.subSectorId = subSectorId;
	}

	public Long getSubSectorId() {
		return subSectorId;
	}

	public void setRegionIds(Long[] regionIds) {
		this.regionIds = regionIds;
	}

	public Long[] getRegionIds() {
		return regionIds;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setZoneIds(Long[] zoneIds) {
		this.zoneIds = zoneIds;
	}

	public Long[] getZoneIds() {
		return zoneIds;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getZoneId() {
		return zoneId;
	}

}
