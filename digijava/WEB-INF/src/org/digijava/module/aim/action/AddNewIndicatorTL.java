package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class AddNewIndicatorTL extends Action 
{
	private static Logger logger = Logger.getLogger(AddNewIndicatorTL.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		IndicatorForm indForm = (IndicatorForm) form;
		AmpMEIndicatorValue ampMEIndValnew = null;
		AmpMEIndicators ampMEIndnew = null;
		AmpMEIndicators indFound = null;
		AmpActivity ampAct = new AmpActivity();
		
		ampAct.setAmpActivityId(indForm.getActivityId());
		
		if(indForm.getIndicatorName().trim() != null && indForm.getIndicatorCode().trim() != null)
		{
			ampMEIndValnew = new AmpMEIndicatorValue();
			ampMEIndnew = new AmpMEIndicators();

			ampMEIndnew.setName(indForm.getIndicatorName());
			String indName = indForm.getIndicatorName();
			ampMEIndnew.setCode(indForm.getIndicatorCode());
			String indCode = indForm.getIndicatorCode();
			ampMEIndnew.setDescription(indForm.getIndicatorDesc());
			ampMEIndnew.setDefaultInd(false);

			DbUtil.add(ampMEIndnew);
			
			indFound = (AmpMEIndicators) MEIndicatorsUtil.findIndicatorId(indName,indCode);

			ampMEIndValnew.setActivityId(ampAct);
			ampMEIndValnew.setMeIndicatorId(indFound);
			ampMEIndValnew.setBaseVal(0);
			ampMEIndValnew.setTargetVal(0);
			ampMEIndValnew.setRevisedTargetVal(0);
			ampMEIndValnew.setBaseValDate(null);
			ampMEIndValnew.setTargetValDate(null);
			ampMEIndValnew.setRevisedTargetValDate(null);
			ampMEIndValnew.setRisk(null);
			ampMEIndValnew.setComments(null);
			
			DbUtil.add(ampMEIndValnew);
		}
		return mapping.findForward("forward");
	}
}