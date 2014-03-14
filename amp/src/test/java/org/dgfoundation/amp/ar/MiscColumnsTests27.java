package org.dgfoundation.amp.ar;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;

import org.dgfoundation.amp.testmodels.ColumnReportDataModel;
import org.dgfoundation.amp.testmodels.GroupColumnModel;
import org.dgfoundation.amp.testmodels.GroupReportModel;
import org.dgfoundation.amp.testmodels.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MiscColumnsTests27 extends ReportsTestCase 
{
	private MiscColumnsTests27(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MiscColumnsTests27.class.getName());
		suite.addTest(new MiscColumnsTests27("testFundingClassificationDate"));
		suite.addTest(new MiscColumnsTests27("testTranslateNull"));
		return suite;
	}
	
	public void testFundingClassificationDate()
	{
		// ========================= one more report ===============================
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17070-Funding-Classification-Date",
				ColumnReportDataModel.withColumns("AMP-17070-Funding-Classification-Date",
						SimpleColumnModel.withContents("Project Title", "mtef activity 1", "mtef activity 1", "Activity with Funding Classification Date", "Activity with Funding Classification Date"), 
						SimpleColumnModel.withContents("Funding Classification Date", "mtef activity 1", "", "Activity with Funding Classification Date", "12/03/2014"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "Activity with Funding Classification Date", "1"))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "Activity with Funding Classification Date", "1")))
					.withTrailCells(null, null, "1", "1"))
				.withTrailCells(null, null, "1", "1")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding Classification Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
					"(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))");
		
		runReportTest("simple test FundingClassificationDate", "AMP-17070-Funding-Classification-Date", new String[] {"Activity with Funding Classification Date", "mtef activity 1"}, fddr_correct);

	}
	
	public void testTranslateNull()
	{
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		assertEquals("", TranslatorWorker.translateText(null));
		TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
		assertEquals("", TranslatorWorker.translateText(null));
		TLSUtils.getThreadLocalInstance().setForcedLangCode("ro");
		assertEquals("", TranslatorWorker.translateText(null));

	}
}
