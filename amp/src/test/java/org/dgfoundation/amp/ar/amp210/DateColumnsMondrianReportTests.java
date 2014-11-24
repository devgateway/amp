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

public class DateColumnsMondrianReportTests extends MondrianReportsTestCase {
	
	public DateColumnsMondrianReportTests() {
		super("mondrian date columns tests");
	}
	
	@Test
	public void testActualStartDateDoesNotCrash() {
		ReportAreaForTests correctReport = null;
		
		runMondrianTestCase(
				buildSpecification("with-dates", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTUAL_START_DATE, ColumnConstants.PROPOSED_START_DATE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_YEARLY),
				"en", 
				Arrays.asList("Project with documents"),
				correctReport); 

		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
	
	@Test
	public void testNullValuesInDateColumns() {
		List<String> acts = Arrays.asList("Project with documents", "Eth Water", "pledged 2");
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Proposed Start Date", "", "Actual Start Date", "", "Actual Disbursements", "995 000", "Actual Commitments", "7 070 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Project with documents", "Proposed Start Date", "2014-10-07", "Actual Start Date", "2014-10-08", "Actual Disbursements", "0", "Actual Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Proposed Start Date", "", "Actual Start Date", "", "Actual Disbursements", "545 000", "Actual Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "pledged 2", "Proposed Start Date", "", "Actual Start Date", "", "Actual Disbursements", "450 000", "Actual Commitments", "7 070 000"));
		
		//runMondrianTestCase("AMP-18563-tab", acts, correctResult, "en");
		
		ReportSpecificationImpl spec = buildSpecification("tab with date columns",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_START_DATE, ColumnConstants.ACTUAL_START_DATE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingRows(true);
		runMondrianTestCase(
				spec,
				"en", acts, correctResult
				);
	}
}
