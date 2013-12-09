package org.dgfoundation.amp.ar;

import java.util.HashSet;
import java.util.List;

import org.apache.axis.handlers.soap.MustUnderstandChecker;
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
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Query;
import org.hibernate.cfg.*;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ComplicatedLayoutsTests extends ReportsTestCase
{
	public ComplicatedLayoutsTests(String name) {
		super(name);
	}
		
	public static Test suite()
	{
		TestSuite suite = new TestSuite(ComplicatedLayoutsTests.class.getName());
		suite.addTest(new ComplicatedLayoutsTests("testMixedWithTotalsFlat"));
		suite.addTest(new ComplicatedLayoutsTests("testTabWithTotalsFlat"));
		suite.addTest(new ComplicatedLayoutsTests("testMultipleDonorAgenciesTotalsOnlyFlat"));
		suite.addTest(new ComplicatedLayoutsTests("testMultipleDonorAgenciesByExecuting"));
		suite.addTest(new ComplicatedLayoutsTests("testCrazyTypesOfRealDisbursementsFlat"));
		suite.addTest(new ComplicatedLayoutsTests("testLotsOfActivitiesWithCrazyRealDisbursementsAndActualDisbursements"));
		suite.addTest(new ComplicatedLayoutsTests("testLotsOfActivitiesWithCrazyRealDisbursementsAndActualDisbursementsByDonor"));
		suite.addTest(new ComplicatedLayoutsTests("testReportWithRealDisbursementsColumnButWithoutRealDisbursements"));
		suite.addTest(new ComplicatedLayoutsTests("testReportWithRealDisbursementsColumnButWithoutRealDisbursementsTotalsOnly"));
		return suite;
	}
	
	public void testMixedWithTotalsFlat()
	{
		// ========================= one more report ===============================
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15967-real-disbursements-mixed", 
				ColumnReportDataModel.withColumns("AMP-15967-real-disbursements-mixed", 
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-1", "AMP-15967-activity-1", "AMP-15967-activity-2", "AMP-15967-activity-2"),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
									SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "15 000")),
								
								GroupColumnModel.withSubColumns("2011", 
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "44 444", "AMP-15967-activity-2", "50 000"),
										GroupColumnModel.withSubColumns("Real Disbursements", 
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "11 222"))),

								GroupColumnModel.withSubColumns("2012", 
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "55 666", "AMP-15967-activity-2", "32 000"),
										GroupColumnModel.withSubColumns("Real Disbursements", 
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "55 666", "AMP-15967-activity-2", "32 000")))),
												
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "100 110", "AMP-15967-activity-2", "97 000"),
								GroupColumnModel.withSubColumns("Real Disbursements", 
									SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "66 888", "AMP-15967-activity-2", "32 000")))
						)).withTrailCells(null, "15 000", "94 444", "11 222", "87 666", "87 666", "197 110", "98 888")
						.withPositionDigest(
								"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colSpan: 5), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 2))",
								"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2))",
								"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1))",
								"(line 3:RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))"
								);
		
		runReportTest("flat Actual Disbursements + Real Disbursements Report", "AMP-15967-real-disbursements-mixed", new String[] {"AMP-15967-activity-1", "AMP-15967-activity-2"}, fddr_correct);
	}
	
	public void testTabWithTotalsFlat()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("aaa", 
				ColumnReportDataModel.withColumns("aaa", 
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-1", "AMP-15967-activity-1", "AMP-15967-activity-2", "AMP-15967-activity-2", "Eth Water", "Eth Water", "date-filters-activity", "date-filters-activity", "Test MTEF directed", "Test MTEF directed"),
												
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "100 110", "AMP-15967-activity-2", "97 000", "Eth Water", "545 000", "date-filters-activity", "72 000", "Test MTEF directed", "143 777"),
								GroupColumnModel.withSubColumns("Real Disbursements", 
									SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "66 888", "AMP-15967-activity-2", "32 000", "Eth Water", "545 000"),
									SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"),
									SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
									SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000"),
									SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333")
									))
						)).withTrailCells(null, "957 887", "643 888", "77 222", "100 000", "15 000", "44 333")
						.withPositionDigest(
								"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 6))", 
								"(line 1:)",
								"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 5))", 
								"(line 3:RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))"
								);
		
		runReportTest("flat tab Actual Disbursements + Real Disbursements Report", "aaa", 
				new String[] {"AMP-15967-activity-1", "AMP-15967-activity-2", "Eth Water", "date-filters-activity", "Test MTEF directed"}, fddr_correct, makeTabReportModifier, null);
	}
	
	public void testMultipleDonorAgenciesTotalsOnlyFlat()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15967-test-mixed-activities-totals", 
				ColumnReportDataModel.withColumns("AMP-15967-test-mixed-activities-totals", 
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-1", "AMP-15967-activity-1", "AMP-15967-activity-2", "AMP-15967-activity-2", "Eth Water", "Eth Water", "date-filters-activity", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR"),
						SimpleColumnModel.withContents("Donor Agency", "AMP-15967-activity-1", "[Finland, USAID]", "AMP-15967-activity-2", "[Finland, Norway]", "Eth Water", "[Finland, Norway, USAID]", "date-filters-activity", "Ministry of Finance"/*, "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR"*/),												
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "125 000"),
								SimpleColumnModel.withContents("Actual Disbursements", 
												"AMP-15967-activity-1", "100 110", "AMP-15967-activity-2", "97 000", "Eth Water", "545 000", "date-filters-activity", "72 000")
						))).withTrailCells(null, null, "125 000", "814 110")
						.withPositionDigest(
								"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colSpan: 1), RHLC Donor Agency: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colSpan: 2))", 
								"(line 1:)", 
								"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1))"
								);
		
		runReportTest("flat Actual Disbursements + Actual Disbursements Report", "AMP-15967-test-mixed-activities-totals", 
				new String[] {"AMP-15967-activity-1", "AMP-15967-activity-2", "Eth Water", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR"}, fddr_correct, makeTabReportModifier, null);
	}	
	
	public void testMultipleDonorAgenciesByExecuting()
	{
		// ========================= one more report ===============================
		GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-executing", 
				GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-executing", 
					ColumnReportDataModel.withColumns("Executing Agency: 72 Local Public Administrations from RM", 
						SimpleColumnModel.withContents("Project Title", "AMP-15967-activity-1", "AMP-15967-activity-1", "AMP-15967-activity-2", "AMP-15967-activity-2"),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2011",
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "11 222"))),

								GroupColumnModel.withSubColumns("2012",
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "55 666", "AMP-15967-activity-2", "32 000"))),

								GroupColumnModel.withSubColumns("2013",
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("EXEC-IMPL", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY)))),
																						
								GroupColumnModel.withSubColumns("Total Costs",
										GroupColumnModel.withSubColumns("Real Disbursements", 
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "66 888", "AMP-15967-activity-2", "32 000"),
											SimpleColumnModel.withContents("EXEC-IMPL", MUST_BE_EMPTY),
											SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY))))
						.withTrailCells(null, "11 222", "87 666", "0", "0", "0", "98 888", "0", "0"),
						
						ColumnReportDataModel.withColumns("Executing Agency: UNDP", 
								SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water"),
								GroupColumnModel.withSubColumns("Funding", 
										GroupColumnModel.withSubColumns("2011",
											GroupColumnModel.withSubColumns("Real Disbursements",
												SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

										GroupColumnModel.withSubColumns("2012",
											GroupColumnModel.withSubColumns("Real Disbursements",
												SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

										GroupColumnModel.withSubColumns("2013",
											GroupColumnModel.withSubColumns("Real Disbursements",
												SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
												SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "40 000"),
												SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY)))),
																								
										GroupColumnModel.withSubColumns("Total Costs",
												GroupColumnModel.withSubColumns("Real Disbursements", 
													SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
													SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "40 000"),
													SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY))))
								.withTrailCells(null, "0", "0", "300 000", "40 000", "0", "300 000", "40 000", "0"),

						ColumnReportDataModel.withColumns("Executing Agency: World Bank", 
							SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water"),
								GroupColumnModel.withSubColumns("Funding", 
									GroupColumnModel.withSubColumns("2011",
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

									GroupColumnModel.withSubColumns("2012",
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

									GroupColumnModel.withSubColumns("2013",
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
											SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "10 000"),
											SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY)))),
																										
								GroupColumnModel.withSubColumns("Total Costs",
									GroupColumnModel.withSubColumns("Real Disbursements", 
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
										SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "10 000"),
										SimpleColumnModel.withContents("IMPL-EXEC", MUST_BE_EMPTY))))
							.withTrailCells(null, "0", "0", "245 000", "10 000", "0", "245 000", "10 000", "0"),

							ColumnReportDataModel.withColumns("Executing Agency: Water Foundation", 
									SimpleColumnModel.withContents("Project Title", "Test MTEF directed", "Test MTEF directed"),
										GroupColumnModel.withSubColumns("Funding", 
											GroupColumnModel.withSubColumns("2011",
												GroupColumnModel.withSubColumns("Real Disbursements",
													SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

											GroupColumnModel.withSubColumns("2012",
												GroupColumnModel.withSubColumns("Real Disbursements",
													SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))),

											GroupColumnModel.withSubColumns("2013",
												GroupColumnModel.withSubColumns("Real Disbursements",
													SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY),
													SimpleColumnModel.withContents("EXEC-IMPL", MUST_BE_EMPTY),
													SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333")))),
																												
										GroupColumnModel.withSubColumns("Total Costs",
												GroupColumnModel.withSubColumns("Real Disbursements",
														SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY),
														SimpleColumnModel.withContents("EXEC-IMPL", MUST_BE_EMPTY),
														SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333"))))
									.withTrailCells(null, "0", "0", "0", "0", "44 333", "0", "0", "44 333")))
									.withTrailCells(null, "11 222", "87 666", "545 000", "50 000", "44 333", "643 888", "50 000", "44 333")
									.withPositionDigest(
											"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colSpan: 5), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 3))",
											"(line 1:RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 3))", 
											"(line 2:RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 3), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 3))", 
											"(line 3:RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))"
											);
											
		
		runReportTest("complicated Real Disbursements report with lots of activities and nonstandard RD by executing agency", "AMP-15337-real-disbursements-by-executing", 
				new String[] {"AMP-15967-activity-1", "AMP-15967-activity-2", "Eth Water", "Test MTEF directed", "mtef activity 1", "ptc activity 2", "SSC Project 2", "date-filters-activity"}, fddr_correct);
	}
	
	
	public void testCrazyTypesOfRealDisbursementsFlat()
	{
		// ========================= one more report ===============================
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15337-real-disbursements", 
				ColumnReportDataModel.withColumns("AMP-15337-real-disbursements", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"))),
								
								GroupColumnModel.withSubColumns("2011", 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "11 222"))),

								GroupColumnModel.withSubColumns("2012", 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "55 666", "AMP-15967-activity-2", "32 000"))),

								GroupColumnModel.withSubColumns("2013", 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"),
										SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
										SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000"),
										SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333")
										))),
										
								GroupColumnModel.withSubColumns("Total Costs", 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000", "AMP-15967-activity-1", "66 888", "AMP-15967-activity-2", "32 000"),
										SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"),
										SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
										SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000"),
										SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333")))
													))
								.withTrailCells(null, "77 222", "11 222", "87 666", "545 000", "100 000", "15 000", "44 333", "643 888", "77 222", "100 000", "15 000", "44 333")
						.withPositionDigest(
								"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colSpan: 7), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 5))", 
								"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 4))", 
								"(line 2:RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 4), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 5))", 
								"(line 3:RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))"
								);
		
		runReportTest("flat huge and hairy Real Disbursements Report", "AMP-15337-real-disbursements", 
				new String[] {"AMP-15967-activity-1", "AMP-15967-activity-2", "Eth Water", "Test MTEF directed", "mtef activity 1", "ptc activity 2", "SSC Project 2", "date-filters-activity"}, fddr_correct);
	}
	
	public void testLotsOfActivitiesWithCrazyRealDisbursementsAndActualDisbursements()
	{
		GroupReportModel fddr_correct = 
				GroupReportModel.withColumnReports("AMP-15967-real-disbursements-mixed",
						ColumnReportDataModel.withColumns("AMP-15967-real-disbursements-mixed",
							SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water", "mtef activity 1", "mtef activity 1", "mtef activity 2", "mtef activity 2", "ptc activity 1", "ptc activity 1", "ptc activity 2", "ptc activity 2", "SSC Project 1", "SSC Project 1", "SSC Project 2", "SSC Project 2", "TAC_activity_1", "TAC_activity_1", "TAC_activity_2", "TAC_activity_2", "date-filters-activity", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Pure MTEF Project", "Test MTEF directed", "Test MTEF directed", "AMP-15967-activity-2", "AMP-15967-activity-2", "AMP-15967-activity-1", "AMP-15967-activity-1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2010",
									SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777", "AMP-15967-activity-2", "15 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213", "date-filters-activity", "60 000"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"))), 
								GroupColumnModel.withSubColumns("2011",
									SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "50 000", "AMP-15967-activity-1", "44 444"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "11 222"))), 
								GroupColumnModel.withSubColumns("2012",
									SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "32 000", "AMP-15967-activity-1", "55 666", "date-filters-activity", "12 000"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-2", "32 000", "AMP-15967-activity-1", "55 666"))), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000", "SSC Project 1", "555 111", "SSC Project 2", "131 845"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"), 
										SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"), 
										SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000"), 
										SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333")))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000", "Test MTEF directed", "143 777", "AMP-15967-activity-2", "97 000", "AMP-15967-activity-1", "100 110", "SSC Project 1", "555 111", "SSC Project 2", "131 845", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213", "date-filters-activity", "72 000"), 
								GroupColumnModel.withSubColumns("Real Disbursements",
									SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000", "AMP-15967-activity-2", "32 000", "AMP-15967-activity-1", "66 888"), 
									SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"), 
									SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"), 
									SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000"), 
									SimpleColumnModel.withContents("IMPL-EXEC", "Test MTEF directed", "44 333"))))
						.withTrailCells(null, "795 311", "77 222", "94 444", "11 222", "99 666", "87 666", "1 231 956", "545 000", "100 000", "15 000", "44 333", "2 221 377", "643 888", "77 222", "100 000", "15 000", "44 333"))
					.withTrailCells(null, "795 311", "77 222", "94 444", "11 222", "99 666", "87 666", "1 231 956", "545 000", "100 000", "15 000", "44 333", "2 221 377", "643 888", "77 222", "100 000", "15 000", "44 333")
						.withPositionDigest(
						"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colSpan: 11), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 6))",
						"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 5))",
						"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 4), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 5))",
						"(line 3:RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC EXEC-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-BENF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC IMPL-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))");
		
		runReportTest("flat huge and hairy Real Disbursements and Actual Disbursements Report", "AMP-15967-real-disbursements-mixed", 
				new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", "AMP-15967-activity-2", "AMP-15967-activity-1"}, 
				fddr_correct);	
	}
	
	public void testLotsOfActivitiesWithCrazyRealDisbursementsAndActualDisbursementsByDonor()
	{
		GroupReportModel fddr_correct = 
				GroupReportModel.withGroupReports("AMP-15967-test-mixed-activities_20_21",
						GroupReportModel.withColumnReports("AMP-15967-test-mixed-activities_20_21",
							ColumnReportDataModel.withColumns("Donor Agency: Finland",
								SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water", "AMP-15967-activity-2", "AMP-15967-activity-2", "AMP-15967-activity-1", "AMP-15967-activity-1", "SSC Project 1", "SSC Project 1"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "50 000", "AMP-15967-activity-1", "44 444"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "11 222"))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000", "SSC Project 1", "555 111"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000")))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000", "AMP-15967-activity-2", "50 000", "AMP-15967-activity-1", "44 444", "SSC Project 1", "555 111"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000", "AMP-15967-activity-1", "11 222"), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "0", "0", "94 444", "11 222", "0", "0", "575 111", "20 000", "669 555", "31 222", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: Ministry of Economy",
								SimpleColumnModel.withContents("Project Title", "Test MTEF directed", "Test MTEF directed"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY)))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY), 
										SimpleColumnModel.withContents("DN-IMPL", "Test MTEF directed", "77 222"))))
							.withTrailCells(null, "143 777", "77 222", "0", "0", "0", "0", "0", "0", "143 777", "0", "77 222"),
							ColumnReportDataModel.withColumns("Donor Agency: Ministry of Finance",
								SimpleColumnModel.withContents("Project Title", "date-filters-activity", "date-filters-activity"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "60 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "12 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY)))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "72 000"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "60 000", "0", "0", "0", "12 000", "0", "0", "0", "72 000", "0", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: Norway",
								SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water", "AMP-15967-activity-2", "AMP-15967-activity-2"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "15 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-2", "32 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-2", "32 000"))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000")))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000", "AMP-15967-activity-2", "47 000"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000", "AMP-15967-activity-2", "32 000"), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "15 000", "0", "0", "0", "32 000", "32 000", "110 000", "110 000", "157 000", "142 000", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: USAID",
								SimpleColumnModel.withContents("Project Title", "Eth Water", "Eth Water", "AMP-15967-activity-1", "AMP-15967-activity-1"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", "AMP-15967-activity-1", "55 666"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "AMP-15967-activity-1", "55 666"))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000")))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000", "AMP-15967-activity-1", "55 666"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000", "AMP-15967-activity-1", "55 666"), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "0", "0", "0", "0", "55 666", "55 666", "415 000", "415 000", "470 666", "470 666", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: Water Foundation",
								SimpleColumnModel.withContents("Project Title", "TAC_activity_2", "TAC_activity_2"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "453 213"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY)))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "453 213"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "453 213", "0", "0", "0", "0", "0", "0", "0", "453 213", "0", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: Water Org",
								SimpleColumnModel.withContents("Project Title", "SSC Project 2", "SSC Project 2"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY)))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "0", "0", "0", "0", "0", "0", "131 845", "0", "131 845", "0", "0"),
							ColumnReportDataModel.withColumns("Donor Agency: World Bank",
								SimpleColumnModel.withContents("Project Title", "TAC_activity_1", "TAC_activity_1"), 
								GroupColumnModel.withSubColumns("Funding",
									GroupColumnModel.withSubColumns("2010",
										SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_1", "123 321"), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2011",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2012",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY))), 
									GroupColumnModel.withSubColumns("2013",
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY), 
										GroupColumnModel.withSubColumns("Real Disbursements",
											SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY)))), 
								GroupColumnModel.withSubColumns("Total Costs",
									SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_1", "123 321"), 
									GroupColumnModel.withSubColumns("Real Disbursements",
										SimpleColumnModel.withContents("DN-EXEC", MUST_BE_EMPTY), 
										SimpleColumnModel.withContents("DN-IMPL", MUST_BE_EMPTY))))
							.withTrailCells(null, "123 321", "0", "0", "0", "0", "0", "0", "0", "123 321", "0", "0"))
						.withTrailCells(null, "795 311", "77 222", "94 444", "11 222", "99 666", "87 666", "1 231 956", "545 000", "2 221 377", "643 888", "77 222"))
					.withTrailCells(null, "795 311", "77 222", "94 444", "11 222", "99 666", "87 666", "1 231 956", "545 000", "2 221 377", "643 888", "77 222")
						.withPositionDigest(
						"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colSpan: 8), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 4, colSpan: 3))",
						"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colSpan: 2))",
						"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colSpan: 2))",
						"(line 3:RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colSpan: 1))");
		
		runReportTest("huge and hairy Real Disbursements and Actual Disbursements Report by donor", "AMP-15967-test-mixed-activities_20_21", 
				new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", "AMP-15967-activity-2", "AMP-15967-activity-1"}, 
				fddr_correct);	
	}	
	
	public void testReportWithRealDisbursementsColumnButWithoutRealDisbursementsTotalsOnly()
	{
		GroupReportModel fddr_correct = 
				GroupReportModel.withColumnReports("act-real-disb",
						ColumnReportDataModel.withColumns("act-real-disb",
							SimpleColumnModel.withContents("Project Title", "SSC Project 2", "SSC Project 2", "TAC_activity_1", "TAC_activity_1"), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845", "TAC_activity_1", "123 321"), 
								SimpleColumnModel.withContents("Real Disbursements", MUST_BE_EMPTY)))
						.withTrailCells(null, "255 166", "0"))
					.withTrailCells(null, "255 166", "0")
						.withPositionDigest(
						"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colSpan: 2))",
						"(line 1:)",
						"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1))");
	
		runReportTest("Tab with Actual Disb + Real Disb", "act-real-disb", 
				new String[] {"SSC Project 2", "TAC_activity_1"}, fddr_correct, makeTabReportModifier, null);			
	}
	
	public void testReportWithRealDisbursementsColumnButWithoutRealDisbursements()
	{
		GroupReportModel fddr_correct = 
				GroupReportModel.withColumnReports("AMP-16525-no-real-disb-transactions",
						ColumnReportDataModel.withColumns("AMP-16525-no-real-disb-transactions",
							SimpleColumnModel.withContents("Project Title", "SSC Project 2", "SSC Project 2", "TAC_activity_1", "TAC_activity_1"), 
							GroupColumnModel.withSubColumns("Funding",
								GroupColumnModel.withSubColumns("2010",
									SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_1", "123 321")), 
								GroupColumnModel.withSubColumns("2013",
									SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845"))), 
							GroupColumnModel.withSubColumns("Total Costs",
								SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 2", "131 845", "TAC_activity_1", "123 321"), 
								SimpleColumnModel.withContents("Real Disbursements", MUST_BE_EMPTY)))
						.withTrailCells(null, "123 321", "131 845", "255 166", "0"))
					.withTrailCells(null, "123 321", "131 845", "255 166", "0")
						.withPositionDigest(
						"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colSpan: 2))",
						"(line 1:RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colSpan: 1), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colSpan: 1))",
						"(line 2:RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1), RHLC Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colSpan: 1))");
	
		runReportTest("Tab with Actual Disb + Real Disb", "AMP-16525-no-real-disb-transactions", 
				new String[] {"SSC Project 2", "TAC_activity_1"}, 
				fddr_correct);			
	}	
}
