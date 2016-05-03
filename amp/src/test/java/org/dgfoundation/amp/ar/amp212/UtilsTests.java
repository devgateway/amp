package org.dgfoundation.amp.ar.amp212;

import java.time.LocalDate;

import org.dgfoundation.amp.nireports.amp.SelectedYearBlock;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Test;


/**
 * 
 * testcases for graph algorithms
 * @author Constantin Dolghier
 *
 */
public class UtilsTests extends AmpTestCase {
	
	public UtilsTests() {
		super("utils tests");
	}
	
	@Test
	public void testPreviousMonthMidYear() {
		SelectedYearBlock block = SelectedYearBlock.buildFor(2014, LocalDate.of(2015, 6, 3)); // 3rd of June 2015
		assertEquals(2014, block.selectedYear);
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2014, 1, 1)), block.selectedYearStartJulian);
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2014, 12, 31)), block.selectedYearEndJulian);
		
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2015, 5, 1)), block.previousMonthStartJulian); //1st of may 2015
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2015, 5, 31)), block.previousMonthEndJulian); //31st of may 2015
	}
	
	@Test
	public void testPreviousMonthBegYear() {
		SelectedYearBlock block = SelectedYearBlock.buildFor(2017, LocalDate.of(2015, 1, 4)); // 4rd of January 2015
		assertEquals(2017, block.selectedYear);
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2014, 12, 1)), block.previousMonthStartJulian); //1st of december 2014
		assertEquals(DateTimeUtil.toJulianDayNumber(LocalDate.of(2014, 12, 31)), block.previousMonthEndJulian); //31st of december 2014
	}

}
