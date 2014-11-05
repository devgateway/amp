package org.dgfoundation.amp.ar.amp210;

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
		ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype");
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		GeneratedReport rep = this.runReportOn(spec, "en", null);
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "17 102 205", "Actual Disbursements", "2 633 600")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_2", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Actual Commitments", "0", "Actual Disbursements", "188 110"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Actual Commitments", "125 000", "Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "ptc activity 2", "Actual Commitments", "333 222", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 2", "Actual Commitments", "567 421", "Actual Disbursements", "131 845"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Actual Commitments", "333 333", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with Zones", "Actual Commitments", "570 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity With Zones and Percentages", "Actual Commitments", "890 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SubNational no percentages", "Actual Commitments", "75 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity Linked With Pledge", "Actual Commitments", "50 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "Actual Commitments", "50 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with primary_program", "Actual Commitments", "32 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with tertiary_program", "Actual Commitments", "15 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged education activity 1", "Actual Commitments", "5 000 000", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000")  );
		assertNull("something", correctReport.getDifferenceAgainst(rep.reportContents));
		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
}
