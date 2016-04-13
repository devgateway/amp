package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
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
public class AmpSchemaFilteringTests extends FilteringSanityChecks {

	final List<String> flowsActs = Arrays.asList(
		"activity with directed MTEFs",
		"Activity with both MTEFs and Act.Comms",
		"mtef activity 1",
		"mtef activity 2",
		"Pure MTEF Project",
		"activity with MTEFs",
		"activity with many MTEFs",
		"Test MTEF directed",
		"activity with pipeline MTEFs and act. disb",
		"Eth Water",
		"Activity with Zones",
		"TAC_activity_2"
	);
	
	public AmpSchemaFilteringTests() {
		super("AmpSchemaFilteringTests sanity tests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	
	@Test
	public void testSavedModeOfPaymentFilter() {
		NiReportModel cor = new NiReportModel("simple-filtered-by-mode-of-payment")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 7))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 2))",
						"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "666 777", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "666 777", "Totals-Actual Disbursements", "0")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Mode of Payment", "Reimbursable", "AMP ID", "8721135", "Funding-2013-Actual Commitments", "666 777", "Totals-Actual Commitments", "666 777")));

		runNiTestCase(cor, spec("simple-filtered-by-mode-of-payment"), acts);
	}
	
	@Test
	public void testSavedModeOfPaymentFilterByRegion() {
		NiReportModel cor = new NiReportModel("simple-filtered-by-mode-of-payment-by-region")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
						"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
						"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Region", "", "Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "1 567 753", "Funding-2013-Actual Disbursements", "721 956", "Funding-2014-Actual Commitments", "111 632,14", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "1 222 386,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "2 901 771,98", "Totals-Actual Disbursements", "796 956")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
				        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "778 110", "Funding-2013-Actual Disbursements", "555 111", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "815 610", "Totals-Actual Disbursements", "555 111", "Region", "Anenii Noi County")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Mode of Payment", "Reimbursable", "AMP ID", "8721135", "Funding-2013-Actual Commitments", "666 777", "Totals-Actual Commitments", "666 777"),
				          new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Mode of Payment", "Direct payment", "AMP ID", "8721137", "Funding-2013-Actual Commitments", "111 333", "Funding-2013-Actual Disbursements", "555 111", "Totals-Actual Commitments", "111 333", "Totals-Actual Disbursements", "555 111"),
				          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Direct payment", "AMP ID", "87211340", "Funding-2014-Actual Commitments", "37 500", "Totals-Actual Commitments", "37 500")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Balti County"))
				        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "222 222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37 500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "266 400", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "526 122", "Totals-Actual Disbursements", "0", "Region", "Balti County")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Mode of Payment", "Direct payment", "AMP ID", "87211332", "Funding-2013-Actual Commitments", "222 222", "Totals-Actual Commitments", "222 222"),
				          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Mode of Payment", "Direct payment", "AMP ID", "87211340", "Funding-2014-Actual Commitments", "37 500", "Totals-Actual Commitments", "37 500"),
				          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Direct payment", "AMP ID", "87211370", "Funding-2015-Actual Commitments", "266 400", "Totals-Actual Commitments", "266 400")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Chisinau City")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "123 456", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "123 456", "Totals-Actual Disbursements", "0", "Region", "Chisinau City")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Mode of Payment", "Direct payment, Non-Cash", "AMP ID", "87211372", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Chisinau County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "110 000", "Region", "Chisinau County")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Mode of Payment", "Direct payment", "AMP ID", "87211374", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Drochia County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621 600", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "621 600", "Totals-Actual Disbursements", "0", "Region", "Drochia County")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Mode of Payment", "Direct payment", "AMP ID", "87211370", "Funding-2015-Actual Commitments", "621 600", "Totals-Actual Commitments", "621 600")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Edinet County")).withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845", "Region", "Edinet County")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Mode of Payment", "Direct payment", "AMP ID", "87211311", "Funding-2013-Actual Commitments", "567 421", "Funding-2013-Actual Disbursements", "131 845", "Totals-Actual Commitments", "567 421", "Totals-Actual Disbursements", "131 845")        ),
				        new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined"))
				        .withContents("Project Title", "", "Mode of Payment", "", "AMP ID", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "36 632,14", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "210 930,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "247 562,98", "Totals-Actual Disbursements", "0", "Region", "Region: Undefined")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Mode of Payment", "Direct payment", "AMP ID", "87211371", "Funding-2014-Actual Commitments", "33 000", "Funding-2015-Actual Commitments", "117 000", "Totals-Actual Commitments", "150 000"),
				          new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Mode of Payment", "Direct payment", "AMP ID", "87211379", "Funding-2014-Actual Commitments", "3 632,14", "Funding-2015-Actual Commitments", "93 930,84", "Totals-Actual Commitments", "97 562,98"))));

		runNiTestCase(cor, spec("simple-filtered-by-mode-of-payment-by-region"), acts);
	}
	
	@Test
	public void testSavedDonorGroupFilter() {
		NiReportModel cor = new NiReportModel("filtered-by-donor-group")
			.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
				"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Region", "", "Primary Sector", "", "Donor Agency", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "890 000", "Funding-2013-Actual Disbursements", "145 000", "Funding-2014-Actual Commitments", "82 100", "Funding-2014-Actual Disbursements", "75 200", "Funding-2015-Actual Commitments", "45 700", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Commitments", "1 231 031", "Totals-Actual Disbursements", "344 021")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Primary Sector", "112 - BASIC EDUCATION", "Donor Agency", "World Bank", "Funding-2010-Actual Disbursements", "123 321", "Funding-2011-Actual Commitments", "213 231", "Totals-Actual Commitments", "213 231", "Totals-Actual Disbursements", "123 321"),
		        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Norway", "Funding-2013-Actual Disbursements", "110 000", "Totals-Actual Disbursements", "110 000"),
		        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP"),
		        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Donor Agency", "Norway", "Funding-2013-Actual Commitments", "890 000", "Totals-Actual Commitments", "890 000"),
		        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
		        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "World Bank", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
		        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
		        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2015-Actual Commitments", "45 000", "Totals-Actual Commitments", "45 000"),
		        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700"),
		        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Primary Sector", "112 - BASIC EDUCATION", "Donor Agency", "Norway", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "500", "Totals-Actual Disbursements", "700"),
		        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Primary Sector", "110 - EDUCATION", "Donor Agency", "UNDP", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000")));

		runNiTestCase(cor, spec("filtered-by-donor-group"), acts);
	}
}
