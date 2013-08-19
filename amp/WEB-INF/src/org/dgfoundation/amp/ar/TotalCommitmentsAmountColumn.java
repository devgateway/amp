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
import org.dgfoundation.amp.ar.cell.TotalCommitmentsAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * @author mihai
 *
 */
public class TotalCommitmentsAmountColumn extends TotalAmountColumn {

	/**
	 * @param worker
	 */
	public TotalCommitmentsAmountColumn(ColumnWorker worker) {
		super(worker);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public TotalCommitmentsAmountColumn(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param filterShowable
	 */
	public TotalCommitmentsAmountColumn(String name, boolean filterShowable) {
		super(name, filterShowable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param name
	 */
	public TotalCommitmentsAmountColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param source
	 */
	public TotalCommitmentsAmountColumn(Column source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Overrides the method for adding cells, to make sure we add only TotalCommitmentsAmountCellS
	 * @param c the cell to be added
	 * @see TotalCommitmentsAmountCellS
	 */
	@Override
	public void addCell(Cell c) {
		AmountCell ac=(AmountCell) c;
		TotalCommitmentsAmountCell uac=new TotalCommitmentsAmountCell(ac.getOwnerId());		
		super.addCell(uac.merge(ac));
	}

    public Column newInstance() {
		return new TotalCommitmentsAmountColumn(this);
	}

	public List<TotalCommitmentsAmountCell> getTrailCells() {
		ArrayList<TotalCommitmentsAmountCell> ar = new ArrayList<TotalCommitmentsAmountCell>();
		TotalCommitmentsAmountCell ac = new TotalCommitmentsAmountCell();		
		Iterator i=items.iterator();
		while (i.hasNext()) {
			TotalCommitmentsAmountCell element = (TotalCommitmentsAmountCell) i.next();
			ac.merge(element,ac);
		}
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}
	
}
