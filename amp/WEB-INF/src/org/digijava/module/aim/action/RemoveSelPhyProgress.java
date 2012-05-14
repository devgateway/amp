package org.digijava.module.aim.action;

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.PhysicalProgress;

public class RemoveSelPhyProgress extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (request.getParameter("pid") == null || request.getParameter("cid") == null) 
			return mapping.findForward("forward");
		
		Long compId = new Long(Long.parseLong(request.getParameter("cid")));
		Long pId = new Long(Long.parseLong(request.getParameter("pid")));
			
		if (eaForm.getComponents().getSelectedComponents() != null) {
			Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
			Components comp = null;
			boolean flag = false;
			while (itr.hasNext()) {
				comp = (Components) itr.next();
				if (comp.getComponentId().equals(compId)) {
					flag = true;
					break;
				}
			}
			
			if (comp != null && flag) {
				eaForm.getComponents().getSelectedComponents().remove(comp);
				comp.getPhyProgress().remove(new PhysicalProgress(pId));
				eaForm.getComponents().getSelectedComponents().add(comp);
			}
		}

		return mapping.findForward("forward");
	}
}
