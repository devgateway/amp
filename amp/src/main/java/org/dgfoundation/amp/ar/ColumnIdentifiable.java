/**
 * XUnique.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 * uniquely identifies the cell on a row. 
 * it is inherited from the column name (ex: "sector", "title")
 */
public interface ColumnIdentifiable {
    public Object getColumnId();
}
