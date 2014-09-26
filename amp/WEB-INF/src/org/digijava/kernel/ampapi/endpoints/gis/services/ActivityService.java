package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.PathSegment;

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
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Activity;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.form.helpers.ActivityFundingDigest;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.util.SearchUtil;

public class ActivityService {
	public static void getActivities(String keyword,HttpServletRequest request, TeamMember tm) {
		Collection<LoggerIdentifiable> matchingActivityIds=SearchUtil.getActivities(keyword,request,tm);
	}
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
	 */
	private static Activity buildActivityDto(AmpActivity ampActivity,boolean addFunding) {
		Activity a = new Activity();
		a.setId(ampActivity.getAmpActivityId());
		a.setName(ampActivity.getName());
		String description = null;
		//do not return description yet since they are stored 
		//in dg_message table an would need to do a query for each
		//row
//		if (ampActivity.getDescription() != null) {
//			if (ampActivity.getDescription().length() > 50) {
//				description = StringUtils
//						.left(ampActivity.getDescription(), 50) + "...";
//			} else {
//				description = ampActivity.getDescription();
//			}
//
//		}
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
			
			//commented out since for now the details are not needed in case they are needed
			//just uncoment both fors
//			for(FundingDetail fd:fundingDigest.getCommitmentsDetails()){
//				a.addCommitments(fd.getTransactionAmount(), fd.getTransactionDate());	
//			}
//			for(FundingDetail fd:fundingDigest.getDisbursementsDetails()){
//				a.addDisbursment(fd.getTransactionAmount(), fd.getTransactionDate());	
//			}		
		}
		return a;
	}
	public static List<JsonBean> getActivitiesMondrian(JsonBean filter) {
		List<JsonBean> activities=new ArrayList<JsonBean>();
		GeneratedReport report = getActivitiesList( filter);

		List<ReportArea> ll = report.reportContents.getChildren();
		for (ReportArea reportArea : ll) {
			JsonBean activity = new JsonBean();
			Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
			Set<ReportOutputColumn> col = row.keySet();
			for (ReportOutputColumn reportOutputColumn : col) {
				activity.set(reportOutputColumn.columnName,row.get(reportOutputColumn).value);
				if (reportOutputColumn.columnName.equals("AMP ID")) {
					System.out.println("* " + row.get(reportOutputColumn).value);
				}
			}
			activities.add(activity);
		}

		return activities;
	}
	public static GeneratedReport getActivitiesList(JsonBean filter) {
		String name= "ActivityList";
		boolean doTotals=false;
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name);
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));
		spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));

		spec.setCalculateColumnTotals(doTotals);
		spec.setCalculateRowTotals(doTotals);
		
		MondrianReportFilters
		filterRules = new MondrianReportFilters();
		List<String>mf=new ArrayList<String>();
		mf.add("5530");
		mf.add("5575");
		mf.add("3256");
		mf.add("5576");
		filterRules.addFilterRule(MondrianReportUtils.getColumn(ColumnConstants.ACTIVITY_ID, ReportEntityType.ENTITY_TYPE_ACTIVITY), 
				new FilterRule(mf, true, true)); 
		spec.setFilters(filterRules);
		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		GeneratedReport report = null;		
		try {
			report = generator.executeReport(spec);
		}catch(AMPException e ){
		
		}
		return report;
	}


}
