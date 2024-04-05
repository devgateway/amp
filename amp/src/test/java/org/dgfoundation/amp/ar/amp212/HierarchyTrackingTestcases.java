package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.schema.ConstantNiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;


/**
 * 
 * testcases for metadata
 * @author Constantin Dolghier
 *
 */
public class HierarchyTrackingTestcases extends AmpTestCase {
    
    NiDimension twoDigits = new ConstantNiDimension("twoDigits", 2, Arrays.asList(
        Arrays.asList(1l, 11l),
        Arrays.asList(1l, 12l),
        Arrays.asList(2l, 21l),
        Arrays.asList(2l, 22l),
        Arrays.asList(3l, -3l)
    ));

    NiDimension threeDigits = new ConstantNiDimension("threeDigits", 3, Arrays.asList(
        Arrays.asList(5l, 55l, 557l),
        Arrays.asList(5l, 56l, 567l),
        Arrays.asList(5l, 56l, 568l),
        Arrays.asList(6l, 65l, 657l),
        Arrays.asList(6l, 66l, 667l),
        Arrays.asList(7l, -7l, -7l)
    ));

    NiDimensionUsage twoDigitsA = twoDigits.getDimensionUsage("a");
    NiDimensionUsage twoDigitsB = twoDigits.getDimensionUsage("b");
    
    NiDimensionUsage threeDigitsX = threeDigits.getDimensionUsage("x");
    NiDimensionUsage threeDigitsY = threeDigits.getDimensionUsage("y"); 
    
    HierarchiesTracker PER_ITEM_EMPTY = HierarchiesTracker.buildEmpty(new CacheHitsCounter());
    BigDecimal ONE = BigDecimal.ONE;
    BigDecimal ZERO = BigDecimal.ZERO;
    BigDecimal HALF = fraction(2);
    BigDecimal QUARTER = fraction(4);
    
    BigDecimal fraction(int d) {
        return ONE.divide(BigDecimal.valueOf(d));
    }
    
    @Test
    public void testPerItemImmutability() {
        HierarchiesTracker t1 = PER_ITEM_EMPTY;
        HierarchiesTracker t2 = t1.advanceHierarchy(twoDigitsA.getLevelColumn(1), 11, BigDecimal.ONE);
        assertTrue(t1 != t2);
    }
    
    @Test
    public void testEmptyFiltering() {
        // test the way "no hiers" is filtering
        HierarchiesTracker t1 = PER_ITEM_EMPTY;
        assertBigDecimalEquals(t1.calculatePercentage(yes()), BigDecimal.ONE); // we do no filtering
        assertBigDecimalEquals(t1.calculatePercentage(no()), BigDecimal.ONE); // we do no filtering
    }
    
    @Test
    public void testPercentagesCalculations() {
        HierarchiesTracker z1 = PER_ITEM_EMPTY.advanceHierarchy(twoDigitsA.getLevelColumn(1), 11, HALF);
        
        assertBigDecimalEquals(HALF, z1.calculatePercentage(yes()));
        assertBigDecimalEquals(ONE, z1.calculatePercentage(no()));
        
        HierarchiesTracker z2 = z1.advanceHierarchy(twoDigitsB.getLevelColumn(1), 11, fraction(5));
        assertBigDecimalEquals(fraction(10), z2.calculatePercentage(yes()));
        assertBigDecimalEquals(ONE, z2.calculatePercentage(no()));
        assertBigDecimalEquals(HALF, z2.calculatePercentage(z -> z == twoDigitsA));
        assertBigDecimalEquals(fraction(5), z2.calculatePercentage(z -> z == twoDigitsB));
    }
    
    @Test
    public void testEmbeddedDimensionWalking() {
        HierarchiesTracker z = PER_ITEM_EMPTY.advanceHierarchy(twoDigitsA.getLevelColumn(0), 1, fraction(2)); // twoA: (0, 0.5)
        assertBigDecimalEquals(fraction(2), z.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(2), z.calculatePercentage(null));
        
        z = z.advanceHierarchy(twoDigitsA.getLevelColumn(1), 12, fraction(5)); // twoA: (1, 0.2)
        assertBigDecimalEquals(fraction(5), z.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(5), z.calculatePercentage(null));
        
        HierarchiesTracker q = z.advanceHierarchy(twoDigitsB.getLevelColumn(0), 1, fraction(2)); // twoA: (1, 0.2), twoB: (0, 0.5)
        assertBigDecimalEquals(fraction(10), q.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(10), q.calculatePercentage(null));
        
        q = q.advanceHierarchy(twoDigitsB.getLevelColumn(1), 21, fraction(5)); // twoA: (1, 0.2), twoB: (1, 0.2)
        
        assertBigDecimalEquals(fraction(5), q.calculatePercentage(dim -> dim == twoDigitsA));
        assertBigDecimalEquals(fraction(5), q.calculatePercentage(dim -> dim == twoDigitsB));
        assertBigDecimalEquals(fraction(25), q.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(25), q.calculatePercentage(null));
    }
    
    @Test
    public void testOuterDimensionWalking() {
        HierarchiesTracker q = PER_ITEM_EMPTY.advanceHierarchy(twoDigitsA.getLevelColumn(1), 12, fraction(5));
        q = q.advanceHierarchy(twoDigitsA.getLevelColumn(0), 1, fraction(2)); // should be ignored
        assertBigDecimalEquals(fraction(5), q.calculatePercentage(dim -> dim == twoDigitsA));
        assertBigDecimalEquals(fraction(5), q.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(5), q.calculatePercentage(null));
        assertBigDecimalEquals(ONE, q.calculatePercentage(no()));
        
        HierarchiesTracker z = q.advanceHierarchy(twoDigitsA.getLevelColumn(0), 1, fraction(2)); // should be ignored
        z = z.advanceHierarchy(threeDigitsX.getLevelColumn(0), 6, fraction(2));
        assertBigDecimalEquals(fraction(10), z.calculatePercentage(yes()));
        assertBigDecimalEquals(fraction(10), z.calculatePercentage(null));
        assertBigDecimalEquals(ONE, z.calculatePercentage(no()));
        assertBigDecimalEquals(fraction(5), z.calculatePercentage(dimUsg -> dimUsg == twoDigitsA));
        assertBigDecimalEquals(ONE, z.calculatePercentage(dimUsg -> dimUsg == twoDigitsB));
        assertBigDecimalEquals(fraction(2), z.calculatePercentage(dimUsg -> dimUsg == threeDigitsX));
        assertBigDecimalEquals(ONE, z.calculatePercentage(dimUsg -> dimUsg == threeDigitsY));
    }
}
