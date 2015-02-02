package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.junit.Test;

public class BasicMondrianReportTests extends MondrianReportsTestCase {
	
	public BasicMondrianReportTests() {
		super("basic mondrian tests");
	}
	
	@Test
	public void testProjectTitleLanguages() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 550 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "545 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				this.buildSpecification("testcase EN", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),						
						"en", 
						Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
						correctReport); 
				
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 550 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Вода Eth", "Actual Commitments", "0", "Actual Disbursements", "545 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Проект КЮЮ 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "обещание 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				buildSpecification("testcase RU", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"ru", 
				Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
				correctReportRu); 

		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
	
	@Test
	public void test_AMP_18497() {
		// for running manually: open http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/32 on the AMP 2.10 testcases database
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Group", "", "Actual Commitments", "999 888", "Actual Disbursements", "1 141 990")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "Donor Group", "American", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Donor Group", "National", "Actual Commitments", "0", "Actual Disbursements", "143 777"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Donor Group", "American, Default Group, European", "Actual Commitments", "0", "Actual Disbursements", "545 000"));

		runMondrianTestCase(
				buildSpecification("AMP-18497", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_GROUP), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Eth Water", "Test MTEF directed", "TAC_activity_2"),
				cor);
	}
	
	@Test
	public void test_AMP_18499_should_fail_for_now() {
		// for running manually: open http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=73 OR http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/73
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "666 777")
	    .withChildren(new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777")  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18499", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents", "ptc activity 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18504_should_fail_for_now() {
		// for running manually: http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=24 or http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/24
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 195 000", "Total Measures-Actual Disbursements", "522 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Donor Agency", "USAID", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18504",
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null, GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "pledged 2"),
				cor);
	}
	
	@Test
	public void test_AMP_18509() {
		// http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/16
		// test is now testing just that the thing is not crashing or outputting malformed output 
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "AMP ID", "", "2009-Q1-Actual Commitments", "100 000", "2009-Q1-Actual Disbursements", "0", "2009-Q1-Actual Expenditures", "0", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "60 000", "2010-Q2-Actual Expenditures", "0", "2012-Q3-Actual Commitments", "25 000", "2012-Q3-Actual Disbursements", "0", "2012-Q3-Actual Expenditures", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "12 000", "2012-Q4-Actual Expenditures", "0", "2013-Q4-Actual Commitments", "2 670 000", "2013-Q4-Actual Disbursements", "0", "2013-Q4-Actual Expenditures", "0", "2014-Q2-Actual Commitments", "4 400 000", "2014-Q2-Actual Disbursements", "450 000", "2014-Q2-Actual Expenditures", "0", "Total Measures-Actual Commitments", "7 195 000", "Total Measures-Actual Disbursements", "522 000", "Total Measures-Actual Expenditures", "0")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Region", "", "AMP ID", "872113null", "2009-Q1-Actual Commitments", "100 000", "2009-Q1-Actual Disbursements", "0", "2009-Q1-Actual Expenditures", "0", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "60 000", "2010-Q2-Actual Expenditures", "0", "2012-Q3-Actual Commitments", "25 000", "2012-Q3-Actual Disbursements", "0", "2012-Q3-Actual Expenditures", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "12 000", "2012-Q4-Actual Expenditures", "0", "2013-Q4-Actual Commitments", "0", "2013-Q4-Actual Disbursements", "0", "2013-Q4-Actual Expenditures", "0", "2014-Q2-Actual Commitments", "0", "2014-Q2-Actual Disbursements", "0", "2014-Q2-Actual Expenditures", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000", "Total Measures-Actual Expenditures", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Region", "Cahul County", "AMP ID", "87211347", "2009-Q1-Actual Commitments", "0", "2009-Q1-Actual Disbursements", "0", "2009-Q1-Actual Expenditures", "0", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "0", "2010-Q2-Actual Expenditures", "0", "2012-Q3-Actual Commitments", "0", "2012-Q3-Actual Disbursements", "0", "2012-Q3-Actual Expenditures", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "0", "2012-Q4-Actual Expenditures", "0", "2013-Q4-Actual Commitments", "2 670 000", "2013-Q4-Actual Disbursements", "0", "2013-Q4-Actual Expenditures", "0", "2014-Q2-Actual Commitments", "4 400 000", "2014-Q2-Actual Disbursements", "450 000", "2014-Q2-Actual Expenditures", "0", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000", "Total Measures-Actual Expenditures", "0")  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18509", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.AMP_ID),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_EXPENDITURES),
						null,
						GroupingCriteria.GROUPING_QUARTERLY),
				"en",
				Arrays.asList("date-filters-activity", "pledged 2"),
				cor);
	}
	
	@Test
	public void test_AMP_18530_no_hier() {
		// report with "Region" as a column, an activity without locations + one with locations
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "458 333", "Total Measures-Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Region", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Region", "Balti County", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")  );
		runMondrianTestCase(
				buildSpecification("AMP-18530-no-hier",						
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null,
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);

	}
	
	@Test
	public void test_AMP_18530_hier() {
		// report with "Region" as a column, an activity without locations + one with locations
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "458 333", "Total Measures-Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Region", "Balti County Totals", "Project Title", "", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Region", "Balti County", "Project Title", "crazy funding 1", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Region", "Region: Undefined Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Region", "Region: Undefined", "Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")    )  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18530-hier", 
						Arrays.asList(ColumnConstants.REGION, ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
		
		runMondrianTestCase(
				buildSpecification("AMP-18541-columns-not-ordered", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18542() {
		// report with "Region" as a column, an activity without locations + one with locations
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Region", "Report Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "458 333", "Total Measures-Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Region", "Balti County Totals", "Project Title", "", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Region", "Balti County", "Project Title", "crazy funding 1", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Region", "Region: Undefined Totals", "Project Title", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Region", "Region: Undefined", "Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")    )  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18542-ordered-columns", 
						Arrays.asList(ColumnConstants.REGION, ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
		
		runMondrianTestCase(
				buildSpecification("AMP-18542-unordered-columns", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18577_only_count_donor_transactions() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Disbursements", "143 777")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Disbursements", "143 777"));
		
		runMondrianTestCase(
				buildSpecification("AMP_18577_only_count_donor_transaction",
				Arrays.asList("Project Title", "Region"),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY),
			"en",
			Arrays.asList("Test MTEF directed"),
			cor
		);
	}
	
	@Test
	public void test_AMP_18330_empty_rows() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Disbursements", "143 777")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with primary_program", "Region", "", "2010-Actual Disbursements", "0", "Total Measures-Actual Disbursements", "0")  );
		
		ReportSpecificationImpl spec = buildSpecification("test_AMP_18330_empty_rows",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		spec.setDisplayEmptyFundingRows(true);
		
		runMondrianTestCase(spec, "en",
			Arrays.asList("Test MTEF directed", "activity with primary_program"),
			cor
		);
	}
	
	@Test
	public void test_AMP_18748_no_data() {
		ReportAreaForTests cor = new ReportAreaForTests().withChildren(  );
		
		ReportSpecificationImpl spec = buildSpecification("test_AMP_18748_no_data",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		runMondrianTestCase(spec, "en",
				Arrays.asList("__hopefully____invalid________name____"),
			cor
		);
	}
	
	@Test
	public void test_AMP_18587_empty_cells_in_tabs() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Executing Agency", "", "2010-Actual Disbursements", "60 000", "2012-Actual Disbursements", "12 000", "Total Measures-Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Executing Agency", "UNDP", "2010-Actual Disbursements", "60 000", "2012-Actual Disbursements", "12 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Executing Agency", "(Executing Agency Unspecified)", "2010-Actual Disbursements", "0", "2012-Actual Disbursements", "0", "Total Measures-Actual Disbursements", "0"));

		
		ReportSpecificationImpl spec = buildSpecification("test_AMP_18587",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.EXECUTING_AGENCY),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		spec.setEmptyOutputForUnspecifiedData(false);
		spec.setDisplayEmptyFundingRows(true);
		
		runMondrianTestCase(spec, "en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
	
	@Test
	public void testAllowEmptyColumnsWithQuarterReport() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2009-Q1-Actual Commitments", "100 000", "2009-Q1-Actual Disbursements", "0", "2009-Q2-Actual Commitments", "0", "2009-Q2-Actual Disbursements", "0", "2009-Q3-Actual Commitments", "0", "2009-Q3-Actual Disbursements", "0", "2009-Q4-Actual Commitments", "0", "2009-Q4-Actual Disbursements", "0", "2010-Q1-Actual Commitments", "0", "2010-Q1-Actual Disbursements", "453 213", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "60 000", "2010-Q3-Actual Commitments", "0", "2010-Q3-Actual Disbursements", "0", "2010-Q4-Actual Commitments", "0", "2010-Q4-Actual Disbursements", "0", "2011-Q1-Actual Commitments", "0", "2011-Q1-Actual Disbursements", "0", "2011-Q2-Actual Commitments", "0", "2011-Q2-Actual Disbursements", "0", "2011-Q3-Actual Commitments", "0", "2011-Q3-Actual Disbursements", "0", "2011-Q4-Actual Commitments", "999 888", "2011-Q4-Actual Disbursements", "0", "2012-Q1-Actual Commitments", "0", "2012-Q1-Actual Disbursements", "0", "2012-Q2-Actual Commitments", "0", "2012-Q2-Actual Disbursements", "0", "2012-Q3-Actual Commitments", "25 000", "2012-Q3-Actual Disbursements", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "12 000", "2013-Q1-Actual Commitments", "0", "2013-Q1-Actual Disbursements", "0", "2013-Q2-Actual Commitments", "0", "2013-Q2-Actual Disbursements", "0", "2013-Q3-Actual Commitments", "0", "2013-Q3-Actual Disbursements", "0", "2013-Q4-Actual Commitments", "333 333", "2013-Q4-Actual Disbursements", "0", "Total Measures-Actual Commitments", "1 458 221", "Total Measures-Actual Disbursements", "525 213")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "2009-Q1-Actual Commitments", "0", "2009-Q1-Actual Disbursements", "0", "2009-Q2-Actual Commitments", "0", "2009-Q2-Actual Disbursements", "0", "2009-Q3-Actual Commitments", "0", "2009-Q3-Actual Disbursements", "0", "2009-Q4-Actual Commitments", "0", "2009-Q4-Actual Disbursements", "0", "2010-Q1-Actual Commitments", "0", "2010-Q1-Actual Disbursements", "453 213", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "0", "2010-Q3-Actual Commitments", "0", "2010-Q3-Actual Disbursements", "0", "2010-Q4-Actual Commitments", "0", "2010-Q4-Actual Disbursements", "0", "2011-Q1-Actual Commitments", "0", "2011-Q1-Actual Disbursements", "0", "2011-Q2-Actual Commitments", "0", "2011-Q2-Actual Disbursements", "0", "2011-Q3-Actual Commitments", "0", "2011-Q3-Actual Disbursements", "0", "2011-Q4-Actual Commitments", "999 888", "2011-Q4-Actual Disbursements", "0", "2012-Q1-Actual Commitments", "0", "2012-Q1-Actual Disbursements", "0", "2012-Q2-Actual Commitments", "0", "2012-Q2-Actual Disbursements", "0", "2012-Q3-Actual Commitments", "0", "2012-Q3-Actual Disbursements", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "0", "2013-Q1-Actual Commitments", "0", "2013-Q1-Actual Disbursements", "0", "2013-Q2-Actual Commitments", "0", "2013-Q2-Actual Disbursements", "0", "2013-Q3-Actual Commitments", "0", "2013-Q3-Actual Disbursements", "0", "2013-Q4-Actual Commitments", "0", "2013-Q4-Actual Disbursements", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "2009-Q1-Actual Commitments", "100 000", "2009-Q1-Actual Disbursements", "0", "2009-Q2-Actual Commitments", "0", "2009-Q2-Actual Disbursements", "0", "2009-Q3-Actual Commitments", "0", "2009-Q3-Actual Disbursements", "0", "2009-Q4-Actual Commitments", "0", "2009-Q4-Actual Disbursements", "0", "2010-Q1-Actual Commitments", "0", "2010-Q1-Actual Disbursements", "0", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "60 000", "2010-Q3-Actual Commitments", "0", "2010-Q3-Actual Disbursements", "0", "2010-Q4-Actual Commitments", "0", "2010-Q4-Actual Disbursements", "0", "2011-Q1-Actual Commitments", "0", "2011-Q1-Actual Disbursements", "0", "2011-Q2-Actual Commitments", "0", "2011-Q2-Actual Disbursements", "0", "2011-Q3-Actual Commitments", "0", "2011-Q3-Actual Disbursements", "0", "2011-Q4-Actual Commitments", "0", "2011-Q4-Actual Disbursements", "0", "2012-Q1-Actual Commitments", "0", "2012-Q1-Actual Disbursements", "0", "2012-Q2-Actual Commitments", "0", "2012-Q2-Actual Disbursements", "0", "2012-Q3-Actual Commitments", "25 000", "2012-Q3-Actual Disbursements", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "12 000", "2013-Q1-Actual Commitments", "0", "2013-Q1-Actual Disbursements", "0", "2013-Q2-Actual Commitments", "0", "2013-Q2-Actual Disbursements", "0", "2013-Q3-Actual Commitments", "0", "2013-Q3-Actual Disbursements", "0", "2013-Q4-Actual Commitments", "0", "2013-Q4-Actual Disbursements", "0", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "2009-Q1-Actual Commitments", "0", "2009-Q1-Actual Disbursements", "0", "2009-Q2-Actual Commitments", "0", "2009-Q2-Actual Disbursements", "0", "2009-Q3-Actual Commitments", "0", "2009-Q3-Actual Disbursements", "0", "2009-Q4-Actual Commitments", "0", "2009-Q4-Actual Disbursements", "0", "2010-Q1-Actual Commitments", "0", "2010-Q1-Actual Disbursements", "0", "2010-Q2-Actual Commitments", "0", "2010-Q2-Actual Disbursements", "0", "2010-Q3-Actual Commitments", "0", "2010-Q3-Actual Disbursements", "0", "2010-Q4-Actual Commitments", "0", "2010-Q4-Actual Disbursements", "0", "2011-Q1-Actual Commitments", "0", "2011-Q1-Actual Disbursements", "0", "2011-Q2-Actual Commitments", "0", "2011-Q2-Actual Disbursements", "0", "2011-Q3-Actual Commitments", "0", "2011-Q3-Actual Disbursements", "0", "2011-Q4-Actual Commitments", "0", "2011-Q4-Actual Disbursements", "0", "2012-Q1-Actual Commitments", "0", "2012-Q1-Actual Disbursements", "0", "2012-Q2-Actual Commitments", "0", "2012-Q2-Actual Disbursements", "0", "2012-Q3-Actual Commitments", "0", "2012-Q3-Actual Disbursements", "0", "2012-Q4-Actual Commitments", "0", "2012-Q4-Actual Disbursements", "0", "2013-Q1-Actual Commitments", "0", "2013-Q1-Actual Disbursements", "0", "2013-Q2-Actual Commitments", "0", "2013-Q2-Actual Disbursements", "0", "2013-Q3-Actual Commitments", "0", "2013-Q3-Actual Disbursements", "0", "2013-Q4-Actual Commitments", "333 333", "2013-Q4-Actual Disbursements", "0", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0")  );
		
		ReportSpecificationImpl spec = buildSpecification("report with empty quarter columns",
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_QUARTERLY);
		spec.setDisplayEmptyFundingColumns(true);
		runMondrianTestCase(
				spec,
				"en", 
				Arrays.asList("TAC_activity_2", "date-filters-activity", "crazy funding 1"), 
				correctResult
				);
	}
	
	@Test
	public void testAllowEmptyColumnsWithMonthlyReport() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-January-Actual Commitments", "0", "2010-January-Actual Disbursements", "453 213", "2010-February-Actual Commitments", "0", "2010-February-Actual Disbursements", "0", "2010-March-Actual Commitments", "0", "2010-March-Actual Disbursements", "0", "2010-April-Actual Commitments", "0", "2010-April-Actual Disbursements", "0", "2010-May-Actual Commitments", "0", "2010-May-Actual Disbursements", "0", "2010-June-Actual Commitments", "0", "2010-June-Actual Disbursements", "0", "2010-July-Actual Commitments", "0", "2010-July-Actual Disbursements", "0", "2010-August-Actual Commitments", "0", "2010-August-Actual Disbursements", "0", "2010-September-Actual Commitments", "0", "2010-September-Actual Disbursements", "0", "2010-October-Actual Commitments", "0", "2010-October-Actual Disbursements", "0", "2010-November-Actual Commitments", "0", "2010-November-Actual Disbursements", "0", "2010-December-Actual Commitments", "0", "2010-December-Actual Disbursements", "0", "2011-January-Actual Commitments", "0", "2011-January-Actual Disbursements", "0", "2011-February-Actual Commitments", "0", "2011-February-Actual Disbursements", "0", "2011-March-Actual Commitments", "0", "2011-March-Actual Disbursements", "0", "2011-April-Actual Commitments", "0", "2011-April-Actual Disbursements", "0", "2011-May-Actual Commitments", "0", "2011-May-Actual Disbursements", "0", "2011-June-Actual Commitments", "0", "2011-June-Actual Disbursements", "0", "2011-July-Actual Commitments", "0", "2011-July-Actual Disbursements", "0", "2011-August-Actual Commitments", "0", "2011-August-Actual Disbursements", "0", "2011-September-Actual Commitments", "0", "2011-September-Actual Disbursements", "0", "2011-October-Actual Commitments", "0", "2011-October-Actual Disbursements", "0", "2011-November-Actual Commitments", "999 888", "2011-November-Actual Disbursements", "0", "2011-December-Actual Commitments", "0", "2011-December-Actual Disbursements", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213")
	    .withChildren(
	      new ReportAreaForTests()
          .withContents("Project Title", "TAC_activity_2", "2010-January-Actual Commitments", "0", "2010-January-Actual Disbursements", "453 213", "2010-February-Actual Commitments", "0", "2010-February-Actual Disbursements", "0", "2010-March-Actual Commitments", "0", "2010-March-Actual Disbursements", "0", "2010-April-Actual Commitments", "0", "2010-April-Actual Disbursements", "0", "2010-May-Actual Commitments", "0", "2010-May-Actual Disbursements", "0", "2010-June-Actual Commitments", "0", "2010-June-Actual Disbursements", "0", "2010-July-Actual Commitments", "0", "2010-July-Actual Disbursements", "0", "2010-August-Actual Commitments", "0", "2010-August-Actual Disbursements", "0", "2010-September-Actual Commitments", "0", "2010-September-Actual Disbursements", "0", "2010-October-Actual Commitments", "0", "2010-October-Actual Disbursements", "0", "2010-November-Actual Commitments", "0", "2010-November-Actual Disbursements", "0", "2010-December-Actual Commitments", "0", "2010-December-Actual Disbursements", "0", "2011-January-Actual Commitments", "0", "2011-January-Actual Disbursements", "0", "2011-February-Actual Commitments", "0", "2011-February-Actual Disbursements", "0", "2011-March-Actual Commitments", "0", "2011-March-Actual Disbursements", "0", "2011-April-Actual Commitments", "0", "2011-April-Actual Disbursements", "0", "2011-May-Actual Commitments", "0", "2011-May-Actual Disbursements", "0", "2011-June-Actual Commitments", "0", "2011-June-Actual Disbursements", "0", "2011-July-Actual Commitments", "0", "2011-July-Actual Disbursements", "0", "2011-August-Actual Commitments", "0", "2011-August-Actual Disbursements", "0", "2011-September-Actual Commitments", "0", "2011-September-Actual Disbursements", "0", "2011-October-Actual Commitments", "0", "2011-October-Actual Disbursements", "0", "2011-November-Actual Commitments", "999 888", "2011-November-Actual Disbursements", "0"	, "2011-December-Actual Commitments", "0", "2011-December-Actual Disbursements", "0", "Total Measures-Actual Commitments", "999 888", "Total Measures-Actual Disbursements", "453 213")  );
		
		ReportSpecificationImpl spec = buildSpecification("report with empty quarter columns",
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_MONTHLY);
		spec.setDisplayEmptyFundingColumns(true);
		runMondrianTestCase(
				spec,
				"en", 
				Arrays.asList("TAC_activity_2"), 
				correctResult
				);
	}
	
	@Test
	public void testAmpActivityIdFilter() {
		List<String> myaaids = Arrays.asList("24"); // eth water
		
		ReportSpecificationImpl spec = buildSpecification("simple report filtered by aaid", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingColumns(true);
		spec.setDisplayEmptyFundingRows(true);
		
		MondrianReportFilters mrf = new MondrianReportFilters();
		mrf.addFilterRule(new ReportColumn(ColumnConstants.INTERNAL_USE_ID), new FilterRule(myaaids, true));
		spec.setFilters(mrf);
		
		ReportAreaForTests cr1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "0", "Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "545 000"));

		runMondrianTestCase(
			spec,
			"en", 
			Arrays.asList("Eth Water", "Pure MTEF Project", "crazy funding 1"), 
			cr1
		);
		
		mrf = new MondrianReportFilters();
		mrf.addFilterRule(new ReportColumn(ColumnConstants.INTERNAL_USE_ID), new FilterRule(myaaids, false));
		spec.setFilters(mrf);
		
		ReportAreaForTests cr2 =  new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "333 333", "Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "Actual Commitments", "0", "Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "crazy funding 1", "Actual Commitments", "333 333", "Actual Disbursements", "0"));

		runMondrianTestCase(
			spec,
			"en", 
			Arrays.asList("Eth Water", "Pure MTEF Project", "crazy funding 1"), 
			cr2
		);
	}
	
}
