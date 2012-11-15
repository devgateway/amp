/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.budgetexport.util.MappingEncoder;

/**
 * @author Alex
 *
 */
public class PlainTrailCellsXLS extends TrailCellsXLS {

	/**
	 * @param parent
	 * @param item
	 */
	public PlainTrailCellsXLS(Exporter parent, Viewable item) {
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
	public PlainTrailCellsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generate() {
		// generate totals:
		ReportData rd = (ReportData) item;
	
		row=sheet.createRow(rowId.shortValue());
			
		this.createFirstCells();
		
		this.createAmountCells();
			
		colId.reset();
		rowId.inc();
		colId.reset();
	}
	
	protected void createFirstCells () {
		
		Integer count	= this.getHierarchiesCount(((ReportData)this.item).getName(), (ReportData)this.item, 0);

		HSSFCell cell=this.getCell(this.getHierarchyOtherStyle());
		cell.setCellValue("");
		makeColSpan(count, true);
	}
	
	private Integer getHierarchiesCount(String baseReportName, ReportData rd, Integer count){
		
		if (rd.getItems().size() > 0 && rd.getItem(0) instanceof ReportData){
			ReportData reportData = (ReportData)rd.getItem(0);
			if (reportData.getName().equals(baseReportName)){
				return getHierarchiesCount(baseReportName, (ReportData)rd.getItem(0), count);	
			}else{
				return getHierarchiesCount(baseReportName, (ReportData)rd.getItem(0), count+1);
			}
		}else{
			return count;
		}
	}
	
	protected void createAmountCells () {
		ReportData grd = (ReportData) item;
		Iterator i = grd.getTrailCells().iterator();
		//the first column will be under the hierarchy cell
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if (element!=null ){
				element.invokeExporter(this);
			}else {
				HSSFCell cell2=this.getCell(this.getHierarchyOtherStyle());
				cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell2.setCellValue("");
				colId.inc();
			}
		
		}
		
		colId.reset();
	}
}
