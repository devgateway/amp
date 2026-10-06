/**
 * ErrorCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.Column;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 17, 2006
 * A text TextCell holds an error message
 * @see org.dgfoundation.amp.ar.cell.TextCell
 *
 */
public class ErrorCell extends TextCell {

    /**
     * 
     */
    public ErrorCell() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ErrorCell(String text,Column column) {
        super();
        value=text;
        this.column=column;
    }

    
    /**
     * @param id
     */
    public ErrorCell(Long id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

}
