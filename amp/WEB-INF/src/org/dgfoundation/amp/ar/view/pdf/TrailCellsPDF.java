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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

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
			
		//	for translation purposes
			String siteId=this.getMetadata().getSiteId();
			String locale=this.getMetadata().getLocale();
			String totalsFor="TOTALS for:";
			try{
				totalsFor=TranslatorWorker.translate("rep:pop:totalsFor",locale,siteId);	
			}
			catch (WorkerException e){;}
			
			
			PdfPCell pdfc = new PdfPCell(new Paragraph(totalsFor+" "+grd.getName(),totalFont));
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
