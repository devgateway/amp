package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetDefaultFlag extends Action {
    
    private static Logger logger = Logger.getLogger(SetDefaultFlag.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        String temp = request.getParameter("id");
        if (temp != null) {
            try {
                long tl = Long.parseLong(temp);
                Long id = new Long(tl);
                FeaturesUtil.setDefaultFlag(id);
                
                ServletContext ctx = getServlet().getServletContext();
                ctx.setAttribute(Constants.DEF_FLAG_EXIST,new Boolean(true));
                
            } catch (NumberFormatException nfe) {
                 logger.error("trying to parse " + temp + " to long");
            }
        }
        return mapping.findForward("forward");
        
    }
    
}
