/*
 * Created on 8/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class EditSurveyList extends Action {

	private static Logger logger = Logger.getLogger(EditSurveyList.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) {

		// if user is not logged in, forward him to the home page
		if (request.getSession().getAttribute("currentMember") == null)
			return mapping.findForward("index");

		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");

		// if user is not a DONOR then forward him to his portfolio
		if (!tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE) && !tm.getTeamType().equalsIgnoreCase("GOVERNMENT"))
			return mapping.findForward("viewMyDesktop");

		logger.debug("In edit survey list action");

		if (form != null) {
			EditActivityForm svForm = (EditActivityForm) form;
			logger.debug("step[before] : " + svForm.getStep());
			svForm.setStep("17");	// for indicators tab in donor-view
			logger.debug("step[after] : " + svForm.getStep());

			if (null == svForm.getSurvey() || svForm.getSurvey().size() < 1) {
				Comparator sfComp = new Comparator() {
					public int compare(Object o1, Object o2) {
						SurveyFunding sf1 = (SurveyFunding) o1;
						SurveyFunding sf2 = (SurveyFunding) o2;
				        return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
					}
				};
				List surveyColl = (List) DbUtil.getAllSurveysByActivity(svForm.getActivityId());
				/*
				if (surveyColl.size() == 1) {
					ActionForward fwd = mapping.findForward("edit");
					StringBuffer path = new StringBuffer(fwd.getPath());
					path.append("?edit=true&surveyId=" + ((SurveyFunding)surveyColl.get(0)).getSurveyId());
					logger.debug("path = " + path);
					//return new ActionForward(path.toString());
					try {
						RequestDispatcher rd = request.getRequestDispatcher(path.toString());
						rd.forward(request, response);
					}
					catch (Exception ex) {
						logger.debug("exception from edit survey list : " + ex);
						ex.printStackTrace(System.out);
					}
				}
				else {	*/
				Collections.sort(surveyColl, sfComp);
				svForm.setSurvey(surveyColl);
				//}
			}
			return mapping.findForward("forward");
		}
		else {
			logger.debug("ActionForm is null.");
			return mapping.findForward("viewMyDesktop");
		}
	}
}
