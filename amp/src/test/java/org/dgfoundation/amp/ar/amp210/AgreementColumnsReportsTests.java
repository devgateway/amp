package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.junit.Test;

public class AgreementColumnsReportsTests extends MondrianReportsTestCase {
	
	public AgreementColumnsReportsTests() {
		super("donor reports with agreement columns");
	}
	
	List<String> theActivities = Arrays.asList(
			"activity 1 with agreement", "Activity 2 with multiple agreements",
			"third activity with agreements", "activity with incomplete agreement",
			"pledged 2", "activity with components"
		);
	
	@Test
	public void testAllAgreementColumnsFlat() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "704 445", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "7 774 445", "Total Measures-Actual Disbursements", "771 765")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with components", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity 1 with agreement", "Agreement Title + Code", "Agreement 1 Title - A1C", "Agreement Code", "A1C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015", "Agreement Effective Date", "05/03/2015", "Agreement Close Date", "24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "456 789", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "456 789", "Total Measures-Actual Disbursements", "321 765"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Activity 2 with multiple agreements", "Agreement Title + Code", "Agreement 1 Title - A1C, Second agreement - AC2", "Agreement Code", "A1C, AC2", "Agreement Signature Date", "01/02/2015, 03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015, 13/02/2015", "Agreement Effective Date", "04/02/2015, 05/03/2015", "Agreement Close Date", "07/02/2015, 24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "1 200", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "1 200", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "third activity with agreements", "Agreement Title + Code", "Second agreement - AC2", "Agreement Code", "AC2", "Agreement Signature Date", "01/02/2015", "Agreement Parlimentary Approval Date", "13/02/2015", "Agreement Effective Date", "04/02/2015", "Agreement Close Date", "07/02/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 456", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 456", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with incomplete agreement", "Agreement Title + Code", "Incomplete agreement en - IA1-C", "Agreement Code", "IA1-C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "06/03/2015", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 000", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 000", "Total Measures-Actual Disbursements", "0"));
		
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "704 445", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "7 774 445", "Total Measures-Actual Disbursements", "771 765")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "проект с подпроектами", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "обещание 2", "Agreement Title + Code", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity 1 with agreement", "Agreement Title + Code", "Имя первого агреемента - A1C", "Agreement Code", "A1C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015", "Agreement Effective Date", "05/03/2015", "Agreement Close Date", "24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "456 789", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "456 789", "Total Measures-Actual Disbursements", "321 765"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Проект с множеством проектов", "Agreement Title + Code", "Второй агреемент - AC2, Имя первого агреемента - A1C", "Agreement Code", "A1C, AC2", "Agreement Signature Date", "01/02/2015, 03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015, 13/02/2015", "Agreement Effective Date", "04/02/2015, 05/03/2015", "Agreement Close Date", "07/02/2015, 24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "1 200", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "1 200", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "third activity with agreements", "Agreement Title + Code", "Второй агреемент - AC2", "Agreement Code", "AC2", "Agreement Signature Date", "01/02/2015", "Agreement Parlimentary Approval Date", "13/02/2015", "Agreement Effective Date", "04/02/2015", "Agreement Close Date", "07/02/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 456", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 456", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with incomplete agreement", "Agreement Title + Code", "incomplete agreement ru - IA1-C", "Agreement Code", "IA1-C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "06/03/2015", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 000", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 000", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase("AMP-19762-flat", theActivities, correctReportEn, "en");
		runMondrianTestCase("AMP-19762-flat", theActivities, correctReportRu, "ru");
	}

	@Test
	public void testAllAgreementColumnByTitleCode() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Agreement Title + Code", "Report Totals", "Project Title", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "704 445", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "7 774 445", "Total Measures-Actual Disbursements", "771 765")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Agreement Title + Code", "Agreement 1 Title - A1C Totals", "Project Title", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "457 289", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "457 289", "Total Measures-Actual Disbursements", "321 765")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "Agreement 1 Title - A1C", "Project Title", "activity 1 with agreement", "Agreement Code", "A1C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015", "Agreement Effective Date", "05/03/2015", "Agreement Close Date", "24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "456 789", "2015-Actual Disbursements", "321 765", "Total Measures-Actual Commitments", "456 789", "Total Measures-Actual Disbursements", "321 765"),
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "", "Project Title", "Activity 2 with multiple agreements", "Agreement Code", "A1C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "04/03/2015", "Agreement Effective Date", "05/03/2015", "Agreement Close Date", "24/03/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "500", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "500", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Agreement Title + Code", "Second agreement - AC2 Totals", "Project Title", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "124 156", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "124 156", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "Second agreement - AC2", "Project Title", "Activity 2 with multiple agreements", "Agreement Code", "AC2", "Agreement Signature Date", "01/02/2015", "Agreement Parlimentary Approval Date", "13/02/2015", "Agreement Effective Date", "04/02/2015", "Agreement Close Date", "07/02/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "700", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "700", "Total Measures-Actual Disbursements", "0"),
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "", "Project Title", "third activity with agreements", "Agreement Code", "AC2", "Agreement Signature Date", "01/02/2015", "Agreement Parlimentary Approval Date", "13/02/2015", "Agreement Effective Date", "04/02/2015", "Agreement Close Date", "07/02/2015", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 456", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 456", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Agreement Title + Code", "Incomplete agreement en - IA1-C Totals", "Project Title", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 000", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 000", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "Incomplete agreement en - IA1-C", "Project Title", "activity with incomplete agreement", "Agreement Code", "IA1-C", "Agreement Signature Date", "03/03/2015", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "06/03/2015", "Agreement Close Date", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "2015-Actual Commitments", "123 000", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "123 000", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Agreement Title + Code", "Agreement Title + Code: Undefined Totals", "Project Title", "", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Agreement Title + Code", "Agreement Title + Code: Undefined", "Project Title", "pledged 2", "Agreement Code", "", "Agreement Signature Date", "", "Agreement Parlimentary Approval Date", "", "Agreement Effective Date", "", "Agreement Close Date", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")));

		runMondrianTestCase("AMP-19762-by-ag-title-code", theActivities, correctReportEn, "en");
	}
	
}
