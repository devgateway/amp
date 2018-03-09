package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class GetDesktopTeamMembers extends TilesAction {

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        
        String settingValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ACTIVITY_LEVEL);                               
        if("true".equals(settingValue)) request.setAttribute("activity_level", "true");
        
        if (session.getAttribute(Constants.MY_TEAM_MEMBERS) == null) {
            TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
            ////System.out.println("######### "+tm.getMemberName()+"  team head:" +tm.getTeamHead() );
            if (tm != null) {
                Collection members = TeamMemberUtil.getAllTeamMembersToDesktop(tm.getTeamId());
                session.setAttribute(Constants.MY_TEAM_MEMBERS,members);
                ////System.out.println("######### "+tm.getMemberName()+"  team head:" +tm.getTeamHead() );
                if(tm.getTeamHead())
                {
                    ////System.out.println("-------team leader"+tm.getMemberName());
                    session.setAttribute("teamHead", "true");
                }
                if("Workspace Manager".compareTo(tm.getRoleName())==0)
                    {
                        ////System.out.println("****team leader"+tm.getMemberName());
                        session.setAttribute("teamHead", "true");
                    }
            }
        }
        return null;
    }
}
