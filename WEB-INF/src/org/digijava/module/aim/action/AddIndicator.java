package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class AddIndicator extends Action {
	private static Logger logger = Logger.getLogger(AddIndicator.class);

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

		String event = request.getParameter("event");
		IndicatorForm indForm = (IndicatorForm) form;

		if (event != null && event.equals("add")) {
			indForm.setErrorFlag(true);
			indForm.setIndicatorName(null);
			indForm.setIndicatorDesc(null);
			indForm.setIndicatorCode(null);
			indForm.setDefaultFlag(false);		
			indForm.setIndId(null);
			return mapping.findForward("forward");
		} else if (event != null && event.equals("edit")) {
			indForm.setErrorFlag(true);
			String temp = request.getParameter("indId");
			if (temp != null) {
				try {
					long id = Long.parseLong(temp);
					if (indForm.getIndicators() != null) {
						Iterator itr = indForm.getIndicators().iterator();
						while (itr.hasNext()) {
							AmpMEIndicatorList iList = (AmpMEIndicatorList) itr.next();
							if (iList.getAmpMEIndId().longValue() == id) {
								indForm.setIndicatorName(iList.getName());
								indForm.setIndicatorDesc(iList.getDescription().trim());
								indForm.setIndicatorCode(iList.getCode());
								indForm.setDefaultFlag(iList.isDefaultInd());
								break;								
							}
						}
					}									
				} catch (NumberFormatException nfe) {
					logger.error("NumberFormatException: Trying to parse " + temp + " to long");
				}
			}
			return mapping.findForward("forward");
		} else {
			if (indForm.getIndicatorName() != null
					&& indForm.getIndicatorCode() != null) {
				
				boolean dupExist = MEIndicatorsUtil.checkDuplicateNameCode(
						indForm.getIndicatorName(), indForm.getIndicatorCode(),indForm.getIndId());

				if (dupExist) {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"error.aim.meAddIndicator.duplicateNameOrCode"));
					saveErrors(request, errors);
					if (indForm.getIndId() != null && indForm.getIndId().longValue() < 1) {
						indForm.setIndId(null);
					}
						
					indForm.setErrorFlag(true);
					return mapping.findForward("forward");
				} else {
					AmpMEIndicators ampIndicators = new AmpMEIndicators();
					ampIndicators.setAmpMEIndId(indForm.getIndId());
					ampIndicators.setName(indForm.getIndicatorName());
					if (indForm.getIndicatorDesc() != null &&
							indForm.getIndicatorDesc().length() > 0)
						ampIndicators.setDescription(indForm.getIndicatorDesc());
					else 
						ampIndicators.setDescription(" ");
					
					ampIndicators.setCode(indForm.getIndicatorCode());
					ampIndicators.setDefaultInd(indForm.isDefaultFlag());
					
					if (indForm.getAscendingInd() == 'A')
						ampIndicators.setAscendingInd(true);
					else
						ampIndicators.setAscendingInd(false);
						
					if (indForm.getIndId() != null &&
							indForm.getIndId().longValue() > 0) {
						MEIndicatorsUtil.updateMEIndicator(ampIndicators);
					} else {
						MEIndicatorsUtil.saveMEIndicator(ampIndicators, null,
								ampIndicators.isDefaultInd());
					}
					
					indForm.setSameIndicatorCode("false");
					indForm.setSameIndicatorName("false");
				}
				indForm.setErrorFlag(false);
				return mapping.findForward("save");
			}
		}
		return null;
	}
}
