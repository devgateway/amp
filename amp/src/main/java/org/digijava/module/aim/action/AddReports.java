package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class AddReports extends Action {

    private static Logger logger = Logger.getLogger(AddReports.class);

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

        ReportsForm repForm = (ReportsForm) form;

        logger.debug("In add reports");
        
        if (repForm.getName() != null) {
            AmpReports ampReport = new AmpReports();
            ampReport.setName(repForm.getName());
            ampReport.setDescription(repForm.getDescription());
            TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
            AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
            ampReport.setOwnerId(ampTeamMember);
            ampReport.setUpdatedDate(new Date(System.currentTimeMillis()));
            DbUtil.add(ampReport);
            logger.debug("reports added");

            return mapping.findForward("added");
        }
        return mapping.findForward("forward");
    }
}
