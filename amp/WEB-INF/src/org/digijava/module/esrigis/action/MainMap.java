package org.digijava.module.esrigis.action;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.util.Constants;
import org.springframework.beans.BeanWrapperImpl;

public class MainMap extends Action{

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		DataDispatcherForm dataDispatcherForm = (DataDispatcherForm) form;
		MapFilter filter = dataDispatcherForm.getFilter();
		if(request.getParameter("reset") != null && request.getParameter("reset").equals("true")){
			filter = null;
		}
		
		
		if (filter == null){
			filter = new MapFilter();
			initializeFilter(filter);
			dataDispatcherForm.setFilter(filter);
		}else{
			//Check if needed structures are loaded TODO: Check why this is happening.
			if(filter.getStructureTypes() == null){
				List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
				sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
				filter.setStructureTypes(sts);
			}
			
		}
		
		if (request.getParameter("exportreport") != null){
			filter.setModeexport(true);
			AmpARFilter reportfilter = (AmpARFilter) request.getSession().getAttribute("ReportsFilter");
			filter.setReportfilterquery(reportfilter.getGeneratedFilterQuery());
		}else{
			filter.setModeexport(false);
		}
		
		if(request.getParameter("popup") != null && request.getParameter("popup").equalsIgnoreCase("true")){
			return mapping.findForward("popup");
			
		}
		
		return mapping.findForward("forward");
	}
	
	
	/**
	 * 
	 * @param filter
	 */
	private void initializeFilter(MapFilter filter) {
		List<AmpOrgGroup> orgGroups = new ArrayList(DbUtil.getAllOrgGroups());
		filter.setOrgGroups(orgGroups);
		List<AmpOrganisation> orgs = null;

		if (filter.getOrgGroupId() == null
				|| filter.getOrgGroupId() == -1) {

			filter.setOrgGroupId(-1l);// -1 option denotes "All Groups", which is the default choice.
		}

		orgs = DbUtil.getDonorOrganisationByGroupId(
				filter.getOrgGroupId(), false); // TODO: Determine how this will work in the public view
		filter.setOrganizations(orgs);
		
		List<AmpSector> sectors = new ArrayList(org.digijava.module.visualization.util.DbUtil.getAllSectors());
		filter.setSectors(sectors);

		if (filter.getYear() == null) {
			Long year = null;
			try {
				year = Long.parseLong(FeaturesUtil
						.getGlobalSettingValue("Current Fiscal Year"));
			} catch (NumberFormatException ex) {
				year = new Long(Calendar.getInstance().get(Calendar.YEAR));
			}
			filter.setYear(year);
		}
		filter.setYears(new ArrayList<BeanWrapperImpl>());
		long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
		long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		long maxYear = yearFrom + countYear;
		if (maxYear < filter.getYear()) {
			maxYear = filter.getYear();
		}
		for (long i = yearFrom; i <= maxYear; i++) {
			filter.getYears().add(new BeanWrapperImpl(new Long(i)));
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
        
        List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
		filter.setOrganizationsType(orgtypes);        

		List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
		filter.setStructureTypes(sts);

	}
	
	
}
