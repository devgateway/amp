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
		return suite;
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
