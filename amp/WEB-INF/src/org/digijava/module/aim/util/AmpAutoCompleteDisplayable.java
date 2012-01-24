/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.digijava.module.aim.util;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;

/**
 * Used to display tree like structures in selectors
 * @author mpostelnicu@dgateway.org
 * since Oct 22, 2010
 */
public interface AmpAutoCompleteDisplayable {
	
	public static class AmpAutoCompleteComparator implements Comparator<AmpAutoCompleteDisplayable> {
		Collator collator;
		public AmpAutoCompleteComparator() {
			collator=Collator.getInstance();
			collator.setStrength(Collator.PRIMARY);
		}
		
		@Override
		public int compare(AmpAutoCompleteDisplayable o1,
				AmpAutoCompleteDisplayable o2) {
			return collator.compare(o1.getAutoCompleteLabel(),o2.getAutoCompleteLabel());
		}
		
	};
	
	public AmpAutoCompleteDisplayable getParent();
	public <T extends AmpAutoCompleteDisplayable> Collection<T> getSiblings();
	public <T extends AmpAutoCompleteDisplayable> Collection<T> getVisibleSiblings();
	public String getAutoCompleteLabel();
}
