/**
 * Filter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 *
 */
public interface Filter {
	public void generateFilterQuery();
	
	public String getGeneratedFilterQuery();
}
