package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;


/**
 * 
 * testcases for metadata
 * @author Constantin Dolghier
 *
 */
public class MetaInfoTests extends AmpTestCase {
    
    @Test
    public void testMetaInfoBuildingFailing() {
        shouldFail(() -> new MetaInfo(null, null));
        shouldFail(() -> new MetaInfo(null, "value"));
        shouldFail(() -> new MetaInfo("key", null));
    }

    @Test
    public void testMetaInfoComparisons() {
        MetaInfo k1v1 = new MetaInfo("key_1", "value_1");
        MetaInfo k1v1_2 = new MetaInfo("key_1", "value_1");
        MetaInfo k1v2 = new MetaInfo("key_1", "value_2");
        MetaInfo k2v1 = new MetaInfo("key_2", "value_1");
        
        assertEquals(k1v1, k1v1_2);
        assertEquals(k1v1, k1v1);
        
        assertFalse(k1v1.equals(k1v2));
        assertFalse(k1v1.equals(k2v1));
    }
    
    @Test
    public void testMetaInfoGenerator() {
        MetaInfoGenerator generator = new MetaInfoGenerator();
        MetaInfo k1v1 = generator.getMetaInfo("colour", "red");
        assertSame(k1v1, generator.getMetaInfo("colour", "red"));
        
        MetaInfo k1v2 = generator.getMetaInfo("colour", "blue");
        assertSame(k1v1, generator.getMetaInfo("colour", "red"));
        assertSame(k1v2, generator.getMetaInfo("colour", "blue"));
        
        assertFalse(k1v1.equals(k1v2));
    }
    
    @Test
    public void testMetaInfoSet() {
        MetaInfoGenerator generator = new MetaInfoGenerator();
        MetaInfoSet set = new MetaInfoSet(generator);
        set.add("length", 1);
        set.add("length", 1);
        assertEquals(set.getSize(), 1);
        set.add("length", "bla");
        set.add("length", 1);
        assertEquals(1, set.getSize());
        set.add("length", 2);
        assertEquals(1, set.getSize());
        assertEquals("(k = length, v = 2)", set.getMetaInfo("length").toString());
    }
}
