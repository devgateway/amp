package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;

public class ActivityService {
	protected static Logger logger = Logger.getLogger(ActivityService.class);
	private static final Map<String, String> columnHeaders;
	private static final Set<String> columnsToProvide;
	 static
	    {
		 columnHeaders = new HashMap<String, String>();
		 columnHeaders.put(ColumnConstants.PROJECT_TITLE,"Title" );
		 columnHeaders.put(ColumnConstants.DONOR_AGENCY, "Donor Agency");
		 columnHeaders.put(ColumnConstants.ACTIVITY_UPDATED_ON, "Date");
		 
		 columnsToProvide = new HashSet<String>();
		 columnsToProvide.add(ColumnConstants.ACTIVITY_ID);
		 columnsToProvide.add(ColumnConstants.PROJECT_TITLE);
		 columnsToProvide.add(MeasureConstants.ACTUAL_COMMITMENTS);
		 columnsToProvide.add(MeasureConstants.ACTUAL_DISBURSEMENTS);
		 columnsToProvide.add(MeasureConstants.PLANNED_COMMITMENTS);
		 columnsToProvide.add(MeasureConstants.PLANNED_DISBURSEMENTS);
	    }
	
	public static JsonBean getActivitiesMondrian(JsonBean config,List<String>activitIds, Integer page, Integer pageSize) throws AmpApiException {
		boolean applyFilter=false;
		List<JsonBean> activities=new ArrayList<JsonBean>();
		
		//we check if we have filter by keyword
		LinkedHashMap<String, Object> otherFilter=null;
		if (config != null) {
			otherFilter=(LinkedHashMap<String, Object>)config.get("otherFilters");
			if(activitIds==null){
				activitIds=new ArrayList<String>();
			}
			activitIds.addAll(FilterUtils.applyKeywordSearch( otherFilter));
		}
		
		
		String name= "ActivityList";
		boolean doTotals=true;
 		ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);

		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_ID));
		//for now we are going to return the donor_id as matchesfilters
		spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));

		//then we have to fetch all other matchesfilters outisde mondrian

		// apply default settings
		SettingsUtils.applySettings(spec, config);
		// apply custom settings
		configureMeasures(spec, config);
		
 		spec.setCalculateColumnTotals(doTotals);
 		
 		// AMP-19772: Needed to avoid problems on GIS js. 
 		spec.setDisplayEmptyFundingRows(true);
		
 		spec.setCalculateRowTotals(doTotals);

		MondrianReportFilters filterRules = FilterUtils.getFilterRules(
				(LinkedHashMap<String, Object>) config.get("columnFilters"),
				otherFilter, activitIds);
		if(filterRules!=null){
			spec.setFilters(filterRules);
		}
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		GeneratedReport report = null;		
		try {
			report = generator.executeReport(spec);
		} catch (AMPException e) {
			logger.error("Cannot execute report", e);
			throw new AmpApiException(e);
		}
		//if pagination is requested
		List<ReportArea> ll=null;
		if(page !=null && pageSize !=null && page>=0 && pageSize>0){
			ReportAreaMultiLinked[] areasDFArray = ReportPaginationUtils.convert(report.reportContents);
			
			ReportArea pagedReport = ReportPaginationUtils.getReportArea(areasDFArray, page, pageSize);
			ll=pagedReport.getChildren();
		}else{ 
			ll = report.reportContents.getChildren();
		}
 		Integer count=report.reportContents.getChildren().size();

		for (ReportArea reportArea : ll) {
			JsonBean activity = new JsonBean();
			JsonBean filters = new JsonBean();
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			for (ReportOutputColumn reportOutputColumn : col) {
				//Filters should be grouped together.
				
				if (columnsToProvide.contains(reportOutputColumn.originalColumnName)) {
					activity.set(reportOutputColumn.originalColumnName,row.get(reportOutputColumn).value);
					if(reportOutputColumn.originalColumnName.equals(ColumnConstants.ACTIVITY_ID)){
						activity.set("ampUrl",ActivityGatekeeper.buildPreviewUrl(String.valueOf(row.get(reportOutputColumn).value)));
					}
				}else{
					//we exclude undefineds or value 999999999 for ids
						//if(!"999999999".equals(row.get(reportOutputColumn).value) ){
							filters.set(reportOutputColumn.originalColumnName,row.get(reportOutputColumn).value);
						//}
				}
			}
			activity.set("matchesFilters",filters);
			activities.add(activity);
		}
		JsonBean list=new JsonBean();
		list.set("count", count);
		list.set("activities", activities);
		return list;
	}
	
	/**
	 * Adds measures to the report specification based on AMP-18874:
	 * a) needs to have 'planned commitments' and  'planned disbursements'  if any planned setting is selected.
	 * b) needs to have 'actual commitments' and  'actual disbursements'  if any actual setting is selected.
	 * 
	 * @param spec
	 * @param config
	 */
	private static void configureMeasures(ReportSpecificationImpl spec, JsonBean config) {
		if (spec != null && config != null) {
			Map<String, Object> settings = (Map<String, Object>) config.get(EPConstants.SETTINGS);
			String fundingType = (String) (settings == null ? null : settings.get(SettingsConstants.FUNDING_TYPE_ID));
			if (fundingType == null)
				fundingType = SettingsConstants.DEFAULT_FUNDING_TYPE_ID;
			if (fundingType.startsWith("Actual")) {
				spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
				spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
			}
			if (fundingType.startsWith("Planned")) {
				spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS));
				spec.addMeasure(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS));
			}
		}
	}

	/**
	 * 
	 * @param extraColumns
	 * @param pageSize
	 * @param config
	 * @return
	 */
	
	public static JSONObject getLastUpdatedActivities(List<String> extraColumns, Integer pageSize, JsonBean config) {
	JSONObject responseJson = new JSONObject();
	if (pageSize == null) {
	    pageSize = new Integer(10);
	}
	ReportSpecificationImpl spec = new ReportSpecificationImpl("LastUpdated", ArConstants.DONOR_TYPE);
	spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
	spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
	spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
	for (String columnName : extraColumns) {
	    if (!columnName.equals(ColumnConstants.ACTIVITY_UPDATED_ON)
			    && !columnName.equals(ColumnConstants.DONOR_AGENCY)
			    && !columnName.equals(ColumnConstants.PROJECT_TITLE)) {
		spec.addColumn(new ReportColumn(columnName));

	    }
	}

	spec.addMeasure(new ReportMeasure(MeasureConstants.ALWAYS_PRESENT));
	spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
	
	if (config != null) {
	    SettingsUtils.applySettings(spec, config);
	}
	
	GeneratedReport report = null;
	MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
			ReportEnvironment.buildFor(TLSUtils.getRequest()), true);
	try {
	    report = generator.executeReport(spec);
	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
	ReportAreaMultiLinked[] areasDFArray = ReportPaginationUtils.convert(report.reportContents);
	ReportArea pagedReport = ReportPaginationUtils.getReportArea(areasDFArray, 0, pageSize);
	List<ReportArea> area = pagedReport.getChildren();
	JSONArray activities = new JSONArray();
	JSONArray headers = new JSONArray();
	boolean headerAdded = false;
	for (Iterator<ReportArea> iterator = area.iterator(); iterator.hasNext();) {
	    JSONObject activityObj = new JSONObject();
	    JSONObject header = new JSONObject();
	    ReportArea reportArea = iterator.next();

	    Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
	    Set<ReportOutputColumn> col = row.keySet();
	    for (ReportOutputColumn reportOutputColumn : col) {

		if (!reportOutputColumn.originalColumnName.equals(MeasureConstants.ALWAYS_PRESENT)) {
		    activityObj.put(reportOutputColumn.originalColumnName, row.get(reportOutputColumn).displayedValue);
		    String displayName = columnHeaders.get(reportOutputColumn.originalColumnName);
		    if (displayName == null) {
			displayName = reportOutputColumn.originalColumnName;
		    }
		    displayName = TranslatorWorker.translateText(displayName);
		    header.put(reportOutputColumn.originalColumnName, displayName);
		}

	    }

	    if (!headerAdded) {
		headers.add(header);
	    }
	    activities.add(activityObj);
	    headerAdded = true;
	}
	responseJson.put("activities", activities);
	responseJson.put("headers", headers);
	return responseJson;

	}
}
