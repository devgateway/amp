/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.PaginatedReportAreaForTests;
import org.dgfoundation.amp.reports.PartialReportArea;
import org.junit.Test;

/**
 * Tests for pagination that are built with tabs related rules 
 * 
 * @author Nadejda Mandrescu
 */
public class TabsPaginationTests extends ReportingTestCase {
	
	public TabsPaginationTests() {
		super("tabs pagination tests");
	}
	
	@Test
	public void testReportPagination() {
		PaginatedReportAreaForTests correctReportEn = new PaginatedReportAreaForTests()
	    .withContents("Region", "Report Totals", "Project Title", "", "Actual Commitments", "0", "Actual Disbursements", "688 777").withCounts(3, 5)
	    .withChildren(
	      new PaginatedReportAreaForTests()
	          .withContents("Region", "Anenii Noi County Totals", "Project Title", "", "Actual Commitments", "0", "Actual Disbursements", "688 777").withCounts(3, 5)
	      .withChildren(
	        new PaginatedReportAreaForTests()
	              .withContents("Region", "Anenii Noi County", "Project Title", "Proposed Project Cost 2 - EUR", "Actual Commitments", "0", "Actual Disbursements", "0").withCounts(1, 1),
	        new PaginatedReportAreaForTests()
	              .withContents("Region", "", "Project Title", "Test MTEF directed", "Actual Commitments", "0", "Actual Disbursements", "143 777").withCounts(1, 1),
	        new PaginatedReportAreaForTests()
	              .withContents("Region", "", "Project Title", "activity with components", "Actual Commitments", "0", "Actual Disbursements", "0").withCounts(1, 1)    )  ); 

		
		runMondrianTestCase("with-region-hier",  
						Arrays.asList("Proposed Project Cost 2 - EUR", "Test MTEF directed", "activity with components", "Eth Water", "mtef activity 2", "	Project with documents"),
						correctReportEn, 
						"en", 
						PartialReportArea.class,
						1, 3
						);
	}
}
