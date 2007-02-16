package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.AllIndicatorForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

import javax.servlet.http.*;

public class OverallIndicatorManager extends Action 
{
	private static Logger logger = Logger.getLogger(OverallIndicatorManager.class);

	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) 
		{
			return mapping.findForward("index");
		} 
		else 
		{
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) 
			{
				return mapping.findForward("index");
			}
		}
		
		AllIndicatorForm allIndForm = (AllIndicatorForm) form;
		String viewPreference = request.getParameter("view");

		if(viewPreference!=null)
		{
			if(viewPreference.equals("indicators"))
			{
				allIndForm.setPrgIndicators(ProgramUtil.getAllThemeIndicators());
				allIndForm.setProjIndicators(MEIndicatorsUtil.getAllActivityIds());
				return mapping.findForward("forward");
			}
			else if(viewPreference.equals("multiprogram"))
				return mapping.findForward("gotoMultiProgram");
			else if(viewPreference.equals("meindicators"))
				return mapping.findForward("gotoMEIndicators");
		}
		allIndForm.setPrgIndicators(ProgramUtil.getAllThemeIndicators());
		allIndForm.setProjIndicators(MEIndicatorsUtil.getAllActivityIds());
		
		return mapping.findForward("forward");
	}
}