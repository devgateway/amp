/**
 * TextCellPDF.java
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
import org.dgfoundation.amp.ar.cell.TextCell;
import org.digijava.module.aim.action.ExportActivityToPDF;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class TextCellPDF extends PDFExporter {

    public TextCellPDF(Exporter parent,Viewable item) {
        super(parent,item);
    }
    
    /**
     * @param table
     * @param item
     */
    public TextCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item,ownerId);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
     */
    @Override
    public void generate() {
        TextCell c = (TextCell) item;
        PdfPCell pdfc = null;
        Font font;
        if (c.isDisquisedPledgeCellWhichShouldBeHighlited()) {
            font = new Font(ExportActivityToPDF.basefont, 9, Font.BOLD); 
            font.setColor(106, 106, 0);
        }
        else { 
            font = new Font(ExportActivityToPDF.basefont, 9, Font.NORMAL);
        }
        pdfc = new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(c.toString()), font));
        
        table.addCell(pdfc);
    }

}
