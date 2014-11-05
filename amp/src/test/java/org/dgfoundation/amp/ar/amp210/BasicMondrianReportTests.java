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
		ReportSpecificationImpl spec = new ReportSpecificationImpl("fundingtype");
		spec.addColumn(MondrianReportUtils.getColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL));
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		GeneratedReport rep = this.runReportOn(spec, "en", Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"));
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		String delta = correctReport.getDifferenceAgainst(rep.reportContents); 
		assertEquals(null, delta);
		
		GeneratedReport repRu = this.runReportOn(spec, "ru", Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"));
		System.out.println(describeReportOutputInCode(rep));
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "7 181 333", "Actual Disbursements", "1 665 111")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Вода Eth", "Actual Commitments", "0", "Actual Disbursements", "660 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Проект КЮЮ 1", "Actual Commitments", "111 333", "Actual Disbursements", "555 111"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "обещание 2", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		delta = correctReportRu.getDifferenceAgainst(repRu.reportContents);
		assertEquals(null, delta);
		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
}
