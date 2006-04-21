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
		AmpMEIndicators ampMEIndnew = null;
		if(indForm.getIndicatorName().trim() != null && indForm.getIndicatorCode().trim() != null) {
			ampMEIndnew = new AmpMEIndicators();
			ampMEIndnew.setName(indForm.getIndicatorName());
			ampMEIndnew.setCode(indForm.getIndicatorCode());
			ampMEIndnew.setDescription(indForm.getIndicatorDesc());
			ampMEIndnew.setDefaultInd(false);

			MEIndicatorsUtil.saveMEIndicator(ampMEIndnew,indForm.getActivityId(),false);
		}
		return mapping.findForward("forward");
	}
}