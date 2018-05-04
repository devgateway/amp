package org.dgfoundation.amp.testutils;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.mock.MockHttpServletRequest;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * various static methods and constants for the report testing infrastructure
 * @author Dolghier Constantin
 *
 */
public class ReportTestingUtils 
{
    /**
     * "null" placeholder in "Object..." or "String..."-taking functions in the testing code (as putting null there is not adviseable)
     */
    public final static String NULL_PLACEHOLDER = "###null###";
    public final static String MUST_BE_EMPTY = "###must-be-empty###";
    
    /**
     * runs the report "reportName" from the database; the report will be presented a dummy WorkspaceFilter 
     * which will only allow amp_activity_id's which have their names equal to one specified 
     * @param reportName - the name of the report to run. Will run any if more with the same name exist in the database
     * @param activitiyNames - the names of the activities which should be presented to the report (through WorkspaceFilter). If null, WorkspaceFilter will not filter out any
     * @return
     */
    public static ImmutablePair<GroupReportData, String> runReportOn(String reportName, AmpReportModifier modifier, String... activityNames)
    {       
//      Session hibSession = PersistenceManager.getSession();
        
        AmpReports report = loadReportByName(reportName);

        MockHttpServletRequest mockRequest = StandaloneAMPInitializer.populateMockRequest();
        mockRequest.setAttribute("ampReportId", report.getId().toString());
        ReportContextData.createWithId(report.getId().toString(), true);

        
        AmpARFilter filter = FilterUtil.buildFilter(report, report.getAmpReportId());
        if (activityNames == null || (activityNames.length == 0))           
            filter.setOverridingTeamFilter(new NonFilteringTeamFilter());
        else
            filter.setOverridingTeamFilter(new NameFilteringTeamFilter(activityNames));
        
        if (modifier != null)
            modifier.modifyAmpReportSettings(report, filter);
        
        ReportContextData.getFromRequest(mockRequest, false).setFilter(filter);
        AmpReportGenerator arg = new MyAmpReportGenerator(report, filter, true, modifier);
        arg.setCleanupMetadata(true);
        arg.generate();
        
        GroupReportData result = arg.getReport();
        return new ImmutablePair<>(result, arg.describeReportInCode(result, 0, true));
    }
    
    /**
     * Retrieves the report filter 
     * @param report 
     * @return 
     */
    public static AmpARFilter getReportFilter(AmpReports report) {
        MockHttpServletRequest mockRequest = StandaloneAMPInitializer.populateMockRequest();
        mockRequest.setAttribute("ampReportId", report.getId().toString());
        ReportContextData.createWithId(report.getId().toString(), true);
        return FilterUtil.buildFilter(report, report.getAmpReportId());
    }
    
    /**
     * loads a report with a given name. If multiple reports with the name exist, it is not defined which of them will be chosen. Given that this will only be run on a testcases database, it is ok
     * @param reportName
     * @return
     */
    public static AmpReports loadReportByName(String reportName)
    {
        Session session = null;
        Query qry = null;
        List<AmpReports> reports = new ArrayList<AmpReports>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from " + AmpReports.class.getName()
                    + " r WHERE " + AmpReports.hqlStringForName("r") + "=:reportname";
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
    
    /**
     * returns the name of an activity looked up by id
     * @param activityId
     * @return
     */
    public static String getActivityName(Long activityId)
    {
        Session session = null;
        Query qry = null;
        List<String> activities = new ArrayList<String>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select " + AmpActivityVersion.hqlStringForName("r") + "FROM " + AmpActivityVersion.class.getName()
                    + " r WHERE r.ampActivityId=:activityId";
            qry = session.createQuery(queryString);
            qry.setLong("activityId", activityId);
            activities = qry.list();            
            if (activities.size() >= 1)
                return activities.get(0);           
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        throw new RuntimeException("no activity with the given id " + activityId + " exists");      
    }
    
    /**
     * returns the name of an activity looked up by id
     * @param activityId
     * @return
     */
    public static String getActivityName_notVersion(Long activityId)
    {
        AmpActivity aa = (AmpActivity) PersistenceManager.getSession().get(AmpActivity.class, activityId);
        if (aa == null)
            throw new RuntimeException("no activity with the given id " + activityId + " exists");
        
        return aa.getName();                
    }
    
    public static AmpActivityVersion loadActivityByName(String actName)
    {
        try
        {
            String queryString = "select act from " + AmpActivity.class.getName() + " act WHERE " + AmpActivityVersion.hqlStringForName("act") + "=:activityName";
            AmpActivityVersion act = (AmpActivityVersion) PersistenceManager.getRequestDBSession().createQuery(queryString).setString("activityName", actName).list().get(0);
            return act;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static Long getActivityIdByName(String actName)
    {
        try
        {
            String queryString = "select act.ampActivityId from " + AmpActivity.class.getName() + " act WHERE " + AmpActivityVersion.hqlStringForName("act") + "=:activityName";
            List<Long> ids = PersistenceManager.getRequestDBSession().createQuery(queryString).setString("activityName", actName).list();
            
            if (ids.isEmpty())
                throw new RuntimeException("no activities with name " + actName + " found");
            if (ids.size() > 1)
                throw new RuntimeException("multiple activities with name " + actName + " found");
        
            return ids.get(0);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
}
