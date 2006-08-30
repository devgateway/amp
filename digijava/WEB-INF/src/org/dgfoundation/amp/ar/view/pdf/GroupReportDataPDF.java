/**
 * GroupReportDataPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.Iterator;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
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
public class GroupReportDataPDF extends PDFExporter {

	/**
	 * @param parent
	 */
	public GroupReportDataPDF(Exporter parent,Viewable item) {
		super(parent,item);
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public GroupReportDataPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		GroupReportData grd=(GroupReportData) item;
		
		
		Font titleFont = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD);
		
		
		if(grd.getParent()!=null) {
			PdfPCell pdfc = new PdfPCell(new Paragraph(grd.getName(),titleFont));
			pdfc.setColspan(grd.getTotalDepth());
			table.addCell(pdfc);
		}
		Iterator i=grd.getItems().iterator();
		while (i.hasNext()) {
			Viewable element = (Viewable) i.next();
			element.invokeExporter(this);			
		}

		
		//add trail cells
		TrailCellsPDF trails=new TrailCellsPDF(this,grd);
		trails.generate();

//		add an empty row
		PdfPCell pdfc2 = new PdfPCell(new Paragraph(" "));
		pdfc2.setColspan(grd.getTotalDepth());
		table.addCell(pdfc2);
		
		
	}

}
