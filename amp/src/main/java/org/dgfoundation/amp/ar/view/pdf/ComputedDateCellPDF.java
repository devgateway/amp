package org.dgfoundation.amp.ar.view.pdf;

import java.io.IOException;
import java.io.StringReader;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Html2TextCallback;

import com.lowagie.text.pdf.PdfPTable;

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
