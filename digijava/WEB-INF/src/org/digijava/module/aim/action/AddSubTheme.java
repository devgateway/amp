package org.digijava.module.aim.action;

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

public class AddSubTheme extends Action 
{
	private static Logger logger = Logger.getLogger(AddSubTheme.class);

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
		String event = request.getParameter("event");
		Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		String indname = request.getParameter("indname");
		themeForm.setPrgParentThemeId(id);
		themeForm.setName(indname);

		if(event != null && event.equals("delete"))
		{
			Long rootThId = new Long(Long.parseLong(request.getParameter("rutId")));
			themeForm.setRootId(rootThId);
			ProgramUtil.deleteTheme(id);
			themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(rootThId));
			return mapping.findForward("forward");
		}
		else
		{
			themeForm.setRootId(id);
		}
		
		themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(id));
		return mapping.findForward("forward");
	}
}