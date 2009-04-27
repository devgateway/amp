/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.UndisbursedAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedAmountColumn extends TotalAmountColumn {

	/**
	 * @param worker
	 */
	public TotalComputedAmountColumn(ColumnWorker worker) {
		super(worker);

	}

	public TotalComputedAmountColumn(String name, boolean filterShowable, int initialCapacity) {
		super(name, filterShowable, initialCapacity);
	}

	/**
	 * @param name
	 */
	public TotalComputedAmountColumn(String name) {
		super(name);

	}

	/**
	 * @param name
	 * @param filterShowable
	 */
	public TotalComputedAmountColumn(String name, boolean filterShowable) {
		super(name, filterShowable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param name
	 */
	public TotalComputedAmountColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param source
	 */
	public TotalComputedAmountColumn(Column source) {
		super(source);

	}

	/**
	 * Overrides the method for adding cells, to make sure we add only
	 * UndisbursedAmountCellS
	 * 
	 * @param c
	 *            the cell to be added
	 * @see UndisbursedAmountCell
	 */
	public void addCell(Object c) {
		AmountCell ac = (AmountCell) c;
		ComputedAmountCell uac = new ComputedAmountCell(ac.getOwnerId());
		uac.merge(uac, ac);
		uac.setColumn(this);
		super.addCell(uac);
	}

	public Column newInstance() {
		return new TotalComputedAmountColumn(this);
	}

	public List getTrailCells() {
		ArrayList ar = new ArrayList();
		Cell ac = new ComputedAmountCell();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Object el = i.next();
			CategAmountCell element = (CategAmountCell) el;
			ac.merge(element, ac);
		}
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}

}
