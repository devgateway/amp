/*
 * Created on 11.07.2006 @author: Mihai Postelnicu - mihai@code.ro
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

public class TotalAmountColumn<K extends AmountCell> extends AmountCellColumn<K> {

    public boolean filterShowable;

    /**
     * @return Returns the filterShowable.
     */
    public boolean isFilterShowable() {
    return filterShowable;
    }

    /**
     * @param filterShowable
     *                The filterShowable to set.
     */
    public void setFilterShowable(boolean filterShowable) {
    this.filterShowable = filterShowable;
    }

    public TotalAmountColumn(ColumnWorker worker) {
    super(worker);
    filterShowable = false;
    }

    public TotalAmountColumn(String name) {
    super(name);
    filterShowable = false;
    }

    public TotalAmountColumn(String name, boolean filterShowable) {
    super(name);
    this.filterShowable = filterShowable;
    }

    public TotalAmountColumn(String name, boolean filterShowable, int initialCapacity) {
    super(name, initialCapacity);
    this.filterShowable = filterShowable;
    }

    public TotalAmountColumn(Column parent, String name) {
    super(parent, name);
    filterShowable = false;
    }

    /**
     * @param source
     */
    public TotalAmountColumn(Column source) {
    super(source);
    filterShowable = false;
    if (source instanceof TotalAmountColumn)
        filterShowable = ((TotalAmountColumn) source).isFilterShowable();
    }

    @Override
    public void addCell(Cell c) {
        try {
            List<AmountCell> tobeMergedCells    = new ArrayList<AmountCell>();
            if ( c instanceof ListCell ) {
                Iterator<Cell> iter = ((ListCell)c).iterator();
                while ( iter.hasNext() ) {
                    tobeMergedCells.add((AmountCell) iter.next());
                }
            }
            else
            {
                AmountCell ac = (AmountCell) c;
                tobeMergedCells.add(ac);
            }
            for (AmountCell ac:tobeMergedCells) 
            {
                if (filterShowable && !ac.isShow())
                    continue;

                AmountCell byOwner = (AmountCell) this.getByOwner(ac.getOwnerId());
                if (byOwner != null && (ac != byOwner))
                    byOwner.mergeWithCell(ac);
                else {
                    AmountCell newcell = (AmountCell) ac.newInstance();
                    //AmountCell newcell = new AmountCell();
                    newcell.mergeWithCell(ac);
                    newcell.setColumn(this);
                    super.addCell((K) newcell);
                }
            }
        }
        catch (ClassCastException e) {
            logger.error("Wrong class for cell in addCell(). It should be AmountCell and not " + c.getClass().getName() );
        }
    }

    public void absorbColumn(AmountCellColumn<? extends AmountCell> column)
    {
        for(AmountCell cell:column.getItems())
            addCell(cell);
    }
    
    public Column newInstance()
    {
        return new TotalAmountColumn(this);
    }
}
