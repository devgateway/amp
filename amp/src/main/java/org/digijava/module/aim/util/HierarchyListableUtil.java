package org.digijava.module.aim.util;

import java.util.Collection;

import org.digijava.module.aim.util.time.StopWatch;
import org.hibernate.LazyInitializationException;

public class HierarchyListableUtil {

    public static void changeTranslateable(HierarchyListable hl, boolean translatable) {
        if ( hl != null ) {
            hl.setTranslateable(translatable);
            HierarchyListableUtil.changeTranslateable(hl.getChildren(), translatable);
        }
    }
    
    public static void changeTranslateable(Collection<? extends HierarchyListable> colHL, boolean translatable) 
        throws LazyInitializationException {
        
        if ( colHL != null && colHL.size() > 0 ) {
            for (HierarchyListable hl : colHL) {
                hl.setTranslateable(translatable);
                Collection<? extends HierarchyListable> children = hl.getChildren();
                HierarchyListableUtil.changeTranslateable(children, translatable);
            }
        }
    }
    
}
