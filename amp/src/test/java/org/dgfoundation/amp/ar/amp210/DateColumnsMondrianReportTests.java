package org.dgfoundation.amp.ar.amp210;

import java.sql.Date;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.module.common.util.DateTimeUtil;
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
	      new ReportAreaForTests().withContents("Project Title", "Project with documents", "Proposed Start Date", "07/10/2014", "Actual Start Date", "08/10/2014", "Actual Disbursements", "0", "Actual Commitments", "0"),
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
	
	@Test
	public void testActivityDateFilterConversion_AMP_19183() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
		.withContents("Project Title", "Report Totals", "Donor Agency", "", "Actual Commitments", "0", "Actual Disbursements", "0")
		.withChildren(
				new ReportAreaForTests()
				.withContents("Project Title", "Project with documents", "Donor Agency", "(Donor Agency Unspecified)", "Actual Commitments", "0", "Actual Disbursements", "0")  );
		
		runMondrianTestCase("AMP-19183", 
				Arrays.asList("Project with documents"), 
				correctResult,
				"en"
				);
	}
	
	@Test
	public void testMondrianDateFiltersSimple() {
		List<String> acts = Arrays.asList("TAC_activity_1", "Project with documents", "Eth Water", "activity with contracting agency");
		ReportSpecificationImpl spec = buildSpecification("report with activity columns",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON, ColumnConstants.ACTUAL_START_DATE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		//DateTimeUtil.toJulianDayString(Date.valueOf("2009-01-01"));
		MondrianReportFilters mrf = new MondrianReportFilters(); // positive filter ("equals")
		mrf.addDateFilterRule(ColumnConstants.ACTIVITY_CREATED_ON, new FilterRule(DateTimeUtil.toJulianDayString(Date.valueOf("2013-08-23")), true));
		spec.setFilters(mrf);
		
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Activity Created On", "", "Actual Start Date", "", "Actual Commitments", "213 231", "Actual Disbursements", "123 321")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "Activity Created On", "23/08/2013", "Actual Start Date", "", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"));
		
		spec.setDisplayEmptyFundingRows(true);
		
		runMondrianTestCase(spec, "en", acts, correctResult);
		
		mrf = new MondrianReportFilters(); // negative filter ("does not equal")
		mrf.addDateFilterRule(ColumnConstants.ACTIVITY_CREATED_ON, new FilterRule(DateTimeUtil.toJulianDayString(Date.valueOf("2013-08-23")), false));
		spec.setFilters(mrf);
		
		correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Activity Created On", "", "Actual Start Date", "", "Actual Commitments", "96 840,58", "Actual Disbursements", "595 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Project with documents", "Activity Created On", "18/11/2013", "Actual Start Date", "08/10/2014", "Actual Commitments", "0", "Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Activity Created On", "01/08/2013", "Actual Start Date", "", "Actual Commitments", "0", "Actual Disbursements", "545 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Activity Created On", "26/11/2014", "Actual Start Date", "", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"));
		runMondrianTestCase(spec, "en", acts, correctResult);
	}
	
	@Test
	public void testMondrianDateFiltersMultiple() throws AmpApiException {
		List<String> acts = Arrays.asList("TAC_activity_1", "Project with documents", "Eth Water", "activity with contracting agency");
		ReportSpecificationImpl spec = buildSpecification("report with activity columns",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON, ColumnConstants.ACTUAL_START_DATE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		//DateTimeUtil.toJulianDayString(Date.valueOf("2009-01-01"));
		MondrianReportFilters mrf = new MondrianReportFilters(); // positive filter ("equals")
		mrf.addDateFilterRule(ColumnConstants.ACTIVITY_CREATED_ON, new FilterRule(DateTimeUtil.toJulianDayString(Date.valueOf("2013-08-23")), false));
		mrf.addDateFilterRule(ColumnConstants.ACTIVITY_UPDATED_ON, new FilterRule(DateTimeUtil.toJulianDayString(Date.valueOf("2013-11-18")), false));
		mrf.addDateRangeFilterRule(new GregorianCalendar(1990, 2 - 1, 2).getTime(), new GregorianCalendar(2019, 2 - 1, 2).getTime());	
		spec.setFilters(mrf);
		
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Activity Created On", "", "Actual Start Date", "", "Actual Commitments", "96 840,58", "Actual Disbursements", "595 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Activity Created On", "01/08/2013", "Actual Start Date", "", "Actual Commitments", "0", "Actual Disbursements", "545 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Activity Created On", "26/11/2014", "Actual Start Date", "", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"));		
		spec.setDisplayEmptyFundingRows(true);
		
		runMondrianTestCase(spec, "en", acts, correctResult);
	}
}
