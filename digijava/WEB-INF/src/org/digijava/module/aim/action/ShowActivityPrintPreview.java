/*
 * ShowActivityPrintPreview.java
 * Created : 24-May-2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;

public class ShowActivityPrintPreview extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		return mapping.findForward("forward");
	}
}