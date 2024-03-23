package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/** Test NumberCell compareTo method
 * 
 * @author Viorel Chihai
 *
 */
public class NumberedCellTest {

    @Test
    public void testCheckBothNull() {
        assertCompareTo(null, null, 0);
    }
    
    
    @Test
    public void testCheckLeftNull() {
        assertCompareTo(null, new BigDecimal(5), -1);
    }
    
    @Test
    public void testCheckRightNull() {
        assertCompareTo(new BigDecimal(5), null, 1);
    }
    
    @Test
    public void testCheckBothNotNull() {
        assertCompareTo(new BigDecimal(6), new BigDecimal(5), 1);
    }
    
    @Test
    public void testCheckBothNotNullEquals() {
        assertCompareTo(new BigDecimal(4), new BigDecimal(4), 0);
    }
    
    private void assertCompareTo(BigDecimal v1, BigDecimal v2, int expectedResult) {
        NumberedCell c1 = generateNumberedCell(v1);
        NumberedCell c2 = generateNumberedCell(v2);
        assertEquals(expectedResult, c1.compareTo(c2));  // null == null
    }
    
    private NumberedCell generateNumberedCell(BigDecimal v) {
        return new NiAmountCell(v, null);
    }
}
