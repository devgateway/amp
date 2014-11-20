package org.digijava.kernel.ampapi.endpoints.gis.services;

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
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.request.TLSUtils;
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
	 * @param adjtype
	 *            (Actual Commitments, Actual Disbursement)
	 * @param n
	 * @param config request configuration that stores filters, settings and any other options
	 * @return
	 */
	public static JsonBean getTops(String type, String adjtype, Integer n, JsonBean config) {
		String err = null;
		String column = "";
		String adjustmenttype = "";
		JsonBean retlist = new JsonBean();
		List<JsonBean> values = new ArrayList<JsonBean>();

		switch (type.toUpperCase()) {
		case "DO":
			if (FeaturesUtil.isVisibleFeature("Show Names As Acronyms")){
				column = MoConstants.ATTR_ORG_ACRONYM;
			}else{
				column = MoConstants.DONOR_AGENCY;
			}
			break;
		case "RE":
			column = MoConstants.H_REGIONS;
			break;
		case "PS":
			column = MoConstants.PRIMARY_SECTOR;
			break;
		default:
			column = MoConstants.DONOR_AGENCY;
			break;
		}

		switch (adjtype.toUpperCase()) {
		case "AC":
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		case "AD":
			adjustmenttype = MoConstants.ACTUAL_DISBURSEMENTS;
			break;
		default:
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		}

		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetTops");
		spec.addColumn(new ReportColumn(column, ReportEntityType.ENTITY_TYPE_ALL));
		spec.getHierarchies().addAll(spec.getColumns());
		spec.addMeasure(new ReportMeasure(adjustmenttype, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportMeasure(adjustmenttype), false));
		spec.setCalculateRowTotals(true);
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = null;
		
		MondrianReportFilters filterRules = null;
 		if(config != null){
			Object columnFilters = config.get("columnFilters");
			if(columnFilters != null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>) config.get("columnFilters"));	
				spec.setFilters (filterRules);
			}
 		}
 		
 		EndpointUtils.applySettings(spec, config);
 		
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			err = e.getMessage();
		}

		// Format the report output return a simple list.
		// this is the report totals, which is not for the top N, but for ALL
		// results
		if (report.reportContents != null && report.reportContents.getContents() != null
				&& report.reportContents.getContents().size() > 0) {
			ReportCell totals = (ReportCell) report.reportContents.getContents().values().toArray()[1];
			retlist.set("total", totals.value);
		} else {
			retlist.set("total", 0);
		}

		String currcode = EndpointUtils.getDefaultCurrencyCode();
		if (spec.getSettings() != null && spec.getSettings().getCurrencyCode() != null)
			currcode = spec.getSettings().getCurrencyCode();
		retlist.set("currency", currcode);

		retlist.set("numberformat", numberformat);

		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean amountObj = new JsonBean();
			ReportAreaImpl reportArea = (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			amountObj.set("name", reportcolumn.displayedValue);
			amountObj.set("amount", reportcell.value);
			values.add(amountObj);
			if (values.size() >= n) {
				break;
			}
		}
		retlist.set("values", values);

		// report the total number of tops available
		retlist.set("maxLimit", report.reportContents.getChildren().size());

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
		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability");
		//spec.addColumn(new ReportColumn(ColumnConstants.SECTOR_GROUP, ReportEntityType.ENTITY_TYPE_ALL));
		//spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.COUNTRY, ReportEntityType.ENTITY_TYPE_ALL));
		spec.getHierarchies().add(new ReportColumn(ColumnConstants.COUNTRY, ReportEntityType.ENTITY_TYPE_ALL));
		//spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MoConstants.PLANNED_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MoConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateRowTotals(true);
		spec.setCalculateColumnTotals(true);
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);

		MondrianReportFilters filterRules = null;
 		if(filter!=null){
			Object columnFilters=filter.get("columnFilters");
			if(columnFilters!=null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>)filter.get("columnFilters"));	
				spec.setFilters (filterRules);
			}
 		}
	

		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = generator.executeReport(spec);
		Map<Integer, JSONObject> results = new TreeMap<>(); // accumulator of per-year results
				
		if (report.reportContents.getContents()!=null) {
			 for (ReportOutputColumn outputColumn:report.reportContents.getContents().keySet()) {
				// ignore non-funding contents
				if (outputColumn.parentColumn == null) {
					continue;
				}
					
				boolean isPlannedColumn = outputColumn.originalColumnName.equals(MoConstants.PLANNED_DISBURSEMENTS);
				boolean isTotalColumn = outputColumn.parentColumn != null && outputColumn.parentColumn.originalColumnName.equals("Total Measures");
				String destination = isPlannedColumn ? "planned" : "actual";
				
				int yearNr = isTotalColumn ? 0 : Integer.parseInt(outputColumn.parentColumn.columnName);
				if (!results.containsKey(yearNr))
					results.put(yearNr, buildEmptyJSon("planned", "actual"));
				JSONObject amountObj = results.get(yearNr);
	
				amountObj.put(destination, report.reportContents.getContents().get(outputColumn).value);
			}
		}
		JSONArray yearsArray = new JSONArray ();
		for(int yearNr:results.keySet())
			if (yearNr > 0) {
				results.get(yearNr).put("year", Integer.toString(yearNr));
				yearsArray.add(results.get(yearNr));
			}
		retlist.put("years", yearsArray);
		retlist.put("totals", results.get(0));		
	
		String currcode = EndpointUtils.getDefaultCurrencyCode();
		retlist.put("currency", currcode);

		retlist.put("numberformat", numberformat);
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
		
		switch (adjtype.toUpperCase()) {
		case "AC":
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		case "AD":
			adjustmenttype = MoConstants.ACTUAL_DISBURSEMENTS;
			break;
		default:
			adjustmenttype = MoConstants.ACTUAL_COMMITMENTS;
			break;
		}
		
		
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype");
		spec.addColumn(new ReportColumn(ColumnConstants.FUNDING_YEAR,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(MoConstants.TYPE_OF_ASSISTANCE,ReportEntityType.ENTITY_TYPE_ALL));
		spec.getHierarchies().addAll(spec.getColumns());
		spec.addMeasure(new ReportMeasure(adjustmenttype,ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportMeasure(adjustmenttype), false));
		spec.setCalculateRowTotals(true);
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, ReportEnvironment.buildFor(TLSUtils.getRequest()), true);
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember");
		String numberformat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
		GeneratedReport report = null;
		
		MondrianReportFilters filterRules = null;
 		if(filter!=null){
			Object columnFilters=filter.get("columnFilters");
			if(columnFilters!=null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>)filter.get("columnFilters"));	
				spec.setFilters (filterRules);
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
		String currcode = EndpointUtils.getDefaultCurrencyCode();
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
					case MoConstants.ACTUAL_COMMITMENTS:
						amountObj.set("amount", value.value);
					default:
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
		
		return retlist;
	}
}
