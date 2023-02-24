package org.digijava.module.aim.action;


import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TabPositionForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class SaveTabPosition  extends Action {
    
    private static Logger logger    = Logger.getLogger(SaveTabPosition.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        TeamMember teamMember                   = (TeamMember)session.getAttribute( Constants.CURRENT_MEMBER );
        TabPositionForm positionForm=(TabPositionForm)form; 
        Integer position = positionForm.getPosition();
        List<AmpReports> tabs=new ArrayList<AmpReports>();
        if(tabs.size()==5&&position!=-1){
            return null;
        }
        if(position==-1){
            TeamMemberUtil.removeDesktopTab(positionForm.getReportId(),teamMember.getMemberId(), position);
        }
        else{
            TeamMemberUtil.addDesktopTab(positionForm.getReportId(),teamMember.getMemberId(), position);
        }
        AmpTeamMember member=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());

        AmpApplicationSettings ampAppSettings   = DbUtil.getTeamAppSettings(teamMember.getTeamId());
        AmpReports defaultTeamReport            = ampAppSettings.getDefaultTeamReport();
        boolean defaultTeamReportAdded          = false;  
        Set<AmpDesktopTabSelection> selections=member.getDesktopTabSelections();
        if (selections != null) {
            for (AmpDesktopTabSelection selection : selections) {
                AmpReports rep=selection.getReport();
                tabs.add(rep);
                if ( defaultTeamReport!=null && defaultTeamReport.getAmpReportId().equals(rep.getAmpReportId()) ){
                    defaultTeamReportAdded  = true;
                }
            }
        }
        if ( defaultTeamReport!=null && !defaultTeamReportAdded){
            tabs.add( defaultTeamReport );
        }
        session.setAttribute(Constants.MY_TABS, tabs);  
        
        return null;
    }
    


}
