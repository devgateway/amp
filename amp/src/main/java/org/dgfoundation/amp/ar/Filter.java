/**
 * Filter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 5, 2006
 * @deprecated
 */
public interface Filter {
    public void generateFilterQuery(HttpServletRequest request);
    
    public String getGeneratedFilterQuery();
}
