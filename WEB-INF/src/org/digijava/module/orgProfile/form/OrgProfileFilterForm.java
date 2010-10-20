package org.digijava.module.orgProfile.form;

import org.digijava.module.aim.helper.Constants;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 *
 * @author medea
 */
public class OrgProfileFilterForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    private Long year;
    private Long currencyId;
    private Boolean workspaceOnly;
    private List<AmpCurrency>currencies;
    private List<AmpOrganisation>organizations;
    private Collection<BeanWrapperImpl> years;
    private int transactionType;
    private List<AmpOrgGroup> orgGroups;
    private Long orgGroupId;
    private List<AmpFiscalCalendar> fiscalCalendars;
    private Long fiscalCalendarId;
    private Long[] orgIds;
    private Integer largestProjectNumb;
    private Boolean divideThousands;
    private Integer divideThousandsDecimalPlaces;
    private Long selRegionId;
    private Long selZoneIds[];
    private List<AmpCategoryValueLocations> regions;
    private List<AmpCategoryValueLocations> zones;
    private int yearsInRange;
    private Boolean pledgeVisible;
    private Boolean expendituresVisible;
    private Boolean fromPublicView;
    private Boolean showOnlyApprovedActivities;

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

    public Long getSelRegionId() {
        return selRegionId;
    }

    public void setSelRegionId(Long selRegionId) {
        this.selRegionId = selRegionId;
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

    public Integer getLargestProjectNumb() {
        return largestProjectNumb;
    }

    public void setLargestProjectNumb(Integer largestProjectNumb) {
        this.largestProjectNumb = largestProjectNumb;
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

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String reset = request.getParameter("reset");
        ServletContext ampContext = getServlet().getServletContext();
        if (reset == null || reset.equals("true")) {
            super.reset(mapping, request);
            setWorkspaceOnly(false);
            setOrgIds(null);
            setSelZoneIds(null);
            setSelRegionId(null);
            setPledgeVisible(false);
            setDivideThousands(false);
            setExpendituresVisible(false);
            setCurrencyId(null);
            setOrgGroupId(null);
            setFiscalCalendarId(null);
            setYear(null);
            setOrgIds(null);
            setTransactionType(Constants.COMMITMENT);
            setLargestProjectNumb(10);
            setSelRegionId(null);
            setSelZoneIds(null);
            setYearsInRange(3);
            setFromPublicView(false);
            setShowOnlyApprovedActivities(false);
        }
        if(reset!=null&&reset.equals("true")){
            setExpendituresVisible(FeaturesUtil.isVisibleFeature("Expenditures", ampContext));
            setPledgeVisible(FeaturesUtil.isVisibleModule("Pledges", ampContext));
        }

    }

    public String getOrgGroupName() {
        AmpOrgGroup group= DbUtil.getAmpOrgGroup(orgGroupId);
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
            if (selRegionId != null && selRegionId != -1) {
                AmpCategoryValueLocations location = LocationUtil.getAmpCategoryValueLocationById(selRegionId);
                name += location.getName();
            }
        }
        return name;

    }
      public String getCurrencyCode() {
        String name = "USD";
        if (currencyId != null && currencyId != -1) {
            AmpCurrency curr = CurrencyUtil.getAmpcurrency(currencyId);
            name = curr.getCurrencyCode();
        }
        return name;

    }
     public Long[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Long[] orgIds) {
        this.orgIds = orgIds;
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
}