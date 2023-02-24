/**
 * 
 */
package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * @author mihai
 *
 */
public class ProjectCostsBreakdown extends TilesAction {
    private static Logger logger = Logger.getLogger(ProjectCostsBreakdown.class);

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        Long activityId=new Long(request.getParameter("ampActivityId"));
        
        HttpSession session = request.getSession();
        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
        if (session.getAttribute("currentMember") == null) {
            return mapping.findForward("index");
        }
        
        request.setAttribute("ampActivityId",activityId);
        return null;
    }
    
}
