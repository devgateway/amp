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
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
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
		String year = request.getParameter("year");

		if (id==null || id.length()==0) {
			return null;
		}
        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();
	    JSONObject child = new JSONObject();
	    
	    Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (year!=null && !year.equals("0") && year.length()!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, Integer.parseInt(year));
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, Integer.parseInt(year));
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange()-1);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
    	}
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        
        DashboardFilter newFilter = filter.getCopyFilterForFunding();
        List<AmpActivityVersion> activities = null;
        
        if (type.equals("RegionProfile")){
	    	Long[] ids = {Long.parseLong(id)};
			newFilter.setSelLocationIds(ids);
			activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	    }
		if (type.equals("SectorProfile")){
	    	Long[] ids = {Long.parseLong(id)};
			newFilter.setSelSectorIds(ids);
			activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
		}
		if (type.equals("DonorProfile")){
	    	Long[] ids = {Long.parseLong(id)};
			newFilter.setOrgIds(ids);
			activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
		}
		if (type.equals("FundingChart")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, Integer.parseInt(id), Constants.ACTUAL);
		}
		if (type.equals("AidPredictability")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), Integer.parseInt(id));
		}
		if (type.equals("AidType")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), Constants.ACTUAL);
		}
		if (type.equals("FinancingInstrument")){
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), Constants.ACTUAL);
		}
		if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new HashMap<AmpActivityVersion, BigDecimal>();
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
	        	itemProjectsList.put(act, total);
			}
	        visualizationForm.setItemProjectsList(itemProjectsList);
		}
		return mapping.findForward("forward");

	}
}
