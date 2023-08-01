package org.digijava.kernel.ampapi.endpoints.gis.services;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.gis.PerformanceFilterParameters;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.type.StandardBasicTypes;

import java.util.List;

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
        
        List<String> actIds = session.createNativeQuery(performanceIssuesQuery)
                            .addScalar("amp_activity_id", StandardBasicTypes.STRING)
                             .list();
        
        return actIds;
    }
    
    public static List<AmpLocator> getLocators() {
        Session session = PersistenceManager.getSession();
        List<AmpLocator> locators = session.createCriteria(AmpLocator.class).addOrder(Order.asc("id")).list();

        return locators;
    }

}
