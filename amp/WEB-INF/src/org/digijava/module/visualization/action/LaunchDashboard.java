package org.digijava.module.visualization.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
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
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.aim.util.DbUtil;
import org.springframework.beans.BeanWrapperImpl;


public class LaunchDashboard extends Action {
	private static Logger logger = Logger.getLogger(LaunchDashboard.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm dForm = (VisualizationForm) form;
		AmpDashboard dashboard = dForm.getDashboard();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        dForm.getFilter().setDashboardType(dashboard.getBaseType());
        switch (dashboard.getBaseType()) {
		case 0:
			dForm.setDashboardTitle(dashboard.getName());
			dForm.setDashboardSubTitle("");
			break;

		case 1:
			String orgGroupIds = request.getParameter("orgGroupIds") != null ? (String) request.getParameter("orgGroupIds") : null;
			if (orgGroupIds!=null && orgGroupIds.length()>0 && !orgGroupIds.equals("-1")){
				String grpId = orgGroupIds.contains(",") ? orgGroupIds.split(",")[0] : orgGroupIds;
				AmpOrgGroup grp = org.digijava.module.visualization.util.DbUtil.getOrgGroup(Long.valueOf(grpId));
				dForm.setDashboardTitle(grp.getOrgGrpName());
			} else {
				try {
		        	dForm.setDashboardTitle(TranslatorWorker.translateText("All Organization Groups", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardTitle("All Organization Groups");
				}
			}
			String orgIds = request.getParameter("orgIds") != null ? (String) request.getParameter("orgIds") : null;
			if (orgIds!=null && orgIds.length()>0){
				if (!orgIds.contains(",")){
					String id = orgIds.split(",")[0];
					AmpOrganisation orga = org.digijava.module.visualization.util.DbUtil.getOrganisation(Long.valueOf(id));
					dForm.setDashboardSubTitle(orga.getName());
				} else {
					try {
			        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple Organizations", locale, siteId));
					} catch (WorkerException e) {
						dForm.setDashboardSubTitle("Multiple Organizations");
					}
				}
			} else {
				try {
		        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("All Organizations", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardSubTitle("All Organizations");
				}
			}
			break;

		case 2:
			String regionIds = request.getParameter("regionIds") != null ? (String) request.getParameter("regionIds") : null;
			if (regionIds!=null && regionIds.length()>0 && !regionIds.equals("-1")){
				String regId = regionIds.contains(",") ? regionIds.split(",")[0] : regionIds;
				AmpCategoryValueLocations reg = org.digijava.module.visualization.util.DbUtil.getLocationById(Long.valueOf(regId));
				dForm.setDashboardTitle(reg.getName());
			} else {
				try {
		        	dForm.setDashboardTitle(TranslatorWorker.translateText("All Regions", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardTitle("All Regions");
				}
			}
			String zoneIds = request.getParameter("zoneIds") != null ? (String) request.getParameter("zoneIds") : null;
			if (zoneIds!=null && zoneIds.length()>0){
				if (!zoneIds.contains(",")){
					String id = zoneIds.split(",")[0];
					AmpCategoryValueLocations zone = org.digijava.module.visualization.util.DbUtil.getLocationById(Long.valueOf(id));
					dForm.setDashboardSubTitle(zone.getName());
				} else {
					try {
			        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple zones", locale, siteId));
					} catch (WorkerException e) {
						dForm.setDashboardSubTitle("Multiple zones");
					}
				}
			} else {
				try {
		        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("All zones", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardSubTitle("All zones");
				}
			}
			break;

		case 3:
			String sectorIds = request.getParameter("sectorIds") != null ? (String) request.getParameter("sectorIds") : null;
			if (sectorIds!=null && sectorIds.length()>0 && !sectorIds.equals("-1")){
				String secId = sectorIds.contains(",") ? sectorIds.split(",")[0] : sectorIds;
				AmpSector sec = SectorUtil.getAmpSector(Long.valueOf(secId));
				dForm.setDashboardTitle(sec.getName());
			} else {
				try {
		        	dForm.setDashboardTitle(TranslatorWorker.translateText("All Sectors", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardTitle("All Sectors");
				}
			}
			String subSectorIds = request.getParameter("subSectorIds") != null ? (String) request.getParameter("subSectorIds") : null;
			if (subSectorIds!=null && subSectorIds.length()>0){
				if (!subSectorIds.contains(",")){
					String id = subSectorIds.split(",")[0];
					AmpSector sec = SectorUtil.getAmpSector(Long.valueOf(id));
					dForm.setDashboardSubTitle(sec.getName());
				} else {
					try {
			        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("Multiple sub sectors", locale, siteId));
					} catch (WorkerException e) {
						dForm.setDashboardSubTitle("Multiple sub sectors");
					}
				}
			} else {
				try {
		        	dForm.setDashboardSubTitle(TranslatorWorker.translateText("All sub sectors", locale, siteId));
				} catch (WorkerException e) {
					dForm.setDashboardSubTitle("All sub sectors");
				}
			}
			break;

		default:
			break;
		}
		String graphsList = request.getParameter("graphs") != null ? (String) request.getParameter("graphs") : null;
		List<AmpGraph> list = new ArrayList<AmpGraph>();
		if(graphsList!=null){
			String[] graphsListSplit = graphsList.split(",");
			for (int i = 0; i < graphsListSplit.length; i++) {
				list.add(org.digijava.module.visualization.util.DbUtil.getDashboardGraphById(Long.valueOf(graphsListSplit[i])).getGraph());
			}
			dForm.setGraphList(list);
		}
		
		return mapping.findForward("forward");

	}

	private void initializeFilter(DashboardFilter filter, HttpServletRequest request) {
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
			filter.setDefaultStartYear(year-3);
			filter.setStartYear(year-3);
			filter.setStartYearQuickFilter(year-3);
			filter.setStartYearFilter(year-3);
			filter.setEndYear(year);
			filter.setDefaultEndYear(year);
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
		if (filter.getShowAmountsInThousands() == null) {
			filter.setShowAmountsInThousands(false);
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
		Collection currency = CurrencyUtil.getActiveAmpCurrencyByName();
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
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute("currentMember");
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
			if (tempSettings!=null && tempSettings.getCurrency()!=null){
				filter.setCurrencyId(tempSettings.getCurrency().getAmpCurrencyId());
				filter.setCurrencyIdQuickFilter(tempSettings.getCurrency().getAmpCurrencyId());
			}
		}
	}

}
