/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

/**
 * Basic Schema element 
 * @author Nadejda Mandrescu
 *
 */
public abstract class MDXElement implements Cloneable {
	protected String name = null;
	
	public MDXElement(String name) {
		this.name = name;
	}
	
	@Override
	public abstract MDXElement clone();
	/**
	 * @return full element name, e.g. [dimension.hierarchy].[level]
	 */
	public abstract String getFullName();
	public abstract String getSortName();
	
	@Override
	public String toString() {
		return quote(name);
	}
	
	public static String quote(String val) {
		return "[" + val + "]" + (val.indexOf(']')==-1 ? "":"]"); //if val contains ], then we should double quote
	}
	
	public boolean hasFullName() {
		return this.name!=null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
