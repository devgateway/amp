package org.dgfoundation.amp.ar.amp212;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.nireports.amp.SelectedYearBlock;
import org.digijava.module.common.util.DateTimeUtil;
import org.junit.Test;

/**
 * 
 * testcases for various offline utility functions / classes
 * @author Constantin Dolghier
 *
 */
public class UtilsTests {

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

    @Test
    public void testKeepNDecimals() {
        double delta = 0.000001;
        assertEquals(0.001, AlgoUtils.keepNDecimals(0.001, 3), delta);
        assertEquals(0.001, AlgoUtils.keepNDecimals(0.0013, 3), delta);
        assertEquals(0.001, AlgoUtils.keepNDecimals(0.0008, 3), delta);
        assertEquals(0.001, AlgoUtils.keepNDecimals(0.0000001, 3), delta);
        assertEquals(0.0001, AlgoUtils.keepNDecimals(0.0000001, 4), delta);
    }   
}
