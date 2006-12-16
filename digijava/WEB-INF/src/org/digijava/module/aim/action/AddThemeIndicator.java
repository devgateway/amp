/*
 * AddThemeIndicator.java 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.ProgramUtil;

public class AddThemeIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(AddThemeIndicator.class);
	private ArrayList indValues = null;
	
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
		
		if(event!=null)
		{
			if(event.equals("save"))
			{
				AmpPrgIndicator ampPrgInd = new AmpPrgIndicator();
				ampPrgInd.setName(themeForm.getName());
				ampPrgInd.setCode(themeForm.getCode());
				ampPrgInd.setType(themeForm.getType());
				ampPrgInd.setCategory(themeForm.getCategory());
				ampPrgInd.setNpIndicator(themeForm.isNpIndicator());
				ampPrgInd.setDescription(themeForm.getIndicatorDescription());
				ampPrgInd.setPrgIndicatorValues(themeForm.getPrgIndValues());
				ProgramUtil.saveThemeIndicators(ampPrgInd,id);
			}
			if(event.equals("indValue"))
			{
				indValues = new ArrayList();
				AmpPrgIndicatorValue prgIndVal = null;
				if(themeForm.getPrgIndValues() != null)
					indValues = new ArrayList(themeForm.getPrgIndValues());
				prgIndVal = getPrgIndicatorValue();
				indValues.add(prgIndVal);
				themeForm.setPrgIndValues(indValues);
				return mapping.findForward("forward");
			}
		}
		themeForm.setCode(null);
		themeForm.setName(null);
		themeForm.setType(null);
		themeForm.setProgramType(null);
		themeForm.setIndicatorDescription(null);
		themeForm.setCategory(0);
		themeForm.setNpIndicator(false);
		themeForm.setPrgIndValues(null);
		themeForm.setPrgIndicators(ProgramUtil.getThemeIndicators(id));
		return mapping.findForward("forward");
	}
	
	private AmpPrgIndicatorValue getPrgIndicatorValue() 
	{
		AmpPrgIndicatorValue prgIndVal = new AmpPrgIndicatorValue();
		prgIndVal.setCreationDate(null);
		prgIndVal.setValAmount(null);
		prgIndVal.setValueType(1);
		return prgIndVal;
	}
}