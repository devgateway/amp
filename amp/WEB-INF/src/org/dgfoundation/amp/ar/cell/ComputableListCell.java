/**
 * ComputableListCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.Computable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 31, 2006
 * @deprecated
 *
 */
public class ComputableListCell extends ListCell implements Computable {

    
    
    public ComputableListCell() {
        super();
        
    }
    
    /**
     * @param ownerId
     * @param name
     * @param value
     */
    public ComputableListCell(Long id) {
        super(id);
    }

    public Double computeTotal() {
        double total=0;
        List lst=(List) this.getValue();
        Iterator i=lst.iterator();
        while (i.hasNext()) {
            Computable element = (Computable) i.next();
            total+=element.computeTotal().longValue();
        }
        return new Double(total);
    }

    
}
