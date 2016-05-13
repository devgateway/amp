package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.AllTests_amp212;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.junit.Test;

/**
 * 
 * testcases for the components part of the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class AmpSchemaComponentsTests extends ReportingTestCase {
	
	public AmpSchemaComponentsTests() {
		super("AmpSchemaComponentsTests");
	}
	
	final String correctTotals = "{RAW / Funding / 2014 / Actual Commitments=2150, RAW / Funding / 2014 / Actual Disbursements=850, RAW / Totals / Actual Commitments=2150, RAW / Totals / Actual Disbursements=850}";

	final List<String> acts = Arrays.asList(
			"activity 1 with agreement",
			"Activity 2 with multiple agreements",
			"Activity Linked With Pledge",
			"Activity with both MTEFs and Act.Comms",
			"activity with capital spending",
			"activity with components",
			"activity with contracting agency",
			"activity with directed MTEFs",
			"activity_with_disaster_response",
			"activity with funded components",
			"activity with incomplete agreement",
			"activity with many MTEFs",
			"activity with pipeline MTEFs and act. disb",
			"Activity with planned disbursements",
			"activity with primary_program",
			"Activity with primary_tertiary_program",
			"activity with tertiary_program",
			"activity-with-unfunded-components",
			"Activity with Zones",
			"Activity With Zones and Percentages",
			"crazy funding 1",
			"date-filters-activity",
			"Eth Water",
			"execution rate activity",
			"mtef activity 1",
			"mtef activity 2",
			"new activity with contracting",
			"pledged 2",
			"pledged education activity 1",
			"Project with documents",
			"Proposed Project Cost 1 - USD",
			"Proposed Project Cost 2 - EUR",
			"ptc activity 1",
			"ptc activity 2",
			"Pure MTEF Project",
			"SSC Project 1",
			"SSC Project 2",
			"SubNational no percentages",
			"TAC_activity_1",
			"TAC_activity_2",
			"Test MTEF directed",
			"third activity with agreements",
			"Unvalidated activity",
			"with weird currencies"
		);
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	
	protected ReportSpecificationImpl buildComponentReport(String reportName, List<String> columns, List<String> measures, 
            List<String> hierarchies, GroupingCriteria groupingCriteria) {
        return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria, 
                ArConstants.COMPONENT_TYPE);
    }
	
	protected ReportSpecificationImpl buildComponentReportFilter(String reportName, List<String> columns, List<String> measures, 
            List<String> hierarchies, GroupingCriteria groupingCriteria, ReportElement elem, FilterRule rule) {
	    ReportSpecificationImpl spec = ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria, 
                ArConstants.COMPONENT_TYPE);
	    
	    ReportFiltersImpl filters = new ReportFiltersImpl();
        filters.addFilterRule(elem, rule);
        spec.setFilters(filters);
	    return spec;
    }
	
	@Test
	public void testComponentReportFlat() {
		NiReportModel cor = new NiReportModel("AMP-18720-no-hier")
			.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
				"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Component Name", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2 150", "Totals-Actual Disbursements", "850")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Component Name", "First Component", "Component Type", "Component Type 1", "Component description", "First Component Description"),
		        new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Component Name", "Monkey Business Component, Unfunded C-EN", "Component Type", "Component Type 2", "Component description", "MB Comp Desc, Unfunded C Desc - EN"),
		        new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Name", "Funded Component Title En, Funded EN", "Component Type", "some component type", "Component description", "Funded Component Description, Funded Desc en", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2 150", "Totals-Actual Disbursements", "850")      ));
		
		runNiTestCase(spec("AMP-18720-no-hier"), "en", acts, cor);
	}
	
	@Test
	public void testComponentReportByComponentName() {
		NiReportModel cor = new NiReportModel("AMP-18720-hier")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
					"(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
					"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Component Name", "", "Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2 150", "Totals-Actual Disbursements", "850")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Component Name", "Funded Component Title En", 14)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "800", "Funding-2014-Actual Disbursements", "0", "Totals-Actual Commitments", "800", "Totals-Actual Disbursements", "0", "Component Name", "Funded Component Title En")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Component Description", "Funding-2014-Actual Commitments", "800", "Totals-Actual Commitments", "800")        ),
			        new ReportAreaForTests(new AreaOwner("Component Name", "Funded EN", 15)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "1 350", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "1 350", "Totals-Actual Disbursements", "850", "Component Name", "Funded EN")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Desc en", "Funding-2014-Actual Commitments", "1 350", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "1 350", "Totals-Actual Disbursements", "850")        )      ));

		
		runNiTestCase(spec("AMP-18720-hier"), "en", acts, cor);
	}
	
	@Test
	public void testComponentReportByComponentType() {
		NiReportModel cor = new NiReportModel("testComponentReportByComponentType")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 6))",
				"(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 4, colSpan: 2))",
				"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null).withContents("Component Type", "", "Project Title", "", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2,150", "Totals-Actual Disbursements", "850")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner("Component Type", "some component type", 3)).withContents("Project Title", "", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2,150", "Totals-Actual Disbursements", "850", "Component Type", "some component type")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2,150", "Totals-Actual Disbursements", "850")        )      ));
		
		ReportSpecificationImpl spec = buildComponentReport("testComponentReportByComponentType", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.COMPONENT_TYPE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				Arrays.asList(ColumnConstants.COMPONENT_TYPE), 
				GroupingCriteria.GROUPING_YEARLY);
		
		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testHierarchiesDoNotChangeTotals() throws Exception {
		
		ReportSpecificationImpl initSpec = buildComponentReport("initSpec", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		 		
		assertEquals(correctTotals, buildDigest(initSpec, acts, BasicSanityChecks.fundingGrandTotalsDigester).toString());
				
		// single-hierarchy reports
		for(boolean isSummary:Arrays.asList(true, false)) {
			for(String hierName:BasicSanityChecks.hierarchiesToTry) {
				ReportSpecificationImpl spec = buildComponentReport(String.format("%s summary: %b", hierName, isSummary), 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, hierName), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(hierName), 
						GroupingCriteria.GROUPING_YEARLY);
				spec.setSummaryReport(isSummary);
				assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, BasicSanityChecks.fundingGrandTotalsDigester).toString());
			}
		}
	}
	
	@Override
	public void setUp() {
		AllTests_amp212.setUp();
	}
}
