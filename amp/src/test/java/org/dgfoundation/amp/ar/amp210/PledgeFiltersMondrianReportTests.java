/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.FiltersGroup;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.junit.Test;

/**
 * Tests for pledge filters and various utilities related to SQL filters
 * 
 * @author Dolghier Constantin
 */
public class PledgeFiltersMondrianReportTests extends ReportingTestCase {
	
	public PledgeFiltersMondrianReportTests() {
		super("pledge/sql filters mondrian tests");
	}

	public static List<String> pledges = Arrays.asList("Test pledge 1", "ACVL Pledge Name 2", "free text name 2", "Heavily used pledge");
	
//	public void testColumnsAreSQLFilters() {
//		List<String[]> columns = Arrays.asList(
//				new String[] {ColumnConstants.PLEDGES_SECTORS, ColumnConstants.PRIMARY_SECTOR},
//				new String[] {ColumnConstants.PLEDGES_SECONDARY_SECTORS, ColumnConstants.SECONDARY_SECTOR},
//				new String[] {ColumnConstants.PLEDGES_PROGRAMS, ColumnConstants.PRIMARY_PROGRAM},
//				new String[] {ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR},
//				new String[] {ColumnConstants.TERTIARY_PROGRAM, ColumnConstants.TERTIARY_PROGRAM},
//				new String[] {ColumnConstants.TERTIARY_PROGRAM_LEVEL_3, ColumnConstants.TERTIARY_PROGRAM},
//				new String[] {ColumnConstants.PLEDGES_ZONES, FiltersGroup.LOCATION_FILTER},
//				new String[] {ColumnConstants.DISTRICT, FiltersGroup.LOCATION_FILTER},
//				new String[] {ColumnConstants.PLEDGES_AID_MODALITY, ColumnConstants.PLEDGES_AID_MODALITY}
//			);
//		
//		for(String[] columnInfo:columns) {
//			final String column = columnInfo[0];
//			final String genericParent = columnInfo[1];
//			
//			ReportSpecificationImpl spec = buildSpecification("test report - " + column, 
//				Arrays.asList(ColumnConstants.PROJECT_TITLE, column),
//				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
//				null, 
//				GroupingCriteria.GROUPING_YEARLY);
//			
//			MondrianReportFilters mrf = new MondrianReportFilters();
//			mrf.addFilterRule(new ReportColumn(column), new FilterRule("2125", true)); // dummy id
//			spec.setFilters(mrf);
//			
//			// testing that stuff does not crash
//			TestsMondrianReportGenerator mrg = new TestsMondrianReportGenerator("en") {
//				protected void mdxConfigCallback(MDXConfig config, ReportSpecification spec) {
//					MondrianReportFilters mrf = (MondrianReportFilters) spec.getFilters();
//					assertEquals(1, mrf.getSqlFilterRules().size());
//					assertTrue(
//						String.format("while testing column %s, the SQL filters should have a superparent of %s, but instead have it as %s", column, genericParent, mrf.getSqlFilterRules().keySet().iterator().next()),
//						mrf.getSqlFilterRules().get(genericParent) != null);
//					//System.err.println("aha: " + mrf.getSqlFilterRules().toString());
//					//System.err.println("aha: " + config.get);
//				};
//			};
//			mrg.executeReport(spec);
//			
//		}
//	}
	
