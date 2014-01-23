package org.digijava.module.visualization.dbentity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass (displayName = "Dashboard")
public class AmpDashboard implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8072077407917128711L;
	private Long id;
	@TranslatableField
	private String name;
	private List<AmpDashboardGraph> graphList;
	private int baseType;
	private int pivot;
	private Boolean showInMenu;
	private Integer maxYearFilter;
	private Integer minYearFilter;
	private Integer transactionTypeFilter;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AmpDashboardGraph> getGraphList() {
		return graphList;
	}
	public void setGraphList(List<AmpDashboardGraph> graphList) {
		this.graphList = graphList;
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
	
}
