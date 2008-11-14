/*
 * AddNewTheme.java 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

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


public class AddNewTheme extends Action 
{
	private static Logger logger = Logger.getLogger(AddNewTheme.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception 
	{
		
	
	Collection themes = new ArrayList();
	ThemeForm themeForm = (ThemeForm) form;
		HttpSession session = request.getSession();
		themes = ProgramUtil.getParentThemes();
		themeForm.setThemes(themes);
		themeForm.setProgramExternalFinancing(Double.valueOf(0));
		themeForm.setProgramInernalFinancing(Double.valueOf(0));
		themeForm.setProgramTotalFinancing(Double.valueOf(0));
		//themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
		return mapping.findForward("forward");
	}
}
