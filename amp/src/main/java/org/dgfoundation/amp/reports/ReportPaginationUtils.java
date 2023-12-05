/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;

/**
 * Reports pagination utility methods
 * @author Nadejda Mandrescu
 */
public class ReportPaginationUtils {
    private static Logger logger = Logger.getLogger(ReportPaginationUtils.class);
    
    /**
     * @return maximum records number per page, excluding rows that display sub-totals and totals
     */
    public static int getRecordsNumberPerPage() {
        AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();             
        
        int recordsPerPage = 100;
        
        if (ampAppSettings != null){
            if (ampAppSettings.getDefaultRecordsPerPage() != 0) {
                recordsPerPage = ampAppSettings.getDefaultRecordsPerPage();
            }else{
                recordsPerPage = Integer.MAX_VALUE;
            }
        }
        
        return recordsPerPage;
    }
    
    /**
     * Adds to the cache report result records
     * @param reportId - report id for the provided results  
     * @param generatedReport - report result
     * @return list of records, excluding totals
     */
    public final static CachedReportData cacheReportData(String reportToken, GeneratedReport generatedReport) {
        if (generatedReport == null) return null;
        //adding
        CachedReportData crd = new CachedReportData(generatedReport);
        ReportCacher.addReportData(reportToken, crd);
        return crd;
    }
}
