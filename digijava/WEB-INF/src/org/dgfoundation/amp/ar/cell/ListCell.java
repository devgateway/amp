/**
 * ListCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.exception.IncompatibleCellException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 *
 */
public class ListCell extends Cell {

	private static Logger logger = Logger.getLogger(ListCell.class);
	
	private static MetaInfo []views=new MetaInfo[] {
		new MetaInfo(GenericViews.HTML,"ListCell.jsp")
		};
	
	protected List value;
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Cell#toString()
	 */

	
	public ListCell() {
		super();
		value=new ArrayList();
	}
	

	public ListCell(ListCell src) {
		super();
		value=new ArrayList();
		value.addAll((Collection) src.getValue());
	}
	
	
	public Class getWorker() {
		if (value.size()>0) return getCell(0).getWorker();
		return null;
	}
	
	
	public Cell getCell(int indexPos) {
		if(indexPos>=value.size()) return null;
		return (Cell)value.get(indexPos);
	}
	
	public void addCell(Cell c) throws IncompatibleCellException {
		if (ownerId==null) ownerId=c.getOwnerId();
		if(c==null) return;
		if(value.size()>0 && !getCell(0).getOwnerId().equals(c.getOwnerId())) 
			throw new IncompatibleCellException("Group Cells must hold items with identical Ids!");
		value.add(c);
	}
	
	public void addCells(Collection cells) throws IncompatibleCellException {
		Iterator i=cells.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			addCell(element);
		}
	}
	
	public ListCell(Long id) {
		super(id);
	}

	
	public String toString() {
		return value.toString();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Cell#getValue()
	 */
	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Cell#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		// TODO Auto-generated method stub

	}

	public int size() {
		return value.size();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
	 */
	protected MetaInfo[] getViewArray() {
		return views;
	}
	
	public Cell filter(Cell metaCell,Set ids) {
		if(!ids.contains(id)) return null;
		ListCell ret=new ListCell();
		Iterator i=value.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			try {
				ret.addCell(element.filter(metaCell,ids));
			} catch (IncompatibleCellException e) {
				logger.error(e);
				e.printStackTrace();
			}
			
		}
		return ret;
	}


	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#add(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Cell merge(Cell c) {
		ListCell ret=new ListCell();
		try {
			ret.addCells((Collection) this.getValue());
			ret.addCells((Collection) c.getValue());
		} catch (IncompatibleCellException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return ret;
	}
	
}
