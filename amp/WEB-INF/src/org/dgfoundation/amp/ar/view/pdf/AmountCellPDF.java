/**
 * AmountCellPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.module.aim.action.ExportActivityToPDF;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class AmountCellPDF extends PDFExporter {

    
    public AmountCellPDF(Exporter parent,Viewable item) {
        super(parent, item);
    }
    
    /**
     * @param table
     * @param item
     */
    public AmountCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
     */
    public void generate()
    {
        AmountCell ac = (AmountCell) item;
        PdfPCell pdfc = new PdfPCell(new Paragraph(ac.toString(),new Font(ExportActivityToPDF.basefont, 9, Font.NORMAL)));
        pdfc.setVerticalAlignment(Element.ALIGN_CENTER);
        pdfc.setHorizontalAlignment(Element.ALIGN_RIGHT);
        if (getExportState().currentBackColor != null)
        {
            pdfc.setBackgroundColor(getExportState().currentBackColor);
        }
        table.addCell(pdfc);
    }
 
}
