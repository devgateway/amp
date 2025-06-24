/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.pagination.PaginatedReport;

/**
 * Report Data to be stored in the cache  
 * 
 * @author Nadejda Mandrescu
 */
public class CachedReportData {
    
    /** Generated report data */
    public final GeneratedReport report;
    
    public final PaginatedReport paginationInfo; 
    
    public CachedReportData(GeneratedReport report) {
        this.report = report;
        this.paginationInfo = new PaginatedReport(report.reportContents);
    }
    
}
