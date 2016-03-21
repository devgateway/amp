package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
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
public class AmpSchemaSortingTests extends SortingSanityChecks {

	public AmpSchemaSortingTests() {
		super("AmpSchemaSortingTests sanity tests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	
	@Test
	public void testSortingByPPC() {
		NiReportModel cor = new NiReportModel("sort by ppc")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 5))",
						"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
						"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Proposed Project Amount", "4,781,901,72", "Donor Agency", "", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance"),
				        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
				        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Totals-Actual Disbursements", "545,000"),
				        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP"),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID", "Totals-Actual Commitments", "333,222"),
				        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "333,333"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland", "Totals-Actual Commitments", "570,000"),
				        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway", "Totals-Actual Commitments", "890,000"),
				        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID", "Totals-Actual Commitments", "5,000,000"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "USAID", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland", "Totals-Actual Commitments", "12,000"),
				        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland", "Totals-Actual Commitments", "123,321"),
				        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP", "Totals-Actual Commitments", "100"),
				        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP", "Totals-Actual Commitments", "45,000"),
				        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "UNDP, USAID", "Totals-Actual Commitments", "1,200"),
				        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland", "Totals-Actual Commitments", "123,000"),
				        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Totals-Actual Disbursements", "770"),
				        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland", "Totals-Actual Commitments", "150,000"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "UNDP", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Disbursements", "90,000"),
				        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, USAID", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "97,562,98"),
				        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35,000", "Donor Agency", "World Bank", "Totals-Actual Commitments", "32,000"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60,000", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66,392,25", "Donor Agency", "UNDP", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70,000", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "15,000"),
				        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Proposed Project Amount", "150,999", "Donor Agency", "Finland", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1,000,000"),
				        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3,399,510,47")));
		
		ReportSpecificationImpl spec = buildSpecification("sort by ppc", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PROPOSED_PROJECT_AMOUNT, ColumnConstants.DONOR_AGENCY), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		spec.addSorter(new SortingInfo(new ReportColumn(ColumnConstants.PROPOSED_PROJECT_AMOUNT), true));
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testSortingByDirectedMtef() {
		NiReportModel cor = new NiReportModel("sort by directed MTEF")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Real MTEF 2011/2012: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 1, colSpan: 2));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 4))",
						"(Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1));(Real MTEF: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
						"(EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Real MTEF 2011/2012-EXEC-BENF", "50,000", "Real MTEF 2011/2012-IMPL-EXEC", "110,500", "Donor Agency", "", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802", "Totals-Real MTEF-EXEC-BENF", "50,000", "Totals-Real MTEF-IMPL-EXEC", "110,500")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Real MTEF 2011/2012-EXEC-BENF", "50,000", "Real MTEF 2011/2012-IMPL-EXEC", "110,500", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "123,456", "Totals-Real MTEF-EXEC-BENF", "50,000", "Totals-Real MTEF-IMPL-EXEC", "110,500"),
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
				        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance"),
				        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
				        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Totals-Actual Disbursements", "545,000"),
				        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP"),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID", "Totals-Actual Commitments", "333,222"),
				        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Totals-Actual Commitments", "333,333"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland", "Totals-Actual Commitments", "570,000"),
				        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway", "Totals-Actual Commitments", "890,000"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Agency", "Finland", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Agency", "UNDP", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Agency", "World Bank", "Totals-Actual Commitments", "32,000"),
				        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy", "Totals-Actual Commitments", "15,000"),
				        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID", "Totals-Actual Commitments", "5,000,000"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "USAID", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland", "Totals-Actual Commitments", "12,000"),
				        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland", "Totals-Actual Commitments", "123,321"),
				        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP", "Totals-Actual Commitments", "100"),
				        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP", "Totals-Actual Commitments", "45,000"),
				        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "UNDP, USAID", "Totals-Actual Commitments", "1,200"),
				        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland", "Totals-Actual Commitments", "123,000"),
				        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Totals-Actual Disbursements", "770"),
				        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland", "Totals-Actual Commitments", "150,000"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "UNDP", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Disbursements", "90,000"),
				        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, USAID", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Ministry of Finance", "Totals-Actual Commitments", "97,562,98")      ));

		ReportSpecificationImpl spec = buildSpecification("sort by directed MTEF", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, "Real MTEF 2011/2012", ColumnConstants.DONOR_AGENCY), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		spec.addSorter(new SortingInfo(Arrays.asList("Real MTEF 2011/2012", "EXEC-BENF"), SortingInfo.ROOT_PATH_NONE, false));
		runNiTestCase(spec, "en", acts, cor);
	}
	
	public void testSortingByFlowTotal() {
		NiReportModel cor = new NiReportModel("sort by Real Disb Flow")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6))",
					"(Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 5))",
					"(DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Donor Agency", "", "Totals-Actual Disbursements", "3,206,802", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Real Disbursements-EXEC-IMPL", "100,000", "Totals-Real Disbursements-IMPL-BENF", "15,000", "Totals-Real Disbursements-IMPL-EXEC", "44,333")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Totals-Actual Disbursements", "143,777", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Real Disbursements-IMPL-EXEC", "44,333"),
			        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Totals-Actual Disbursements", "123,321"),
			        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Totals-Actual Disbursements", "453,213"),
			        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
			        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
			        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance"),
			        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
			        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Totals-Actual Disbursements", "545,000", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-EXEC-IMPL", "100,000", "Totals-Real Disbursements-IMPL-BENF", "15,000"),
			        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP"),
			        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Disbursements", "72,000"),
			        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID"),
			        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Totals-Actual Disbursements", "555,111"),
			        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Totals-Actual Disbursements", "131,845"),
			        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Agency", "Ministry of Economy"),
			        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Agency", "UNDP"),
			        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Agency", "World Bank"),
			        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy"),
			        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID"),
			        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "USAID", "Totals-Actual Disbursements", "450,000"),
			        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Totals-Actual Disbursements", "80,000"),
			        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland", "Totals-Actual Disbursements", "50,000"),
			        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP"),
			        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP"),
			        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Totals-Actual Disbursements", "321,765"),
			        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "UNDP, USAID"),
			        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Ministry of Finance"),
			        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Totals-Actual Disbursements", "770"),
			        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Agency", "Ministry of Economy"),
			        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "UNDP", "Totals-Actual Disbursements", "110,000"),
			        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance", "Totals-Actual Disbursements", "90,000"),
			        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, USAID", "Totals-Actual Disbursements", "80,000"),
			        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Ministry of Finance")      ));
		
		ReportSpecificationImpl spec = buildSpecification("sort by Real Disb Flow", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY), 
			Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.REAL_DISBURSEMENTS),
			null, 
			GroupingCriteria.GROUPING_TOTALS_ONLY);
	
		spec.addSorter(new SortingInfo(Arrays.asList(MeasureConstants.REAL_DISBURSEMENTS, "DN-IMPL"), SortingInfo.ROOT_PATH_TOTALS, false));
		runNiTestCase(spec, "en", acts, cor);
	}
}
