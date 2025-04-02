/**
 * CummulativeAmountCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.workers.ExpressionColWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 26, 2006
 *
 */
public class ExpressionAmountCell extends AmountCell {

    /**
     * 
     */
    public ExpressionAmountCell() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     */
    public ExpressionAmountCell(Long id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    public Class getWorker() {
        return ExpressionColWorker.class;
    }

    
}
