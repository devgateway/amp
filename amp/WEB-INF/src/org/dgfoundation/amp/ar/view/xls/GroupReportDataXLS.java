/**
 * GroupReportDataXLS.java
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
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;


/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public class GroupReportDataXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public GroupReportDataXLS(Exporter parent, Viewable item) {
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
	public GroupReportDataXLS(HSSFWorkbook wb,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
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
		GroupReportData grd = (GroupReportData) item;
	
		
		//show Headings:		
		ReportHeadingsXLS headings=new ReportHeadingsXLS(this,grd.getFirstColumnReport());
		headings.setAutoSize(this.isAutoSize());
		headings.generate();

		//		trail cells:
		if ((grd != null) && ((GroupReportData)grd.getParent() != null) && ((GroupReportData)grd.getParent()).getLevelDepth() != 0){
			TrailCellsXLS trails2=new TrailCellsXLS(this,grd);
			trails2.generate();
		}
		//iterate the data
		Iterator i = grd.getItems().iterator();
		while (i.hasNext()) {
			Viewable element = (Viewable) i.next();
			element.invokeExporter(this);
		}
		
		/*rowId.inc();
		colId.reset();
		
		TrailCellsXLS trails=new TrailCellsXLS(this,grd);
		trails.generate();
		*/

		// add an empty row
		//HSSFRow row=sheet.createRow(rowId.shortValue());
		//HSSFCell cell=this.getRegularCell(row);
		//cell.setCellValue("xx");
		//makeColSpan(grd.getTotalDepth());

		if ((grd.getParent() == null) || ((GroupReportData)grd.getParent()).getLevelDepth() == 0){
			TrailCellsXLS trails2=new TrailCellsXLS(this,grd);
			trails2.generate();
		}
		
	}

}
