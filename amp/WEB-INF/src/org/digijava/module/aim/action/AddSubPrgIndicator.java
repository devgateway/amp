package org.digijava.module.aim.action;

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
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddSubPrgIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(AddSubPrgIndicator.class);

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
		Long roothemeId = new Long(Long.parseLong(request.getParameter("rootId")));
		int indlevel = Integer.parseInt(request.getParameter("indlevel"));
		themeForm.setPrgParentThemeId(id);
		
		
		AmpTheme ampThem = ProgramUtil.getThemeObject(id);
		
		//gett root theme name to display at the top of the page
		AmpTheme parent=ampThem;
		while (parent.getParentThemeId() != null) {
			parent=parent.getParentThemeId();
			
		}
		String indname = parent.getName();
		themeForm.setName(indname);
		
		//String indname = request.getParameter("indname");
		//themeForm.setName(indname);
		
		themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(id));
		for(Iterator itr = themeForm.getSubPrograms().iterator(); itr.hasNext();){
			AmpTheme itm = (AmpTheme)itr.next();
			if(itm.getName().equalsIgnoreCase(themeForm.getProgramName())){
				
				return mapping.findForward("notadd");
			}
		}
		
		
		if(event != null && event.equals("addSubProgram"))	
		{
			themeForm.setProgramName(null);
			themeForm.setProgramCode(null);
			themeForm.setProgramDescription(null);
			themeForm.setProgramTypeCategValId(new Long(0));
			themeForm.setPrgParentThemeId(new Long(Long.parseLong(request.getParameter("themeId"))));
			themeForm.setPrgLevel(indlevel);
			themeForm.setRootId(roothemeId);
			themeForm.setPrgLanguage(null);
			themeForm.setVersion(null);
			//themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
			return mapping.findForward("addProgram");
		}
		else if(event != null && event.equals("save"))
		{
			int level = indlevel+1;
			AmpTheme ampTheme = new AmpTheme();
			ampTheme.setName(themeForm.getProgramName());
			ampTheme.setThemeCode(themeForm.getProgramCode());
			ampTheme.setDescription(themeForm.getProgramDescription());
			ampTheme.setTypeCategoryValue( CategoryManagerUtil.getAmpCategoryValueFromDb(themeForm.getProgramTypeCategValId()));
			ampTheme.setParentThemeId(ProgramUtil.getThemeObject(id));
			ampTheme.setIndlevel(new Integer(level));
			ampTheme.setLanguage(null);
			ampTheme.setVersion(null);
			DbUtil.add(ampTheme);
			themeForm.setRootId(roothemeId);
		}
		themeForm.setRootId(roothemeId);
		themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(roothemeId));
		return mapping.findForward("forward");
	}
}