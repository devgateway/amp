package org.dgfoundation.amp.ar.amp212;

import org.junit.platform.suite.api.SelectClasses;

/**
 * entry point for offline NiReports tests
 *
 * @author Dolghier Constantin
 */
@org.junit.platform.suite.api.Suite
@SelectClasses({
        GraphAlgorithmsTests.class,
        InclusiveRunnerTests.class,
        MetaInfoTests.class,
        FundingFlowsInnerTests.class,
        DimensionSnapshotTests.class,
        BigDecimalPrecisionTests.class,
        HierarchyTrackingTestcases.class,
        OffDbNiReportEngineTests.class,
        OffDbNiReportSortingTests.class,
        PercentagesCorrectorTests.class,
        SummaryReportsTests.class,
        PaginationTests.class,
        FormattingTests.class,
        OffDbNiReportFilteringTests.class,
        UtilsTests.class,
        UnitsSettingsUtilityTests.class,
        ETLTests.class,
        FilterRuleTests.class,
        ExpressionTreeTestcases.class
})
public class OfflineNiReportsTestSuite {
}
