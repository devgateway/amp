/**
 * TrailCellsPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.awt.Color;
import java.util.Iterator;

import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

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
		Font totalFont = new Font(Font.COURIER, 10, Font.BOLD);
		
		if(grd.getParent()!=null) {
			
			ReportData parent=(ReportData)grd.getParent();
			while (parent.getReportMetadata()==null)
			{
				parent=parent.getParent();
			}
			//when we get to the top of the hierarchy we have access to AmpReports
			
			//requirements for translation purposes
			//this is the translation part for the rep:pop:totalsfor and the rep:pop:($donorName} part
			TranslatorWorker translator=TranslatorWorker.getInstance();
			String siteId=parent.getReportMetadata().getSiteId();
			String locale=parent.getReportMetadata().getLocale();
			String totalsFor="Totals For";
			String translatedName=grd.getName();
			try{
				//TODO TRN: no record for this key. its all right to have key here but it is better to replace with default text
				totalsFor=TranslatorWorker.translateText(totalsFor,locale,siteId);
				//String namePrefix="rep:pop:";
				translatedName=TranslatorWorker.translateText(grd.getName(),locale,siteId );
			}
			catch (WorkerException e){;}
			String result;
			
			//create the actual output string for the totals line
			result=totalsFor+": ";
			
			if(translatedName.compareTo("")==0 )
				result+=grd.getName();
			else result+=translatedName;
				
			PdfPCell pdfc = new PdfPCell(new Paragraph(result+" ("+grd.getTotalUniqueRows()+")",totalFont));
			
			pdfc.setColspan(grd.getSourceColsCount().intValue());
			currentBackColor=new  Color(235,235,235);
			
			pdfc.setBackgroundColor(currentBackColor);
			table.addCell(pdfc);
			
			
			Iterator i=grd.getTrailCells().iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				if (element!=null)
					element.invokeExporter(this);
			}
			
			currentBackColor=null;
		}
	

	}

}
