package org.digijava.module.aim.action ;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpMEIndicators;

import javax.servlet.http.*;

public class SaveIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(SaveIndicator.class);
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request,
								 HttpServletResponse response) throws java.lang.Exception 
	{
		IndicatorForm indForm = (IndicatorForm) form;

		if (indForm.getIndicatorName() != null) 
		{	
			logger.info("name ===========: "+indForm.getIndicatorName()+"code===== : "+indForm.getIndicatorCode());
			
			AmpMEIndicators ampIndicators = new AmpMEIndicators();
			ampIndicators.setName(indForm.getIndicatorName());
			ampIndicators.setDescription(indForm.getIndicatorDesc());
			ampIndicators.setCode(indForm.getIndicatorCode());
			ampIndicators.setDefaultInd(indForm.getDefaultFlag());
	
			DbUtil.add(ampIndicators);
			logger.info("Indicators added");
		}
		return mapping.findForward("forward");
	}
}