package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.amp.AmpReportsScratchpad;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class NiComputedMeasuresTests extends ReportingTestCase {
	
	final List<String> acts = Arrays.asList(
			"expenditure class", 
			"Eth Water",
			"activity with directed MTEFs"
		);
	
	public NiComputedMeasuresTests() {
		super("NiComputedMeasuresTests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		AmpReportsScratchpad.forcedNowDate = LocalDate.of(2016, 5, 3);
		return getDbExecutor(activityNames);
	}
	
	@Test
	public void testPriorActualDisbursements() {
		NiReportModel cor = new NiReportModel("AMP-22639-prior-month")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 4))",
					"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Prior Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Previous Month Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545 000", "Funding-2015-Actual Commitments", "123 456", "Funding-2015-Actual Disbursements", "0", "Funding-2016-Actual Commitments", "62 000", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Commitments", "185 456", "Totals-Actual Disbursements", "798 700", "Totals-Prior Actual Disbursements", "87 500", "Totals-Previous Month Disbursements", "84 200")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456"),
			        new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Actual Commitments", "62 000", "Funding-2016-Actual Disbursements", "253 700", "Totals-Actual Commitments", "62 000", "Totals-Actual Disbursements", "253 700", "Totals-Prior Actual Disbursements", "87 500", "Totals-Previous Month Disbursements", "84 200")));

		runNiTestCase(cor, spec("AMP-22639-prior-month"), acts);
	}
	
	@Override
	public void tearDown() {
		AmpReportsScratchpad.forcedNowDate = null;
	}
}
