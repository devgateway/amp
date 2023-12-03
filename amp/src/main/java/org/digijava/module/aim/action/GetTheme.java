/*
 * GetTheme.java
 

package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AddThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;


public class GetTheme extends Action {

    private static Logger logger = Logger.getLogger(GetTheme.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        AddThemeForm themeForm = (AddThemeForm) form;

        logger.debug("In get theme");
        String action = request.getParameter("action");

        if (request.getParameter("id") != null) {

            Long themeId = new Long(Long.parseLong(request.getParameter("id")));
            AmpTheme ampTheme = ProgramUtil.getThemeObject(themeId);
            themeForm.setThemeId(themeId);
            themeForm.setThemeCode(ampTheme.getThemeCode());
            themeForm.setThemeName(ampTheme.getName());
            themeForm.setDescription(ampTheme.getDescription());
            if (ampTheme.getTypeCategoryValue() != null)
                themeForm.setType( ampTheme.getTypeCategoryValue().getId() );
            logger.debug("values set.");
        }

        if (action != null && action.equals("edit")) {
            return mapping.findForward("editTheme");
        } else if (action != null && action.equals("delete")) {
            Iterator itr = DbUtil.getActivityTheme(themeForm.getThemeId())
                    .iterator();
            if (itr.hasNext()) {
                themeForm.setFlag("activityReferences");
            } else {
                themeForm.setFlag("delete");
            }

            return mapping.findForward("deleteTheme");
        } else if (action == null) {
            return mapping.findForward("forward");
        } else {
            return null;
        }
    }
}
*/
