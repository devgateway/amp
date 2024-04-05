/**
 * DateCellPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;


import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.DateCell;
import org.digijava.module.aim.action.ExportActivityToPDF;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class DateCellPDF extends PDFExporter {

    /**
     * @param parent
     */
    public DateCellPDF(Exporter parent, Viewable item)
    {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public DateCellPDF(PdfPTable table, Viewable item, Long ownerId)
    {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        DateCell c=(DateCell) item;
        PdfPCell  pdfc = new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(c.toString()),new Font(ExportActivityToPDF.basefont, 9, Font.NORMAL)));
        
        table.addCell(pdfc);
    }

}
