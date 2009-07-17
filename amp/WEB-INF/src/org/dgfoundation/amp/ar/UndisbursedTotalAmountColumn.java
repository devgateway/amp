/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.UndisbursedAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * @author mihai
 *
 */
public class UndisbursedTotalAmountColumn extends TotalAmountColumn {

	/**
	 * @param worker
	 */
	public UndisbursedTotalAmountColumn(ColumnWorker worker) {
		super(worker);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public UndisbursedTotalAmountColumn(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param filterShowable
	 */
	public UndisbursedTotalAmountColumn(String name, boolean filterShowable) {
		super(name, filterShowable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param name
	 */
	public UndisbursedTotalAmountColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param source
	 */
	public UndisbursedTotalAmountColumn(Column source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Overrides the method for adding cells, to make sure we add only UndisbursedAmountCellS
	 * @param c the cell to be added
	 * @see UndisbursedAmountCell
	 */
	public void addCell(Object c) {
		AmountCell ac=(AmountCell) c;
		UndisbursedAmountCell uac=new UndisbursedAmountCell(ac.getOwnerId());		
		uac.merge(uac,ac);
		super.addCell(uac);
	}

    public Column newInstance() {
		return new UndisbursedTotalAmountColumn(this);
	}

	public List getTrailCells() {
		ArrayList ar=new ArrayList();
		Cell ac=new UndisbursedAmountCell();		
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Object el = i.next();
			UndisbursedAmountCell element = (UndisbursedAmountCell) el;
			ac.merge(element,ac);			
		}
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}
	
}
