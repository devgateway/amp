/*
 * Created on 8/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
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
       // if (!tm.getTeamType().equalsIgnoreCase("GOVERNMENT"))
        //      return mapping.findForward("viewMyDesktop");

        logger.debug("In edit survey list action");

        EditActivityForm svForm = (EditActivityForm) form;
        logger.debug("step[before] : " + svForm.getStep());
        svForm.setStep("17"); // for indicators tab in donor-view
        logger.debug("step[after] : " + svForm.getStep());

        Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };
        List surveyColl = (List) DbUtil.getAllSurveysByActivity(svForm.getActivityId());

        Collections.sort(surveyColl, sfComp);
        svForm.getSurvey().setSurvey(surveyColl);

        return mapping.findForward("forward");
    }
}
