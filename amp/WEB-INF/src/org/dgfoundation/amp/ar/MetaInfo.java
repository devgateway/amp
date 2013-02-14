/**
 * MetaInfo.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class describing objects with purpose of wrapping metainformation. a
 * MetaObject is a normal Comparable object with some category text attached.
 * The category, is particularized as a String.
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 15, 2006
 * 
 */
public class MetaInfo<T extends Comparable<? super T>> implements Comparable<MetaInfo<T>> , Serializable, Cloneable  {

	protected String category;

	protected T value;

	/**
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return Returns the value.
	 */
	public T getValue() {
		return value;
	}

	public MetaInfo(String category, T value) {
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
	public int compareTo(MetaInfo<T> mo) {
		if (getCategory().equals(mo.getCategory())) {
			return getValue().compareTo(mo.getValue());
		}
		return getCategory().compareTo(mo.getCategory());
	}
	
	public String toString() {
		return category+": "+value;
	}
	
	//BOZO: remove	
	static long calls = 0;
	static long iterations = 0;
	
	public static <V extends Comparable<? super V>> MetaInfo<V> getMetaInfo(Collection<MetaInfo<V>> metaData, String category) {
		Iterator<MetaInfo<V>> i = metaData.iterator();
		calls ++;
		while (i.hasNext()) {
			iterations ++;
			MetaInfo<V> element =  i.next();
			if (element == null)
				continue;
			if (element.getCategory().equals(category))
				return element;
		}
		return null;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MetaInfo){
			MetaInfo<?> theObj = (MetaInfo<?>) obj;
			if (this.category != null && this.value != null)
				return this.category.equals(theObj.category) && this.value.equals(theObj.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if(this.category == null)
			return 0;
		return this.category.hashCode();
	}

}
