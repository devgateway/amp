package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.annotations.reports.Identificator;

public class AmpColumns  implements Serializable, Comparable
{
	@Identificator
	private Long columnId ;
	private String columnName ;
	private String columnNameTrimmed;
	private String aliasName;
	private Set reports;
	private String cellType;
	private String extractorView;
	private Set filters;
	private String tokenExpression;
	
	// header calculations
	private String totalExpression;
	private Boolean showRowCalculations;
	
	private String relatedContentPersisterClass;
	private String description;
	/**
	 * true if the column data is needed in order for correct filtering to be applied
	 * @see http://bugs.digijava.org/jira/browse/AMP-3454?focusedCommentId=39811#action_39811
	 */
	private Boolean filterRetrievable;
	


    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

	public Boolean getFilterRetrievable() {
		return filterRetrievable;
	}
	public void setFilterRetrievable(Boolean retrieveIfFiltered) {
		this.filterRetrievable = retrieveIfFiltered;
	}
	public String getRelatedContentPersisterClass() {
	    return relatedContentPersisterClass;
	}
	public void setRelatedContentPersisterClass(String relatedContentPersisterClass) {
	    this.relatedContentPersisterClass = relatedContentPersisterClass;
	}
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
	public String getColumnNameTrimmed() {
		return columnName.replaceAll(" ", "");
	}
	public void setColumnNameTrimmed(String columnNameTrimmed) {
		this.columnNameTrimmed = columnNameTrimmed;
	}
	
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof AmpColumns))
			return -1;
		AmpColumns auxColumn=(AmpColumns) o;
		return this.getColumnName().compareTo(auxColumn.getColumnName());
	}
	
	public String toString(){
		return columnName;
	}
	public Set getFilters() {
		return filters;
	}
	public void setFilters(Set filters) {
		this.filters = filters;
	}
	public String getTokenExpression() {
		return tokenExpression;
	}
	public void setTokenExpression(String tokenExpression) {
		this.tokenExpression = tokenExpression;
	}

	public String getTotalExpression() {
		return totalExpression;
	}

	public void setTotalExpression(String totalExpression) {
		this.totalExpression = totalExpression;
	}

	public Boolean isShowRowCalculations() {
		return showRowCalculations;
	}

	public void setShowRowCalculations(Boolean showRowCalculations) {
		this.showRowCalculations = showRowCalculations;
	}
	
}
