/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedMeasureCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedMeasureColumn extends TotalAmountColumn {

	/**
	 * @param worker
	 */
	public TotalComputedMeasureColumn(ColumnWorker worker) {
		super(worker);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public TotalComputedMeasureColumn(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param filterShowable
	 */
	public TotalComputedMeasureColumn(String name, boolean filterShowable) {
		super(name, filterShowable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param name
	 */
	public TotalComputedMeasureColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param source
	 */
	public TotalComputedMeasureColumn(Column source) {
		super(source);
		this.setExpression(source.getExpression());
		// TODO Auto-generated constructor stub
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
		ComputedMeasureCell uac = new ComputedMeasureCell(ac.getOwnerId());
		uac.merge(uac, ac);
		super.addCell(uac);
	}

	public Column newInstance() {
		return new TotalComputedMeasureColumn(this);
	}

	public List getTrailCells() {
		ArrayList ar = new ArrayList();
		Cell ac = new ComputedMeasureCell();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Object el = i.next();
			ComputedMeasureCell element = (ComputedMeasureCell) el;
			ac.merge(element, ac);
		}
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}

}
