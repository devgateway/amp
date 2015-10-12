package org.dgfoundation.amp.ar.amp211;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.VirtualCurrenciesMaintainer;
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
	
	public OldReportsNewFeaturesTests() {
		super("currency deflator tests");
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
}
