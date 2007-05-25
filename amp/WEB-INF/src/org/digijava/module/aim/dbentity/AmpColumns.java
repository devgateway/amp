package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;
//import java.io.Serializable;

public class AmpColumns  implements Serializable
{

	private Long columnId ;
	private String columnName ;
	private String aliasName;
	private Set reports;
	private String cellType;
	private String extractorView;
	

	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Set getReports() {
		return reports;
	}
	public void setReports(Set reports) {
		this.reports = reports;
	}
	/**
	 * @return Returns the cellType.
	 */
	public String getCellType() {
		return cellType;
	}
	/**
	 * @param cellType The cellType to set.
	 */
	public void setCellType(String extractorClass) {
		this.cellType = extractorClass;
	}
	/**
	 * @return Returns the extractorView.
	 */
	public String getExtractorView() {
		return extractorView;
	}
	/**
	 * @param extractorView The extractorView to set.
	 */
	public void setExtractorView(String extractorView) {
		this.extractorView = extractorView;
	}
}
