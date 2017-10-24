/**
 * CategAmountCellPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class CategAmountCellPDF extends AmountCellPDF {

    public CategAmountCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
    }
    
    
    /**
     * @param table
     * @param item
     */
    public CategAmountCellPDF(PdfPTable table, Viewable item,Long ownerId) {
        super(table, item,ownerId);
        // TODO Auto-generated constructor stub
    }

}
