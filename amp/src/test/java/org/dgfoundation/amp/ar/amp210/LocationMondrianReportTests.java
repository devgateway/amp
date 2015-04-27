package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.junit.Test;

public class LocationMondrianReportTests extends MondrianReportsTestCase {
	
	public LocationMondrianReportTests() {
		super("location mondrian tests");
	}
	
	@Test
	public void testRawColumns() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "Zone", "", "2013-Actual Commitments", "2 138 754", "2013-Actual Disbursements", "686 956", "Total Measures-Actual Commitments", "2 138 754", "Total Measures-Actual Disbursements", "686 956")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "mtef activity 1", "Region", "", "Zone", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Zone", "", "2013-Actual Commitments", "111 333", "2013-Actual Disbursements", "555 111", "Total Measures-Actual Commitments", "111 333", "Total Measures-Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 2", "Region", "Edinet County", "Zone", "", "2013-Actual Commitments", "567 421", "2013-Actual Disbursements", "131 845", "Total Measures-Actual Commitments", "567 421", "Total Measures-Actual Disbursements", "131 845"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Zone", "Bulboaca, Glodeni", "2013-Actual Commitments", "570 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "570 000", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Zone", "Dolboaca, Glodeni", "2013-Actual Commitments", "890 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "890 000", "Total Measures-Actual Disbursements", "0")  );

		runMondrianTestCase(
				"testing-locations-raw-data",						
				Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
				correctReport,
				"en"); // this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
		
		ReportSpecificationImpl spec = buildSpecification("raw report with location columns", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ZONE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		spec.setDisplayEmptyFundingRows(true); 
		
		runMondrianTestCase(
				spec,
				"en",
				Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
				correctReport);
		
	}
	
	@Test
	public void testFilteredByRegion() {
		// this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "Zone", "", "2013-Actual Commitments", "997 000", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "997 000", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with Zones", "Region", "Balti County", "Zone", "Glodeni", "2013-Actual Commitments", "285 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "285 000", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity With Zones and Percentages", "Region", "Balti County", "Zone", "Glodeni", "2013-Actual Commitments", "712 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "712 000", "Total Measures-Actual Disbursements", "0")  );

		runMondrianTestCase(
				"testing-locations-filtered-balti",						
				Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
				correctReport,
				"en"); // this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
	}
	
	@Test
	public void testFilteredByZone() {
		// this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
		ReportAreaForTests correctReport =  new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Region", "", "Zone", "", "2013-Actual Commitments", "178 000", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "178 000", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County", "Zone", "Dolboaca", "2013-Actual Commitments", "178 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "178 000", "Total Measures-Actual Disbursements", "0")  );
		
		runMondrianTestCase(
				"testing-locations-by-zone",						
				Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
				correctReport,
				"en"); // this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
	}
	
}
