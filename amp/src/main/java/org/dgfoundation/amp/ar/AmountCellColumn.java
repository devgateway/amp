/**
 * AmountCellColumn.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 1, 2006
 *
 */
public class AmountCellColumn<K extends AmountCell> extends CellColumn<K> {

    /**
     * @param worker
     */
    public AmountCellColumn(ColumnWorker worker) {
        super(worker);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param name
     */
    public AmountCellColumn(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public AmountCellColumn(String name,int initialCapacity) {
        super(name,initialCapacity);
    }

    
    /**
     * @param source
     */
    public AmountCellColumn(Column source) {
        super(source);
        // TODO Auto-generated constructor stub
    }
 
    /**
     * @param parent
     * @param name
     */
    public AmountCellColumn(Column parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public Column postProcess() {
        AmountCellColumn res=(AmountCellColumn) super.postProcess();
        //now we get rid of those ListCellS, we merge the content - we can do that now because there is no forking in the future
        for(int i = 0; i < res.getItems().size(); i++) {
            Cell element = (Cell) res.getItem(i);
            if(element instanceof ListCell) {
                Cell repl = ((ListCell) element).createSummaryElement();
                Iterator ii=((ListCell)element).iterator();
                while (ii.hasNext()) {
                    AmountCell ac = (AmountCell) ii.next();
                    repl.merge(ac,repl);
                }
                res.replaceCell(element,repl);              
            }       
        }
        return res;
    }


    public List<? extends AmountCell> getTrailCells() {
        ArrayList<AmountCell> ar = new ArrayList<AmountCell>();
        AmountCell ac = new AmountCell();       
        Iterator i = items.iterator();
        while (i.hasNext()) {
            AmountCell element = (AmountCell) i.next();
            //logger.info("Merging cell for owner "+element.getOwnerId()+" containing "+element.getMergedCells().size()+" merged cells");
            ac.mergeWithCell(element);          
        }
        ac.setColumn(this);
        ar.add(ac);
        return ar;
    }
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#newInstance()
     */
    public Column newInstance() {
        return new AmountCellColumn(this);
    }
    
    public boolean hasTrailCells() {
        return true;
        }
    
}
