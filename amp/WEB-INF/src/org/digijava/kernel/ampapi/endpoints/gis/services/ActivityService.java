package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.dgfoundation.amp.reports.ReportAreaMultiLinked;
import org.dgfoundation.amp.reports.ReportPaginationUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.util.SearchUtil;

public class ActivityService {
	protected static Logger logger = Logger.getLogger(ActivityService.class);
	
	
	public static List<JsonBean> getActivitiesMondrian(JsonBean config,List<String>activitIds, Integer page, Integer pageSize) throws AmpApiException {
		boolean applyFilter=false;
		List<JsonBean> activities=new ArrayList<JsonBean>();
		
		//we check if we have filter by keyword
		Object otherFilter=null;
		if (config != null) {
			otherFilter=config.get("otherFilters");
			if (otherFilter!=null && ((Map<String,Object>)otherFilter).get("keyword") != null) {
				String keyword = ((Map<String,Object>)otherFilter).get("keyword").toString();
				Collection<LoggerIdentifiable> activitySearch = SearchUtil
						.getActivities(keyword,
								TLSUtils.getRequest(), (TeamMember) TLSUtils.getRequest().getSession().getAttribute("currentMember"));
				if (activitySearch != null && activitySearch.size() > 0) {
					if(activitIds==null){
						activitIds=new ArrayList<String>();
					}
					for (LoggerIdentifiable loggerIdentifiable : activitySearch) {
						activitIds.add(loggerIdentifiable.getIdentifier().toString());
					}
				}
			}
		}
		
		
		String name= "ActivityList";
		boolean doTotals=true;
 		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);

		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addColumn(new ReportColumn(ColumnConstants.DONOR_ID, ReportEntityType.ENTITY_TYPE_ALL));
		//for now we are going to return the donor_id as matchesfilters
		//then we have to fetch all other matchesfilters outisde mondrian

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
 		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

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
 		MondrianReportFilters filterRules = null;
 		if(config!=null){
			Object filter=config.get("columnFilters");
			if(filter!=null){
				filterRules = FilterUtils.getApiColumnFilter((LinkedHashMap<String, Object>)config.get("columnFilters"));	
			}
			if(otherFilter!=null){
				filterRules = FilterUtils.getApiOtherFilters((Map<String,Object>)otherFilter, filterRules);
			}
 		}
		if(activitIds!=null && activitIds.size()>0){
			//if we have activityIds to add to the filter comming from the search by keyworkd
			if(filterRules==null){
				filterRules = new MondrianReportFilters();
			}

			filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
					new FilterRule(activitIds, true, true)); 

		}
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
			ReportArea page0_10 = ReportPaginationUtils.getReportArea(areasDFArray, 0, 10);
			ll=page0_10.getChildren();
		}else{ 
			ll = report.reportContents.getChildren();
		}
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

		return activities;
	}
}
