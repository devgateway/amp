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

	public NewFeaturesMondrianReportTests() {
		super("amp 2.11 new features mondrian tests");
	}
	
	@Test
	public void testForecastExecutionRateYearly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "155 200", "2015-Actual Disbursements", "570", "Total Measures-Actual Disbursements", "190 770", "Total Measures-Forecast Execution Rate", "", "Total Measures-Execution Rate", "")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "80 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "80 000", "Total Measures-Forecast Execution Rate", "0", "Total Measures-Execution Rate", "88,89"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with planned disbursements", "2013-Actual Disbursements", "", "2014-Actual Disbursements", "200", "2015-Actual Disbursements", "570", "Total Measures-Actual Disbursements", "770", "Total Measures-Forecast Execution Rate", "0", "Total Measures-Execution Rate", "96,25"),
	      new ReportAreaForTests().withContents("Project Title", "activity with pipeline MTEFs and act. disb", "2013-Actual Disbursements", "35 000", "2014-Actual Disbursements", "75 000", "2015-Actual Disbursements", "", "Total Measures-Actual Disbursements", "110 000", "Total Measures-Forecast Execution Rate", "129,41", "Total Measures-Execution Rate", "0"));
		
		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate",						
			activities,
			correctReport,
			"en");
	}
	
	@Test
	public void testForecastExecutionRateTotalsOnly() {
		ReportAreaForTests correctReport =  new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Disbursements", "190 770", "Forecast Execution Rate", "129,41", "Execution Rate", "185,14")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "Actual Disbursements", "80 000", "Forecast Execution Rate", "0", "Execution Rate", "88,89"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with planned disbursements", "Actual Disbursements", "770", "Forecast Execution Rate", "0", "Execution Rate", "96,25"),
	      new ReportAreaForTests().withContents("Project Title", "activity with pipeline MTEFs and act. disb", "Actual Disbursements", "110 000", "Forecast Execution Rate", "129,41", "Execution Rate", "0"));

		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate-totals-only",						
			activities,
			correctReport,
			"en");
	}
	
	@Test
	public void testForecastExecutionRateTotalsOnlyWildDateFilters() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Total Measures-Actual Disbursements", "0", "Total Measures-Forecast Execution Rate", "", "Total Measures-Execution Rate", "", "Total Measures-Pipeline MTEF Projections", "0", "Total Measures-Projection MTEF Projections", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "Total Measures-Actual Disbursements", "0", "Total Measures-Forecast Execution Rate", "66,87", "Total Measures-Execution Rate", "0", "Total Measures-Pipeline MTEF Projections", "0", "Total Measures-Projection MTEF Projections", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with pipeline MTEFs and act. disb", "Total Measures-Actual Disbursements", "0", "Total Measures-Forecast Execution Rate", "129,41", "Total Measures-Execution Rate", "0", "Total Measures-Pipeline MTEF Projections", "0", "Total Measures-Projection MTEF Projections", "0"));

		runMondrianTestCase(
			"AMP-21240-forecast-execution-rate-date-filters-gone-wild",						
			activities2,
			correctReport,
			"en");
	}
}
