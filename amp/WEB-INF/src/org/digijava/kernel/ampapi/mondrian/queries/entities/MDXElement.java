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
	
	public int hashCode() {
		return this.getFullName().hashCode();
	}
	
	/**
	 * The equality is based upon element full name, without any value filters
	 */
	public boolean equals(Object o) {
		if (o!=null && MDXElement.class.isAssignableFrom(o.getClass())) {
			MDXElement mdxAttr = (MDXElement)o;
			return mdxAttr.getFullName().equals(this.getFullName());
		}
		return false;
	}
	
	public static boolean filterEquals(MDXElement e1, MDXElement e2) {
		if (e1 != null && e2 != null) {
			return e1.equals(e2) 
					|| MDXAttribute.class.isAssignableFrom(e1.getClass()) && MDXAttribute.class.isAssignableFrom(e2.getClass()) && MDXAttribute.filterEquals((MDXAttribute)e1, (MDXAttribute)e2); 
		}
		return e1 == null && e2 == null;
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
