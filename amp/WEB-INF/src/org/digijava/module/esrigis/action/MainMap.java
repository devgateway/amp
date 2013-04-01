package org.digijava.module.esrigis.action;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ecs.storage.Array;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.form.MainMapForm;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.visualization.util.Constants;
import org.digijava.module.visualization.util.DashboardUtil;
import org.springframework.beans.BeanWrapperImpl;

public class MainMap extends Action {
	private static Logger logger = Logger.getLogger(MainMap.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataDispatcherForm dataDispatcherForm = (DataDispatcherForm) form;
		MapFilter filter = dataDispatcherForm.getFilter();
		if (request.getParameter("reset") != null
				&& request.getParameter("reset").equals("true")) {
			filter = null;
		}

		if (request.getParameter("action") != null) {
			return displayIcon(mapping, form, request, response);
		}

		AmpMapConfig map = DbHelper.getMapByType(MapConstants.MapType.ARCGIS_API);
		if(map != null && map.getMapUrl() != null && !"".equals(map.getMapUrl()))
			dataDispatcherForm.setApiurl(map.getMapUrl());

		//Load indicator layers
		List<AmpMapConfig> indicators = DbHelper.getMapsBySubType(MapConstants.MapSubType.INDICATOR);
		if(indicators.size() > 0) {
			dataDispatcherForm.setIndicators(indicators);
		}
		
		if (filter == null) {
			filter = new MapFilter();
			if (request.getParameter("public") != null
					&& request.getParameter("public").equalsIgnoreCase("true")) {
				filter.setFromPublicView(true);
			}
			initializeFilter(filter,request);
			dataDispatcherForm.setFilter(filter);
		} else {
			// Check if needed structures are loaded TODO: Check why this is
			// happening.
			if (filter.getStructureTypes() == null) {
				List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
				sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
				filter.setStructureTypes(sts);
			}
			if (request.getParameter("public") != null
					&& request.getParameter("public").equalsIgnoreCase("true")) {
				filter.setFromPublicView(true);
			}

		}
		
		List<AmpCategoryValue> categoryvaluesfinanisnt = null;
		categoryvaluesfinanisnt = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		filter.setFinancingInstruments(categoryvaluesfinanisnt);
		
		List<AmpCategoryValue> categoryvaluestypeofassis = null;
		categoryvaluestypeofassis = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
		filter.setTypeofassistences(categoryvaluestypeofassis);
		
		List<AmpCategoryValue> categoryvaluesprojectstatus = null;
		categoryvaluesprojectstatus = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
		filter.setProjectstatus(categoryvaluesprojectstatus);
		
		if (request.getParameter("exportreport") != null) {
			filter.setModeexport(true);
			AmpARFilter reportfilter = ReportContextData.getFromRequest().getFilter();
			filter.setReportfilterquery(reportfilter.getGeneratedFilterQuery());
			filter.setCurrencyId(reportfilter.getCurrency() == null ? null : reportfilter.getCurrency().getAmpCurrencyId());
			if (reportfilter.getRenderStartYear()!=null && reportfilter.getRenderStartYear()!=0){
				filter.setStartYear(reportfilter.getRenderStartYear().longValue());
			}else{
				Integer defaultStart = getDefaultStartYear(request);
				if (defaultStart!=-1){
					filter.setStartYear(defaultStart.longValue());
				}
			}
			if (reportfilter.getRenderStartYear()!=null && reportfilter.getRenderStartYear()!=0){
				filter.setEndYear(reportfilter.getRenderEndYear().longValue());
			}else{
				Integer defaultEnd = getDefaultEndYear(request);
				if (defaultEnd!=-1){
					filter.setEndYear(defaultEnd.longValue());
				}
			}
				
			Collection<AmpCategoryValueLocations> locs = reportfilter.getLocationSelected();
			if (locs!=null && locs.size() > 0){
				Long[] sellocs= new Long[locs.size()];
				int i=0;
				for (Iterator iterator2 = locs.iterator(); iterator2.hasNext();) {
					AmpCategoryValueLocations loc = (AmpCategoryValueLocations) iterator2.next();
					sellocs[i]=loc.getId();
					i++;
				}
				filter.setSelLocationIds(sellocs);
			}
		} else {
			filter.setModeexport(false);
		}

		if (request.getParameter("popup") != null
				&& request.getParameter("popup").equalsIgnoreCase("true")) {
			return mapping.findForward("popup");

		}

		Collection<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		sts = DbHelper.getAllStructureTypes();
		request.setAttribute("structureTypesList", sts);

		return mapping.findForward("forward");
	}
	
	
	
