/*
 * AddThemeIndicator.java 
 */

package org.digijava.module.aim.action;

import java.util.Set;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.DbUtil;

public class AddThemeIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(AddThemeIndicator.class);

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
		Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		String event = request.getParameter("event");
		themeForm.setThemeId(id);
		AmpTheme ampTheme = ProgramUtil.getTheme(id);
		
		if(event!=null && event.equals("save"))
		{
			Set ampThemeSet = new HashSet();
			Set ampIndSet = new HashSet();
			ampThemeSet.add(ampTheme);
			AmpMEIndicators ampMEInd = new AmpMEIndicators();
			ampMEInd.setValueType(themeForm.getValueType());
			ampMEInd.setCode(themeForm.getCode());
			ampMEInd.setName(themeForm.getName());
			ampMEInd.setType(themeForm.getType());
			ampMEInd.setDescription(themeForm.getIndicatorDescription());
			ampMEInd.setCreationDate(DateConversion.getDate(themeForm.getCreationDate()));
			ampMEInd.setIndicatorCategory(themeForm.getCategory());
			ampMEInd.setNpIndicator(themeForm.isNpIndicator());
			ampMEInd.setThemes(ampThemeSet);
			DbUtil.add(ampMEInd);
			//ampMEInd.setThemes(ampThemeSet);
			ampIndSet.add(ampMEInd);
			ampTheme.setIndicators(ampIndSet);
			DbUtil.update(ampTheme);
		}
		themeForm.setValueType(0);
		themeForm.setCode(null);
		themeForm.setName(null);
		themeForm.setProgramType(null);
		themeForm.setIndicatorDescription(null);
		themeForm.setCreationDate(null);
		themeForm.setCategory(0);
		themeForm.setNpIndicator(false);
		themeForm.setPrgIndicators(ProgramUtil.getThemeIndicators(id));

		return mapping.findForward("forward");
	}
}