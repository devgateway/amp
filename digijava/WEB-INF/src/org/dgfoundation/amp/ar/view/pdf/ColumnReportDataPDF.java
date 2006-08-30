/**
 * ColumnReportDataPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.util.Iterator;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class ColumnReportDataPDF extends PDFExporter {

	/**
	 * @param parent
	 */
	public ColumnReportDataPDF(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param table
	 * @param item
	 * @param ownerId
	 */
	public ColumnReportDataPDF(PdfPTable table, Viewable item, Long ownerId) {
		super(table, item, ownerId);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;

		Font titleFont = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD);
		//title
		if (columnReport.getParent() != null) {
			PdfPCell pdfc = new PdfPCell(new Paragraph(columnReport.getName(),titleFont));
			pdfc.setColspan(columnReport.getTotalDepth());
			table.addCell(pdfc);
		}
		
		
		// headings

		Font font = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);
		
		for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++) {
			Iterator i = columnReport.getItems().iterator();
			while (i.hasNext()) {
				
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				
				if(ii.hasNext())
				while (ii.hasNext()) {
					Column element2 = (Column) ii.next();
					PdfPCell pdfc = new PdfPCell(new Paragraph(element2
							.getName(),font));
					pdfc.setColspan(element2.getWidth());
					table.addCell(pdfc);
				} else {
					PdfPCell pdfc = new PdfPCell(new Paragraph(""));
					pdfc.setColspan(col.getWidth());
					table.addCell(pdfc);
				}
			}
		}

		// add data



		Iterator i = columnReport.getOwnerIds().iterator();
		while (i.hasNext()) {
			Long element = (Long) i.next();
			this.setOwnerId(element);
			Iterator ii = columnReport.getItems().iterator();
			while (ii.hasNext()) {
				Viewable velement = (Viewable) ii.next();
				velement.invokeExporter(this);
			}
		}
		
		//add trail cells
		TrailCellsPDF trails=new TrailCellsPDF(this,columnReport);
		trails.generate();


	}

}