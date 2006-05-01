/*
 * Created on 9/03/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class EditSurvey extends Action {

	private static Logger logger = Logger.getLogger(EditSurvey.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) {
		
		// if user is not logged in, forward him to the home page
		if (request.getSession().getAttribute("currentMember") == null)
			return mapping.findForward("index");

		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
		
		// if user is not a DONOR then forward him to his portfolio
		if (!tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) 
			return mapping.findForward("viewMyDesktop");
		
		logger.debug("In edit survey action");
		
		if (form != null) {
			
			EditActivityForm svForm = (EditActivityForm) form;
			
			final int NUM_RECORDS = 5;
			long surveyId = 0;
			boolean flag = false;
			
			try {
				String survey = request.getParameter("surveyId");
				if (null != survey && survey.trim().length() > 0) {
					surveyId = Long.parseLong(survey);
					if (null == svForm.getAmpSurveyId() || surveyId != svForm.getAmpSurveyId().longValue()) {
						svForm.setAmpSurveyId(new Long(surveyId));
						flag = true;
					}
				}
			}
			catch (NumberFormatException nex) {
				logger.debug("incorrect survey-id in ActionForm : " + nex.getMessage());
				return mapping.findForward("survey");				
			}
			
			if (null != svForm.getAmpSurveyId()) {
				if (null == svForm.getIndicators() || flag) {
					svForm.setIndicators(DbUtil.getResposesBySurvey(svForm.getAmpSurveyId(), svForm.getActivityId()));
					logger.debug("svForm.getIndicator().size() : " + svForm.getIndicators().size());
					AmpAhsurvey survey = DbUtil.getAhSurvey(svForm.getAmpSurveyId());
					svForm.setFundingOrg(survey.getAmpDonorOrgId().getAcronym());
						
					svForm.setPageColl(new ArrayList());
					int numPages = svForm.getIndicators().size() / NUM_RECORDS;
					numPages += (svForm.getIndicators().size() % NUM_RECORDS != 0) ? 1 : 0;
					if (numPages > 1) {
						for (int i = 0; i < numPages; i++)
							svForm.getPageColl().add(new Integer(i+1));
					}
				}
				
				// saving survey responses
				if (null != request.getParameter("action") && "save".equalsIgnoreCase(request.getParameter("action"))) {
					if (null != svForm.getAmpSurveyId())
						DbUtil.saveSurveyResponses(svForm.getAmpSurveyId(), svForm.getIndicators());
					//logger.debug("mapping.getAttribute() : " + mapping.getAttribute());
					request.getSession().removeAttribute(mapping.getAttribute());
					logger.debug("returning after saving survey responses from edit survey action...");
					return mapping.findForward("viewMyDesktop");
				}
				
				int page = 0;
				if (request.getParameter("page") == null || request.getParameter("page").trim().length() < 1) {
					page = 1;
				} else {
					try {
				 		page = Integer.parseInt(request.getParameter("page"));
				 	}
				 	catch (NumberFormatException nex) {
						logger.debug("incorrect page in request scope : " + nex.getMessage());
						//return mapping.findForward("forward");
					}
				}
				svForm.setCurrPage(new Integer(page));
				svForm.setStartIndex(new Integer(NUM_RECORDS * (page - 1)));
				logger.debug("currPage : " + svForm.getCurrPage());
				logger.debug("startIndex : " + svForm.getStartIndex());
				
				return mapping.findForward("forward");
			}
			
			return null;
		}
		else {
			logger.debug("ActionForm is null.");
			return mapping.findForward("viewMyDesktop");
		}
	}
}