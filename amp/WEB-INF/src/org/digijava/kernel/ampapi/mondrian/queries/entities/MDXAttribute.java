/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * Configuration of an Attribute based on its dimension, name and (optionally) value <br>
 * More directly useful for Mondrian 4 version
 * @author Nadejda Mandrescu
 *
 */
public class MDXAttribute extends MDXElement {
	protected String dimension;
	protected String value;
	
	/**
	 * Attribute configuration
	 * @param name
	 * @param dimension
	 */
	public MDXAttribute(String dimension, String name) {
		this(dimension, name, null); 
	}
	
	/**
	 * Attribute configuration by 'value' filter 
	 * @param name
	 * @param dimension
	 * @param value
	 */
	public MDXAttribute(String dimension, String name, String value) {
		super(name);
		this.dimension  = dimension;
		this.value = value;
	}
	
	@Override
	public MDXAttribute clone() {
		return new MDXAttribute(this.dimension, this.name, this.value);
	}
	
	@Override
	public String toString() {
		//(new IdentifierNode(new NameSegment(elem.getDimension()), new NameSegment(elem.getName()))).toString() + "." + MoConstants.MEMBERS;
		return  getFullName() + "." + (this.value==null ? MoConstants.MEMBERS : quote(this.value));
	}
	
	@Override
	public String getFullName() {
		return quote(this.dimension) + "." + quote(this.name);
	}
	
	@Override
	public boolean hasFullName() {
		return this.dimension!=null && this.name!=null;
	}
	
	public String getDimensionAndHierarchy() {
		return quote(this.dimension);
	}

	public String getCurrentMemberName() {
		return getFullName() + "." + MoConstants.CURRENT_MEMBER;
	}

	@Override
	public String getSortName() {
		if (this.value != null)
			return toString();
		return getCurrentMemberName() + "." + MoConstants.MEMBER_NAME;
	}

	/**
	 * @return the dimension
	 */
	public String getDimension() {
		return dimension;
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
