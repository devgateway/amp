package org.dgfoundation.amp.ar.view.pdf;


import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Diego Dimunzio Ago 15, 2009
 */

public class ComputedMeasureCellPDF extends AmountCellPDF {

    public ComputedMeasureCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    public ComputedMeasureCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
