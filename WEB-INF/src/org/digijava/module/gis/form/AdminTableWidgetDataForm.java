package org.digijava.module.gis.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.dbentity.AmpDaColumn;

/**
 * Widget Data administration form.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetDataForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long widgetId;
	private String[][] matrix;
	private List<AmpDaColumn> columns;
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

	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
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

	public String[] getMatrix(int index){
		return this.matrix[index];
	}
	public String getMatrix(int index1,int index2){
		return this.matrix[index1][index2];
	}
	public void setMatrix(String[] value,int index){
		this.matrix[index]=value;
	}
	public void setMatrix(String value,int index1,int index2){
		this.matrix[index1][index2]=value;
	}

}
