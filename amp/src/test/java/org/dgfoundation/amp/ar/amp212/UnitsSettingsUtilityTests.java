/**
 * 
 */
package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.junit.Test;

/**
 * Tests report settings
 * @author Nadejda Mandrescu
 */
public class UnitsSettingsUtilityTests extends ReportingTestCase {
	
	public UnitsSettingsUtilityTests() {
		super("settings mondrian tests");
	}
	
	@Test
	public void testAmountUnitsValues() {
		assertEquals(AmpARFilter.AMOUNT_OPTION_IN_UNITS, AmountsUnits.AMOUNTS_OPTION_UNITS.code);
		assertEquals(1, AmountsUnits.AMOUNTS_OPTION_UNITS.divider);
		assertEquals(1.0d, AmountsUnits.AMOUNTS_OPTION_UNITS.multiplier);
		
		assertEquals(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS, AmountsUnits.AMOUNTS_OPTION_THOUSANDS.code);
		assertEquals(1000, AmountsUnits.AMOUNTS_OPTION_THOUSANDS.divider);
		assertEquals(0.001, AmountsUnits.AMOUNTS_OPTION_THOUSANDS.multiplier);

		assertEquals(AmpARFilter.AMOUNT_OPTION_IN_MILLIONS, AmountsUnits.AMOUNTS_OPTION_MILLIONS.code);
		assertEquals(1000 * 1000, AmountsUnits.AMOUNTS_OPTION_MILLIONS.divider);
		assertEquals(0.001d * 0.001, AmountsUnits.AMOUNTS_OPTION_MILLIONS.multiplier);
	}
	
	@Test
	public void testFindByMultiplier() {
		assertEquals(AmountsUnits.AMOUNTS_OPTION_UNITS, AmountsUnits.findByMultiplier(1));		
		assertEquals(AmountsUnits.AMOUNTS_OPTION_THOUSANDS, AmountsUnits.findByMultiplier(0.001));
		assertEquals(AmountsUnits.AMOUNTS_OPTION_MILLIONS, AmountsUnits.findByMultiplier(0.001 * 0.001));
	}
	
	@Test
	public void testCrashes() {
		shouldFail(() -> AmountsUnits.getForValue(15));
		shouldFail(() -> AmountsUnits.getAmountMultiplier(12));
		shouldFail(() -> AmountsUnits.findByMultiplier(0.25));
	}
}
