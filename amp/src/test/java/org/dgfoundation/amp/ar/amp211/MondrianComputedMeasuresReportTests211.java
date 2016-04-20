/**
 * 
 */
package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class MondrianComputedMeasuresReportTests211 extends ReportingTestCase {
	
	public MondrianComputedMeasuresReportTests211() {
		super("computed measures mondrian tests 2.11");
	}
	
	@Test
	public void test_AMP_19721_Selected_Year_Planned_Disbursements() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2014-March-Planned Disbursements", "300", "2014-November-Planned Disbursements", "90 000", "Total Measures-Planned Disbursements", "90 300", "Total Measures-Selected Year Planned Disbursements", "90 300")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with capital spending", "2014-March-Planned Disbursements", "", "2014-November-Planned Disbursements", "90 000", "Total Measures-Planned Disbursements", "90 000", "Total Measures-Selected Year Planned Disbursements", "90 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with planned disbursements", "2014-March-Planned Disbursements", "300", "2014-November-Planned Disbursements", "", "Total Measures-Planned Disbursements", "300", "Total Measures-Selected Year Planned Disbursements", "300")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "activity with capital spending", "Activity with planned disbursements");
		runMondrianTestCase("AMP-19721-Selected-Year-Planned-Disbursements",
				activities,
				correctReport,
				"en");
	}
	
	public void test_AMP_19723_ExecutionRateMergeNonHierarchical() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
			    .withContents("Primary Sector", "Report Totals", "Project Title", "", "Region", "", "2014-Actual Disbursements", "185 200", "2014-Planned Disbursements", "146 300", "2015-Actual Disbursements", "35 570", "2015-Planned Disbursements", "36 500", "Total Measures-Actual Disbursements", "220 770", "Total Measures-Planned Disbursements", "182 800", "Total Measures-Execution Rate", "")
			    .withChildren(
			      new ReportAreaForTests()
			          .withContents("Primary Sector", "110 - EDUCATION Totals", "Project Title", "", "Region", "", "2014-Actual Disbursements", "165 000", "2014-Planned Disbursements", "146 000", "2015-Actual Disbursements", "35 000", "2015-Planned Disbursements", "36 000", "Total Measures-Actual Disbursements", "200 000", "Total Measures-Planned Disbursements", "182 000", "Total Measures-Execution Rate", "")
			      .withChildren(
			        new ReportAreaForTests()      .withContents("Primary Sector", "110 - EDUCATION", "Project Title", "activity with capital spending", "Region", "Chisinau County", "2014-Actual Disbursements", "80 000", "2014-Planned Disbursements", "90 000", "2015-Actual Disbursements", "", "2015-Planned Disbursements", "", "Total Measures-Actual Disbursements", "80 000", "Total Measures-Planned Disbursements", "90 000", "Total Measures-Execution Rate", "88,89"),
			        new ReportAreaForTests()      .withContents("Primary Sector", "", "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "2014-Actual Disbursements", "30 000", "2014-Planned Disbursements", "", "2015-Actual Disbursements", "", "2015-Planned Disbursements", "", "Total Measures-Actual Disbursements", "30 000", "Total Measures-Planned Disbursements", "0", "Total Measures-Execution Rate", "0"),
			        new ReportAreaForTests()      .withContents("Primary Sector", "", "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "2014-Actual Disbursements", "55 000", "2014-Planned Disbursements", "56 000", "2015-Actual Disbursements", "35 000", "2015-Planned Disbursements", "36 000", "Total Measures-Actual Disbursements", "90 000", "Total Measures-Planned Disbursements", "92 000", "Total Measures-Execution Rate", "97,83")    ),
			      new ReportAreaForTests()
			          .withContents("Primary Sector", "112 - BASIC EDUCATION Totals", "Project Title", "", "Region", "", "2014-Actual Disbursements", "5 200", "2014-Planned Disbursements", "300", "2015-Actual Disbursements", "570", "2015-Planned Disbursements", "500", "Total Measures-Actual Disbursements", "5 770", "Total Measures-Planned Disbursements", "800", "Total Measures-Execution Rate", "")
			      .withChildren(
			        new ReportAreaForTests()      .withContents("Primary Sector", "112 - BASIC EDUCATION", "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "2014-Actual Disbursements", "5 000", "2014-Planned Disbursements", "", "2015-Actual Disbursements", "", "2015-Planned Disbursements", "", "Total Measures-Actual Disbursements", "5 000", "Total Measures-Planned Disbursements", "0", "Total Measures-Execution Rate", "0"),
			        new ReportAreaForTests()      .withContents("Primary Sector", "", "Project Title", "Activity with planned disbursements", "Region", "", "2014-Actual Disbursements", "200", "2014-Planned Disbursements", "300", "2015-Actual Disbursements", "570", "2015-Planned Disbursements", "500", "Total Measures-Actual Disbursements", "770", "Total Measures-Planned Disbursements", "800", "Total Measures-Execution Rate", "96,25")    ),
			      new ReportAreaForTests()    .withContents("Primary Sector", "120 - HEALTH Totals", "Project Title", "", "Region", "", "2014-Actual Disbursements", "15 000", "2014-Planned Disbursements", "0", "2015-Actual Disbursements", "0", "2015-Planned Disbursements", "0", "Total Measures-Actual Disbursements", "15 000", "Total Measures-Planned Disbursements", "0", "Total Measures-Execution Rate", "")
			      .withChildren(
			        new ReportAreaForTests()      .withContents("Primary Sector", "120 - HEALTH", "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "2014-Actual Disbursements", "15 000", "2014-Planned Disbursements", "", "2015-Actual Disbursements", "", "2015-Planned Disbursements", "", "Total Measures-Actual Disbursements", "15 000", "Total Measures-Planned Disbursements", "0", "Total Measures-Execution Rate", "0")    )  );
		
		List<String> activities = Arrays.asList("execution rate activity", "activity with capital spending", "Activity with planned disbursements", "activity with contracting agency");
		runMondrianTestCase("AMP-19723-execution-rate-merging-columns",
				activities,
				correctReport,
				"en");
	}

}
