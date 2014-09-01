package org.digijava.kernel.ampapi.endpoints.reports;

import org.dgfoundation.amp.ar.AmpARFilter;

public class JSONTab {
	private long id;
	private String name;
	private Boolean visible;
	private AmpARFilter filter;

	public JSONTab(Long ampReportId, String name, boolean visible) {
		this.id = ampReportId;
		this.name = name;
		this.visible = visible;
	}
	public JSONTab() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AmpARFilter getFilter() {
		return filter;
	}
	public void setFilter(AmpARFilter filter) {
		this.filter = filter;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}
