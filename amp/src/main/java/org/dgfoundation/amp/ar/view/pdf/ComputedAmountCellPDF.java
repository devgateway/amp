package org.dgfoundation.amp.ar.view.pdf;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.workers.ComputedAmountColWorker;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.TokenRepository;

import com.lowagie.text.pdf.PdfPTable;

public class ComputedAmountCellPDF extends CategAmountCellPDF {

    public ComputedAmountCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
    }

    /**
     * @param table
     * @param item
     */
    public ComputedAmountCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }
}
