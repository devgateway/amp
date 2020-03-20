package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.amp212.AmpSchemaComponentsTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaFilteringTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaPledgesTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaRegionalTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaSanityTests;
import org.dgfoundation.amp.ar.amp212.AmpSchemaSortingTests;
import org.dgfoundation.amp.ar.amp212.CurrencyConvertorTests;
import org.dgfoundation.amp.ar.amp212.DateTimeTests;
import org.dgfoundation.amp.ar.amp212.DimensionsFetchingTests;
import org.dgfoundation.amp.ar.amp212.ExpenditureClassTests;
import org.dgfoundation.amp.ar.amp212.ForecastExecutionRateTests;
import org.dgfoundation.amp.ar.amp212.FundingFlowsTests;
import org.dgfoundation.amp.ar.amp212.InflationRatesTests;
import org.dgfoundation.amp.ar.amp212.NiComputedMeasuresTests;
import org.dgfoundation.amp.ar.amp212.NiReportsFetchingTests;
import org.dgfoundation.amp.ar.amp212.OfflineNiReportsTestSuite;
import org.dgfoundation.amp.ar.amp212.OriginalCurrencyTests;
import org.dgfoundation.amp.ar.amp212.SQLUtilsTests;
import org.dgfoundation.amp.ar.amp212.UnitsSettingsUtilityTests;
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
