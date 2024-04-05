package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.MetaInfoSet;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

public class MiscReportsTests extends ReportsTestCase {

    @Test
    public void testMetaInfoSet()
    {
        MetaInfoSet mis = new MetaInfoSet();
        mis.add(new MetaInfo<String>("cat", "value_1"));
        assertTrue(mis.hasMetaInfo("cat"));
        assertFalse(mis.hasMetaInfo("cat_2"));
        assertEquals("value_1", mis.getMetaInfo("cat").getValue());
        mis.add(new MetaInfo<String>("cat", "value_2"));
        assertEquals("value_2", mis.getMetaInfo("cat").getValue());
        assertEquals(1, mis.size());
    }

    @Test
    public void testMetaInfoInHashes()
    {
        MetaInfo<String> mi11 = new MetaInfo<String>("cat_1", "val_1");
        MetaInfo<String> mi12 = new MetaInfo<String>("cat_1", "val_2");
        MetaInfo<String> mi21 = new MetaInfo<String>("cat_2", "val_1");
        MetaInfo<String> mi22 = new MetaInfo<String>("cat_2", "val_2");
        
        assertFalse(mi11.equals(mi12));
        assertFalse(mi11.equals(mi21));
        assertFalse(mi11.equals(mi22));
        
        assertFalse(mi12.equals(mi11));
        assertFalse(mi12.equals(mi21));
        assertFalse(mi12.equals(mi22));
        
        assertFalse(mi21.equals(mi11));
        assertFalse(mi21.equals(mi12));
        assertFalse(mi21.equals(mi22));

        assertFalse(mi22.equals(mi11));
        assertFalse(mi22.equals(mi12));
        assertFalse(mi22.equals(mi21));

        HashSet<MetaInfo> set = new HashSet<MetaInfo>();
        set.add(mi11);
        set.add(mi12);
        set.add(mi21);
        set.add(mi22);
        
        assertEquals(4, set.size());
        
        HashMap<MetaInfo, MetaInfo> map = new HashMap<MetaInfo, MetaInfo>();
        map.put(mi11, mi11);
        map.put(mi12, mi12);
        map.put(mi21, mi21);
        map.put(mi22, mi22);
        checkMapEquality(map, mi11);
        checkMapEquality(map, mi12);
        checkMapEquality(map, mi21);
        checkMapEquality(map, mi22);
    }
    
    protected void checkMapEquality(HashMap<MetaInfo, MetaInfo> map, MetaInfo<String> val)
    {
        MetaInfo<String> newMetaInfo = new MetaInfo<String>(val.getCategory(), val.getValue());
        assertTrue(map.get(newMetaInfo) == val);
    }
    
}
