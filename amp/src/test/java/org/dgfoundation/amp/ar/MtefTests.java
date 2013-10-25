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
		suite.addTest(new MtefTests("testPlainMtef"));
		suite.addTest(new MtefTests("testMtefByDonorAgency"));
		suite.addTest(new MtefTests("testMtefByImplementingAgency"));
		suite.addTest(new MtefTests("testMtefByExecutingAgency"));
		
		suite.addTest(new MtefTests("testPurePlainMtef"));
		suite.addTest(new MtefTests("testPureMtefByDonorAgency"));
		suite.addTest(new MtefTests("testPureMtefByImplementingAgency"));
		suite.addTest(new MtefTests("testPureMtefByExecutingAgency"));
		
		suite.addTest(new MtefTests("testPurePlainMtefEUR"));
		suite.addTest(new MtefTests("testPurePlainMtefEURInThousands"));
		suite.addTest(new MtefTests("testPurePlainMtefEURInThousandsMoreActivities"));

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
										SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY/*"Eth Water", "0", "mtef activity 1", "0", "mtef activity 2", "0"*/),
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
						)));
		
		runReportTest("all Mtef report", "AMP-15794", new String[] {"Eth Water", "mtef activity 1", "mtef activity 2"}, fddr_correct);
	}
	
	public void testPlainMtef()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs", 
				ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Test MTEF directed", "150 000"),
						SimpleColumnModel.withContents("MTEF 2012/2013", "Test MTEF directed", "65 000"),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
						))).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777");
		
		runReportTest("all Mtef, implicit filter by Donor", "AMP-16100-flat-mtefs", new String[] {"Test MTEF directed"}, fddr_correct);
	}

	public void testMtefByDonorAgency()
	{
		GroupReportModel fddr_correct =
				GroupReportModel.withGroupReports("AMP-16100-mtef-by-donor-agency", 
				GroupReportModel.withColumnReports("AMP-16100-mtef-by-donor-agency", 
				ColumnReportDataModel.withColumns("Donor Agency: Ministry of Economy", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Test MTEF directed", "150 000"),
						SimpleColumnModel.withContents("MTEF 2012/2013", "Test MTEF directed", "65 000"),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
						))).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777")
					).withTrailCells(null, "150 000", "65 000", "0", "0", "143 777", "0", "143 777");
		
		runReportTest("all Mtef, by Donor", "AMP-16100-mtef-by-donor-agency", new String[] {"Test MTEF directed"}, fddr_correct);
	}

	public void testMtefByImplementingAgency()
	{
		GroupReportModel fddr_correct =
				GroupReportModel.withGroupReports("AMP-16100-mtef-projection-by-impl", 
				GroupReportModel.withColumnReports("AMP-16100-mtef-projection-by-impl", 
				ColumnReportDataModel.withColumns("Implementing Agency: USAID", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Test MTEF directed", "25 400"),
						SimpleColumnModel.withContents("MTEF 2012/2013", "Test MTEF directed", "62 500"),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
						))).withTrailCells(null, "25 400", "62 500", "0", "0", "143 777", "0", "143 777")
					).withTrailCells(null, "25 400", "62 500", "0", "0", "143 777", "0", "143 777");
		
		runReportTest("all Mtef, by Implementing Agency", "AMP-16100-mtef-projection-by-impl", new String[] {"Test MTEF directed"}, fddr_correct);
	}

	public void testMtefByExecutingAgency()
	{
		GroupReportModel fddr_correct =
				GroupReportModel.withGroupReports("AMP-16100-mtef-projections-by-exec", 
				GroupReportModel.withColumnReports("AMP-16100-mtef-projections-by-exec", 
				ColumnReportDataModel.withColumns("Executing Agency: Water Foundation", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2012/2013", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777")
						))).withTrailCells(null, "0", "0", "0", "0", "143 777", "0", "143 777")
					).withTrailCells(null, "0", "0", "0", "0", "143 777", "0", "143 777");
		
		runReportTest("all Mtef, by Executing Agency", "AMP-16100-mtef-projections-by-exec", new String[] {"Test MTEF directed"}, fddr_correct);
	}

	public void testPurePlainMtef()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs", 
				ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "33 888"),
						SimpleColumnModel.withContents("MTEF 2012/2013", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						/*GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
												)),*/
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
						))).withTrailCells(null, "33 888", "0", "0", "0", "0");
		
		runReportTest("pure Mtef, implicit filter by Donor", "AMP-16100-flat-mtefs", new String[] {"Pure MTEF Project"}, fddr_correct);
	}

	public void testPureMtefByDonorAgency()
	{
		GroupReportModel fddr_correct =
				GroupReportModel.withGroupReports("AMP-16100-mtef-by-donor-agency", 
				GroupReportModel.withColumnReports("AMP-16100-mtef-by-donor-agency", 
				ColumnReportDataModel.withColumns("Donor Agency: Ministry of Finance", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "33 888"),
						SimpleColumnModel.withContents("MTEF 2012/2013", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						/*GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
												)),*/
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
						))).withTrailCells(null, "33 888", "0", "0", "0", "0")
					).withTrailCells(null, "33 888", "0", "0", "0", "0");
		
		runReportTest("pure Mtef, by Donor", "AMP-16100-mtef-by-donor-agency", new String[] {"Pure MTEF Project"}, fddr_correct);
	}

	public void testPureMtefByImplementingAgency()
	{
		GroupReportModel fddr_correct = GroupReportModel.empty("AMP-16100-mtef-projection-by-impl");
		
		runReportTest("pure Mtef, by Implementing Agency", "AMP-16100-mtef-projection-by-impl", new String[] {"Pure MTEF Project"}, fddr_correct);
	}

	public void testPureMtefByExecutingAgency()
	{
		GroupReportModel fddr_correct =
				GroupReportModel.withGroupReports("AMP-16100-mtef-projections-by-exec", 
				GroupReportModel.withColumnReports("AMP-16100-mtef-projections-by-exec", 
				ColumnReportDataModel.withColumns("Executing Agency: Ministry of Economy", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2012/2013", "Pure MTEF Project", "55 333"),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						/*GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
												)),*/
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
						))).withTrailCells(null, "0", "55 333", "0", "0", "0")
					).withTrailCells(null, "0", "55 333", "0", "0", "0");
		
		runReportTest("pure Mtef, by Executing Agency", "AMP-16100-mtef-projections-by-exec", new String[] {"Pure MTEF Project"}, fddr_correct);
	}

	public void testPurePlainMtefEUR()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur", 
				ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "25 311"),
						SimpleColumnModel.withContents("MTEF 2012/2013", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						/*GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
												)),*/
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
						))).withTrailCells(null, "25 311", "0", "0", "0", "0");
		
		runReportTest("pure Mtef, implicit filter by Donor, EUR", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project"}, fddr_correct);
	}

	public void testPurePlainMtefEURInThousands()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur", 
				ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "25"),
						SimpleColumnModel.withContents("MTEF 2012/2013", MUST_BE_EMPTY),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						/*GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
												)),*/
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
						))).withTrailCells(null, "25", "0", "0", "0", "0");
		
		AmpReportModifier modifier = new AmpReportModifier() {			
			@Override
			public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {				
				filter.setAmountinthousand(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS);
			}
		};
		runReportTest("pure Mtef, implicit filter by Donor, EUR, THOUSANDS", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project"}, fddr_correct, modifier, null);
	}
	
	public void testPurePlainMtefEURInThousandsMoreActivities()
	{
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16100-flat-mtefs-eur", 
				ColumnReportDataModel.withColumns("AMP-16100-flat-mtefs-eur", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("MTEF 2011/2012", "Pure MTEF Project", "25", "Test MTEF directed", "112"),
						SimpleColumnModel.withContents("MTEF 2012/2013", "Test MTEF directed", "49"),
						SimpleColumnModel.withContents("MTEF 2013/2014", MUST_BE_EMPTY),
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2010", 
										SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY),
										SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "332", "Test MTEF directed", "105")
										),
								GroupColumnModel.withSubColumns("2011", 
										SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_2", "747"),
										SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)
										)
												
								),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_2", "747"),
								SimpleColumnModel.withContents("Actual Disbursements", "TAC_activity_2", "332", "Test MTEF directed", "105")
						))).withTrailCells(null, "137", "49", "0", "0", "438", "747", "0", "747", "438");
		
		AmpReportModifier modifier = new AmpReportModifier() {			
			@Override
			public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {				
				filter.setAmountinthousand(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS);
			}
		};
		runReportTest("pure Mtef, implicit filter by Donor, EUR, THOUSANDS", "AMP-16100-flat-mtefs-eur", new String[] {"Pure MTEF Project", "Test MTEF directed", "TAC_activity_2"}, fddr_correct, modifier, null);
	}	
	
}

