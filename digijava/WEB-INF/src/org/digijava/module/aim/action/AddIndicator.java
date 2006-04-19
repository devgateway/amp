package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.*;


public class AddIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(AddIndicator.class);

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
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) 
			{
				return mapping.findForward("index");
			}
		}
		
		String create = request.getParameter("create");
		
		IndicatorForm indForm = (IndicatorForm) form;
		AmpActivity ampAct = null;
		
		if(create.equals("true")) 
		{
			indForm.setErrorFlag(false);
			indForm.setEvent("create");
			return mapping.findForward("create");
		} 
		else 
		{
			if (indForm.getIndicatorName() != null && indForm.getIndicatorCode() != null) 
			{	
				boolean dupExist = MEIndicatorsUtil.checkDuplicateNameCode(
						indForm.getIndicatorName(),indForm.getIndicatorCode());

				indForm.setEvent("added");
				if (dupExist) 
				{
					ActionErrors errors = new ActionErrors();
					errors.add(ActionErrors.GLOBAL_ERROR, 
							new ActionError("error.aim.meAddIndicator.duplicateNameOrCode"));
					saveErrors(request, errors);
					indForm.setErrorFlag(true);
					indForm.setEvent("create");
					return mapping.getInputForward();
				} 
				else 
				{
					AmpMEIndicators ampIndicators = new AmpMEIndicators();
					ampIndicators.setName(indForm.getIndicatorName());
					ampIndicators.setDescription(indForm.getIndicatorDesc());
					ampIndicators.setCode(indForm.getIndicatorCode());
					ampIndicators.setDefaultInd(indForm.getDefaultFlag());
					
					DbUtil.add(ampIndicators);
					
					indForm.setSameIndicatorCode("false");
					indForm.setSameIndicatorName("false");
					indForm.setEvent("added");
					if(indForm.getDefaultFlag() == true)
					{
						
						Collection col = MEIndicatorsUtil.getActivityList();
						
						Iterator itrIndVal = col.iterator();
						while(itrIndVal.hasNext())
						{
							ampAct = (AmpActivity) itrIndVal.next();
							AmpMEIndicatorValue ampIndValue = new AmpMEIndicatorValue();
							ampIndValue.setBaseVal(0);
							ampIndValue.setTargetVal(0);
							ampIndValue.setRevisedTargetVal(0);
							ampIndValue.setBaseValDate(null);
							ampIndValue.setTargetValDate(null);
							ampIndValue.setRevisedTargetValDate(null);
							ampIndValue.setActivityId(ampAct);
							ampIndValue.setMeIndicatorId(ampIndicators);
							ampIndValue.setRisk(null);
							ampIndValue.setComments(null);
							
							DbUtil.add(ampIndValue);
						}
					}					
				}
				return mapping.findForward("forward");
			}
		}
		return null;
	}
}
