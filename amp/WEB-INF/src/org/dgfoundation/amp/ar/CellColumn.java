/**
 * CellColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.exception.IncompatibleCellException;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 19, 2006 a column that holds cells
 */
public class CellColumn<K extends Cell> extends Column<K> {

	/**
	 * Returns the number of items in the column as the visible rows (they are already unique) so this means
	 * the number activities that have data for this column
	 */
	@Override
	public int getVisibleRows() {
		return items.size();
	}

	/**
	 * Iterator for the internal list of items
	 * 
	 * @return the Iterator
	 */
	public Iterator<K> iterator() {
		return items.iterator();
	}

	protected HashMap<Long, List<Cell>> itemsMap = new HashMap<Long, List<Cell>>();

	public CellColumn(ColumnWorker worker) {
		super(worker);
	}

	public CellColumn(String name) {
		super(name);
	}

	public CellColumn(String name, int initialCapacity) {
		super(name, initialCapacity);
		itemsMap = new HashMap<Long, List<Cell>>(initialCapacity);
	}

	public CellColumn(Column source) {
		super(source.getParent(), source.getName());
		this.contentCategory = source.getContentCategory();
		this.dimensionClass = source.getDimensionClass();
		this.relatedContentPersisterClass = source.getRelatedContentPersisterClass();
		
		this.worker=source.getWorker();
		
		this.setDescription(source.getDescription());
		this.setExpression(source.getExpression());
		this.setTotalExpression(source.getTotalExpression());
	}

	public CellColumn(Column parent, String name) {
		super(parent, name);
		this.contentCategory = parent.getContentCategory();
	}

	public Cell getByOwner(Long ownerId) {
		List<Cell> cells = itemsMap.get(ownerId);
		if (cells == null || cells.isEmpty())
			return null;
		
		return cells.get(0); // return the first one since the caller doesn't care
	}

	static long calls = 0;
	static long iterations = 0;
	
	public Cell getByOwnerAndValue(Long ownerId, Object value) {
		calls ++;
		
		List<Cell> list = itemsMap.get(ownerId);
		if (list == null)
			return null;
		
		for(Cell element:list)
		{
			iterations ++;
			if (value.equals(element.getValue()))
				return element;
		}
		
//		Iterator i = items.iterator();
//		while (i.hasNext()) {
//			iterations ++;
//			Cell element = (Cell) i.next();
//			if (element.getOwnerId().equals(ownerId)
//					&& value.equals(element.getValue()))
//				return element;
//		}
		return null;
	}

	public Cell getCell(int i) {
		return (Cell) getItem(i);
	}

