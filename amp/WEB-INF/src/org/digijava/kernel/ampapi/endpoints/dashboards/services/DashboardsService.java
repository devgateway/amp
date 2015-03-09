package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSettings;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstans;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Diego Dimunzio
 * 
 */

public class DashboardsService {

	private static Logger logger = Logger.getLogger(DashboardsService.class);

	/**
	 * Utility method for creating the small objects for the list of tops Note
	 * -- I (Phil) hacked this in... it could probably be done better
	 * 
	 * @param pathId
	 *            the id to use in the path for the actual data
	 * @param name
	 *            the human-readable name for this top
	 * @return
	 */
	private static JsonBean getTopsListBean(String pathId, String name) {
		JsonBean obj = new JsonBean();
		obj.set("id", pathId);
		obj.set("name", name);
		return obj;
	}

	/**
	 * Return a list of the available top __ for the dashboard charts Note -- I
	 * (Phil) hacked this in, so it probably could use a review Also, I
	 * hard-coded the names ("Donor Agency" etc.) but they should be translated
	 * 
	 * @return
	 */
	public static List<JsonBean> getTopsList() {
		List<JsonBean> tops = new ArrayList<JsonBean>();
		tops.add(getTopsListBean("do", "Donor Agency"));
		tops.add(getTopsListBean("re", "Region"));
		tops.add(getTopsListBean("ps", "Primary Sector"));
		return tops;
	}

	/**
	 * Return (n) Donors sorted by amount
	 * 
	 * @param type
	 *            (Donor, Regions, Primary Sector)
	 * @param n
	 * @param config request configuration that stores filters, settings and any other options
	 * @return
	 */
	public static JsonBean getTops(String type, Integer n, JsonBean config) {
		String err = null;
		String column = "";
		JsonBean retlist = new JsonBean();
		String name = "";
		String title = "";
		List<JsonBean> values = new ArrayList<JsonBean>();

		switch (type.toUpperCase()) {
		case "DO":
			if (FeaturesUtil.isVisibleFeature("/Show Names As Acronyms")){
				column = MoConstants.ATTR_ORG_ACRONYM;
			} else {
				column = MoConstants.DONOR_AGENCY;
			}
			title = TranslatorWorker.translateText(DashboardConstans.TOP_DONOR_AGENCIES);
			name = DashboardConstans.TOP_DONOR_AGENCIES;
			break;
		case "RE":
			column = MoConstants.H_REGIONS;
			title = TranslatorWorker.translateText(DashboardConstans.TOP_REGIONS);
			name = DashboardConstans.TOP_REGIONS;
			break;
		case "PS":
			column = MoConstants.PRIMARY_SECTOR;
			title = TranslatorWorker.translateText(DashboardConstans.TOP_SECTORS);
			name = DashboardConstans.TOP_SECTORS;
			break;
		case "DG":
			column = ColumnConstants.DONOR_GROUP;
			title = TranslatorWorker.translateText(DashboardConstans.TOP_DONOR_GROUPS);
			name = DashboardConstans.TOP_DONOR_GROUPS;
			break;
		default:
			column = MoConstants.DONOR_AGENCY;
			title = TranslatorWorker.translateText(DashboardConstans.TOP_DONOR_AGENCIES);
			name = DashboardConstans.TOP_DONOR_AGENCIES;
			break;
		}

		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetTops", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(column));
		spec.getHierarchies().addAll(spec.getColumns());
		// applies settings, including funding type as a measure
		SettingsUtils.applyExtendedSettings(spec, config);
		spec.addSorter(new SortingInfo(spec.getMeasures().iterator().next(), false, true));
		spec.setCalculateRowTotals(true);
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = null;
		
		MondrianReportFilters filterRules = null;
 		if(config!=null){
 			LinkedHashMap<String, Object> columnFilters=(LinkedHashMap<String, Object>)config.get("columnFilters");
 			LinkedHashMap<String, Object> otherFilter=(LinkedHashMap<String, Object>)config.get("otherFilters");
			filterRules = FilterUtils.getFilterRules(columnFilters,
 					otherFilter, null);
 			if(filterRules!=null){
 				spec.setFilters(filterRules);
 			}
 		
 		}
 		
		// AMP-18740: For dashboards we need to use the default number formatting and leave the rest of the settings
		// configurable (calendar, currency, etc).
		MondrianReportSettings defaultSettings = MondrianReportUtils.getCurrentUserDefaultSettings();
		MondrianReportSettings currentSettings = (MondrianReportSettings) spec.getSettings();
		currentSettings.setCurrencyFormat(defaultSettings.getCurrencyFormat());
 		
 		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}

