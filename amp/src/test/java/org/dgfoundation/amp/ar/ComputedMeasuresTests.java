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
									SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "660 000"),
									SimpleColumnModel.withContents("Total Commitments", "Eth Water", "0", "ptc activity 1", "666 777", "ptc activity 2", "333 222")
												)),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "ptc activity 2", "333 222"),
								SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "660 000"),
								SimpleColumnModel.withContents("Percentage of Total Commitments", "Eth Water", "", "ptc activity 1", "66,68", "ptc activity 2", "33,32")
						))).withTrailCells(null, "999 999", "660 000", "999 999", "999 999", "660 000", "100");
		runReportTest("flat Percentage of Total Commitments", "AMP-15795-percentage-of-total-commitments", new String[] {"ptc activity 1", "ptc activity 2", "Eth Water"}, fptc_correct);
	}
}
