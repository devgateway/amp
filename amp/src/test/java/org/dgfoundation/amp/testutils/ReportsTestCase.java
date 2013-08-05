package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import junit.framework.TestCase;

public abstract class ReportsTestCase extends TestCase
{
	public ReportsTestCase(String name) {
		super(name);
	}
	
	/**
	 * runs a single report test and compares the result with the expected cor 
	 * @param testName - test name to be displayed in case of error
	 * @param reportName - the name of the report (AmpReports entry in the tests database) which should be run
	 * @param activities - the names of the activities which should be presented via a dummy WorkspaceFilter to the report. Put ReportTestingUtils.NULL_PLACEHOLDER if NO WorkspaceFilter should be put (e.g. let the report see all the activities) 
	 * @param correctResult - a model (sketch) of the expected result
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult)
	{
		GroupReportData report = ReportTestingUtils.runReportOn(reportName, activities);
		String error = correctResult.matches(report);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}

}
