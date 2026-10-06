package org.dgfoundation.amp.ar.view.pdf;

import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

public class TrnTextCellPDF extends TextCellPDF {

    public TrnTextCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    public TrnTextCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }
}
