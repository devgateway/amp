package org.digijava.kernel.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineVersionTest {

    @Test(expected = NullPointerException.class)
    public void testCompareNull() {
        AmpOfflineVersion v1 = new AmpOfflineVersion("1.0.0");
        v1.compareTo(null);
    }

    @Test
    public void testCompareEqual() {
        AmpOfflineVersion v1 = new AmpOfflineVersion("1.0.0");
        AmpOfflineVersion v2 = new AmpOfflineVersion("1.0.0");
        assertEquals(0, v1.compareTo(v2));
        assertEquals(0, v2.compareTo(v1));
    }

    @Test
    public void testCompareMajorIncreased() {
        assertGreater(new AmpOfflineVersion("2.0.0"), new AmpOfflineVersion("1.0.0"));
    }

    @Test
    public void testCompareMinorIncreased() {
        assertGreater(new AmpOfflineVersion("1.1.0"), new AmpOfflineVersion("1.0.0"));
    }

    @Test
    public void testComparePatchIncreased() {
        assertGreater(new AmpOfflineVersion("1.0.1"), new AmpOfflineVersion("1.0.0"));
    }

    @Test
    public void testCompareOneSuffix() {
        assertGreater(new AmpOfflineVersion("1.0.0"), new AmpOfflineVersion("1.0.0-alpha"));
    }

    @Test
    public void testCompareSuffix() {
        assertGreater(new AmpOfflineVersion("1.0.0-beta"), new AmpOfflineVersion("1.0.0-alpha"));
    }

    @Test
    public void testCompareSuffixCaseInsensitiveEqual() {
        assertEquals(0, new AmpOfflineVersion("1.0.0-beta").compareTo(new AmpOfflineVersion("1.0.0-BETA")));
    }

    @Test
    public void testCompareSuffixCaseInsensitiveNotEqual() {
        assertGreater(new AmpOfflineVersion("1.0.0-beta"), new AmpOfflineVersion("1.0.0-ALPHA"));
        assertGreater(new AmpOfflineVersion("1.0.0-BETA"), new AmpOfflineVersion("1.0.0-alpha"));
    }

    private <T extends Comparable<T>> void assertGreater(T o1, T o2) {
        assertEquals(1, o1.compareTo(o2));
        assertEquals(-1, o2.compareTo(o1));
    }
}
