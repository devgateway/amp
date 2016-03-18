package org.dgfoundation.amp.ar.amp211;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.junit.Test;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;

/**
 * testcases for new features added into the old reports engine in AMP 2.11
 * @author Constantin Dolghier
 *
 */
public class OldReportsNewFeaturesTests extends ReportsTestCase {
	
	public static String[] activities = new String[] {"Eth Water", "pledged 2", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms"};
	public static String[] mtefActivities = new String[] {"TAC_activity_1", "Test MTEF directed", "Pure MTEF Project", "mtef activity 1", "Activity with both MTEFs and Act.Comms"};
	
	public OldReportsNewFeaturesTests() {
		super("mtef tests in old reports");
	}
	
	@Test
	public void testRealCommsDisbsByRegion() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-try-real-mtef-column-1",
			ColumnReportDataModel.withColumns("AMP-21355-try-real-mtef-column-1",
					SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
					GroupColumnModel.withSubColumns("Funding",
						GroupColumnModel.withSubColumns("2011",
							SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Real MTEFs",
								SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
								SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("2013",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2014",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2015",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
					GroupColumnModel.withSubColumns("Total Costs",
						SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
						SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000", "Eth Water", "545 000").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Real MTEFs",
							SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
							SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
				.withTrailCells(null, "0", "0", "50 000", "110 500", "2 670 000", "545 000", "0", "4 400 000", "450 000", "0", "1 011 456", "0", "0", "8 081 456", "995 000", "50 000", "110 500"))
			.withTrailCells(null, "0", "0", "50 000", "110 500", "2 670 000", "545 000", "0", "4 400 000", "450 000", "0", "1 011 456", "0", "0", "8 081 456", "995 000", "50 000", "110 500")
			.withPositionDigest(true,
				"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 13), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 14, colSpan: 4))",
				"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 4), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 3))",
				"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 13, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 15, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
				"(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1))");

		runReportTest("Real MTEF alongside Actual Commitments and Actual Disbursements", "AMP-21355-try-real-mtef-column-1", activities, 
			cor, null, "en");
	}

	@Test
	public void testRealMtefAndMtefColumns() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-and-mtef-columns",
			ColumnReportDataModel.withColumns("AMP-21355-real-mtef-and-mtef-columns",
					SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
					SimpleColumnModel.withContents("MTEF 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
					GroupColumnModel.withSubColumns("Funding",
						GroupColumnModel.withSubColumns("2011",
							SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Real MTEFs",
								SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
								SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("2013",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2014",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2015",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
					GroupColumnModel.withSubColumns("Total Costs",
						SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Real MTEFs",
							SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
							SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
				.withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500"))
			.withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500")
			.withPositionDigest(true,
				"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC MTEF 2012/2013: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 11, colSpan: 3))",
				"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2))",
				"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
				"(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		runReportTest("MTEF columns alongside Real MTEF columns for different years", "AMP-21355-real-mtef-and-mtef-columns", activities, 
			cor, null, "en");
	}
	
	@Test
	public void testRealMtefAndMtefColumnsAndRealMtefColumns() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure",
			ColumnReportDataModel.withColumns("AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure",
					SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
					SimpleColumnModel.withContents("MTEF 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
					GroupColumnModel.withSubColumns("Funding",
						GroupColumnModel.withSubColumns("2011",
							SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Real MTEFs",
								SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
								SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("2013",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2014",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false)), 
						GroupColumnModel.withSubColumns("2015",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
							SimpleColumnModel.withContents("Real MTEFs", MUST_BE_EMPTY).setIsPledge(false))), 
					GroupColumnModel.withSubColumns("Total Costs",
						SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "pledged 2", "7 070 000", "activity with directed MTEFs", "123 456").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Real MTEFs",
							SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
							SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
				.withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500"))
			.withTrailCells(null, "150 000", "0", "50 000", "110 500", "2 670 000", "0", "4 400 000", "0", "1 011 456", "0", "8 081 456", "50 000", "110 500")
			.withPositionDigest(true,
				"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC MTEF 2012/2013: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 11, colSpan: 3))",
				"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 2))",
				"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
				"(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		
		runReportTest("Real MTEF columns, MTEF columns, Real Mtefs measure", "AMP-21355-real-mtef-and-mtef-columns-and-real-mtef-measure", activities, 
			cor, null, "en");
	}
	
	@Test
	public void testRealMtefColumnsAndMeasure() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21355-real-mtef-columns-and-measure",
			ColumnReportDataModel.withColumns("AMP-21355-real-mtef-columns-and-measure",
					SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "pledged 2", "pledged 2", "Eth Water", "Eth Water", "activity with directed MTEFs", "activity with directed MTEFs").setIsPledge(false), 
					GroupColumnModel.withSubColumns("Funding",
						GroupColumnModel.withSubColumns("2011",
							GroupColumnModel.withSubColumns("Real MTEFs",
								SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
								SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false)))), 
					GroupColumnModel.withSubColumns("Total Costs",
						GroupColumnModel.withSubColumns("Real MTEFs",
							SimpleColumnModel.withContents("EXEC-BENF", "activity with directed MTEFs", "50 000").setIsPledge(false), 
							SimpleColumnModel.withContents("IMPL-EXEC", "activity with directed MTEFs", "110 500").setIsPledge(false))))
				.withTrailCells(null, "50 000", "110 500", "50 000", "110 500"))
			.withTrailCells(null, "50 000", "110 500", "50 000", "110 500")
			.withPositionDigest(true,
				"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colStart: 3, colSpan: 2))",
				"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2))",
				"(line 2:RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC Real MTEFs: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
				"(line 3:RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC EXEC-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
		
		runReportTest("Real MTEF columns, Real Mtefs measure", "AMP-21355-real-mtef-columns-and-measure", activities, 
			cor, null, "en");
	}
	
	@Test
	public void testMtefProjectionsAsColumns() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-split-mtef-projections-as-columns",
				ColumnReportDataModel.withColumns("AMP-21275-split-mtef-projections-as-columns",
						SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2011/2012", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2012/2013", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2011/2012", "mtef activity 1", "789 123").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
					.withTrailCells(null, "1 673 011", "883 888", "65 000", "0", "789 123", "150 000", "0", "213 231", "888 000", "1 101 231"))
				.withTrailCells(null, "1 673 011", "883 888", "65 000", "0", "789 123", "150 000", "0", "213 231", "888 000", "1 101 231")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pipeline MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pipeline MTEF Projections 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Projection MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Projection MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Projection MTEF Projections 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 1))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		
		runReportTest("AMP-21275-split-mtef-projections-as-columns", "AMP-21275-split-mtef-projections-as-columns", mtefActivities, cor, null, "en");
	}
	
	@Test
	public void testMtefProjectionsAsColumnsNoMtefs() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-split-mtef-projections-as-columns-small",
				ColumnReportDataModel.withColumns("AMP-21275-split-mtef-projections-as-columns-small",
						SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2012/2013", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
					.withTrailCells(null, "65 000", "150 000", "213 231", "888 000", "1 101 231"))
				.withTrailCells(null, "65 000", "150 000", "213 231", "888 000", "1 101 231")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pipeline MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Projection MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
		
		runReportTest("AMP-21275-split-mtef-projections-as-columns-small", "AMP-21275-split-mtef-projections-as-columns-small", mtefActivities, cor, null, "en");
	}
	
	@Test
	public void testMtefProjectionsAllAsColumns() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs",
				ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs",
						SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2011/2012", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2011/2012", "mtef activity 1", "789 123").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2012/2013", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
					.withTrailCells(null, "1 673 011", "883 888", "789 123", "215 000", "65 000", "150 000", "0", "0", "0", "213 231", "888 000", "1 101 231"))
				.withTrailCells(null, "1 673 011", "883 888", "789 123", "215 000", "65 000", "150 000", "0", "0", "0", "213 231", "888 000", "1 101 231")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Projection MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC MTEF 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pipeline MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Projection MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC MTEF 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Pipeline MTEF Projections 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 8, colSpan: 1), RHLC Projection MTEF Projections 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 9, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 1))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("AMP-21275-all-plain-mtefs", "AMP-21275-all-plain-mtefs", mtefActivities, cor, null, "en");
	}
	
