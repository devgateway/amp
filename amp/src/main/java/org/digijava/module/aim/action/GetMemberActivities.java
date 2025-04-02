package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

public class GetMemberActivities extends Action {

    private static Logger logger = Logger.getLogger(GetMemberActivities.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In get member activities");
        
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
        

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;

        Long id = null;

        if (request.getParameter("id") != null) {
            id = new Long(Long.parseLong(request.getParameter("id")));
        } else if (request.getAttribute("memberId") != null) {
            id = (Long) request.getAttribute("memberId");
        } else if (session.getAttribute("currentMember") != null) {
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            id = tm.getMemberId();
        }

        if (id != null) {
            AmpTeamMember ampMem = TeamMemberUtil.getAmpTeamMember(id);
            Collection col = TeamMemberUtil.getAllMemberActivities(id);
            
            //fferreyra. It seems that this action is not being used anymore, commented usage of activity bean as part of refactoring
            Comparator acronymComp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    AmpActivity r1 = (AmpActivity) o1;
                    AmpActivity r2 = (AmpActivity) o2;
                    return r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase());
                }
            };
            Comparator racronymComp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    AmpActivity r1 = (AmpActivity) o1;
                    AmpActivity r2 = (AmpActivity) o2;
                    return -(r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase()));
                }
            };
            
            List temp = (List)col;
            String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
            String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();
            
            if ( sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
                Collections.sort(temp);
                taForm.setSort("activity");
                taForm.setSortOrder("asc");
            }
            else {
                if ("activity".equals(sort)) {
                    if ("asc".equals(sortOrder))
                        Collections.sort(temp);
                    else
                        Collections.sort(temp,Collections.reverseOrder());
                }
                else if ("donor".equals(sort)) {
                    if ("asc".equals(sortOrder))
                        Collections.sort(temp, acronymComp);
                    else
                        Collections.sort(temp, racronymComp);
                }
            }
            col = (Collection) temp;                        
            taForm.setActivities(col);
            taForm.setMemberId(id);
            taForm.setMemberName(ampMem.getUser().getName());
            return mapping.findForward("forward");
        } else {
            return null;
        }
    }
}
