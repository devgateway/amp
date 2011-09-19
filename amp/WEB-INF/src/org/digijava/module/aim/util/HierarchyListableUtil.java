package org.digijava.module.aim.util;

import java.util.Collection;

import org.hibernate.LazyInitializationException;

public class HierarchyListableUtil {

	public static void changeTranslateable(HierarchyListable hl, boolean translateable) {
		if ( hl != null ) {
			hl.setTranslateable(translateable);
			HierarchyListableUtil.changeTranslateable(hl.getChildren(), translateable);
		}
	}

	public static void changeTranslateable(Collection<? extends HierarchyListable> colHL, boolean translateable) 
		throws LazyInitializationException {
		
		if ( colHL != null && colHL.size() > 0 ) {
			for (HierarchyListable hl : colHL) {
				hl.setTranslateable(translateable);
				HierarchyListableUtil.changeTranslateable(hl.getChildren(), translateable);
			}
		}
	}
	
}
