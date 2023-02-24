package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.*;

/**
 * interface for specifying a way of filtering cells
 * @author Dolghier Constantin
 *
 */
public interface CellFilterCriteria {
    
    /**
     * returns a filtered cell to replace the source one (might be the src one) or NULL
     * @param source
     * @return
     */
    public Cell filter(Cell source);
}