		// Format the report output return a simple list.
		// this is the report totals, which is not for the top N, but for ALL
		// results
 		ReportCell totals = null;
		if (report.reportContents != null && report.reportContents.getContents() != null
				&& report.reportContents.getContents().size() > 0) {
			totals = (ReportCell) report.reportContents.getContents().values().toArray()[1];
			retlist.set("total", totals.value);
		} else {
			retlist.set("total", 0);
		}

		String currcode = null;
		currcode = spec.getSettings().getCurrencyCode();
		retlist.set("currency", currcode);

		retlist.set("numberformat", numberformat);
		
		
		Integer maxLimit = report.reportContents.getChildren().size();
		
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean amountObj = new JsonBean();
			ReportAreaImpl reportArea = (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			String dvalue = reportcolumn.displayedValue;
			//Remove undefined from region's chart AMP-18632
			if(!dvalue.equalsIgnoreCase(MoConstants.REGION_UNDEFINED)){
				amountObj.set("name", dvalue);
				amountObj.set("amount", reportcell.value);
				amountObj.set("formattedAmount", reportcell.displayedValue);
				values.add(amountObj);
			}else{
				//Subtract National from the total 
				if ((Double) retlist.get("total") != 0){
					retlist.set("total",(Double) retlist.get("total") - (Double) reportcell.value);
				}
				//Remove National's bar from the chart
				maxLimit --;
			}
			
			if (values.size() >= n) {
				break;
			}
		}
		retlist.set("values", values);

		// report the total number of tops available
		retlist.set("maxLimit", maxLimit );
		
		retlist.set("name", name);
		retlist.set("title", title);

