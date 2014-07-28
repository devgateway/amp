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
	protected Boolean sortAsc = null; //no sorting by default
	
	public MDXElement(String name, Boolean sortAsc) {
		this.name = name;
		this.sortAsc = sortAsc;
	}
	
	@Override
	public abstract MDXElement clone();
	public abstract String getFullName();
	
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

	/**
	 * @return the sortAsc
	 */
	public Boolean getSortAsc() {
		return sortAsc;
	}

	/**
	 * @param sortAsc the sortAsc to set
	 */
	public void setSortAsc(Boolean sortAsc) {
		this.sortAsc = sortAsc;
	}
}
