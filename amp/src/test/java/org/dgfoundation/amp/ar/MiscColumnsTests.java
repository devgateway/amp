package org.dgfoundation.amp.ar;

import java.util.HashSet;
import java.util.List;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;

import org.hibernate.cfg.*;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MiscColumnsTests extends ReportsTestCase
{
	public MiscColumnsTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MiscColumnsTests.class.getName());
		suite.addTest(new MiscColumnsTests("testSscColumns"));
		return suite;
	}
	
	public void testSscColumns()
	{
		GroupReportModel fssc_correct = GroupReportModel.withColumnReports("AMP-15844-ssc-columns", 
				ColumnReportDataModel.withColumns("AMP-15844-ssc-columns", 
						SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
						SimpleColumnModel.withContents("Type of Cooperation", "SSC Project 1", "Official Development Aid (ODA)", "SSC Project 2", "Regional South South Cooperation"),
						SimpleColumnModel.withContents("Type of Implementation", "SSC Project 1", "Program", "SSC Project 2", "Action"),
						SimpleColumnModel.withContents("Modalities", "SSC Project 1", "Diplomats and courses", "SSC Project 2", "Virtual Platforms and blogs to consult, learn, and exchange ideas"),
						SimpleColumnModel.withContents("Component Description", "SSC Project 1", "SSC Project 1 DescriptionOfComponent", "SSC Project 2", "SSC Project 2 Description of Component"),
						SimpleColumnModel.withContents("Component Title", "SSC Project 1", "SSC Project 1 TitleOfComponent", "SSC Project 2", "SSC Project 2 Title of Component"),

						
						GroupColumnModel.withSubColumns("Funding", 
								GroupColumnModel.withSubColumns("2013", 
									SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "SSC Project 1", "111 333", "SSC Project 2", "567 421"),
									SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 1", "555 111", "SSC Project 2", "131 845"))),
						GroupColumnModel.withSubColumns("Total Costs", 
								SimpleColumnModel.withContents("Actual Commitments", "ptc activity 1", "666 777", "SSC Project 1", "111 333", "SSC Project 2", "567 421"),
								SimpleColumnModel.withContents("Actual Disbursements", "SSC Project 1", "555 111", "SSC Project 2", "131 845")))
						);
		runReportTest("flat SSC Columns", "AMP-15844-ssc-columns", new String[] {"ptc activity 1", "SSC Project 1", "SSC Project 2"}, fssc_correct);
	}
}
