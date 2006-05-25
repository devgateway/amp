package org.digijava.module.aim.action;
/**
 * @author Govind G Dalwani
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.aim.form.ParisIndicatorForm;
import org.digijava.module.aim.helper.ParisIndicatorReportHelper;
import org.digijava.module.aim.helper.ParisIndicatorHelper;
import org.digijava.module.aim.util.ParisUtil;

public class ParisIndicatorReport1 extends Action {
	
	private static Logger logger = Logger.getLogger(YearlyInfoFilter.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) 
		throws java.lang.Exception {								 	

		HttpSession session = request.getSession();
		ParisIndicatorForm parisForm = (ParisIndicatorForm) form;
		
		logger.info("in the first report");
		Collection indicatorValues = ParisUtil.getParisIndicators();
		parisForm.setParisIndicatorsList(indicatorValues);
		logger.info("this is the value got "+indicatorValues);
		String parisIndicatorId =request.getParameter("pid");
		logger.info("this is aprisIndicator........"+parisIndicatorId);
		if(request.getParameter("pid") != null)
		{
			logger.info("=== report 1 continues..."+request.getParameter("pid"));
			Integer a = new Integer(parisIndicatorId);
			Collection temp = ParisUtil.getParisIndicatorReport1();
			parisForm.setDonorList(temp);
			Collection questionTemp = ParisUtil.getParisQuestions(a);
			parisForm.setReportQuestions(questionTemp);
			return mapping.findForward("pindicr1");
		}
		
		logger.info("fwd now....");
		return mapping.findForward("pindicmenu");
	}
}