	@Test
	public void testPledgeAidModalityFilter() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges Tertiary Sectors", "", "Pledges sectors", "", "Pledges Type Of Assistance", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "1 041 110,52", "Total Measures-Actual Pledge", "1 041 110,52")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "free text name 2", "Pledges Aid Modality", "Diplomats and courses", "Pledges Districts", "", "Pledges Donor Group", "Default Group", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges Tertiary Sectors", "", "Pledges sectors", "", "Pledges Type Of Assistance", "default type of assistance", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "1 041 110,52", "Total Measures-Actual Pledge", "1 041 110,52"));
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-aid-modality",
			"pledge-test-filters-aid-modality", pledges, correctReport, "en");		
	}
	
	@Test
	public void testPledgeLocationFilter() {
		ReportAreaForTests correctReport = new ReportAreaForTests()  .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "0,06", "2014-Actual Pledge", "49 343,91", "Total Measures-Actual Pledge", "49 343,97")
				  .withChildren(
						    new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Aid Modality", "Development of shared analytical studies, Interchanging models, proposals, and printed materials, Sending and exchanging experts, researchers, and professors", "Pledges Districts", "", "Pledges Donor Group", "European", "Pledges National Plan Objectives", "", "Pledge Status", "second status", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "Transnistrian Region", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Tertiary Programs", "OP1 name", "2012-Actual Pledge", "0,06", "2014-Actual Pledge", "49 343,9", "Total Measures-Actual Pledge", "49 343,97"));
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-location-transnistria",
			"pledge-test-filters-location-transnistria", pledges, correctReport, "en");		
	}
	
	@Test
	public void testPledgePrimaryProgramFilter() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "1,25", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "1 924 949,11")
	    .withChildren(
	      new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Aid Modality", "Development of shared analytical studies, Interchanging models, proposals, and printed materials, Sending and exchanging experts, researchers, and professors", "Pledges Districts", "", "Pledges Donor Group", "European", "Pledges National Plan Objectives", "", "Pledge Status", "second status", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Tertiary Programs", "OP1 name", "2012-Actual Pledge", "1,25", "2014-Actual Pledge", "986 878,1", "Total Measures-Actual Pledge", "986 879,35"),
	      new ReportAreaForTests().withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges Aid Modality", "Conferences, seminars, capacity specializations", "Pledges Districts", "", "Pledges Donor Group", "Default Group", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Tertiary Programs", "OP1 name", "2012-Actual Pledge", "", "2014-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"));

		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-prim-program-p1b",
			"pledge-test-filters-prim-program-p1b", pledges, correctReport, "en");		
	}

	@Test
	public void testPledgePrimarySectorFilter() {
		ReportAreaForTests correctReport = new ReportAreaForTests()  .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "0,81", "2014-Actual Pledge", "641 470,77", "Total Measures-Actual Pledge", "641 471,58")
				  .withChildren(
						    new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Aid Modality", "Development of shared analytical studies, Interchanging models, proposals, and printed materials, Sending and exchanging experts, researchers, and professors", "Pledges Districts", "", "Pledges Donor Group", "European", "Pledges National Plan Objectives", "", "Pledge Status", "second status", "Pledges sectors", "113 - SECONDARY EDUCATION", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Tertiary Programs", "OP1 name", "2012-Actual Pledge", "0,81", "2014-Actual Pledge", "641 470,77", "Total Measures-Actual Pledge", "641 471,58"));

		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-prim-sector",
			"pledge-test-filters-prim-sector", pledges, correctReport, "en");		
	}
	
	@Test
	public void testPledgeSecondarySectorFilter() {
		ReportAreaForTests correctReport =new ReportAreaForTests()
	    .withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "1 041 110,52", "Total Measures-Actual Pledge", "1 979 180,27")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "ACVL Pledge Name 2", "Pledges Aid Modality", "Conferences, seminars, capacity specializations", "Pledges Districts", "", "Pledges Donor Group", "Default Group", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Tertiary Programs", "OP1 name", "2012-Actual Pledge", "", "Total Measures-Actual Pledge", "938 069,75"),
	      new ReportAreaForTests()
	          .withContents("Pledges Titles", "free text name 2", "Pledges Aid Modality", "Diplomats and courses", "Pledges Districts", "", "Pledges Donor Group", "Default Group", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges sectors", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "1-DEMOCRATIC COUNTRY", "Pledges Tertiary Programs", "", "2012-Actual Pledge", "1 041 110,52", "Total Measures-Actual Pledge", "1 041 110,52")  );
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-secondary-sector",
			"pledge-test-filters-secondary-sector", pledges, correctReport, "en");		
	}
	
	@Test
	public void testPledgeTypeOfAssistanceFilter() {
		ReportAreaForTests correctReport = new ReportAreaForTests().withContents("Pledges Titles", "Report Totals", "Pledges Aid Modality", "", "Pledges Districts", "", "Pledges Donor Group", "", "Pledges National Plan Objectives", "", "Pledge Status", "", "Pledges Tertiary Sectors", "", "Pledges sectors", "", "Pledges Type Of Assistance", "", "Pledges Programs", "", "Pledges Regions", "", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "", "Pledges Tertiary Programs", "", "2014-Actual Pledge", "219 202,1", "Total Measures-Actual Pledge", "219 202,1")
				.withChildren(
						new ReportAreaForTests().withContents("Pledges Titles", "Test pledge 1", "Pledges Aid Modality", "Interchanging models, proposals, and printed materials", "Pledges Districts", "", "Pledges Donor Group", "European", "Pledges National Plan Objectives", "", "Pledge Status", "second status", "Pledges Tertiary Sectors", "", "Pledges sectors", "112 - BASIC EDUCATION, 113 - SECONDARY EDUCATION", "Pledges Type Of Assistance", "second type of assistance", "Pledges Programs", "Subprogram p1.b", "Pledges Regions", "Balti County, Cahul County, Transnistrian Region", "Pledges Secondary Programs", "", "Pledges Secondary Sectors", "3 NATIONAL COMPETITIVENESS, 5 REGIONAL DEVELOPMENT", "Pledges Tertiary Programs", "OP1 name", "2014-Actual Pledge", "219 202,1", "Total Measures-Actual Pledge", "219 202,1"));
		
		// String testName, String reportName, List<String> activities, GeneratedReport correctResult, String locale
		runMondrianTestCase("pledge-test-filters-type-of-assistance",
			"pledge-test-filters-type-of-assistance", pledges, correctReport, "en");		
	}
	

}
