/**
 * ReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 *
 */
public abstract class ReportData extends Viewable {
	
	protected static Logger logger = Logger.getLogger(ReportData.class);
	
	protected String name;
	
	protected List trailCells;

	protected List items;
	
	protected ReportData parent;

	public abstract Integer getSourceColsCount();
	
	/**
	 * @return Returns the parent.
	 */
	public ReportData getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(ReportData parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}

	public Iterator iterator() {
		return items.iterator();
	}
		
	public Object getItem(int idx) {
		return items.get(idx);
	}
	
	/**
	 * Hierarchy generator. This method splits horizontally a report into subReprots,
	 * based on categories (hierarchies). Descendants will support this for any type
	 * of ReportData object (nested or plain). 
	 * @param columnName
	 * @return
	 * @throws UnidentifiedItemException
	 * @throws IncompatibleColumnException
	 */
	public abstract GroupReportData horizSplitByCateg(String columnName)  throws UnidentifiedItemException,IncompatibleColumnException;
	
	public abstract void postProcess();
		
	public ReportData(String name) {
		this.name=name;
		items=new ArrayList();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

		
	public List getTrailCells() {
		return trailCells;
	}
	
	public List getTrailRow() {
		ArrayList ret=new ArrayList();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getTrailCells());
		}
		return ret;
	}
	
	public abstract int getTotalDepth();
	
	
	
}