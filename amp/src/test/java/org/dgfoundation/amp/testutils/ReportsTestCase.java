package org.dgfoundation.amp.testutils;

import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.um.exception.UMException;

import junit.framework.TestCase;

public abstract class ReportsTestCase extends AmpTestCase
{
	public ReportsTestCase(String name) {
		super(name);
	}
	
	/**
	 * runs a single report test and compares the result with the expected cor 
	 * @param testName - test name to be displayed in case of error
	 * @param reportName - the name of the report (AmpReports entry in the tests database) which should be run
	 * @param activities - the names of the activities which should be presented via a dummy WorkspaceFilter to the report. Put ReportTestingUtils.NULL_PLACEHOLDER if NO WorkspaceFilter should be put (e.g. let the report see all the activities) 
	 * @param correctResult - a model (sketch) of the expected result
	 * @param modifier - the modifier (might be null) to postprocess AmpReports and AmpARFilter after being loaded from the DB
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult, AmpReportModifier modifier, String locale)
	{
		try
		{
			if (locale != null)
				TLSUtils.getThreadLocalInstance().setForcedLangCode(locale);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		ImmutablePair<GroupReportData, String> report = ReportTestingUtils.runReportOn(reportName, modifier, activities);
		////System.out.println(ReportTestingUtils.describeReportInCode(report, 1, true));
//		checkThatAllCRDsHaveIdenticalReportHeadingsLayoutData(report);
		String error = correctResult.matches(report.k);
		if (error != null)
			System.err.format("test %s, report %s, the output is: %s\n", testName, reportName, report.v);
		assertNull(String.format("test %s, report %s: %s", testName, reportName, error), error);
	}
	
//	/**
//	 * all the CRDs of the report should have the same columns' structures, e.g. any column-removal should be done in such a way as to be reflected in all the CRD's of a report
//	 */
//	protected void checkThatAllCRDsHaveIdenticalReportHeadingsLayoutData(GroupReportData report)
//	{
//		ArrayList<ColumnReportData> crds = new ArrayList<ColumnReportData>();
//		collectAllCRD(report, crds);
//		
//		if (crds.isEmpty())
//			return;
//		
//		String first = crds.get(0).digestReportHeadingData(true).toString();
//		for(int i = 1; i < crds.size(); i++)
//		{
//			String checked = crds.get(i).digestReportHeadingData(true).toString();
//			assertEquals(first, checked, 
//					"CRD " + crds.get(i).getAbsoluteReportName() + " has a different layout digest than " + crds.get(0).getAbsoluteReportName());
//		}
//	}
	
	/**
	 * shorthand for {@link #runReportTest(String, String, String[], GroupReportModel, AmpReportModifier, null)}
	 * @param testName
	 * @param reportName
	 * @param activities
	 * @param correctResult
	 * @param modifier
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult, AmpReportModifier modifier)
	{
		runReportTest(testName, reportName, activities, correctResult, modifier, null);
	}
	
//	/**
//	 * all the CRDs of the report should have the same columns' structures, e.g. any column-removal should be done in such a way as to be reflected in all the CRD's of a report
//	 */
//	protected void checkThatAllCRDsHaveIdenticalReportHeadingsLayoutData(GroupReportData report)
//	{
//		ArrayList<ColumnReportData> crds = new ArrayList<ColumnReportData>();
//		collectAllCRD(report, crds);
//		
//		if (crds.isEmpty())
//			return;
//		
//		String first = crds.get(0).digestReportHeadingData().toString();
//		for(int i = 1; i < crds.size(); i++)
//		{
//			String checked = crds.get(i).digestReportHeadingData().toString();
//			assertTrue("CRD " + crds.get(i).getAbsoluteReportName() + " has a different layout digest than " + crds.get(0).getAbsoluteReportName(), first.equals(checked));
//		}
//	}
	
	protected void collectAllCRD(GroupReportData report, List<ColumnReportData> crds)
	{
		for(ReportData rd: report.getItems())
		{
			if (rd instanceof GroupReportData)
				collectAllCRD((GroupReportData) rd, crds);
			if (rd instanceof ColumnReportData)
				crds.add((ColumnReportData) rd);
		}
	}
	
	/**
	 * please see {@link #runReportTest(String, String, String[], GroupReportModel, AmpReportModifier)}
	 * @param testName
	 * @param reportName
	 * @param activities
	 * @param correctResult
	 */
	protected void runReportTest(String testName, String reportName, String[] activities, GroupReportModel correctResult)
	{
		runReportTest(testName, reportName, activities, correctResult, null, null);
	}	

	public final static AmpReportModifier makeTabReportModifier = new AmpReportModifier() {
		
		@Override
		public void modifyAmpReportSettings(AmpReports report, AmpARFilter filter) {
			filter.setWidget(true);			
		}
	};

}
