
package org.digijava.module.aim.form ;

import java.util.Collection ;
import org.apache.struts.action.ActionForm ;

public class AdvancedReportForm extends ActionForm 
{
	private Collection ampColumns = null; // Contains the columns got from the DB
	private Long selectedColumns[] = null; // list of columns after selecting.
	private Long removeColumns[] = null; // list of columns after selecting.
	private Long columnId;
	private Collection addedColumns = null; // contains the columns that have been added.
	private boolean isAdd = false;
	private String step = null;
	private Collection finalData = null;
	private String perspective ;
	private String perspectiveFilter ;
	private String reportTitle = "";
	private String reportDescription = "";
	
	public String getPerspectiveFilter() {
		return perspectiveFilter;
	}
	public void setPerspectiveFilter(String perspectiveFilter) {
		this.perspectiveFilter = perspectiveFilter;
	}
	public String getPerspective() {
		return perspective;
	}
	public void setPerspective(String perspective) {
		this.perspective = perspective;
	}
	public Collection getFinalData() {
		return finalData;
	}
	public void setFinalData(Collection finalData) {
		this.finalData = finalData;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public Collection getAddedColumns() {
		return addedColumns;
	}
	public void setAddedColumns(Collection addedColumns) {
		this.addedColumns = addedColumns;
	}
	public Collection getAmpColumns() {
		return ampColumns;
	}
	public void setAmpColumns(Collection ampColumns) {
		this.ampColumns = ampColumns;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public Long[] getSelectedColumns() {
		return selectedColumns;
	}
	public void setSelectedColumns(Long[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}
	
	public Long[] getRemoveColumns() {
		return removeColumns;
	}
	public void setRemoveColumns(Long[] removeColumns) {
		this.removeColumns = removeColumns;
	}
	public String getReportDescription() {
		return reportDescription;
	}
	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
} //		End of Class

