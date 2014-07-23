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
	 * @param report - {@link ReportSpecification}
	 * @param reportAreaType - implementing class of {@link ReportArea} interface
	 * @throws AMPException
	 */
	public GeneratedReport executeReport(ReportSpecification report, Class<? extends ReportArea> reportAreaType) throws AMPException;
}
