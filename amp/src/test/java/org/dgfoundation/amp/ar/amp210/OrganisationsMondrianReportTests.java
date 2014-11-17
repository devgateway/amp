package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.junit.Test;

/**
 * organisation filters: DA, EA, IA, RO, BA
 * @author simple
 *
 */
public class OrganisationsMondrianReportTests extends MondrianReportsTestCase {
	
	public OrganisationsMondrianReportTests() {
		super("organisation mondrian tests");
	}
		
	@Test
	public void testProjectsWithoutDonorFunding() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "5 200", "Total Measures-Actual Disbursements", "688 777")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with components", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Project with documents", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Donor Group", "American, Default Group, European", "Donor Type", "Default", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "4 000", "Total Measures-Actual Disbursements", "545 000"));
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Proposed Project Cost 1 - USD", "Test MTEF directed", "Pure MTEF Project", "activity with components", "Project with documents", "Eth Water"),
				correctReport,
				"en");
	}
	
	@Test
	public void testPureMtefProjectsDonorInfo() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance", "Donor Group", "National", "Donor Type", "Default", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"));
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Pure MTEF Project"),
				correctReport,
				"en"); // tests that projects

	}
	
	@Test
	public void testDirectedMtefProjectsDonorInfo() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "143 777")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "143 777"));
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Test MTEF directed"),
				correctReport,
				"en"); // tests that projects

	}
	
	@Test
	public void testFundlessDonorInfo() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "200", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Project with documents", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "100", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents"),
				correctReport,
				"en"); // tests that projects

	}
}
