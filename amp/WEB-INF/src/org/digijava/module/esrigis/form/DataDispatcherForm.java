package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.esrigis.helpers.MapFilter;

public class DataDispatcherForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapFilter filter = null;
	private String apiurl;
	private String structures;

	public String getStructures() {
		return structures;
	}

	public void setStructures(String structures) {
		this.structures = structures;
	}

	public MapFilter getFilter() {
		return filter;
	}

	public void setFilter(MapFilter filter) {
		this.filter = filter;
	}
	
	public String getApiurl() {
		return apiurl;
	}
	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}
}
