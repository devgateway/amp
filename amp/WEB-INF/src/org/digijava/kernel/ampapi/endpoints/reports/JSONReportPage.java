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
	
	public JSONReportPage(ReportArea pageArea, Integer recordsPerPage, 
			Integer currentPageNumber, Integer totalPageCount) {
		this.pageArea = pageArea;
		this.recordsPerPage = recordsPerPage;
		this.currentPageNumber = currentPageNumber;
		this.totalPageCount = totalPageCount;
	}
}
