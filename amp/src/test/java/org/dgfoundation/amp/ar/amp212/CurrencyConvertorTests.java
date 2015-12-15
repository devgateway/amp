package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Test;


/**
 * 
 * testcases for the AMP currency convertor
 * @author Constantin Dolghier
 *
 */
public class CurrencyConvertorTests extends AmpTestCase {
	
	public CurrencyConvertorTests() {
		super("Currency Convertor tests");
	}
	
	@Test
	public void testLocalDateToJulian() {
		assertEquals(2456320, DateTimeUtil.toJulianDayNumber(LocalDate.of(2013, 1, 27)));
		assertEquals(2456268, DateTimeUtil.toJulianDayNumber(LocalDate.of(2012, 12, 06)));
		assertEquals(2454236, DateTimeUtil.toJulianDayNumber(LocalDate.of(2007, 05, 15)));
	}
}
