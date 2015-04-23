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
 * Tests report settings
 * @author Nadejda Mandrescu
 */
public class SettingsMondrianReportTests extends MondrianReportsTestCase {
	
	public SettingsMondrianReportTests() {
		super("settings mondrian tests");
	}
	
	@Test
	public void test_AMP_19554_AmountsInThousands() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2013-Actual Commitments", "1 700", "2014-Actual Commitments", "3 379,67", "Total Measures-Actual Commitments", "5 079,67")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged education activity 1", "2013-Actual Commitments", "1 700", "2014-Actual Commitments", "3 300", "Total Measures-Actual Commitments", "5 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with capital spending", "2013-Actual Commitments", "", "2014-Actual Commitments", "79,67", "Total Measures-Actual Commitments", "79,67")  );
		
		List<String> activities = Arrays.asList("pledged education activity 1", "activity with capital spending");
		runMondrianTestCase("AMP-19554-Amounts-in-Thousands",
				activities,
				correctReport,
				"en");
	}
}
