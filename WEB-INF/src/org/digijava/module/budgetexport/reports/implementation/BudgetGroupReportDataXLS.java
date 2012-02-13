/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.ReportHeadingsXLS;
import org.dgfoundation.amp.ar.view.xls.TrailCellsXLS;

/**
 * @author alex
 *
 */
public class BudgetGroupReportDataXLS extends GroupReportDataXLS {

	/**
	 * @param parent
	 * @param item
	 */
	public BudgetGroupReportDataXLS(Exporter parent, Viewable item) {
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
	public BudgetGroupReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
			HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
			Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void showHeadings () {
		super.showHeadings();
	}
	
	@Override
	protected void createTrailCellsCase1 () {
	}
	
	@Override
	protected void createTrailCellsCase2 () {
	}
	
	@Override
	protected void invokeChildExporter( Viewable element) {
		element.invokeExporter(this, true);
	}

}
