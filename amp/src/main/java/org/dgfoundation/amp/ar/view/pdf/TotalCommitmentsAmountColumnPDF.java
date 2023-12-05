/**
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * @author mihai
 *
 */
public class TotalCommitmentsAmountColumnPDF extends TotalAmountColumnPDF {

    /**
     * @param parent
     * @param item
     */
    public TotalCommitmentsAmountColumnPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public TotalCommitmentsAmountColumnPDF(PdfPTable table, Viewable item,
                                           Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
