package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ComponentsForm;
import org.digijava.module.aim.util.ComponentsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ComponentIndicatorManager extends Action 
{
    private static Logger logger = Logger.getLogger(ComponentIndicatorManager.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception 
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        ComponentsForm compForm = (ComponentsForm) form;
        
        compForm.setCompIndicators(ComponentsUtil.getAllComponentIndicators());
        
        return mapping.findForward("forward");
    }
}
