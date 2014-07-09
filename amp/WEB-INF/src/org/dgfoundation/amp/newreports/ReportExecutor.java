package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.error.AMPException;

/**
 * a class which can execute a report
 * @author Dolghier Constantin
 *
 */
public interface ReportExecutor {
	/**
	 * Executes a report based on specifications provided
	 * @param report {@link ReportSpecification}
	 * @return {@link GeneratedReport} result
	 * @throws AMPException
	 */
	public GeneratedReport executeReport(ReportSpecification report) throws AMPException;
}
