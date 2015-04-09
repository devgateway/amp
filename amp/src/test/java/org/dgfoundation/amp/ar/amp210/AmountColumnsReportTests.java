/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

/**
 * Stores the set of tests that checks general specifics for amount columns
 * @author Nadejda Mandrescu
 */
public class AmountColumnsReportTests extends MondrianReportsTestCase {
		
	public AmountColumnsReportTests() {
		super("amount columns mondrian tests");
	}
	
	@Test
	public void test_AMP_19724_PPC_Missing_Measure_Amounts() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Proposed Project Amount", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "123 321", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2014-Actual Commitments", "15 000", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "228 231", "Total Measures-Actual Disbursements", "123 321")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "Proposed Project Amount", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2014-Actual Commitments", "15 000", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "15 000", "Total Measures-Actual Disbursements", "0")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "Proposed Project Cost 2 - EUR", "activity with tertiary_program");
		runMondrianTestCase("Proposed-cost-USD",
				activities,
				correctReport,
				"en");
	}
}
