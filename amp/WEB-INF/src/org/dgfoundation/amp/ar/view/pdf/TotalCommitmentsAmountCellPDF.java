/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since 31.10.2006
 * Copyright (c) 31.10.2006 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.view.pdf;

import com.lowagie.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * @author mihai
 * @since 31.10.2006
 *
 */
public class TotalCommitmentsAmountCellPDF extends AmountCellPDF {

    /**
     * @param parent
     * @param item
     */
    public TotalCommitmentsAmountCellPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public TotalCommitmentsAmountCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

}
