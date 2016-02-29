/**
 * 
 */
package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;

import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class PledgeReportsTests211 extends ReportingTestCase {
	
	public PledgeReportsTests211() {
		super("pledge reports mondrian tests 211");
	}
	
	
	@Test
	public void testAMP_21336_detail_dates_and_contacts() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Detail Date Range", "", "Pledges Detail End Date", "", "Pledges Detail Start Date", "", "Pledge Contact 1 - Address", "", "Pledge Contact 1 - Alternate Contact", "", "Pledge Contact 1 - Email", "", "Pledge Contact 1 - Ministry", "", "Pledge Contact 1 - Name", "", "Pledge Contact 1 - Telephone", "", "Pledge Contact 1 - Title", "", "Pledge Contact 2 - Title", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 186 878,1", "Total Measures-Actual Pledge", "12 966 059,62")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Detail Date Range", "01/04/2014 - 16/04/2014, 06/06/2012 - 04/04/2014, 18/04/2014 - 24/04/2014", "Pledges Detail End Date", "04/04/2014, 16/04/2014, 24/04/2014", "Pledges Detail Start Date", "01/04/2014, 06/06/2012, 18/04/2014", "Pledge Contact 1 - Address", "Some Contact Address", "Pledge Contact 1 - Alternate Contact", "Alternative Guy first contact", "Pledge Contact 1 - Email", "contact@amp.org", "Pledge Contact 1 - Ministry", "Ministry of Pledge affairs", "Pledge Contact 1 - Name", "A Contact name", "Pledge Contact 1 - Telephone", "8976535", "Pledge Contact 1 - Title", "Dr", "Pledge Contact 2 - Title", "alternate contact title", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35"),
	      new ReportAreaForTests().withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges Detail Date Range", "02/01/1998 - Undefined", "Pledges Detail End Date", "", "Pledges Detail Start Date", "02/01/1998", "Pledge Contact 1 - Address", "", "Pledge Contact 1 - Alternate Contact", "", "Pledge Contact 1 - Email", "virvan@gmail.com", "Pledge Contact 1 - Ministry", "Ministry of Pledges", "Pledge Contact 1 - Name", "Vanessa Goas", "Pledge Contact 1 - Telephone", "", "Pledge Contact 1 - Title", "Dr", "Pledge Contact 2 - Title", "", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests().withContents("Pledges Titles", "free text name 2", "Pledges Detail Date Range", "02/03/2012 - 03/03/2015", "Pledges Detail End Date", "03/03/2015", "Pledges Detail Start Date", "02/03/2012", "Pledge Contact 1 - Address", "", "Pledge Contact 1 - Alternate Contact", "", "Pledge Contact 1 - Email", "", "Pledge Contact 1 - Ministry", "", "Pledge Contact 1 - Name", "", "Pledge Contact 1 - Telephone", "", "Pledge Contact 1 - Title", "", "Pledge Contact 2 - Title", "", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	      new ReportAreaForTests().withContents("Pledges Titles", "Heavily used pledge", "Pledges Detail Date Range", "01/02/2013 - 29/04/2014, 08/04/2014 - 11/02/2015", "Pledges Detail End Date", "11/02/2015, 29/04/2014", "Pledges Detail Start Date", "01/02/2013, 08/04/2014", "Pledge Contact 1 - Address", "", "Pledge Contact 1 - Alternate Contact", "", "Pledge Contact 1 - Email", "", "Pledge Contact 1 - Ministry", "", "Pledge Contact 1 - Name", "", "Pledge Contact 1 - Telephone", "", "Pledge Contact 1 - Title", "", "Pledge Contact 2 - Title", "", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000"));
		
		runMondrianTestCase("AMP-21336-pledge-details-contacts-in-mondrian",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names 
			correctReportEn, "en");
	}

}