	public void addCell(K c) {
		//Cell c = (Cell) cc;
		c.setColumn(this);
		if (!itemsMap.containsKey(c.getOwnerId()))
			itemsMap.put(c.getOwnerId(), new ArrayList<Cell>());
		itemsMap.get(c.getOwnerId()).add(c);
		items.add(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
	 */
	@Override
	public Column filterCopy(Cell metaCell, Set<Long> ids) {
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
	@Override
	public Column postProcess() {
		CellColumn dest = (CellColumn) this.newInstance();
		ListCell lc = new ListCell();
		Iterator i = this.iterator();
		HashMap<Long, ListCell> ownerToCells = new HashMap<Long, ListCell>();
		while (i.hasNext()) {
			try {
				Object objelement = i.next();
				// If type total column do nothing
				if (!(objelement instanceof Column)) {

					Cell element = (Cell) objelement;
					
					// if we don't have items in the cell list, just add the
					// cell
					/*if (lc.size() == 0)
						lc.addCell(element);
					else*/
					// if we have, verify if the owner of one cell in the list
					// is
					// the same
					// if it is, add this cell to the list. if not, verify if
					// the
					// list has
					// more than one element. if it does, add the whole list to
					// the
					// dest column
					// if it has only one, add just that element to the dest
					// colmn
					/*if (lc.getCell(0).getOwnerId().equals(element.getOwnerId()))
						lc.addCell(element);
					else {
						if (lc.size() == 1)
							dest.addCell(lc.getCell(0));
						else
							dest.addCell(lc);
						lc = new ListCell();
						lc.addCell(element);
					}*/
					
					ListCell listCell	= ownerToCells.get(element.getOwnerId());
					if ( listCell == null ) {
						listCell	= new ListCell();
						ownerToCells.put(element.getOwnerId(), listCell);
					}
					listCell.addCell(element);
					
				}
			} catch (IncompatibleCellException e) {
				logger.error(e);
				e.printStackTrace();
			}

		}
		Iterator<Entry<Long, ListCell>> iter	= ownerToCells.entrySet().iterator();
		while (iter.hasNext() ) {
			Entry<Long, ListCell> entry			= iter.next();
			ListCell lCell						= entry.getValue();
			if ( lCell != null && lCell.size() > 0 ) {
				if ( lCell.size() == 1 ) {
					dest.addCell( lCell.getCell(0) );
				}
				else {
					dest.addCell(lCell);
				}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
	 */
	@Override
	public List getSubColumns(int depth) {
		ArrayList ret = new ArrayList();
		if (depth > 0)
			return ret;
		ret.add(this);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	@Override
	public int getColumnSpan() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getCurrentRowSpan()
	 */
	public int getCurrentRowSpan() {
		return rowSpan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
	 */
	@Override
	public Set<Long> getOwnerIds() {
		TreeSet<Long> ret = new TreeSet<Long>();
		for(Cell element:((List<Cell>)items))
			ret.add(element.getOwnerId());
		return ret;
	}

	/**
	 * Trail Cells are by default TextCellS, in any CellColumn. Override this to
	 * add a different behaviour...
	 */
	@Override
	public List getTrailCells() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#newInstance()
	 */
	@Override
	public Column newInstance() {
		return new CellColumn(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	@Override
	public int getColumnDepth() {
		if (this.getName() != null && !this.getName().equals("")){
			return 1;	
		}else{
			return 0;
		}
		
	}

	/**
	 * will crash if oldCell does not exist in the column
	 * @param oldCell
	 * @param newCell
	 */
	public void replaceCell(K oldCell, K newCell) {
		int idx = items.indexOf(oldCell);
		items.remove(idx);
		items.add(idx, newCell);
		
		int idx2 = itemsMap.get(oldCell.getOwnerId()).indexOf(oldCell);
		itemsMap.get(oldCell.getOwnerId()).remove(idx2);
		itemsMap.get(oldCell.getOwnerId()).add(newCell);

		newCell.setColumn(this);
	}

	/*;
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#cellCount()
	 */
	@Override
	public int getCellCount() {
		return items.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Column#getVisibleCellCount()
	 */
	@Override
	public int getVisibleCellCount(Long ownerId) {
		int count = 0;
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if (element.getOwnerId().equals(ownerId)
					&& !(element.toString().trim().equals("")))
				count++;
		}
		return count;
	}

	public boolean hasTrailCells() {
		// TODO Auto-generated method stub
		return false;
	}

//	public HashMap getItemsMap() {
//		return itemsMap;
//	}
//
//	public void setItemsMap(HashMap itemsMap) {
//		this.itemsMap = itemsMap;
//	}
	
	@Override
	public boolean removeEmptyChildren(boolean checkFunding) {
		if ( this.getOwnerIds().size() > 0 ) {
			return false;
		}
		else
			return true;
	}
	
	@Override
	public Column hasSorterColumn(String namePath) {
		if ( namePath == null ) {
			logger.error("namePath param is not supposed to be null here. Something's wrong");
			return null;
		}
		if ( namePath.equals(this.getNamePath()) ) {
			return this;
		}
		else
			return null;
	}
	
	@Override
	public void filterByIds(Set<Long> idsToRetain)
	{
		Iterator<K> cells = this.iterator();
		while(cells.hasNext())
		{
			Cell cell = cells.next();
			if (!idsToRetain.contains(cell.getOwnerId()))
				cells.remove();
		}
	}
	
	@Override
	public List<Cell> getAllCells(List<Cell> src, boolean freeze)
	{
		for(Object obj:this.getItems())
		{
			src.add((Cell) obj);
			if (obj instanceof AmountCell)
				((AmountCell) obj).freeze();
		}
		return src;
	}
	
	@Override
	public void deleteByOwnerId(Set<Long> ownerId)
	{
		// pay attention when merging with AMP2.4, as the way cells are kept there has been slightly changed
		for(Long id:ownerId)
			itemsMap.remove(id);
		
		Iterator<Cell> iter = (Iterator<Cell>) items.iterator();
		while (iter.hasNext())
		{
			Cell cell = iter.next();
			if (ownerId.contains(cell.getOwnerId()))
				iter.remove();
		}
	}
}

