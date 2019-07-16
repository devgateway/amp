package org.dgfoundation.amp.ar.amp212;

import java.math.BigDecimal;

import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

/**
 * 
 * testcases for the NiReports AmpPrecisionSetting
 * 
 * @author Alexandru Cartaleanu
 *
 */

public class BigDecimalPrecisionTests extends AmpTestCase {
    
    AmpPrecisionSetting p = new AmpPrecisionSetting();

    public String getSequenceOfNumber(int number, int count) {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < count; i++)
            bld.append(number);
        return bld.toString();
    }
    

    public void testPair(String beforeAdjustment, String afterAdjustment) {
        assertBigDecimalEquals(p.adjustPrecision(new BigDecimal(beforeAdjustment)), new BigDecimal(afterAdjustment));
    }
    
    @Test
    public void testLargeNumbers() {
        testPair("9223372036854775807", "9223372036854775807");
        testPair("9223372036854775807.999999", "9223372036854775807.999999");
        testPair("9223372036854775807.9999999", "9223372036854775808");
        testPair("9223372036854775807.0000001", "9223372036854775807.000000");
        testPair("9223372036854775807.0000009", "9223372036854775807.000001");
    }

    @Test
    public void testHumongousNumbers() {
        testPair(getSequenceOfNumber(9, 99999),getSequenceOfNumber(9, 99999));

    }

    @Test
    public void testNegativeHumongousNumbers() {
        testPair("-" + getSequenceOfNumber(9, 99999),"-" + getSequenceOfNumber(9, 99999));

    }
    
    
    public void testZeroHandling() {
        testPair("0.000000", "0.000000");
        testPair("0.000000", "0");
        testPair("0", "0.000000");
    }

    public void testNegativeZeroHandling() {
        testPair("-0.000000", "-0.000000");
        testPair("-0.000000", "-0");
        testPair("-0", "-0.000000");
        testPair("-0.000000", "0.000000");
        testPair("-0.000000", "0");
        testPair("-0", "0.000000");
        testPair("0.000000", "-0.000000");
        testPair("0.000000", "-0");
        testPair("0", "-0.000000");
    }
    
    
    
    public void testTrailingZeroHandling() {
        testPair("0." + getSequenceOfNumber(0, 999), "0.000000");
        testPair("0." + getSequenceOfNumber(0, 999), "0");
        testPair("0." + getSequenceOfNumber(0, 999), "0." + getSequenceOfNumber(0, 999));
    }   

    public void testNegativeTrailingZeroHandling() {
        testPair("0." + getSequenceOfNumber(0, 999), "-0.000000");
        testPair("0." + getSequenceOfNumber(0, 999), "-0");
        testPair("0." + getSequenceOfNumber(0, 999), "-0." + getSequenceOfNumber(0, 999));
        testPair("-0." + getSequenceOfNumber(0, 999), "0.000000");
        testPair("-0." + getSequenceOfNumber(0, 999), "0");
        testPair("-0." + getSequenceOfNumber(0, 999), "0." + getSequenceOfNumber(0, 999));
        testPair("-0." + getSequenceOfNumber(0, 999), "-0.000000");
        testPair("-0." + getSequenceOfNumber(0, 999), "-0");
        testPair("-0." + getSequenceOfNumber(0, 999), "-0." + getSequenceOfNumber(0, 999));
    }       
    
    
    
    public void testLeadingZeroHandling() {
        testPair(getSequenceOfNumber(0, 999) + "." + getSequenceOfNumber(0, 999), "0");
        testPair(getSequenceOfNumber(0, 999) + "." + getSequenceOfNumber(0, 999), getSequenceOfNumber(0, 999) + "." + getSequenceOfNumber(0, 999));
        testPair(getSequenceOfNumber(0, 999) + "." + getSequenceOfNumber(0, 999), getSequenceOfNumber(0, 999) + "." + "0");
        testPair(getSequenceOfNumber(0, 999) + "." + getSequenceOfNumber(0, 999), "0." + getSequenceOfNumber(0, 999));
    }   
    
    @Test
    public void testSmallRoundingNumbers() {
        testPair("0.0000001", "0.000000");
        testPair("0.0000009", "0.000001");
        testPair("0.999999", "0.999999");
        testPair("0." + getSequenceOfNumber(0, 999) + "1", "0");
        testPair("0." + getSequenceOfNumber(9, 999), "1");
    }

    @Test
    public void testNegativeSmallRoundingNumbers() {
        testPair("-0.0000001", "0");
        testPair("-0.0000009", "-0.000001");
        testPair("-0.999999", "-0.999999");
        testPair("0." + getSequenceOfNumber(0, 999) + "1", "0");
        testPair("-0." + getSequenceOfNumber(9, 999), "-1");
    }
    
    
    @Test 
    public void testHalfRounding() {
        testPair("0.0000005", "0.000000");
        testPair("0.00005", "0.00005");
        testPair("0.000005", "0.000005");
        testPair("0." + getSequenceOfNumber(0, 999) + "5", "0");
        testPair("0." + getSequenceOfNumber(5, 999), "0.555556");
    }
    
    @Test 
    public void testNegativeHalfRounding() {
        testPair("-0.0000005", "0");
        testPair("-0.00005", "-0.00005");
        testPair("-0.000005", "-0.000005");
        testPair("-0." + getSequenceOfNumber(0, 999) + "5", "0");
        testPair("-0." + getSequenceOfNumber(5, 999), "-0.555556");
    }   
    @Test 
    public void testNullHandling() {
        shouldFail(() -> p.adjustPrecision(null));
    }
}
