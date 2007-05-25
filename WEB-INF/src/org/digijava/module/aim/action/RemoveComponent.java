/*
 * RemoveComponent.java
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;

public class RemoveComponent extends Action {
	
	private static Logger logger = Logger.getLogger(ComponentSelected.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		Long comp[] = eaForm.getSelComp();
		for (int i = 0;i < comp.length; i++) {
		    Components temp = new Components();
		    temp.setComponentId(comp[i]);
		    eaForm.getSelectedComponents().remove(temp);
		}
		eaForm.setSelComp(null);
		return mapping.findForward("forward");
	}
}

