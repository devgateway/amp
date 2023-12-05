package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

/**
 * Tests report settings
 * @author Nadejda Mandrescu
 */
public class UnitsSettingsUtilityTests extends AmpTestCase {

    @Test
    public void testAmountUnitsValues() {
        assertEquals(AmpARFilter.AMOUNT_OPTION_IN_UNITS, AmountsUnits.AMOUNTS_OPTION_UNITS.code);
        assertEquals(1, AmountsUnits.AMOUNTS_OPTION_UNITS.divider);

        assertEquals(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS, AmountsUnits.AMOUNTS_OPTION_THOUSANDS.code);
        assertEquals(1000, AmountsUnits.AMOUNTS_OPTION_THOUSANDS.divider);

        assertEquals(AmpARFilter.AMOUNT_OPTION_IN_MILLIONS, AmountsUnits.AMOUNTS_OPTION_MILLIONS.code);
        assertEquals(1000 * 1000, AmountsUnits.AMOUNTS_OPTION_MILLIONS.divider);

        assertEquals(AmpARFilter.AMOUNT_OPTION_IN_BILLIONS, AmountsUnits.AMOUNTS_OPTION_BILLIONS.code);
        assertEquals(1000 * 1000 * 1000, AmountsUnits.AMOUNTS_OPTION_BILLIONS.divider);
    }
    
    @Test
    public void testCrashes() {
        shouldFail(() -> AmountsUnits.getForValue(15));
        shouldFail(() -> AmountsUnits.getAmountDivider(12));
    }
}
