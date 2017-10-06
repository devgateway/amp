package org.dgfoundation.amp.ar.amp212;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dgfoundation.amp.nireports.schema.TimeRange;
import org.junit.Test;

/**
 * 
 * testcases for various utility classes of NiReports
 * @author Constantin Dolghier
 *
 */
public class UtilityClassesTests {
    

    @Test
    public void testTimeRange() {
        assertTrue(TimeRange.MONTH.compareTo(TimeRange.YEAR) > 0);
        assertFalse(TimeRange.YEAR.compareTo(TimeRange.MONTH) > 0);
        
        for(TimeRange tr:TimeRange.values()) {
            assertTrue(tr.compareTo(tr) == 0);
            if (tr != TimeRange.NONE) {
                assertTrue(tr.compareTo(TimeRange.NONE) > 0);
                assertTrue(TimeRange.NONE.compareTo(tr) < 0);
            }
        }
        
        assertTrue(TimeRange.NONE.compareTo(TimeRange.YEAR) < 0);
        assertTrue(TimeRange.YEAR.compareTo(TimeRange.QUARTER) < 0);
        assertTrue(TimeRange.QUARTER.compareTo(TimeRange.MONTH) < 0);
    }
}
