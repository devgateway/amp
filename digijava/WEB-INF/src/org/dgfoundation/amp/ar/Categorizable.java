/**
 * Categorizable.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.Set;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006
 *
 */
public interface Categorizable {
	public Set getMetaData();
	public MetaInfo getMetaInfo(String category);
	
	public boolean hasMetaInfo(MetaInfo m);

}
