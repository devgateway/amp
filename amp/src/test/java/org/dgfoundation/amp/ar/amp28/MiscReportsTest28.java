/**
 * 
 */
package org.dgfoundation.amp.ar.amp28;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;



/**
 * Reports tests for 2.8
 * @author Nadia Mandrescu
 */
public class MiscReportsTest28 extends ReportsTestCase {

	private MiscReportsTest28(String name) {
		super(name);
	}
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }

	public static Test suite()
	{
		TestSuite suite = new TestSuite(PledgeReportsTests.class.getName());
		suite.addTest(new MiscReportsTest28("testNoProjectsReport"));
		suite.addTest(new MiscReportsTest28("testTotalsOnlySummaryReport"));
		suite.addTest(new MiscReportsTest28("testAnnualProposedProjectCostByRegion"));
		suite.addTest(new MiscReportsTest28("testAnnualProposedProjectCostFlat"));
		suite.addTest(new MiscReportsTest28("testAnnualProposedProjectCostByDonor"));
		suite.addTest(new MiscReportsTest28("testAnnualProposedProjectDonorFlat"));
		return suite;
	}
	
	public void testAnnualProposedProjectCostFlat() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withColumnReports("AMP-18094-annual-ppc-flat",
				ColumnReportDataModel.withColumns("AMP-18094-annual-ppc-flat",
						SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "activity with APPC 1", "activity with APPC 1", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "267,77").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "500").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "300").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "123").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "activity with APPC 1", "123", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "1 067,77").setIsPledge(false)))
					.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77"))
				.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("Report With Annual Proposed Project Flat", "AMP-18094-annual-ppc-flat", new String[] {"Activity With Zones and Percentages", "pledged 2", "activity with APPC 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testAnnualProposedProjectDonorFlat() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withColumnReports("AMP-18094-annual-ppc-donor-flat",
				ColumnReportDataModel.withColumns("AMP-18094-annual-ppc-donor-flat",
						SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "activity with APPC 1", "activity with APPC 1", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
						SimpleColumnModel.withContents("Donor Agency", "pledged 2", "USAID", "activity with APPC 1", "Norway", "Activity With Zones and Percentages", "Norway").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "267,77").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "500").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "300").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "123").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "activity with APPC 1", "123", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "1 067,77").setIsPledge(false)))
					.withTrailCells(null, null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77"))
				.withTrailCells(null, null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Donor Agency: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 2))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		
		runReportTest("Report With Annual Proposed Project Cost Donor Flat", "AMP-18094-annual-ppc-donor-flat", new String[] {"Activity With Zones and Percentages", "pledged 2", "activity with APPC 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testAnnualProposedProjectCostByDonor() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withGroupReports("AMP-18094-annual-ppc-donor-hier",
				GroupReportModel.withColumnReports("AMP-18094-annual-ppc-donor-hier",
						ColumnReportDataModel.withColumns("Donor Agency: Norway",
							SimpleColumnModel.withContents("Project Title", "activity with APPC 1", "activity with APPC 1", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "267,77").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "500").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "300").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "123").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "123", "Activity With Zones and Percentages", "890 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "1 067,77").setIsPledge(false)))
						.withTrailCells(null, "0", "267,77", "0", "500", "890 000", "300", "0", "0", "123", "0", "890 123", "1 067,77"),
						ColumnReportDataModel.withColumns("Donor Agency: USAID",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "2 670 000", "0", "4 400 000", "0", "0", "0", "7 070 000", "0"))
					.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77"))
				.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("Report With Annual Proposed Project Cost by Donor", "AMP-18094-annual-ppc-donor-hier", new String[] {"Activity With Zones and Percentages", "pledged 2", "activity with APPC 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testAnnualProposedProjectCostByRegion() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withGroupReports("AMP-18094-annual-ppc-by-region",
				GroupReportModel.withColumnReports("AMP-18094-annual-ppc-by-region",
						ColumnReportDataModel.withColumns("Region: Anenii Noi County",
							SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "178 000", "0", "0", "0", "0", "0", "178 000", "0"),
						ColumnReportDataModel.withColumns("Region: Balti County",
							SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "712 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "712 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "712 000", "0", "0", "0", "0", "0", "712 000", "0"),
						ColumnReportDataModel.withColumns("Region: Cahul County",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "activity with APPC 1", "activity with APPC 1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "133,89").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "250").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "150").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "61,5").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "activity with APPC 1", "61,5").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "533,89").setIsPledge(false)))
						.withTrailCells(null, "0", "133,89", "0", "250", "2 670 000", "150", "4 400 000", "0", "61,5", "0", "7 070 061,5", "533,89"),
						ColumnReportDataModel.withColumns("Region: Chisinau City",
							SimpleColumnModel.withContents("Project Title", "activity with APPC 1", "activity with APPC 1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "80,33").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "150").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "90").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "36,9").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "36,9").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "320,33").setIsPledge(false)))
						.withTrailCells(null, "0", "80,33", "0", "150", "0", "90", "0", "0", "36,9", "0", "36,9", "320,33"),
						ColumnReportDataModel.withColumns("Region: Hincesti County",
							SimpleColumnModel.withContents("Project Title", "activity with APPC 1", "activity with APPC 1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "53,55").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "100").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "60").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "24,6").setIsPledge(false), 
									SimpleColumnModel.withContents("Annual Proposed Project Cost", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "activity with APPC 1", "24,6").setIsPledge(false), 
								SimpleColumnModel.withContents("Annual Proposed Project Cost", "activity with APPC 1", "213,55").setIsPledge(false)))
						.withTrailCells(null, "0", "53,55", "0", "100", "0", "60", "0", "0", "24,6", "0", "24,6", "213,55"))
					.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77"))
				.withTrailCells(null, "0", "267,77", "0", "500", "3 560 000", "300", "4 400 000", "0", "123", "0", "7 960 123", "1 067,77")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Annual Proposed Project Cost: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("Report With Annual Proposed Project Cost by Region", "AMP-18094-annual-ppc-by-region", new String[] {"Activity With Zones and Percentages", "pledged 2", "activity with APPC 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testTotalsOnlySummaryReport() {
		GroupReportModel summaryTotalsOnly = GroupReportModel.withColumnReports("AMP-17646",
				ColumnReportDataModel.withColumns("AMP-17646",
						SimpleColumnModel.withContents("", MUST_BE_EMPTY).setIsPledge(false), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "ptc activity 2", "333 222", "ptc activity 1", "666 777").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
					.withTrailCells(null, "1 333 332", "0"))
				.withTrailCells(null, "1 333 332", "0")
				.withPositionDigest(true,
					"(line 0:RHLC : (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
					"(line 1:RHLC Actual Commitments: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");
		
		runReportTest("Summary report with totals only", "AMP-17646", new String[] {"ptc activity 1", "ptc activity 2", "crazy funding 1"}, 
				summaryTotalsOnly, null, "en");
	}
	
	public void testNoProjectsReport() {
		GroupReportModel noProjects = GroupReportModel.withColumnReports("AMP-17400-no-projects", (ColumnReportDataModel[])null)
				.withTrailCells().withPositionDigest(true, (String[])null);
		
		runReportTest("AMP-17400 no projects", "AMP-17400-no-projects", new String[]{""}, noProjects);
	}
}
