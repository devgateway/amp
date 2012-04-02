package org.digijava.module.visualization.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.visualization.util.DbUtil;

public class ShowProjectsList extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		DashboardFilter filter = visualizationForm.getFilter();
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String startYear = request.getParameter("startYear");
		String endYear = request.getParameter("endYear");
		
		int startYearInt = 0;
		if (startYear.contains("-")) {
			startYearInt = Integer.parseInt(startYear.substring(startYear.lastIndexOf("-")+1,startYear.lastIndexOf("-")+3))+2000-1;
		} else {
			startYearInt = Integer.parseInt(startYear);
		}

		int endYearInt = 0;
		if (endYear == null || endYear.equals("null")){
			endYearInt = startYearInt;
		} else {
			if (endYear.contains("-")) {
				endYearInt = Integer.parseInt(endYear.substring(endYear.lastIndexOf("-")+1,endYear.lastIndexOf("-")+3))+2000-2;
			} else {
				endYearInt = Integer.parseInt(endYear);
			}
		}
		Long[] ids = null;
		//AidPredictability contains text instead of db ids.
		if (!type.equals("AidPredictability")) {
			if (id==null || id.length()==0) {
				return null;
			} else if (id.contains("-")){
				String[] strArr = id.split("-");
				ids = new Long[strArr.length];
				for (int i = 0; i < strArr.length; i++) {
					ids[i] = Long.parseLong(strArr[i]);
				}
			} else if (id.equalsIgnoreCase("Actual") || id.equalsIgnoreCase("Planned")){
				ids = null;
			}
			else
			{
				ids = new Long[1];
				ids[0] = Long.parseLong(id);
			}
		}
		
        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();
	    JSONObject child = new JSONObject();
	    
	    Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (startYearInt!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYearInt);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYearInt);
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
    	}
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        
        DashboardFilter newFilter = filter.getCopyFilterForFunding();
        List<AmpActivityVersion> activities = null;
        Map<String, Map<AmpActivityVersion, BigDecimal>> itemProjectsList = new HashMap<String, Map<AmpActivityVersion, BigDecimal>>();
        if (type.equals("RegionProfile")){
	    	//Long[] ids = {Long.parseLong(id)};
        	//newFilter.setSelLocationIds(ids);
			//activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				String itemName = LocationUtil.getAmpLocationByCVLocation(long1).getLocation().getName();
				Long[] id1 = {long1};
				newFilter.setSelLocationIds(id1);
				activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
	    }
		if (type.equals("SectorProfile")){
	    	//Long[] ids = {Long.parseLong(id)};
	    	//newFilter.setSelSectorIds(ids);
			//activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				String itemName = SectorUtil.getAmpSector(long1).getName();
				Long[] id1 = {long1};
				newFilter.setSelSectorIds(id1);
				activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("DonorProfile")){
	    	//Long[] ids = {Long.parseLong(id)};
			//newFilter.setOrgIds(ids);
			//activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				String itemName = DbUtil.getOrganisation(long1).getName();
				Long[] id1 = {long1};
				newFilter.setOrgIds(id1);
				activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("FundingChart")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put("", getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidPredictability")){
    		HardCodedCategoryValue adjustmentType;
    		
    		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()))
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
    		else
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
    			
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType);
            itemProjectsList.put("", getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidType")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put("", getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("FinancingInstrument")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put("", getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		/*if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new HashMap<AmpActivityVersion, BigDecimal>();
	        BigDecimal totalSum = BigDecimal.ZERO;
	        while(it.hasNext()){
	        	AmpActivityVersion act = it.next();
				newFilter.setActivityId(act.getAmpActivityId());
	        	DecimalWraper fundingCal = null;
	        	if (type.equals("FundingChart")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, Integer.parseInt(id), Constants.ACTUAL);
	        	} else if (type.equals("AidPredictability")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Integer.parseInt(id));
	        	} else {
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	        	}
	        	BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        	totalSum = totalSum.add(total);
	        	itemProjectsList.put(act, total);
			}
	        visualizationForm.setItemProjectsList(itemProjectsList);
		}*/
		return mapping.findForward("forward");

	}
	
	private Map<AmpActivityVersion, BigDecimal> getActivitiesValues (List<AmpActivityVersion> activities, DashboardFilter filter, String type, String id, int startYearInt, int endYearInt)
	throws java.lang.Exception {
		 Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (startYearInt!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYearInt);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYearInt);
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
    	}
        DashboardFilter newFilter = filter.getCopyFilterForFunding();
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
	    if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new HashMap<AmpActivityVersion, BigDecimal>();
	        BigDecimal totalSum = BigDecimal.ZERO;
	        while(it.hasNext()){
	        	AmpActivityVersion act = it.next();
	        	newFilter.setActivityId(act.getAmpActivityId());
	        	DecimalWraper fundingCal = null;
	        	if (type.equals("FundingChart")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("AidPredictability")){
	        		HardCodedCategoryValue adjustmentType;
	        		
	        		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()))
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
	        		else
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), adjustmentType);
	        	} else if (type.equals("AidType")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, Long.parseLong(id), null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("FinancingInstrument")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, Long.parseLong(id), newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("SectorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelSectorIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelSectorIds(null);
	        	} else if (type.equals("RegionProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelLocationIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelLocationIds(null);
	        	} else if (type.equals("DonorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setOrgIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setOrgIds(null);
	        	} else {
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	}
	        	BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(newFilter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        	totalSum = totalSum.add(total);
	        	itemProjectsList.put(act, total);
	        	newFilter.setActivityId(null);
			}
	        return itemProjectsList;
		}
		return null;
	}
}
