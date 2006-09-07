/**
 * CellColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.exception.IncompatibleCellException;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 19, 2006 a column that holds cells
 */
public class CellColumn extends Column {

	public CellColumn(ColumnWorker worker) {
		super(worker);
	}

	public CellColumn(String name) {
		super(name);
	}

	public CellColumn(Column source) {
		super(source.getParent(),source.getName());
		// TODO Auto-generated constructor stub
	}
	
	public CellColumn(Column parent, String name) {
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	public Cell getByOwner(Long ownerId) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if(element.getOwnerId().equals(ownerId)) return element;
		}
		return null;
	}
	
	public Cell getCell(int i) {
		return (Cell) getItem(i);
	}

	public void addCell(Object cc) {
		Cell c = (Cell) cc;
		c.setColumn(this);
		items.add(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Column filterCopy(Cell metaCell, Set ids) {
		CellColumn dest = (CellColumn) this.newInstance();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			Cell destCell = element.filter(metaCell, ids);
			if (destCell != null)
				dest.addCell(destCell);
		}
		return dest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#process()
	 */
	public Column postProcess() {
		CellColumn dest = (CellColumn) this.newInstance();
		ListCell lc = new ListCell();
		Iterator i = this.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			try {
				// if we don't have items in the cell list, just add the cell
				if (lc.size() == 0)
					lc.addCell(element);
				else
				// if we have, verify if the owner of one cell in the list is
				// the same
				// if it is, add this cell to the list. if not, verify if the
				// list has
				// more than one element. if it does, add the whole list to the
				// dest column
				// if it has only one, add just that element to the dest colmn
				if (lc.getCell(0).getOwnerId().equals(element.getOwnerId()))
					lc.addCell(element);
				else {
					if (lc.size() == 1)
						dest.addCell(lc.getCell(0));
					else
						dest.addCell(lc);
					lc = new ListCell();
					lc.addCell(element);
				}
			} catch (IncompatibleCellException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		if (lc.size() == 1)
			dest.addCell(lc.getCell(0));
		if (lc.size() > 1)
			dest.addCell(lc);

		return dest;

	}

	public int getWidth() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
	 */
	public List getSubColumns(int depth) {
		ArrayList ret=new ArrayList();
		if(depth>0) return ret;
		ret.add(this);
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnSpan() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getCurrentRowSpan()
	 */
	public int getCurrentRowSpan() {
		return rowSpan;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
	 */
	public Set getOwnerIds() {
		TreeSet ret=new TreeSet();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			ret.add(element.getOwnerId());
		}
		return ret;
	}

	/**
	 * Trail Cells are by default TextCellS, in any CellColumn. Override this to add a different behaviour...
	 */
	public List getTrailCells() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#newInstance()
	 */
	public Column newInstance() {
		return new CellColumn(this);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnDepth() {
		return 1;
	}
	

	public void replaceCell(Cell oldCell, Cell newCell) {
		int idx = items.indexOf(oldCell);
		items.remove(idx);
		items.add(idx, newCell);
		newCell.setColumn(this);
	}
}


