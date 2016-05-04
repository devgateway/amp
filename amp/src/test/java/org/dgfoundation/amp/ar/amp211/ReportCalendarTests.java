/**
 * 
 */
package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;

import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class ReportCalendarTests extends ReportingTestCase {
	
	public ReportCalendarTests() {
		super("Report Calendar conversion tests");
	}
	
	@Test
	public void test_AMP_21466_default_calendar_conversion() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "1998-Actual Commitments", "96 840,58", "2005-Actual Commitments", "666 777", "2007-Actual Commitments", "65 760,63", "Total Measures-Actual Commitments", "829 378,21")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "1998-Actual Commitments", "", "2005-Actual Commitments", "666 777", "2007-Actual Commitments", "", "Total Measures-Actual Commitments", "666 777"),
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "1998-Actual Commitments", "", "2005-Actual Commitments", "", "2007-Actual Commitments", "65 760,63", "Total Measures-Actual Commitments", "65 760,63"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "1998-Actual Commitments", "96 840,58", "2005-Actual Commitments", "", "2007-Actual Commitments", "", "Total Measures-Actual Commitments", "96 840,58"));
		
		runMondrianTestCase("AMP-21466 Global Year Range Conversion",
			Arrays.asList("Test pledge 1", "ptc activity 1", "activity with contracting agency", "activity with capital spending"), 
			correctReportEn, "en");
	}
	
	@Test
	public void test_AMP_21466_only_1998_ethiopian_year() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "1998-Actual Commitments", "96 840,58", "Total Measures-Actual Commitments", "829 378,21")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "1998-Actual Commitments", "", "Total Measures-Actual Commitments", "666 777"),
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "1998-Actual Commitments", "", "Total Measures-Actual Commitments", "65 760,63"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "1998-Actual Commitments", "96 840,58", "Total Measures-Actual Commitments", "96 840,58"));
		
		runMondrianTestCase("AMP-21466 Show only 1998 Ethiopian Year",
			Arrays.asList("Test pledge 1", "ptc activity 1", "activity with contracting agency", "activity with capital spending"), 
			correctReportEn, "en");
	}
	
	@Test
    public void test_AMP_22733_gregorian_fiscal() {
	    ReportAreaForTests correctReportEn = new ReportAreaForTests()
	            .withContents("Project Title", "Report Totals", "Fiscal Year 2013 - 2014-February-Actual Commitments", "75.000", "Fiscal Year 2014 - 2015-March-Actual Commitments", "32.000", "Fiscal Year 2014 - 2015-January-Actual Commitments", "45.000", "Fiscal Year 2015 - 2016-March-Actual Commitments", "456.789", "Total Measures-Actual Commitments", "608.789")
	            .withChildren(
	              new ReportAreaForTests()    .withContents("Project Title", "SubNational no percentages", "Fiscal Year 2013 - 2014-February-Actual Commitments", "75.000", "Fiscal Year 2014 - 2015-March-Actual Commitments", "", "Fiscal Year 2014 - 2015-January-Actual Commitments", "", "Fiscal Year 2015 - 2016-March-Actual Commitments", "", "Total Measures-Actual Commitments", "75.000"),
	              new ReportAreaForTests()    .withContents("Project Title", "activity with primary_program", "Fiscal Year 2013 - 2014-February-Actual Commitments", "", "Fiscal Year 2014 - 2015-March-Actual Commitments", "32.000", "Fiscal Year 2014 - 2015-January-Actual Commitments", "", "Fiscal Year 2015 - 2016-March-Actual Commitments", "", "Total Measures-Actual Commitments", "32.000"),
	              new ReportAreaForTests()    .withContents("Project Title", "Unvalidated activity", "Fiscal Year 2013 - 2014-February-Actual Commitments", "", "Fiscal Year 2014 - 2015-March-Actual Commitments", "", "Fiscal Year 2014 - 2015-January-Actual Commitments", "45.000", "Fiscal Year 2015 - 2016-March-Actual Commitments", "", "Total Measures-Actual Commitments", "45.000"),
	              new ReportAreaForTests()    .withContents("Project Title", "activity 1 with agreement", "Fiscal Year 2013 - 2014-February-Actual Commitments", "", "Fiscal Year 2014 - 2015-March-Actual Commitments", "", "Fiscal Year 2014 - 2015-January-Actual Commitments", "", "Fiscal Year 2015 - 2016-March-Actual Commitments", "456.789", "Total Measures-Actual Commitments", "456.789")  );
	    
	    runMondrianTestCase("AMP-22733 Monthly Gergorian Fiscal March",
	            Arrays.asList("activity with primary_program", "SubNational no percentages", "Unvalidated activity", "activity 1 with agreement"), 
	            correctReportEn, "en");
	    
	}
}
