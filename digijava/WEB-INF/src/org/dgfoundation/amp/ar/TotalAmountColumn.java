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

    public TotalAmountColumn(ColumnWorker worker) {
        super(worker);
        // TODO Auto-generated constructor stub
    }

    public TotalAmountColumn(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public TotalAmountColumn(Column parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @param source
	 */
	public TotalAmountColumn(Column source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

    public void addCell(Object c) {
        AmountCell ac=(AmountCell) c;
        Iterator i=items.iterator();
        Cell freshc=null;
        while (i.hasNext()) {
            AmountCell element = (AmountCell) i.next();            
            if(element.getOwnerId().equals(ac.getOwnerId())) {
                freshc=element.merge(ac);
                freshc.setOwnerId(element.getOwnerId());
                i.remove();
                break;                
            }
        }
        if(freshc==null)
			super.addCell(ac);
        else 
        	super.addCell(freshc);
        	
    }
    
    public Column newInstance() {
		return new TotalAmountColumn(this);
	}
}
