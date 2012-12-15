/**
 * ColumnReportDataPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

import com.lowagie.text.Element;
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
		
		
		ReportData parent=(ReportData)columnReport.getParent();
		
		while (parent.getReportMetadata()==null)
		{
			parent=parent.getParent();
		}
		//when we get to the top of the hierarchy we have access to AmpReports
		
		//requirements for translation purposes
		TranslatorWorker translator=TranslatorWorker.getInstance();
		Long siteId=parent.getReportMetadata().getSiteId();
		String locale=parent.getReportMetadata().getLocale();
		
//		title
		if ((columnReport.getParent() != null)&&(!columnReport.getName().equalsIgnoreCase(columnReport.getParent().getName()))) {
			
			//introducing the translaton issues
			
			//String prefix="rep:pop:";
			String translatedName=null;
			try{
				//translatedName=TranslatorWorker.translate(prefix+columnReport.getName(),locale,siteId);
				
				//AMP-6253  
				String simplename ="";
				if (columnReport.getName().indexOf(":")>0){
					simplename = columnReport.getName().substring(0,columnReport.getName().indexOf(":"));
				}else{
					simplename = columnReport.getName();
				}
				
				
				translatedName=TranslatorWorker.translateText(simplename,locale,siteId);
				if (columnReport.getName().indexOf(":")>0){
					translatedName += columnReport.getName().substring(columnReport.getName().indexOf(":"));
				}
			}catch (WorkerException e)
				{////System.out.println(e);
				
				}
			
			PdfPCell pdfc; 
			if(translatedName.compareTo("")==0)
				pdfc= new PdfPCell(new Paragraph(columnReport.getName(),titleFont));
			else 
				pdfc=new PdfPCell(new Paragraph(translatedName,titleFont));
			pdfc.setColspan(columnReport.getTotalDepth());
			table.addCell(pdfc);
		}
		
		
		// headings
		Font font = new Font(Font.COURIER, 9, Font.BOLD);
		font.setColor(new Color(255,255,255));
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false)  {
			PDFExporter.headingCells=new ArrayList();
			columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		
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
					//element2.setMaxNameDisplayLength(16);
					
					if ( !StringUtils.isEmpty(element2.getName()) ){

						String cellValue=element2.getName(metadata.getHideActivities());
						//this value should be translated
						String translatedCellValue=new String();
						//String prefix="aim:reportBuilder:";
						
						try{
							translatedCellValue=TranslatorWorker.translateText(cellValue,locale,siteId);
						}catch (WorkerException e)
							{
							e.printStackTrace();
							
							}
						PdfPCell pdfc=null;
						font.setSize(9);
						if(translatedCellValue.compareTo("")==0){
						    if(cellValue!=null && cellValue.length() < 18){
							font.setSize(12);
						    }
						    pdfc = new PdfPCell(new Paragraph(cellValue,font));
						   	
						}else{
						    if(translatedCellValue.length() < 18){
							font.setSize(12);
						    }
						    pdfc = new PdfPCell(new Paragraph(translatedCellValue,font));
						   }
						
						pdfc.setHorizontalAlignment(Element.ALIGN_CENTER);
						pdfc.setVerticalAlignment(Element.ALIGN_MIDDLE);
						pdfc.setColspan(element2.getWidth());
						if (rowsp > 1){
							pdfc.setRowspan(rowsp);
						}
						pdfc.setBackgroundColor(new Color(51,102,153));
						//table.addCell(pdfc);
						headingCells.add(pdfc);
					}
				} else {
					/*
					PdfPCell pdfc = new PdfPCell(new Paragraph(""));
					pdfc.setColspan(col.getWidth());
					pdfc.setBackgroundColor(new Color(51,102,153));
					//table.addCell(pdfc);
					headingCells.add(pdfc);
					*/
				}
			}
		}
		}

		// add data

		if (metadata.getHideActivities() == null
				|| metadata.getHideActivities().booleanValue() == false) {

		for (Long element:columnReport.getOwnerIds()) {
			this.setOwnerId(element);
			for (Viewable velement:columnReport.getItems()) {
				velement.invokeExporter(this);
			}
		}
		}
		
		//add trail cells
		TrailCellsPDF trails=new TrailCellsPDF(this,columnReport);
		trails.generate();


	}

}