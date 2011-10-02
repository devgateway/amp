package org.digijava.module.visualization.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.springframework.beans.BeanWrapperImpl;

public class ShowDashboard extends Action {
	private static Logger logger = Logger.getLogger(ShowDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		String visualizationType = request.getParameter("type") == null ? "donor"
				: (String) request.getParameter("type");
		
		HttpSession session = request.getSession();
	    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		boolean fromPublicView = false;
		if (tm == null) {
			fromPublicView = true;
		}
		if (visualizationType.equals("sector")) {
			prepareSectorDashboard(visualizationForm,fromPublicView);
			return mapping.findForward("sector");
		} else if (visualizationType.equals("donor")) {
			prepareDonorDashboard(visualizationForm,fromPublicView);
			return mapping.findForward("donor");
		} else if (visualizationType.equals("region")) {
			prepareRegionDashboard(visualizationForm,fromPublicView);
			return mapping.findForward("region");
		} else {
			return null;
		}

	}

	private void prepareRegionDashboard(VisualizationForm visualizationForm,boolean fromPublicView) {
		DashboardFilter filter = visualizationForm.getFilter();
		
		//If there's a filter but it's not of the right type, reset it.
		if (filter != null && filter.getDashboardType() != Constants.DashboardType.REGION) {
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
			filter.setDashboardType(Constants.DashboardType.REGION);
		}
		if (filter == null){
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
		}
		filter.setFromPublicView(fromPublicView);
	}

	private void prepareDonorDashboard(VisualizationForm visualizationForm,boolean fromPublicView) {

		// Check if filter is initialized
		// Filter initialization includes options for the filter
		// If not, load information for all
		DashboardFilter filter = visualizationForm.getFilter();
		
		//If there's a filter but it's not of the right type, reset it.
		if (filter != null && filter.getDashboardType() != Constants.DashboardType.DONOR) {
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
			filter.setDashboardType(Constants.DashboardType.DONOR);
		}
		if (filter == null){
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
		}
		filter.setFromPublicView(fromPublicView);

		// Get Summary Information
		//DashboardUtil.getSummaryAndRankInformation(visualizationForm);

		//visualizationForm.getRanksInformation().setFullDonors(DashboardUtil.getRankDonors());
		//visualizationForm.getRanksInformation().setFullProjects(DashboardUtil.getRankActivities());
		//visualizationForm.getRanksInformation().setFullRegions(DashboardUtil.getRankRegions());
		//visualizationForm.getRanksInformation().setFullSectors(DashboardUtil.getRankSectors());
		
		// Get Top Projects
		// Get Top Sectors
		// Get Top Regions
		// Get Sector Working Groups (define where this is going to be set)

	}

