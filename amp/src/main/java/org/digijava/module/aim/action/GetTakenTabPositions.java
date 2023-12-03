package org.digijava.module.aim.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.util.Set;

public class GetTakenTabPositions extends Action {
    
    private static Logger logger    = Logger.getLogger(GetTakenTabPositions.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        TeamMember teamMember   = (TeamMember)session.getAttribute( Constants.CURRENT_MEMBER );
        AmpTeamMember member=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
        Set<AmpDesktopTabSelection> selections=member.getDesktopTabSelections();
        JSONArray jsonArray = new JSONArray();
        if (selections != null) {
            for (AmpDesktopTabSelection selection : selections) {
                JSONObject jposition = new JSONObject();
                jposition.put("position", selection.getIndex());
                jsonArray.add(jposition);
            }
        }
        response.setContentType("text/plain");
        OutputStreamWriter outputStream = null;

        try {
            outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
            outputStream.write(jsonArray.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return null;
    }

}
