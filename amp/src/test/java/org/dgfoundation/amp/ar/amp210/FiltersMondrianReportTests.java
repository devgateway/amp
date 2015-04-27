/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.junit.Test;

/**
 * Tests for different filters
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersMondrianReportTests extends MondrianReportsTestCase {
	
	public FiltersMondrianReportTests() {
		super("filters mondrian tests");
	}

	@Test
	public void test_programmatic_sector_filters() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Primary Sector", "Report Totals", "Project Title", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Primary Sector", "110 - EDUCATION Totals", "Project Title", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Sector", "110 - EDUCATION", "Project Title", "activity with primary_program", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")    )  );
		
		ReportSpecificationImpl spec = buildSpecification("programmatic_sector_filter",
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
				GroupingCriteria.GROUPING_YEARLY);
		
		// test filtering by the value of the id field
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID), new FilterRule("6237", true));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID), new FilterRule("6257", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);
		
		// test filtering by the id of the id field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID), new FilterRule("6237", true));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID), new FilterRule("6257", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);

		// test filtering by the id of the value field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), new FilterRule("6237", true));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR), new FilterRule("6257", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);
	}
	
	@Test
	public void test_converted_sector_filters() {
		//throw new RuntimeException("put the cor here!!!!");
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Primary Sector", "Report Totals", "Project Title", "", "2013-Actual Commitments", "267 000", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "299 000")
	    .withChildren(new ReportAreaForTests().withContents("Primary Sector", "110 - EDUCATION Totals", "Project Title", "", "2013-Actual Commitments", "267 000", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "299 000")
	      .withChildren(new ReportAreaForTests().withContents("Primary Sector", "110 - EDUCATION", "Project Title", "Activity With Zones and Percentages", "2013-Actual Commitments", "267 000", "2014-Actual Commitments", "", "Total Measures-Actual Commitments", "267 000"),
	        new ReportAreaForTests().withContents("Primary Sector", "", "Project Title", "activity with primary_program", "2013-Actual Commitments", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")    )  );
		
		runMondrianTestCase("AMP-18514 - programmatic report filter", "AMP-18514",
				Arrays.asList("Activity With Zones and Percentages", "pledged education activity 1", "activity with primary_program"),
				correctResult, "en");
	}
	
	@Test
	public void test_plain_program_details() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 497 000", "Total Measures-Actual Commitments", "7 167 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "Subprogram p1, Subprogram p1.b", "Secondary Program Detail", "", "Tertiary Program Detail", "OP111 name", "2013-Actual Commitments", "", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with primary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "Program #1", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with tertiary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "OP112 name", "2013-Actual Commitments", "", "2014-Actual Commitments", "15 000", "Total Measures-Actual Commitments", "15 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 400 000", "Total Measures-Actual Commitments", "7 070 000")  );
		
		runMondrianTestCase("program details columns", "AMP-17190-all-details",
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"),
				correctResult, "en");
	}
	
	@Test
	public void test_primary_program_hier() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Primary Program", "Report Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 497 000", "Total Measures-Actual Commitments", "7 167 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Primary Program", "Subprogram p1 Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Program", "Subprogram p1", "Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")    ),
	      new ReportAreaForTests()
	          .withContents("Primary Program", "Subprogram p1.b Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Program", "Subprogram p1.b", "Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")    ),
	      new ReportAreaForTests()
	          .withContents("Primary Program", "Primary Program: Undefined Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 447 000", "Total Measures-Actual Commitments", "7 117 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Program", "Primary Program: Undefined", "Project Title", "activity with primary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"),
	        new ReportAreaForTests()
	              .withContents("Primary Program", "", "Project Title", "activity with tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "", "2014-Actual Commitments", "15 000", "Total Measures-Actual Commitments", "15 000"),
	        new ReportAreaForTests()
	              .withContents("Primary Program", "", "Project Title", "pledged 2", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 400 000", "Total Measures-Actual Commitments", "7 070 000")    )  );
		runMondrianTestCase("program details columns", "AMP-17190-all-programs-by-primary",
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"),
				correctResult, "en");
	}
	
	@Test
	public void test_primary_program_filter_converter() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "National Planning Objectives", "", "Primary Program", "", "Secondary Program", "", "Tertiary Program", "", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Primary Program", "Subprogram p1, Subprogram p1.b", "Secondary Program", "", "Tertiary Program", "OP1 name", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 000"));
		
		runMondrianTestCase("program details columns", "AMP-17190-all-programs-no-hier-p1",
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"),
				correctResult, "en");
	}
	
	@Test
	public void test_primary_program_filter() {
		ReportAreaForTests correctResult2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Primary Program", "", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"));
		
		ReportAreaForTests correctResult1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Primary Program", "", "2014-Actual Commitments", "82 000", "Total Measures-Actual Commitments", "82 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1, Subprogram p1.b", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with primary_program", "Primary Program", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"));

		ReportSpecificationImpl spec = buildSpecification("test-programmatic-program-filter",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_PROGRAM),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		// test filtering by the value of the id field
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule("2", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult2);
		
		// test filtering by the value of the id field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule(Arrays.asList("2", "1"), true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult1);
		
		ReportAreaForTests correctResult3 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Primary Program", "", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1.b", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"));
		
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1), new FilterRule("3", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1), new FilterRule(Arrays.asList("3", "3"), true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID), new FilterRule("3", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID), new FilterRule("3", true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);
	}
	
//	@Test(expected=RuntimeException.class)
//	public void testFilteringByValue() {
//		ReportAreaForTests correctResult2 = new ReportAreaForTests()
//    	.withContents("Project Title", "Report Totals", "Primary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
//    	.withChildren(
//    		new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"));
//	
//		ReportSpecificationImpl spec = buildSpecification("test-programmatic-program-filter",
//			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_PROGRAM),
//			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
//			null,
//			GroupingCriteria.GROUPING_YEARLY);
//	
//		// test filtering by the value of the id field
//		MondrianReportFilters filters = new MondrianReportFilters();
//		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule("2", true));
//		spec.setFilters(filters);
//	
//		Exception ex = null;
//		try {
//			runMondrianTestCase(spec, "en", 
//				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
//				correctResult2);
//		}
//		catch(Exception e) {
//			ex = e;
//		}
//		assertNotNull(ex);
//	}
	
	@Test
	public void testDateFiltersRange() throws Exception {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000"));
		
		ReportSpecificationImpl spec = buildSpecification("testDateFilters",
			Arrays.asList(ColumnConstants.PROJECT_TITLE),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addDateRangeFilterRule(new GregorianCalendar(2009, 2 - 1, 2).getTime(), new GregorianCalendar(2009, 2 - 1, 2).getTime());				
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("date-filters-activity"), correctResult);
	}
	
	@Test
	public void testDateFiltersValue() throws Exception {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000"));
		
		ReportSpecificationImpl spec = buildSpecification("testDateFilters",
			Arrays.asList(ColumnConstants.PROJECT_TITLE),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addSingleDateFilterRule(new GregorianCalendar(2009, 2 - 1, 2).getTime(), true);
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("date-filters-activity"), correctResult);
	}
	
	@Test
	public void testDateFiltersGreaterOrEqual() throws Exception {
		ReportAreaForTests correctResult = new ReportAreaForTests()
			.withContents("Project Title", "Report Totals", "2012-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
			.withChildren(
				new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "2012-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"));
		
		ReportSpecificationImpl spec = buildSpecification("testDateFilters",
			Arrays.asList(ColumnConstants.PROJECT_TITLE),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addDateRangeFilterRule(new GregorianCalendar(2012, 9 - 1, 12).getTime(), null);		
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("date-filters-activity"), correctResult);
	}
	
	@Test
	public void testDateFiltersSmallerOrEqual() throws Exception {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "2009-Actual Commitments", "100 000", "Total Measures-Actual Commitments", "100 000"));
		
		ReportSpecificationImpl spec = buildSpecification("testDateFilters",
			Arrays.asList(ColumnConstants.PROJECT_TITLE),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addDateRangeFilterRule(null, new GregorianCalendar(2009, 2 - 1, 2).getTime());
		spec.setFilters(filters);
		
 		runMondrianTestCase(spec, "en", Arrays.asList("date-filters-activity"), correctResult);
	}
	
	@Test
	public void testImplementationLevel() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Implementation Level", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 195 000", "Total Measures-Actual Disbursements", "522 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "mtef activity 1", "Implementation Level", "National", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Implementation Level", "National", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "mtef activity 2", "Implementation Level", "Provincial", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Implementation Level", "Provincial", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")  );
		
		runMondrianTestCase("AMP-18736-impl-level-location", 
			Arrays.asList("mtef activity 1", "mtef activity 2", "date-filters-activity", "pledged 2"),
			correctResult, "en");
		
		ReportSpecificationImpl spec = buildSpecification("testImplementaionLevelFilteri",
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.IMPLEMENTATION_LEVEL),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
			null,
			GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingRows(true);
			
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.IMPLEMENTATION_LEVEL), new FilterRule("69", true));
		spec.setFilters(filters);
			
		ReportAreaForTests correctResult2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Implementation Level", "", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "mtef activity 2", "Implementation Level", "Provincial", "Actual Commitments", "0", "Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Implementation Level", "Provincial", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"));
		
		runMondrianTestCase(spec, "en", 
			Arrays.asList("mtef activity 1", "mtef activity 2", "date-filters-activity", "pledged 2"), correctResult2);
	}
	
	@Test
	public void testACVFactTableColumns() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Financing Instrument", "", "Mode of Payment", "", "Type Of Assistance", "", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "903 333", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "903 333", "Total Measures-Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Project with documents", "Financing Instrument", "default financing instrument", "Mode of Payment", "Cash", "Type Of Assistance", "default type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Financing Instrument", "default financing instrument, second financing instrument", "Mode of Payment", "Direct payment", "Type Of Assistance", "default type of assistance, second type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "545 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "mtef activity 1", "Financing Instrument", "default financing instrument", "Mode of Payment", "", "Type Of Assistance", "default type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Financing Instrument", "default financing instrument, second financing instrument", "Mode of Payment", "Cash, Direct payment", "Type Of Assistance", "default type of assistance, second type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "333 333", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "333 333", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with Zones", "Financing Instrument", "default financing instrument", "Mode of Payment", "", "Type Of Assistance", "default type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "570 000", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "570 000", "Total Measures-Actual Disbursements", "0")  );
		
		runMondrianTestCase("AMP-18593-acv-facttable-filters", 
			Arrays.asList("Project with documents", "Eth Water", "mtef activity 1", "crazy funding 1", "Activity with Zones"),
			cor, "en");
		
		
		ReportSpecificationImpl spec = buildSpecification("AMP-18593-acv-facttable-filters-programmatic",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_COOPERATION, ColumnConstants.TYPE_OF_IMPLEMENTATION),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
			spec.setDisplayEmptyFundingRows(true);
		runMondrianTestCase(spec, "en", 
			Arrays.asList("Project with documents", "Eth Water", "mtef activity 1", "crazy funding 1", "Activity with Zones"), 
			cor);
		
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.FINANCING_INSTRUMENT), new FilterRule("2125", true)); // second financing instrument
		spec.setFilters(filters);
 
		ReportAreaForTests filteredCor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Financing Instrument", "", "Mode of Payment", "", "Type Of Assistance", "", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "222 222", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "222 222", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Financing Instrument", "second financing instrument", "Mode of Payment", "Direct payment", "Type Of Assistance", "second type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "crazy funding 1", "Financing Instrument", "second financing instrument", "Mode of Payment", "Direct payment", "Type Of Assistance", "second type of assistance", "Type of Cooperation", "", "Type of Implementation", "", "2013-Actual Commitments", "222 222", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "222 222", "Total Measures-Actual Disbursements", "0")  );
		
		runMondrianTestCase(spec, "en", 
			Arrays.asList("Project with documents", "Eth Water", "mtef activity 1", "crazy funding 1", "Activity with Zones"), 
			filteredCor);
	}
	
	@Test
	public void testApprovalStatusDraft() {
		List<String> activities = Arrays.asList("new activity with contracting", "Unvalidated activity", "ptc activity 1", "Eth Water");
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "12 000", "Actual Disbursements", "0")
	    .withChildren(new ReportAreaForTests().withContents("Project Title", "new activity with contracting", "Actual Commitments", "12 000", "Actual Disbursements", "0"));
		runMondrianTestCase("AMP-18485-draft-existing-new", activities, cor, "en");
	}

	@Test
	public void testApprovalStatusValidated() {
		List<String> activities = Arrays.asList("new activity with contracting", "Unvalidated activity", "ptc activity 1", "Eth Water");
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "666 777", "Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "545 000"),
	      new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777", "Actual Disbursements", "0"));
		runMondrianTestCase("AMP-18485-validated", activities, cor, "en");
	}
	
	@Test
	public void testApprovalStatusUnvalidated() {
		List<String> activities = Arrays.asList("new activity with contracting", "Unvalidated activity", "ptc activity 1", "Eth Water");
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "45 000", "Actual Disbursements", "0")
	    .withChildren(new ReportAreaForTests().withContents("Project Title", "Unvalidated activity", "Actual Commitments", "45 000", "Actual Disbursements", "0"));
		
		runMondrianTestCase("AMP-18485-unvalidated-existing-new", activities, cor, "en");
	}	
	
	@Test
	public void testAmpActivityIdFilter() {
		List<String> myaaids = Arrays.asList("24"); // eth water
		
		ReportSpecificationImpl spec = buildActivityListingReportSpec("simple report filtered by aaid");
		spec.setFilters(buildSimpleFilter(ColumnConstants.INTERNAL_USE_ID, myaaids, true));
		
		ReportAreaForTests cr1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "0", "Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "545 000"));

		runMondrianTestCase(spec, "en", Arrays.asList("Eth Water", "Pure MTEF Project", "crazy funding 1"), cr1);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.INTERNAL_USE_ID, myaaids, false));
		
		ReportAreaForTests cr2 =  new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "333 333", "Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "Actual Commitments", "0", "Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "crazy funding 1", "Actual Commitments", "333 333", "Actual Disbursements", "0"));

		runMondrianTestCase(spec, "en", Arrays.asList("Eth Water", "Pure MTEF Project", "crazy funding 1"), cr2);
	}
	
	@Test
	public void testTeamIdFilter() {
		List<String> teamid = Arrays.asList("5"); // ssc workspace
		List<String> activities = Arrays.asList("Activity with Zones", "Real SSC Activity 1", "Real SSC Activity 2", "TAC_activity_1");
		
		ReportSpecificationImpl spec = buildActivityListingReportSpec("simple report filtered by team id"); 
		spec.setFilters(buildSimpleFilter(ColumnConstants.TEAM, teamid, true));
		
		ReportAreaForTests cr1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "0", "Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Real SSC Activity 2", "Actual Commitments", "0", "Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Real SSC Activity 1", "Actual Commitments", "0", "Actual Disbursements", "0"));	
		runMondrianTestCase(spec, "en", activities, cr1);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.TEAM_ID, teamid, false));
		ReportAreaForTests cr2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "783 231", "Actual Disbursements", "123 321")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with Zones", "Actual Commitments", "570 000", "Actual Disbursements", "0"));

		
		runMondrianTestCase(spec, "en", activities, cr2);
	}
	
	@Test
	public void testAmpIdFilter() {
		List<String> ampid = Arrays.asList("872113null", "87211351"); // ssc workspace
		List<String> activities = Arrays.asList("TAC_activity_1",
				"TAC_activity_2",
				"date-filters-activity",
				"activity with contracting agency",
				"Eth Water");
		
		ReportSpecificationImpl spec = buildActivityListingReportSpec("simple report filtered by amp id"); 
		spec.setFilters(buildSimpleFilter(ColumnConstants.AMP_ID, ampid, true));
		
		ReportAreaForTests cr1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "1 434 959,58", "Actual Disbursements", "698 534")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"),
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_2", "Actual Commitments", "999 888", "Actual Disbursements", "453 213"),
	      new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "Actual Commitments", "125 000", "Actual Disbursements", "72 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"));
		runMondrianTestCase(spec, "en", activities, cr1);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.AMP_ID, ampid, false));
		ReportAreaForTests cr2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "0", "Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Actual Commitments", "0", "Actual Disbursements", "545 000"));

		runMondrianTestCase(spec, "en", activities, cr2);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.AMP_ID, "87211351", true));
		ReportAreaForTests cr3 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"));

		runMondrianTestCase(spec, "en", activities, cr3);
	}
	
	@Test
	public void testDraftFilter() {
		List<String> draft = Arrays.asList("true"); // ssc workspace
		List<String> activities = Arrays.asList("TAC_activity_1", "new activity with contracting");
		
		ReportSpecificationImpl spec = buildActivityListingReportSpec("simple report filtered by draft"); 
		
		// draft selected area
		ReportAreaForTests cr1 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "12 000", "Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "new activity with contracting", "Actual Commitments", "12 000", "Actual Disbursements", "0"));
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, draft, true));
		runMondrianTestCase(spec, "en", activities, cr1);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, "true", true));
		runMondrianTestCase(spec, "en", activities, cr1);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, "true", true));
		runMondrianTestCase(spec, "en", activities, cr1);
		
		// draft unselected area
		ReportAreaForTests cr2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "213 231", "Actual Disbursements", "123 321")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"));
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, draft, false));
		runMondrianTestCase(spec, "en", activities, cr2);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, "true", false));
		runMondrianTestCase(spec, "en", activities, cr2);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.DRAFT, "false", true));
		runMondrianTestCase(spec, "en", activities, cr2);
	}
}
