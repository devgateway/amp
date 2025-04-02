/**
 * Cell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnIdentifiable;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.RowIdentifiable;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.digijava.kernel.translator.TranslatorWorker;


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
public abstract class Cell <C extends Cell> extends Viewable implements RowIdentifiable, ColumnIdentifiable, Comparable<C>,Cloneable {

    
    @Override
    public ReportData getNearestReportData() {
        return column.getNearestReportData();
    }
    
    public static class CellComparator implements Comparator<Cell> { 
        public final int compare (Cell o1, Cell o2) {
            if (o1 == null || o2 == null)
                return 0;
            //logger.debug("3 cell o1 instance : "+o1.getValue());
            //logger.debug("4 cell o2 instance : "+o2.getValue());            
            return o1.comparableToken().compareTo(o2.comparableToken());
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
    
//  isShowable static duplicate, just to speed things up 
    protected boolean show;
    
    protected static Logger logger = Logger.getLogger(Cell.class);
    
    /**
     * Returns worker class assigned for this type of Cell. The Worker class
     * is a ColumnWorker descendant.
     * @return the ColumnWorker class
     * @see ColumnWorker
     */
    public abstract Class<? extends ColumnWorker> getWorker();
    

    /**
     * compares two lists, first by length, then element by element
     * @param a
     * @param b
     * @return
     */
    protected int compareLists(Collection<Comparable> a, Collection<Comparable> b)
    {
        if (a.size() != b.size())
            return a.size() - b.size();
        int sz = a.size(); // sizes are equal
        Iterator<Comparable> itA = a.iterator();
        Iterator<Comparable> itB = b.iterator();
        for(int i = 0; i < sz; i ++)
        {
            Comparable elemA = itA.next();
            Comparable elemB = itB.next();
            int z = elemA.compareTo(elemB);
            if (z != 0)
                return z;
        }
        return 0;
    }
    
    
    public int compareTo(C o) {
        if (this.getValue() == null)
        {
            if (o.getValue() == null)
                return 0;
            return -1;
        }
        if (o.getValue() == null)
            return 1;
        
        // got till here -> no nulls
        if (this.getValue() instanceof Collection)
        {
            if (o.getValue() instanceof Collection)
                return compareLists((Collection<Comparable>)this.getValue(), (Collection<Comparable>)o.getValue());
            
            return 1;
        }
        if (o.getValue() instanceof Collection)
        {
            // the first one is not a list -> return -1
            return -1;
        }
        
        return ((Comparable)this.getValue()).compareTo(o.getValue());
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
        if(ids.contains(ownerId)) 
            return cloneYourself();
        else
            return null;
    }

    //public static java.util.Map<String, Long> cloneCalls = new java.util.TreeMap<String, Long>();
    protected Cell cloneYourself()
    {
//      String className = this.getClass().getName();
//      if (!cloneCalls.containsKey(className))
//          cloneCalls.put(className, 0L);
//      cloneCalls.put(className, cloneCalls.get(className) + 1);
        try
        {
            return (Cell) this.clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new RuntimeException("error cloning cell of type "+ this.getClass().getName(), e);
        }
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
    
    public String getWrapDirective() {
         return "nowrap=\"nowrap\"";
    }
    
    /**
     * returns true IFF this cell "isunallocated"
     * <b>WARNING</b> implemented as an incredibly ugly hack (this is just old code moved around) - it can get ruined by a location or sector named "Unallocated" (or its translation thereof in the currently-used language).
     * TODO: fix as soon as some free time is found by redoing ColumnReportData#horizSplitByCateg 
     * @return
     */
    public boolean isUnallocatedCell()
    {
        String text = TranslatorWorker.translateText(ArConstants.UNALLOCATED);
        String translatedTextUnallocated = text;
        if (translatedTextUnallocated.compareTo("") == 0)
            translatedTextUnallocated = text;
        
        return getValue().toString().contains(translatedTextUnallocated) && (this.getId() == null || this.getId() < 1);
    }
    

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
    
    public boolean isPledgeDisquisedAsActivity() {
        return this.getOwnerId() > AmpReportGenerator.PLEDGES_IDS_START;
    }
    
    public boolean isDisquisedPledgeCellWhichShouldBeHighlited() {
        return this.isPledgeDisquisedAsActivity() && this.getColumn() != null && 
                (this.getColumn().getName().equals(ArConstants.COLUMN_PROJECT_TITLE) || this.getColumn().getName().equals(ArConstants.COLUMN_AMP_ID));  
    }
    
    @Override
    public String prettyPrint()
    {
        return String.format("%s %s", this.getClass().getSimpleName(), this.toString());
    }
}
