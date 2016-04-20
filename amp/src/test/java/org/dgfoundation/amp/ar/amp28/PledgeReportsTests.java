package org.dgfoundation.amp.ar.amp28;


import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.AmpReportModifier;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.PledgesFilterModifier;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * pledge-reports tests
 * @author Dolghier Constantin
 *
 */
public class PledgeReportsTests extends ReportsTestCase
{
	
	private PledgeReportsTests(String name)
	{
		super(name);		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(PledgeReportsTests.class.getName());
		suite.addTest(new PledgeReportsTests("testPledgeDateRange"));
		suite.addTest(new PledgeReportsTests("testRichPledgeByRelatedProject"));
		suite.addTest(new PledgeReportsTests("testRichPledgeFlat"));
		suite.addTest(new PledgeReportsTests("testRichPledgeByPrimarySector"));
		suite.addTest(new PledgeReportsTests("testRichPledgeBySecondarySector"));
		suite.addTest(new PledgeReportsTests("testRichPledgeByRegion"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterByAidModality"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterByTypeOfAssistance"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterBySector"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterByRegion"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterByProgramNothing"));
		suite.addTest(new PledgeReportsTests("testPledgeFilterByProgramSomething"));
		suite.addTest(new PledgeReportsTests("testPledgeStatusFlat"));
		suite.addTest(new PledgeReportsTests("testPledgeStatusHier"));
		return suite;
	}
	
	public void testPledgeStatusFlat(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16003-flat",
				ColumnReportDataModel.withColumns("AMP-16003-flat",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledge Status", "Test pledge 1", "second status", "Heavily used pledge", "default status").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98", "Heavily used pledge", "8 200 000").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52", "Heavily used pledge", "10 000 000").setIsPledge(true)))
					.withTrailCells(null, null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5"))
				.withTrailCells(null, null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledge Status: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
		
		runReportTest("Pledge report with Pledge Status Column", "AMP-16003-flat", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}
	
	public void testPledgeStatusHier(){
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-16003-by-pledge-status",
				GroupReportModel.withColumnReports("AMP-16003-by-pledge-status",
						ColumnReportDataModel.withColumns("Pledge Status: Pledge Status Unallocated",
							SimpleColumnModel.withContents("Pledges Titles", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "free text name 2", "1 041 110,52").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52").setIsPledge(true)))
						.withTrailCells(null, "1 041 110,52", "0", "0", "1 979 180,27"),
						ColumnReportDataModel.withColumns("Pledge Status: default status",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "8 200 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "10 000 000").setIsPledge(true)))
						.withTrailCells(null, "0", "1 800 000", "8 200 000", "10 000 000"),
						ColumnReportDataModel.withColumns("Pledge Status: second status",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23").setIsPledge(true)))
						.withTrailCells(null, "1,25", "0", "1 033 244,98", "1 033 246,23"))
					.withTrailCells(null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5"))
				.withTrailCells(null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 3), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 1))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
		
		runReportTest("Pledge report with Pledge Status Hier", "AMP-16003-by-pledge-status", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}

	public void testPledgeFilterByProgramNothing(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-programs")
				.withTrailCells()
				.withPositionDigest(true);
		runReportTest("Pledge report filtered by Program", "AMP-17423-programs", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}
	
	public void testPledgeFilterByProgramSomething(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-programs",
				ColumnReportDataModel.withColumns("AMP-17423-programs",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b", "ACVL Pledge Name 2", "Subprogram p1.b").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)))
					.withTrailCells(null, null, "0", "1,25", "50 000", "1 033 244,98", "50 000", "1 971 315,98"))
				.withTrailCells(null, null, "0", "1,25", "50 000", "1 033 244,98", "50 000", "1 971 315,98")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))");
		
		AmpReportModifier modifier = new AmpReportModifier() {
			
			@Override public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {
				filter.setSelectedPrimaryPrograms(new java.util.HashSet<AmpTheme>(){{
					add(ProgramUtil.getThemeById(2L));
					add(ProgramUtil.getThemeById(3L));
				}});
				filter.postprocess();
			}};
		runReportTest("Pledge report filtered by Program", "AMP-17423-programs", new String[] {"irrelevant since this is a pledge report"}, fddr_correct, modifier, "en");
	}
	
	public void testPledgeFilterByRegion(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-regions",
				ColumnReportDataModel.withColumns("AMP-17423-regions",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Regions", "Test pledge 1", "Cahul County", "Heavily used pledge", "Anenii Noi County").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,62").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "630 000").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "516 622,49", "Heavily used pledge", "2 870 000").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "516 623,12", "Heavily used pledge", "3 500 000").setIsPledge(true)))
					.withTrailCells(null, null, "0,62", "630 000", "3 386 622,49", "4 016 623,12"))
				.withTrailCells(null, null, "0,62", "630 000", "3 386 622,49", "4 016 623,12")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Regions: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 3), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
		runReportTest("Pledge report filtered by Region", "AMP-17423-regions", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}

	public void testPledgeFilterBySector(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-sectors",
				ColumnReportDataModel.withColumns("AMP-17423-sectors",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "112 - BASIC EDUCATION", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,44").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 300 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "361 635,74", "Heavily used pledge", "8 200 000").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "5 970 000").setIsPledge(true), 
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "361 636,18", "Heavily used pledge", "10 000 000").setIsPledge(true)))
					.withTrailCells(null, null, null, "0", "0,44", "2 670 000", "1 800 000", "3 300 000", "8 561 635,74", "5 970 000", "10 361 636,18"))
				.withTrailCells(null, null, null, "0", "0,44", "2 670 000", "1 800 000", "3 300 000", "8 561 635,74", "5 970 000", "10 361 636,18")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Secondary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		runReportTest("Pledge report filtered by Sector", "AMP-17423-sectors", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}
	
	public void testPledgeFilterByAidModality(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-aid-modality-filtered-development",
				ColumnReportDataModel.withColumns("AMP-17423-aid-modality-filtered-development",
					SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
					SimpleColumnModel.withContents("Pledges Aid Modality", "Test pledge 1", "Development of shared analytical studies").setIsPledge(true), 
					GroupColumnModel.withSubColumns("Funding",
						GroupColumnModel.withSubColumns("2012",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25").setIsPledge(true))), 
					GroupColumnModel.withSubColumns("Total Costs",
						SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25").setIsPledge(true)))
				.withTrailCells(null, null, "1,25", "1,25"))
			.withTrailCells(null, null, "1,25", "1,25")
			.withPositionDigest(true,
				"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Aid Modality: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
				"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
				"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))");

		runReportTest("Pledge report filtered by AidModality", "AMP-17423-aid-modality-filtered-development", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);
	}
	
	public void testPledgeFilterByTypeOfAssistance(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17423-type-of-assistance-filter",
				ColumnReportDataModel.withColumns("AMP-17423-type-of-assistance-filter",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Type Of Assistance", "Test pledge 1", "second type of assistance").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "265 568,98").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "265 568,98").setIsPledge(true)))
					.withTrailCells(null, null, "265 568,98", "265 568,98"))
				.withTrailCells(null, null, "265 568,98", "265 568,98")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Type Of Assistance: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
					"(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))");

		runReportTest("Pledge report filtered by ToA", "AMP-17423-type-of-assistance-filter", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}
	
	public void testRichPledgeByRegion(){
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17196-by-regions",
				GroupReportModel.withColumnReports("AMP-17196-by-regions",
						ColumnReportDataModel.withColumns("Pledges Regions: Anenii Noi County",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "Heavily used pledge", "[pledged 2, pledged education activity 1]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "630 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "934 500").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-304 500").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "2 870 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "1 155 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "1 715 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "3 500 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 089 500").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "1 410 500").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0", "0", "0", "630 000", "934 500", "-304 500", "2 870 000", "1 155 000", "1 715 000", "3 500 000", "2 089 500", "1 410 500"),
						ColumnReportDataModel.withColumns("Pledges Regions: Balti County",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,56").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,56").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "464 960,24").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "464 960,24").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "464 960,8").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "464 960,8").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0,56", "0", "0,56", "0", "0", "0", "464 960,24", "0", "464 960,24", "464 960,8", "0", "464 960,8"),
						ColumnReportDataModel.withColumns("Pledges Regions: Cahul County",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,62").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,62").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "516 622,49").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "516 622,49").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "516 623,12").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "516 623,12").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0,62", "0", "0,62", "0", "0", "0", "516 622,49", "0", "516 622,49", "516 623,12", "0", "516 623,12"),
						ColumnReportDataModel.withColumns("Pledges Regions: Lapusna County",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "Heavily used pledge", "[pledged 2, pledged education activity 1]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 170 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "1 735 500").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-565 500").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "5 330 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 145 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "3 185 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "6 500 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 880 500").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "2 619 500").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0", "0", "0", "1 170 000", "1 735 500", "-565 500", "5 330 000", "2 145 000", "3 185 000", "6 500 000", "3 880 500", "2 619 500"),
						ColumnReportDataModel.withColumns("Pledges Regions: Pledges Regions Unallocated",
							SimpleColumnModel.withContents("Pledges Titles", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "ACVL Pledge Name 2", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "ACVL Pledge Name 2", "Activity Linked With Pledge").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "free text name 2", "1 041 110,52").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "free text name 2", "1 041 110,52").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "-50 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 041 110,52").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 110,52", "0", "1 041 110,52", "0", "0", "0", "0", "50 000", "-50 000", "1 979 180,27", "50 000", "1 929 180,27"),
						ColumnReportDataModel.withColumns("Pledges Regions: Transnistrian Region",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,06").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,06").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "51 662,25").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "51 662,25").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "51 662,31").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "51 662,31").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0,06", "0", "0,06", "0", "0", "0", "51 662,25", "0", "51 662,25", "51 662,31", "0", "51 662,31"))
					.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5"))
				.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Secondary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Secondary Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pledges Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pledges National Plan Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Pledges Tertiary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Related Projects: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 20, colSpan: 3))",
					"(line 1:RHLC 1998: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 3))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1))");

		runReportTest("Rich pledge report, by region", "AMP-17196-by-regions", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}
	
	
	public void testRichPledgeByPrimarySector(){
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17196-by-pledge-primary-sector",
				GroupReportModel.withColumnReports("AMP-17196-by-pledge-primary-sector",
						ColumnReportDataModel.withColumns("Pledges sectors: 112 - BASIC EDUCATION",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "Heavily used pledge", "[pledged 2, pledged education activity 1]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,44").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,44").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-870 000").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "361 635,74", "Heavily used pledge", "8 200 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 300 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "361 635,74", "Heavily used pledge", "4 900 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "361 636,18", "Heavily used pledge", "10 000 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "5 970 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "361 636,18", "Heavily used pledge", "4 030 000").setIsPledge(true)))
						.withTrailCells(null, null, "0", "0", "0", "0,44", "0", "0,44", "1 800 000", "2 670 000", "-870 000", "8 561 635,74", "3 300 000", "5 261 635,74", "10 361 636,18", "5 970 000", "4 391 636,18"),
						ColumnReportDataModel.withColumns("Pledges sectors: 113 - SECONDARY EDUCATION",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,81").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,81").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "671 609,24").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "671 609,24").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "671 610,05").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "671 610,05").setIsPledge(true)))
						.withTrailCells(null, null, "0", "0", "0", "0,81", "0", "0,81", "0", "0", "0", "671 609,24", "0", "671 609,24", "671 610,05", "0", "671 610,05"),
						ColumnReportDataModel.withColumns("Pledges sectors: Pledges Sectors Unallocated",
							SimpleColumnModel.withContents("Pledges Titles", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "ACVL Pledge Name 2", "Activity Linked With Pledge").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "free text name 2", "1 041 110,52").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "free text name 2", "1 041 110,52").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "-50 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 041 110,52").setIsPledge(true)))
						.withTrailCells(null, null, "938 069,75", "0", "938 069,75", "1 041 110,52", "0", "1 041 110,52", "0", "0", "0", "0", "50 000", "-50 000", "1 979 180,27", "50 000", "1 929 180,27"))
					.withTrailCells(null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5"))
				.withTrailCells(null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Related Projects: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 3))",
					"(line 1:RHLC 1998: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 3))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1))");
		
				runReportTest("Rich pledge report, by primary sector", "AMP-17196-by-pledge-primary-sector", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}	
	
	public void testRichPledgeBySecondarySector(){
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17196-by-sec-sector",
				GroupReportModel.withColumnReports("AMP-17196-by-sec-sector",
						ColumnReportDataModel.withColumns("Pledges Secondary Sectors: 1-DEMOCRATIC COUNTRY",
							SimpleColumnModel.withContents("Pledges Titles", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "ACVL Pledge Name 2", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "ACVL Pledge Name 2", "Activity Linked With Pledge").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "free text name 2", "1 041 110,52").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "free text name 2", "1 041 110,52").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "-50 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 041 110,52").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 110,52", "0", "1 041 110,52", "0", "0", "0", "0", "50 000", "-50 000", "1 979 180,27", "50 000", "1 929 180,27"),
						ColumnReportDataModel.withColumns("Pledges Secondary Sectors: 3 NATIONAL COMPETITIVENESS",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,69").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,69").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "568 284,74").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "568 284,74").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "568 285,43").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "568 285,43").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0,69", "0", "0,69", "0", "0", "0", "568 284,74", "0", "568 284,74", "568 285,43", "0", "568 285,43"),
						ColumnReportDataModel.withColumns("Pledges Secondary Sectors: 5 REGIONAL DEVELOPMENT",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "0,56").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "0,56").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "464 960,24").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "464 960,24").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "464 960,8").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "464 960,8").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0,56", "0", "0,56", "0", "0", "0", "464 960,24", "0", "464 960,24", "464 960,8", "0", "464 960,8"),
						ColumnReportDataModel.withColumns("Pledges Secondary Sectors: Pledges Secondary Sectors Unallocated",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(true), 
							SimpleColumnModel.withContents("Related Projects", "Heavily used pledge", "[pledged 2, pledged education activity 1]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-870 000").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "8 200 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 300 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 900 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "10 000 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "5 970 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 030 000").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0", "0", "0", "1 800 000", "2 670 000", "-870 000", "8 200 000", "3 300 000", "4 900 000", "10 000 000", "5 970 000", "4 030 000"))
					.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5"))
				.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Secondary Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pledges National Plan Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pledges Tertiary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Pledges Regions: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Related Projects: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 20, colSpan: 3))",
					"(line 1:RHLC 1998: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 3))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1))");

		runReportTest("Rich pledge report, by secondary sector", "AMP-17196-by-sec-sector", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}	
	
	
	public void testRichPledgeFlat(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17196-flat",
				ColumnReportDataModel.withColumns("AMP-17196-flat",
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b", "ACVL Pledge Name 2", "Subprogram p1.b").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
						SimpleColumnModel.withContents("Related Projects", "ACVL Pledge Name 2", "Activity Linked With Pledge", "Heavily used pledge", "[pledged 2, pledged education activity 1]").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Regions", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("1998",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-870 000").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98", "Heavily used pledge", "8 200 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000", "Heavily used pledge", "3 300 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1 033 244,98", "ACVL Pledge Name 2", "-50 000", "Heavily used pledge", "4 900 000").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52", "Heavily used pledge", "10 000 000").setIsPledge(true), 
							SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000", "Heavily used pledge", "5 970 000").setIsPledge(true), 
							SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1 033 246,23", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 041 110,52", "Heavily used pledge", "4 030 000").setIsPledge(true)))
					.withTrailCells(null, null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5"))
				.withTrailCells(null, null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Secondary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Secondary Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pledges Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pledges National Plan Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Pledges Tertiary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Related Projects: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Pledges Regions: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 8, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 21, colSpan: 3))",
					"(line 1:RHLC 1998: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 3), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 3))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1))");
		
		runReportTest("Rich pledge report, flat", "AMP-17196-flat", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}
	
	public void testRichPledgeByRelatedProject(){
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17196-by-related-project",
				GroupReportModel.withColumnReports("AMP-17196-by-related-project",
						ColumnReportDataModel.withColumns("Related Projects: Activity Linked With Pledge",
							SimpleColumnModel.withContents("Pledges Titles", "ACVL Pledge Name 2", "ACVL Pledge Name 2").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "ACVL Pledge Name 2", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", MUST_BE_EMPTY).setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "-50 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "ACVL Pledge Name 2", "938 069,75").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "50 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "ACVL Pledge Name 2", "888 069,75").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "0", "0", "0", "0", "0", "0", "0", "50 000", "-50 000", "938 069,75", "50 000", "888 069,75"),
						ColumnReportDataModel.withColumns("Related Projects: Related Projects Unallocated",
							SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "free text name 2", "free text name 2").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", "Test pledge 1", "Subprogram p1.b").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1 033 244,98").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23", "free text name 2", "1 041 110,52").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Test pledge 1", "1 033 246,23", "free text name 2", "1 041 110,52").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "1 041 111,77", "0", "1 041 111,77", "0", "0", "0", "1 033 244,98", "0", "1 033 244,98", "2 074 356,75", "0", "2 074 356,75"),
						ColumnReportDataModel.withColumns("Related Projects: pledged 2",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-870 000").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "8 200 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 900 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "10 000 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 670 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 030 000").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0", "0", "0", "1 800 000", "2 670 000", "-870 000", "8 200 000", "0", "4 900 000", "10 000 000", "2 670 000", "4 030 000"),
						ColumnReportDataModel.withColumns("Related Projects: pledged education activity 1",
							SimpleColumnModel.withContents("Pledges Titles", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges sectors", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Secondary Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Programs", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges National Plan Objectives", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Tertiary Sectors", MUST_BE_EMPTY).setIsPledge(true), 
							SimpleColumnModel.withContents("Pledges Regions", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(true), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("1998",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Pledge", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", MUST_BE_EMPTY).setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "-870 000").setIsPledge(true)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "8 200 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 300 000").setIsPledge(true), 
									SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 900 000").setIsPledge(true))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "10 000 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 300 000").setIsPledge(true), 
								SimpleColumnModel.withContents("Commitment Gap", "Heavily used pledge", "4 030 000").setIsPledge(true)))
						.withTrailCells(null, null, null, null, null, null, null, null, "0", "0", "0", "0", "0", "0", "1 800 000", "0", "-870 000", "8 200 000", "3 300 000", "4 900 000", "10 000 000", "3 300 000", "4 030 000"))
					.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5"))
				.withTrailCells(null, null, null, null, null, null, null, null, "938 069,75", "0", "938 069,75", "1 041 111,77", "0", "1 041 111,77", "1 800 000", "2 670 000", "-870 000", "9 233 244,98", "3 350 000", "5 883 244,98", "13 012 426,5", "6 020 000", "6 992 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Secondary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Secondary Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Pledges Programs: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Pledges National Plan Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Pledges Tertiary Sectors: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Pledges Regions: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 20, colSpan: 3))",
					"(line 1:RHLC 1998: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 3), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 3), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 3), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 3))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1), RHLC Commitment Gap: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1))");
		
		runReportTest("Rich pledge report by related project", "AMP-17196-by-related-project", new String[] {"irrelevant since this is a pledge report"}, fddr_correct);					
	}
	
	public void testPledgeDateRange()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16007-date-range-columns",
				ColumnReportDataModel.withColumns("AMP-16007-date-range-columns",
						SimpleColumnModel.withContents("Pledges Detail Date Range", "Test pledge 1", "[2012-06-06 - 2014-04-04, 2014-04-01 - 2014-04-16, 2014-04-18 - 2014-04-24]", "free text name 2", "2012-03-02 - 2015-03-03", "Heavily used pledge", "[2013-02-01 - 2014-04-29, 2014-04-08 - 2015-02-11]").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Detail End Date", "Test pledge 1", "[04/04/2014, 16/04/2014, 24/04/2014]", "free text name 2", "03/03/2015", "Heavily used pledge", "[29/04/2014, 11/02/2015]").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Detail Start Date", "Test pledge 1", "[06/06/2012, 01/04/2014, 18/04/2014]", "free text name 2", "02/03/2012", "Heavily used pledge", "[01/02/2013, 08/04/2014]").setIsPledge(true), 
						SimpleColumnModel.withContents("Pledges Titles", "Test pledge 1", "Test pledge 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2", "Heavily used pledge", "Heavily used pledge").setIsPledge(true), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1,25", "free text name 2", "1 041 110,52").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Pledge", "Heavily used pledge", "1 800 000").setIsPledge(true)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 244,98", "Heavily used pledge", "8 200 000").setIsPledge(true))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Pledge", "Test pledge 1", "1 033 246,23", "ACVL Pledge Name 2", "938 069,75", "free text name 2", "1 041 110,52", "Heavily used pledge", "10 000 000").setIsPledge(true)))
					.withTrailCells(null, null, null, null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5"))
				.withTrailCells(null, null, null, null, "1 041 111,77", "1 800 000", "9 233 244,98", "13 012 426,5")
				.withPositionDigest(true,
					"(line 0:RHLC Pledges Detail Date Range: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Pledges Detail End Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Pledges Detail Start Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Pledges Titles: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 3), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 1))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1))",
					"(line 2:RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Pledge: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))");
		
		runReportTest("Basic pledge report with date range", "AMP-16007-date-range-columns", new String[] {"irrelevant since this is a pledge report"}, fddr_correct/*, new PledgesFilterModifier("Test pledge 1", "ACVL Pledge Name 2", "free text name 2"), null*/);
	}
	
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}