/**
 * Column.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * Wraps the items that can be displayed in a report column. A Column can hold
 * CellS,other ColumnS or some more exotic structures that are yet to be defined
 * ...
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 31, 2006
 * 
 */
public abstract class Column<K> extends Viewable implements ColumnIdentifiable {
	
	private String expression;
	private String totalExpression;
	public String getTotalExpression() {
		return totalExpression;
	}

	public void setTotalExpression(String totalExpression) {
		this.totalExpression = totalExpression;
	}

	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected int maxNameDisplayLength=0;
	
	protected int spanCount = 0;

	protected List<K> items;

	protected Viewable parent;

	protected int rowSpan;
	
	/**
	 * The hibernate bean that holds information relative to this cell. This cell is a usually a subset of the information that
	 * the content persister class holds.
	 */
	protected Class relatedContentPersisterClass;
	
	/**
	 * This class controls the behaviour of dimensions. a dimension is cross hierarchy linking between objects. For example
	 * Sector and Sub-Sector share the same dimension, thus they are subject of filtering while in nested hierarchy
	 */
	protected Class dimensionClass;
	
	protected String contentCategory;
	
	protected int currentDepth = 0;

	protected String name;

	public abstract int getCurrentRowSpan();

	public String toString() {
		return name + " (" + items.size() + " items)";
	}

	protected static Logger logger = Logger.getLogger(Column.class);

	protected ColumnWorker worker;

	public abstract int getWidth();

	public abstract Column postProcess();

	public abstract Column filterCopy(Cell filter, Set<Long> ids);
	
	public abstract boolean removeEmptyChildren(boolean checkFunding);

	/**
	 * Iterator for the internal list of items
	 * 
	 * @return the Iterator
	 */
	public Iterator<K> iterator() {
		return items.iterator();
	}

	/**
	 * returns the identifier for this column
	 */
	public String getColumnId() {
		return name;
	}

	/**
	 * gets the last Cell added to the list
	 * 
	 * @return the Cell or null if unavailable
	 */
	public Object getLastItem() {
		if (items.size() > 0)
			return getItem(items.size() - 1);
		else
			return null;
	}

	
	
	/**
	 * Returns true for ColumnS that can generate trail cells
	 * @return true if the column can generate trail cells
	 */
	public abstract boolean hasTrailCells();
	
	/**
	 * Gets the name of the current Column based on the hideContent parameter and hasTrailCells property
	 * @param hideContent true if we are hiding content rows (summary report)
	 * @return the Column name or "-"
	 */
	public String getName(Boolean hideContent) {
		if(hasTrailCells() || hideContent==null || !hideContent.booleanValue()) return getName(); 
		return "-";
	}
	
	/**
	 * returns the cell with the specified list position
	 * 
	 * @param indexPos
	 *            the index position in the intenal items list
	 * @return the Cell, or null if unavailable
	 * @see org.dgfoundation.amp.ar.cell.Cell
	 */
	public K getItem(int indexPos) {
		if (indexPos > items.size() - 1)
			return null;

		return items.get(indexPos);
	}

	/**
	 * Constructs a column from a given
	 * 
	 * @param parent
	 * @param name
	 */
	public Column(Viewable parent, String name) {
		super();
		this.name = name;
		items = new ArrayList<K>();
		this.parent = parent;
	}

	public Column(ColumnWorker worker) {
		super();
		this.worker = worker;
		items = new ArrayList<K>();
	}

	public Column(String name) {
		super();
		items = new ArrayList<K>();
		this.name = name;
	}

	public Column(String name,int initialCapacity) {
		super();
		items = new ArrayList<K>(initialCapacity);
		this.name = name;
	}

	
	public Column() {
		super();
		items = new ArrayList<K>();
	}

	/**
	 * @return Returns the worker.
	 */
	public ColumnWorker getWorker() {
		return worker;
	}

	/**
	 * @param worker
	 *            The worker to set.
	 */
	public void setWorker(ColumnWorker worker) {
		this.worker = worker;
	}

