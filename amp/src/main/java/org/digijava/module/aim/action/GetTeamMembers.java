package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetTeamMembers extends Action {

    private static Logger logger = Logger.getLogger(GetTeamMembers.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In get team members");

        TeamMemberForm upMemForm = (TeamMemberForm) form;
        
        HttpSession session     = request.getSession();
        TeamMember tm = null;
        if (session.getAttribute("currentMember") != null) {
            tm = (TeamMember) session.getAttribute("currentMember");    
        }
        else
            return mapping.findForward("index");
        
        try {
            
        boolean permitted = false;
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            logger.info("Key :" + key);
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;   
                    }
                }
            }logger.info(" this is the key to tell us "+key);
        }
        

        
        
        Long id = null; 
        if (upMemForm.getTeamId() == null) {
            id = tm.getTeamId();
        } else {
            id = upMemForm.getTeamId();
        }
        
        logger.debug("Getting members of the team with id " + id + " ....");
        
        if (id != null) {
            AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
            Collection col = TeamMemberUtil.getAllTeamMembers(id);
            upMemForm.setTeamMembers(col);
            upMemForm.setTeamId(id);
            upMemForm.setTeamName(ampTeam.getName());
            if (tm != null) {
                upMemForm.setTeamMemberId(tm.getMemberId());
            }
            if ( permitted )
                return mapping.findForward("forward");
            else
                return mapping.findForward("justList");
        }
        
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return null;
    }
}
