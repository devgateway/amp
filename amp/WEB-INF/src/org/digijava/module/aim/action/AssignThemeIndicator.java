package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AssignThemeIndicator extends Action {

    //private static Logger logger = Logger.getLogger(AssignThemeIndicator.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception 
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) 
        {
            return mapping.findForward("index");
        } 
        else 
        {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) 
            {
                return mapping.findForward("index");
            }
        }
        
        ThemeForm themeForm = (ThemeForm) form;
        Long id = new Long(Long.parseLong(request.getParameter("indicatorId")));
        
        // comment by pcsing due to some doubt about indicators default values
        //ProgramUtil.assignThemeInd(id,themeForm.getSelectTheme());
           
        return mapping.findForward("forward");
    }
}
