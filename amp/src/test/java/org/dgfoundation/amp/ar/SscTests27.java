package org.dgfoundation.amp.ar;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.logic.FundingCalculationsHelper;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SscTests27 extends ReportsTestCase
{
	public SscTests27(String name) {
		super(name);
	}
		
	public static Test suite()
	{
		TestSuite suite = new TestSuite(SscTests27.class.getName());
		suite.addTest(new SscTests27("testNoFilterFlat"));
		suite.addTest(new SscTests27("testNoFilterByDonor"));
		suite.addTest(new SscTests27("testDonorFilterFlat"));
		suite.addTest(new SscTests27("testDonorFilterByDonor"));
		
		suite.addTest(new SscTests27("testActivityPreviewTotals1"));
		suite.addTest(new SscTests27("testActivityPreviewTotals2"));
		
		return suite;
	}
	
	public void testNoFilterFlat()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16688-flat",
				ColumnReportDataModel.withColumns("AMP-16688-flat",
						SimpleColumnModel.withContents("Project Title", "Real SSC Activity 2", "Real SSC Activity 2", "Real SSC Activity 1", "Real SSC Activity 1"), 
						SimpleColumnModel.withContents("Donor Agency", "Real SSC Activity 2", "Norway", "Real SSC Activity 1", "[Finland, USAID, World Bank]"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "12 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "42 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", "Real SSC Activity 1", "64 000")), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000", "Real SSC Activity 1", "150 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000", "Real SSC Activity 1", "150 000"), 
							SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "54 000"), 
							SimpleColumnModel.withContents("Triangular SSC Commitments", "Real SSC Activity 1", "64 000")))
					.withTrailCells(null, null, "0", "12 000", "0", "0", "42 000", "64 000", "175 000", "0", "0", "175 000", "54 000", "64 000"))
				.withTrailCells(null, null, "0", "12 000", "0", "0", "42 000", "64 000", "175 000", "0", "0", "175 000", "54 000", "64 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Donor Agency: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3))",
					"(line 2:RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		runReportTest("SSC report with no filter or hierarchy", "AMP-16688-flat", new String[] {"Real SSC Activity 1", "Real SSC Activity 2"}, fddr_correct);		
	}
	
	public void testNoFilterByDonor()
	{
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-16688-by-donor",
				GroupReportModel.withColumnReports("AMP-16688-by-donor",
						ColumnReportDataModel.withColumns("Donor Agency: Finland",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 1", "Real SSC Activity 1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 1", "150 000"), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 1", "150 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
						.withTrailCells(null, "0", "0", "0", "0", "35 000", "0", "150 000", "0", "0", "150 000", "35 000", "0"),
						ColumnReportDataModel.withColumns("Donor Agency: Norway",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 2", "Real SSC Activity 2"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000"), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
						.withTrailCells(null, "0", "0", "0", "0", "0", "0", "25 000", "0", "0", "25 000", "0", "0"),
						ColumnReportDataModel.withColumns("Donor Agency: USAID",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 1", "Real SSC Activity 1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "12 000"), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", "Real SSC Activity 1", "64 000")), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "12 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", "Real SSC Activity 1", "64 000")))
						.withTrailCells(null, "0", "12 000", "0", "0", "0", "64 000", "0", "0", "0", "0", "12 000", "64 000"),
						ColumnReportDataModel.withColumns("Donor Agency: World Bank",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 1", "Real SSC Activity 1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "7 000"), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "7 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
						.withTrailCells(null, "0", "0", "0", "0", "7 000", "0", "0", "0", "0", "0", "7 000", "0"))
					.withTrailCells(null, "0", "12 000", "0", "0", "42 000", "64 000", "175 000", "0", "0", "175 000", "54 000", "64 000"))
				.withTrailCells(null, "0", "12 000", "0", "0", "42 000", "64 000", "175 000", "0", "0", "175 000", "54 000", "64 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 3))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 3))",
					"(line 2:RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		runReportTest("SSC report with donor hierarchy", "AMP-16688-by-donor", new String[] {"Real SSC Activity 1", "Real SSC Activity 2"}, fddr_correct);		
	}

	public void testDonorFilterFlat()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16688-flat-finland-norway",
				ColumnReportDataModel.withColumns("AMP-16688-flat-finland-norway",
						SimpleColumnModel.withContents("Project Title", "Real SSC Activity 2", "Real SSC Activity 2", "Real SSC Activity 1", "Real SSC Activity 1"), 
						SimpleColumnModel.withContents("Donor Agency", "Real SSC Activity 2", "Norway", "Real SSC Activity 1", "Finland"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000", "Real SSC Activity 1", "150 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000", "Real SSC Activity 1", "150 000"), 
							SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
							SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
					.withTrailCells(null, null, "0", "35 000", "0", "175 000", "0", "0", "175 000", "35 000", "0"))
				.withTrailCells(null, null, "0", "35 000", "0", "175 000", "0", "0", "175 000", "35 000", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Donor Agency: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 3))",
					"(line 2:RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		runReportTest("SSC report with donor filter and no hierarchy", "AMP-16688-flat-finland-norway", new String[] {"Real SSC Activity 1", "Real SSC Activity 2"}, fddr_correct);		
	}
	
	public void testDonorFilterByDonor()
	{
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-16688-by-donor-finland-norway",
				GroupReportModel.withColumnReports("AMP-16688-by-donor-finland-norway",
						ColumnReportDataModel.withColumns("Donor Agency: Finland",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 1", "Real SSC Activity 1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 1", "150 000"), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 1", "150 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", "Real SSC Activity 1", "35 000"), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
						.withTrailCells(null, "0", "35 000", "0", "150 000", "0", "0", "150 000", "35 000", "0"),
						ColumnReportDataModel.withColumns("Donor Agency: Norway",
							SimpleColumnModel.withContents("Project Title", "Real SSC Activity 2", "Real SSC Activity 2"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Official Development Aid Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000"), 
									SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Official Development Aid Commitments", "Real SSC Activity 2", "25 000"), 
								SimpleColumnModel.withContents("Bilateral SSC Commitments", MUST_BE_EMPTY), 
								SimpleColumnModel.withContents("Triangular SSC Commitments", MUST_BE_EMPTY)))
						.withTrailCells(null, "0", "0", "0", "25 000", "0", "0", "25 000", "0", "0"))
					.withTrailCells(null, "0", "35 000", "0", "175 000", "0", "0", "175 000", "35 000", "0"))
				.withTrailCells(null, "0", "35 000", "0", "175 000", "0", "0", "175 000", "35 000", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3))",
					"(line 2:RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Official Development Aid Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Bilateral SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Triangular SSC Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))");
		runReportTest("SSC report with donor filter and donor hierarchy", "AMP-16688-by-donor-finland-norway", new String[] {"Real SSC Activity 1", "Real SSC Activity 2"}, fddr_correct);		
	}

	public void testActivityPreviewTotals1()
	{
		FundingCalculationsHelper globalHelper = new FundingCalculationsHelper();
		
		AmpActivityVersion act = ReportTestingUtils.loadActivityByName("Real SSC Activity 1");
		for(AmpFunding funding:act.getFunding())
		{
			FundingCalculationsHelper localHelper = new FundingCalculationsHelper();
			globalHelper.doCalculations(funding, "USD");
			localHelper.doCalculations(funding, "USD");
			if (funding.getAmpDonorOrgId().getName().equals("Finland"))
			{
				assertBigDecimalEquals(BigDecimal.valueOf(150000), localHelper.getTotOdaSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(35000), localHelper.getTotBilateralSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(0), localHelper.getTotTriangularSscComm().getValue());
			} else if (funding.getAmpDonorOrgId().getName().equals("USAID"))
			{
				assertEquals(BigDecimal.valueOf(0), localHelper.getTotOdaSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(12000), localHelper.getTotBilateralSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(64000), localHelper.getTotTriangularSscComm().getValue());				
			} else if (funding.getAmpDonorOrgId().getName().equals("World Bank"))
			{
				assertBigDecimalEquals(BigDecimal.valueOf(0), localHelper.getTotOdaSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(7000), localHelper.getTotBilateralSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(0), localHelper.getTotTriangularSscComm().getValue());
			} else
				throw new RuntimeException("unexpected donor org found in " + act.getName() + ": " + funding.getAmpDonorOrgId().getName());
		}
		assertBigDecimalEquals(BigDecimal.valueOf(150000), globalHelper.getTotOdaSscComm().getValue());
		assertBigDecimalEquals(BigDecimal.valueOf(54000), globalHelper.getTotBilateralSscComm().getValue());
		assertBigDecimalEquals(BigDecimal.valueOf(64000), globalHelper.getTotTriangularSscComm().getValue());
	}
	
	public void testActivityPreviewTotals2()
	{
		FundingCalculationsHelper globalHelper = new FundingCalculationsHelper();
		
		AmpActivityVersion act = ReportTestingUtils.loadActivityByName("Real SSC Activity 2");
		for(AmpFunding funding:act.getFunding())
		{
			FundingCalculationsHelper localHelper = new FundingCalculationsHelper();
			globalHelper.doCalculations(funding, "USD");
			localHelper.doCalculations(funding, "USD");
			if (funding.getAmpDonorOrgId().getName().equals("Norway"))
			{
				assertBigDecimalEquals(BigDecimal.valueOf(25000), localHelper.getTotOdaSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(0), localHelper.getTotBilateralSscComm().getValue());
				assertBigDecimalEquals(BigDecimal.valueOf(0), localHelper.getTotTriangularSscComm().getValue());
			} else
				throw new RuntimeException("unexpected donor org found in " + act.getName() + ": " + funding.getAmpDonorOrgId().getName());
		}
		assertBigDecimalEquals(BigDecimal.valueOf(25000), globalHelper.getTotOdaSscComm().getValue());
		assertBigDecimalEquals(BigDecimal.valueOf(0), globalHelper.getTotBilateralSscComm().getValue());
		assertBigDecimalEquals(BigDecimal.valueOf(0), globalHelper.getTotTriangularSscComm().getValue());
	}
	
}

