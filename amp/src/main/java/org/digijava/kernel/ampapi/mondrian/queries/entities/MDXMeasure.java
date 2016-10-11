/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import org.digijava.kernel.ampapi.mondrian.util.MoConstants;


/**
 * Measure configuration for MDX query based on measure name
 * @author Nadejda Mandrescu
 *
 */
public class MDXMeasure extends MDXElement {
	/**
	 * Measure MDX Element
	 * @param name
	 */
	public MDXMeasure(String name) {
		super(name);
	}
	
	@Override
	public MDXMeasure clone() {
		return new MDXMeasure(name);
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
	@Override
	public String getFullName() {
		return MoConstants.MEASURES + "." + super.toString();
	}
	
	@Override
	public String getSortName() {
		return getFullName();
	}
}
