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
import java.util.SortedSet;
import java.util.TreeSet;

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
    
    /**
     * they must be kept in-order and unique
     */
    protected SortedSet<Cell> value = new TreeSet<Cell>();
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#toString()
     */

    
    public ListCell() {
        super();
    }
    

    public ListCell(ListCell src) {
        super();
        value.addAll(src.getValue());
    }
    
    
    public Class getWorker()
    {
        Cell firstCell = getFirstCell();
        if (firstCell == null)
            return null;
        return firstCell.getWorker();
    }
    
    
    public Cell getFirstCell()
    {
        if (value == null)
            return null;
        if (value.isEmpty())
            return null;
        return value.first();
    }
    
    public void addCell(Cell c) throws IncompatibleCellException
    {
        if(c==null)
            return;
        
        if (ownerId == null)
            ownerId = c.getOwnerId();
        
        if ((getFirstCell() != null) && (!getFirstCell().getOwnerId().equals(c.getOwnerId()))) 
            throw new IncompatibleCellException("Group Cells must hold items with identical Ids!");
        
        value.add(c);
    }
    
    public Iterator<Cell> iterator() {
        return value.iterator();
    }
    
    public void addCells(Collection<Cell> cells) throws IncompatibleCellException
    {
        for(Cell cell:cells)
            addCell(cell);
    }
    
    public ListCell(Long id) {
        super(id);
    }

    
    public String toString() {
        return value.toString();
    }

    @Override
    public Collection<Cell> getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
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
                logger.error(e.getMessage(), e);
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
            logger.error(e.getMessage(), e);
        }
        return ret;
    }


    public Cell newInstance() {
        return new ListCell();
    }


    public Comparable comparableToken() {
        return getFirstCell().comparableToken();
    }


    @Override
    public void merge(Cell c1, Cell c2) {
        try {
            if(!this.equals(c1)) this.addCells((Collection) c1.getValue());
            if(!this.equals(c2)) this.addCells((Collection) c2.getValue());
            
        } catch (IncompatibleCellException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * create an element which has the same class as the first element in the ListCell (or AmountCell if no child exist)
     * @return
     */
    public Cell createSummaryElement()
    {
        Cell firstCell = getFirstCell();
        
        if (firstCell == null)
            return new AmountCell(); // failsafe result, preserves old behaviour
                
        Class childClass = firstCell.getClass();
        try
        {
            return (Cell) (childClass.newInstance());
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            return new AmountCell();
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
            return new AmountCell();
        }
        
    }
}
