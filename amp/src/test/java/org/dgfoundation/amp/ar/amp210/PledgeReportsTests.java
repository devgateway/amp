package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.junit.Test;

public class PledgeReportsTests extends ReportingTestCase {
	
	public PledgeReportsTests() {
		super("pledge reports mondrian tests");
	}
	
	@Test
	public void testPledgeStatusHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledge Status", "Report Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 186 878,1", "Total Measures-Actual Pledge", "12 966 059,62")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledge Status", "default status Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledge Status", "default status", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")    ),
	      new ReportAreaForTests().withContents("Pledge Status", "second status Totals", "Pledges Titles", "", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "0", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledge Status", "second status", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35")    ),
	      new ReportAreaForTests()
	          .withContents("Pledge Status", "Pledge Status: Undefined Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "0", "2014-Actual Pledge", "0", "Total Measures-Actual Pledge", "1 979 180,27")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledge Status", "Pledge Status: Undefined", "Pledges Titles", "ACVL Pledge Name 2", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	        new ReportAreaForTests().withContents("Pledge Status", "", "Pledges Titles", "free text name 2", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52")));
		
		runMondrianTestCase("AMP-16003-by-pledge-status",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names 
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeStatusFlat() {
		ReportAreaForTests correctReportEn =  new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledge Status", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 186 878,1", "Total Measures-Actual Pledge", "12 966 059,62")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledge Status", "second status", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35"),
	      new ReportAreaForTests().withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledge Status", "", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests().withContents("Pledges Titles", "free text name 2", "Pledge Status", "", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	      new ReportAreaForTests().withContents("Pledges Titles", "Heavily used pledge", "Pledge Status", "default status", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000"));
		
		runMondrianTestCase("AMP-16003-flat",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeTermsOfAssistanceHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Type Of Assistance", "Report Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 186 878,1", "Total Measures-Actual Pledge", "12 966 059,62")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Type Of Assistance", "default type of assistance Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "0", "2014-Actual Pledge", "8 967 676", "Total Measures-Actual Pledge", "10 946 857,52")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "default type of assistance", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "767 676", "Total Measures-Actual Pledge", "767 677,25"),
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "", "Pledges Titles", "ACVL Pledge Name 2", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "", "Pledges Titles", "free text name 2", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "8 200 000")    ),
	      new ReportAreaForTests().withContents("Pledges Type Of Assistance", "second type of assistance Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "0", "2014-Actual Pledge", "219 202,1", "Total Measures-Actual Pledge", "219 202,1")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "second type of assistance", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "219 202,1", "Total Measures-Actual Pledge", "219 202,1")    ),
	      new ReportAreaForTests().withContents("Pledges Type Of Assistance", "Pledges Type Of Assistance: Undefined Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "0", "Total Measures-Actual Pledge", "1 800 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Pledges Type Of Assistance", "Pledges Type Of Assistance: Undefined", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 800 000")));
		
		runMondrianTestCase("AMP-17195-by-ToA",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");

	}

	@Test
	public void testPledgeTermsOfAssistanceFlat() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Type Of Assistance", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 186 878,1", "Total Measures-Actual Pledge", "12 966 059,62")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Type Of Assistance", "default type of assistance, second type of assistance", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35"),
	      new ReportAreaForTests().withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests().withContents("Pledges Titles", "free text name 2", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	      new ReportAreaForTests().withContents("Pledges Titles", "Heavily used pledge", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000"));
		
		runMondrianTestCase("AMP-17195-flat",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeSectorColumnsFlat() {
		// AMP-17423-sectors
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges sectors", "", "Pledges Secondary Sectors", "", "2012-Actual Commitments", "0", "2012-Actual Pledge", "0,44", "2013-Actual Commitments", "2 670 000", "2013-Actual Pledge", "1 800 000", "2014-Actual Commitments", "3 300 000", "2014-Actual Pledge", "8 545 407,34", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Actual Pledge", "10 345 407,77")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "2012-Actual Commitments", "", "2012-Actual Pledge", "0,44", "2013-Actual Commitments", "", "2013-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Actual Pledge", "345 407,34", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Pledge", "345 407,77"),
	      new ReportAreaForTests().withContents("Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "2012-Actual Commitments", "", "2012-Actual Pledge", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Pledge", "1 800 000", "2014-Actual Commitments", "3 300 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Actual Pledge", "10 000 000"));
		
		runMondrianTestCase("AMP-17423-sectors",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");

	}

}
