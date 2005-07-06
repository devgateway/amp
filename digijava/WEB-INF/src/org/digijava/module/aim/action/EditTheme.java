/*
 * EditTheme.java 
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
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AddThemeForm;
import org.digijava.module.aim.util.DbUtil;


public class EditTheme extends Action {

	private static Logger logger = Logger.getLogger(EditTheme.class);

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

		AddThemeForm editThemeForm = (AddThemeForm) form;

		logger.debug("In edit theme");

		if (editThemeForm.getThemeId() != null) {
			if (session.getAttribute("ampThemes") != null)
				session.removeAttribute("ampThemes");

			AmpTheme ampTheme = new AmpTheme();
			ampTheme.setAmpThemeId(editThemeForm.getThemeId());
			ampTheme.setThemeCode(editThemeForm.getThemeCode());
			ampTheme.setName(editThemeForm.getThemeName());
			ampTheme.setType(editThemeForm.getType());
			if (editThemeForm.getDescription() == null
					|| editThemeForm.getDescription().trim().equals("")) {
				ampTheme.setDescription(new String(" "));
			} else {
				ampTheme.setDescription(editThemeForm.getDescription());
			}

			DbUtil.update(ampTheme);
			logger.debug("Theme updated");
		}
		return mapping.findForward("forward");
	}
}
