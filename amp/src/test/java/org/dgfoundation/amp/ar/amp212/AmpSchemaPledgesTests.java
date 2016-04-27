package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
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
public class AmpSchemaPledgesTests extends ReportingTestCase {
	
	public AmpSchemaPledgesTests() {
		super("AmpSchemaPledgesTests");
	}
	
	final List<String> acts = Arrays.asList(
			"activity 1 with agreement",
			"Activity 2 with multiple agreements",
			"Activity Linked With Pledge",
			"Activity with both MTEFs and Act.Comms",
			"activity with capital spending",
			"activity with components",
			"activity with contracting agency",
			"activity with directed MTEFs",
			"activity_with_disaster_response",
			"activity with funded components",
			"activity with incomplete agreement",
			"activity with many MTEFs",
			"activity with pipeline MTEFs and act. disb",
			"Activity with planned disbursements",
			"activity with primary_program",
			"Activity with primary_tertiary_program",
			"activity with tertiary_program",
			"activity-with-unfunded-components",
			"Activity with Zones",
			"Activity With Zones and Percentages",
			"crazy funding 1",
			"date-filters-activity",
			"Eth Water",
			"execution rate activity",
			"mtef activity 1",
			"mtef activity 2",
			"new activity with contracting",
			"pledged 2",
			"pledged education activity 1",
			"Project with documents",
			"Proposed Project Cost 1 - USD",
			"Proposed Project Cost 2 - EUR",
			"ptc activity 1",
			"ptc activity 2",
			"Pure MTEF Project",
			"SSC Project 1",
			"SSC Project 2",
			"SubNational no percentages",
			"TAC_activity_1",
			"TAC_activity_2",
			"Test MTEF directed",
			"third activity with agreements",
			"Unvalidated activity",
			"with weird currencies"
		);
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	
	@Test
	public void testPlainPledgeReport() {
		NiReportModel cor = new NiReportModel("AMP-16003-flat")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 5))",
					"(Pledges Titles: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 3));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
					"(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
					"(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: column Pledge Status not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Pledges Titles", "", "Funding-2012-Actual Pledge", "1 041 111,77", "Funding-2013-Actual Pledge", "1 800 000", "Funding-2014-Actual Pledge", "9 186 878,1", "Totals-Actual Pledge", "12 966 059,62")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(3), "Pledges Titles", "Test pledge 1", "Funding-2012-Actual Pledge", "1,25", "Funding-2014-Actual Pledge", "986 878,1", "Totals-Actual Pledge", "986 879,35"),
			        new ReportAreaForTests(new AreaOwner(4), "Pledges Titles", "ACVL Pledge Name 2", "Totals-Actual Pledge", "938 069,75"),
			        new ReportAreaForTests(new AreaOwner(5), "Pledges Titles", "free text name 2", "Funding-2012-Actual Pledge", "1 041 110,52", "Totals-Actual Pledge", "1 041 110,52"),
			        new ReportAreaForTests(new AreaOwner(6), "Pledges Titles", "Heavily used pledge", "Funding-2013-Actual Pledge", "1 800 000", "Funding-2014-Actual Pledge", "8 200 000", "Totals-Actual Pledge", "10 000 000")      ));
		
		runNiTestCase(spec("AMP-16003-flat"), "en", acts, cor);
	}
	
	@Test
	public void testActualPledgeActualCommitmentsByPrimarySector() {
		NiReportModel cor = new NiReportModel("AMP-17196-by-pledge-primary-sector")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
					"(Pledges sectors: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Pledges Titles: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Related Projects: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
					"(1998: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
					"(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Pledge: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: measure Commitment Gap not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Pledges sectors", "", "Pledges Titles", "", "Related Projects", "", "Funding-1998-Actual Pledge", "938 069,75", "Funding-1998-Actual Commitments", "0", "Funding-2012-Actual Pledge", "1 041 111,77", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Pledge", "1 800 000", "Funding-2013-Actual Commitments", "5 340 000", "Funding-2014-Actual Pledge", "9 186 878,1", "Funding-2014-Actual Commitments", "3 350 000", "Totals-Actual Pledge", "12 966 059,62", "Totals-Actual Commitments", "8 690 000")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Pledges sectors", "112 - BASIC EDUCATION"))
			        .withContents("Pledges Titles", "", "Related Projects", "", "Funding-1998-Actual Pledge", "0", "Funding-1998-Actual Commitments", "0", "Funding-2012-Actual Pledge", "0,44", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Pledge", "1 800 000", "Funding-2013-Actual Commitments", "5 340 000", "Funding-2014-Actual Pledge", "8 545 407,34", "Funding-2014-Actual Commitments", "3 300 000", "Totals-Actual Pledge", "10 345 407,77", "Totals-Actual Commitments", "8 640 000", "Pledges sectors", "112 - BASIC EDUCATION")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(3), "Pledges Titles", "Test pledge 1", "Funding-2012-Actual Pledge", "0,44", "Funding-2014-Actual Pledge", "345 407,34", "Totals-Actual Pledge", "345 407,77"),
			          new ReportAreaForTests(new AreaOwner(6), "Pledges Titles", "Heavily used pledge", "Related Projects", "pledged 2, pledged education activity 1", "Funding-2013-Actual Pledge", "1 800 000", "Funding-2013-Actual Commitments", "5 340 000", "Funding-2014-Actual Pledge", "8 200 000", "Funding-2014-Actual Commitments", "3 300 000", "Totals-Actual Pledge", "10 000 000", "Totals-Actual Commitments", "8 640 000")        ),
			        new ReportAreaForTests(new AreaOwner("Pledges sectors", "113 - SECONDARY EDUCATION")).withContents("Pledges Titles", "", "Related Projects", "", "Funding-1998-Actual Pledge", "0", "Funding-1998-Actual Commitments", "0", "Funding-2012-Actual Pledge", "0,81", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Pledge", "0", "Funding-2013-Actual Commitments", "0", "Funding-2014-Actual Pledge", "641 470,77", "Funding-2014-Actual Commitments", "0", "Totals-Actual Pledge", "641 471,58", "Totals-Actual Commitments", "0", "Pledges sectors", "113 - SECONDARY EDUCATION")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(3), "Pledges Titles", "Test pledge 1", "Funding-2012-Actual Pledge", "0,81", "Funding-2014-Actual Pledge", "641 470,77", "Totals-Actual Pledge", "641 471,58")        ),
			        new ReportAreaForTests(new AreaOwner("Pledges sectors", "Pledges sectors: Undefined"))
			        .withContents("Pledges Titles", "", "Related Projects", "", "Funding-1998-Actual Pledge", "938 069,75", "Funding-1998-Actual Commitments", "0", "Funding-2012-Actual Pledge", "1 041 110,52", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Pledge", "0", "Funding-2013-Actual Commitments", "0", "Funding-2014-Actual Pledge", "0", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Pledge", "1 979 180,27", "Totals-Actual Commitments", "50 000", "Pledges sectors", "Pledges sectors: Undefined")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(4), "Pledges Titles", "ACVL Pledge Name 2", "Related Projects", "Activity Linked With Pledge", "Funding-1998-Actual Pledge", "938 069,75", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Pledge", "938 069,75", "Totals-Actual Commitments", "50 000"),
			          new ReportAreaForTests(new AreaOwner(5), "Pledges Titles", "free text name 2", "Funding-2012-Actual Pledge", "1 041 110,52", "Totals-Actual Pledge", "1 041 110,52")        )      ));

		
		runNiTestCase(spec("AMP-17196-by-pledge-primary-sector"), "en", acts, cor);
	}
}
