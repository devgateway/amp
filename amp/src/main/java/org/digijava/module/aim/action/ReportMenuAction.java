package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class ReportMenuAction extends DispatchAction {
    private static Logger logger = Logger.getLogger(ReportMenuAction.class);

    public ActionForward getOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        try {

            HashMap<String, String> options = new HashMap<String, String>();

            HttpSession session = request.getSession();

            Long activityId = Long.parseLong(request.getParameter("id"));

            TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
            AmpTeam currentTeam = null;
            if (teamMember != null)
                currentTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());

            Session hsession = null;

            hsession = PersistenceManager.getSession();
            AmpActivityVersion activity = null;
            boolean gatePermEditAllowed = false;
            boolean isDraft, needsApproval, isUserTeamLead;
            isDraft = needsApproval = isUserTeamLead = false;
            if (activityId != null) {
                activity = (AmpActivityVersion) hsession.load(AmpActivityVersion.class, activityId);
                if (activity != null) {
                    boolean hasTeamLead = true;
                    if (currentTeam != null) {
                        AmpTeamMember teamHead = TeamMemberUtil.getTeamHead(currentTeam.getAmpTeamId());
                        isUserTeamLead = (teamHead.getAmpTeamMemId().compareTo(teamMember.getMemberId()) == 0) ;
                    }
                    if (activity.getDraft() != null && activity.getDraft()) {
                        isDraft = true;
                        // It's Draft Activity
                    } else {
                        // It's not draft activity
                        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(activity.getApprovalStatus())) {
                            // Needs Approval
                            needsApproval = true;
                        }
                    }
                }
            }
            options.put(TranslatorWorker.translateText("Details"), "/aim/viewActivityPreview.do~activityId=");
            if (needsApproval && !isDraft && isUserTeamLead) {
                options.put(TranslatorWorker.translateText("Validate"), "/wicket/onepager/activity/");
            } else {
                options.put(TranslatorWorker.translateText("Edit"), "/wicket/onepager/activity/");
            }

            StringBuffer output = new StringBuffer();
            output.append("<options>");
            Set<String> key = options.keySet();
            for (String theKey : key) {
                output.append("<option url='");
                output.append(options.get(theKey));
                output.append("'>");

                output.append(theKey);
                output.append("</option>");
            }

            response.setContentType("text/xml");
            OutputStreamWriter outputStream = new OutputStreamWriter(
                    response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);

            output.append("</options>");
            out.print(output.toString());
            outputStream.close();
            return null;

        } catch (Exception e) {
            logger.debug("Exception " + e.getMessage());
        }
        return null;
    }
}
