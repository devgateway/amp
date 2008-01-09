/*
 * Created on 11.07.2006
 *
 * @author: Mihai Postelnicu - mihai@code.ro
 */
package org.dgfoundation.amp.ar;

import java.util.Iterator;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

public class TotalAmountColumn extends AmountCellColumn {

	public boolean filterShowable; 
	
    /**
	 * @return Returns the filterShowable.
	 */
	public boolean isFilterShowable() {
		return filterShowable;
	}

	/**
	 * @param filterShowable The filterShowable to set.
	 */
	public void setFilterShowable(boolean filterShowable) {
		this.filterShowable = filterShowable;
	}

	public TotalAmountColumn(ColumnWorker worker) {
        super(worker);
        filterShowable=false;
    }

    public TotalAmountColumn(String name) {
        super(name);
        filterShowable=false;
    }

    public TotalAmountColumn(String name,boolean filterShowable) {
        super(name);
        this.filterShowable=filterShowable;
    }
    

    public TotalAmountColumn(String name,boolean filterShowable,int initialCapacity) {
        super(name,initialCapacity);
        this.filterShowable=filterShowable;
    }

    
    public TotalAmountColumn(Column parent, String name) {
        super(parent, name);
        filterShowable=false;
    }
    
    /**
	 * @param source
	 */
	public TotalAmountColumn(Column source) {
		super(source);
		filterShowable=false;
	}

	public void addCell(Object c) {
        AmountCell ac=(AmountCell) c;
        if(filterShowable && !ac.isShow()) return; 
        Iterator i=items.iterator();
        Cell freshc=new AmountCell();
        while (i.hasNext()) {
            AmountCell element = (AmountCell) i.next();            
            if(element.getOwnerId().equals(ac.getOwnerId())) {
        	freshc.setOwnerId(element.getOwnerId());
        	freshc.merge(ac,element);
                i.remove();
                break;                
            }
        }
        if(freshc.getOwnerId()==null)
		super.addCell(ac);
        else 
        	super.addCell(freshc);
        	
    }
    
    public Column newInstance() {
		return new TotalAmountColumn(this);
	}
}
