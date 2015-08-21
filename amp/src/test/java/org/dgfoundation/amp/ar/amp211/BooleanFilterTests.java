package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.junit.Test;

/**
 * testcases for Boolean SQLfilters in the Mondrian-based RE
 * @author Constantin Dolghier
 *
 */
public class BooleanFilterTests extends MondrianReportsTestCase {

	public final static List<String> activities = Arrays.asList(
			"TAC_activity_1", "date-filters-activity", "Eth Water");
	
	public BooleanFilterTests() {
		super("boolean SQL filter tests");
	}
	
	@Test
	public void testHumanitarianAidSimpleValue() {
		ReportAreaForTests correctReportTrue = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Humanitarian Aid", "", "Actual Commitments", "213 231", "Actual Disbursements", "123 321")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "Humanitarian Aid", "Yes", "Actual Commitments", "213 231", "Actual Disbursements", "123 321"));
		
		ReportAreaForTests correctReportFalse = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Humanitarian Aid", "", "Actual Commitments", "125 000", "Actual Disbursements", "72 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "date-filters-activity", "Humanitarian Aid", "No", "Actual Commitments", "125 000", "Actual Disbursements", "72 000")  );
		
		ReportAreaForTests correctReportNull = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Humanitarian Aid", "", "Actual Commitments", "0", "Actual Disbursements", "545 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Eth Water", "Humanitarian Aid", "", "Actual Commitments", "0", "Actual Disbursements", "545 000"));
	  		
		
		ReportSpecificationImpl spec = buildSpecification("HumanitarianAid", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.HUMANITARIAN_AID), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
			null,
			GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.HUMANITARIAN_AID, FilterRule.TRUE_VALUE, true));
		runMondrianTestCase(spec, "en", activities, correctReportTrue);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.HUMANITARIAN_AID, FilterRule.FALSE_VALUE, true));
		runMondrianTestCase(spec, "en", activities, correctReportFalse);

		spec.setFilters(buildSimpleFilter(ColumnConstants.HUMANITARIAN_AID, FilterRule.NULL_VALUE, true));
		runMondrianTestCase(spec, "en", activities, correctReportNull);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.HUMANITARIAN_AID, Arrays.asList(FilterRule.FALSE_VALUE, FilterRule.TRUE_VALUE), false)); // NOT (FALSE OR TRUE) ---> NULL
		runMondrianTestCase(spec, "en", activities, correctReportNull);
		
		spec.setFilters(buildSimpleFilter(ColumnConstants.HUMANITARIAN_AID, Arrays.asList(FilterRule.NULL_VALUE, FilterRule.TRUE_VALUE), false)); // NOT (NULL OR TRUE) ---> FALSE
		runMondrianTestCase(spec, "en", activities, correctReportFalse);

	}
}
