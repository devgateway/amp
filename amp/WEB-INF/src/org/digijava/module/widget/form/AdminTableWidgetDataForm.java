package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;

/**
 * Widget Data administration form.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetDataForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long widgetId;
	private List<WiColumn> columns;
	private List<WiRow> rows;
	private Integer rowIndex;
	private String tableName;
	private WiTable table;
	private String data[][];
	private Long filterColumnId;
	private Long selectedFilterItemId;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}

	public List<WiColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<WiColumn> columns) {
		this.columns = columns;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public List<WiRow> getRows() {
		return rows;
	}

	public void setRows(List<WiRow> rows) {
		this.rows = rows;
	}
	
	public WiRow getRows(int index) {
		return table.getDataRowByPk(new Long(index));
	}
	
	//This 3 methods are required for old struts to submit directly in these beans, please note differences in names====
	/**
	 * Old style method for struts.
	 * Please do not remove or improve - do not add generic declarations.
	 */
	@SuppressWarnings("unchecked")
	public List getRow() {
		return rows;
	}

	/**
	 * Old style setter for use with struts.
	 * Please do not remove or improve - do not add generic declarations.
	 * @param rows
	 */
	@SuppressWarnings("unchecked")
	public void setRow(List rows) {
		//System.out.println("setRow(List)");
		this.rows = rows;
	}
	
	/**
	 * Old style indexed getter for use with struts.
	 * Please do not remove or improve - do not add generic declarations.
	 * @param index not list index but pk of the row to find in table.
	 * @return table row object coresponding to specified pk.
	 */
	public WiRow getRow(int index) {
//		return (rows==null)?null:rows.get(index);
		return table.getDataRowByPk(new Long(index));
	}

	public void setTable(WiTable table) {
		this.table = table;
	}

	public WiTable getTable() {
		return table;
	}

	public void setData(String data[][]) {
		this.data = data;
	}

	public String[][] getData() {
		return data;
	}

	public void setFilterColumnId(Long filterColumnId) {
		this.filterColumnId = filterColumnId;
	}

	public Long getFilterColumnId() {
		return filterColumnId;
	}

	public void setSelectedFilterItemId(Long selectedFilterItemId) {
		this.selectedFilterItemId = selectedFilterItemId;
	}

	public Long getSelectedFilterItemId() {
		return selectedFilterItemId;
	}

}
