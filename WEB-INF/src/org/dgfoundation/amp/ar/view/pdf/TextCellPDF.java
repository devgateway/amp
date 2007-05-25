/**
 * TextCellPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

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
	public TextCellPDF(PdfPTable table, Viewable item,Long ownerId) {
		super(table, item,ownerId);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.view.pdf.PDFExporter#generate()
	 */
	public void generate() {
		TextCell c=(TextCell) item;
		PdfPCell pdfc = new PdfPCell(new Paragraph(c.toString()));
		table.addCell(pdfc);
	}

}
