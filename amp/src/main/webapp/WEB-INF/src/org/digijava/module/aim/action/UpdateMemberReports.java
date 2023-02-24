package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

public class UpdateMemberReports extends Action {

    private static Logger logger = Logger.getLogger(UpdateMemberReports.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In update member reports");
        
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;   
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }
        

        ReportsForm rForm = (ReportsForm) form;

        Long id = null;
        TeamMember tm = null;

        if (session.getAttribute("currentMember") != null) {
            tm = (TeamMember) session.getAttribute("currentMember");
            id = tm.getTeamId();
        }

        try {
        if (rForm.getAddReport() != null) {
            /* show all unassigned reports */
            
            Collection col = TeamMemberUtil.getUnassignedMemberReports(id,rForm.getMemberId());
            List temp = (List) col;
            Collections.sort(temp);
            col = (Collection) temp;                        
            rForm.setReports(col);
            rForm.setTeamId(tm.getTeamId());
            return mapping.findForward("showAddReports");
        } else if (rForm.getRemoveReports() != null) {
            /* remove all selected reports */
            TeamMemberUtil.removeReportsFromMember(rForm.getMemberId(),rForm.getSelReports());
            return mapping.findForward("forward");
        } else if (rForm.getAssignReports() != null) {
            /* add the selected reports */
            TeamMemberUtil.assignReportsToMember(rForm.getMemberId(),rForm.getSelReports());
            return mapping.findForward("forward");
        } else {
            return mapping.findForward(null);
        }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return null;
    }
}

