package org.dgfoundation.amp.ar.amp211;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.junit.Test;

/**
 * testcases for directed Funding Flows
 * currently the testcases data is not prime-time-quality because we rushed the implementation
 * @author Constantin Dolghier
 *
 */
public class NewFeaturesMondrianReportTests extends MondrianReportsTestCase {

	public final static List<String> activities = Arrays.asList("activity with capital spending", "Activity with planned disbursements", "activity with pipeline MTEFs and act. disb");
	public final static List<String> activities2 = Arrays.asList("activity with capital spending", "Activity with planned disbursements", "activity with pipeline MTEFs and act. disb", "Test MTEF directed");
	public final static List<String> activities3 = Arrays.asList("activity with capital spending", "Activity with planned disbursements", "activity with pipeline MTEFs and act. disb", "Test MTEF directed", "activity with many MTEFs");

	public NewFeaturesMondrianReportTests() {
		super("amp 2.11 new features mondrian tests");
	}
	
	@Test
	public void testForecastExecutionRateYearly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Forecast Execution Rate", "", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "155 200", "2015-Actual Disbursements", "570", "Total Measures-Actual Disbursements", "190 770", "Total Measures-Execution Rate", "")
	    .withChildren(
	      new ReportAreaForTests()    .withContents("Project Title", "activity with capital spending", "Forecast Execution Rate", "", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "80 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "80 000", "Total Measures-Execution Rate", "88,89"),
	      new ReportAreaForTests()    .withContents("Project Title", "Activity with planned disbursements", "Forecast Execution Rate", "", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "200", "2015-Actual Disbursements", "570", "Total Measures-Actual Disbursements", "770", "Total Measures-Execution Rate", "96,25"),
	      new ReportAreaForTests()    .withContents("Project Title", "activity with pipeline MTEFs and act. disb", "Forecast Execution Rate", "129,41", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "75 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "110 000", "Total Measures-Execution Rate", "0"));
		
		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate",						
			activities,
			correctReport,
			"en");
	}
	
	@Test
	public void testForecastExecutionRateTotalsOnly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
			    .withContents("Project Title", "Report Totals", "Forecast Execution Rate", "", "Actual Disbursements", "190 770", "Execution Rate", "")
			    .withChildren(
			      new ReportAreaForTests()    .withContents("Project Title", "activity with capital spending", "Forecast Execution Rate", "", "Actual Disbursements", "80 000", "Execution Rate", "88,89"),
			      new ReportAreaForTests()    .withContents("Project Title", "Activity with planned disbursements", "Forecast Execution Rate", "", "Actual Disbursements", "770", "Execution Rate", "96,25"),
			      new ReportAreaForTests()    .withContents("Project Title", "activity with pipeline MTEFs and act. disb", "Forecast Execution Rate", "129,41", "Actual Disbursements", "110 000", "Execution Rate", "0")  );

		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate-totals-only",						
			activities,
			correctReport,
			"en");
	}
	
	@Test
	/**
	 * this testcase is BS - because of a Mondrian limitation (measures-disguised-as-columns obey the filters indirectly via the other measures of the report)
	 */
	public void testForecastExecutionRateTotalsOnlyWildDateFilters() {
		ReportAreaForTests correctReport = new ReportAreaForTests().withChildren();
		
		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate-date-filters-gone-wild",						
			activities2,
			correctReport,
			"en");
	}
	
	@Test
	public void testForecastExecutionRateProjPipeSameYearDifferentFundingItems() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Forecast Execution Rate", "", "2010-Actual Disbursements", "143 777", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "155 200", "2015-Actual Disbursements", "80 570", "Total Measures-Actual Disbursements", "414 547", "Total Measures-Execution Rate", "")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "Forecast Execution Rate", "66,87", "2010-Actual Disbursements", "143 777", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Execution Rate", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "Forecast Execution Rate", "", "2010-Actual Disbursements", "", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "80 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "80 000", "Total Measures-Execution Rate", "88,89"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with planned disbursements", "Forecast Execution Rate", "", "2010-Actual Disbursements", "", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "200", "2015-Actual Disbursements", "570", "Total Measures-Actual Disbursements", "770", "Total Measures-Execution Rate", "96,25"),
	      new ReportAreaForTests().withContents("Project Title", "activity with pipeline MTEFs and act. disb", "Forecast Execution Rate", "129,41", "2010-Actual Disbursements", "", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "75 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "110 000", "Total Measures-Execution Rate", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with many MTEFs", "Forecast Execution Rate", "98,77", "2010-Actual Disbursements", "", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "", "2015-Actual Disbursements", "80 000", "Total Measures-Actual Disbursements", "80 000", "Total Measures-Execution Rate", "0"));

		
		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate",						
			activities3, correctReport, "en");
	}
}
