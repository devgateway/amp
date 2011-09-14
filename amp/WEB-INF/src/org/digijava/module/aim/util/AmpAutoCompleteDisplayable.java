/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.digijava.module.aim.util;

import java.util.Collection;
import java.util.Comparator;

/**
 * Used to display tree like structures in selectors
 * @author mpostelnicu@dgateway.org
 * since Oct 22, 2010
 */
public interface AmpAutoCompleteDisplayable {
	
	public static class AmpAutoCompleteComparator implements Comparator<AmpAutoCompleteDisplayable> {
		@Override
		public int compare(AmpAutoCompleteDisplayable o1,
				AmpAutoCompleteDisplayable o2) {
			return o1.getLabel().compareTo(o2.getLabel());
		}
		
	};
	
	public AmpAutoCompleteDisplayable getParent();
	public <T extends AmpAutoCompleteDisplayable> Collection<T> getSiblings();
	public <T extends AmpAutoCompleteDisplayable> Collection<T> getVisibleSiblings();
	public String getLabel();
}
