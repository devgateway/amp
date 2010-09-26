/**
 * TrailCellsXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
	public TrailCellsXLS(XSSFWorkbook wb ,XSSFSheet sheet, XSSFRow row, IntWrapper rowId,
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
		
			XSSFCellStyle hierarchyStyle;
			if(grd.getLevelDepth()==2) 
				hierarchyStyle = this.getHierarchyLevel1Style();
			else hierarchyStyle=this.getHierarchyOtherStyle();
			
			
			XSSFCell cell = this.getCell(hierarchyStyle);
			
			String modifiedName = (grd.getName()==null)?"":grd.getName();

			int pos = modifiedName.indexOf(':'); 
			if (pos >= 0)
				modifiedName = modifiedName.substring(pos + 1);
			
			//requirements for translation purposes
			String siteId=this.getMetadata().getSiteId();
			String locale=this.getMetadata().getLocale();
			//Translate already truncated string (AMP-5669)
			try {
				modifiedName = TranslatorWorker.translateText(modifiedName, locale, siteId);
			} catch (WorkerException e) {
				//We should never get here!
				logger.warn("Error translating trial cell value, using value without translation. See error below.");
				logger.error(e);
			}
			
			if (grd.getParent().getParent() == null)
				modifiedName = "TOTAL";
			
			if (grd.getReportMetadata().isHideActivities()!=null){
				if (grd.getReportMetadata()!=null && grd.getReportMetadata().isHideActivities())
					//cell.setCellValue(indent + modified);
					cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
				else
					cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
			}else{
				cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
			}
			colId.inc();
			Iterator i = grd.getTrailCells().iterator();
			//the first column will be under the hierarchy cell
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				if (element!=null){
					element.invokeExporter(this);
				}else if (!metadata.getHideActivities()){
					XSSFCell cell2=this.getCell(hierarchyStyle);
					cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell2.setCellValue("");
					colId.inc();
				}
			
			}
			colId.reset();
			rowId.inc();
			colId.reset();
		}

	}

}
