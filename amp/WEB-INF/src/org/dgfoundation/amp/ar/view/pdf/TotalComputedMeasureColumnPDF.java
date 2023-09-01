/**
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Diego Dimunzio Ago 15, 2009
 */
public class TotalComputedMeasureColumnPDF extends TotalAmountColumnPDF {

    public TotalComputedMeasureColumnPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    public TotalComputedMeasureColumnPDF(PdfPTable table, Viewable item,
                                         Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
