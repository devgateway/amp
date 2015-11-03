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

}
