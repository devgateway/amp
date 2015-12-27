/**
 * 
 */
package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.junit.Test;

/**
 * Tests report settings
 * @author Nadejda Mandrescu
 */
public class SettingsMondrianReportTests extends MondrianReportsTestCase {
	
	public SettingsMondrianReportTests() {
		super("settings mondrian tests");
	}
	
	@Test
	public void test_AMP_19554_AmountsInThousands() {
		ReportAreaForTests correctReport = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "2013-Actual Commitments", "1 700", "2014-Actual Commitments", "3 365,76", "Total Measures-Actual Commitments", "5 065,76")
	    .withChildren(
	      new ReportAreaForTests().withContents("Project Title", "pledged education activity 1", "2013-Actual Commitments", "1 700", "2014-Actual Commitments", "3 300", "Total Measures-Actual Commitments", "5 000"),
	      new ReportAreaForTests().withContents("Project Title", "activity with capital spending", "2013-Actual Commitments", "", "2014-Actual Commitments", "65,76", "Total Measures-Actual Commitments", "65,76"));
		
		List<String> activities = Arrays.asList("pledged education activity 1", "activity with capital spending");
		runMondrianTestCase("AMP-19554-Amounts-in-Thousands",
				activities,
				correctReport,
				"en");
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
		shouldFail(new AmpRunnable() {
			@Override public void run() throws Exception {
				AmountsUnits.getForValue(15);
			}
		});
		
		shouldFail(new AmpRunnable() {
			@Override public void run() throws Exception {
				AmountsUnits.getAmountMultiplier(12);
			}
		});
		
		shouldFail(new AmpRunnable() {
			@Override public void run() throws Exception {
				AmountsUnits.findByMultiplier(0.25);
			}
		});		
		
	}
}