	/**
	 * @return Returns the parent.
	 */
	public Viewable getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(Column parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		if(this.getMaxNameDisplayLength()==0) return name;
		if(name.length()<this.maxNameDisplayLength)
		return name; else 
		return name.substring(0,maxNameDisplayLength);
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the items.
	 */
	public List<K> getItems() {
		return items;
	}

	/**
	 * @param parentReport
	 *            The parentReport to set.
	 */

	public String getCurrentView() {
	    try { 
		return parent.getCurrentView();
	    } catch (Exception e) {
		e.printStackTrace();
		return null;
	    }
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(Viewable parent) {
		this.parent = parent;
	}

	public abstract int getColumnSpan();

	/**
	 * returns a list of ColumnS that can be found at the specified depth level.
	 * 
	 * @param depth
	 *            the depth level from where we want subcolumns extracted. a
	 *            CellColumn will always return itself regardless of the depth
	 *            level specified (because it has no subcolumns). A depth of 0
	 *            generally means the current column.
	 * @return a list of ColumnS on the specified depth position
	 */
	public abstract List getSubColumns(int depth);

	public List getSubColumnList() {
		return getSubColumns(currentDepth);
	}

	/**
	 * @return Returns the currentDepth.
	 */
	public int getCurrentDepth() {
		return currentDepth;
	}

	/**
	 * @param currentDepth
	 *            The currentDepth to set.
	 */
	public void setCurrentDepth(int currentDepth) {
		this.currentDepth = currentDepth;
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan(int rowSpan) {
		spanCount = 0;
		this.rowSpan = rowSpan;
	}

	public abstract Set<Long> getOwnerIds();

	/**
	 * Produces a list of trail CellS. These are usually custom made cells that
	 * represent some information regarding the Column, that are usually
	 * displayed at the bottom of the column. For AmountCellColumnS this is the
	 * place to display totals. However, other types of behaviour can be
	 * implemented, like error reporting for debugging purposes
	 * @return the list of trail cells
	 */
	public abstract List getTrailCells();

	public abstract Column newInstance();
	
	
	@Override
	public ReportData getNearestReportData() {
		if (parent == null)
			return null;
		return parent.getNearestReportData();
	}
	
	
	public abstract int getColumnDepth();

	/**
	 * Counts the leafs (Cells) in the column tree
	 * @return the cell count
	 */
	public abstract int getCellCount();

	/**
	 * Counts the leafs (Cells) in the column tree that are visible (toString not empty)
	 * @param ownerId TODO
	 * @return the cell count
	 */
	public abstract int getVisibleCellCount(Long ownerId);
	
	/**
	 * Returns the full column name for this column. This means that, if this column has a parent
	 * it wil append its name to the output
	 * @return the complete column name as seen in the header
	 */
	public String getAbsoluteColumnName(){
		if (parent!=null && parent instanceof Column) return ((Column)parent).getAbsoluteColumnName()+"--"+ this.name;
		else return this.name;
	}

	
    /**
     * @see Column.getAbsoluteColumnName
     * Useful for translating the column names
     * @return the absolute column name as a collection, starting with the topmost parent (root) and ending with the self name
     */
	public Collection<String> getAbsoluteColumnNameAsList(){
		List<String> names = new ArrayList<String>();
		if (parent != null && parent instanceof Column) 
			names.addAll(((Column)parent).getAbsoluteColumnNameAsList()) ;
		names.add(this.name);
		return names;
	}

    public Collection<Column> getAbsoluteColumnAsList(){
        List<Column> cols = new ArrayList<Column>();
        if (parent != null && parent instanceof Column)
            cols.addAll(((Column)parent).getAbsoluteColumnAsList()) ;
        cols.add(this);
        return cols;
    }

	
	/**
	 * @param contentCategory The contentCategory to set.
	 */
	public void setContentCategory(String categ) {
		if(this.contentCategory!=null) return;
		this.contentCategory = categ;
		//logger.info("Column "+this.getAbsoluteColumnName()+" has categ="+categ);
	}

	
	/**
	 * @return Returns the contentCategory.
	 */
	public String getContentCategory() {
		return contentCategory;
	}

	/**
	 * @return Returns the maxNameDisplayLength.
	 */
	public int getMaxNameDisplayLength() {
		return maxNameDisplayLength;
	}

	/**
	 * @param maxNameDisplayLength The maxNameDisplayLength to set.
	 */
	public void setMaxNameDisplayLength(int maxNameDisplayLength) {
		this.maxNameDisplayLength = maxNameDisplayLength;
	}

	public void setItems(List<K> items) {
	    this.items = items;
	}

	public Class getRelatedContentPersisterClass() {
	    return relatedContentPersisterClass;
	}

	public void setRelatedContentPersisterClass(Class relatedContentPersisterClass) {
	    this.relatedContentPersisterClass = relatedContentPersisterClass;
	}

	public Class getDimensionClass() {
	    return dimensionClass;
	}

	public void setDimensionClass(Class dimensionArbiter) {
	    this.dimensionClass = dimensionArbiter;
	}


	@Override
	public boolean equals(Object o) {
		if (o!=null && this.getName()!=null) {
		    Column c=(Column) o;
		    return this.getName().equals(c.getName());
		} else {
			return false;
		}
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public abstract Column hasSorterColumn(String namePath);
	
	public String getNamePath () {
		if ( parent instanceof Column ) {
			return ((Column) parent).getNamePath() + "/" + name;
		}
		else
			return "/" + name;
	}
	
	/**
	 * remove all Cells whose ownerId is not among the ones in the specified set
	 * might leave an empty column!
	 * @param idsToRetain
	 */
	public abstract void filterByIds(Set<Long> idsToRetain);
	
	
	public abstract List<Cell> getAllCells(List<Cell> src, boolean freeze);
	/**
	 * remove any sign of existence of any data with given ownerId's
	 * @param ownerId
	 */
	public abstract void deleteByOwnerId(Set<Long> ownerId);
	
}

