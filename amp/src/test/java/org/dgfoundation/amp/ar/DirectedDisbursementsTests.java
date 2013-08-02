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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * testcase for Directed Disbursements (AMP-15337)
 * this can only be run IFF amp has been started standalone. For this, AllTests.setUp() should have been run previously (typically called by AllTests.suite() as part of the JUnit discovery process)
 * @author Dolghier Constantin
 *
 */
public class DirectedDisbursementsTests extends TestCase
{
	public DirectedDisbursementsTests(String name) {
		super(name);
	}
		
	public static Test suite()
	{
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTest(new DirectedDisbursementsTests("testFlatReport"));
		suite.addTest(new DirectedDisbursementsTests("testByBeneficiary"));
		suite.addTest(new DirectedDisbursementsTests("testByDonor"));
		suite.addTest(new DirectedDisbursementsTests("testByExecuting"));
		return suite;
	}	
		
	protected List<AmpActivity> getAllActivities() throws Exception
	{
		 org.hibernate.Session session = PersistenceManager.getRequestDBSession();
	      String qryStr = "select a from " + AmpActivity.class.getName() + " a ";
	      Query qry = session.createQuery(qryStr);
	      return (List<AmpActivity>) qry.list();
	}
		
	/**
	 * runs a single report test and compares the result with the expected cor 
	 * @param testName - test name to be displayed in case of error
	 * @param reportName - the name of the report (AmpReports entry in the tests database) which should be run
	 * @param activities - the names of the activities which should be presented via a dummy WorkspaceFilter to the report. Put ReportTestingUtils.NULL_PLACEHOLDER if NO WorkspaceFilter should be put (e.g. let the report see all the activities) 
	 * @param correctResult - a model (sketch) of the expected result
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult)
	{
		GroupReportData report = ReportTestingUtils.runReportOn(reportName, activities);
		String error = correctResult.matches(report);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}
	
	/**
	 * a flat report containing RealDisbursements of a single activity
	 */
	public void testFlatReport()
	{
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
	}
	
	/**
	 * same report as {@link #testFlatReport()}, but with a hierarchy by Beneficiary Agency
	 */
	public void testByBeneficiary()
	{
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
	}
	
	/**
	 * same report as {@link #testFlatReport()}, but with a hierarchy by Donor Agency
	 */
	public void testByDonor()
	{
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
	}
	
	/**
	 * same report as {@link #testFlatReport()}, but with a hierarchy by Executing Agency
	 */	
	public void testByExecuting()
	{
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
}

