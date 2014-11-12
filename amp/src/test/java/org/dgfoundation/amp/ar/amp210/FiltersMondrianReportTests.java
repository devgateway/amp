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
	public void test_AMP_18514_Group_Filters() {
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Primary Sector", "Report Totals", "Project Title", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Primary Sector", "110 - EDUCATION Totals", "Project Title", "", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Primary Sector", "110 - EDUCATION", "Project Title", "activity with primary_program", "2014-Actual Commitments", "32 000", "Total Measures-Actual Commitments", "32 000")    )  );
		
		ReportSpecificationImpl spec = (ReportSpecificationImpl) buildSpecification("test_AMP_18514_Group_Filters",
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
				GroupingCriteria.GROUPING_YEARLY);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID), new FilterRule("6237", true, false));
		filters.addFilterRule(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID), new FilterRule("6257", true, false));
		spec.setFilters(filters);
		
		runMondrianTestCase(
				spec,
				"en",
				Arrays.asList("activity with primary_program"),
				cor
		);
	}
}
