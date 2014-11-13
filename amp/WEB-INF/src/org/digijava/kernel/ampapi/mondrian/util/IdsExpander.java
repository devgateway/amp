package org.digijava.kernel.ampapi.mondrian.util;

import java.util.List;
import java.util.Set;

/**
 * interface used for knowing how to process (expand by walking hierarchy, etc) a filter 
 * @author Constantin Dolghier
 *
 */
public abstract class IdsExpander {
	
	/**
	 * the fact table into onto which the expander-generated SQL operates
	 */
	public final String factTableColumn;
	public abstract Set<Long> expandIds(List<Long> usedSelectedIds);
	
	public IdsExpander(String factTableColumn) {
		this.factTableColumn = factTableColumn;
	}
}
