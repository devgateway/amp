package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.esrigis.helpers.MapFilter;

public class MapHelpForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapFilter filter = null;

	public MapFilter getFilter() {
		return filter;
	}

	public void setFilter(MapFilter filter) {
		this.filter = filter;
	}

}
