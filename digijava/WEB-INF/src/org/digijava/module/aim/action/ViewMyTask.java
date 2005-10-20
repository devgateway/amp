/*
 * Created on 17/10/2005
 * @author akashs
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;


public class ViewMyTask extends Action {

	private static Logger logger = Logger.getLogger(ViewMyTask.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		
		//	If user is not logged in, forward him to the home page
		if(teamMember == null)
			return mapping.findForward("index");
		
		String showTask = request.getParameter("showTask");
		logger.debug("request.getParameter is : " + showTask);
		
		if (showTask == null || showTask.equals(""))
			return mapping.findForward("view");
		else if ("showTask".equals(showTask))
			return mapping.findForward("forward");
		
		return null;
	}
}
