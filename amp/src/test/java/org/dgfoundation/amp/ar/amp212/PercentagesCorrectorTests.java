package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

/**
 * 
 * testcases for the AMP PercentagesCorrector
 * 
 * @author Dolghier Constantin
 *
 */

public class PercentagesCorrectorTests extends AmpTestCase {
    
    final double EPSI = 0.001;

    void testCleanActivity(PercentagesCorrector.Snapshot snapshot) {
        assertEquals(56.0, snapshot.correctPercentage(1l, 56.0, 0), EPSI);
        assertEquals(100.0, snapshot.correctPercentage(1l, 100.0, 0), EPSI);
        assertEquals(100.0, snapshot.correctPercentage(1l, null, 1), EPSI);
        shouldFail(() -> snapshot.correctPercentage(1l, null, 2));
    }
    
    @Test
    public void testCleanData() {
        testCleanActivity(new PercentagesCorrector.Snapshot(Collections.emptyMap()));
    }
    
    @Test
    public void testDirtyDataSingleNull() {
        PercentagesCorrector.Snapshot snapshot = new PercentagesCorrector.Snapshot(new HashMap<Long, Double>() {{
            put(2l, 150d);
            put(3l, 200d);
        }});
        testCleanActivity(snapshot);
        assertEquals(66.6667, snapshot.correctPercentage(2l, 100d, 0), EPSI);
        assertEquals(66.6667, snapshot.correctPercentage(2l, null, 1), EPSI);
        assertEquals(33.3333, snapshot.correctPercentage(2l, 50d, 0), EPSI);
        assertEquals(50.0, snapshot.correctPercentage(2l, 75d, 0), EPSI);
        assertEquals(100.0, snapshot.correctPercentage(2l, 150d, 0), EPSI);
        assertEquals(100.0, snapshot.correctPercentage(2l, 50d, 1), EPSI);
        
        assertEquals(50d, snapshot.correctPercentage(3l, 100d, 0), EPSI);
        assertEquals(50d, snapshot.correctPercentage(3l, null, 1), EPSI);
        assertEquals(100d, snapshot.correctPercentage(3l, null, 2), EPSI);
        assertEquals(100d, snapshot.correctPercentage(3l, 200d, 0), EPSI);
        assertEquals(100d, snapshot.correctPercentage(3l, 100d, 1), EPSI);
    }
    
    @Test
    public void testEpsilonDirtyData() {
        PercentagesCorrector.Snapshot snapshot = new PercentagesCorrector.Snapshot(new HashMap<Long, Double>() {{
            put(5l, 100.000001);
        }});
        testCleanActivity(snapshot);
        assertEquals(99.99999, snapshot.correctPercentage(5l, null, 1), EPSI);
        assertEquals(99.99999, snapshot.correctPercentage(5l, 100.0, 0), EPSI);
    }
}