	private void initializeFilter(DashboardFilter filter) {
		filter.setDashboardType(Constants.DashboardType.DONOR);

		List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>(DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupsWithOrgsList = new ArrayList<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>>();
		for(AmpOrgGroup orgGroup:orgGroups){
			List<AmpOrganisation> organizations=DbUtil.getOrganisationByGroupId(orgGroup.getAmpOrgGrpId());
			orgGroupsWithOrgsList.add(new EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>(orgGroup,organizations));
		}
		filter.setOrgGroupWithOrgsList(orgGroupsWithOrgsList);
		List<AmpOrganisation> orgs = null;

		if (filter.getOrgGroupId() == null
				|| filter.getOrgGroupId() == -1) {

			filter.setOrgGroupId(-1l);// -1 option denotes
												// "All Groups", which is the
												// default choice.
		}
		if(filter.getOrgGroupId()!=-1){

		orgs = DbUtil.getDonorOrganisationByGroupId(
				filter.getOrgGroupId(), filter.getFromPublicView());
		}
		filter.setOrganizations(orgs);
		try {
		if(filter.getSelSectorConfigId()==null){
				filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
		}
		filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
		filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
		List<AmpSector> sectors = org.digijava.module.visualization.util.DbUtil
					.getParentSectorsFromConfig(filter.getSelSectorConfigId());
		filter.setSectors(sectors);
		for(AmpClassificationConfiguration config: filter.getSectorConfigs()){
			List<AmpSector> currentConfigSectors = org.digijava.module.visualization.util.DbUtil.getParentSectorsFromConfig(config.getId());
			List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorsWithSubSectors = new ArrayList<EntityRelatedListHelper<AmpSector,AmpSector>>();
			for(AmpSector sector:currentConfigSectors){;
				List<AmpSector> sectorList=new ArrayList<AmpSector>(sector.getSectors());
				sectorsWithSubSectors.add(new EntityRelatedListHelper<AmpSector,AmpSector>(sector,sectorList));
			}
			filter.getConfigWithSectorAndSubSectors().add(new EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>(config,sectorsWithSubSectors));
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (filter.getStartYear() == null) {
			Long year = null;
			try {
				year = Long.parseLong(FeaturesUtil
						.getGlobalSettingValue("Current Fiscal Year"));
			} catch (NumberFormatException ex) {
				year = new Long(Calendar.getInstance().get(Calendar.YEAR));
			}
			filter.setStartYear(year-5);
			filter.setStartYearQuickFilter(year-5);
			filter.setStartYearFilter(year-5);
			filter.setEndYear(year);
			filter.setEndYearQuickFilter(year);
			filter.setEndYearFilter(year);
			filter.setYearToCompare(year-1);
		}
		filter.setYears(new ArrayList<BeanWrapperImpl>());
		long yearFrom = Long
				.parseLong(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		long countYear = Long
				.parseLong(FeaturesUtil
						.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getStartYear()) {
			maxYear = filter.getStartYear();
		}
		for (long i = yearFrom; i <= maxYear; i++) {
			filter.getYears().add(new BeanWrapperImpl(new Long(i)));
		}

		Collection calendars = DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
		}
		// if (fromPublicView == false) {
		// if (orgForm.getFiscalCalendarId() == null) {
		// Long fisCalId = tm.getAppSettings().getFisCalId();
		// if (fisCalId == null) {
		// String value = FeaturesUtil
		// .getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		// if (value != null) {
		// fisCalId = Long.parseLong(value);
		// }
		// }
		// orgForm.setFiscalCalendarId(fisCalId);
		// }
		// } else {
		String value = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
		}
		// }
		if (filter.getLargestProjectNumber() == null) {
			filter.setLargestProjectNumber(10);
		}
		if (filter.getDivideThousands() == null) {
			filter.setDivideThousands(false);
		}
		if (filter.getDivideThousandsDecimalPlaces() == null) {
			filter.setDivideThousandsDecimalPlaces(0);
		}
		//Initialize formatting information
		if(filter.getDecimalSeparator() == null || filter.getGroupSeparator() == null ){
			filter.setDecimalSeparator(FormatHelper.getDecimalSymbol());
			filter.setGroupSeparator(FormatHelper.getGroupSymbol());
		}
		
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
						DynLocationManagerUtil.
						getRegionsOfDefCountryHierarchy()));
				List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones = new ArrayList<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>>();
				for(AmpCategoryValueLocations region:filter.getRegions()){
					List<AmpCategoryValueLocations> zones=new ArrayList<AmpCategoryValueLocations>(region.getChildLocations());
					regionWithZones.add(new EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>(region,zones));
				}
				filter.setRegionWithZones(regionWithZones);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long[] regionId = filter.getRegionIds();
		List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

		if (regionId != null && regionId.length!=0 && regionId[0] != -1) {
			AmpCategoryValueLocations region;
			try {
				region = LocationUtil.getAmpCategoryValueLocationById(regionId[0]);
				if (region.getChildLocations() != null) {
					zones.addAll(region.getChildLocations());

				}
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		filter.setZones(zones);
		Collection currency = CurrencyUtil.getAmpCurrency();
        List<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
        filter.setCurrencies(validcurrencies);
        //Only currencies which have exchanges rates
        for (Iterator iter = currency.iterator(); iter.hasNext();) {
            AmpCurrency element = (AmpCurrency) iter.next();
            try {
				if (CurrencyUtil.isRate(element.getCurrencyCode()) == true) {
					filter.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
				}
			} catch (AimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	private void prepareSectorDashboard(VisualizationForm visualizationForm,boolean fromPublicView) {
		DashboardFilter filter = visualizationForm.getFilter();
		
		//If there's a filter but it's not of the right type, reset it.
		if (filter != null && filter.getDashboardType() != Constants.DashboardType.SECTOR) {
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
			filter.setDashboardType(Constants.DashboardType.SECTOR);
		}
		if (filter == null){
			filter = new DashboardFilter();
			visualizationForm.setFilter(filter);
			initializeFilter(filter);
		}
		filter.setFromPublicView(fromPublicView);
	}

}
