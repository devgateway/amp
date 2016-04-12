package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.junit.Test;

/**
 * filtering testcases common between both the offdb schema and the AmpReportsSchema-using one.
 * These are not supposed to be exhaustive tests; instead they are concerned about "no stupid or weird things happening"
 * @author Dolghier Constantin
 *
 */
public abstract class FilteringSanityChecks extends ReportingTestCase {
			
	public FilteringSanityChecks(String name) {
		super(name);
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
	
	protected ReportSpecificationImpl buildSpecForFiltering(String title, List<String> cols, List<String> hiers, String filterColumnName, List<Long> ids, boolean positive) {
		ReportSpecificationImpl spec = buildSpecification(title, 
				cols, 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				hiers, 
				GroupingCriteria.GROUPING_YEARLY);
		
		ReportFiltersImpl filters = new ReportFiltersImpl();
		FilterRule rule = ids.size() == 1 ? new FilterRule(ids.get(0).toString(), positive) : new FilterRule(ids.stream().map(z -> z.toString()).collect(Collectors.toList()), positive);
		filters.addFilterRule(new ReportElement(new ReportColumn(filterColumnName)), rule);
		spec.setFilters(filters);
		return spec;
	}
	
	@Test
	public void testFilteringByModeOfPaymentCash() {
		NiReportModel cor = new NiReportModel("flat mop [cash]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
						"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Commitments", "111,111", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Totals-Actual Commitments", "161,111", "Totals-Actual Disbursements", "143,777")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")      ));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [cash]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l), true);
		runNiTestCase(spec, "en", acts, cor);
	}

	@Test
	public void testFilteringByModeOfPaymentCash_DirectPayment() {
		NiReportModel cor = new NiReportModel("flat mop [cash, Direct Payment]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
						"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2013-Actual Commitments", "1,012,087", "Funding-2013-Actual Disbursements", "721,956", "Funding-2014-Actual Commitments", "161,632,14", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "1,222,386,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "2,396,105,98", "Totals-Actual Disbursements", "940,733")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [cash, Direct Payment]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l, 2094l), true);
		runNiTestCase(spec, "en", acts, cor);
	}

	
//	TODO: disabled for now - known bug	
//	@Test
//	public void testFilteringByModeOfPaymentCashNegative() {
//		NiReportModel cor = null;
//		
//		ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [NOT cash]", 
//				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l), false);
//		runNiTestCase(spec, "en", acts, cor);
//	}
	
	@Test
	public void testFilteringTypeOfAssistanceSecond() {
		NiReportModel cor = new NiReportModel("flat toa [second]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
						"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,892,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,589,081,63", "Funding-2014-Actual Disbursements", "530,000", "Totals-Actual Commitments", "7,481,303,63", "Totals-Actual Disbursements", "530,000")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City")      ));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat toa [second]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.TYPE_OF_ASSISTANCE, Arrays.asList(2124l), true);
		
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testFilteringTypeOfAssistanceNotSecond() {
		NiReportModel cor = new NiReportModel("flat toa [not second]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 18, colSpan: 2))",
						"(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "4,949,864", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "3,570,732,14", "Funding-2014-Actual Disbursements", "180,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "11,927,387,56", "Totals-Actual Disbursements", "2,676,802")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Region", "Cahul County"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
				        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", ""),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
				        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
				        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
				        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
				        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
				        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				        new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
				        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
				        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
				        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
				        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
				        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
				        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
				        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")      ));
;
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat toa [not second]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.TYPE_OF_ASSISTANCE, Arrays.asList(2124l), false);
		
		runNiTestCase(spec, "en", acts, cor);
	}

	@Test
	public void testFilterByImplementationLevel() {
		NiReportModel cor = new NiReportModel("flat impl. level [national]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 2))",
						"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2014-Actual Commitments", "133,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "637,662,98", "Totals-Actual Disbursements", "72,770")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", ""),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
				        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
				        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
				        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
				        new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
				        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
				        new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));

		ReportSpecificationImpl spec = buildSpecForFiltering("flat impl. level [national]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.IMPLEMENTATION_LEVEL, Arrays.asList(70l), true);
		
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testFilterByImplementationLevelNot() {
		NiReportModel cor = new NiReportModel("flat impl. level [not national]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
						"(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "720,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,014,081,63", "Funding-2014-Actual Disbursements", "710,000", "Funding-2015-Actual Commitments", "1,592,901", "Funding-2015-Actual Disbursements", "436,765", "Totals-Actual Commitments", "18,759,028,21", "Totals-Actual Disbursements", "3,134,032")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Region", "Drochia County"),
				        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Region", "Cahul County"),
				        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Region", "Balti County"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
				        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
				        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
				        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
				        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
				        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
				        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")));

		ReportSpecificationImpl spec = buildSpecForFiltering("flat impl. level [not national]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.IMPLEMENTATION_LEVEL, Arrays.asList(70l), false);
		
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testFilterByActivityId() {
		NiReportModel cor = new NiReportModel("flat filter by amp_activity_id")
			.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
				"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Region", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "45,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "45,000", "Totals-Actual Disbursements", "90,000")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
		        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000")));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by amp_activity_id", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.ACTIVITY_ID, Arrays.asList(77l, 64l), true);
		
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testFilterByNotActivityId() {
		NiReportModel cor2 = new NiReportModel("flat filter by [not amp_activity_id]")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
						"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "995,000")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by [not amp_activity_id]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.ACTIVITY_ID, Arrays.asList(77l, 64l), false);
		
		runNiTestCase(spec, "en", Arrays.asList("Unvalidated activity", "execution rate activity", "Eth Water", "pledged 2"), cor2);
	}
	
	@Test
	public void testFilterByDonorAgency() {
		NiReportModel cor2 = new NiReportModel("flat filter by donor agency")
			.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
				"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Region", "", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "865,000")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
		        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")));

		ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by donor agency", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.DONOR_AGENCY, Arrays.asList(21696l, 21702l, 21701l), true);
		
		runNiTestCase(spec, "en", Arrays.asList("Unvalidated activity", "execution rate activity", "Eth Water", "pledged 2"), cor2);
	}
	
	@Test
	public void testFilterByDonorGroup() {
		NiReportModel cor2 = new NiReportModel("flat filter by NOT donor group")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 19))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Donor Group: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 14));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 17, colSpan: 2))",
						"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Primary Sector", "", "Donor Group", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "6,160,643", "Funding-2013-Actual Disbursements", "691,845", "Funding-2014-Actual Commitments", "7,875,732,14", "Funding-2014-Actual Disbursements", "580,200", "Funding-2015-Actual Commitments", "387,042,84", "Funding-2015-Actual Disbursements", "115,570", "Totals-Actual Commitments", "15,761,536,98", "Totals-Actual Disbursements", "2,179,926")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "International", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Donor Group", "American", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Primary Sector", "110 - EDUCATION", "Donor Group", "National"),
				        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, European", "Funding-2013-Actual Disbursements", "525,000", "Totals-Actual Disbursements", "525,000"),
				        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Primary Sector", "110 - EDUCATION", "Donor Group", "International"),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Primary Sector", "110 - EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
				        new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Primary Sector", "110 - EDUCATION, 120 - HEALTH", "Donor Group", "European", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
				        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
				        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
				        new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Primary Sector", "110 - EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
				        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Primary Sector", "113 - SECONDARY EDUCATION", "Donor Group", "American", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
				        new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
				        new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Primary Sector", "110 - EDUCATION", "Donor Group", "American, International", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
				        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Donor Group", "American, European", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
				        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Primary Sector", "110 - EDUCATION", "Donor Group", "International", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Donor Group", "National", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
				        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Primary Sector", "110 - EDUCATION", "Donor Group", "American", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
				        new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Donor Group", "National", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")));
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat filter by NOT donor group", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_GROUP), null, ColumnConstants.DONOR_GROUP, Arrays.asList(17l), false);
		
		runNiTestCase(spec, "en", acts, cor2);
	}
}
