package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateMemberActivities extends Action {

    private static Logger logger = Logger
            .getLogger(UpdateMemberActivities.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In update member activities");
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

        try {
        
        TeamActivitiesForm taForm = (TeamActivitiesForm) form;

        Long id = null;
        TeamMember tm = null;

        if (session.getAttribute("currentMember") != null) {
            tm = (TeamMember) session.getAttribute("currentMember");
            id = tm.getTeamId();
        } else {
            return mapping.findForward("index");
        }
        
        if (taForm.getRemoveActivity() != null) {
            /* remove all selected activities */
            
            TeamMemberUtil.removeActivitiesFromMember(taForm.getMemberId(),
                    taForm.getSelActivities());
            
            return mapping.findForward("forward");
        } else if (taForm.getAssignActivity() != null) {
            /* add the selected activities to the team list */
            
            TeamMemberUtil.assignActivitiesToMember(taForm.getMemberId(),
                    taForm.getSelActivities());

            return mapping.findForward("forward");
        } else {
            /* show all unassigned activities */
            Collection col = null;
            
            /*
             * AMP-3476
             * 
            if (tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
                col = TeamMemberUtil.getUnassignedDonorMemberActivities(id,
                        taForm.getMemberId());
            } else {
                col = TeamMemberUtil.getUnassignedMemberActivities(id,
                        taForm.getMemberId());
            }*/
            
            // AMP-3476
            col = TeamMemberUtil.getUnassignedMemberActivities(id,
                    taForm.getMemberId());
            
            
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
            taForm.setTeamId(tm.getTeamId());
            return mapping.findForward("showAddActivity");          
        }
        
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
