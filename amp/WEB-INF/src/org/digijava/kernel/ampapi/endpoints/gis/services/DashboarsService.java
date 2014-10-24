package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
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

public class DashboarsService {

	private static Logger logger = Logger.getLogger(DashboarsService.class);

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
	 * @return
	 */
	public static JsonBean getTops(String type, String adjtype, Integer n) {
		String err = null;
		String column = "";
		String adjustmenttype = "";
		JsonBean retlist = new JsonBean();
		List<JsonBean> values = new ArrayList<JsonBean>();

		switch (type.toUpperCase()) {
		case "DO":
			column = MoConstants.DONOR_AGENCY;
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
		retlist.set("currency", currcode);

		retlist.set("numberformat", numberformat);

		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			JsonBean amountObj = new JsonBean();
			ReportAreaImpl reportArea = (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea
					.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content
					.values().toArray()[0];
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

	public static JSONObject getAidPredictability(JsonBean filter) {

		JSONObject retlist = new JSONObject();
		ReportSpecificationImpl spec = new ReportSpecificationImpl("GetAidPredictability");
		//spec.addColumn(new ReportColumn(ColumnConstants.SECTOR_GROUP, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
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
		GeneratedReport report = null;
		try {
			report = generator.executeReport(spec);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		if (report.reportContents != null && report.reportContents.getContents() != null
				&& report.reportContents.getContents().size() > 0) {
			
			Iterator <ReportOutputColumn> iterator = report.reportContents.getContents().keySet().iterator();
			JSONArray array = new JSONArray ();
			while (iterator.hasNext()) {
				JSONObject amountObj = new JSONObject();
				ReportOutputColumn outputColumnPlanned = iterator.next();
				// it is 'Project Title' or 'Activity Id' column, we don't
				// process them now
				if (outputColumnPlanned.parentColumn == null) {
					continue;
				}
				ReportOutputColumn outputColumnActual = iterator.next();
				if (outputColumnPlanned.parentColumn.columnName.equals("totals")) {
					amountObj.put("planned", //outputColumnPlanned.columnName,
							report.reportContents.getContents().get(outputColumnPlanned).value);
					amountObj.put("actual", //outputColumnActual.columnName,
							report.reportContents.getContents().get(outputColumnActual).value);
					retlist.put(outputColumnPlanned.parentColumn.columnName, amountObj);
				} else {
					amountObj.put("planned", //outputColumnPlanned.columnName,
							report.reportContents.getContents().get(outputColumnPlanned).value);
					amountObj.put("actual", //outputColumnActual.columnName,
							report.reportContents.getContents().get(outputColumnActual).value);
					amountObj.put("year", outputColumnPlanned.parentColumn.columnName);
					array.add(amountObj);
				}
			}
			retlist.put("years", array);

		} else {
			retlist.put("total", 0);  // Is this behaviour specified anywhere? When could this happen? --phil
		}
	
		String currcode = EndpointUtils.getDefaultCurrencyCode();
		retlist.put("currency", currcode);

		retlist.put("numberformat", numberformat);
		return retlist;
	}

	
}
