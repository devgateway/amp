package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.junit.Test;

/**
 * testcases for directed Funding Flows
 * currently the testcases data is not prime-time-quality because we rushed the implementation
 * @author Constantin Dolghier
 *
 */
public class FundingFlowsMondrianReportTests extends MondrianReportsTestCase {

	public final static List<String> activities = Arrays.asList("Proposed Project Cost 1 - USD", "Test MTEF directed", "Eth Water");
	public final static List<String> activities_with_mtef = Arrays.asList("Proposed Project Cost 1 - USD", "Test MTEF directed", "Eth Water", "activity with directed MTEFs");
	
	public FundingFlowsMondrianReportTests() {
		super("funding flows mondrian tests");
	}
	
	@Test
	public void testRealDisbursements() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0"));
		
		runMondrianTestCase(
			"AMP-15337-real-disbursements",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testMixedFlowsHeader() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"));		

		runMondrianTestCase(
			"AMP-15337-crazy-header-only-flows",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testMixedCrazyFlowsHeader() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "688 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "545 000", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"));
		
		runMondrianTestCase(
			"AMP-15337-crazy-header",						
			activities,
			correctReportEn,
			"en");
	}

	@Test
	public void testDirectedByDonor() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Donor Agency", "Report Totals", "Project Title", "", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "545 000", "Total Measures-Real Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Donor Agency", "Norway Totals", "Project Title", "", "2010-Real Disbursements-DN - IMPL", "0", "2013-Real Disbursements-DN - EXEC", "110 000", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Norway", "Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "110 000", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "USAID Totals", "Project Title", "", "2010-Real Disbursements-DN - IMPL", "0", "2013-Real Disbursements-DN - EXEC", "415 000", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "USAID", "Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "415 000", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Finland Totals", "Project Title", "", "2010-Real Disbursements-DN - IMPL", "0", "2013-Real Disbursements-DN - EXEC", "20 000", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Finland", "Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2013-Real Disbursements-DN - EXEC", "20 000", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Ministry of Economy Totals", "Project Title", "", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "0", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Ministry of Economy", "Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2013-Real Disbursements-DN - EXEC", "", "Total Measures-Real Disbursements", "0")));		
		
		runMondrianTestCase(
			"AMP-15337-real-disbursements-by-donor",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testDirectedByExecuting() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Executing Agency", "Report Totals", "Project Title", "", "2013-Real Disbursements-EXEC - IMPL", "100 000", "Total Measures-Real Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Executing Agency", "UNDP Totals", "Project Title", "", "2013-Real Disbursements-EXEC - IMPL", "80 000", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Executing Agency", "UNDP", "Project Title", "Eth Water", "2013-Real Disbursements-EXEC - IMPL", "80 000", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Executing Agency", "World Bank Totals", "Project Title", "", "2013-Real Disbursements-EXEC - IMPL", "20 000", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Executing Agency", "World Bank", "Project Title", "Eth Water", "2013-Real Disbursements-EXEC - IMPL", "20 000", "Total Measures-Real Disbursements", "0")));

		runMondrianTestCase(
			"AMP-15337-real-disbursements-by-executing",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testDirectedByImplementing() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Implementing Agency", "Report Totals", "Project Title", "", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Implementing Agency", "USAID Totals", "Project Title", "", "2013-Real Disbursements-IMPL - BENF", "0", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "USAID", "Project Title", "Test MTEF directed", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Finance Totals", "Project Title", "", "2013-Real Disbursements-IMPL - BENF", "10 000", "2013-Real Disbursements-IMPL - EXEC", "0", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Finance", "Project Title", "Eth Water", "2013-Real Disbursements-IMPL - BENF", "10 000", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Economy Totals", "Project Title", "", "2013-Real Disbursements-IMPL - BENF", "5 000", "2013-Real Disbursements-IMPL - EXEC", "0", "Total Measures-Real Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Economy", "Project Title", "Eth Water", "2013-Real Disbursements-IMPL - BENF", "5 000", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0")));
		runMondrianTestCase(
			"AMP-15337-real-disbursements-by-implementing",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testManyEmptyColumns_amp_21236() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "545 000", "2015-Actual Commitments- ", "123 456", "Total Measures-Actual Commitments", "123 456", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "688 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "545 000", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "545 000", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with directed MTEFs", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "123 456", "Total Measures-Actual Commitments", "123 456", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")); 

		runMondrianTestCase(
			"AMP-15337-crazy-header-many-empty-columns",						
			activities_with_mtef,
			correctReportEn,
			"en");
	}

	@Test
	public void testMtefDirected() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with directed MTEFs", "2010-Real Disbursements-DN - IMPL", "", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "Total Measures-Real Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"));

		runMondrianTestCase(
			"AMP-15337-crazy-header-only-flows",						
			activities_with_mtef,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testMtefDirectedMixed() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "545 000", "2015-Actual Commitments- ", "123 456", "Total Measures-Actual Commitments", "123 456", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "688 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "143 777", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "", "2011-Real MTEFs-IMPL - EXEC", "", "2012-Real MTEFs-EXEC - EXEC", "", "2012-Real MTEFs-IMPL - BENF", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "545 000", "2015-Actual Commitments- ", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "545 000", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with directed MTEFs", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2011-Real MTEFs-EXEC - BENF", "50 000", "2011-Real MTEFs-IMPL - EXEC", "110 500", "2012-Real MTEFs-EXEC - EXEC", "22 000", "2012-Real MTEFs-IMPL - BENF", "43 000", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements- ", "", "2015-Actual Commitments- ", "123 456", "Total Measures-Actual Commitments", "123 456", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")); 

		runMondrianTestCase(
			"AMP-15337-crazy-header",
			activities_with_mtef,
			correctReportEn,
			"en");
	}
}
