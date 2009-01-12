/**
 * TrailCellsXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class TrailCellsXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public TrailCellsXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public TrailCellsXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		// generate totals:
		ReportData grd = (ReportData) item;


	
		if (grd.getParent() != null) {
			String indent = "";
			if (colId.value == 0)
				for (int k = 0; k < ((ReportData)grd.getParent()).getLevelDepth() - 1; k++)
					indent = indent + Constants.excelIndexString;
//			rowId.inc();
//			colId.reset();
			row=sheet.createRow(rowId.shortValue());
			HSSFCell cell = this.getCell(this.getHighlightedStyle(true));
			
//			introducing the translaton issues
			
			//requirements for translation purposes
			TranslatorWorker translator=TranslatorWorker.getInstance();
			String siteId=this.getMetadata().getSiteId();
			String locale=this.getMetadata().getLocale();
			//String prefix="rep:pop:";
			String translatedName=null;
			try{
				translatedName=TranslatorWorker.translateText(grd.getName(),locale,siteId);
			}catch (WorkerException e)
				{ 
				e.printStackTrace();
				}
			
			
			String modified;
			if(translatedName.compareTo("")==0)
				modified = grd.getName();
			else
				modified = translatedName;
			int pos = modified.indexOf(':'); 
			if (pos >= 0)
				modified = modified.substring(pos + 1);
			
			if (grd.getParent().getParent() == null)
				modified = "TOTAL";
			if (grd.getReportMetadata().isHideActivities()!=null){
				if (grd.getReportMetadata()!=null && grd.getReportMetadata().isHideActivities())
					//cell.setCellValue(indent + modified);
					cell.setCellValue(indent + modified+" ("+grd.getTotalUniqueRows()+")");
				else
					cell.setCellValue(indent + modified+" ("+grd.getTotalUniqueRows()+")");
			}else{
				cell.setCellValue(indent + modified+" ("+grd.getTotalUniqueRows()+")");
			}
			
			makeColSpan(grd.getSourceColsCount().intValue(),false);
			//colId.inc();
			Iterator i = grd.getTrailCells().iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				element.invokeExporter(this);
			}

			colId.reset();
			rowId.inc();
			colId.reset();
		}

	}

}
