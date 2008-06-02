package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;

/**
 * Widget Data administration form.
 * @author Irakli Kobiashvili
 *
 */
public class AdminTableWidgetDataForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Long widgetId;

	
	
	public Long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	

}
