package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
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
	      new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "2014-Planned Disbursements - Capital", "0", "2014-Planned Disbursements - Expenditure", "0", "2014-Actual Disbursements - Recurrent", "0", "2014-Actual Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Expenditure", "0", "Total Measures-Actual Disbursements - Recurrent", "0", "Total Measures-Actual Disbursements - Capital", "0"),
	      new ReportAreaForTests().withContents("Project Title", "pledged 2", "2014-Planned Disbursements - Capital", "0", "2014-Planned Disbursements - Expenditure", "0", "2014-Actual Disbursements - Recurrent", "0", "2014-Actual Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Capital", "0", "Total Measures-Planned Disbursements - Expenditure", "0", "Total Measures-Actual Disbursements - Recurrent", "0", "Total Measures-Actual Disbursements - Capital", "0"),
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "2014-Planned Disbursements - Capital", "10 800", "2014-Planned Disbursements - Expenditure", "79 200", "2014-Actual Disbursements - Recurrent", "52 800", "2014-Actual Disbursements - Capital", "27 200", "Total Measures-Planned Disbursements - Capital", "10 800", "Total Measures-Planned Disbursements - Expenditure", "79 200", "Total Measures-Actual Disbursements - Recurrent", "52 800", "Total Measures-Actual Disbursements - Capital", "27 200"));		
		List<String> activities = Arrays.asList("pledged 2", "activity with capital spending", "ptc activity 1");
		runMondrianTestCase("AMP-18565",
				"AMP-18565",
				activities,
				correctReport,
				"en"); 
	}
	
}
