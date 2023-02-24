/**
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.cell.XmlHierarchyCell;
import org.dgfoundation.amp.ar.helper.HierarchycalItem;
import org.digijava.module.aim.action.ExportActivityToPDF;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Alex
 *
 */
public class XmlHierarchyCellPDF extends PDFExporter {
    public XmlHierarchyCellPDF(Exporter parent,Viewable item) {
        super(parent,item);
    }
    
    /**
     * @param table
     * @param item
     */
    public XmlHierarchyCellPDF(PdfPTable table, Viewable item,Long ownerId) {
        super(table, item,ownerId);
        // TODO Auto-generated constructor stub
    }
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    @Override
    public void generate() {
        XmlHierarchyCell xmlCell=(XmlHierarchyCell) item;
        PdfPCell pdfc;
        if (xmlCell.getRootItems() == null || xmlCell.getRootItems().size() == 0 ) {
            pdfc    = new PdfPCell(new Paragraph("",new Font(ExportActivityToPDF.basefont, 9, Font.NORMAL)));
        }
        else {
            List list       = new List(false);
            this.populateList(list, xmlCell.getRootItems() );
            pdfc    = new PdfPCell();
            pdfc.addElement(list);
        }

        table.addCell(pdfc);
    }

    private void populateList(List pdfList, java.util.List<HierarchycalItem> srcList) {
        for (HierarchycalItem srcItem: srcList) {
            Chunk nameChunk         = new Chunk(srcItem.getDateString()+ " " + srcItem.getName() + ": ", new Font(ExportActivityToPDF.basefont, 9, Font.BOLD) );
            Chunk descriptionChunk  = new Chunk(srcItem.getDescription(), new Font(ExportActivityToPDF.basefont, 9) );
            ListItem listItem       = new ListItem();
            listItem.add(nameChunk);
            listItem.add(descriptionChunk);
            pdfList.add(listItem);
            if ( srcItem.getChildren() != null && srcItem.getChildren().size() > 0 ) {
                List childList  = new List(false);
                childList.setIndentationLeft(20);
                
                this.populateList(childList, srcItem.getChildren() );
                
                pdfList.add(childList);
            }
        }
    }
}
