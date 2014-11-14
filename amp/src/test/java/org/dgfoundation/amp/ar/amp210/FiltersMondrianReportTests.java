/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
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
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID), new FilterRule("6237", true, false));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID), new FilterRule("6257", true, false));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);
		
		// test filtering by the id of the id field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID), new FilterRule("6237", true, true));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID), new FilterRule("6257", true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);

		// test filtering by the id of the value field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR), new FilterRule("6237", true, true));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR), new FilterRule("6257", true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", Arrays.asList("activity with primary_program"), cor);
	}
	
	@Test
	public void test_converted_sector_filters() {
		//throw new RuntimeException("put the cor here!!!!");
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Primary Sector", "Report Totals", "Project Title", "", "2013-Actual Commitments", "2 590 000", "2014-Actual Commitments", "3 332 000", "Total Measures-Actual Commitments", "5 922 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Primary Sector", "110 - EDUCATION Totals", "Project Title", "", "2013-Actual Commitments", "1 967 000", "2014-Actual Commitments", "3 332 000", "Total Measures-Actual Commitments", "5 299 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Primary Sector", "110 - EDUCATION", "Project Title", "Activity With Zones and Percentages", "2013-Actual Commitments", "267 000", "2014-Actual Commitments", "0", "Total Measures-Actual Commitments", "267 000"),
	        new ReportAreaForTests().withContents("Primary Sector", "", "Project Title", "activity with primary_program", "2013-Actual Commitments", "0", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"),
	        new ReportAreaForTests().withContents("Primary Sector", "", "Project Title", "pledged education activity 1", "2013-Actual Commitments", "1 700 000", "2014-Actual Commitments", "3 300 000", "Total Measures-Actual Commitments", "5 000 000")),
	      new ReportAreaForTests().withContents("Primary Sector", "120 - HEALTH Totals", "Project Title", "", "2013-Actual Commitments", "623 000", "2014-Actual Commitments", "0", "Total Measures-Actual Commitments", "623 000")
	      .withChildren(
	        new ReportAreaForTests().withContents("Primary Sector", "120 - HEALTH", "Project Title", "Activity With Zones and Percentages", "2013-Actual Commitments", "623 000", "2014-Actual Commitments", "0", "Total Measures-Actual Commitments", "623 000")));
		
		runMondrianTestCase("AMP-18514 - programmatic report filter", "AMP-18514",
				Arrays.asList("Activity With Zones and Percentages", "pledged education activity 1", "activity with primary_program"),
				correctResult, "en");
	}
	
	@Test
	public void test_plain_program_details() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 497 000", "Total Measures-Actual Commitments", "7 167 400")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "Subprogram p1, Subprogram p1.b", "Secondary Program Detail", "", "Tertiary Program Detail", "OP111 name", "2013-Actual Commitments", "0", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 100"),
	      new ReportAreaForTests().withContents("Project Title", "activity with primary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "Program #1", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 100"),
	      new ReportAreaForTests().withContents("Project Title", "activity with tertiary_program", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "OP112 name", "2013-Actual Commitments", "0", "2014-Actual Commitments", "15 000", "Total Measures-Actual Commitments", "15 100"),
	      new ReportAreaForTests().withContents("Project Title", "pledged 2", "National Planning Objectives Detail", "", "Primary Program Detail", "", "Secondary Program Detail", "", "Tertiary Program Detail", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 400 000", "Total Measures-Actual Commitments", "7 070 100"));
		
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
	              .withContents("Primary Program", "Subprogram p1", "Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")    ),
	      new ReportAreaForTests()
	          .withContents("Primary Program", "Subprogram p1.b Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Program", "Subprogram p1.b", "Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")    ),
	      new ReportAreaForTests()
	          .withContents("Primary Program", "Primary Program: Undefined Totals", "Project Title", "", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 447 000", "Total Measures-Actual Commitments", "7 117 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Program", "Primary Program: Undefined", "Project Title", "activity with primary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"),
	        new ReportAreaForTests()
	              .withContents("Primary Program", "", "Project Title", "activity with tertiary_program", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "OP1 name", "2013-Actual Commitments", "0", "2014-Actual Commitments", "15 000", "Total Measures-Actual Commitments", "15 000"),
	        new ReportAreaForTests()
	              .withContents("Primary Program", "", "Project Title", "pledged 2", "National Planning Objectives", "", "Secondary Program", "", "Tertiary Program", "", "2013-Actual Commitments", "2 670 000", "2014-Actual Commitments", "4 400 000", "Total Measures-Actual Commitments", "7 070 000")));
		runMondrianTestCase("program details columns", "AMP-17190-all-programs-by-primary",
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"),
				correctResult, "en");
	}
	
	@Test
	public void test_primary_program_filter_converter() {
		ReportAreaForTests correctResult = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "National Planning Objectives", "", "Primary Program", "", "Secondary Program", "", "Tertiary Program", "", "2014-Actual Commitments", "82 000", "Total Measures-Actual Commitments", "82 200")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "National Planning Objectives", "", "Primary Program", "Subprogram p1, Subprogram p1.b", "Secondary Program", "", "Tertiary Program", "OP1 name", "2014-Actual Commitments", "50 000", "Total Measures-Actual Commitments", "50 100"),
	      new ReportAreaForTests().withContents("Project Title", "activity with primary_program", "National Planning Objectives", "", "Primary Program", "", "Secondary Program", "", "Tertiary Program", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 100"));
		
		runMondrianTestCase("program details columns", "AMP-17190-all-programs-no-hier-p1",
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"),
				correctResult, "en");
	}
	
	@Test
	public void test_primary_program_filter() {
		ReportAreaForTests correctResult2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Primary Program", "", "2014-Actual Commitments", "57 000", "Total Measures-Actual Commitments", "57 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with primary_program", "Primary Program", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"));
		
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
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule("2", true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult2);
		
		// test filtering by the value of the id field
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule(Arrays.asList("2", "1"), true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult1);
		
		ReportAreaForTests correctResult3 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Primary Program", "", "2014-Actual Commitments", "57 000", "Total Measures-Actual Commitments", "57 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1.b", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with primary_program", "Primary Program", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000"));
		
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1), new FilterRule("3", true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1), new FilterRule(Arrays.asList("3", "3"), true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID), new FilterRule("3", true, true));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);

		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1_ID), new FilterRule("3", true, false));
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", 
				Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
				correctResult3);
	}
	
	@Test(expected=RuntimeException.class)
	public void testFilteringByValue() {
		ReportAreaForTests correctResult2 = new ReportAreaForTests()
    	.withContents("Project Title", "Report Totals", "Primary Program", "", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000")
    	.withChildren(
    		new ReportAreaForTests().withContents("Project Title", "Activity with primary_tertiary_program", "Primary Program", "Subprogram p1", "2013-Actual Commitments", "0", "2014-Actual Commitments", "25 000", "Total Measures-Actual Commitments", "25 000"));
	
		ReportSpecificationImpl spec = buildSpecification("test-programmatic-program-filter",
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_PROGRAM),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
	
		// test filtering by the value of the id field
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM), new FilterRule("2", true, false));
		spec.setFilters(filters);
	
		Exception ex = null;
		try {
			runMondrianTestCase(spec, "en", 
					Arrays.asList("Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program", "pledged 2"), 
					correctResult2);
		}
		catch(Exception e) {
			ex = e;
		}
		assertNotNull(ex);
	}
}
