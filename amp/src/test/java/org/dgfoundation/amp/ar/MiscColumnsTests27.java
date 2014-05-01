package org.dgfoundation.amp.ar;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;
import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

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
		suite.addTest(new MiscColumnsTests27("testAgreementsFieldsRu"));
		suite.addTest(new MiscColumnsTests27("testAgreementsFieldsEn"));
		return suite;
	}
	
	public void testAgreementsFieldsRu(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17349",
				ColumnReportDataModel.withColumns("AMP-17349",
						SimpleColumnModel.withContents("Project Title", "activity with agreements русский", "activity with agreements русский"), 
						SimpleColumnModel.withContents("Capital - Expenditure", "activity with agreements русский", "[Capital, Expenditure]"), 
						SimpleColumnModel.withContents("Agreement Code", "activity with agreements русский", "MLAGR"), 
						SimpleColumnModel.withContents("Agreement Title + Code", "activity with agreements русский", "Какая-то соглашеnie - MLAGR"), 
						SimpleColumnModel.withContents("Agreement Close Date", "activity with agreements русский", "06/05/2015"), 
						SimpleColumnModel.withContents("Agreement Effective Date", "activity with agreements русский", "02/02/2014"), 
						SimpleColumnModel.withContents("Agreement Signature Date", "activity with agreements русский", "01/01/2014"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "activity with agreements русский", "65 000"), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "activity with agreements русский", "65 000"), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
					.withTrailCells(null, null, null, null, null, null, null, "65 000", "0", "65 000", "0"))
				.withTrailCells(null, null, null, null, null, null, null, "65 000", "0", "65 000", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Capital - Expenditure: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Agreement Code: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Agreement Title + Code: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Agreement Close Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Agreement Effective Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Agreement Signature Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
					"(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		runReportTest("simple test FundingClassificationDate", "AMP-17349", new String[] {"activity with agreements русский"}, fddr_correct, null, "ru");
	}
	
	public void testAgreementsFieldsEn(){
		GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17349",
				ColumnReportDataModel.withColumns("AMP-17349",
						SimpleColumnModel.withContents("Project Title", "activity with agreements EN", "activity with agreements EN"), 
						SimpleColumnModel.withContents("Capital - Expenditure", "activity with agreements EN", "[Capital, Expenditure]"), 
						SimpleColumnModel.withContents("Agreement Code", "activity with agreements EN", "MLAGR"), 
						SimpleColumnModel.withContents("Agreement Title + Code", "activity with agreements EN", "Some Agreement (En) - MLAGR"), 
						SimpleColumnModel.withContents("Agreement Close Date", "activity with agreements EN", "06/05/2015"), 
						SimpleColumnModel.withContents("Agreement Effective Date", "activity with agreements EN", "02/02/2014"), 
						SimpleColumnModel.withContents("Agreement Signature Date", "activity with agreements EN", "01/01/2014"), 
						GroupColumnModel.withSubColumns("Funding",
							GroupColumnModel.withSubColumns("2014",
								SimpleColumnModel.withContents("Actual Commitments", "activity with agreements EN", "65 000"), 
								SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
						GroupColumnModel.withSubColumns("Total Costs",
							SimpleColumnModel.withContents("Actual Commitments", "activity with agreements EN", "65 000"), 
							SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
					.withTrailCells(null, null, null, null, null, null, null, "65 000", "0", "65 000", "0"))
				.withTrailCells(null, null, null, null, null, null, null, "65 000", "0", "65 000", "0")
				.withPositionDigest(true,
					"(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Capital - Expenditure: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Agreement Code: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Agreement Title + Code: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Agreement Close Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Agreement Effective Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Agreement Signature Date: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
					"(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
					"(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))");
		runReportTest("simple test FundingClassificationDate", "AMP-17349", new String[] {"activity with agreements EN"}, fddr_correct, null, "en");
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
