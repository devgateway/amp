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
	    .withContents("Project Title", "Report Totals", "Region", "", "Zone", "", "2013-Actual Commitments", "2 138 754", "2013-Actual Disbursements", "686 956", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "2 139 254", "Total Measures-Actual Disbursements", "686 956")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Zone", "", "2013-Actual Commitments", "111 333", "2013-Actual Disbursements", "555 111", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "111 433", "Total Measures-Actual Disbursements", "555 111"),
	      new ReportAreaForTests().withContents("Project Title", "SSC Project 2", "Region", "Edinet County", "Zone", "", "2013-Actual Commitments", "567 421", "2013-Actual Disbursements", "131 845", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "567 521", "Total Measures-Actual Disbursements", "131 845"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Zone", "Bulboaca, Glodeni", "2013-Actual Commitments", "570 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "570 100", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Zone", "Dolboaca, Glodeni", "2013-Actual Commitments", "890 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "890 100", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase(
				buildSpecification("raw report with location columns", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ZONE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
				correctReport);
		
		runMondrianTestCase(
			"testing-locations-raw-data",						
			Arrays.asList("SSC Project 1", "SSC Project 2", "Activity with Zones", "Activity With Zones and Percentages", "Subnational no percentages", "mtef activity 1"),
			correctReport,
			"en"); // this one will fail because of https://jira.dgfoundation.org/browse/AMP-18676
	}
	
}
