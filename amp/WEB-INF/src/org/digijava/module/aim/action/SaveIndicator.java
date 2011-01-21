package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.util.DbUtil;

public class SaveIndicator extends Action {
	private static Logger logger = Logger.getLogger(SaveIndicator.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		IndicatorForm indForm = (IndicatorForm) form;

		if (indForm.getIndicatorName() != null) {
			AmpMEIndicators ampIndicators = new AmpMEIndicators();
			ampIndicators.setName(indForm.getIndicatorName());
			if (indForm.getIndicatorDesc() != null &&
					indForm.getIndicatorDesc().length() > 0)
				ampIndicators.setDescription(indForm.getIndicatorDesc());
			else 
				ampIndicators.setDescription(" ");
				
			ampIndicators.setCode(indForm.getIndicatorCode());
			ampIndicators.setDefaultInd(indForm.isDefaultFlag());
			DbUtil.add(ampIndicators);
		}
		return mapping.findForward("forward");
	}
}