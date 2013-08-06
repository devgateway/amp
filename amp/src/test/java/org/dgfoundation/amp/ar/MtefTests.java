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
import org.hibernate.Query;

import org.hibernate.cfg.*;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * testcase for Directed Disbursements (AMP-15337)
 * this can only be run IFF amp has been started standalone. For this, AllTests.setUp() should have been run previously (typically called by AllTests.suite() as part of the JUnit discovery process)
 * @author Dolghier Constantin
 *
 */
public class MtefTests extends ReportsTestCase
{
	public MtefTests(String name) {
		super(name);
	}
		
	public static Test suite()
	{
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTest(new MtefTests("testAllMtef"));
		return suite;
	}		
		
	
	/**
	 * a flat report containing RealDisbursements of a single activity
	 */
	public void testAllMtef()
	{
		// ========================= one more report ===============================
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15794", 
				ColumnReportDataModel.withColumns("AMP-15794", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "mtef activity 1", "789 123"),
						SimpleColumnModel.withContents("MTEF 2013/2014", "mtef activity 2", "123 654"),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2013", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "660 000")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY/*"Eth Water", "0", "mtef activity 1", "0", "mtef activity 2", "0"*/),
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "660 000")
						)));
		
		runReportTest("all Mtef report", "AMP-15794", new String[] {"Eth Water", "mtef activity 1", "mtef activity 2"}, fddr_correct);
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

