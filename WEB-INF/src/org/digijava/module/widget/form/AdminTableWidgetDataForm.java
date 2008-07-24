package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.table.DaRow;

/**
 * Widget Data administration form.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetDataForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long widgetId;
	private List<AmpDaColumn> columns;
	private List<DaRow> rows;
	private Integer rowIndex;

	private String tableName;
	
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

	public List<AmpDaColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<AmpDaColumn> columns) {
		this.columns = columns;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public List<DaRow> getRows() {
		return rows;
	}

	public void setRows(List<DaRow> rows) {
		this.rows = rows;
	}
	
	public DaRow getRows(int index) {
		//System.out.println("getRows(int index)");
		//if (this.rows==null) //System.out.println("Ooo, rows is NULL !!!");
		return (rows==null)?null:rows.get(index);
	}
	
	//This 3 methods are required for old struts to submit directly in these beans, please note differences in names====
	public List getRow() {
		return rows;
	}

	public void setRow(List rows) {
		this.rows = rows;
	}
	public DaRow getRow(int index) {
		//System.out.println("getRows(int index)");
		//if (this.rows==null) //System.out.println("Ooo, rows is NULL !!!");
		return (rows==null)?null:rows.get(index);
	}

}
