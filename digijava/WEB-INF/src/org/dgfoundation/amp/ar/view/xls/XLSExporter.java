/**
 * XLSExporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public abstract class XLSExporter extends Exporter {

	protected IntWrapper rowId;

	protected IntWrapper colId;

	protected Long ownerId;

	protected HSSFRow row;

	protected HSSFSheet sheet;

	public void makeColSpan(int size) {
		size--;
		sheet.addMergedRegion(new Region(rowId.intValue(),colId.shortValue(),rowId.intValue(),(short)(colId.shortValue()+size)));
		colId.inc(++size);
	}
	
	public void makeRowSpan(int size) {
		sheet.addMergedRegion(new Region(rowId.intValue(),colId.shortValue(),rowId.intValue()+size,colId.shortValue()));
	}
	
	/**
	 * @return Returns the row.
	 */
	public HSSFRow getRow() {
		return row;
	}

	/**
	 * @param row
	 *            The row to set.
	 */
	public void setRow(HSSFRow row) {
		this.row = row;
	}

	/**
	 * @return Returns the colId.
	 */
	public IntWrapper getColId() {
		return colId;
	}

	/**
	 * @param colId
	 *            The colId to set.
	 */
	public void setColId(IntWrapper colId) {
		this.colId = colId;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            The ownerId to set.
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the rowId.
	 */
	public IntWrapper getRowId() {
		return rowId;
	}

	/**
	 * @param rowId
	 *            The rowId to set.
	 */
	public void setRowId(IntWrapper rowId) {
		this.rowId = rowId;
	}

	/**
	 * @param parent
	 * @param item
	 */
	public XLSExporter(Exporter parent, Viewable item) {
		super(parent, item);
		XLSExporter xlsParent = (XLSExporter) parent;
		this.colId = xlsParent.getColId();
		this.ownerId = xlsParent.getOwnerId();
		this.rowId = xlsParent.getRowId();
		this.row = xlsParent.getRow();
		this.sheet = xlsParent.getSheet();
	}

	public XLSExporter(HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		this.sheet = sheet;
		this.row = row;
		this.rowId = rowId;
		this.colId = colId;
		this.ownerId = ownerId;
		this.item = item;
	}

	/**
	 * @return Returns the sheet.
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            The sheet to set.
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

}
