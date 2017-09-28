package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;


public class GetTabPositionsCount extends Action {

    private static Logger logger = Logger.getLogger(SaveTabPosition.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setContentType("text/plain");
        HttpSession session = request.getSession();
        TeamMember teamMember                   = (TeamMember)session.getAttribute( Constants.CURRENT_MEMBER );
        AmpApplicationSettings ampAppSettings   = DbUtil.getTeamAppSettings(teamMember.getTeamId());
        AmpReports defaultTeamReport            = ampAppSettings.getDefaultTeamReport();
        int size = 0;
        AmpTeamMember member=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
        Set<AmpDesktopTabSelection> selections=member.getDesktopTabSelections();
        boolean defaulHasPosition=false;
            if (selections != null) {
                for (AmpDesktopTabSelection selection : selections) {
                    AmpReports rep=selection.getReport();
                    if ( defaultTeamReport!=null && defaultTeamReport.getAmpReportId().equals(rep.getAmpReportId()) ){
                        defaulHasPosition=true;
                    }
                    size++;
                }
            }
            if(defaultTeamReport!=null&&!defaulHasPosition&&size<5){
                size--;
            }
        
        try {
            response.getWriter().print(size);
        } catch (IOException e) {
            // TODO handle this exception
            e.printStackTrace();
        }

        return null;

    }

}
