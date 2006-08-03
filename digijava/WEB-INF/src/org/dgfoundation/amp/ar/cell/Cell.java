/**
 * Cell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Set;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnIdentifiable;
import org.dgfoundation.amp.ar.RowIdentifiable;
import org.dgfoundation.amp.ar.Viewable;


/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 * the (table) cell a Horizontally and Vertically unique 
 * (implemending ColumnIdentifiable and RowIdentifiable)  is the most
 * basic part of our structure. The Cell has some crucial properties 
 * implemented by all the subtypes:
 */
public abstract class Cell extends Viewable implements RowIdentifiable, ColumnIdentifiable, Comparable {

	protected Long ownerId;
	protected Long id;
	protected Column column;
	
	/**
	 * Returns worker class assigned for this type of Cell. The Worker class
	 * is a ColumnWorker descendant.
	 * @return
	 */
	public abstract Class getWorker();
	
	public int compareTo(Object o) {
		Cell c=(Cell) o;
		return ((Comparable)this.getValue()).compareTo(c.getValue());
	}
	
	
	public Cell() {
		
	}
	
	public Cell(Long id) {
		this.ownerId=id;
	}
	
	/**
	 * method used to output the value of the cell as string data, making the
	 * necessary conversions
	 */
	public String toString() {
		return getValue()!=null?getValue().toString():"";
	}
	
	/**  
	 * @return the value wrapped by this Cell. This can also be a List.
	 */
	public abstract Object getValue();
	
	public abstract void setValue(Object value);

	/**
	 * returns the identifier of the row (owner id)
	 */
	public Object getRowId() {
		return ownerId;
	}
	
	public Object getColumnId() {
		return column.getColumnId();
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId The ownerId to set.
	 */
	public void setOwnerId(Long id) {
		this.ownerId = id;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	

	/**
	 * Hierarchy filtering method that in most cases returns the Cell object itself.
	 * It is used to create the destination Cell in step 2 (data processing) when
	 * hierarchy views are needed.
	 * @see org.dgfoundation.amp.ar.ReportData
	 * @param metaCell
	 * @return
	 */
	public Cell filter(Cell metaCell,Set ids) {
		if(ids.contains(ownerId)) return this; else return null;
	}


	/**
	 * @return Returns the column.
	 */
	public Column getColumn() {
		return column;
	}
	
	public String getCurrentView() {
		return column.getCurrentView();
	}
	

	/**
	 * @param column The column to set.
	 */
	public void setColumn(Column column) {
		this.column = column;
	}
		
	
	public abstract Cell merge(Cell c);
	
}
