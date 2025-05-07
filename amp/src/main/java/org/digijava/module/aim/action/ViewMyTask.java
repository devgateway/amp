/*
 * Created on 17/10/2005
 * @author akashs
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.TeamMember;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ViewMyTask extends Action {

    private static Logger logger = Logger.getLogger(ViewMyTask.class);
    private ServletContext ampContext = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
        
        //  If user is not logged in, forward him to the home page
        if(teamMember == null)
            return mapping.findForward("index");
        
        if (teamMember.getTeamHead()) { // // checking user is a TL of a working team

            // If approval process is set 'off' by the admin then forward him to his portfolio
            ampContext = getServlet().getServletContext();
            if ("off".equals(ampContext.getAttribute("approvalStatus")))
                return mapping.findForward("view");     
            
            String showTask = request.getParameter("showTask");
            logger.debug("request.getParameter is : " + showTask);
            
            if (showTask == null || showTask.equals(""))
                return mapping.findForward("view");
            else if ("showTask".equals(showTask))
                return mapping.findForward("forward");
        }
        else
            // If user is not a TL of a working team then forward him to his portfolio
            return mapping.findForward("view");
        
        return null;
    }
}
