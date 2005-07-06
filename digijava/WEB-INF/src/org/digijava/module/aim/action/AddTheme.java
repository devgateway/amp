/*
 * AddTheme.java 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AddThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;


public class AddTheme extends Action {

	private static Logger logger = Logger.getLogger(AddTheme.class);

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

		AddThemeForm addThemeForm = (AddThemeForm) form;

		logger.debug("In add theme");

		ActionErrors errors = new ActionErrors();
		if (addThemeForm.getThemeName() != null) {
			AmpTheme ampTheme = ProgramUtil.getTheme(addThemeForm.getThemeName());
			if (ampTheme == null) {
				session.removeAttribute("ampThemes");
				ampTheme = new AmpTheme();
				ampTheme.setThemeCode(addThemeForm.getThemeCode());
				ampTheme.setName(addThemeForm.getThemeName());
				ampTheme.setType(addThemeForm.getType());
				if (addThemeForm.getDescription() == null
						|| addThemeForm.getDescription().trim().equals("")) {
					ampTheme.setDescription(new String(" "));
				} else {
					ampTheme.setDescription(addThemeForm.getDescription());
				}
				ampTheme.setLanguage(null);
				ampTheme.setVersion(null);

				DbUtil.add(ampTheme);
				logger.debug("Program added");
				return mapping.findForward("added");				
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"error.aim.programNameAlreadyExist"));
				saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.findForward("forward");
	}
}
