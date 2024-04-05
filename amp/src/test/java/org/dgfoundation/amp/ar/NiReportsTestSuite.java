package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.amp212.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All tests for written for AMP 2.12.
 *
 * @author Dolghier Constantin
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
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
