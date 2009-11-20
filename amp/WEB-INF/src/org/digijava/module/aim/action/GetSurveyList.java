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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.SurveyForm;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.gateperm.core.GatePermConst;

public class GetSurveyList extends TilesAction {

    private static Logger logger = Logger.getLogger(GetSurveyList.class);

    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        SurveyForm svForm = (SurveyForm) form;
        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
        Long actId = null;
        try {
            svForm.setTabIndex(request.getParameter("tabIndex"));
            String activityId = request.getParameter("ampActivityId");
            if (null != activityId && activityId.trim().length() > 0) {
                actId = Long.valueOf(activityId);
                logger.debug("actId : " + actId);
                svForm.setAmpActivityId(actId);
            }
        } catch (NumberFormatException nex) {
            logger.debug("incorrect activity-id in ActionForm : " + nex.getMessage());
        }

        Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };

        List surveyColl = (List) DbUtil.getAllSurveysByActivity(actId);
        Collections.sort(surveyColl, sfComp);
        svForm.setSurvey(surveyColl);

        return null;

    }
}