	@Test
	public void testMtefProjectionsAllAsColumnsRare() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs-rare",
				ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs-rare",
						SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "33 888", "Activity with both MTEFs and Act.Comms", "700 000", "Test MTEF directed", "150 000", "mtef activity 1", "789 123").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2011/2012", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY).setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
					.withTrailCells(null, "1 673 011", "883 888", "215 000", "150 000", "0", "213 231", "888 000", "1 101 231"))
				.withTrailCells(null, "1 673 011", "883 888", "215 000", "150 000", "0", "213 231", "888 000", "1 101 231")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC MTEF 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pipeline MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC MTEF 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Projection MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC MTEF 2013/2014: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 1))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))");
		
		runReportTest("AMP-21275-all-plain-mtefs-rare", "AMP-21275-all-plain-mtefs-rare", mtefActivities, cor, null, "en");
	}
	
	@Test
	public void testMtefProjectionsAllAsColumnsVeryRare() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-21275-all-plain-mtefs-very-rare",
				ColumnReportDataModel.withColumns("AMP-21275-all-plain-mtefs-very-rare",
						SimpleColumnModel.withContents("Project Title", "Activity with both MTEFs and Act.Comms", "Activity with both MTEFs and Act.Comms", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "mtef activity 1", "mtef activity 1", "TAC_activity_1", "TAC_activity_1").setIsPledge(false), 
						SimpleColumnModel.withContents("Pipeline MTEF Projections 2011/2012", "Activity with both MTEFs and Act.Comms", "700 000", "Pure MTEF Project", "33 888", "Test MTEF directed", "150 000").setIsPledge(false), 
						SimpleColumnModel.withContents("MTEF 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000", "Test MTEF directed", "65 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Projection MTEF Projections 2012/2013", "Activity with both MTEFs and Act.Comms", "150 000").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2011",
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with both MTEFs and Act.Comms", "888 000", "TAC_activity_1", "213 231").setIsPledge(false)))
					.withTrailCells(null, "883 888", "215 000", "150 000", "213 231", "888 000", "1 101 231"))
				.withTrailCells(null, "883 888", "215 000", "150 000", "213 231", "888 000", "1 101 231")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pipeline MTEF Projections 2011/2012: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC MTEF 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Projection MTEF Projections 2012/2013: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
					"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))");
		
		runReportTest("AMP-21275-all-plain-mtefs-very-rare", "AMP-21275-all-plain-mtefs-very-rare", mtefActivities, cor, null, "en");
	}
	
	public static String[] expenditureClassActivities = new String[] {"execution rate activity", "ptc activity 1", "activity_with_expenditure_class_1", "another_activity_with_expenditure_class"};
	
	@Test
	public void testFlatRawExpenditureClass() { // MJAJAJAJA: CORRECT
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-22234-flat",
				ColumnReportDataModel.withColumns("AMP-22234-flat",
						SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "ptc activity 1", "ptc activity 1", "execution rate activity", "execution rate activity", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
						SimpleColumnModel.withContents("Expenditure Class", "another_activity_with_expenditure_class", "Capital Expenditure", "activity_with_expenditure_class_1", "[Capital Expenditure, Compensation / Salaries, Goods and Services]").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "75 000").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "activity_with_expenditure_class_1", "67 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "55 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "79 500").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "activity_with_expenditure_class_1", "70 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "35 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "ptc activity 1", "666 777", "activity_with_expenditure_class_1", "137 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "90 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111", "activity_with_expenditure_class_1", "154 500").setIsPledge(false)))
					.withTrailCells(null, null, "666 777", "0", "75 000", "78 222", "55 000", "79 500", "70 000", "35 000", "22 111", "814 999", "90 000", "176 611"))
				.withTrailCells(null, null, "666 777", "0", "75 000", "78 222", "55 000", "79 500", "70 000", "35 000", "22 111", "814 999", "90 000", "176 611")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Expenditure Class: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		
		runReportTest("AMP-22234-flat", "AMP-22234-flat", expenditureClassActivities, cor, null, "en");
	}
	

	@Test
	public void testAMP22449() {
		GroupReportModel cor = GroupReportModel.withGroupReports("AMP-22449-exp-class-hier",
				GroupReportModel.withColumnReports("AMP-22449-exp-class-hier",
						ColumnReportDataModel.withColumns("Expenditure Class: Expenditure Class Unallocated",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Planned Expenditures", "another_activity_with_expenditure_class", "44 678").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Planned Expenditures", "another_activity_with_expenditure_class", "44 678").setIsPledge(false)))
						.withTrailCells(null, "44 678", "44 678"))
					.withTrailCells(null, "44 678", "44 678"))
				.withTrailCells(null, "44 678", "44 678")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
					"(line 1:RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
					"(line 2:RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");
		
		String[] acts = new String[] {"another_activity_with_expenditure_class"};
		runReportTest("AMP-22449-exp-class-hier", "AMP-22449-exp-class-hier", acts, cor, null, "en");		
	}
	
	@Test
	public void testHierRawExpenditureClass() { // MJAJAJAJA: CORRECT
		GroupReportModel cor = GroupReportModel.withGroupReports("AMP-22234-hier",
				GroupReportModel.withColumnReports("AMP-22234-hier",
						ColumnReportDataModel.withColumns("Expenditure Class: Capital Expenditure",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "50 000").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111", "activity_with_expenditure_class_1", "50 000").setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "0", "50 000", "0", "0", "22 111", "0", "0", "72 111"),
						ColumnReportDataModel.withColumns("Expenditure Class: Compensation / Salaries",
							SimpleColumnModel.withContents("Project Title", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "18 000").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "18 000").setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "0", "18 000", "0", "0", "0", "0", "0", "18 000"),
						ColumnReportDataModel.withColumns("Expenditure Class: Expenditure Class Unallocated",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "ptc activity 1", "ptc activity 1", "execution rate activity", "execution rate activity", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "75 000").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "activity_with_expenditure_class_1", "67 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "55 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "11 500").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity_with_expenditure_class_1", "70 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "35 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "ptc activity 1", "666 777", "activity_with_expenditure_class_1", "137 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "90 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "86 500").setIsPledge(false)))
						.withTrailCells(null, "666 777", "0", "75 000", "78 222", "55 000", "11 500", "70 000", "35 000", "0", "814 999", "90 000", "86 500"))
					.withTrailCells(null, "666 777", "0", "75 000", "78 222", "55 000", "79 500", "70 000", "35 000", "22 111", "814 999", "90 000", "176 611"))
				.withTrailCells(null, "666 777", "0", "75 000", "78 222", "55 000", "79 500", "70 000", "35 000", "22 111", "814 999", "90 000", "176 611")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 3))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("AMP-22234-hier", "AMP-22234-hier", expenditureClassActivities, cor, null, "en");
	}

	@Test
	public void testHierFilteredExpenditureClass() {
		GroupReportModel cor = GroupReportModel.withGroupReports("AMP-22234-hier-filtered-by-capital-expenditure",
				GroupReportModel.withColumnReports("AMP-22234-hier-filtered-by-capital-expenditure",
						ColumnReportDataModel.withColumns("Expenditure Class: Capital Expenditure",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "50 000").setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "another_activity_with_expenditure_class", "22 111", "activity_with_expenditure_class_1", "50 000").setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "0", "50 000", "0", "0", "22 111", "0", "0", "72 111"),
						ColumnReportDataModel.withColumns("Expenditure Class: Expenditure Class Unallocated",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "ptc activity 1", "ptc activity 1", "execution rate activity", "execution rate activity", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "activity_with_expenditure_class_1", "67 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "55 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity_with_expenditure_class_1", "70 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "35 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "ptc activity 1", "666 777", "activity_with_expenditure_class_1", "137 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "90 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "0", "70 000", "35 000", "0", "814 999", "90 000", "0"))
					.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "50 000", "70 000", "35 000", "22 111", "814 999", "90 000", "72 111"))
				.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "50 000", "70 000", "35 000", "22 111", "814 999", "90 000", "72 111")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 3))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");

		runReportTest("AMP-22234-hier-filtered-by-capital-expenditure", "AMP-22234-hier-filtered-by-capital-expenditure", expenditureClassActivities, cor, null, "en");
	}
	
	@Test
	public void testFlatFilteredExpenditureClass() {
		GroupReportModel cor = GroupReportModel.withColumnReports("AMP-22234-flat-filtered-compensation",
				ColumnReportDataModel.withColumns("AMP-22234-flat-filtered-compensation",
						SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "ptc activity 1", "ptc activity 1", "execution rate activity", "execution rate activity", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
						SimpleColumnModel.withContents("Expenditure Class", "activity_with_expenditure_class_1", "Compensation / Salaries").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "activity_with_expenditure_class_1", "67 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "55 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "18 000").setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2015",
								SimpleColumnModel.withContents("Actual Commitments", "activity_with_expenditure_class_1", "70 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "35 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Expenditures", MUST_BE_EMPTY).setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "ptc activity 1", "666 777", "activity_with_expenditure_class_1", "137 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "90 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Expenditures", "activity_with_expenditure_class_1", "18 000").setIsPledge(false)))
					.withTrailCells(null, null, "666 777", "0", "0", "78 222", "55 000", "18 000", "70 000", "35 000", "0", "814 999", "90 000", "18 000"))
				.withTrailCells(null, null, "666 777", "0", "0", "78 222", "55 000", "18 000", "70 000", "35 000", "0", "814 999", "90 000", "18 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Expenditure Class: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))");
		
		runReportTest("AMP-22234-flat-filtered-compensation", "AMP-22234-flat-filtered-compensation", expenditureClassActivities, cor, null, "en");
	}
	
	@Test
	public void testExpenditureClassHierWithManyMeasures() {
		GroupReportModel cor = GroupReportModel.withGroupReports("AMP-22449-exp-class-hier-many-measures",
				GroupReportModel.withColumnReports("AMP-22449-exp-class-hier-many-measures",
						ColumnReportDataModel.withColumns("Expenditure Class: Expenditure Class Unallocated",
							SimpleColumnModel.withContents("Project Title", "another_activity_with_expenditure_class", "another_activity_with_expenditure_class", "ptc activity 1", "ptc activity 1", "execution rate activity", "execution rate activity", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "activity_with_expenditure_class_1", "67 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "55 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", "activity_with_expenditure_class_1", "70 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "35 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", "another_activity_with_expenditure_class", "44 678").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "another_activity_with_expenditure_class", "11 222", "ptc activity 1", "666 777", "activity_with_expenditure_class_1", "137 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "execution rate activity", "90 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Planned Expenditures", "another_activity_with_expenditure_class", "44 678").setIsPledge(false)))
						.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "0", "70 000", "35 000", "44 678", "814 999", "90 000", "44 678"),
						ColumnReportDataModel.withColumns("Expenditure Class: Goods and Services",
							SimpleColumnModel.withContents("Project Title", "activity_with_expenditure_class_1", "activity_with_expenditure_class_1").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2015",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Planned Expenditures", "activity_with_expenditure_class_1", "32 000").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false), 
								SimpleColumnModel.withContents("Planned Expenditures", "activity_with_expenditure_class_1", "32 000").setIsPledge(false)))
						.withTrailCells(null, "0", "0", "0", "0", "0", "0", "0", "0", "32 000", "0", "0", "32 000"))
					.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "0", "70 000", "35 000", "76 678", "814 999", "90 000", "76 678"))
				.withTrailCells(null, "666 777", "0", "0", "78 222", "55 000", "0", "70 000", "35 000", "76 678", "814 999", "90 000", "76 678")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 9), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 3))",
					"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 3))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Planned Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");
		
		runReportTest("AMP-22449-exp-class-hier-many-measures", "AMP-22449-exp-class-hier-many-measures", expenditureClassActivities, cor, null, "en");
	}
	
	@Test
	public void testRevisedProjectCostEUR() {
		GroupReportModel prop_cost_eur_correct = GroupReportModel.withColumnReports("AMP-22376-revised-project-amount-eur",
				ColumnReportDataModel.withColumns("AMP-22376-revised-project-amount-eur",
						SimpleColumnModel.withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD").setIsPledge(false), 
						SimpleColumnModel.withContents("Proposed Project Amount", "Proposed Project Cost 2 - EUR", "2 500 000", "Proposed Project Cost 1 - USD", "770 600").setIsPledge(false), 
						SimpleColumnModel.withContents("Revised Project Amount", "Proposed Project Cost 2 - EUR", "2 350 000", "Proposed Project Cost 1 - USD", "916 522,7").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
					.withTrailCells(null, "3 270 600", "3 266 522,7", "0", "0"))
				.withTrailCells(null, "3 270 600", "3 266 522,7", "0", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1), RHLC Proposed Project Amount: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC Revised Project Amount: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
					"(line 1:RHLC Actual Commitments: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
		
		runReportTest("AMP-22376-revised-project-amount-eur", "AMP-22376-revised-project-amount-eur", new String[] {"Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR"}, prop_cost_eur_correct);
	}
	
	@Test
	public void testRevisedProjectCostUSD() {
		GroupReportModel prop_cost_usd_correct = GroupReportModel.withColumnReports("AMP-22376-revised-project-amount",
				ColumnReportDataModel.withColumns("AMP-22376-revised-project-amount",
						SimpleColumnModel.withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD").setIsPledge(false), 
						SimpleColumnModel.withContents("Proposed Project Amount", "Proposed Project Cost 2 - EUR", "3 399 510,47", "Proposed Project Cost 1 - USD", "1 000 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Revised Project Amount", "Proposed Project Cost 2 - EUR", "3 195 539,84", "Proposed Project Cost 1 - USD", "1 217 000").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
					.withTrailCells(null, "4 399 510,47", "4 412 539,84", "0", "0"))
				.withTrailCells(null, "4 399 510,47", "4 412 539,84", "0", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1), RHLC Proposed Project Amount: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC Revised Project Amount: (startRow: 0, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
					"(line 1:RHLC Actual Commitments: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
		
		runReportTest("AMP-22376-revised-project-amount", "AMP-22376-revised-project-amount", new String[] {"Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR"}, prop_cost_usd_correct);
	}
	
	@Test
	public void testLoanFields() {
		GroupReportModel prop_cost_usd_correct = GroupReportModel.withColumnReports("AMP-22403-loan-fields",
			ColumnReportDataModel.withColumns("AMP-22403-loan-fields",
				SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "with-loan-info", "with-loan-info").setIsPledge(false), 
				SimpleColumnModel.withContents("Loan Grace Period", "with-loan-info", "[0, 15]").setIsPledge(false), 
				SimpleColumnModel.withContents("Loan Interest Rate", "with-loan-info", "[2.1, 3.4]").setIsPledge(false), 
				SimpleColumnModel.withContents("Loan Maturity Date", "with-loan-info", "[15/07/2014, 01/03/2016]").setIsPledge(false), 
				SimpleColumnModel.withContents("Loan Ratification Date", "with-loan-info", "[06/05/2015, 14/10/2015]").setIsPledge(false), 
				GroupColumnModel.withSubColumns("Funding",
					GroupColumnModel.withSubColumns("2013",
						SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333").setIsPledge(false), 
						SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
					GroupColumnModel.withSubColumns("2015",
						SimpleColumnModel.withContents("Actual Commitments", "with-loan-info", "82 000").setIsPledge(false), 
						SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
				GroupColumnModel.withSubColumns("Total Costs",
					SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "with-loan-info", "82 000").setIsPledge(false), 
					SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
			.withTrailCells(null, null, null, null, null, "333 333", "0", "82 000", "0", "415 333", "0"))
		.withTrailCells(null, null, null, null, null, "333 333", "0", "82 000", "0", "415 333", "0")
		.withPositionDigest(true,
			"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Loan Grace Period: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Loan Interest Rate: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Loan Maturity Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Loan Ratification Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 4), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
			"(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2015: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
			"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		
		runReportTest("AMP-22403-loan-fields", "AMP-22403-loan-fields", new String[] {"crazy funding 1", "with-loan-info"}, prop_cost_usd_correct);
	}
}