		return retlist;
	}
	
	protected static JSONObject buildEmptyJSon(String...keys) {
		JSONObject obj = new JSONObject();
		for(String key:keys)
			obj.put(key, 0d);
		return obj;
	}
	
	/**
	 * 
	 * @param filter
	 * @return
	 */
	
	public static JSONObject getAidPredictability(JsonBean filter) throws Exception {

		JSONObject retlist = new JSONObject();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.COUNTRY));
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.COUNTRY));
		spec.addMeasure(new ReportMeasure(MoConstants.PLANNED_DISBURSEMENTS));
		spec.addMeasure(new ReportMeasure(MoConstants.ACTUAL_DISBURSEMENTS));
		spec.setCalculateRowTotals(true);
		spec.setCalculateColumnTotals(true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filterRules = null;
 		if(filter!=null){
 			LinkedHashMap<String, Object> columnFilters=(LinkedHashMap<String, Object>)filter.get("columnFilters");
 			LinkedHashMap<String, Object> otherFilter=(LinkedHashMap<String, Object>)filter.get("otherFilters");
			filterRules = FilterUtils.getFilterRules(columnFilters,
 					otherFilter, null);
 			if(filterRules!=null){
 				spec.setFilters(filterRules);
 			}
 		
 		}
 	

		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		
		SettingsUtils.applySettings(spec, filter);
		
		GeneratedReport report = generator.executeReport(spec);
		//Not only years, we can have values like 'Fiscal calendar 2010-2011', so the Map should be <String,JSONObject>
		Map<String, JSONObject> results = new TreeMap<>(); // accumulator of per-year results
				
		if (report.reportContents.getContents()!=null) {
			 for (ReportOutputColumn outputColumn:report.reportContents.getContents().keySet()) {
				// ignore non-funding contents
				if (outputColumn.parentColumn == null) {
					continue;
				}
					
				boolean isPlannedColumn = outputColumn.originalColumnName.equals(MoConstants.PLANNED_DISBURSEMENTS);
				boolean isTotalColumn = outputColumn.parentColumn != null && outputColumn.parentColumn.originalColumnName.equals("Total Measures");
				String destination = isPlannedColumn ? "planned" : "actual";
				
				String yearValue = isTotalColumn ? "totals" : outputColumn.parentColumn.columnName;
				if (!results.containsKey(yearValue))
					results.put(yearValue, buildEmptyJSon("planned", "actual"));
				JSONObject amountObj = results.get(yearValue);
	
				amountObj.put(destination, report.reportContents.getContents().get(outputColumn).value);
			}
		}
		JSONArray yearsArray = new JSONArray ();
		for(String yearValue:results.keySet())
			if (!yearValue.equals("totals")) {
				results.get(yearValue).put("year", yearValue);
				yearsArray.add(results.get(yearValue));
			}
		retlist.put("years", yearsArray);
		retlist.put("totals", results.get("totals"));		
	
		String currcode = null;
		currcode = spec.getSettings().getCurrencyCode();
		retlist.put("currency", currcode);
		
		retlist.put("numberformat", numberformat);
		
		retlist.put("name", DashboardConstans.AID_PREDICTABILITY);
		retlist.put("title", TranslatorWorker.translateText(DashboardConstans.AID_PREDICTABILITY));
		return retlist;
	}

	/**
	 * Get a list of funding types by year.
	 * @param adjtype - Measure Actual commitment, Actual Disbursement, Etc.
	 * @param filter
	 * @return
	 */
	
	public static JsonBean fundingtype(String adjtype, JsonBean filter) {
		String adjustmenttype = "";
		String err = null;
		JsonBean retlist = new JsonBean();
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype", ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.FUNDING_YEAR));
		spec.addColumn(new ReportColumn(MoConstants.TYPE_OF_ASSISTANCE));
		spec.getHierarchies().addAll(spec.getColumns());
		spec.setCalculateRowTotals(true);
		
		// also configures funding type
		SettingsUtils.applyExtendedSettings(spec, filter);
		
		spec.addSorter(new SortingInfo(spec.getMeasures().get(0), false));
		
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = null;
		
		MondrianReportFilters filterRules = null;
 		if(filter!=null){
 			LinkedHashMap<String, Object> columnFilters=(LinkedHashMap<String, Object>)filter.get("columnFilters");
 			LinkedHashMap<String, Object> otherFilter=(LinkedHashMap<String, Object>)filter.get("otherFilters");
			filterRules = FilterUtils.getFilterRules(columnFilters,
 					otherFilter, null);
 			if(filterRules!=null){
 				spec.setFilters(filterRules);
 			}
 		
 		}
		
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}
		
		//Get total
		if (report.reportContents != null && report.reportContents.getContents() != null && report.reportContents.getContents().size() > 0) {
			ReportCell totals = (ReportCell)report.reportContents.getContents().values().toArray()[2];
			retlist.set("total", totals.value);
		} else {
			retlist.set("total", 0);
		}
		
		String currcode = null;
		currcode = spec.getSettings().getCurrencyCode();
		retlist.set("currency", currcode);
		
		retlist.set("Numberformat",numberformat);
		List<JsonBean> values = new ArrayList<JsonBean>();
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			List<JsonBean> subvalues = new ArrayList<JsonBean>();
			ReportAreaImpl reportArea = (ReportAreaImpl) iterator.next();
			JsonBean year = new JsonBean();
			for (int i = 0; i < reportArea.getChildren().size(); i++) {
				Map<ReportOutputColumn, ReportCell> row = reportArea.getChildren().get(i).getContents();
				JsonBean amountObj = new JsonBean();
				for (Entry<ReportOutputColumn, ReportCell> entry : row.entrySet()) {
					ReportOutputColumn key = entry.getKey();
					ReportCell value = entry.getValue();
					switch (key.originalColumnName) {
					case ColumnConstants.FUNDING_YEAR:
						if (!"".equals(value.value)) {
							year.set("Year", value.value);
						}
						break;
					case ColumnConstants.TYPE_OF_ASSISTANCE:
						amountObj.set("type", value.displayedValue);
						break;
					default:
						amountObj.set("amount", value.value);
						break;
					}
					if (amountObj.getSize()==2){
						subvalues.add(amountObj);
					}
				}
			}
			year.set("values", subvalues);
			values.add(year);
		}
		retlist.set("values", values);
		
		retlist.set("name", DashboardConstans.FUNDING_TYPE);
		retlist.set("title", TranslatorWorker.translateText(DashboardConstans.FUNDING_TYPE));
		
		return retlist;
	}
}
