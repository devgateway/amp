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

	public final static List<String> activities = Arrays.asList(
			"Proposed Project Cost 1 - USD", "Test MTEF directed", "Eth Water");
	
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
	    .withContents("Project Title", "Report Totals", "2010-Actual Commitments-DN - IMPL", "0", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "66 555", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements-DN - EXEC", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "688 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "2010-Actual Commitments-DN - IMPL", "", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements-DN - EXEC", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "0", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-Actual Commitments-DN - IMPL", "", "2010-Real Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements-DN - IMPL", "77 222", "2010-Actual Disbursements- ", "66 555", "2013-Real Disbursements-DN - EXEC", "", "2013-Real Disbursements-EXEC - IMPL", "", "2013-Real Disbursements-IMPL - BENF", "", "2013-Real Disbursements-IMPL - EXEC", "44 333", "2013-Actual Disbursements-DN - EXEC", "", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "2010-Actual Commitments-DN - IMPL", "", "2010-Real Disbursements-DN - IMPL", "", "2010-Actual Disbursements-DN - IMPL", "", "2010-Actual Disbursements- ", "", "2013-Real Disbursements-DN - EXEC", "545 000", "2013-Real Disbursements-EXEC - IMPL", "100 000", "2013-Real Disbursements-IMPL - BENF", "15 000", "2013-Real Disbursements-IMPL - EXEC", "", "2013-Actual Disbursements-DN - EXEC", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Real Disbursements", "0", "Total Measures-Actual Disbursements", "545 000", "Total Measures-Real MTEFs", "0", "Total Measures-Real Commitments", "0"));
		
		runMondrianTestCase(
			"AMP-15337-crazy-header",						
			activities,
			correctReportEn,
			"en");
	}

}
