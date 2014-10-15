package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.form.helpers.ActivityFundingDigest;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.util.SearchUtil;

public class ActivityService {
	protected static Logger logger = Logger.getLogger(ActivityService.class);
	/**
	 * 
	 * @param filter
	 * @return
	 * @deprecated this will be removed once the implementation with mondrian is done
	 */
	
	public static List<Activity> getActivities(JsonBean filter){

		List<Activity> activities = new ArrayList<Activity>();
		String ids=null;
		
		
		// lets see if the filter contains the keyword
		if (filter != null) {
			if (filter.get("keyword") != null) {
				Collection<LoggerIdentifiable> activitySearch = SearchUtil
						.getActivities(filter.get("keyword").toString(),
								TLSUtils.getRequest(), null);
				if (activitySearch != null && activitySearch.size() > 0) {
					List<Long> searchIds = new ArrayList<Long>();
					for (LoggerIdentifiable loggerIdentifiable : activitySearch) {
						searchIds.add((Long) loggerIdentifiable.getIdentifier());
					}
					ids=org.dgfoundation.amp.Util.toCSString(searchIds);
					
				}
			}
		}
		
		List<AmpActivity> ampActivities = QueryUtil.getActivities(ids);

		if (ampActivities != null) {
			for (AmpActivity ampActivity : ampActivities) {
				activities.add(buildActivityDto(ampActivity,false));
			}

		}

		return activities;
	}
	@Deprecated //will be deleted
	public static List<Activity> getActivities(String activityIds) {
		List<Activity> l=new ArrayList<Activity>();
		List<AmpActivity>activities=QueryUtil.getActivities(activityIds);
		for (AmpActivity activity : activities) {

			if(activity!=null){
				l.add(buildActivityDto(activity,true));
			}
		}
		return l;
	}
	
	/**
	 * build an activity to be serialized base upon AmpActivity class
	 * 
	 * @param ampActivity
	 * @return
	 */@Deprecated //will be deleted
	private static Activity buildActivityDto(AmpActivity ampActivity,boolean addFunding) {
		Activity a = new Activity();
		a.setId(ampActivity.getAmpActivityId());
		a.setName(ampActivity.getName());
		String description = null;

		a.setDescription(description);
		a.setAmpUrl(ActivityGatekeeper.buildPreviewUrl(String.valueOf(ampActivity
				.getAmpActivityId())));
		JsonBean j=new JsonBean();
		Map<Long,List<Long>> sectors=new HashMap<Long,List<Long>>();
		Map<Long,List<Long>> roles=new HashMap<Long,List<Long>>();
		Map<Long,List<Long>> programs=new HashMap<Long,List<Long>>();
		for (Object o : ampActivity.getSectors()) {
			AmpActivitySector as=(AmpActivitySector)o;
			if(
			sectors.get(as.getClassificationConfig().getId())  ==null){
				sectors.put(as.getClassificationConfig().getId(), new ArrayList<Long>());
			}
			sectors.get(as.getClassificationConfig().getId()).add(as.getSectorId().getAmpSectorId());
		}
		for(AmpOrgRole orgRole:ampActivity.getOrgrole()){
				if(roles.get(orgRole.getRole().getAmpRoleId())==null){
				roles.put(orgRole.getRole().getAmpRoleId(),new ArrayList<Long>());
			}
			roles.get(orgRole.getRole().getAmpRoleId()).add(orgRole.getOrganisation().getAmpOrgId());
		}
		
		
		for(Object o :ampActivity.getActPrograms()){
			AmpActivityProgram aap=(AmpActivityProgram)o;
			if(programs.get(aap.getProgramSetting().getAmpProgramSettingsId()) ==null){
				programs.put(aap.getProgramSetting().getAmpProgramSettingsId(),new ArrayList<Long>());
			}
			programs.get(aap.getProgramSetting().getAmpProgramSettingsId()).add(aap.getProgram().getAmpThemeId());
			
		}
		
		j.set("sectors", sectors);
		j.set("organizations", roles);
		j.set("programs", programs);
		
		a.setMatchesFilters(j);
		
		
		//add funding in case its not the list
		if(addFunding){
			ActivityFundingDigest fundingDigest=new ActivityFundingDigest();
			fundingDigest.populateFromFundings(ampActivity.getFunding(), "US", null, false);
			a.setTotalCommitments(fundingDigest.getTotalCommitments());
			a.setTotalDisbursments(fundingDigest.getTotalDisbursements());
		}
		return a;
	}
	public static List<JsonBean> getActivitiesMondrian(JsonBean filter,List<String>activitIds, Integer page, Integer pageSize) throws AmpApiException {
		boolean applyFilter=false;
		List<JsonBean> activities=new ArrayList<JsonBean>();
		
		//we check if we have filter by keyword
		if (filter != null) {
			if (filter.get("keyword") != null) {
				Collection<LoggerIdentifiable> activitySearch = SearchUtil
						.getActivities(filter.get("keyword").toString(),
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

		if(activitIds==null){
			activitIds=new ArrayList<String>();
			activitIds.add("42193");
			activitIds.add("42188");
			activitIds.add("42179");
			activitIds.add("42178");
			activitIds.add("42176");
			activitIds.add("42175");
			activitIds.add("42200");
	
		}
		if(activitIds!=null && activitIds.size()>0){
			MondrianReportFilters
			filterRules = new MondrianReportFilters();

			filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
					new FilterRule(activitIds, true, true)); 
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
				if (reportOutputColumn.columnName.equals(ColumnConstants.ACTIVITY_ID)
					|| reportOutputColumn.columnName.equals(ColumnConstants.PROJECT_TITLE)
							|| reportOutputColumn.columnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)
							|| reportOutputColumn.columnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)
							)
						 {
					activity.set(reportOutputColumn.columnName,row.get(reportOutputColumn).value);
					if(reportOutputColumn.columnName.equals(ColumnConstants.ACTIVITY_ID)){
						activity.set("ampUrl",ActivityGatekeeper.buildPreviewUrl(String.valueOf(row.get(reportOutputColumn).value)));
					}
				}else{
					//we exclude undefineds or value 999999999 for ids
						//if(!"999999999".equals(row.get(reportOutputColumn).value) ){
							filters.set(reportOutputColumn.columnName,row.get(reportOutputColumn).value);
						//}
				}
			}
			activity.set("matchesFilters",filters);
			activities.add(activity);
		}

		return activities;
	}
}
