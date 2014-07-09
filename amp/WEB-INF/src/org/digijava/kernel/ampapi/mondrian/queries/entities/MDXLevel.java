/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.Arrays;
import java.util.List;

/**
 * Hierarchy Level configuration based on dimension, hierarchy, (optionally) level name and (optionally) slicing values 
 * @author Nadejda Mandrescu
 *
 */
public class MDXLevel extends MDXAttribute {
	private String hierarchy;
	private List<String> values; 
	
	/**
	 * Configures Level settings
	 * @param dimension 
	 * @param level
	 */
	public MDXLevel(String dimension, String level) {
		this(dimension, null, level);
	}
	
	/**
	 * Configures Level settings
	 * @param dimension 
	 * @param hierarchy
	 * @param level
	 * @param values - list of hierarchy values to filter by
	 */
	public MDXLevel(String dimension, String hierarchy, String level, String... values) {
		super(dimension, level, null);
		this.hierarchy = hierarchy;
		this.values = values.length > 0 ? Arrays.asList(values) : null;
	}
	
	@Override
	public MDXLevel clone() {
		return new MDXLevel(this.dimension, this.hierarchy, this.name, this.values.toArray(new String[this.values.size()]));
	}
	
	@Override
	public String toString() {
		return getFullName() + valuesToKey(); 
	}
	
	private String valuesToKey() {
		String res = "";
		if (values!=null && values.size()>0 ) {
			res = ".";
			if (values.size()==1) 
				res += quote(values.get(0));
			else 
				for (String val:values) {
					res += "&" + quote(val);
				}
		}
		return res;
	}
	
	@Override
	public String getFullName() {
		return quote(this.dimension) + (this.hierarchy==null ? "" : "." + quote(this.hierarchy)) + (this.name==null? "" : "." + quote(this.name));
	}
	
	public String getLevel() {
		return this.name;
	}

	/**
	 * @return the hierarchy
	 */
	public String getHierarchy() {
		return this.hierarchy;
	}

	/**
	 * @param hierarchy the hierarchy to set
	 */
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
}
