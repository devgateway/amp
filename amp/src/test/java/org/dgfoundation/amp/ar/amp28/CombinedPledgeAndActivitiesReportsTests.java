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
public class CombinedPledgeAndActivitiesReportsTests extends ReportsTestCase
{
	
	private CombinedPledgeAndActivitiesReportsTests(String name)
	{
		super(name);		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(CombinedPledgeAndActivitiesReportsTests.class.getName());
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingFlat"));
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingByPrimarySector"));
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingByPrimaryProgram"));
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingByTertiaryProgram"));
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingByRegion"));
		suite.addTest(new CombinedPledgeAndActivitiesReportsTests("testPledgeMergingFilterByPrimarySector"));
		return suite;
	}
	
	public void testPledgeMergingFilterByPrimarySector() {
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17746-test-merging-basic-education",
				ColumnReportDataModel.withColumns("AMP-17746-test-merging-basic-education",
						SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge", "pledged education activity 1", "pledged education activity 1").setIsPledge(false), 
						SimpleColumnModel.withContents("Region", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]", "Heavily used pledge", "[Anenii Noi County, Lapusna County]", "pledged education activity 1", "Chisinau County").setIsPledge(false), 
						SimpleColumnModel.withContents("Primary Sector", "Test pledge 1", "112 - BASIC EDUCATION", "Heavily used pledge", "112 - BASIC EDUCATION", "pledged education activity 1", "110 - EDUCATION").setIsPledge(false), 
						SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]", "pledged education activity 1", "4 HUMAN RESOURCES").setIsPledge(false), 
						SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
						SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name", "Heavily used pledge", "[OP1 name, OP2 name]").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,44").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-870 000", "pledged education activity 1", "1 700 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "361 635,74", "Heavily used pledge", "4 900 000", "pledged education activity 1", "3 300 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "361 636,18", "Heavily used pledge", "4 030 000", "pledged education activity 1", "5 000 000").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
					.withTrailCells(null, null, null, null, null, null, null, null, "0,44", "0", "830 000", "0", "8 561 635,74", "0", "9 391 636,18", "0"))
				.withTrailCells(null, null, null, null, null, null, null, null, "0,44", "0", "830 000", "0", "8 561 635,74", "0", "9 391 636,18", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))");
		
		runReportTest("activities + pledges report, filtered by primary sector", "AMP-17746-test-merging-basic-education", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);

	}
	
	public void testPledgeMergingFlat() {
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17746-test-merging",
				ColumnReportDataModel.withColumns("AMP-17746-test-merging",
						SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge", "pledged education activity 1", "pledged education activity 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(false), 
						SimpleColumnModel.withContents("Region", "pledged 2", "Cahul County", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]", "Heavily used pledge", "[Anenii Noi County, Lapusna County]", "pledged education activity 1", "Chisinau County").setIsPledge(false), 
						SimpleColumnModel.withContents("Primary Sector", "pledged 2", "113 - SECONDARY EDUCATION", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]", "Heavily used pledge", "112 - BASIC EDUCATION", "pledged education activity 1", "110 - EDUCATION").setIsPledge(false), 
						SimpleColumnModel.withContents("Secondary Sector", "pledged 2", "4 HUMAN RESOURCES", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]", "pledged education activity 1", "4 HUMAN RESOURCES", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
						SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
						SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
						SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name", "Heavily used pledge", "[OP1 name, OP2 name]", "ACVL Pledge Name 2", "OP1 name").setIsPledge(false), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2012",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1,25", "free text name 2", "1 044 176,71").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2013",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000", "Heavily used pledge", "-870 000", "pledged education activity 1", "1 700 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000", "Test pledge 1", "1 033 244,98", "Heavily used pledge", "4 900 000", "pledged education activity 1", "3 300 000", "ACVL Pledge Name 2", "-50 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "Test pledge 1", "1 033 246,23", "Heavily used pledge", "4 030 000", "pledged education activity 1", "5 000 000", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 044 176,71").setIsPledge(false), 
							SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false)))
					.withTrailCells(null, null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000"))
				.withTrailCells(null, null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 14, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))");
		
		runReportTest("activities + pledges report", "AMP-17746-test-merging", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);
	}
	
	public void testPledgeMergingByRegion() {
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17746-test-merging-by-region",
				GroupReportModel.withColumnReports("AMP-17746-test-merging-by-region",
						ColumnReportDataModel.withColumns("Region: Anenii Noi County",
							SimpleColumnModel.withContents("Project Title", "Heavily used pledge", "Heavily used pledge").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Heavily used pledge", "[OP1 name, OP2 name]").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-304 500").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "1 715 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "1 410 500").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0", "0", "-304 500", "0", "1 715 000", "0", "1 410 500", "0"),
						ColumnReportDataModel.withColumns("Region: Balti County",
							SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,56").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "464 960,24").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "464 960,8").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0,56", "0", "0", "0", "464 960,24", "0", "464 960,8", "0"),
						ColumnReportDataModel.withColumns("Region: Cahul County",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "Test pledge 1", "Test pledge 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "pledged 2", "113 - SECONDARY EDUCATION", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged 2", "4 HUMAN RESOURCES", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,62").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000", "Test pledge 1", "516 622,49").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "Test pledge 1", "516 623,12").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0,62", "0", "2 670 000", "0", "4 916 622,49", "450 000", "7 586 623,12", "450 000"),
						ColumnReportDataModel.withColumns("Region: Chisinau County",
							SimpleColumnModel.withContents("Project Title", "pledged education activity 1", "pledged education activity 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "pledged education activity 1", "110 - EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged education activity 1", "4 HUMAN RESOURCES").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "1 700 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "3 300 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "5 000 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0", "0", "1 700 000", "0", "3 300 000", "0", "5 000 000", "0"),
						ColumnReportDataModel.withColumns("Region: Lapusna County",
							SimpleColumnModel.withContents("Project Title", "Heavily used pledge", "Heavily used pledge").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Heavily used pledge", "[OP1 name, OP2 name]").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-565 500").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "3 185 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 619 500").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0", "0", "-565 500", "0", "3 185 000", "0", "2 619 500", "0"),
						ColumnReportDataModel.withColumns("Region: Region Unallocated",
							SimpleColumnModel.withContents("Project Title", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "ACVL Pledge Name 2", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "free text name 2", "1 044 176,71").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "-50 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 044 176,71").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1 044 176,71", "0", "0", "0", "-50 000", "0", "1 932 246,46", "0"),
						ColumnReportDataModel.withColumns("Region: Transnistrian Region",
							SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,06").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "51 662,25").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "51 662,31").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0,06", "0", "0", "0", "51 662,25", "0", "51 662,31", "0"))
					.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000"))
				.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))");
		
		runReportTest("activities + pledges report, hier by region", "AMP-17746-test-merging-by-region", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);
	}

	public void testPledgeMergingByPrimaryProgram() {
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17746-test-merging-by-primary-program",
				GroupReportModel.withColumnReports("AMP-17746-test-merging-by-primary-program",
						ColumnReportDataModel.withColumns("Primary Program: Primary Program Unallocated",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "Heavily used pledge", "Heavily used pledge", "pledged education activity 1", "pledged education activity 1", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "pledged 2", "Cahul County", "Heavily used pledge", "[Anenii Noi County, Lapusna County]", "pledged education activity 1", "Chisinau County").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "pledged 2", "113 - SECONDARY EDUCATION", "Heavily used pledge", "112 - BASIC EDUCATION", "pledged education activity 1", "110 - EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged 2", "4 HUMAN RESOURCES", "pledged education activity 1", "4 HUMAN RESOURCES", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Heavily used pledge", "[OP1 name, OP2 name]", "ACVL Pledge Name 2", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "free text name 2", "1 044 176,71").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000", "Heavily used pledge", "-870 000", "pledged education activity 1", "1 700 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000", "Heavily used pledge", "4 900 000", "pledged education activity 1", "3 300 000", "ACVL Pledge Name 2", "-50 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "Heavily used pledge", "4 030 000", "pledged education activity 1", "5 000 000", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 044 176,71").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1 044 176,71", "0", "3 500 000", "0", "12 550 000", "450 000", "18 032 246,46", "450 000"),
						ColumnReportDataModel.withColumns("Primary Program: Subprogram p1.b",
							SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1,25").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1 033 244,98").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1 033 246,23").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1,25", "0", "0", "0", "1 033 244,98", "0", "1 033 246,23", "0"))
					.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000"))
				.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))");
		
		runReportTest("activities + pledges report, hier by primary program", "AMP-17746-test-merging-by-primary-program", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);
	}
	
	public void testPledgeMergingByTertiaryProgram() {
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17746-test-merging-by-tertiary-program",
				GroupReportModel.withColumnReports("AMP-17746-test-merging-by-tertiary-program",
						ColumnReportDataModel.withColumns("Tertiary Program: OP1 name",
							SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge", "ACVL Pledge Name 2", "ACVL Pledge Name 2").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Test pledge 1", "[112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION]", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1,25").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-348 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1 033 244,98", "Heavily used pledge", "1 960 000", "ACVL Pledge Name 2", "-50 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "1 033 246,23", "Heavily used pledge", "1 612 000", "ACVL Pledge Name 2", "888 069,75").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1,25", "0", "-348 000", "0", "2 943 244,98", "0", "3 533 315,98", "0"),
						ColumnReportDataModel.withColumns("Tertiary Program: OP2 name",
							SimpleColumnModel.withContents("Project Title", "Heavily used pledge", "Heavily used pledge").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "Heavily used pledge", "112 - BASIC EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-522 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 940 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "2 418 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0", "0", "-522 000", "0", "2 940 000", "0", "2 418 000", "0"),
						ColumnReportDataModel.withColumns("Tertiary Program: Tertiary Program Unallocated",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "pledged education activity 1", "pledged education activity 1", "free text name 2", "free text name 2").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "pledged 2", "Cahul County", "pledged education activity 1", "Chisinau County").setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Sector", "pledged 2", "113 - SECONDARY EDUCATION", "pledged education activity 1", "110 - EDUCATION").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged 2", "4 HUMAN RESOURCES", "pledged education activity 1", "4 HUMAN RESOURCES", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "free text name 2", "1 044 176,71").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000", "pledged education activity 1", "1 700 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000", "pledged education activity 1", "3 300 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "pledged education activity 1", "5 000 000", "free text name 2", "1 044 176,71").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1 044 176,71", "0", "4 370 000", "0", "7 700 000", "450 000", "13 114 176,71", "450 000"))
					.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000"))
				.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))");
		
		runReportTest("activities + pledges report, hier by tertiary program", "AMP-17746-test-merging-by-tertiary-program", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);
	}
	
	public void testPledgeMergingByPrimarySector() {
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17746-test-merging-by-primary-sector",
				GroupReportModel.withColumnReports("AMP-17746-test-merging-by-primary-sector",
						ColumnReportDataModel.withColumns("Primary Sector: 110 - EDUCATION",
							SimpleColumnModel.withContents("Project Title", "pledged education activity 1", "pledged education activity 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "pledged education activity 1", "Chisinau County").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged education activity 1", "4 HUMAN RESOURCES").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", MUST_BE_EMPTY).setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "1 700 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "3 300 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged education activity 1", "5 000 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0", "0", "1 700 000", "0", "3 300 000", "0", "5 000 000", "0"),
						ColumnReportDataModel.withColumns("Primary Sector: 112 - BASIC EDUCATION",
							SimpleColumnModel.withContents("Project Title", "Test pledge 1", "Test pledge 1", "Heavily used pledge", "Heavily used pledge").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]", "Heavily used pledge", "[Anenii Noi County, Lapusna County]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name", "Heavily used pledge", "[OP1 name, OP2 name]").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,44").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "Heavily used pledge", "-870 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "361 635,74", "Heavily used pledge", "4 900 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "361 636,18", "Heavily used pledge", "4 030 000").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0,44", "0", "-870 000", "0", "5 261 635,74", "0", "4 391 636,18", "0"),
						ColumnReportDataModel.withColumns("Primary Sector: 113 - SECONDARY EDUCATION",
							SimpleColumnModel.withContents("Project Title", "pledged 2", "pledged 2", "Test pledge 1", "Test pledge 1").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", "pledged 2", "Cahul County", "Test pledge 1", "[Balti County, Cahul County, Transnistrian Region]").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "pledged 2", "4 HUMAN RESOURCES", "Test pledge 1", "[3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT]").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", "Test pledge 1", "Subprogram p1.b").setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "Test pledge 1", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "Test pledge 1", "0,81").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "2 670 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "4 400 000", "Test pledge 1", "671 609,24").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "pledged 2", "7 070 000", "Test pledge 1", "671 610,05").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", "pledged 2", "450 000").setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "0,81", "0", "2 670 000", "0", "5 071 609,24", "450 000", "7 741 610,05", "450 000"),
						ColumnReportDataModel.withColumns("Primary Sector: Primary Sector Unallocated",
							SimpleColumnModel.withContents("Project Title", "ACVL Pledge Name 2", "ACVL Pledge Name 2", "free text name 2", "free text name 2").setIsPledge(false), 
							SimpleColumnModel.withContents("Region", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Sector", "ACVL Pledge Name 2", "1-DEMOCRATIC COUNTRY", "free text name 2", "1-DEMOCRATIC COUNTRY").setIsPledge(false), 
							SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY).setIsPledge(false), 
							SimpleColumnModel.withContents("Tertiary Program", "ACVL Pledge Name 2", "OP1 name").setIsPledge(false), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Commitments", "free text name 2", "1 044 176,71").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY).setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)), 
								GroupColumnModel.withSubColumns("2014",
									SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "-50 000").setIsPledge(false), 
									SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Commitments", "ACVL Pledge Name 2", "888 069,75", "free text name 2", "1 044 176,71").setIsPledge(false), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY).setIsPledge(false)))
						.withTrailCells(null, null, null, null, null, null, null, "1 044 176,71", "0", "0", "0", "-50 000", "0", "1 932 246,46", "0"))
					.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000"))
				.withTrailCells(null, null, null, null, null, null, null, "1 044 177,96", "0", "3 500 000", "0", "13 583 244,98", "450 000", "19 065 492,69", "450 000")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Secondary Sector: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 6), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 2))",
					"(line 1:RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2), RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))");
		
		runReportTest("activities + pledges report, hier by primary sector", "AMP-17746-test-merging-by-primary-sector", new String[] {"pledged education activity 1", "pledged 2"}, fddr_correct);
	}	
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}