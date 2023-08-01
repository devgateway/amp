package org.dgfoundation.amp.ar.view.pdf;

import com.lowagie.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

public class ComputedDateCellPDF extends TextCellPDF {

    public ComputedDateCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
    }

    /**
     * @param table
     * @param item
     */
    public ComputedDateCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
