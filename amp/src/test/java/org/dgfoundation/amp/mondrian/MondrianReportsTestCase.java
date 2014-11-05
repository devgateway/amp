package org.dgfoundation.amp.mondrian;

import java.util.List;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpReports;
import org.dgfoundation.amp.newreports.ReportAreaImpl;

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
	protected void runReportTest(String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale) {
		GeneratedReport report = runReportOn(reportName, locale, activities);
		String error = compareOutputs(correctResult, report);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}
	
	protected void runReportTest(String testName, ReportSpecification reportSpec, List<String> activities, GeneratedReport correctResult, String locale) {
		GeneratedReport report = runReportOn(reportSpec, locale, activities);
		String error = compareOutputs(correctResult, report);
		assertNull(String.format("test %s, report %s: %s", testName, reportSpec, error), error);
	}
	
	protected GeneratedReport runReportOn(String reportName, String locale, List<String> activities) {
		AmpReports report = ReportTestingUtils.loadReportByName(reportName);
		ReportSpecification spec = ReportsUtil.getReport(report.getAmpReportId());
		return runReportOn(spec, locale, activities);
	}
	
	protected GeneratedReport runReportOn(ReportSpecification spec, String locale, List<String> activities) {
		try {
			ReportEnvironment env = new ReportEnvironment(locale, new ActivityIdsFetcher(activities));
			MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, env);
			GeneratedReport res = generator.executeReport(spec);
			return res;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//protected void runRe
	public static String compareOutputs(GeneratedReport correct, GeneratedReport output) {
		return null;
	}
	
	public static String describeReportOutputInCode(GeneratedReport gr) {
		ReportAreaForTests rai = (ReportAreaForTests) gr.reportContents;
		return rai.describeInCode(1);
	}
}

