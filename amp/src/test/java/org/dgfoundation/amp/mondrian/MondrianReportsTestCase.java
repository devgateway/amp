package org.dgfoundation.amp.mondrian;


import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.module.aim.dbentity.AmpReports;

public abstract class MondrianReportsTestCase extends AmpTestCase
{
	public MondrianReportsTestCase(String name) {
		super(name);
	}
	
	/**
	 * runs a single report test and compares the result with the expected cor 
	 * @param testName - test name to be displayed in case of error
	 * @param reportName - the name of the report (AmpReports entry in the tests database) which should be run
	 * @param activities - the names of the activities which should be presented via a dummy WorkspaceFilter to the report. Put ReportTestingUtils.NULL_PLACEHOLDER if NO WorkspaceFilter should be put (e.g. let the report see all the activities) 
	 * @param correctResult - a model (sketch) of the expected result
	 * @param modifier - the modifier (might be null) to postprocess AmpReports and AmpARFilter after being loaded from the DB
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GeneratedReport correctResult, String locale)
	{
		GeneratedReport report = runReportOn(reportName, locale, activities);
		////System.out.println(ReportTestingUtils.describeReportInCode(report, 1, true));
//		checkThatAllCRDsHaveIdenticalReportHeadingsLayoutData(report);
		String error = compareOutputs(correctResult, report);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}
	
	protected GeneratedReport runReportOn(String reportName, String locale, String[] activities) {
		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		
		//ReportContextData.createWithId(report.getId().toString(), true);
			
		ReportEnvironment env = new ReportEnvironment(locale, new ActivityIdsFetcher(activities));
				
		return null;
	}
	
	//protected void runRe
	public static String compareOutputs(GeneratedReport correct, GeneratedReport output) {
		return null;
	}
}

