/*
 * GetDesktopActivities.java
 * Created: 29-May-2006
 */
package org.digijava.module.aim.action;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Loads all desktop activities for the user
 * @author Priyajith
 *
 */
public class GetDesktopActivities extends Action {

    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        DesktopForm dForm = (DesktopForm) form;
            
        /*
         * Do not uncomment ---- FIX for bug AMP-2030
         * if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)==null){
                  session.setAttribute(Constants.CURRENT_TAB_REPORT, null);
                }
        */

        session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
        
/*      TODO-CONSTANTIN: DON'T KNOW WHAT THIS CODE IS SUPPOSED TO DO. In case of bug, set up parameter forwarding to the relevant action and take care of it there.
 *      impossible to do this here, as here we don't know what report & filter this parameter should apply to 
 *  
        String risk=(String) request.getParameter("risk");
        AmpARFilter arf = ReportContextData.getFromRequest(true).getFilter();
        if(risk!=null)
        {
            if (arf == null)
                arf = new AmpARFilter();
            
            final AmpIndicatorRiskRatings airr = FeaturesUtil.getFilter(risk);
            arf.setRisks(new HashSet(){{add(airr);}});
            ReportContextData.getFromRequest().setFilter(arf);          
        }*/

        if (Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(tm.getTeamAccessType())) {
            dForm.setShowAddActivityLink(false);
        } else {
            dForm.setShowAddActivityLink(true);
        }
        dForm.setTeamId(tm.getTeamId());
        dForm.setTeamHead(tm.getTeamHead());
        

        // load activities
        /*
         * The activities of the user is stored in a session scoped varible 'ampProjects' declared in the class
         * org.digijava.module.aim.helper.Constants.
         */

        
        /* Do not uncomment this too. This load is not useful, just uses 100% CPU and high memory and db load.

        Collection col = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
        //if (col == null) {
            col = DesktopUtil.getDesktopActivities(tm.getTeamId(),tm.getMemberId(),
                    tm.getTeamHead());
            session.setAttribute(Constants.AMP_PROJECTS,col);
        //}
        dForm.setTotalCalculated(false);
        dForm.setSearchKey(null);
        Collection col1 = (Collection) session.getAttribute(Constants.AMP_PROJECTS);
        dForm.setActivities(new ArrayList(col1));
        */
        
        return mapping.findForward("forward");
    }
}
