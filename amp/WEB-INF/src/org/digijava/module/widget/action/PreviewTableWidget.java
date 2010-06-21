package org.digijava.module.widget.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.widget.form.TableWidgetTeaserForm;

public class PreviewTableWidget extends Action {

    private static Logger logger = Logger.getLogger(PreviewTableWidget.class);

    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TableWidgetTeaserForm tform = (TableWidgetTeaserForm)form;
		
		logger.debug(tform.getTableId());
		
		
		return mapping.findForward("forward");
	}

}
