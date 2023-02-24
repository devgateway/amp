/**
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

/**
 * @author mihai
 *
 */
public class MetaTextCellPDF extends TextCellPDF {

    /**
     * @param parent
     * @param item
     */
    public MetaTextCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public MetaTextCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
