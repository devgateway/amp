package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;

/**
 * Shows the index page
 * 
 * A user cannot login to AMP if he has already logged in, using the same session.
 * If the user is logged in and then if he tries to access the index page or login page
 * he will be automatically redirected to the MyDesktop page
 * 
 */
public class ViewAmp extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        /*
         * If the user information is present in the current session, then forward to the 
         * MyDesktop page, when he tries to login again in that same session. 
         */
		HttpSession session = request.getSession();
    	String siteAdmin = (String) session.getAttribute("ampAdmin");
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (tm != null && tm.getTeamId() != null && tm.getTeamId().longValue() > 0) {
			String fwdUrl = "viewMyDesktop.do";
			response.sendRedirect(fwdUrl);        		
		} else if (siteAdmin != null && "yes".equals(siteAdmin)) {
			String fwdUrl = "admin.do";
			response.sendRedirect(fwdUrl);        		
    	}		
		
		return mapping.findForward("forward");
	}
}
