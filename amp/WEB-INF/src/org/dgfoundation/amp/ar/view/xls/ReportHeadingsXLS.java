/**
 * ReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mihai
 * @since 22.06.2007
 *
 */
public class ReportHeadingsXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public ReportHeadingsXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public ReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;
//		requirements for translation purposes
		Long siteId= new Long(this.getMetadata().getSiteId());
		String locale=this.getMetadata().getLocale();
		boolean fundingReached = false;
		
		// column headings:
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {

		rowId.inc();
		colId.reset();
		
		columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++) {
			row = sheet.createRow(rowId.shortValue());
//			if (rowId.value == 8){
//				row.setHeight((short)(3*row.getHeight()));
//				//System.out.println((short)(3*row.getHeight()));
//			}
			Iterator i = columnReport.getItems().iterator();
			//int cellCount = 0;
			while (i.hasNext()) {
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				if (ii.hasNext()){
					while (ii.hasNext()) {
						//cellCount++;
						Column element2 = (Column) ii.next();
						HSSFCell cell =  this.getCell(row,this.getHighlightedStyle(true));
						String cellValue=element2.getName(metadata.getHideActivities());
						
						//if (rowId.value == 8){
						if (rowId.value < 10 && cellValue.length() > 0){
							//here we set the cell width
							if (sheet.getColumnWidth((short)colId.value)<cellValue.length()*256){
								short val;
								if ((short)((cellValue.length())*256) < 2560)
									val = 2560; //at least 10 chars
								else
									val = (short)((cellValue.length())*256);
								if (rowId.value == 6 && !fundingReached){
									if (cellValue == "Funding")
										fundingReached = true;
									else
										val *=3;
								}
								if (colId.value == 0)
									sheet.setColumnWidth((short)colId.value, (short)10240);
								else
									sheet.setColumnWidth((short)colId.value, val);
							}

							
						}
						//this value should be translated
						String translatedCellValue=new String();
						//String prefix="aim:reportBuilder:";
						
						try{
							translatedCellValue=TranslatorWorker.translateText(cellValue,locale,siteId);
						}catch (WorkerException e)
						{
							e.printStackTrace();
						}
						
						
						if(translatedCellValue.compareTo("")==0)
							cell.setCellValue(cellValue);
						else 
							cell.setCellValue(translatedCellValue);
						
						// ////System.out.println("["+rowId.intValue()+"]["+colId.intValue()+"]
						// depth="+curDepth+" "+element2.getName());
						// create spanning
						// if(rowsp>1) makeRowSpan(rowsp);
						
						if (element2.getWidth() > 1)
							makeColSpan(element2.getWidth(),true);
						else
							colId.inc();
						
					}
				}
				else {
					HSSFCell cell =  this.getCell(row,this.getHighlightedStyle(true));
					cell.setCellValue(" ");
					makeColSpan(col.getWidth(),true);
				}
			}
			rowId.inc();
			colId.reset();
		}
		}

	}

}
