package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.junit.Test;

/**
 * organisation filters: DA, EA, IA, RO, BA
 * @author simple
 *
 */
public class OrganisationsMondrianReportTests extends MondrianReportsTestCase {
	
	public OrganisationsMondrianReportTests() {
		super("organisation mondrian tests");
	}
		
	@Test
	public void testProjectsWithoutDonorFunding() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "688 777")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Finance", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "activity with components", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Project with documents", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Donor Group", "American, Default Group, European", "Donor Type", "Default", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "545 000")  );
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Proposed Project Cost 1 - USD", "Test MTEF directed", "Pure MTEF Project", "activity with components", "Project with documents", "Eth Water"),
				correctReport,
				"en");
	}
	
	@Test
	public void testPureMtefProjectsDonorInfo() {
		// annual non-hierarchical reports without data per years provide empty results (same behavior was in old reports engine)
		ReportAreaForTests correctReport = new ReportAreaForTests().withChildren(  );
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Pure MTEF Project"),
				correctReport,
				"en"); // tests that projects

	}
	
	@Test
	public void testDirectedMtefProjectsDonorInfo() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "Donor Group", "", "Donor Type", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy", "Donor Group", "National", "Donor Type", "Default", "2010-Actual Commitments", "", "2010-Actual Disbursements", "143 777", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777")  );
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Test MTEF directed"),
				correctReport,
				"en"); // tests that projects

	}
	
	@Test
	public void testFundlessDonorInfo() {
		// annual non-hierarchical reports without data per years provide empty results (same behavior was in old reports engine)
		ReportAreaForTests correctReport = new ReportAreaForTests().withChildren(  );
		
		runMondrianTestCase(
				"all-donor-info",						
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents"),
				correctReport,
				"en"); // tests that projects

	}
	
	@Test
	public void testRelatedAgenciesFlat() {
		List<String> activities = 
				Arrays.asList("Proposed Project Cost 2 - EUR", "Test MTEF directed", "Pure MTEF Project", "Eth Water", "mtef activity 1", "date-filters-activity", "pledged 2");

		ReportAreaForTests correctReport = new ReportAreaForTests()
    .withContents("Project Title", "Report Totals", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "203 777", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "545 000", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 195 000", "Total Measures-Actual Disbursements", "1 210 777")
  .withChildren(
    new ReportAreaForTests()
        .withContents("Project Title", "Proposed Project Cost 2 - EUR", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
    new ReportAreaForTests()
        .withContents("Project Title", "Test MTEF directed", "Responsible Organization", "72 Local Public Administrations from RM, UNDP", "Responsible Organization Groups", "Default Group, International", "Executing Agency", "Water Foundation", "Executing Agency Groups", "American", "Implementing Agency", "USAID", "Implementing Agency Groups", "American", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
    new ReportAreaForTests()
        .withContents("Project Title", "Pure MTEF Project", "Responsible Organization", "Ministry of Finance", "Responsible Organization Groups", "National", "Executing Agency", "Ministry of Economy", "Executing Agency Groups", "National", "Implementing Agency", "Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
    new ReportAreaForTests()
        .withContents("Project Title", "Eth Water", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "UNDP, World Bank", "Executing Agency Groups", "International", "Implementing Agency", "Ministry of Economy, Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "Water Foundation, Water Org", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "2014-Actual Commitments", "0", "2014-Actual Disbursements", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "545 000"),
    new ReportAreaForTests()
        .withContents("Project Title", "mtef activity 1", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
    new ReportAreaForTests()
        .withContents("Project Title", "date-filters-activity", "Responsible Organization", "Ministry of Economy", "Responsible Organization Groups", "National", "Executing Agency", "UNDP", "Executing Agency Groups", "International", "Implementing Agency", "Finland", "Implementing Agency Groups", "Default Group", "Beneficiary Agency", "Water Foundation", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
    new ReportAreaForTests()
        .withContents("Project Title", "pledged 2", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")  );
		
		runMondrianTestCase(
				"all-related-agencies-flat",
				activities,
				correctReport,
				"en");
				
		ReportSpecificationImpl spec = buildSpecification("all-related-agencies-flat-programmatically",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, 
						ColumnConstants.RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS,
						ColumnConstants.EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY_GROUPS,
						ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS,
						ColumnConstants.BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY_GROUPS),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		spec.setDisplayEmptyFundingRows(true);
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		runMondrianTestCase(spec, "en", activities, correctReport);
	}
	
	@Test
	public void testResponsibleOrganisationFilter() {
		List<String> activities = 
				Arrays.asList("Proposed Project Cost 2 - EUR", "Test MTEF directed", "Pure MTEF Project", "Eth Water", "mtef activity 1", "date-filters-activity", "pledged 2");

		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "71 888,5", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "71 888,5")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Responsible Organization", "72 Local Public Administrations from RM", "Responsible Organization Groups", "Default Group", "Executing Agency", "Water Foundation", "Executing Agency Groups", "American", "Implementing Agency", "USAID", "Implementing Agency Groups", "American", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "71 888,5", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "71 888,5")  );
				
		ReportSpecificationImpl spec = buildSpecification("all-related-agencies-flat-filtered-programmatically",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, 
						ColumnConstants.RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS,
						ColumnConstants.EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY_GROUPS,
						ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS,
						ColumnConstants.BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY_GROUPS),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		spec.setDisplayEmptyFundingRows(true);
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION), new FilterRule("21378", true)); //72 local
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", activities, correctReport);
		
		
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS), new FilterRule("17", true)); // default group
		spec.setFilters(filters);
		runMondrianTestCase(spec, "en", activities, correctReport);
	}
	
	@Test
	public void testExecutingAgencyFilter() {
		List<String> activities = 
				Arrays.asList("Proposed Project Cost 2 - EUR", "Test MTEF directed", "Pure MTEF Project", "Eth Water", "mtef activity 1", "date-filters-activity", "pledged 2");

		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "272 500", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "272 500")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "World Bank", "Executing Agency Groups", "International", "Implementing Agency", "Ministry of Economy, Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "Water Foundation, Water Org", "Beneficiary Agency Groups", "American", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "272 500", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "272 500"));
				
		ReportSpecificationImpl spec = buildSpecification("all-related-agencies-flat-filtered-programmatically",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, 
						ColumnConstants.RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS,
						ColumnConstants.EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY_GROUPS,
						ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS,
						ColumnConstants.BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY_GROUPS),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		spec.setDisplayEmptyFundingRows(true);
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.EXECUTING_AGENCY), new FilterRule("21697", true)); //world bank
		spec.setFilters(filters);
		
		runMondrianTestCase(spec, "en", activities, correctReport);
		
		
		ReportAreaForTests correctReport2 = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "617 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "UNDP, World Bank", "Executing Agency Groups", "International", "Implementing Agency", "Ministry of Economy, Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "Water Foundation, Water Org", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "545 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Responsible Organization", "Ministry of Economy", "Responsible Organization Groups", "National", "Executing Agency", "UNDP", "Executing Agency Groups", "International", "Implementing Agency", "Finland", "Implementing Agency Groups", "Default Group", "Beneficiary Agency", "Water Foundation", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")  );
		
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.EXECUTING_AGENCY_GROUPS), new FilterRule("20", true)); // international
		spec.setFilters(filters);
		runMondrianTestCase(spec, "en", activities, correctReport2);

	}
	
	@Test
	public void testOrgTypeFiltering() {
		List<String> activities = 
				Arrays.asList("Proposed Project Cost 2 - EUR", "Test MTEF directed", "Pure MTEF Project", "Eth Water", "mtef activity 1", "date-filters-activity", "pledged 2");

		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "", "Executing Agency Groups", "", "Implementing Agency", "", "Implementing Agency Groups", "", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "203 777", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "760 777")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "Test MTEF directed", "Responsible Organization", "72 Local Public Administrations from RM, UNDP", "Responsible Organization Groups", "Default Group, International", "Executing Agency", "Water Foundation", "Executing Agency Groups", "American", "Implementing Agency", "USAID", "Implementing Agency Groups", "American", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "143 777", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "0", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "143 777"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Pure MTEF Project", "Responsible Organization", "Ministry of Finance", "Responsible Organization Groups", "National", "Executing Agency", "Ministry of Economy", "Executing Agency Groups", "National", "Implementing Agency", "Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "", "Beneficiary Agency Groups", "", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "0"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "Eth Water", "Responsible Organization", "", "Responsible Organization Groups", "", "Executing Agency", "UNDP, World Bank", "Executing Agency Groups", "International", "Implementing Agency", "Ministry of Economy, Ministry of Finance", "Implementing Agency Groups", "National", "Beneficiary Agency", "Water Foundation, Water Org", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "0", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "0", "2012-Actual Commitments", "0", "2012-Actual Disbursements", "0", "2013-Actual Commitments", "0", "2013-Actual Disbursements", "545 000", "Total Measures-Actual Commitments", "0", "Total Measures-Actual Disbursements", "545 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Responsible Organization", "Ministry of Economy", "Responsible Organization Groups", "National", "Executing Agency", "UNDP", "Executing Agency Groups", "International", "Implementing Agency", "Finland", "Implementing Agency Groups", "Default Group", "Beneficiary Agency", "Water Foundation", "Beneficiary Agency Groups", "American", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000")  );
		
				
		ReportSpecificationImpl spec = buildSpecification("all-related-agencies-filter-by-IA",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, 
						ColumnConstants.RESPONSIBLE_ORGANIZATION, ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS,
						ColumnConstants.EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY_GROUPS,
						ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS,
						ColumnConstants.BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY_GROUPS),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		spec.setDisplayEmptyFundingRows(true);
		spec.setCalculateColumnTotals(true);
		spec.setCalculateRowTotals(true);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY_TYPE), new FilterRule("38", true)); // "default"
		spec.setFilters(filters);
		runMondrianTestCase(spec, "en", activities, correctReport);
	
		
		filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY_TYPE), new FilterRule("338", true)); // nonexistant
		spec.setFilters(filters);
		runMondrianTestCase(spec, "en", activities, new ReportAreaForTests().withChildren()); //empty output
	}
	
	@Test
	public void testMiscAgencies() {
		ReportSpecificationImpl spec = buildSpecification("AMP-18829", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.CONTRACTING_AGENCY, ColumnConstants.REGIONAL_GROUP, ColumnConstants.SECTOR_GROUP), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingRows(true);
		ReportAreaForTests correct = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Contracting Agency", "", "Regional Group", "", "Sector Group", "", "Actual Commitments", "7 178 840,58", "Actual Disbursements", "500 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "pledged 2", "Contracting Agency", "", "Regional Group", "", "Sector Group", "", "Actual Commitments", "7 070 000", "Actual Disbursements", "450 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Contracting Agency", "Water Foundation", "Regional Group", "UNDP", "Sector Group", "World Bank", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"),
	      new ReportAreaForTests().withContents("Project Title", "new activity with contracting", "Contracting Agency", "Norway", "Regional Group", "UNDP", "Sector Group", "", "Actual Commitments", "12 000", "Actual Disbursements", "0"));
		List<String> activities = Arrays.asList("pledged 2", "activity with contracting agency", "new activity with contracting");
		runMondrianTestCase(spec, "en", activities, correct);
		
		MondrianReportFilters filters = new MondrianReportFilters();
		filters.addFilterRule(new ReportColumn(ColumnConstants.CONTRACTING_AGENCY), new FilterRule("21702", true));
		spec.setFilters(filters);
		ReportAreaForTests filteredCorrect = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Contracting Agency", "", "Regional Group", "", "Sector Group", "", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "activity with contracting agency", "Contracting Agency", "Water Foundation", "Regional Group", "UNDP", "Sector Group", "World Bank", "Actual Commitments", "96 840,58", "Actual Disbursements", "50 000"));
		runMondrianTestCase(spec, "en", activities, filteredCorrect);
	}
}
