/**
 * GroupColumnPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import com.lowagie.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.Viewable;

import java.util.Iterator;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public class GroupColumnPDF extends PDFExporter {

    /**
     * @param parent
     */
    public GroupColumnPDF(Exporter parent,Viewable item) {
        super(parent,item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public GroupColumnPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        GroupColumn gc=(GroupColumn) item;
        Iterator i=gc.getItems().iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            element.invokeExporter(this);
        }

    }

}
