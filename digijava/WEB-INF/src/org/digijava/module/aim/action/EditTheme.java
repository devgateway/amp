/*
 * EditTheme.java 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
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
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;


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

		ThemeForm themeForm = (ThemeForm) form;
		String event = request.getParameter("event");
		Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		
		if (event != null && event.equals("edit"))
		{
			AmpTheme ampTheme = ProgramUtil.getTheme(id);
			themeForm.setThemeId(id);
			themeForm.setProgramName(ampTheme.getName());
			themeForm.setProgramCode(ampTheme.getThemeCode());
			themeForm.setProgramDescription(ampTheme.getDescription());
			themeForm.setProgramType(ampTheme.getType());
			if(ampTheme.getParentThemeId() != null)
				themeForm.setPrgParentThemeId(ampTheme.getParentThemeId().getAmpThemeId());
			else
				themeForm.setPrgParentThemeId(null);
			return mapping.findForward("editProgram");
		}
		else if (event != null && event.equals("update"))
		{
			AmpTheme ampTheme = new AmpTheme();
			ampTheme = ProgramUtil.getTheme(id);
			ampTheme.setName(themeForm.getProgramName());
			ampTheme.setThemeCode(themeForm.getProgramCode());
			ampTheme.setDescription(themeForm.getProgramDescription());
			ampTheme.setType(themeForm.getProgramType());
			if(ampTheme.getParentThemeId() != null)
				ampTheme.setParentThemeId(ampTheme.getParentThemeId());
			else
				ampTheme.setParentThemeId(null);
			ampTheme.setLanguage(null);
			ampTheme.setVersion(null);
			DbUtil.update(ampTheme);
			return mapping.findForward("forward");
		}
		else if (event != null && event.equals("delete"))
		{
			Collection colTheme = getRelatedThemes(id);
			Iterator colThemeItr = colTheme.iterator();
			while(colThemeItr.hasNext())
			{
				AmpTheme tempTheme = (AmpTheme) colThemeItr.next();
				DbUtil.delete(tempTheme);			
			}
			return mapping.findForward("forward");
		}
		else
			return mapping.findForward("forward");
	}
	
	Collection temp = new ArrayList();
	
	public Collection getRelatedThemes(Long id)
	{
		AmpTheme ampTheme = new AmpTheme();
		ampTheme = ProgramUtil.getTheme(id);
		Collection themeCol = ProgramUtil.getSubThemes(id);
		temp.add(ampTheme);
		if(!themeCol.isEmpty())
		{
			Iterator itr = themeCol.iterator();
			AmpTheme tempTheme = new AmpTheme();
			while(itr.hasNext())
			{
				tempTheme = (AmpTheme) itr.next();
				getRelatedThemes(tempTheme.getAmpThemeId());
			}
		}
		return temp;
	}
}
