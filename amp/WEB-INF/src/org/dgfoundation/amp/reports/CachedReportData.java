/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * Report Data to be stored in the cache  
 * 
 * @author Nadejda Mandrescu
 */
public class CachedReportData {
	/** Generated report data */
	public final GeneratedReport report;
	/** Leaf report areas list */
	public final ReportAreaMultiLinked[] areas;
	
	public CachedReportData(GeneratedReport report, ReportAreaMultiLinked[] areas) {
		this.report = report;
		this.areas = areas;
	}
	
}
