package org.dgfoundation.amp.newreports;

/**
 * a class which can execute a report
 * @author Dolghier Constantin
 *
 */
public interface ReportExecutor {
	public GeneratedReport executeReport(ReportSpecification report);
}
