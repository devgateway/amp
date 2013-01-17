package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class IndicatorValues extends Action 
{
	private static Logger logger = Logger.getLogger(IndicatorValues.class);
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request,
								 HttpServletResponse response) throws java.lang.Exception 
	{
		logger.info("inside Indicator values=======");
		Long indId = null;
		IndicatorForm indForm = (IndicatorForm) form;
		Collection indicators = new ArrayList();
		Collection indicatorValues = new ArrayList();
		
		indId = new Long(Long.parseLong(request.getParameter("id")));
		
		indicators = MEIndicatorsUtil.getAllIndicators();
//		indicatorValues = MEIndicatorsUtil.getIndicatorValues(indId);
		
		indForm.setIndicators(indicators);
		indForm.setIndicatorValues(indicatorValues);
		indForm.setIndId(indId);		
		logger.info("moving out of Indicator values=======");
		return mapping.findForward("forward");
	}
}
