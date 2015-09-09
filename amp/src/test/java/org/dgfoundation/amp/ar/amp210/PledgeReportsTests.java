package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.junit.Test;

public class PledgeReportsTests extends MondrianReportsTestCase {
	
	public PledgeReportsTests() {
		super("pledge reports mondrian tests");
	}
	
	@Test
	public void testPledgeStatusHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledge Status", "Report Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 233 244,98", "Total Measures-Actual Pledge", "13 012 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledge Status", "default status Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledge Status", "default status", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")    ),
	      new ReportAreaForTests()
	          .withContents("Pledge Status", "second status Totals", "Pledges Titles", "", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "0", "2014-Actual Pledge", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledge Status", "second status", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23")    ),
	      new ReportAreaForTests()
	          .withContents("Pledge Status", "Pledge Status: Undefined Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "0", "2014-Actual Pledge", "0", "Total Measures-Actual Pledge", "1 979 180,27")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledge Status", "Pledge Status: Undefined", "Pledges Titles", "ACVL Pledge Name 2", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	        new ReportAreaForTests()
	              .withContents("Pledge Status", "", "Pledges Titles", "free text name 2", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52")    )  );
		
		runMondrianTestCase("AMP-16003-by-pledge-status",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names 
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeStatusFlat() {
		ReportAreaForTests correctReportEn =  new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledge Status", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 233 244,98", "Total Measures-Actual Pledge", "13 012 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Test pledge 1", "Pledge Status", "second status", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledge Status", "", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "free text name 2", "Pledge Status", "", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Heavily used pledge", "Pledge Status", "default status", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")  );

		
		runMondrianTestCase("AMP-16003-flat",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeTermsOfAssistanceHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Type Of Assistance", "Report Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 233 244,98", "Total Measures-Actual Pledge", "13 012 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Type Of Assistance", "default type of assistance Totals", "Pledges Titles", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "0", "2014-Actual Pledge", "8 967 676", "Total Measures-Actual Pledge", "10 946 857,52")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "default type of assistance", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "767 676", "Total Measures-Actual Pledge", "767 677,25"),
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "", "Pledges Titles", "ACVL Pledge Name 2", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "", "Pledges Titles", "free text name 2", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "8 200 000")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Type Of Assistance", "second type of assistance Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "0", "2014-Actual Pledge", "265 568,98", "Total Measures-Actual Pledge", "265 568,98")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "second type of assistance", "Pledges Titles", "Test pledge 1", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "265 568,98", "Total Measures-Actual Pledge", "265 568,98")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Type Of Assistance", "Pledges Type Of Assistance: Undefined Totals", "Pledges Titles", "", "2012-Actual Pledge", "0", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "0", "Total Measures-Actual Pledge", "1 800 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Type Of Assistance", "Pledges Type Of Assistance: Undefined", "Pledges Titles", "Heavily used pledge", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 800 000")    )  );
		
		runMondrianTestCase("AMP-17195-by-ToA",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");

	}

	@Test
	public void testPledgeTermsOfAssistanceFlat() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Type Of Assistance", "", "2012-Actual Pledge", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "9 233 244,98", "Total Measures-Actual Pledge", "13 012 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Test pledge 1", "Pledges Type Of Assistance", "default type of assistance, second type of assistance", "2012-Actual Pledge", "1,25", "2013-Actual Pledge", "", "2014-Actual Pledge", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "free text name 2", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "1 041 110,52", "2013-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "1 041 110,52"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Heavily used pledge", "Pledges Type Of Assistance", "default type of assistance", "2012-Actual Pledge", "", "2013-Actual Pledge", "1 800 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Pledge", "10 000 000")  );
		
		runMondrianTestCase("AMP-17195-flat",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgePrimarySectorHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges sectors", "Report Totals", "Pledges Titles", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "3 350 000", "2014-Commitment Gap", "5 883 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "6 020 000", "Total Measures-Commitment Gap", "6 992 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges sectors", "112 - BASIC EDUCATION Totals", "Pledges Titles", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,44", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,44", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "8 561 635,74", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "5 261 635,74", "Total Measures-Actual Pledge", "10 361 636,18", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Commitment Gap", "4 391 636,18")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges sectors", "112 - BASIC EDUCATION", "Pledges Titles", "Test pledge 1", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,44", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,44", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "361 635,74", "2014-Actual Commitments", "", "2014-Commitment Gap", "361 635,74", "Total Measures-Actual Pledge", "361 636,18", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "361 636,18"),
	        new ReportAreaForTests()
	              .withContents("Pledges sectors", "", "Pledges Titles", "Heavily used pledge", "<null>", "pledged 2, pledged education activity 1", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "8 200 000", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "4 900 000", "Total Measures-Actual Pledge", "10 000 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Commitment Gap", "4 030 000")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges sectors", "113 - SECONDARY EDUCATION Totals", "Pledges Titles", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,81", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,81", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "671 609,24", "2014-Actual Commitments", "0", "2014-Commitment Gap", "671 609,24", "Total Measures-Actual Pledge", "671 610,05", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "671 610,05")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges sectors", "113 - SECONDARY EDUCATION", "Pledges Titles", "Test pledge 1", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,81", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,81", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "671 609,24", "2014-Actual Commitments", "", "2014-Commitment Gap", "671 609,24", "Total Measures-Actual Pledge", "671 610,05", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "671 610,05")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges sectors", "Pledges Sectors: Undefined Totals", "Pledges Titles", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "0", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "1 979 180,27", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "1 929 180,27")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges sectors", "Pledges Sectors: Undefined", "Pledges Titles", "ACVL Pledge Name 2", "<null>", "Activity Linked With Pledge", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "938 069,75", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "888 069,75"),
	        new ReportAreaForTests()
	              .withContents("Pledges sectors", "", "Pledges Titles", "free text name 2", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "1 041 110,52", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 041 110,52")    )  );
		
		runMondrianTestCase("AMP-17196-by-pledge-primary-sector",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeRegionsHier() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Regions", "Report Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "3 350 000", "2014-Commitment Gap", "5 883 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "6 020 000", "Total Measures-Commitment Gap", "6 992 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Anenii Noi County Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "630 000", "2013-Actual Commitments", "934 500", "2013-Commitment Gap", "-304 500", "2014-Actual Pledge", "2 870 000", "2014-Actual Commitments", "1 155 000", "2014-Commitment Gap", "1 715 000", "Total Measures-Actual Pledge", "3 500 000", "Total Measures-Actual Commitments", "2 089 500", "Total Measures-Commitment Gap", "1 410 500")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Anenii Noi County", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "pledged 2, pledged education activity 1", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "630 000", "2013-Actual Commitments", "934 500", "2013-Commitment Gap", "-304 500", "2014-Actual Pledge", "2 870 000", "2014-Actual Commitments", "1 155 000", "2014-Commitment Gap", "1 715 000", "Total Measures-Actual Pledge", "3 500 000", "Total Measures-Actual Commitments", "2 089 500", "Total Measures-Commitment Gap", "1 410 500")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Balti County Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,56", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,56", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "464 960,24", "2014-Actual Commitments", "0", "2014-Commitment Gap", "464 960,24", "Total Measures-Actual Pledge", "464 960,8", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "464 960,8")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Balti County", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,56", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,56", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "464 960,24", "2014-Actual Commitments", "", "2014-Commitment Gap", "464 960,24", "Total Measures-Actual Pledge", "464 960,8", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "464 960,8")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Cahul County Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,62", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,62", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "516 622,49", "2014-Actual Commitments", "0", "2014-Commitment Gap", "516 622,49", "Total Measures-Actual Pledge", "516 623,12", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "516 623,12")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Cahul County", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,62", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,62", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "516 622,49", "2014-Actual Commitments", "", "2014-Commitment Gap", "516 622,49", "Total Measures-Actual Pledge", "516 623,12", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "516 623,12")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Lapusna County Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "1 170 000", "2013-Actual Commitments", "1 735 500", "2013-Commitment Gap", "-565 500", "2014-Actual Pledge", "5 330 000", "2014-Actual Commitments", "2 145 000", "2014-Commitment Gap", "3 185 000", "Total Measures-Actual Pledge", "6 500 000", "Total Measures-Actual Commitments", "3 880 500", "Total Measures-Commitment Gap", "2 619 500")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Lapusna County", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "pledged 2, pledged education activity 1", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "1 170 000", "2013-Actual Commitments", "1 735 500", "2013-Commitment Gap", "-565 500", "2014-Actual Pledge", "5 330 000", "2014-Actual Commitments", "2 145 000", "2014-Commitment Gap", "3 185 000", "Total Measures-Actual Pledge", "6 500 000", "Total Measures-Actual Commitments", "3 880 500", "Total Measures-Commitment Gap", "2 619 500")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Transnistrian Region Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,06", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,06", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "51 662,25", "2014-Actual Commitments", "0", "2014-Commitment Gap", "51 662,25", "Total Measures-Actual Pledge", "51 662,31", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "51 662,31")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Transnistrian Region", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,06", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,06", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "51 662,25", "2014-Actual Commitments", "", "2014-Commitment Gap", "51 662,25", "Total Measures-Actual Pledge", "51 662,31", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "51 662,31")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Regions", "Pledges Regions: Undefined Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "0", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "1 979 180,27", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "1 929 180,27")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "Pledges Regions: Undefined", "Pledges Titles", "ACVL Pledge Name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "Activity Linked With Pledge", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "938 069,75", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "888 069,75"),
	        new ReportAreaForTests()
	              .withContents("Pledges Regions", "", "Pledges Titles", "free text name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "1 041 110,52", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 041 110,52")    )  );

	              
		runMondrianTestCase("AMP-17196-by-regions",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}

	
	@Test
	public void testPledgeManyFieldsFlat() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "Pledges Regions", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "3 350 000", "2014-Commitment Gap", "5 883 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "6 020 000", "Total Measures-Commitment Gap", "6 992 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1,25", "2012-Actual Commitments", "", "2012-Commitment Gap", "1,25", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "1 033 244,98", "2014-Actual Commitments", "", "2014-Commitment Gap", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 033 246,23"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "Activity Linked With Pledge", "Pledges Regions", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "938 069,75", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "888 069,75"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "free text name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "", "Pledges Regions", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "1 041 110,52", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 041 110,52"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "<null>", "pledged 2, pledged education activity 1", "Pledges Regions", "Anenii Noi County, Lapusna County", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "8 200 000", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "4 900 000", "Total Measures-Actual Pledge", "10 000 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Commitment Gap", "4 030 000")  );
		
		runMondrianTestCase("AMP-17196-flat",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeManyFieldsByRelatedProject() {
		// AMP-17196-by-related-project - this one gives wildly different result compared to old reports engine because of a bug in old reports engine's implementation of "related project"
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("<null>", "Report Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "3 350 000", "2014-Commitment Gap", "5 883 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "6 020 000", "Total Measures-Commitment Gap", "6 992 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("<null>", "Activity Linked With Pledge Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "0", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "-50 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("<null>", "Activity Linked With Pledge", "Pledges Titles", "ACVL Pledge Name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "-50 000")    ),
	      new ReportAreaForTests()
	          .withContents("<null>", "pledged education activity 1 Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "0", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "-3 300 000", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "3 300 000", "Total Measures-Commitment Gap", "-3 300 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("<null>", "pledged education activity 1", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Anenii Noi County, Lapusna County", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "-3 300 000", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "3 300 000", "Total Measures-Commitment Gap", "-3 300 000")    ),
	      new ReportAreaForTests()
	          .withContents("<null>", "pledged 2 Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "0", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-2 670 000", "2014-Actual Pledge", "0", "2014-Actual Commitments", "0", "2014-Commitment Gap", "0", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "2 670 000", "Total Measures-Commitment Gap", "-2 670 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("<null>", "pledged 2", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Anenii Noi County, Lapusna County", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-2 670 000", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "0", "Total Measures-Actual Commitments", "2 670 000", "Total Measures-Commitment Gap", "-2 670 000")    ),
	      new ReportAreaForTests()
	          .withContents("<null>", "Related Projects: Undefined Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "0", "2013-Commitment Gap", "1 800 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "0", "2014-Commitment Gap", "9 233 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "13 012 426,5")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("<null>", "Related Projects: Undefined", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1,25", "2012-Actual Commitments", "", "2012-Commitment Gap", "1,25", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "1 033 244,98", "2014-Actual Commitments", "", "2014-Commitment Gap", "1 033 244,98", "Total Measures-Actual Pledge", "1 033 246,23", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 033 246,23"),
	        new ReportAreaForTests()
	              .withContents("<null>", "", "Pledges Titles", "ACVL Pledge Name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "938 069,75", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "938 069,75"),
	        new ReportAreaForTests()
	              .withContents("<null>", "", "Pledges Titles", "free text name 2", "Pledges sectors", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "1 041 110,52", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 041 110,52"),
	        new ReportAreaForTests()
	              .withContents("<null>", "", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Anenii Noi County, Lapusna County", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "", "2013-Commitment Gap", "1 800 000", "2014-Actual Pledge", "8 200 000", "2014-Actual Commitments", "", "2014-Commitment Gap", "8 200 000", "Total Measures-Actual Pledge", "10 000 000", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "10 000 000")    )  );
		
		runMondrianTestCase("AMP-17196-by-related-project",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
	@Test
	public void testPledgeManyFieldsBySecondarySector() {
		//AMP-17196-by-sec-sector
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Secondary Sectors", "Report Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 111,77", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 111,77", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "9 233 244,98", "2014-Actual Commitments", "3 350 000", "2014-Commitment Gap", "5 883 244,98", "Total Measures-Actual Pledge", "13 012 426,5", "Total Measures-Actual Commitments", "6 020 000", "Total Measures-Commitment Gap", "6 992 426,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "0", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "0", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "0", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "1 979 180,27", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "1 929 180,27")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Titles", "ACVL Pledge Name 2", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "Activity Linked With Pledge", "1998-Actual Pledge", "938 069,75", "1998-Actual Commitments", "", "1998-Commitment Gap", "938 069,75", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "50 000", "2014-Commitment Gap", "-50 000", "Total Measures-Actual Pledge", "938 069,75", "Total Measures-Actual Commitments", "50 000", "Total Measures-Commitment Gap", "888 069,75"),
	        new ReportAreaForTests()
	              .withContents("Pledges Secondary Sectors", "", "Pledges Titles", "free text name 2", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "1 041 110,52", "2012-Actual Commitments", "", "2012-Commitment Gap", "1 041 110,52", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Commitment Gap", "", "Total Measures-Actual Pledge", "1 041 110,52", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "1 041 110,52")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,69", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,69", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "568 284,74", "2014-Actual Commitments", "0", "2014-Commitment Gap", "568 284,74", "Total Measures-Actual Pledge", "568 285,43", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "568 285,43")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,69", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,69", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "568 284,74", "2014-Actual Commitments", "", "2014-Commitment Gap", "568 284,74", "Total Measures-Actual Pledge", "568 285,43", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "568 285,43")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Secondary Sectors", "5 REGIONAL DEVELOPMENT Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0,56", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0,56", "2013-Actual Pledge", "0", "2013-Actual Commitments", "0", "2013-Commitment Gap", "0", "2014-Actual Pledge", "464 960,24", "2014-Actual Commitments", "0", "2014-Commitment Gap", "464 960,24", "Total Measures-Actual Pledge", "464 960,8", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "464 960,8")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Secondary Sectors", "5 REGIONAL DEVELOPMENT", "Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Secondary Programs", "", "Pledges Programs", "Subprogram p1.b", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "<null>", "", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "0,56", "2012-Actual Commitments", "", "2012-Commitment Gap", "0,56", "2013-Actual Pledge", "", "2013-Actual Commitments", "", "2013-Commitment Gap", "", "2014-Actual Pledge", "464 960,24", "2014-Actual Commitments", "", "2014-Commitment Gap", "464 960,24", "Total Measures-Actual Pledge", "464 960,8", "Total Measures-Actual Commitments", "0", "Total Measures-Commitment Gap", "464 960,8")    ),
	      new ReportAreaForTests()
	          .withContents("Pledges Secondary Sectors", "Pledges Secondary Sectors: Undefined Totals", "Pledges Titles", "", "Pledges sectors", "", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "", "<null>", "", "1998-Actual Pledge", "0", "1998-Actual Commitments", "0", "1998-Commitment Gap", "0", "2012-Actual Pledge", "0", "2012-Actual Commitments", "0", "2012-Commitment Gap", "0", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "8 200 000", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "4 900 000", "Total Measures-Actual Pledge", "10 000 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Commitment Gap", "4 030 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Pledges Secondary Sectors", "Pledges Secondary Sectors: Undefined", "Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Programs", "", "Pledges Programs", "", "Pledges National Plan Objectives", "", "Pledges Tertiary Sectors", "", "Pledges Regions", "Anenii Noi County, Lapusna County", "<null>", "pledged 2, pledged education activity 1", "1998-Actual Pledge", "", "1998-Actual Commitments", "", "1998-Commitment Gap", "", "2012-Actual Pledge", "", "2012-Actual Commitments", "", "2012-Commitment Gap", "", "2013-Actual Pledge", "1 800 000", "2013-Actual Commitments", "2 670 000", "2013-Commitment Gap", "-870 000", "2014-Actual Pledge", "8 200 000", "2014-Actual Commitments", "3 300 000", "2014-Commitment Gap", "4 900 000", "Total Measures-Actual Pledge", "10 000 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Commitment Gap", "4 030 000")    )  );
		
		runMondrianTestCase("AMP-17196-by-sec-sector",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");
	}
	
//	/**
//	 * no filtering by pledges aid modality implemented in new reports
//	 */
//	@Test
//	public void testPledgesAidModalityFiltered() {
//		// AMP-17423-aid-modality-filtered
//		
//		ReportAreaForTests correctReportEn = new ReportAreaForTests()
//	    .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "2012-Actual Pledge", "1,25", "Total Measures-Actual Pledge", "1,25")
//	    .withChildren(
//	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Aid Modality", "Development of shared analytical studies", "2012-Actual Pledge", "1,25", "Total Measures-Actual Pledge", "1,25"));
//		
//		runMondrianTestCase("AMP-17423-aid-modality-filtered-development",
//			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
//			correctReportEn, "en");
//	}
	
	@Test
	public void testPledgeSectorColumnsFlat() {
		// AMP-17423-sectors
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges sectors", "", "Pledges Secondary Sectors", "", "2012-Actual Commitments", "0", "2012-Actual Pledge", "0,44", "2013-Actual Commitments", "2 670 000", "2013-Actual Pledge", "1 800 000", "2014-Actual Commitments", "3 300 000", "2014-Actual Pledge", "8 561 635,74", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Actual Pledge", "10 361 636,18")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Test pledge 1", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "2012-Actual Commitments", "", "2012-Actual Pledge", "0,44", "2013-Actual Commitments", "", "2013-Actual Pledge", "", "2014-Actual Commitments", "", "2014-Actual Pledge", "361 635,74", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Pledge", "361 636,18"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "Heavily used pledge", "Pledges sectors", "112 - BASIC EDUCATION", "Pledges Secondary Sectors", "", "2012-Actual Commitments", "", "2012-Actual Pledge", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Pledge", "1 800 000", "2014-Actual Commitments", "3 300 000", "2014-Actual Pledge", "8 200 000", "Total Measures-Actual Commitments", "5 970 000", "Total Measures-Actual Pledge", "10 000 000")  );
		
		runMondrianTestCase("AMP-17423-sectors",
			Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge"), // pledge report -> pledge names
			correctReportEn, "en");

	}

}
