package org.dgfoundation.amp.testutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.StringGenerator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Query;
import org.hibernate.Session;

public class ReportTestingUtils 
{
	public final static String NULL_PLACEHOLDER = "###null###";
	
	/**
	 * runs the report "reportName" from the database; the report will be presented a dummy WorkspaceFilter 
	 * which will only allow amp_activity_id's which have their names equal to one specified 
	 * @param reportName - the name of the report to run. Will run any if more with the same name exist in the database
	 * @param activitiyNames - the names of the activities which should be presented to the report (through WorkspaceFilter). If null, WorkspaceFilter will not filter out any
	 * @return
	 */
	public static GroupReportData runReportOn(String reportName, String... activityNames)
	{		
		Session hibSession = PersistenceManager.getSession();
		
		AmpReports report = loadReportByName(reportName);;
		
		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		mockRequest.setAttribute("ampReportId", report.getId().toString());
		TLSUtils.populate(mockRequest);
		ReportContextData.createWithId(report.getId().toString(), true);

		
		AmpARFilter filter = FilterUtil.buildFilter(report, report.getAmpReportId());
		if (activityNames == null || (activityNames.length == 0))			
			filter.setOverridingTeamFilter(new NonFilteringTeamFilter());
		else
			filter.setOverridingTeamFilter(new NameFilteringTeamFilter(activityNames));
		
		AmpReportGenerator arg = new AmpReportGenerator(report, filter, true);
		arg.setCleanupMetadata(true);
		arg.generate();
		
		GroupReportData result = arg.getReport();
		return result;
	}
	
	public static AmpReports loadReportByName(String reportName)
	{
		Session session = null;
		Query qry = null;
		List<AmpReports> reports = new ArrayList<AmpReports>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r WHERE r.name=:reportname";
			qry = session.createQuery(queryString);
			qry.setString("reportname", reportName);
			reports = qry.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (reports.size() >= 1)
			return reports.get(0);
		throw new RuntimeException("no report with the given name " + reportName + " exists");
	}
	
	public static String getActivityName(Long activityId)
	{
		Session session = null;
		Query qry = null;
		List<AmpActivity> activities = new ArrayList<AmpActivity>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from " + AmpActivity.class.getName()
					+ " r WHERE r.ampActivityId=:activityId";
			qry = session.createQuery(queryString);
			qry.setLong("activityId", activityId);
			activities = qry.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (activities.size() >= 1)
			return activities.get(0).getName();
		throw new RuntimeException("no activity with the given id " + activityId + " exists");
		
	}
}
