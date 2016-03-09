/**
 * 
 */
package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class MondrianComputedMeasuresReportTests211 extends MondrianReportsTestCase {
	
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
	
	public void test_UncommittedBalanceTotals() {
		ReportAreaForTests correctReport =  new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Project Title", "", "2014-Actual Commitments", "75 000", "Total Measures-Actual Commitments", "75 000", "Total Measures-Uncommitted Balance", "4 384 510,47")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Region", "Anenii Noi County Totals", "Project Title", "", "2014-Actual Commitments", "37 500", "Total Measures-Actual Commitments", "37 500", "Total Measures-Uncommitted Balance", "3 422 010,47")
	      .withChildren(
	        new ReportAreaForTests()      .withContents("Region", "Anenii Noi County", "Project Title", "Proposed Project Cost 2 - EUR", "2014-Actual Commitments", "", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "3 399 510,47"),
	        new ReportAreaForTests()      .withContents("Region", "", "Project Title", "SubNational no percentages", "2014-Actual Commitments", "37 500", "Total Measures-Actual Commitments", "37 500", "Total Measures-Uncommitted Balance", "22 500")    ),
	      new ReportAreaForTests()    .withContents("Region", "Balti County Totals", "Project Title", "", "2014-Actual Commitments", "37 500", "Total Measures-Actual Commitments", "37 500", "Total Measures-Uncommitted Balance", "22 500")
	      .withChildren(
	        new ReportAreaForTests()      .withContents("Region", "Balti County", "Project Title", "SubNational no percentages", "2014-Actual Commitments", "37 500", "Total Measures-Actual Commitments", "37 500", "Total Measures-Uncommitted Balance", "22 500")    ),
	      new ReportAreaForTests()    .withContents("Region", "Drochia County Totals", "Project Title", "", "2014-Actual Commitments", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "1 000 000")
	      .withChildren(
	        new ReportAreaForTests()      .withContents("Region", "Drochia County", "Project Title", "Proposed Project Cost 1 - USD", "2014-Actual Commitments", "", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "1 000 000")    )  );
		
		List<String> activities = Arrays.asList("Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "SubNational no percentages", "Project with documents");
		runMondrianTestCase("AMP-20236-Uncommitted-Balance-Totals",
				activities,
				correctReport,
				"en");
	}
	
	public void test_AMP_19710_UncommittedBalanceYearRange() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Proposed Project Amount", "4 459 510,47", "Total Measures-Actual Commitments", "75 000", "Total Measures-Uncommitted Balance", "4 384 510,47")
	    .withChildren(
	      new ReportAreaForTests()    .withContents("Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "1 000 000"),
	      new ReportAreaForTests()    .withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "3 399 510,47"),
	      new ReportAreaForTests()    .withContents("Project Title", "Project with documents", "Proposed Project Amount", "", "Total Measures-Actual Commitments", "0", "Total Measures-Uncommitted Balance", "0"),
	      new ReportAreaForTests()    .withContents("Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Total Measures-Actual Commitments", "75 000", "Total Measures-Uncommitted Balance", "-15 000")  );
		
		List<String> activities = Arrays.asList("Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "SubNational no percentages", "Project with documents");
		runMondrianTestCase("AMP-19710-Uncommitted-Balance-ReducedYearRange",
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
