/*
 * DeleteTheme.java 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AddThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class DeleteTheme extends Action {

    private static Logger logger = Logger.getLogger(DeleteTheme.class);

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
        AddThemeForm deleteThemeForm = (AddThemeForm) form;

        logger.debug("In delete theme");

        if (deleteThemeForm.getThemeId() != null) {
            if (session.getAttribute("ampThemes") != null) {
                session.removeAttribute("ampThemes");
            }

            AmpTheme ampTheme = ProgramUtil
                    .getThemeById(deleteThemeForm.getThemeId());
            DbUtil.deleteTheme(ampTheme);
            deleteThemeForm.setThemeId(null);
            logger.debug("Theme deleted");
        }
        return mapping.findForward("forward");
    }
}
