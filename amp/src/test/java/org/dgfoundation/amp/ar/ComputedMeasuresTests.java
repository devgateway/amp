package org.dgfoundation.amp.ar;

import java.util.HashSet;
import java.util.List;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.Query;
import org.hibernate.cfg.*;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ComputedMeasuresTests extends ReportsTestCase
{
	public ComputedMeasuresTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(ComputedMeasuresTests.class.getName());
		suite.addTest(new ComputedMeasuresTests("testPercentageOfTotalCommitments"));
		suite.addTest(new ComputedMeasuresTests("testPercentageOfTotalCommitmentsSpuriousQColumns"));
		suite.addTest(new ComputedMeasuresTests("testActualPlannedDisbursementsCapital"));
		suite.addTest(new ComputedMeasuresTests("testDisbRatioWithRealDisbAndHier"));
		return suite;
	}
	
	public void testDisbRatioWithRealDisbAndHier()
	{
		GroupReportModel fddr_correct = 		
		GroupReportModel.withGroupReports("AMP-16651-by-region-only-disb-ratio",
				GroupReportModel.withColumnReports("AMP-16651-by-region-only-disb-ratio",
					ColumnReportDataModel.withColumns("Region: Anenii Noi County",
						SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water", "Test MTEF directed", "Test MTEF directed", "SSC Project 1", "SSC Project 1"), 
						SimpleColumnModel.withContents("Primary Sector", "Eth Water", "110 - EDUCATION", "Test MTEF directed", "110 - EDUCATION", "SSC Project 1", "110 - EDUCATION"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000", "SSC Project 1", "555 111"))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000", "Test MTEF directed", "143 777", "SSC Project 1", "555 111"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "Eth Water", "43,81", "Test MTEF directed", "11,56", "SSC Project 1", "44,63")))
					.withTrailCells(null, null, "143 777", "0", "0", "1 100 111", "1 243 888", "100"),
					ColumnReportDataModel.withColumns("Region: Balti County",
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-1", "AMP-15967-activity-1"), 
						SimpleColumnModel.withContents("Primary Sector", "AMP-15967-activity-1", "110 - EDUCATION"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "44 444")), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "55 666")), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "100 110"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "AMP-15967-activity-1", "100")))
					.withTrailCells(null, null, "0", "44 444", "55 666", "0", "100 110", "100"),
					ColumnReportDataModel.withColumns("Region: Cahul County",
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-2", "AMP-15967-activity-2", "AMP-16536-first", "AMP-16536-first"), 
						SimpleColumnModel.withContents("Primary Sector", "AMP-15967-activity-2", "110 - EDUCATION", "AMP-16536-first", "110 - EDUCATION"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "15 000")), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "50 000")), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "32 000", "AMP-16536-first", "9 000")), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-16536-first", "7 600"))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "97 000", "AMP-16536-first", "16 600"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "AMP-15967-activity-2", "85,39", "AMP-16536-first", "14,61")))
					.withTrailCells(null, null, "15 000", "50 000", "41 000", "7 600", "113 600", "100"),
					ColumnReportDataModel.withColumns("Region: Dubasari County",
						SimpleColumnModel.withContents("Project Title", "TAC_activity_1", "TAC_activity_1", "date-filters-activity", "date-filters-activity"), 
						SimpleColumnModel.withContents("Primary Sector", "TAC_activity_1", "112 - BASIC EDUCATION", "date-filters-activity", "110 - EDUCATION"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_1", "123 321", "date-filters-activity", "60 000")), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "12 000")), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_1", "123 321", "date-filters-activity", "72 000"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "TAC_activity_1", "63,14", "date-filters-activity", "36,86")))
					.withTrailCells(null, null, "183 321", "0", "12 000", "0", "195 321", "100"),
					ColumnReportDataModel.withColumns("Region: Edinet County",
						SimpleColumnModel.withContents("Project Title", "SSC Project 2", "SSC Project 2"), 
						SimpleColumnModel.withContents("Primary Sector", "SSC Project 2", "112 - BASIC EDUCATION"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845"))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "SSC Project 2", "100")))
					.withTrailCells(null, null, "0", "0", "0", "131 845", "131 845", "100"),
					ColumnReportDataModel.withColumns("Region: Falesti County",
						SimpleColumnModel.withContents("Project Title", "TAC_activity_2", "TAC_activity_2"), 
						SimpleColumnModel.withContents("Primary Sector", "TAC_activity_2", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2010",
								SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "453 213")), 
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "453 213"), 
							SimpleColumnModel.withContents("Disbursment Ratio", "TAC_activity_2", "100")))
					.withTrailCells(null, null, "453 213", "0", "0", "0", "453 213", "100"))
				.withTrailCells(null, null, "795 311", "94 444", "108 666", "1 239 556", "2 237 977", "100"))
			.withTrailCells(null, null, "795 311", "94 444", "108 666", "1 239 556", "2 237 977", "100")
				.withPositionDigest(true,
				"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
				"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
				"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Disbursment Ratio: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))");

		runReportTest("Disbursment Ratio with hierarchy and Real Disbursements", "AMP-16651-by-region-only-disb-ratio", 
				new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", "AMP-15967-activity-2", "AMP-15967-activity-1", "AMP-16536-first"}, 
				fddr_correct);	
		
	}
	
	public void testActualPlannedDisbursementsCapital()
	{
		GroupReportModel fddr_correct = 
				GroupReportModel.withColumnReports("AMP-16536",
						ColumnReportDataModel.withColumns("AMP-16536",
							SimpleColumnModel.withContents("Project Title", "AMP-16536-first", "AMP-16536-first"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Disbursements", "AMP-16536-first", "9 000"), 
									SimpleColumnModel.withContents("Actual Disbursements - Capital", "AMP-16536-first", "1 800"), 
									SimpleColumnModel.withContents("Planned Disbursements", MUST_BE_EMPTY), 
									SimpleColumnModel.withContents("Planned Disbursements - Capital", MUST_BE_EMPTY)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Disbursements", "AMP-16536-first", "7 600"), 
									SimpleColumnModel.withContents("Actual Disbursements - Capital", "AMP-16536-first", "0"), 
									SimpleColumnModel.withContents("Planned Disbursements", "AMP-16536-first", "15 000"), 
									SimpleColumnModel.withContents("Planned Disbursements - Capital", "AMP-16536-first", "4 500"))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-16536-first", "16 600"), 
								SimpleColumnModel.withContents("Actual Disbursements - Capital", "AMP-16536-first", "1 800"), 
								SimpleColumnModel.withContents("Planned Disbursements", "AMP-16536-first", "15 000"), 
								SimpleColumnModel.withContents("Planned Disbursements - Capital", "AMP-16536-first", "4 500")))
						.withTrailCells(null, "9 000", "1 800", "0", "0", "7 600", "0", "15 000", "4 500", "16 600", "1 800", "15 000", "4 500"))
					.withTrailCells(null, "9 000", "1 800", "0", "0", "7 600", "0", "15 000", "4 500", "16 600", "1 800", "15 000", "4 500")
						.withPositionDigest(true,
						"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 8), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 4))",
						"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 4), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 4))",
						"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Planned Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Planned Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Planned Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Planned Disbursements - Capital: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("Actual / Planned Disbursements - Capital", "AMP-16536", new String[] {"AMP-16536-first"}, fddr_correct);
	}
	
	
	public void testPercentageOfTotalCommitments()
	{
		GroupReportModel fptc_correct = GroupReportModel.withColumnReports("AMP-15795-percentage-of-total-commitments", 
				ColumnReportDataModel.withColumns("AMP-15795-percentage-of-total-commitments", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2013", 
									SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "ptc activity 2", "333 222"),
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000"),
									SimpleColumnModel.withContents("Total Commitments", "ptc activity 1", "666 777", "ptc activity 2", "333 222")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "ptc activity 2", "333 222"),
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000"),
								SimpleColumnModel.withContents("Percentage of Total Commitments", "Eth Water", "0", "ptc activity 1", "66,68", "ptc activity 2", "33,32")
						))).withTrailCells(null, "999 999", "545 000", "999 999", "999 999", "545 000", "100");
		runReportTest("flat Percentage of Total Commitments", "AMP-15795-percentage-of-total-commitments", new String[] {"ptc activity 1", "ptc activity 2", "Eth Water"}, fptc_correct);
	}
	
	public void testPercentageOfTotalCommitmentsSpuriousQColumns()
	{
		GroupReportModel fptc_correct = GroupReportModel.withColumnReports("AMP-15796-no-quarter-columns-for-disbursements", 
				ColumnReportDataModel.withColumns("AMP-15796-no-quarter-columns-for-disbursements", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("Region", "SSC Project 1", "Anenii Noi County", "TAC_activity_1", "Dubasari County", "TAC_activity_2", "Falesti County"),
						GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2011", 
										GroupColumnModel.withSubColumns("Q3",
												SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231"),
												SimpleColumnModel.withContents("Total Commitments", "TAC_activity_1", "213 231")
													),
										GroupColumnModel.withSubColumns("Q4",
												SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_2", "999 888"),
												SimpleColumnModel.withContents("Total Commitments", "TAC_activity_2", "999 888")
													)),
													
								GroupColumnModel.withSubColumns("2013", 
										GroupColumnModel.withSubColumns("Q3",
												SimpleColumnModel.withContents("Actual Commitments", "SSC Project 1", "111 333"),
												SimpleColumnModel.withContents("Total Commitments", "SSC Project 1", "111 333"))
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "SSC Project 1", "111 333", "TAC_activity_1", "213 231", "TAC_activity_2", "999 888"),
								SimpleColumnModel.withContents("Percentage of Total Commitments", "SSC Project 1", "8,41", "TAC_activity_1", "16,1", "TAC_activity_2", "75,49")
								)
								));						
		
		runReportTest("flat Percentage of Total Commitments", "AMP-15796-no-quarter-columns-for-disbursements", new String[] {"TAC_activity_1", "TAC_activity_2", "SSC Project 1"}, fptc_correct);
	}
}
