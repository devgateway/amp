/**
 * SortedString.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Nov 22, 2006
 * Implements customized ordering for sortable Collections like TreeSetS.
 */
public abstract class SortedString implements Comparable {
	public String string;
	
	public SortedString(String string) {
		this.string=string;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {	
		return this.getOrder()-((SortedString)o).getOrder();
	}

	public abstract int getOrder();
	
	public String toString() {
		return string;
	}
	
}
