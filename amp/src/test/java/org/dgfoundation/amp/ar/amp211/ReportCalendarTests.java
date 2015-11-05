/**
 * 
 */
package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class ReportCalendarTests extends MondrianReportsTestCase {
	
	public ReportCalendarTests() {
		super("Report Calendar conversion tests");
	}
	
	@Test
	public void test_AMP_21466_default_calendar_conversion() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "1998-Actual Commitments", "96 840,58", "2005-Actual Commitments", "666 777", "2007-Actual Commitments", "79 670,69", "Total Measures-Actual Commitments", "843 288,27")
	    .withChildren(
	      new ReportAreaForTests()    .withContents("Project Title", "ptc activity 1", "1998-Actual Commitments", "", "2005-Actual Commitments", "666 777", "2007-Actual Commitments", "", "Total Measures-Actual Commitments", "666 777"),
	      new ReportAreaForTests()    .withContents("Project Title", "activity with capital spending", "1998-Actual Commitments", "", "2005-Actual Commitments", "", "2007-Actual Commitments", "79 670,69", "Total Measures-Actual Commitments", "79 670,69"),
	      new ReportAreaForTests()    .withContents("Project Title", "activity with contracting agency", "1998-Actual Commitments", "96 840,58", "2005-Actual Commitments", "", "2007-Actual Commitments", "", "Total Measures-Actual Commitments", "96 840,58")  );
		
		runMondrianTestCase("AMP-21466 Global Year Range Conversion",
			Arrays.asList("Test pledge 1", "ptc activity 1", "activity with contracting agency", "activity with capital spending"), 
			correctReportEn, "en");
	}
	
	@Test
	public void test_AMP_21466_only_1998_ethiopian_year() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "1998-Actual Commitments", "96 840,58", "Total Measures-Actual Commitments", "843 288,27")
	    .withChildren(
	      new ReportAreaForTests()    .withContents("Project Title", "ptc activity 1", "1998-Actual Commitments", "", "Total Measures-Actual Commitments", "666 777"),
	      new ReportAreaForTests()    .withContents("Project Title", "activity with capital spending", "1998-Actual Commitments", "", "Total Measures-Actual Commitments", "79 670,69"),
	      new ReportAreaForTests()    .withContents("Project Title", "activity with contracting agency", "1998-Actual Commitments", "96 840,58", "Total Measures-Actual Commitments", "96 840,58")  );
		
		runMondrianTestCase("AMP-21466 Show only 1998 Ethiopian Year",
			Arrays.asList("Test pledge 1", "ptc activity 1", "activity with contracting agency", "activity with capital spending"), 
			correctReportEn, "en");
	}
}
