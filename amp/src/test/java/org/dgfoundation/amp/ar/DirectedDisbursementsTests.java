package org.dgfoundation.amp.ar;

import java.util.HashSet;
import java.util.List;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.ActivityUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;

import junit.framework.TestCase;

public class DirectedDisbursementsTests extends TestCase
{
//	public static Test suite() {
//		TestSuite suite = new TestSuite(DirectedDisbursementsTests.class.getName());
//		//$JUnit-BEGIN$
//
//		suite.addTest(new FlatDirectedReportTestCase("testDummy"));
//
//		//$JUnit-END$
//		return suite;
//	}
//	
//	public static void main(String[] args) {
//		junit.textui.TestRunner.run(suite());
//	}

	public DirectedDisbursementsTests(String name) {
		super(name);
	}
		
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
		
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
		
	protected List<AmpActivity> getAllActivities() throws Exception
	{
		 org.hibernate.Session session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpActivity.class.getName() + " a ";
	      Query qry = session.createQuery(qryStr);
	      return (List<AmpActivity>) qry.list();
	}
		
	
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult)
	{
		GroupReportData report = ReportTestingUtils.runReportOn(reportName, activities);
		String error = correctResult.matches(report);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}
	
	
	public void testReports()
	{
		StandaloneUtils.runWithinAmpContext(new MyRunnable()
		{
			public void run() throws Exception
			{				
				System.out.println("AMP started up!");
				
				List<AmpActivity> activities = getAllActivities(); //ActivityUtil.getActivityById(new HashSet<Long>(){{add(57L); add(2351L);}}, hibSession);
//				System.out.format("you have the following %d activities:\n", activities.size());
//				for(AmpActivity activity:activities)
//				{
//					System.out.format("\tactivity name: %s\n", activity.getName());
//				}
				
				// ========================= one more report ===============================
				GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15337-real-disbursements", 
						ColumnReportDataModel.withColumns("AMP-15337-real-disbursements", 
								SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
								GroupColumnModel.withSubColumns("Funding", 
										GroupColumnModel.withSubColumns("2013", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"),
														SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
														SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000")
														))),
								GroupColumnModel.withSubColumns("Total Costs", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"),
														SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
														SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000")
								))));
				
				runReportTest("flat Directed Disbursements Report", "AMP-15337-real-disbursements", new String[] {"Eth Water"}, fddr_correct);
				
				
				/// ========================= one more report ===============================
				GroupReportModel by_benef_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-beneficiary",
						GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-beneficiary",					
						ColumnReportDataModel.withColumns("Beneficiary Agency: Water Foundation", 
								SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
								GroupColumnModel.withSubColumns("Funding", 
										GroupColumnModel.withSubColumns("2013", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "5 000")
														))),
								GroupColumnModel.withSubColumns("Total Costs", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "5 000")
								))),
								ColumnReportDataModel.withColumns("Beneficiary Agency: Water Org", 
										SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
										GroupColumnModel.withSubColumns("Funding", 
												GroupColumnModel.withSubColumns("2013", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "10 000")
																))),
										GroupColumnModel.withSubColumns("Total Costs", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "10 000")
										)))
								
						));	
				
				runReportTest("by beneficiary Directed Disbursements Report", "AMP-15337-real-disbursements-by-beneficiary", new String[] {"Eth Water"}, by_benef_ddr_correct);
				
				
				/// ========================= one more report ===============================
				GroupReportModel by_donor_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-donor",
						GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-donor",					
						ColumnReportDataModel.withColumns("Donor Agency: Finland", 
								SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
								GroupColumnModel.withSubColumns("Funding", 
										GroupColumnModel.withSubColumns("2013", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000")
														))),
								GroupColumnModel.withSubColumns("Total Costs", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000")
								))),
								ColumnReportDataModel.withColumns("Donor Agency: Norway", 
										SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
										GroupColumnModel.withSubColumns("Funding", 
												GroupColumnModel.withSubColumns("2013", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000")
																))),
										GroupColumnModel.withSubColumns("Total Costs", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000")
										))),
										
								ColumnReportDataModel.withColumns("Donor Agency: USAID", 
										SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
										GroupColumnModel.withSubColumns("Funding", 
												GroupColumnModel.withSubColumns("2013", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000")
																))),
										GroupColumnModel.withSubColumns("Total Costs", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000")
										)))
																		
						));	
				
				runReportTest("by donor Directed Disbursements Report", "AMP-15337-real-disbursements-by-donor", new String[] {"Eth Water"}, by_donor_ddr_correct);
				
				
				
				
				/// ========================= one more report ===============================
				GroupReportModel by_exec_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-executing",
						GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-executing",					
						ColumnReportDataModel.withColumns("Executing Agency: UNDP", 
								SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
								GroupColumnModel.withSubColumns("Funding", 
										GroupColumnModel.withSubColumns("2013", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
														SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "40 000")
														))),
								GroupColumnModel.withSubColumns("Total Costs", 
												GroupColumnModel.withSubColumns("Real Disbursements", 
														SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
														SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "40 000")
								))),
								ColumnReportDataModel.withColumns("Executing Agency: World Bank", 
										SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
										GroupColumnModel.withSubColumns("Funding", 
												GroupColumnModel.withSubColumns("2013", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
																SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "10 000")
																))),
										GroupColumnModel.withSubColumns("Total Costs", 
														GroupColumnModel.withSubColumns("Real Disbursements", 
																SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
																SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "10 000")
										)))
								
						));	
				
				runReportTest("by executing Directed Disbursements Report", "AMP-15337-real-disbursements-by-executing", new String[] {"Eth Water"}, by_exec_ddr_correct);
				

			}
		});
	}
}
