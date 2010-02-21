package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mauricio Coria
 *
 */

public class SelectorSwitchAction extends Action {

	public SelectorSwitchAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		ActionForward forward = null;		
		Object startSelection = request.getAttribute(SelectorAction.ATTRIBUTE_START);
		String session_attribute = mapping.getPath() + "/RETURN_FORWARD";
		if(startSelection != null &&  startSelection instanceof String){			
			session.setAttribute(session_attribute, startSelection);
			forward = this.switchStart(mapping, form, request, response);
			if(forward == null) 
				forward = mapping.findForward(mapping.getParameter());
		}else{
			String startSelectionReturn = (String) session.getAttribute(session_attribute);
			session.removeAttribute(session_attribute);
			request.setAttribute(SelectorAction.ATTRIBUTE_END, true);
			forward = this.switchEnd(mapping, form, request, response);
			if(forward == null)
				forward = mapping.findForward(startSelectionReturn);
		}
		return forward;
	}
	
	public ActionForward switchStart(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{	
		return null;
	}
	
	public ActionForward switchEnd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{	
		return null;
	}
}