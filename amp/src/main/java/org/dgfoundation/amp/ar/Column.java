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
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.helper.ReportHeadingLayoutCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReports;

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
    
    protected ReportHeadingLayoutCell positionInHeading;
    
//  /**
//   * the number of times "getRowSpan" was called on this instance - a proxy for "the row we are in now"
//   */
//  protected int spanCount = 0;

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

    public ReportHeadingLayoutCell getPositionInHeading()
    {
        return this.positionInHeading;
    }
    
//  public int getRowSpan()
//  {
//      return this.positionInHeading.getRowSpan();
//  }
    /**
     * only to be used internally during initialization of the layout. Real uses of the code should go to {@link #getPositionInHeading()}<br />
     * notice that the reports engine might decide for the column to have more rows than the one specified by this function (for filling the heading upto the bottom) - but never less 
     * @return
     */
    protected abstract int getRowSpanInHeading_internal();

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
     * sets, recursively, the row span for this column and all of its subcolumns
     */
    public abstract void setPositionInHeadingLayout(int totalRowSpan, int startingDepth, int startingColumn);
    
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

    /**
     * the total <b>rowspan</b> of this column and all of this subcolumns in the report's heading<br />
     * for the rowspan of this column per se (the number of columns needed to display its title), please see {@link #getNewRowSpan()} <br />
     * only called once per item when initialized CRD
     * @return
     */
    public abstract int calculateTotalRowSpan();

    /**
     * returns a list of ColumnS whose name is displayed starting at a given depth in the table header
     * 
     * @param depth
     *            the depth level from where we want subcolumns extracted.
     * @return a list of ColumnS which start on the specified depth position in the table header
     */
    public abstract List<Column> getSubColumns(int depth);

    /**
     * can this column be sorted by?
     * @return
     */
    public abstract boolean isSortableBy();
    /**
     * equivalent to calling {@link #getSubColumns(this.currentDepth)} - this is done because one can't supply arguments when using a function from JSP<br />
     * <b>generally one should not use this function except frmo JSPs</b>
     * @return
     */
    public List<Column> getSubColumnList() {
        return getSubColumns(currentDepth);
    }

    /**
     * @return Returns the currentDepth.
     */
    public int getCurrentDepth() {
        return currentDepth;
    }

    /**
     * @param currentDepth - the currently rendered row of the report's header. Done for the only reason that JSP does not support calling a function with arguments
     * 
     */
    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

//  /**
//   * @return Returns the rowSpan.
//   */
//  public int getRowSpan() {
//      return rowSpan;
//  }

//  /**
//   * @param rowSpan
//   *            The rowSpan to set.
//   */
//  public void setRowSpan(int rowSpan) {
//      spanCount = 0;
//      this.rowSpan = rowSpan;
//  }

    public abstract Set<Long> getOwnerIds();

    /**
     * Produces a list of trail CellS. These are usually custom made cells that
     * represent some information regarding the Column, that are usually
     * displayed at the bottom of the column. For AmountCellColumnS this is the
     * place to display totals. However, other types of behaviour can be
     * implemented, like error reporting for debugging purposes
     * @return the list of trail cells
     */ 
    public abstract List<? extends AmountCell> getTrailCells();

    public abstract Column newInstance();
    
    
    @Override
    public ReportData getNearestReportData() {
        if (parent == null)
            return null;
        return parent.getNearestReportData();
    }
    
    /**
     * the <b>colspan</b> of the column
     * @return
     */
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

    @Override public int hashCode() {
        if (this.getName() == null)
            return 0;
        return this.getName().hashCode();
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
    
    @Override
    public String prettyPrint()
    {
        StringBuffer res = new StringBuffer(String.format("(%s %s: ", this.getClass().getSimpleName(), this.getName()));
        boolean smthAdded = false;
        for(Object child:this.getItems())
        {
            String z = child.toString(); 
            if (child instanceof Viewable)
                z = ((Viewable) child).prettyPrint();
            if (smthAdded)
                res.append(", ");
            res.append(z);
            smthAdded = true;
        }
        res.append(")");
        return res.toString();
    }
    
    /**
     * Helper method that only uses one category to create a categorized tree. Returns null if categorizing failed for some reasons
     * @param src The source column to be categorized
     * @param category the category to categorize the data with
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     * @see verticalSplitByCategs
     */
    public abstract GroupColumn verticalSplitByCateg(String category, Set<Long> ids, boolean generateTotalCols, AmpReports reportMetadata);
    
    public GroupColumn verticalSplitByCateg(String category, boolean generateTotalCols, AmpReports reportMetadata)
    {
        return verticalSplitByCateg(category, null, generateTotalCols, reportMetadata);
    }
    
    /**
     * Split a column holding CategAmountCellS into several subcolumns based on the categorized amount data and some
     * categories given as reference. The result will be a categorized column tree of GroupColumnS and CellColumnS as leafs. 
     * Each category will create another level on the tree while on the same level we will find several GroupColumnS that 
     * share the same metainfo category but not the same value.
     * @param src The source CellColumn to be categorized
     * @param categories the list of categories to be applied to the src column
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS 
     * @return a GroupColumn that holds the categorized data
     * @see MetaInfo, TotalAmountColumn, CategAmountCell
     */
    public Column verticalSplitByCategs(List<String> categories, Set<Long> ids, boolean generateTotalCols, AmpReports reportMetadata) 
    {
        if (categories == null || categories.isEmpty())
            return null;
        Column curCol = this;
        int nrCategories = categories.size();
        for(int i = 0; i < nrCategories; i++)
        {
            String cat = categories.get(i);         
            GroupColumn splitCol = curCol.verticalSplitByCateg(cat, ids, generateTotalCols && (i == nrCategories - 1), reportMetadata);
            if (splitCol != null)
                curCol = splitCol; // if column is not splittable by this category, just skip this category
        }
        return curCol;
    }
    
    /**
     * convenience method for not specifying ids
     * @param categories
     * @param generateTotalCols
     * @param reportMetadata
     * @return
     */
    public Column verticalSplitByCategs(List<String> categories, boolean generateTotalCols, AmpReports reportMetadata) 
    {
        return verticalSplitByCategs(categories, null, generateTotalCols, reportMetadata);
    }
}

