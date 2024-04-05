package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewPrintableActivityChart extends Action {
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        Long actId = null;
        if (request.getParameter("ampActivityId") != null) {
            actId = new Long(Long.parseLong(
                    request.getParameter("ampActivityId")));
        }
        request.setAttribute("actId",actId);
        
        if (request.getParameter("cType") != null) {
            char cType = request.getParameter("cType").charAt(0);
            if (cType == 'P') {
                return mapping.findForward("actPerformance");
            } else if (cType == 'R') {
                return mapping.findForward("actRisk");
            }
        }
        return null;
    }
}
