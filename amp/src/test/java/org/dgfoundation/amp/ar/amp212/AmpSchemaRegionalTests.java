package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmpReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class AmpSchemaRegionalTests extends AmpReportingTestCase {

    private static final List<String> ACTS = Arrays.asList(
            "regional funding activity 1",
            "regional funding activity 2",
            "regional funding activity 3");

    private static final List<String> HIERARCHIES = Arrays.asList(
            "Status", "Primary Sector", "Primary Sector Sub-Sector", "National Planning Objectives Level 1", "Regional Region"
    );

    private static final String CORRECT_TOTALS =
            "{RAW / Funding / 2017 / Actual Commitments=660, RAW / Totals / Actual Commitments=660}";

    @Test
    public void testSingleHierarchyDoesNotChangeTotals() {
        assertTotalsWithoutHierarchy();

        for (boolean isSummary : Arrays.asList(true, false)) {
            for (String hierName : HIERARCHIES) {
                assertTotalsOneHier(isSummary, hierName);
            }
        }
    }

    private void assertTotalsOneHier(boolean isSummary, String hierName) {
        ReportSpecificationImpl spec = buildRegionalReport(
                String.format("%s summary: %b", hierName, isSummary),
                Arrays.asList(ColumnConstants.PROJECT_TITLE, hierName),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(hierName),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setSummaryReport(isSummary);
        String totals = buildDigest(spec, ACTS, BasicSanityChecks.fundingGrandTotalsDigester).toString();
        assertEquals(spec.getReportName(), CORRECT_TOTALS, totals);
    }

    @Test
    public void testDoubleHierarchyDoesNotChangeTotals() {
        assertTotalsWithoutHierarchy();

        for (boolean isSummary : Arrays.asList(true, false)) {
            for (String hier1Name : HIERARCHIES) {
                for (String hier2Name : HIERARCHIES) {
                    if (!hier2Name.equals(hier1Name)) {
                        assertTotalsTwoHier(isSummary, hier1Name, hier2Name);
                    }
                }
            }
        }
    }

    private void assertTotalsTwoHier(boolean isSummary, String hier1Name, String hier2Name) {
        ReportSpecificationImpl spec = buildRegionalReport(
                String.format("%s %s summary: %b", hier1Name, hier2Name, isSummary),
                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(hier1Name, hier2Name),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setSummaryReport(isSummary);
        String totals = buildDigest(spec, ACTS, BasicSanityChecks.fundingGrandTotalsDigester).toString();
        assertEquals(spec.getReportName(), CORRECT_TOTALS, totals);
    }

    @Test
    public void testTripleHierarchyDoesNotChangeTotals() {
        assertTotalsWithoutHierarchy();

        for (boolean isSummary : Arrays.asList(true, false)) {
            for (String hier1Name : HIERARCHIES) {
                for (String hier2Name : HIERARCHIES) {
                    if (!hier2Name.equals(hier1Name)) {
                        for (String hier3Name : HIERARCHIES) {
                            if (!hier3Name.equals(hier1Name) && !hier3Name.equals(hier2Name)) {
                                assertTotalsThreeHier(isSummary, hier1Name, hier2Name, hier3Name);
                            }
                        }
                    }
                }
            }
        }
    }

    private void assertTotalsThreeHier(boolean isSummary, String hier1Name, String hier2Name, String hier3Name) {
        ReportSpecificationImpl spec = buildRegionalReport(
                String.format("%s %s %s summary: %b", hier1Name, hier2Name, hier3Name, isSummary),
                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name, hier3Name),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(hier1Name, hier2Name, hier3Name),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setSummaryReport(isSummary);
        String totals = buildDigest(spec, ACTS, BasicSanityChecks.fundingGrandTotalsDigester).toString();
        assertEquals(spec.getReportName(), CORRECT_TOTALS, totals);
    }

    private void assertTotalsWithoutHierarchy() {
        ReportSpecificationImpl initSpec = buildRegionalReport(
                "initSpec",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);

        String initTotal = buildDigest(initSpec, ACTS, BasicSanityChecks.fundingGrandTotalsDigester).toString();
        assertEquals(CORRECT_TOTALS, initTotal);
    }

    @Test
    public void testPlainReport() {
        NiReportModel cor = new NiReportModel("testPlainReport")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 3))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                        "(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Funding-2017-Actual Commitments", "660", "Totals-Actual Commitments", "660")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(100), "Project Title", "regional funding activity 1", "Funding-2017-Actual Commitments", "204", "Totals-Actual Commitments", "204"),
                                new ReportAreaForTests(new AreaOwner(101), "Project Title", "regional funding activity 2", "Funding-2017-Actual Commitments", "216", "Totals-Actual Commitments", "216"),
                                new ReportAreaForTests(new AreaOwner(102), "Project Title", "regional funding activity 3", "Funding-2017-Actual Commitments", "240", "Totals-Actual Commitments", "240")      ));

        ReportSpecificationImpl spec = buildRegionalReport(
                "testPlainReport",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(cor, spec, ACTS);
    }

    @Test
    public void testHierarchyByRegion() {
        NiReportModel cor = new NiReportModel("testHierarchyByRegion")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                        "(Regional Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
                        "(2017: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Regional Region", "", "Project Title", "", "Funding-2017-Actual Commitments", "660", "Totals-Actual Commitments", "660")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Regional Region", "Balti County", 9086))
                                        .withContents("Project Title", "", "Funding-2017-Actual Commitments", "208", "Totals-Actual Commitments", "208", "Regional Region", "Balti County")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(100), "Project Title", "regional funding activity 1", "Funding-2017-Actual Commitments", "101", "Totals-Actual Commitments", "101"),
                                                new ReportAreaForTests(new AreaOwner(101), "Project Title", "regional funding activity 2", "Funding-2017-Actual Commitments", "107", "Totals-Actual Commitments", "107")        ),
                                new ReportAreaForTests(new AreaOwner("Regional Region", "Cahul County", 9087))
                                        .withContents("Project Title", "", "Funding-2017-Actual Commitments", "216", "Totals-Actual Commitments", "216", "Regional Region", "Cahul County")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(100), "Project Title", "regional funding activity 1", "Funding-2017-Actual Commitments", "103", "Totals-Actual Commitments", "103"),
                                                new ReportAreaForTests(new AreaOwner(102), "Project Title", "regional funding activity 3", "Funding-2017-Actual Commitments", "113", "Totals-Actual Commitments", "113")        ),
                                new ReportAreaForTests(new AreaOwner("Regional Region", "Chisinau County", 9089))
                                        .withContents("Project Title", "", "Funding-2017-Actual Commitments", "236", "Totals-Actual Commitments", "236", "Regional Region", "Chisinau County")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(101), "Project Title", "regional funding activity 2", "Funding-2017-Actual Commitments", "109", "Totals-Actual Commitments", "109"),
                                                new ReportAreaForTests(new AreaOwner(102), "Project Title", "regional funding activity 3", "Funding-2017-Actual Commitments", "127", "Totals-Actual Commitments", "127")        )      ));

        ReportSpecificationImpl spec = buildRegionalReport(
                "testHierarchyByRegion",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGIONAL_REGION),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.REGIONAL_REGION),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(cor, spec, ACTS);
    }

    private ReportSpecificationImpl buildRegionalReport(String reportName, List<String> columns, List<String> measures,
            List<String> hierarchies, GroupingCriteria groupingCriteria) {
        return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria,
                ArConstants.REGIONAL_TYPE);
    }
}
