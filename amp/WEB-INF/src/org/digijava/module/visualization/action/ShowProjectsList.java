package org.digijava.module.visualization.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
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
        String itemName = "";
		
		int startYearInt = 0;
		if (startYear.contains("-")) {
			String startYearStr = startYear.substring(startYear.lastIndexOf("-")+1,startYear.lastIndexOf("-")+3);
			
			if(type.equals("AidPredictabilityQuarter") && startYearStr.trim().startsWith("Q")){
				//Fix for AMP-14389, line area graph is not sending the same data format as the bar graph
				//Example: Line graph -> startYear = FY 11-12 - Q1 
				//                       id = Actual
				//         Bar chart -> startYear = FY 11-12 
				//                      id = Actual-1

				//Extracting the quarter and adding it to the id, used forward
				String quarterStr = startYear.substring(startYear.lastIndexOf("-")+1,startYear.lastIndexOf("-")+4);
				id = id +"-"+ quarterStr.trim().substring(1,2);
				//Calculating startYear after removing the quarter
				startYearStr = startYear.substring(0, startYear.lastIndexOf("-"));
				startYearStr = startYearStr.substring(startYearStr.lastIndexOf("-")+1,startYearStr.lastIndexOf("-")+3);
			}
			startYearInt = Integer.parseInt(startYearStr)+2000-1;
		} else {
			startYearInt = Integer.parseInt(startYear);
		}

		int endYearInt = 0;
		if (endYear == null || endYear.equals("null") || endYear.equals("undefined")){
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
		if (!type.contains("AidPredictability")) {
			if (id==null || id.length()==0) {
				return null;
			} else if (id.contains("_")){
				String[] strArr = id.split("_");
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
        
        HashMap<Long, AmpActivityVersion> activities = null;
        Map<String, Map<AmpActivityVersion, BigDecimal>> itemProjectsList = new TreeMap<String, Map<AmpActivityVersion, BigDecimal>>();
        if (type.equals("RegionProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				if(!long1.equals(0l)){
					itemName = LocationUtil.getAmpLocationByCVLocation(long1).getLocation().getName();
				} else {
					itemName = TranslatorWorker.translateText("Unallocated");
				}
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelLocationIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty())
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
	    }
		if (type.equals("SectorProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = SectorUtil.getAmpSector(long1).getName();
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelSectorIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("OrganizationProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getOrganisation(long1).getName();
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelOrgIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("BeneficiaryAgencyProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getOrganisation(long1).getName();
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setAgencyType(org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY);
				newFilter.setSelOrgIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("ExecutingAgencyProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getOrganisation(long1).getName();
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setAgencyType(org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY);
				newFilter.setSelOrgIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
				
		if (type.equals("NPOProfile")||type.equals("ProgramProfile")){
	    	for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getProgramById(long1).getName();
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelProgramIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("BudgetBreakdown")){
	    	for (int i = 0; i < ids.length; i++) {
	    		Long long1 = ids[i];
				if(!long1.equals(-1l)){
					AmpCategoryValue categoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(long1);
					itemName = categoryValue.getValue();
				} else {
					itemName = TranslatorWorker.translateText("Unallocated");
				}
				Long[] id1 = {long1};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelCVIds(id1);
				activities = this.getActivities(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				BigDecimal divideByDenominator;
				divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
				if (!activities.isEmpty()){
					itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
				}
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("Fundings")){
			switch (Integer.parseInt(id)) {
				case Constants.COMMITMENT:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Commitments");		
					break;
				case Constants.DISBURSEMENT:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Disbursements");
					break;
				case Constants.EXPENDITURE:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Expenditures");
					break;
				default:
					break;
				}
            activities = this.getActivities(filter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            BigDecimal divideByDenominator;
			divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
			if (!activities.isEmpty()){
				itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), filter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
			}
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidPredictability")){
    		HardCodedCategoryValue adjustmentType;
    		
    		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
    			itemName = TranslatorWorker.translateText("Aid Predictability - Actual");
    		} else {
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
    			itemName = TranslatorWorker.translateText("Aid Predictability - Planned");
    		}
            activities = this.getActivities(filter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType);
            BigDecimal divideByDenominator;
			divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
			if (!activities.isEmpty()){
				itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), filter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType, filter.getDecimalsToShow(),divideByDenominator));
			}
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidPredictabilityQuarter")){
    		HardCodedCategoryValue adjustmentType;
    		
    		if(id.toLowerCase().contains(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().toLowerCase())){
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
    			itemName = TranslatorWorker.translateText("Aid Predictability Quarter - Actual");
    		} else {
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
    			itemName = TranslatorWorker.translateText("Aid Predictability Quarter - Planned");
    		}
    		Date startDateQ = null;
			Date endDateQ = null;
			Calendar cal = Calendar.getInstance();  
			Integer quarter = Integer.valueOf(id.split("-")[1]);
    		switch (quarter) {
    		case 1:
				startDateQ = startDate;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
				endDateQ = cal.getTime();
				break;
    		case 2:
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
				startDateQ = cal.getTime();
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
				endDateQ = cal.getTime();
				break;
    		case 3:
    			cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
				startDateQ = cal.getTime();
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
				endDateQ = cal.getTime();
				break;
    		case 4:
    			cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
				startDateQ = cal.getTime();
				endDateQ = endDate;
				break;
			default:
				break;
			}
            activities = this.getActivities(filter, startDateQ, endDateQ, null, null, filter.getTransactionType(), adjustmentType);
            BigDecimal divideByDenominator;
			divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
			if (!activities.isEmpty()){
				itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), filter, startDateQ, endDateQ, null, null, filter.getTransactionType(), adjustmentType, filter.getDecimalsToShow(),divideByDenominator));
			}
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidType")){
			Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
	        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
				AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
				if (ampCategoryValue.getId()== Long.parseLong(id)) {
					itemName = TranslatorWorker.translateText("Aid Type - " + ampCategoryValue.getValue());
				}
			}
			activities = this.getActivities(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			BigDecimal divideByDenominator;
			divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
			if (!activities.isEmpty()){
				itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
			}
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidModality")){
			Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
	        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
				AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
				if (ampCategoryValue.getId()== Long.parseLong(id)) {
					itemName = TranslatorWorker.translateText("Aid Type - " + ampCategoryValue.getValue());
				}
			}
			activities = this.getActivities(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			BigDecimal divideByDenominator;
			divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
			if (!activities.isEmpty()){
				itemProjectsList.put(itemName, DbUtil.getFundingByActivityList(activities.keySet(), filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, filter.getDecimalsToShow(),divideByDenominator));
			}
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
        
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000 / 
				Math.max(1000000, FeaturesUtil.getAmountMultiplier())); // Math.max added for the future, in case "billions" 
		
	    if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new TreeMap<AmpActivityVersion, BigDecimal>();
	        BigDecimal totalSum = BigDecimal.ZERO;
	        while(it.hasNext()){
	        	AmpActivityVersion act = it.next();
	        	//Long temp = filter.getActivityId();
	        	DashboardFilter newFilter = filter.getCopyFilterForFunding();
	        	newFilter.setActivityId(act.getAmpActivityId());
	        	DecimalWraper fundingCal = null;
	        	if (type.equals("Fundings")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("AidPredictability")){
	        		HardCodedCategoryValue adjustmentType;
	        		
	        		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()))
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
	        		else
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType);
	        	} else if (type.equals("AidPredictabilityQuarter")){
	        		HardCodedCategoryValue adjustmentType;
	        		if(id.toLowerCase().contains(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().toLowerCase()))
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
	        		else
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;

	        		Date startDateQ = null;
					Date endDateQ = null;
					Calendar cal = Calendar.getInstance();  
					Integer quarter = Integer.valueOf(id.split("-")[1]);
	        		switch (quarter) {
	        		case 1:
						startDateQ = startDate;
						cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
						endDateQ = cal.getTime();
						break;
	        		case 2:
						cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
						startDateQ = cal.getTime();
						cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
						endDateQ = cal.getTime();
						break;
	        		case 3:
	        			cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
						startDateQ = cal.getTime();
						cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
						endDateQ = cal.getTime();
						break;
	        		case 4:
	        			cal.setTime(startDate);  
						cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
						startDateQ = cal.getTime();
						endDateQ = endDate;
						break;
					default:
						break;
					}
	        		fundingCal = DbUtil.getFunding(newFilter, startDateQ, endDateQ, null, null, filter.getTransactionType(), adjustmentType);
	        	} else if (type.equals("AidType")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("AidModality")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("SectorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelSectorIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelSectorIds(null);
	        	} else if (type.equals("NPOProfile")||type.equals("ProgramProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelProgramIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelProgramIds(null);
	        	} else if (type.equals("BudgetBreakdown")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setBudgetCVIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setBudgetCVIds(null);
	        	} else if (type.equals("RegionProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelLocationIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelLocationIds(null);
	        	} else if (type.equals("DonorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		newFilter.setSelOrgIds(id1);
					fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					newFilter.setSelOrgIds(null);
	        	} else {
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	}
	        	BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        	totalSum = totalSum.add(total);
	        	itemProjectsList.put(act, total);
	        	//filter.setActivityId(temp);
	        	
			}
	        return itemProjectsList;
		}
		return null;
	}
	
	private HashMap<Long, AmpActivityVersion> getActivities (DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,HardCodedCategoryValue adjustmentTypeActual){
		Collection activityListReduced;
		try {
			activityListReduced = DbUtil.getActivities(filter, startDate, endDate, assistanceTypeId, financingInstrumentId, transactionType, adjustmentTypeActual);
			HashMap<Long, AmpActivityVersion> activityList = new HashMap<Long, AmpActivityVersion>();
	        Iterator iter = activityListReduced.iterator();
	        while (iter.hasNext()) {
	            Object[] item = (Object[])iter.next();
	            Long ampActivityId = (Long) item[0];
	            String ampId = (String) item[1];
	            String name = (String) item[2];
	            AmpActivityVersion activity = new AmpActivityVersion(ampActivityId, name, ampId);
	            activityList.put(ampActivityId, activity);
	        }
	        return activityList;
		} catch (DgException e) {
			return null;
		}
		
	}
	
}
