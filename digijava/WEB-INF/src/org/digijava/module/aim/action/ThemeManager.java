/*
 * ThemeManager.java
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
import org.digijava.module.aim.dbentity.AmpProgramType;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
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
		//added for the indicator
		
		String viewPreference = request.getParameter("view");
		if(viewPreference!=null)
		{
			if(viewPreference.equals("multiprogram"))
			{
				Collection themes = new ArrayList();
				ThemeForm themeForm = (ThemeForm) form;

				themes = ProgramUtil.getParentThemes();
				themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
				
				themeForm.setThemes(themes);
				return mapping.findForward("forward");
			}
			else if(viewPreference.equals("indicators"))
			{
				return mapping.findForward("gotoIndicators");
			}
			else if(viewPreference.equals("meindicators"))
			{
				return mapping.findForward("gotoMEIndicators");
			}
		}
		
		Collection themes = new ArrayList();
		ThemeForm themeForm = (ThemeForm) form;
		String event = request.getParameter("event");
		
		if (event != null && event.equals("delete"))
		{
			//Iterator itr = DbUtil.getActivityTheme(themeForm.getThemeId()).iterator();
			logger.info(" theme Id is ... "+themeForm.getThemeId());
			//Iterator itr = DbUtil.getActivityThemeFromAAT(new Long(Long.parseLong(request.getParameter("themeId")))).iterator();
			Collection col = DbUtil.getActivityThemeFromAAT(themeForm.getThemeId());
			if(col!=null)
			{
				//System.out.println("activity references i can not delete this theme!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
				themeForm.setFlag("activityReferences");
			}
			else
			{
				
				themeForm.setFlag("deleted");
				//System.out.println("I deleted this theme....ups!!!!!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
				Long id = new Long(Long.parseLong(request.getParameter("themeId")));
				
				ProgramUtil.deleteTheme(id);
				return mapping.findForward("delete");
			}
			/*Iterator itr = DbUtil.getActivityThemeFromAAT(themeForm.getThemeId()).iterator();
			if (itr.hasNext()) {
				System.out.println("activity references i can not delete this theme!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
				themeForm.setFlag("activityReferences");
			}
			else {
				themeForm.setFlag("deleted");
				System.out.println("I deleted this theme....ups!!!!!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
				Long id = new Long(Long.parseLong(request.getParameter("themeId")));
				ProgramUtil.deleteTheme(id);
				mapping.findForward("delete");
			}*/
			
			
		}
		themes = ProgramUtil.getParentThemes();
		themeForm.setThemes(themes);
		themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
		return mapping.findForward("forward");
	}
}
