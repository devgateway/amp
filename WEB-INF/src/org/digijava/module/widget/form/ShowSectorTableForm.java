
package org.digijava.module.widget.form;


import org.apache.struts.action.ActionForm;

public class ShowSectorTableForm  extends ActionForm {
	private static final long serialVersionUID = 1L;
	private Long widgetId;
    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

}
