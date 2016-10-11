package org.digijava.module.visualization.dbentity;

import java.io.Serializable;

public class AmpDashboardGraph implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8914382426416977447L;
	private Long id;
	private AmpDashboard dashboard;
	private AmpGraph graph;
	private int order;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpDashboard getDashboard() {
		return dashboard;
	}
	public void setDashboard(AmpDashboard dashboard) {
		this.dashboard = dashboard;
	}
	public AmpGraph getGraph() {
		return graph;
	}
	public void setGraph(AmpGraph graph) {
		this.graph = graph;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
}
