package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.amp212.*;
import org.junit.platform.suite.api.SelectClasses;

/**
 * All tests for written for AMP 2.12.
 *
 * @author Dolghier Constantin
 */
@org.junit.platform.suite.api.Suite
@SelectClasses({
        OfflineNiReportsTestSuite.class,
        NiReportsFetchingTests.class,
        AmpSchemaSanityTests.class,
        AmpSchemaSortingTests.class,
        AmpSchemaFilteringTests.class,
        AmpSchemaPledgesTests.class,
        AmpSchemaComponentsTests.class,
        AmpSchemaRegionalTests.class,
        ForecastExecutionRateTests.class,
        FundingFlowsTests.class,
        ExpenditureClassTests.class,
        NiComputedMeasuresTests.class,
        CurrencyConvertorTests.class,
        OriginalCurrencyTests.class,
        UnitsSettingsUtilityTests.class,
        DimensionsFetchingTests.class,
        SQLUtilsTests.class,
        InflationRatesTests.class,
        DateTimeTests.class
})
public class NiReportsTestSuite {
}
