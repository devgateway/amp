package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.junit.Test;

public class ComponentReportsTests extends MondrianReportsTestCase {
	
	public ComponentReportsTests() {
		super("component reports mondrian tests");
	}
	
	@Test
	public void testPlainComponentReport() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Component Name", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with components", "Component Name", "First Component", "Component Type", "Component Type 1", "Component description", "First Component Description", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity-with-unfunded-components", "Component Name", "Monkey Business Component, Unfunded C-EN", "Component Type", "Component Type 2", "Component description", "MB Comp Desc, Unfunded C Desc - EN", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with funded components", "Component Name", "Funded Component Title En, Funded EN", "Component Type", "some component type", "Component description", "Funded Component Description, Funded Desc en", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")  );

		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Component Name", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "проект с подпроектами", "Component Name", "Первый подпроект", "Component Type", "Component Type 1", "Component description", "Описание первого подпроекта", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity-with-unfunded-components", "Component Name", "Monkey Business Component, Unfunded C - RU", "Component Type", "Component Type 2", "Component description", "MB Comp Desc, Unfunded C Desc - EN", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with funded components", "Component Name", "Funded Component Title Ru, Funded RU", "Component Type", "some component type", "Component description", "Funded Component Description, hooray ru!", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")  );

		runMondrianTestCase("AMP-18720-no-hier",
			Arrays.asList("activity with components", "activity-with-unfunded-components", "activity with funded components", "crazy funding 1", "Eth Water"), 
			correctReportEn, "en");

		runMondrianTestCase("AMP-18720-no-hier",
				Arrays.asList("activity with components", "activity-with-unfunded-components", "activity with funded components", "crazy funding 1", "Eth Water"), 
				correctReportRu, "ru");
	}
	
	//@Test
	public void testHierComponentReport() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Component Name", "Report Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Component Name", "Funded Component Title En Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "800", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Component Name", "Funded Component Title En", "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Component Description", "2014-Actual Commitments", "800", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Component Name", "Funded EN Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "1 350", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "1 350", "Total Measures-Actual Disbursements", "850")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Component Name", "Funded EN", "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Desc en", "2014-Actual Commitments", "1 350", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "1 350", "Total Measures-Actual Disbursements", "850")    )  );
		
		
		ReportAreaForTests correctReportRu = new ReportAreaForTests()
	    .withContents("Component Name", "Report Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "2 150", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "2 150", "Total Measures-Actual Disbursements", "850")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Component Name", "Funded Component Title Ru Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "800", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "0")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Component Name", "Funded Component Title Ru", "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Component Description", "2014-Actual Commitments", "800", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "800", "Total Measures-Actual Disbursements", "0")    ),
	      new ReportAreaForTests()
	          .withContents("Component Name", "Funded RU Totals", "Project Title", "", "Component Type", "", "Component description", "", "2014-Actual Commitments", "1 350", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "1 350", "Total Measures-Actual Disbursements", "850")
	      .withChildren(
	        new ReportAreaForTests()
	              .withContents("Component Name", "Funded RU", "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "hooray ru!", "2014-Actual Commitments", "1 350", "2014-Actual Disbursements", "850", "Total Measures-Actual Commitments", "1 350", "Total Measures-Actual Disbursements", "850")    )  );
	              

		runMondrianTestCase("AMP-18720-hier",
			Arrays.asList("activity with components", "activity-with-unfunded-components", "activity with funded components", "crazy funding 1", "Eth Water"), 
			correctReportEn, "en");

		runMondrianTestCase("AMP-18720-hier",
				Arrays.asList("activity with components", "activity-with-unfunded-components", "activity with funded components", "crazy funding 1", "Eth Water"), 
				correctReportRu, "ru");
	}
	
	@Test
	public void testComponentFundingOrg() {
		ReportAreaForTests correctReportEn = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Component Funding Organization", "", "Actual Commitments", "2 150", "Actual Disbursements", "850")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with funded components", "Component Funding Organization", "UNDP, World Bank", "Actual Commitments", "2 150", "Actual Disbursements", "850"));

		runMondrianTestCase("AMP-18720-component-funding-org",
			Arrays.asList("activity with components", "activity-with-unfunded-components", "activity with funded components", "crazy funding 1", "Eth Water"),
			correctReportEn, "en");
	}
	
}
