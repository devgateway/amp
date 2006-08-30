/**
 * TrailCellsPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.Iterator;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 29, 2006
 *
 */
public class TrailCellsPDF extends PDFExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public TrailCellsPDF(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public TrailCellsPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		//generate totals:
		ReportData grd=(ReportData) item;
		Font totalFont = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.ITALIC);
		
		if(grd.getParent()!=null) {
			PdfPCell pdfc = new PdfPCell(new Paragraph("TOTALS FOR "+grd.getName(),totalFont));
			pdfc.setColspan(grd.getSourceColsCount().intValue());
			table.addCell(pdfc);
			
			Iterator i=grd.getTrailCells().iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				element.invokeExporter(this);
			}
			
			
		}
	

	}

}
