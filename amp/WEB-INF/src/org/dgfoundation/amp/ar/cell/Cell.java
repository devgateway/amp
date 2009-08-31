/**
 * Cell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Comparator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnIdentifiable;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.RowIdentifiable;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.workers.ColumnWorker;


/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 * the (table) cell a Horizontally and Vertically unique 
 * (implementing ColumnIdentifiable and RowIdentifiable)  is the most
 * basic part of our structure. The Cell has some crucial properties 
 * implemented by all the subtypes:
 */
/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Nov 13, 2006
 *
 */
public abstract class Cell extends Viewable implements RowIdentifiable, ColumnIdentifiable, Comparable,Cloneable {

	
	@Override
	public ReportData getNearestReportData() {
		return column.getNearestReportData();
	}
	
	public static class CellComparator implements Comparator { 
		public final int compare (Object o1, Object o2) {
			Cell c1=(Cell) o1;
			Cell c2=(Cell) o2;
			//AMP-6574
			if (c1 == null && c2 ==null){
				return 0;
			}
			if (c1 instanceof ComputedDateCell && c2 instanceof ComputedDateCell) {
				Double v1 = 0d, v2 = 0d;

				if (c1.getValue() != null) {
					String value = (String) c1.getValue();
					if (!value.equalsIgnoreCase("")) {
						v1 = Double.parseDouble((String) c1.getValue());
					}
				}
				if (c2.getValue() != null) {
					String value = (String) c2.getValue();
					if (!value.equalsIgnoreCase("")) {
						v2 = Double.parseDouble((String) c2.getValue());
					}
				}
				return Double.compare(v1, v2);
			}
			
	        if(c1 instanceof TextCell && c2 instanceof TextCell){
	            String c1Value=c1.comparableToken().toString();
	            String c2Value=c2.comparableToken().toString();
	           return c1Value.compareToIgnoreCase(c2Value);
	        } 
	        if(c1 instanceof AmountCell && c2 instanceof AmountCell){
	        	AmountCell ac1 = (AmountCell) c1;
	        	AmountCell ac2 = (AmountCell) c2;
	        	return Double.compare(ac1.getAmount(), ac2.getAmount());
	        }
	        logger.info("3 cell c1 instance : "+c1.getValue());
	        logger.info("4 cell c2 instance : "+c2.getValue());            
			return c1.comparableToken().toString().compareTo(c2.comparableToken().toString());
		}
	}
	
	@Override
	public int getVisibleRows() {
	    return 1;
	}

	/**
	 * Useful for seeing which cell are unique
	 * @see AmountCell.jsp
	 * @return
	 */
	public int getHashCode() {
		return this.hashCode();
	}
	
	protected Comparator sorter; 
	
	protected Long ownerId;
	protected Long id;
	protected Column column;
	
//	isShowable static duplicate, just to speed things up 
	protected boolean show;
	
	protected static Logger logger = Logger.getLogger(Cell.class);
	
	/**
	 * Returns worker class assigned for this type of Cell. The Worker class
	 * is a ColumnWorker descendant.
	 * @return the ColumnWorker class
	 * @see ColumnWorker
	 */
	public abstract Class getWorker();
	

	public int compareTo(Object o) {
		Cell c=(Cell) o;
		return ((Comparable)this.getValue()).compareTo(c.getValue());
	}
	
	public abstract Comparable comparableToken();
		
	
	public Cell() {
		show=true;
	}
	
	public Cell(Long id) {
		this.ownerId=id;
		show=true;
	}
	
	/**
	 * method used to output the value of the cell as string data, making the
	 * necessary conversions. By default it returns a string representation of the object returned 
	 * by getValue
	 */
	public String toString() {
		return getValue()!=null?getValue().toString():"";
	}
	
	/**  
	 * @return the value wrapped by this Cell. This can also be a List. This is the useful data of the cell.
	 */
	public abstract Object getValue();
	
	public abstract void setValue(Object value);

	/**
	 * returns the identifier of the row. This is by default the owner of the Cell (ownerId).
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
	 * hierarchy views are needed. Creating hierarchy views means basically creating more reports into one, thus totals are 
	 * different and Cells must be cloned.
	 * @see org.dgfoundation.amp.ar.ReportData
	 * @param metaCell
	 * @return the resulting Cell
	 */
	public Cell filter(Cell metaCell,Set ids) {
		try {
		if(ids.contains(ownerId)) return (Cell) this.clone(); else return null;
		} catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @return Returns the column.
	 */
	public Column getColumn() {
		return column;
	}
	
	/**
	 * @return the current view TYPE of the cell. A view type is a name that identifies an output type. For example a Cell
	 * can have a view of type HTML, this means that the cell will provide output that is displayable in a html file.
	 * In the same way this can be XLS, PDF, etc...   
	 */
	public String getCurrentView() {
		return column.getCurrentView();
	}
	

	/**
	 * Sets the column that is the keeper for this cell. A cell can have only one column.
	 * @param column The column to set.
	 */
	public void setColumn(Column column) {
		this.column = column;
	}
		
	/**
	 * This method is used only by subtypes that need to implement cell merging behaviour. The current cell can  be merged with
	 * another cell of the same type, thus resulting a new cell. A clear example is the AmountCells. Merging two amount cells
	 * will produce a new amount cell that will hold references to both of the cells. Thus, later, the view of that cell can perform
	 * math operations on the amounts, displaying for example the sum.
	 * @param c the cell to be merged with
	 * @return the new cell resulted out of merging
	 */
	public abstract Cell merge(Cell c);
	
	/**
	 * This merge method will perform the operation without instantiating a third Cell.
	 * It is assumed 
	 * @see merge(Cell c)
	 * @param c
	 * @param dest the destination cell - already instantiated
	 * 
	 */
	public abstract void merge(Cell c, Cell dest);
	
	/**
	 * Provides direct approach to instantiate new CellS using inheritance based constructor.
	 * @return a new instantiated Cell, with default values.
	 */
	public abstract Cell newInstance();
	
	


	/**
	 * @return Returns the show.
	 */
	public boolean isShow() {
		return show;
	}

	/**
	 * @param show The show to set.
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

	public String getObjectId() {
		return super.toString();
	}
	
}
