package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.junit.Test;

public class BasicMondrianReportTests extends MondrianReportsTestCase {
	
	public BasicMondrianReportTests() {
		super("basic mondrian tests");
	}
	
	@Test
	public void testProjectTitleLanguages() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				"en", 
				Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
				correctReport,
				"testcase EN"); 
				
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Вода Eth", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Проект КЮЮ 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "обещание 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				"ru", 
				Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
				correctReportRu,
				"testcase RU"); 

		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
	
	@Test
	public void test_AMP_18497() {
		// for running manually: open http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/32 on the AMP 2.10 testcases database
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Group", "", "Actual Commitments", "999 888", "Actual Disbursements", "1 301 323")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "Donor Group", "American", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Donor Group", "American, National", "Actual Commitments", "0", "Actual Disbursements", "188 110"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Donor Group", "American, Default Group, European, International, National", "Actual Commitments", "0", "Actual Disbursements", "660 000"));

		runMondrianTestCase(
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_GROUP),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				"en", 
				Arrays.asList("Eth Water", "Test MTEF directed", "TAC_activity_2"), 
				cor, "AMP-18497");
	}
	
	@Test
	public void test_AMP_18499() {
		// for running manually: open http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=73 OR http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/73
		ReportAreaForTests cor = new ReportAreaForTests()
	    	.withContents("Project Title", "Report Totals", "Actual Commitments", "666 777")
	    	.withChildren(
	    			new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777"),
	    			new ReportAreaForTests().withContents("Project Title", "Proposed Project Cost 1 - USD", "Actual Commitments", "0"),
	    			new ReportAreaForTests().withContents("Project Title", "Project with documents", "Actual Commitments", "0")
	    	);
		
		runMondrianTestCase(
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
				"en",
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents", "ptc activity 1"),
				cor, "AMP-18499");
	}
}
