package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.junit.Test;

/**
 * testcases linked to AMP-18558
 * @author Dolghier Constantin
 *
 */
public class MondrianSummaryReportTests extends MondrianReportsTestCase {
	
	public List<String> activities = Arrays.asList("TAC_activity_2", "date-filters-activity", "crazy funding 1");
	
	public MondrianSummaryReportTests() {
		super("mondrian summary reports tests");
	}
	
	@Test
	public void testSummaryReportsDepth2TotalsOnly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Primary Sector", "", "Actual Commitments", "1 458 221", "Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests().withContents("Region", "Balti County Totals", "Primary Sector", "", "Actual Commitments", "333 333", "Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Balti County", "Primary Sector", "110 - EDUCATION", "Actual Commitments", "333 333", "Actual Disbursements", "0")),
	      new ReportAreaForTests().withContents("Region", "Falesti County Totals", "Primary Sector", "", "Actual Commitments", "999 888", "Actual Disbursements", "453 213")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Falesti County", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Actual Commitments", "999 888", "Actual Disbursements", "453 213")    ),
	      new ReportAreaForTests().withContents("Region", "Region: Undefined Totals", "Primary Sector", "", "Actual Commitments", "125 000", "Actual Disbursements", "72 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Region: Undefined", "Primary Sector", "110 - EDUCATION", "Actual Commitments", "125 000", "Actual Disbursements", "72 000")));
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-two-hier-totals",
				"AMP-18558-two-hier-totals",
				activities,
				correctReport,
				"en");		
	}
	
	@Test
	public void testSummaryReportsDepth2Yearly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Primary Sector", "", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "513 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "1 458 221", "Total Measures-Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests().withContents("Region", "Balti County Totals", "Primary Sector", "", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Balti County", "Primary Sector", "110 - EDUCATION", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Region", "Falesti County Totals", "Primary Sector", "", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "453 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Falesti County", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "453 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213")    ),
	      new ReportAreaForTests().withContents("Region", "Region: Undefined Totals", "Primary Sector", "", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "60 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Region: Undefined", "Primary Sector", "110 - EDUCATION", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "60 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")));
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-two-hier-yearly",
				"AMP-18558-two-hier-yearly",
				activities,
				correctReport,
				"en");
	}

	@Test
	public void testSummaryReportsDepth1Yearly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "513 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "1 458 221", "Total Measures-Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Region", "Balti County", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Region", "Falesti County", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "453 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Region", "Region: Undefined", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "60 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"));
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-one-hier-yearly",
				"AMP-18558-one-hier-yearly",
				activities,
				correctReport,
				"en");
	}

	@Test
	public void testSummaryReportsDepth1TotalsOnly() {
		ReportAreaForTests correctReport =  new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Actual Commitments", "1 458 221", "Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Region", "Balti County", "Actual Commitments", "333 333", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Region", "Falesti County", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Region", "Region: Undefined", "Actual Commitments", "125 000", "Actual Disbursements", "72 000"));
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-one-hier-totals",
				"AMP-18558-one-hier-totals",
				activities,
				correctReport,
				"en");
	}

	@Test
	public void testSummaryReportGauge() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "Primary Sector", "", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "513 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "1 458 221", "Total Measures-Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "Region", "Falesti County", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "453 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Region", "", "Primary Sector", "110 - EDUCATION", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "60 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Region", "Balti County", "Primary Sector", "110 - EDUCATION", "2009-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0"));
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-raw",
				"AMP-18558-raw",
				activities,
				correctReport,
				"en"); 
	}
	
	@Test
	public void testSummaryReportsNoHierTotalsOnly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
		  .withChildren(
		    new ReportAreaForTests().withContents("Constant", "constant", "Actual Commitments", "1 458 221", "Actual Disbursements", "525 213"));
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("AMP-18558-no-hier-totals",
				"AMP-18558-no-hier-totals",
				activities,
				correctReport,
				"en"); 
	}
	
	@Test
	public void testSummaryReportsNoHierYearly() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
		  .withChildren(
		    new ReportAreaForTests().withContents("Constant", "constant", "2009-Actual Commitments", "100 000", "2010-Actual Disbursements", "513 213", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "1 458 221", "Total Measures-Actual Disbursements", "525 213"));

		runMondrianTestCase("AMP-18558-no-hier-yearly",
				"AMP-18558-no-hier-yearly",
				activities,
				correctReport,
				"en"); 
	}
	
	@Test
	public void testRegionHierarchy() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "25 000", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "1 458 221")
	    .withChildren(
	      new ReportAreaForTests().withContents("Region", "Balti County Totals", "Project Title", "", "2009-Actual Commitments", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Balti County", "Project Title", "crazy funding 1", "2009-Actual Commitments", "0", "2011-Actual Commitments", "0", "2012-Actual Commitments", "0", "2013-Actual Commitments", "333 333", "Total Measures-Actual Commitments", "333 333")),
	      new ReportAreaForTests().withContents("Region", "Falesti County Totals", "Project Title", "", "2009-Actual Commitments", "0", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Falesti County", "Project Title", "TAC_activity_2", "2009-Actual Commitments", "0", "2011-Actual Commitments", "999 888", "2012-Actual Commitments", "0", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "999 888")),
	      new ReportAreaForTests().withContents("Region", "Region: Undefined Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Region", "Region: Undefined", "Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "2011-Actual Commitments", "0", "2012-Actual Commitments", "25 000", "2013-Actual Commitments", "0", "Total Measures-Actual Commitments", "125 000")));

		runMondrianTestCase("test-sums",
				"test-sums",
				activities,
				correctReport,
				"en"); 
	}
}
