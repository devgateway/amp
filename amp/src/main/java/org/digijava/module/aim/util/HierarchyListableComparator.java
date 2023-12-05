package org.digijava.module.aim.util;

import java.util.Comparator;

public class HierarchyListableComparator implements
        Comparator<HierarchyListable> {

    @Override
    public int compare(HierarchyListable o1, HierarchyListable o2) {
        if (o1 == null && o2 == null)
            return 0;
        if (o1 == null )
            return 1;
        return o1.getLabel().compareTo( o2.getLabel() );
    }

}
