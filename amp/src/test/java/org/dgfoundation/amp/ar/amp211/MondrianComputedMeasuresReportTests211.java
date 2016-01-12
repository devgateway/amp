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

}
