package org.digijava.kernel.ampapi.endpoints.util;

public class AvailableMethod {
	public AvailableMethod() {
		this.ui = false;
	}

	private String name;
	private Boolean ui;
	private String endpoint;
	private String method;
	private String id;
	private FilterType [] filterType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Boolean getUi() {
		return ui;
	}

	public void setUi(Boolean ui) {
		this.ui = ui;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FilterType[] getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType[] filterType) {
		this.filterType = filterType;
	}
	

}