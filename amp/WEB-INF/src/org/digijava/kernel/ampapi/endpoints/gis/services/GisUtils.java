package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.gis.PerformanceFilterParameters;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

public final class GisUtils {
    
    private GisUtils() {

    }
    
    public static void configurePerformanceFilter(PerformanceFilterParameters config, AmpReportFilters filterRules) {
        if (config.getShowActivitiewWithPerformanceIssues() != null) {
            boolean showActivitiewWithPerformanceIssues = config.getShowActivitiewWithPerformanceIssues();
            List<String> actIds = getActivitiesWithPerformanceIssues();
            filterRules.addFilterRule(new ReportColumn(ColumnConstants.ACTIVITY_ID), 
                    new FilterRule(actIds, showActivitiewWithPerformanceIssues)); 
        } 
    }
    
    public static List<String> getActivitiesWithPerformanceIssues() {
        Session session = PersistenceManager.getSession();
        
        final String performanceIssuesQuery = "SELECT amp_activity_id FROM v_performance_alert_level";
        
        List<String> actIds = session.createSQLQuery(performanceIssuesQuery)
                            .addScalar("amp_activity_id", StandardBasicTypes.STRING)
                             .list();
        
        return actIds;
    }
    
}
