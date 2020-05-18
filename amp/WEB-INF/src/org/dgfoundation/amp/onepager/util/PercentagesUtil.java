package org.dgfoundation.amp.onepager.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Octavian Ciubotaru
 */
public class PercentagesUtil {

    public static class SplitResult {

        private final BigDecimal val1;

        private final int nVal1;

        private final BigDecimal val2;

        public SplitResult(BigDecimal val1, int nVal1, BigDecimal val2) {
            this.val1 = val1;
            this.nVal1 = nVal1;
            this.val2 = val2;
        }

        public BigDecimal getValueFor(int i) {
            return i < nVal1 ? val1 : val2;
        }
    }

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * Splits sum into n parts such that sum of all parts is equal to the original sum.
     * <p>For example splitting 100 with scale 2 in 3 parts will result in: 33.33, 33.33 and 33.34.</p>
     *
     * @param sum the amount to be split
     * @param n number of parts to split into
     * @param scale the number of fractional digits to use for each part
     * @throws ArithmeticException when scale of the sum is greater than the scale for the requested parts
     *                             when n is 0 or negative
     *                             when sum is negative or greater than 100
     * @return split result can be used to find out value of each part
     */
    public static SplitResult split(BigDecimal sum, int n, int scale) {
        if (sum.scale() > scale) {
            throw new ArithmeticException("Scale of the sum is greater than scale of the requested parts");
        }
        if (sum.compareTo(BigDecimal.ZERO) < 0 || sum.compareTo(ONE_HUNDRED) > 0) {
            throw new ArithmeticException(
                    "Designed to work with percentages. Only values between 0 and 100 are allowed.");
        }
        if (n <= 0) {
            throw new ArithmeticException("Number of parts must be greater than zero.");
        }

        int intSum = sum.scaleByPowerOfTen(scale).intValueExact();

        int alloc = intSum / n;
        int dif = intSum - alloc * n;

        return new SplitResult(
                new BigDecimal(alloc).scaleByPowerOfTen(-scale),
                n - dif,
                dif > 0 ? new BigDecimal(alloc + 1).scaleByPowerOfTen(-scale) : null);
    }

    /**
     * Returns a BigDecimal with a specific scale that is closest to specified float value.
     * <p>This is needed because percentages in AmpActivityProgram are mapped with float type. Can remove this code
     * once percentages are migrated to BigDecimal as well.</p>
     *
     * @param val   float value
     * @param scale number of decimal digits in the fractional part for the returned BigDecimal
     * @return float value converted to BigDecimal with the specified scale
     */
    public static BigDecimal closestTo(float val, int scale) {
        return new BigDecimal(val).setScale(scale, RoundingMode.HALF_EVEN);
    }
}
