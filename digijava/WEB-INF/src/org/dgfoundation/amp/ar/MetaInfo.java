/**
 * MetaInfo.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * Class describing objects with purpose of wrapping metainformation. a
 * MetaObject is a normal Comparable object with some category text attached.
 * The category, is particularized as a String.
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 15, 2006
 * 
 */
public class MetaInfo implements Comparable {

	protected String category;

	protected Comparable value;

	/**
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return Returns the value.
	 */
	public Comparable getValue() {
		return value;
	}

	public MetaInfo(String category, Comparable value) {
		this.category = category;
		this.value = value;
	}

	/**
	 * MetaInfo objects are comparable only if the categories are equal. The
	 * comparison is based on their values.
	 * 
	 * @param o
	 *            the MetaInfo to be compared with
	 * @return the compareTo of getValue for the objects
	 */
	public int compareTo(Object o) {
		MetaInfo mo = (MetaInfo) o;
		if (getCategory().equals(mo.getCategory())) {
			return getValue().compareTo(mo.getValue());
		}
		return -1;
	}

}