	private Integer getDefaultStartYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rStart = -1;
		if (tempSettings != null && tempSettings.getReportStartYear() != null && tempSettings.getReportStartYear().intValue() != 0) {
			rStart = tempSettings.getReportStartYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rStart = Integer.parseInt(gvalue);
			}
		}

		return rStart;
	}

	private Integer getDefaultEndYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rEnd = -1;
		if (tempSettings != null && tempSettings.getReportEndYear() != null && tempSettings.getReportEndYear().intValue() != 0) {
			rEnd = tempSettings.getReportEndYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rEnd = Integer.parseInt(gvalue);
			}
		}
		return rEnd;
	}
	
	
	public static AmpApplicationSettings getAppSetting(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
		}
		return tempSettings;
	}

	
	/**
	 * 
	 * @param filter
	 */
	private void initializeFilter(MapFilter filter,HttpServletRequest request) {
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
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

		List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
		filter.setOrganizationsType(orgtypes);
		
		List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>(DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>> orgGroupsWithOrgsList = new ArrayList<EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>>();
		for(AmpOrgGroup orgGroup:orgGroups){
			List<AmpOrganisation> organizations=DbUtil.getOrganisationByGroupId(orgGroup.getAmpOrgGrpId());
			orgGroupsWithOrgsList.add(new EntityRelatedListHelper<AmpOrgGroup,AmpOrganisation>(orgGroup,organizations));
		}
		filter.setOrgGroupWithOrgsList(orgGroupsWithOrgsList);
		
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
						DynLocationManagerUtil.getRegionsOfDefCountryHierarchy()));
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
		
		if (filter.getStartYear() == null) {
			Long year = null;
			try {
				year = Long.parseLong(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENT_SYSTEM_YEAR));
			} catch (NumberFormatException ex) {
				year = new Long(Calendar.getInstance().get(Calendar.YEAR));
			}
			filter.setDefaultStartYear(year - 3);
			filter.setStartYear(year - 3);
			filter.setStartYearFilter(year - 3);
			filter.setEndYear(year);
			filter.setDefaultEndYear(year);
		}
		
		List<AmpCurrency> currency = CurrencyUtil.getActiveAmpCurrencyByName();
        List<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
        filter.setCurrencies(validcurrencies);
        
        //Only currencies which have exchanges rates
        for (AmpCurrency element:currency) {
            try {
				if (CurrencyUtil.isRate(element.getCurrencyCode()) == true) {
					filter.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
		filter.setYears(new TreeMap<Integer, Integer>());
		int yearFrom = Integer
				.parseInt(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		int countYear = Integer
				.parseInt(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getStartYear()) {
			maxYear = filter.getStartYear();
		}
		for (int i = yearFrom; i <= maxYear; i++) {
			filter.getYears().put(i,i);
		}
		
		Collection calendars = DbUtil.getAllFisCalenders();
		if (calendars != null) {
			filter.setFiscalCalendars(new ArrayList(calendars));
		}

		String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (value != null) {
			Long fisCalId = Long.parseLong(value);
			filter.setFiscalCalendarId(fisCalId);
		}
		if (filter.getLargestProjectNumber() == null) {
			filter.setLargestProjectNumber(10);
		}
		if (filter.getDivideThousands() == null) {
			filter.setDivideThousands(false);
		}
		if (filter.getDivideThousandsDecimalPlaces() == null) {
			filter.setDivideThousandsDecimalPlaces(0);
		}
		if (filter.getRegions() == null) {
			try {
				filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
						DynLocationManagerUtil
								.getLocationsOfTypeRegionOfDefCountry()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long[] regionId = filter.getRegionIds();
		List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

		if (regionId != null && regionId.length != 0 && regionId[0] != -1) {
			AmpCategoryValueLocations region;
			try {
				region = LocationUtil
						.getAmpCategoryValueLocationById(regionId[0]);
				if (region.getChildLocations() != null) {
					zones.addAll(region.getChildLocations());

				}
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
		filter.setStructureTypes(sts);
		
		List<AmpCategoryValue> categoryvaluesfinanisnt = null;
		categoryvaluesfinanisnt = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		filter.setFinancingInstruments(categoryvaluesfinanisnt);
		
		List<AmpCategoryValue> categoryvaluestypeofassis = null;
		categoryvaluestypeofassis = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
		filter.setTypeofassistences(categoryvaluestypeofassis);
		
		List<AmpCategoryValue> categoryvaluesprojectstatus = null;
		categoryvaluesprojectstatus = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
		filter.setProjectstatus(categoryvaluesprojectstatus);
	}

	public ActionForward displayIcon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		String index = request.getParameter("id");

		if (index != null) {
			try {
				Long structureTypeId = Long.parseLong(index);
				AmpStructureType structureType = DbHelper
						.getStructureType(structureTypeId);
				ServletOutputStream os = response.getOutputStream();
				if (structureType.getIconFile() != null) {
					response.setContentType(structureType
							.getIconFileContentType());
					os.write(structureType.getIconFile());
					os.flush();
				} else {
					BufferedImage bufferedImage = new BufferedImage(30, 30,
							BufferedImage.TRANSLUCENT);
					ImageIO.write(bufferedImage, "png", os);
					os.flush();
				}
			} catch (NumberFormatException nfe) {
				logger.error("Trying to parse " + index + " to int");
			}
		} else {
			BufferedImage bufferedImage = new BufferedImage(30, 30,
					BufferedImage.TRANSLUCENT);
			ServletOutputStream os = response.getOutputStream();
			ImageIO.write(bufferedImage, "png", os);
			os.flush();
		}
		return null;
	}
}
