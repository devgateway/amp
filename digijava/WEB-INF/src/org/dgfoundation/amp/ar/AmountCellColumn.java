/**
 * AmountCellColumn.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 1, 2006
 *
 */
public class AmountCellColumn extends CellColumn {

	/**
	 * @param worker
	 */
	public AmountCellColumn(ColumnWorker worker) {
		super(worker);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public AmountCellColumn(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param source
	 */
	public AmountCellColumn(Column source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param name
	 */
	public AmountCellColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	public List getTrailCells() {
		ArrayList ar=new ArrayList();
		Cell ac=new AmountCell();		
		Iterator i=items.iterator();
		while (i.hasNext()) {
			AmountCell element = (AmountCell) i.next();
			ac=ac.merge(element);			
		}
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#newInstance()
	 */
	public Column newInstance() {
		return new AmountCellColumn(this);
	}
	
}
