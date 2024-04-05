package org.dgfoundation.amp.onepager.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Ciubotaru
 */
public class PercentagesUtilTest {

    @Test
    public void test100In3() {
        assertPartsSumIsCorrect(new BigDecimal(100), 3, 0);
    }

    @Test
    public void test100In3Scale2() {
        assertPartsSumIsCorrect(new BigDecimal(100), 3, 2);
    }

    @Test
    public void test34In3Scale2() {
        assertPartsSumIsCorrect(new BigDecimal(34), 3, 2);
    }

    @Test
    public void test100In1() {
        assertPartsSumIsCorrect(new BigDecimal(100), 1, 0);
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            int scale = ThreadLocalRandom.current().nextInt(3);
            int unscaledSum = ThreadLocalRandom.current().nextInt(100 * (int) Math.pow(10, scale));
            BigDecimal sum = new BigDecimal(unscaledSum).scaleByPowerOfTen(-scale);
            int n = ThreadLocalRandom.current().nextInt(1, 10);

            assertPartsSumIsCorrect(sum, n, scale);
        }
    }

    private void assertPartsSumIsCorrect(BigDecimal sum, int n, int scale) {
        PercentagesUtil.SplitResult split = PercentagesUtil.split(sum, n, scale);
        BigDecimal verifySum = BigDecimal.ZERO;
        for (int i = 0; i < n; i++) {
            verifySum = verifySum.add(split.getValueFor(i));
        }
        assertEquals(0, verifySum.compareTo(sum));
    }

    @Test
    public void testClosestTo() {
        assertEquals(0, PercentagesUtil.closestTo((float) 14.8999996185303, 2).compareTo(new BigDecimal("14.9")));
        assertEquals(0, PercentagesUtil.closestTo((float) 85.0999984741211, 2).compareTo(new BigDecimal("85.1")));
    }
}
