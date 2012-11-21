/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.ReportHeadingsXLS;

/**
 * @author Alex Gartner
 *
 */
public class BudgetReportHeadingsXLS extends ReportHeadingsXLS {

	/**
	 * @param parent
	 * @param item
	 */
	public BudgetReportHeadingsXLS(Exporter parent, Viewable item) {
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
	public BudgetReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet,
			HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
			Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;
		
		// column headings:
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {
	
			rowId.inc();
			colId.reset();
			
			columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
				row = sheet.createRow(rowId.shortValue());
				
				
				this.createHierarchyHeaderCell(0);
				
			//	this.createHeadingBorders(0);
				
				this.createHeaderCells();
				
				rowId.inc();
				colId.reset();
			
		
		}
	}
			
	
	@Override
	protected void createHierarchyHeaderCell (int curDepth) {
		if (curDepth == 0) {
			String path				=  this.getMetadata().getHierarchiesPath();
			String[] pathElements		= path.split("/");
			if ( pathElements != null && pathElements.length > 0 ) {
				for (int i = 0; i < pathElements.length; i++) {
					String elem = pathElements[i];
					if ( elem != null && elem.trim().length() > 0 ) {
						HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
						cell1.setCellValue( elem.trim() );
						colId.inc();
					}
					
				}
			}
		}
		else {
			HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
			cell1.setCellValue("AAA");
		}
	}
	
	protected void createHeaderCells () {
		ArrayList<String> cellValues	= new ArrayList<String>();
		this.prepareHeaderCellsList(null, null, cellValues);
		if ( cellValues != null ) {
			for (String val : cellValues) {
				HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
				cell1.setCellValue( val );
				colId.inc();
			}
		}
	}
	
	protected void prepareHeaderCellsList (List columns, String parentName, List<String> cellValues) {
		if ( columns == null ) {
			ColumnReportData columnReport = (ColumnReportData) item;
			columns			= columnReport.getItems();
		}
		
		if ( columns != null ) {
			Iterator iter	= columns.iterator();
			while (iter.hasNext()) {
				Column tempCol 		= (Column) iter.next();
				String colName		= tempCol.getName(metadata.getHideActivities());
				if ( ! ArConstants.COLUMN_TOTAL.equals(colName) ) {
					List items 	= tempCol.getItems();
					String name	 = (parentName == null)?colName:parentName + " - " + colName;
					if ( items != null && items.size() > 0 && items.get(0) instanceof Column ) {
						this.prepareHeaderCellsList(items, name, cellValues);
					}
					else {
						cellValues.add(name);
					}
				}
			}
		}
		
	}

}
