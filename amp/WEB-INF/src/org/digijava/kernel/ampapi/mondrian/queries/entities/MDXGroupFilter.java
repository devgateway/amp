/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores a group of MDX Filters that are part of the same hierarchy
 * 
 * @author Nadejda Mandrescu
 */
public class MDXGroupFilter {
	private Map<MDXAttribute, List<MDXFilter>> filters = new HashMap<MDXAttribute, List<MDXFilter>>();
	
	public MDXGroupFilter() {
	}
	/**
	 * @return the filters
	 */
	public Map<MDXAttribute, List<MDXFilter>> getFilters() {
		return filters;
	}
	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<MDXAttribute, List<MDXFilter>> filters) {
		this.filters = filters;
	}
}
