package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ReportMenuAction extends DispatchAction {
	private static Logger logger = Logger.getLogger(ReportMenuAction.class);

	public ActionForward getOPtions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		try {

			HashMap<String, String> options = new HashMap<String, String>();

			HttpSession session = request.getSession();

			Long id = Long.parseLong(request.getParameter("id"));

			TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
			Long ampTeamId = teamMember.getTeamId();
			Boolean teamLeadFlag = teamMember.getTeamHead();
			Boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);
			String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
			Boolean isDraft = ActivityUtil.isDraft(id);
		
			options.put(TranslatorWorker.translateText("Details", request), "/aim/selectActivityTabs.do~ampActivityId=");
			AmpTeam activityTeam = ActivityUtil.getActivityTeam(id);
			options.put(TranslatorWorker.translateText("Preview", request), "/aim/viewActivityPreview.do~pageId=2~isPreview=1~activityId=");
			// activity.getTeam().getAmpTeamId()
			if (workingTeamFlag) {
				if (!(isDraft != null && isDraft) && (actApprovalStatus != null && Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(actApprovalStatus.toLowerCase()))) {
					if (workingTeamFlag && teamLeadFlag && teamMember.getTeamId().equals(activityTeam.getAmpTeamId())) {
						options.put(TranslatorWorker.translateText("Validate",request), "/aim/editActivity.do~pageId=1~step=1~action=edit~surveyFlag=true~debugFM=true~activityId=");
					}else{
						options.put("Edit", "/aim/editActivity.do~pageId=1~step=1~action=edit~surveyFlag=true~debugFM=true~activityId=");
					}
				}else{
					options.put("Edit", "/aim/editActivity.do~pageId=1~step=1~action=edit~surveyFlag=true~activityId=");
				}
			}
			
			
			StringBuffer ouPut = new StringBuffer();
			ouPut.append("<options>");
			Set<String> key = options.keySet();
			for (String theKey : key) {
				ouPut.append("<option url='");
				ouPut.append(options.get(theKey));
				ouPut.append("'>");
				
				ouPut.append(theKey);
				ouPut.append("</option>");
			}
			

			response.setContentType("text/xml");
			OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
			PrintWriter out = new PrintWriter(outputStream, true);

			ouPut.append("</options>");
			out.print(ouPut.toString());
			outputStream.close();
			return null;

		} catch (Exception e) {
			logger.debug("Exception " + e.getMessage());
		}
		return null;
	}
}
