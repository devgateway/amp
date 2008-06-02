package org.digijava.module.gis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.TableWidgetTeaserForm;

public class PreviewTableWidget extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TableWidgetTeaserForm tform = (TableWidgetTeaserForm)form;
		
		HttpSession session = request.getSession();
		
		
		
		return mapping.findForward("forward");
	}

}
