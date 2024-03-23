package org.dgfoundation.amp.ar.amp212;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * testcases for the components part of the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class AmpSchemaComponentsTests extends AmpReportingTestCase {
    
    private static final String correctTotals = "{RAW / Funding / 2014 / Actual Commitments=2150, RAW / Funding / 2014 / Actual Disbursements=850, RAW / Funding / 2017 / Actual Commitments=1100, RAW / Funding / 2017 / Actual Disbursements=0, RAW / Totals / Actual Commitments=3250, RAW / Totals / Actual Disbursements=850}";

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
            "activity with funded components 2",
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

    private static final List<String> HIERARCHIES_TO_TRY = new ImmutableList.Builder<String>()
            .addAll(BasicSanityChecks.HIERARCHIES_TO_TRY)
            .add(ColumnConstants.COMPONENT_TYPE)
            .add(ColumnConstants.COMPONENT_NAME)
            .add(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION)
            .add(ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION)
            .build();

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
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Component Name", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "1 100", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "3 250", "Totals-Actual Disbursements", "850")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Component Name", "First Component", "Component Type", "Component Type 1", "Component description", "First Component Description"),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Component Name", "Monkey Business Component, Unfunded C-EN", "Component Type", "Component Type 2", "Component description", "MB Comp Desc, Unfunded C Desc - EN"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Name", "Funded Component Title En, Funded EN", "Component Type", "some component type", "Component description", "Funded Component Description, Funded Desc en", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "2 150", "Totals-Actual Disbursements", "850"),
                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Name", "Funded Component 1, Funded Component 2", "Component Type", "Component Type 1, Component Type 2", "Component description", "Desc 1, Desc 2", "Funding-2017-Actual Commitments", "1 100", "Totals-Actual Commitments", "1 100")      ));
        
        runNiTestCase(spec("AMP-18720-no-hier"), "en", acts, cor);
    }
    
    @Test
    public void testComponentReportByComponentName() {
        NiReportModel cor = new NiReportModel("AMP-18720-hier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                        "(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 2))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Component Name", "", "Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "2 150", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "1 100", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "3 250", "Totals-Actual Disbursements", "850")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Component Name", "Funded Component 1", 21)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "600", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "600", "Totals-Actual Disbursements", "0", "Component Name", "Funded Component 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Type", "Component Type 1", "Component description", "Desc 1", "Funding-2017-Actual Commitments", "600", "Totals-Actual Commitments", "600")        ),
                                new ReportAreaForTests(new AreaOwner("Component Name", "Funded Component 2", 20)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "500", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "500", "Totals-Actual Disbursements", "0", "Component Name", "Funded Component 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Type", "Component Type 2", "Component description", "Desc 2", "Funding-2017-Actual Commitments", "500", "Totals-Actual Commitments", "500")        ),
                                new ReportAreaForTests(new AreaOwner("Component Name", "Funded Component Title En", 14)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "800", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "0", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "800", "Totals-Actual Disbursements", "0", "Component Name", "Funded Component Title En")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Component Description", "Funding-2014-Actual Commitments", "800", "Totals-Actual Commitments", "800")        ),
                                new ReportAreaForTests(new AreaOwner("Component Name", "Funded EN", 15)).withContents("Project Title", "", "Component Type", "", "Component description", "", "Funding-2014-Actual Commitments", "1 350", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "0", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "1 350", "Totals-Actual Disbursements", "850", "Component Name", "Funded EN")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Type", "some component type", "Component description", "Funded Desc en", "Funding-2014-Actual Commitments", "1 350", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Commitments", "1 350", "Totals-Actual Disbursements", "850")        )      ));

        
        runNiTestCase(spec("AMP-18720-hier"), "en", acts, cor);
    }
    
    @Test
    public void testComponentReportByComponentType() {
        NiReportModel cor = new NiReportModel("testComponentReportByComponentType")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
                        "(Component Type: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Component Type", "", "Project Title", "", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "1,100", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "3,250", "Totals-Actual Disbursements", "850")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Component Type", "Component Type 1", 1)).withContents("Project Title", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "600", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "600", "Totals-Actual Disbursements", "0", "Component Type", "Component Type 1")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Funding-2017-Actual Commitments", "600", "Totals-Actual Commitments", "600")        ),
                                new ReportAreaForTests(new AreaOwner("Component Type", "Component Type 2", 2)).withContents("Project Title", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "500", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "500", "Totals-Actual Disbursements", "0", "Component Type", "Component Type 2")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Funding-2017-Actual Commitments", "500", "Totals-Actual Commitments", "500")        ),
                                new ReportAreaForTests(new AreaOwner("Component Type", "some component type", 3)).withContents("Project Title", "", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "0", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "2,150", "Totals-Actual Disbursements", "850", "Component Type", "some component type")
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
        for (boolean isSummary : Arrays.asList(true, false)) {
            for (String hierName : HIERARCHIES_TO_TRY) {
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

    @Test
    public void testFundingOrgHierarchy() {
        NiReportModel cor = new NiReportModel("testFundingOrgHierarchy")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                        "(Component Funding Organization: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Component Second Responsible Organization: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1));(Description of Component Funding: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
                        "(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Component Funding Organization", "", "Project Title", "", "Component Name", "", "Component description", "", "Component Second Responsible Organization", "", "Description of Component Funding", "", "Funding-2014-Actual Commitments", "2,150", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "1,100", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "3,250", "Totals-Actual Disbursements", "850")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Component Funding Organization", "UNDP", 21695)).withContents("Project Title", "", "Component Name", "", "Component description", "", "Component Second Responsible Organization", "", "Description of Component Funding", "", "Funding-2014-Actual Commitments", "800", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "0", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "800", "Totals-Actual Disbursements", "0", "Component Funding Organization", "UNDP")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Name", "Funded Component Title En", "Component description", "Funded Component Description", "Funding-2014-Actual Commitments", "800", "Totals-Actual Commitments", "800")        ),
                                new ReportAreaForTests(new AreaOwner("Component Funding Organization", "World Bank", 21697)).withContents("Project Title", "", "Component Name", "", "Component description", "", "Component Second Responsible Organization", "", "Description of Component Funding", "", "Funding-2014-Actual Commitments", "1,350", "Funding-2014-Actual Disbursements", "0", "Funding-2017-Actual Commitments", "0", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "1,350", "Totals-Actual Disbursements", "0", "Component Funding Organization", "World Bank")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Name", "Funded EN", "Component description", "Funded Desc en", "Funding-2014-Actual Commitments", "1,350", "Totals-Actual Commitments", "1,350")        ),
                                new ReportAreaForTests(new AreaOwner("Component Funding Organization", "Component Funding Organization: Undefined", -999999999))
                                        .withContents("Project Title", "", "Component Name", "", "Component description", "", "Component Second Responsible Organization", "", "Description of Component Funding", "", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "850", "Funding-2017-Actual Commitments", "1,100", "Funding-2017-Actual Disbursements", "0", "Totals-Actual Commitments", "1,100", "Totals-Actual Disbursements", "850", "Component Funding Organization", "Component Funding Organization: Undefined")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Component Name", "Funded EN", "Component description", "Funded Desc en", "Funding-2014-Actual Disbursements", "850", "Totals-Actual Disbursements", "850"),
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Name", "Funded Component 1, Funded Component 2", "Component description", "Desc 1, Desc 2", "Description of Component Funding", "CC Desc 1, CC Desc 2", "Funding-2017-Actual Commitments", "1,100", "Totals-Actual Commitments", "1,100")        )      ));

        ReportSpecificationImpl spec = buildComponentReport("testFundingOrgHierarchy",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, ColumnConstants.COMPONENT_NAME, ColumnConstants.COMPONENT_DESCRIPTION, ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                Arrays.asList(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testSecondResponsibleOrgHierarchy() {
        NiReportModel cor = new NiReportModel("testSecondResponsibleOrgHierarchy")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                        "(Component Second Responsible Organization: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Component Name: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Component description: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Description of Component Funding: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 2));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 2))",
                        "(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
                        "(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Component Second Responsible Organization", "", "Project Title", "", "Component Name", "", "Component description", "", "Description of Component Funding", "", "Funding-2017-Planned Commitments", "0", "Funding-2017-Planned Disbursements", "1,120", "Totals-Planned Commitments", "0", "Totals-Planned Disbursements", "1,120")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Component Second Responsible Organization", "Finland", 21698)).withContents("Project Title", "", "Component Name", "", "Component description", "", "Description of Component Funding", "", "Funding-2017-Planned Commitments", "0", "Funding-2017-Planned Disbursements", "250", "Totals-Planned Commitments", "0", "Totals-Planned Disbursements", "250", "Component Second Responsible Organization", "Finland")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Name", "Funded Component 2", "Component description", "Desc 2", "Description of Component Funding", "CD Desc 2", "Funding-2017-Planned Disbursements", "250", "Totals-Planned Disbursements", "250")        ),
                                new ReportAreaForTests(new AreaOwner("Component Second Responsible Organization", "Norway", 21694)).withContents("Project Title", "", "Component Name", "", "Component description", "", "Description of Component Funding", "", "Funding-2017-Planned Commitments", "0", "Funding-2017-Planned Disbursements", "870", "Totals-Planned Commitments", "0", "Totals-Planned Disbursements", "870", "Component Second Responsible Organization", "Norway")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(99), "Project Title", "activity with funded components 2", "Component Name", "Funded Component 1", "Component description", "Desc 1", "Description of Component Funding", "CD Desc 1", "Funding-2017-Planned Disbursements", "870", "Totals-Planned Disbursements", "870")        )      ));

        ReportSpecificationImpl spec = buildComponentReport("testSecondResponsibleOrgHierarchy",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, ColumnConstants.COMPONENT_NAME, ColumnConstants.COMPONENT_DESCRIPTION, ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING),
                Arrays.asList(MeasureConstants.PLANNED_COMMITMENTS, MeasureConstants.PLANNED_DISBURSEMENTS),
                Arrays.asList(ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", acts, cor);
    }
}
