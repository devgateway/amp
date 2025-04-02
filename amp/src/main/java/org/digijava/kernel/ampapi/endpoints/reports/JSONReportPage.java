/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.reports;

import org.dgfoundation.amp.newreports.ReportArea;

/**
 * Stores one page result 
 * @author Nadejda Mandrescu
 */
public class JSONReportPage {
    public final ReportArea pageArea;
    public final Integer recordsPerPage;
    public final Integer currentPageNumber;
    public final Integer totalPageCount;
    public final Integer totalRecords;
    
    public JSONReportPage(ReportArea pageArea, Integer recordsPerPage, 
            Integer currentPageNumber, Integer totalPageCount, Integer totalRecords) {
        this.pageArea = pageArea;
        this.recordsPerPage = recordsPerPage;
        this.currentPageNumber = currentPageNumber;
        this.totalPageCount = totalPageCount;
        this.totalRecords = totalRecords;
    }
}
