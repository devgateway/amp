/**
 * ColumnReportDataXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class ColumnReportDataXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public ColumnReportDataXLS(Exporter parent, Viewable item) {
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
	public ColumnReportDataXLS(HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;
		rowId.inc();
		colId.reset();
		// title:
		if (columnReport.getParent() != null) {
			HSSFRow row = sheet.createRow(rowId.shortValue());
			HSSFCell cell = row.createCell(colId.shortValue());
			cell.setCellValue(columnReport.getName());
			makeColSpan(columnReport.getTotalDepth());
			rowId.inc();
			colId.reset();
		}

		// column headings:

		for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++) {
			row = sheet.createRow(rowId.shortValue());
			Iterator i = columnReport.getItems().iterator();
			while (i.hasNext()) {
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				if (ii.hasNext())
					while (ii.hasNext()) {
						Column element2 = (Column) ii.next();
						HSSFCell cell = row.createCell(colId.shortValue());
						cell.setCellValue(element2.getName());
						// System.out.println("["+rowId.intValue()+"]["+colId.intValue()+"]
						// depth="+curDepth+" "+element2.getName());
						// create spanning
						// if(rowsp>1) makeRowSpan(rowsp);

						if (element2.getWidth() > 1)
							makeColSpan(element2.getWidth());
						else
							colId.inc();

					}
				else {
					HSSFCell cell = row.createCell(colId.shortValue());
					cell.setCellValue(" ");
					makeColSpan(col.getWidth());
				}
			}
			rowId.inc();
			colId.reset();
		}

		// add data
		if (metadata.getHideActivities() == null
				|| metadata.getHideActivities().booleanValue() == false) {
			Iterator i = columnReport.getOwnerIds().iterator();
			while (i.hasNext()) {
				Long element = (Long) i.next();
				this.setOwnerId(element);
				row = sheet.createRow(rowId.shortValue());
				Iterator ii = columnReport.getItems().iterator();
				while (ii.hasNext()) {
					Viewable velement = (Viewable) ii.next();
					velement.invokeExporter(this);
				}
				rowId.inc();
				colId.reset();
			}
		}

		// add trail cells
		TrailCellsXLS trails = new TrailCellsXLS(this, columnReport);
		trails.generate();

	}

}
