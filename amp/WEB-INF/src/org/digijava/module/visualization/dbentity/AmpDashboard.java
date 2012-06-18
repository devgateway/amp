package org.digijava.module.visualization.dbentity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


public class AmpDashboard implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8072077407917128711L;
	private Long id;
	private String name;
	private List<AmpDashboardGraph> graphList;
	
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
	
}
