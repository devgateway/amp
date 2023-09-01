/*
 * CloseThemeIndicator.java 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CloseThemeIndicator extends Action 
{
    private static Logger logger = Logger.getLogger(CloseThemeIndicator.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception 
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) 
        {
            return mapping.findForward("index");
        } else 
        {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) 
            {
                return mapping.findForward("index");
            }
        }
        
        ThemeForm themeForm = (ThemeForm) form;
        String type = request.getParameter("type");
        
        themeForm.setName(null);
        themeForm.setCode(null);
        themeForm.setType(null);
        themeForm.setCreationDate(null);
        themeForm.setCategory(0);
        themeForm.setNpIndicator(false);
        themeForm.setIndicatorDescription(null);
        themeForm.setPrgIndValues(null);
        themeForm.setThemeId(null);
        if(type!=null && type.equals("overall"))
            return mapping.findForward("overall");
        
        return mapping.findForward("forward");
    }
}
        
