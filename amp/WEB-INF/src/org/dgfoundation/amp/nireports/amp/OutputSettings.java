/**
 * 
 */
package org.dgfoundation.amp.nireports.amp;

import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportWarning;

/**
 * Stores custom output settings and information
 * 
 * @author Nadejda Mandrescu
 */
public class OutputSettings {
	
	/** columns for which to provide ids/values map (expensive operation) */
	public final HashSet<String> idsValuesColumns;
	//protected Optional<Set<ReportWarning>> reportWarnings = Optional.ofNullable(null);  TODO: why are ReportWarnings in OutputSettings instead of GeneratedReport?
	
	public OutputSettings(Set<String> idsValuesColumns) {
		this.idsValuesColumns = idsValuesColumns == null ? new HashSet<>() : new HashSet<>(idsValuesColumns);
	}

//	/**
//	 * @return the reportWarnings
//	 */
//	public Optional<Set<ReportWarning>> getReportWarnings() {
//		return reportWarnings;
//	}
//
//	/**
//	 * @param reportWarnings the reportWarnings to set
//	 */
//	public void setReportWarnings(Set<ReportWarning> reportWarnings) {
//		this.reportWarnings = Optional.ofNullable(reportWarnings);
//	}
}
