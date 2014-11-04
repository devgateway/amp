package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.util.SearchUtil;

public class ActivityService {
	protected static Logger logger = Logger.getLogger(ActivityService.class);
	private static final Map<String, String> columnHeaders;
	 static
	    {
		 columnHeaders = new HashMap<String, String>();
		 columnHeaders.put(ColumnConstants.PROJECT_TITLE,"Title" );
		 columnHeaders.put(ColumnConstants.DONOR_AGENCY, "Donor");
		 columnHeaders.put(ColumnConstants.ACTIVITY_UPDATED_ON, "Date");
	    }
	
	public static JsonBean getActivitiesMondrian(JsonBean config,List<String>activitIds, Integer page, Integer pageSize) throws AmpApiException {
		boolean applyFilter=false;
		List<JsonBean> activities=new ArrayList<JsonBean>();
		
		//we check if we have filter by keyword
		Object otherFilter=null;
		if (config != null) {
			otherFilter=config.get("otherFilters");
			if(activitIds==null){
				activitIds=new ArrayList<String>();
			}
			activitIds.addAll(GisUtil.applyKeywordSearch( otherFilter));
		}
		
		
		String name= "ActivityList";
		boolean doTotals=true;
 		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);

		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
//		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		//for now we are going to return the donor_id as matchesfilters
		//then we have to fetch all other matchesfilters outisde mondrian

		/*
		 * Enabled this and remove explicit measures config 
		 * when confirmed that the expected measures config is indeed coming from settings
		GisUtil.applySettings(config);
		 */
		//TODO: remove this -- start --
		EndpointUtils.applySettings(spec, config);
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
 		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
 		// remove this -- end --

 		spec.setCalculateColumnTotals(doTotals);
		
 		spec.setCalculateRowTotals(doTotals);
		//following ids are added only for testing purposes Please dont commit

//		if(activitIds==null){
//			activitIds=new ArrayList<String>();
//			activitIds.add("42193");
//			activitIds.add("42188");
//			activitIds.add("42179");
//			activitIds.add("42178");
//			activitIds.add("42176");
//			activitIds.add("42175");
//			activitIds.add("42200");
//	
//		}
	
 		
 		MondrianReportFilters filterRules = GisUtil.getFilterRules(config, activitIds,
				otherFilter);
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
				
				if (reportOutputColumn.originalColumnName.equals(ColumnConstants.ACTIVITY_ID)
					|| reportOutputColumn.originalColumnName.equals(ColumnConstants.PROJECT_TITLE)
							|| reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)
							|| reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)
							)
						 {
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







	public static JSONObject getLastUpdatedActivities(List<String> extraColumns, Integer pageSize) {
		JSONObject responseJson = new JSONObject();
		if (pageSize == null) {
			pageSize = new Integer(10);
		}
		ReportSpecificationImpl spec = new ReportSpecificationImpl("LastUpdated");
		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ALL));
		for (String columnName : extraColumns) {
			if (!columnName.equals(ColumnConstants.ACTIVITY_UPDATED_ON)
					&& !columnName.equals(ColumnConstants.DONOR_AGENCY)
					&& !columnName.equals(ColumnConstants.PROJECT_TITLE)) {
				spec.addColumn(new ReportColumn(columnName));

			}
		}

		spec.addMeasure(new ReportMeasure(MeasureConstants.ALWAYS_PRESENT, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON), false));
		GeneratedReport report = null;
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
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
			int columnIndex = 0;
			for (ReportOutputColumn reportOutputColumn : col) {
				if (!reportOutputColumn.originalColumnName.equals(MeasureConstants.ALWAYS_PRESENT)) {

					activityObj.put(reportOutputColumn.originalColumnName, row.get(reportOutputColumn).value);
					String displayName = columnHeaders.get(reportOutputColumn.originalColumnName);
					if (displayName == null) {
						displayName = reportOutputColumn.originalColumnName;
					}
					header.put("HEADER_" + columnIndex, displayName);
					columnIndex++;
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
