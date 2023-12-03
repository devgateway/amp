/**
 * ListCellPDF.java
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
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.digijava.module.aim.action.ExportActivityToPDF;

import java.util.Iterator;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class ListCellPDF extends PDFExporter {

    public ListCellPDF(Exporter parent,Viewable item) {
        super(parent,item);
    }
    
    /**
     * @param table
     * @param item
     */
    public ListCellPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item,ownerId);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
     */
    public void generate() {
        ListCell lc=(ListCell) item;
        //Collection<Cell> items=(Collection<Cell>) lc.getValue();
        String res="";
        
        Iterator<Cell> i = lc.iterator();       
        while (i.hasNext()) {
            Cell element = i.next();
            res+=element.toString();
            if(i.hasNext()) res+=", ";
        }
        PdfPCell pdfc = new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(res),new Font(ExportActivityToPDF.basefont, 10, Font.NORMAL)));
        table.addCell(pdfc);
    }

}
