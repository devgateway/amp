package org.dgfoundation.amp.ar.amp211;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.junit.Test;

/**
 * testcases for non-directed MTEFs and also for generic MTEF routines
 * @author Constantin Dolghier
 *
 */
public class PlainMTEFMondrianReportTests extends MondrianReportsTestCase {

	public final static List<String> activities = Arrays.asList(
			"TAC_activity_1", "Test MTEF directed", "Pure MTEF Project", "mtef activity 1", "Activity with both MTEFs and Act.Comms");
	
	public PlainMTEFMondrianReportTests() {
		super("plain MTEF mondrian tests");
	}
	
	@Test
	public void testFlatMtefs() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "mtef activity 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0"));
		
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Проект_TAC_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321"),
	      new ReportAreaForTests().withContents("Project Title", "Тест направленных МТЕФ", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests().withContents("Project Title", "Чисто-МТЕФ-Проект", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Проект МТЕФ 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase(
				"AMP-16100-flat-mtefs",						
				activities,
				correctReportEn,
				"en");
		
		runMondrianTestCase(
				"AMP-16100-flat-mtefs",						
				activities,
				correctReportRu,
				"ru");
	}
	
	@Test
	public void testFlatMtefsEur() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "210 094", "2011-MTEF Projections", "1 249 572", "2011-Actual Commitments", "149 411", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "160 605", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "668 753", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 410 177", "Total Measures-Actual Commitments", "818 164", "Total Measures-Actual Disbursements", "210 094")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "97 559", "2011-MTEF Projections", "", "2011-Actual Commitments", "149 411", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "149 411", "Total Measures-Actual Disbursements", "97 559"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "112 534", "2011-MTEF Projections", "112 035", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "48 555", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "160 590", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "112 534"),
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "25 311", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "25 311", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "mtef activity 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "589 396", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "589 396", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "522 830", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "112 050", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "668 753", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "634 880", "Total Measures-Actual Commitments", "668 753", "Total Measures-Actual Disbursements", "0"));
		
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "210 094", "2011-MTEF Projections", "1 249 572", "2011-Actual Commitments", "149 411", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "160 605", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "668 753", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 410 177", "Total Measures-Actual Commitments", "818 164", "Total Measures-Actual Disbursements", "210 094")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "Проект_TAC_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "97 559", "2011-MTEF Projections", "", "2011-Actual Commitments", "149 411", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "149 411", "Total Measures-Actual Disbursements", "97 559"),
	      new ReportAreaForTests().withContents("Project Title", "Тест направленных МТЕФ", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "112 534", "2011-MTEF Projections", "112 035", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "48 555", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "160 590", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "112 534"),
	      new ReportAreaForTests().withContents("Project Title", "Чисто-МТЕФ-Проект", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "25 311", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "25 311", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Проект МТЕФ 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "589 396", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "589 396", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "522 830", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "112 050", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "668 753", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "634 880", "Total Measures-Actual Commitments", "668 753", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase(
				"AMP-16100-flat-mtefs-eur",
				activities,
				correctReportEn,
				"en");
		
		runMondrianTestCase(
				"AMP-16100-flat-mtefs-eur",
				activities,
				correctReportRu,
				"ru");
	}
	
	@Test
	public void testByDonorMtefs() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Donor Agency", "Report Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Donor Agency", "UNDP Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "UNDP", "Project Title", "mtef activity 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "World Bank Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "0", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "World Bank", "Project Title", "TAC_activity_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Finland Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Finland", "Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Ministry of Finance Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Ministry of Finance", "Project Title", "Pure MTEF Project", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Ministry of Economy Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Ministry of Economy", "Project Title", "Test MTEF directed", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")));
		
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Donor Agency", "Report Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Donor Agency", "ПРООН Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "ПРООН", "Project Title", "Проект МТЕФ 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Всемирный банк Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "0", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Всемирный банк", "Project Title", "Проект_TAC_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Финляндия Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Финляндия", "Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Министерство финансов Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Министерство финансов", "Project Title", "Чисто-МТЕФ-Проект", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Donor Agency", "Министерство экономики Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")
	      .withChildren(
	        new ReportAreaForTests().withContents("Donor Agency", "Министерство экономики", "Project Title", "Тест направленных МТЕФ", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")));
		
		runMondrianTestCase(
				"AMP-16100-mtef-by-donor-agency",						
				activities,
				correctReportEn,
				"en");
		
		runMondrianTestCase(
				"AMP-16100-mtef-by-donor-agency",						
				activities,
				correctReportRu,
				"ru");
	}
	
	@Test
	public void testByImplAgencyMtefs() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Implementing Agency", "Report Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "267 098", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Implementing Agency", "USAID Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "USAID", "Project Title", "Test MTEF directed", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")    ),
	      new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Finance Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "0", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "Ministry of Finance", "Project Title", "Pure MTEF Project", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests().withContents("Implementing Agency", "Implementing Agency: Undefined Totals", "Project Title", "", "2010-MTEF Projections", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "1 489 123", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 639 123", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "123 321")
	      .withChildren(
	        new ReportAreaForTests().withContents("Implementing Agency", "Implementing Agency: Undefined", "Project Title", "TAC_activity_1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "123 321", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321"),
	        new ReportAreaForTests().withContents("Implementing Agency", "", "Project Title", "mtef activity 1", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	        new ReportAreaForTests().withContents("Implementing Agency", "", "Project Title", "Activity with both MTEFs and Act.Comms", "2010-MTEF Projections", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "2015-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0")));
		
		runMondrianTestCase(
				"AMP-16100-mtef-projection-by-impl",						
				activities,
				correctReportEn,
				"en");
	}
	
	@Test
	public void testOneSingleMtefColumn() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2011-MTEF Projections", "0", "2011-Actual Commitments", "213 231", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2015-MTEF Projections", "0", "2015-Actual Commitments", "888 000", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "1 101 231")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "2011-MTEF Projections", "", "2011-Actual Commitments", "213 231", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2011-MTEF Projections", "", "2011-Actual Commitments", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "Total Measures-MTEF Projections", "65 000", "Total Measures-Actual Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "2011-MTEF Projections", "", "2011-Actual Commitments", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2011-MTEF Projections", "", "2011-Actual Commitments", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2015-MTEF Projections", "", "2015-Actual Commitments", "888 000", "Total Measures-MTEF Projections", "150 000", "Total Measures-Actual Commitments", "888 000"));
		
		runMondrianTestCase(
			"AMP-20872-one-single-MTEF-year",						
			activities,
			correctReportEn,
			"en");
	}
	
	@Test
	public void testMtefsMixedEthiopianCalendar() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2002-MTEF Projections", "0", "2002-Actual Commitments", "0", "2002-Actual Disbursements", "267 098", "2003-MTEF Projections", "0", "2003-Actual Commitments", "213 231", "2003-Actual Disbursements", "0", "2007-MTEF Projections", "0", "2007-Actual Commitments", "888 000", "2007-Actual Disbursements", "0", "2011-MTEF Projections", "1 673 011", "2011-Actual Commitments", "0", "2011-Actual Disbursements", "0", "2012-MTEF Projections", "215 000", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "Total Measures-MTEF Projections", "1 888 011", "Total Measures-Actual Commitments", "1 101 231", "Total Measures-Actual Disbursements", "267 098")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "TAC_activity_1", "2002-MTEF Projections", "", "2002-Actual Commitments", "", "2002-Actual Disbursements", "123 321", "2003-MTEF Projections", "", "2003-Actual Commitments", "213 231", "2003-Actual Disbursements", "", "2007-MTEF Projections", "", "2007-Actual Commitments", "", "2007-Actual Disbursements", "", "2011-MTEF Projections", "", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "Total Measures-MTEF Projections", "0", "Total Measures-Actual Commitments", "213 231", "Total Measures-Actual Disbursements", "123 321"),
	      new ReportAreaForTests().withContents("Project Title", "Test MTEF directed", "2002-MTEF Projections", "", "2002-Actual Commitments", "", "2002-Actual Disbursements", "143 777", "2003-MTEF Projections", "", "2003-Actual Commitments", "", "2003-Actual Disbursements", "", "2007-MTEF Projections", "", "2007-Actual Commitments", "", "2007-Actual Disbursements", "", "2011-MTEF Projections", "150 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "65 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "Total Measures-MTEF Projections", "215 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests().withContents("Project Title", "Pure MTEF Project", "2002-MTEF Projections", "", "2002-Actual Commitments", "", "2002-Actual Disbursements", "", "2003-MTEF Projections", "", "2003-Actual Commitments", "", "2003-Actual Disbursements", "", "2007-MTEF Projections", "", "2007-Actual Commitments", "", "2007-Actual Disbursements", "", "2011-MTEF Projections", "33 888", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "Total Measures-MTEF Projections", "33 888", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "mtef activity 1", "2002-MTEF Projections", "", "2002-Actual Commitments", "", "2002-Actual Disbursements", "", "2003-MTEF Projections", "", "2003-Actual Commitments", "", "2003-Actual Disbursements", "", "2007-MTEF Projections", "", "2007-Actual Commitments", "", "2007-Actual Disbursements", "", "2011-MTEF Projections", "789 123", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "Total Measures-MTEF Projections", "789 123", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests().withContents("Project Title", "Activity with both MTEFs and Act.Comms", "2002-MTEF Projections", "", "2002-Actual Commitments", "", "2002-Actual Disbursements", "", "2003-MTEF Projections", "", "2003-Actual Commitments", "", "2003-Actual Disbursements", "", "2007-MTEF Projections", "", "2007-Actual Commitments", "888 000", "2007-Actual Disbursements", "", "2011-MTEF Projections", "700 000", "2011-Actual Commitments", "", "2011-Actual Disbursements", "", "2012-MTEF Projections", "150 000", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "Total Measures-MTEF Projections", "850 000", "Total Measures-Actual Commitments", "888 000", "Total Measures-Actual Disbursements", "0"));
		
		runMondrianTestCase(
			"AMP-16100-flat-mtefs-ethiopian",
			activities,
			correctReport,
			"en"
		);
	}
	
	@Test
	public void testMtefConverterDayCodes() {
		for(int i = ArConstants.MIN_SUPPORTED_YEAR; i < ArConstants.MAX_SUPPORTED_YEAR; i++) {
			if (MtefConverter.instance.mtefInfos.get(i).periodEndDayCode + 1 != MtefConverter.instance.mtefInfos.get(i + 1).periodStartDayCode)
				throw new RuntimeException(String.format("Julian day code for year %d end does not equal 1 + code for year %d", i, i + 1));
		}
		assertEquals("(11983 - 11983)", MtefConverter.instance.mtefInfos.get(1983).toString());
		assertEquals("(12008 - 12008)", MtefConverter.instance.mtefInfos.get(2008).toString());
	}
	
	@Test
	public void testMtefConverterSingleColumn() {
		AmpReports report = new AmpReports();
		report.setColumns(new HashSet<>(Arrays.asList(ampReportColumnForColName("MTEF 2011/2012", 1))));
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("aaaa", ArConstants.DONOR_TYPE);
		MtefConverter.instance.convertMtefs(report, spec);
		assertEquals("[entity: MTEF Projections]", spec.getMeasures().toString());
		assertEquals("{ElementType = MTEF_DATE, NamedTypedEntity =[null]=[FilterRule=RANGE, [12011 : 12011]]}", spec.getFilters().getFilterRules().toString());
	}
	
	@Test
	public void testMtefConverterTwoColumns() {
		AmpReports report = new AmpReports();
		report.setColumns(new HashSet<>(Arrays.asList(ampReportColumnForColName("MTEF 2011/2012", 1), ampReportColumnForColName("MTEF 2012/2013", 2))));
		
		ReportSpecificationImpl spec = new ReportSpecificationImpl("aaaa", ArConstants.DONOR_TYPE);
		MtefConverter.instance.convertMtefs(report, spec);
		assertEquals("[entity: MTEF Projections]", spec.getMeasures().toString());
		assertEquals("{ElementType = MTEF_DATE, NamedTypedEntity =[null]=[FilterRule=RANGE, [12011 : 12011], FilterRule=RANGE, [12012 : 12012]]}", spec.getFilters().getFilterRules().toString());
	}
}
