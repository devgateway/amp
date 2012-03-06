package org.digijava.module.orgProfile.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

/**
 *
 * @author medea
 */
public class FilterHelper implements Serializable {

    private Long currId;
    private Long year;
    private int transactionType;
    private TeamMember teamMember;
    private Long orgGroupId;
    private Long fiscalCalendarId;
    private Long[] orgIds;
    private Integer largestProjectNumb;
    private Boolean divideThousands;
    private Integer divideThousandsDecimalPlaces;
    private Long regionId;
    private Long[] zoneIds;
    private Collection<Long> locationIds;
    private Date startDate;
    private Date endDate;
    private int yearsInRange;
    private boolean expendituresVisible;
    private boolean pledgeVisible;
    private boolean fromPublicView;
    private boolean showOnlyApprovedActivities;
    private Boolean showComplexLabel;

    public boolean isExpendituresVisible() {
        return expendituresVisible;
    }

    public void setExpendituresVisible(boolean expendituresVisible) {
        this.expendituresVisible = expendituresVisible;
    }

    public boolean isPledgeVisible() {
        return pledgeVisible;
    }

    public void setPledgeVisible(boolean pledgeVisible) {
        this.pledgeVisible = pledgeVisible;
    }

    public int getYearsInRange() {
        return yearsInRange;
    }

    public void setYearsInRange(int yearsInRange) {
        this.yearsInRange = yearsInRange;
    }
  
     public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Collection<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(Collection<Long> locationIds) {
        this.locationIds = locationIds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long[] getZoneIds() {
        return zoneIds;
    }

    public void setZoneIds(Long[] zoneIds) {
        this.zoneIds = zoneIds;
    }


    public Integer getLargestProjectNumb() {
        return largestProjectNumb;
    }

    public void setLargestProjectNumb(Integer largestProjectNumb) {
        this.largestProjectNumb = largestProjectNumb;
    }

    public Long[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Long[] orgIds) {
        this.orgIds = orgIds;
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

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
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

    public FilterHelper(OrgProfileFilterForm form) throws DgException {
        this.currId = form.getCurrencyId();
        this.year = form.getYear();
        this.transactionType = form.getTransactionType();
        this.orgGroupId = form.getOrgGroupId();
        this.fiscalCalendarId = form.getFiscalCalendarId();
        this.orgIds = form.getOrgIds();
        this.largestProjectNumb=form.getLargestProjectNumb();
        this.regionId=form.getSelRegionId();
        this.zoneIds=form.getSelZoneIds();
        this.yearsInRange=form.getYearsInRange();
        this.divideThousands=form.getDivideThousands();
        this.divideThousandsDecimalPlaces=form.getDivideThousandsDecimalPlaces();
        this.pledgeVisible=form.getPledgeVisible();
        this.expendituresVisible=form.getExpendituresVisible();
        this.fromPublicView=form.getFromPublicView();
        this.showOnlyApprovedActivities=form.getShowOnlyApprovedActivities();
        this.showComplexLabel=form.getShowComplexLabel();
        initDerivedProperties();
    }
     public FilterHelper(FilterHelper helper) throws DgException {
        this.currId = helper.getCurrId();
        this.year = helper.getYear();
        this.transactionType = helper.getTransactionType();
        this.orgGroupId = helper.getOrgGroupId();
        this.fiscalCalendarId = helper.getFiscalCalendarId();
        this.orgIds = helper.getOrgIds();
        this.largestProjectNumb=helper.getLargestProjectNumb();
        this.regionId=helper.getRegionId();
        this.zoneIds=helper.getZoneIds();
        this.teamMember=helper.getTeamMember();
        this.yearsInRange=helper.getYearsInRange();
        this.divideThousands=helper.getDivideThousands();
        this.divideThousandsDecimalPlaces=helper.getDivideThousandsDecimalPlaces();
        this.startDate=helper.getStartDate();
        this.endDate=helper.getEndDate();
        this.locationIds=helper.getLocationIds();
        this.fromPublicView=helper.getFromPublicView();
        this.showOnlyApprovedActivities=helper.getShowOnlyApprovedActivities();
        this.showComplexLabel=helper.getShowComplexLabel();
    }

    public FilterHelper(Long orgGroupId, Long year, Long fiscalCalendarId) {
        this.year = year;
        this.orgGroupId = orgGroupId;
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public FilterHelper(OrgProfileFilterForm form, TeamMember tm) throws DgException {
        this(form);
        this.teamMember = tm;
    }

    public Long getCurrId() {
        return currId;
    }

    public void setCurrId(Long currId) {
        this.currId = currId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public AmpOrganisation getOrganization() {
        AmpOrganisation org = null;
        //view entire group...
        if (orgIds != null) {
            if (orgIds.length == 1) {
                org = DbUtil.getOrganisation(orgIds[0]);
                return org;
            }
        }
        //view particular organization...
        return org;
    }


    public AmpOrgGroup getOrgGroup() {
        AmpOrgGroup orgGroup = DbUtil.getAmpOrgGroup(orgGroupId);
        return orgGroup;
    }

    public String getCurrName() {
        AmpCurrency curr = CurrencyUtil.getAmpcurrency(this.currId);
        String currName = curr.getCurrencyName();
        return currName;

    }
    public String getCurrCode() {
        AmpCurrency curr = CurrencyUtil.getAmpcurrency(this.currId);
        String currCode = curr.getCurrencyCode();
        return currCode;

    }
    private void initDerivedProperties() throws DgException {
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        int fiscalYear = year.intValue();
        this.startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, fiscalYear);
        this.endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, fiscalYear);
        this.locationIds = new ArrayList<Long>();
        if (zoneIds != null && zoneIds.length > 0 && zoneIds[0] != -1) {
            List<Long> zonesAsList =new ArrayList<Long>();
            zonesAsList.addAll(Arrays.asList(zoneIds));
            LocationUtil.populateWithDescendants(zonesAsList);
            this.locationIds.addAll(zonesAsList);
        } else {
            if (regionId != null && regionId != -1) {
                List<Long> regionAsList = new ArrayList<Long>();
                regionAsList.add(regionId);
                LocationUtil.populateWithDescendants(regionAsList);
                this.locationIds.addAll(regionAsList);
            }
        }
    }

	public boolean getFromPublicView() {
		return fromPublicView;
	}

	public void setFromPublicView(boolean fromPublicView) {
		this.fromPublicView = fromPublicView;
	}

	public boolean getShowOnlyApprovedActivities() {
		return showOnlyApprovedActivities;
	}

	public void setShowOnlyApprovedActivities(boolean showOnlyApprovedActivities) {
		this.showOnlyApprovedActivities = showOnlyApprovedActivities;
	}

	public Boolean getShowComplexLabel() {
		return this.showComplexLabel;
	}
	public void setShowComplexLabel(Boolean showComplexLabel) {
		this.showComplexLabel = showComplexLabel;
	}
}
