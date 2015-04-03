package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class MondrianComputedMeasuresReportTests extends MondrianReportsTestCase {
	
	public MondrianComputedMeasuresReportTests() {
		super("computed measures mondrian tests");
	}
	
	
	@Test
	public void test_AMP_18565() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2014-Planned Disbursements - Capital", "10 800", "2014-Planned Disbursements - Expenditure", "79 200", "2014-Actual Disbursements - Recurrent", "52 800", "2014-Actual Disbursements - Capital", "27 200", "Total Measures-Planned Disbursements - Capital", "10 800", "Total Measures-Planned Disbursements - Expenditure", "79 200", "Total Measures-Actual Disbursements - Recurrent", "52 800", "Total Measures-Actual Disbursements - Capital", "27 200")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "ptc activity 1", "2014-Planned Disbursements - Capital", "", "2014-Planned Disbursements - Expenditure", "", "2014-Actual Disbursements - Recurrent", "", "2014-Actual Disbursements - Capital", "", "Total Measures-Planned Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Expenditure", "0", "Total Measures-Actual Disbursements - Recurrent", "0", "Total Measures-Actual Disbursements - Capital", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "2014-Planned Disbursements - Capital", "", "2014-Planned Disbursements - Expenditure", "", "2014-Actual Disbursements - Recurrent", "", "2014-Actual Disbursements - Capital", "", "Total Measures-Planned Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Expenditure", "0", "Total Measures-Actual Disbursements - Recurrent", "0", "Total Measures-Actual Disbursements - Capital", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with capital spending", "2014-Planned Disbursements - Capital", "10 800", "2014-Planned Disbursements - Expenditure", "79 200", "2014-Actual Disbursements - Recurrent", "52 800", "2014-Actual Disbursements - Capital", "27 200", "Total Measures-Planned Disbursements - Capital", "10 800", "Total Measures-Planned Disbursements - Expenditure", "79 200", "Total Measures-Actual Disbursements - Recurrent", "52 800", "Total Measures-Actual Disbursements - Capital", "27 200")  );		
		List<String> activities = Arrays.asList("pledged 2", "activity with capital spending", "ptc activity 1");
		runMondrianTestCase("AMP-18565",
				"AMP-18565",
				activities,
				correctReport,
				"en"); 
	}
	
	@Test
	public void test_UndisbursedBalance() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Mode of Payment", "", "2010-Undisbursed Balance", "-267 098", "2011-Undisbursed Balance", "213 231", "2013-Undisbursed Balance", "-443 778", "Total Measures-Undisbursed Balance", "-497 645")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "Mode of Payment", "", "2010-Undisbursed Balance", "-123 321", "2011-Undisbursed Balance", "213 231", "2013-Undisbursed Balance", "", "Total Measures-Undisbursed Balance", "89 910"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Mode of Payment", "Cash", "2010-Undisbursed Balance", "-143 777", "2011-Undisbursed Balance", "0", "2013-Undisbursed Balance", "0", "Total Measures-Undisbursed Balance", "-143 777"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Project with documents", "Mode of Payment", "Cash", "2010-Undisbursed Balance", "", "2011-Undisbursed Balance", "", "2013-Undisbursed Balance", "", "Total Measures-Undisbursed Balance", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Mode of Payment", "Direct payment", "2010-Undisbursed Balance", "", "2011-Undisbursed Balance", "", "2013-Undisbursed Balance", "-443 778", "Total Measures-Undisbursed Balance", "-443 778")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "Test MTEF directed", "SSC Project 1", "Project with documents");
		runMondrianTestCase("AMP-15863-mode-of-payment-undisbursed-balance",
				activities,
				correctReport,
				"en"); 
	}
	
	
	@Test
	public void test_UncommittedBalance() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Proposed Project Amount", "", "Uncommitted Balance", "", "Actual Commitments", "75 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Uncommitted Balance", "1 000 000", "Actual Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Uncommitted Balance", "3 399 510,47", "Actual Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Project with documents", "Proposed Project Amount", "", "Uncommitted Balance", "", "Actual Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Uncommitted Balance", "-15 000", "Actual Commitments", "75 000")  );
		
		List<String> activities = Arrays.asList("Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "SubNational no percentages", "Project with documents");
		runMondrianTestCase("Uncommitted Balance",
				activities,
				correctReport,
				"en"); 
	}
	
	@Test
	public void test_TotalCommitments() {
		// another measure be appear instead of AC based on AMP-19808 solution 
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Total Commitments", "", "2011-Actual Commitments", "213 231", "2013-Actual Commitments", "111 333", "Total Measures-Actual Commitments", "324 564")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "Total Commitments", "213 231", "2011-Actual Commitments", "213 231", "2013-Actual Commitments", "", "Total Measures-Actual Commitments", "213 231"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Total Commitments", "0", "2011-Actual Commitments", "", "2013-Actual Commitments", "", "Total Measures-Actual Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Project with documents", "Total Commitments", "0", "2011-Actual Commitments", "", "2013-Actual Commitments", "", "Total Measures-Actual Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "Total Commitments", "111 333", "2011-Actual Commitments", "", "2013-Actual Commitments", "111 333", "Total Measures-Actual Commitments", "111 333")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "Test MTEF directed", "SSC Project 1", "Project with documents");
		runMondrianTestCase("AMP-17400-no-projects",
				activities,
				correctReport,
				"en");
	}
	
	@Test
	public void test_PercentageOfTotalCommitments() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2010-Percentage of Total Commitments", "0", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2011-Percentage of Total Commitments", "4", "2013-Actual Commitments", "1 811 333", "2013-Actual Disbursements", "555 111", "2013-Percentage of Total Commitments", "34,02", "2014-Actual Commitments", "3 300 000", "2014-Actual Disbursements", "0", "2014-Percentage of Total Commitments", "61,98", "Total Measures-Actual Commitments", "5 324 564", "Total Measures-Actual Disbursements", "822 209", "Total Measures-Percentage of Total Commitments", "100")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "TAC_activity_1", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2010-Percentage of Total Commitments", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2011-Percentage of Total Commitments", "4", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2013-Percentage of Total Commitments", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "2014-Percentage of Total Commitments", "", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321", "Total Measures-Percentage of Total Commitments", "4"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2010-Percentage of Total Commitments", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2011-Percentage of Total Commitments", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2013-Percentage of Total Commitments", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "2014-Percentage of Total Commitments", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777", "Total Measures-Percentage of Total Commitments", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SSC Project 1", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2010-Percentage of Total Commitments", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2011-Percentage of Total Commitments", "", "2013-Actual Commitments", "111 333", "2013-Actual Disbursements", "555 111", "2013-Percentage of Total Commitments", "2,09", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "2014-Percentage of Total Commitments", "", "Total Measures-Actual Commitments", "111 333", "Total Measures-Actual Disbursements", "555 111", "Total Measures-Percentage of Total Commitments", "2,09"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged education activity 1", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2010-Percentage of Total Commitments", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2011-Percentage of Total Commitments", "", "2013-Actual Commitments", "1 700 000", "2013-Actual Disbursements", "", "2013-Percentage of Total Commitments", "31,93", "2014-Actual Commitments", "3 300 000", "2014-Actual Disbursements", "", "2014-Percentage of Total Commitments", "61,98", "Total Measures-Actual Commitments", "5 000 000", "Total Measures-Actual Disbursements", "0", "Total Measures-Percentage of Total Commitments", "93,9")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "Test MTEF directed", "SSC Project 1", "pledged education activity 1");
		runMondrianTestCase("AMP-15795-percentage-of-total-commitments",
				activities,
				correctReport,
				"en");
	}
	
	@Test
	public void test_AMP_19721_Selected_Year_Planned_Disbursements() {
		ReportAreaForTests correctReport =  new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Selected Year Planned Disbursements", "", "2014-November-Planned Disbursements", "90 000", "Total Measures-Planned Disbursements", "90 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with capital spending", "Selected Year Planned Disbursements", "90 000", "2014-November-Planned Disbursements", "90 000", "Total Measures-Planned Disbursements", "90 000")  );
		
		List<String> activities = Arrays.asList("TAC_activity_1", "activity with capital spending");
		runMondrianTestCase("AMP-19721-Selected-Year-Planned-Disbursements",
				activities,
				correctReport,
				"en");
	}
	
	@Test
	public void test_AMP_19708_Cumulative_Amounts() {
		// the output may change after AMP-19724
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Proposed Project Amount", "", "Uncommitted Cumulative Balance", "", "Cumulative Commitment", "", "Cumulative Disbursement", "", "Undisbursed Cumulative Balance", "", "Cumulative Execution Rate", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2014-Actual Commitments", "75 000", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "75 000", "Total Measures-Actual Disbursements", "0")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Proposed Project Amount", "", "Uncommitted Cumulative Balance", "", "Cumulative Commitment", "0", "Cumulative Disbursement", "143 777", "Undisbursed Cumulative Balance", "-143 777", "Cumulative Execution Rate", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Uncommitted Cumulative Balance", "-15 000", "Cumulative Commitment", "75 000", "Cumulative Disbursement", "0", "Undisbursed Cumulative Balance", "75 000", "Cumulative Execution Rate", "0", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2014-Actual Commitments", "75 000", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "75 000", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with capital spending", "Proposed Project Amount", "", "Uncommitted Cumulative Balance", "", "Cumulative Commitment", "79 670,69", "Cumulative Disbursement", "80 000", "Undisbursed Cumulative Balance", "-329,31", "Cumulative Execution Rate", "100,41", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")  );
		
		List<String> activities = Arrays.asList("Test MTEF directed", "SubNational no percentages", "activity with capital spending");
		runMondrianTestCase("AMP-19708 Cumulative Amounts",
				activities,
				correctReport,
				"en");
	}
	
}
