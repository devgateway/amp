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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.form.EditSurveyForm;
import org.digijava.module.aim.util.DbUtil;

public class GetSurvey extends TilesAction {

	private static Logger logger = Logger.getLogger(GetSurvey.class);
	
	public ActionForward execute( ComponentContext context, ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response) {
		
		// if user is not logged in, forward him to the home page
		//if (request.getSession().getAttribute("currentMember") == null)
			//return mapping.findForward("index");

		logger.debug("In get survey action");
		
		if (form != null) {
			
			EditSurveyForm svForm = (EditSurveyForm) form;
			
			final int NUM_RECORDS = 5;
			long surveyId = 0;
			
			try {
				String survey = request.getParameter("surveyId");
				if (null != survey && survey.trim().length() > 0) {
					surveyId = Long.parseLong(survey);
					logger.debug("surveyId : " + surveyId);
					svForm.setAmpSurveyId(new Long(surveyId));
				}
			}
			catch (NumberFormatException nex) {
				logger.debug("incorrect surveyId in request scope : " + nex.getMessage());
				nex.printStackTrace(System.out);
			}
			
			if (null != request.getParameter("reset") && request.getParameter("reset").trim().length() > 1) {
				if ("true".equalsIgnoreCase(request.getParameter("reset")))
					svForm.setReset(new Boolean(true));
				else
					svForm.setReset(new Boolean(false));
			}
				
			if (null == svForm.getIndicators() || svForm.getReset().booleanValue()) {
				svForm.setIndicators(DbUtil.getResposesBySurvey(svForm.getAmpSurveyId(), svForm.getAmpActivityId()));
				svForm.setReset(new Boolean(false));
				AmpAhsurvey survey = DbUtil.getAhSurvey(svForm.getAmpSurveyId());
				svForm.setFundingOrg(survey.getAmpDonorOrgId().getAcronym());
				
				svForm.setPages(new ArrayList());
				int numPages = svForm.getIndicators().size() / NUM_RECORDS;
				numPages += (svForm.getIndicators().size() % NUM_RECORDS != 0) ? 1 : 0;
				if (numPages > 1) {
					for (int i = 0; i < numPages; i ++)
						svForm.getPages().add(new Integer(i+1));
				}
			}
			
			int page = 0;
			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				try {
			 		page = Integer.parseInt(request.getParameter("page"));
			 	}
			 	catch (NumberFormatException nex) {
					logger.debug("incorrect page in request scope : " + nex.getMessage());
				}
			}
			svForm.setCurrentPage(new Integer(page));
			svForm.setOffset(new Integer(NUM_RECORDS * (page - 1)));
			logger.debug("currentPage : " + svForm.getCurrentPage());
			logger.debug("offset : " + svForm.getOffset());
			
			return null;
		}
		else {
			logger.debug("ActionForm is null.");
			//return mapping.findForward("forward");
			return null;
		}
	}
}