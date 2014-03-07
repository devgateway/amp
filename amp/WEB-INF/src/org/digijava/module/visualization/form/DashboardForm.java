package org.digijava.module.visualization.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.helper.DashboardFilter;

public class DashboardForm extends ActionForm {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private String dashboardName;
	private List<AmpDashboardGraph> dashGraphList;
	private List<AmpGraph> graphList;
	private List<AmpDashboard> dashboardList;
	private AmpDashboard dashboard;
	private Long dashboardId;
	private DashboardFilter filter; 
	private int baseType;
	private int pivot;
	private Boolean showInMenu;
	private Boolean showAcronymForOrgNames;
	private Integer maxYearFilter;
	private Integer minYearFilter;
	private Integer transactionTypeFilter;
	
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	public List<AmpDashboardGraph> getDashGraphList() {
		return dashGraphList;
	}
	public void setDashGraphList(List<AmpDashboardGraph> dashGraphList) {
		this.dashGraphList = dashGraphList;
	}
	public List<AmpGraph> getGraphList() {
		return graphList;
	}
	public void setGraphList(List<AmpGraph> graphList) {
		this.graphList = graphList;
	}
	public List<AmpDashboard> getDashboardList() {
		return dashboardList;
	}
	public void setDashboardList(List<AmpDashboard> dashboardList) {
		this.dashboardList = dashboardList;
	}
	public AmpDashboard getDashboard() {
		return dashboard;
	}
	public void setDashboard(AmpDashboard dashboard) {
		this.dashboard = dashboard;
	}
	public Long getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(Long dashboardId) {
		this.dashboardId = dashboardId;
	}
	public DashboardFilter getFilter() {
		return filter;
	}
	public void setFilter(DashboardFilter filter) {
		this.filter = filter;
	}
	public int getBaseType() {
		return baseType;
	}
	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}
	public Boolean getShowInMenu() {
		return showInMenu;
	}
	public void setShowInMenu(Boolean showInMenu) {
		this.showInMenu = showInMenu;
	}
	public int getPivot() {
		return pivot;
	}
	public void setPivot(int pivot) {
		this.pivot = pivot;
	}
	public Integer getMaxYearFilter() {
		return maxYearFilter;
	}
	public void setMaxYearFilter(Integer maxYearFilter) {
		this.maxYearFilter = maxYearFilter;
	}
	public Integer getMinYearFilter() {
		return minYearFilter;
	}
	public void setMinYearFilter(Integer minYearFilter) {
		this.minYearFilter = minYearFilter;
	}
	public Integer getTransactionTypeFilter() {
		return transactionTypeFilter;
	}
	public void setTransactionTypeFilter(Integer transactionTypeFilter) {
		this.transactionTypeFilter = transactionTypeFilter;
	}
	public Boolean getShowAcronymForOrgNames() {
		return showAcronymForOrgNames;
	}
	public void setShowAcronymForOrgNames(Boolean showAcronymForOrgNames) {
		this.showAcronymForOrgNames = showAcronymForOrgNames;
	}
	
}
