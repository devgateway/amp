/*
 * ThemeManager.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.ProgramUtil;


public class ThemeManager extends Action {

	private static Logger logger = Logger.getLogger(ThemeManager.class);

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

		final int NUM_RECORDS = 10;

		Collection themes = new ArrayList();
		ThemeForm themeForm = (ThemeForm) form;
		int page = 0;

		logger.debug("In theme manager");

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}

		Collection ampThemes = (Collection) session.getAttribute("ampThemes");
		if (ampThemes == null) {
			ampThemes = ProgramUtil.getAllThemes();
			session.setAttribute("ampThemes", ampThemes);
		}

		int stIndex = ((page - 1) * NUM_RECORDS) + 1;
		int edIndex = page * NUM_RECORDS;
		if (edIndex > ampThemes.size()) {
			edIndex = ampThemes.size();
		}

		Vector vect = new Vector();
		vect.addAll(ampThemes);

		for (int i = (stIndex - 1); i < edIndex; i++) {
			themes.add(vect.get(i));
		}

		int numPages = ampThemes.size() / NUM_RECORDS;
		numPages += (ampThemes.size() % NUM_RECORDS != 0) ? 1 : 0;

		Collection pages = null;

		if (numPages > 1) {
			pages = new ArrayList();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		themeForm.setThemes(themes);
		themeForm.setPages(pages);

		logger.debug("Theme manager returning");
		return mapping.findForward("forward");
	}
}
