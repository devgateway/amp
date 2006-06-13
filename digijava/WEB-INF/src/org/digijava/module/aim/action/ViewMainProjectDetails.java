package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.form.MainProjectDetailsForm;
import org.digijava.module.aim.helper.COverSubString;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewMainProjectDetails extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewMainProjectDetails.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		MainProjectDetailsForm formBean = (MainProjectDetailsForm) form;
		HttpSession session = request.getSession();
		
		try {
		
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		} else {
			formBean.setSessionExpired(false);
			Long id = new Long(formBean.getAmpActivityId());

			int tabIndex = formBean.getTabIndex();
			String channelOverviewTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String financialProgressTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String physicalProgressTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			String documentsTabColor = Constants.INACTIVE_MAIN_TAB_CLASS;
			formBean.setAmpActivityId(id.longValue());
			formBean.setTabIndex(tabIndex);
			AmpActivity ampActivity = ActivityUtil.getProjectChannelOverview(id);
			if (ampActivity.getDescription() == null)
				formBean.setDescription("");
			else {
				formBean.setDescription(ampActivity.getDescription());
				formBean.setObjectives(ampActivity.getObjective());
			}
			formBean.setFlag(COverSubString.getCOverSubStringLength(ampActivity
					.getDescription(), 'D'));
			formBean.setName(ampActivity.getName());
			formBean.setAmpId(ampActivity.getAmpId());
			if (tabIndex == 0)
				channelOverviewTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			else if (tabIndex == 1)
				financialProgressTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			else if (tabIndex == 2)
				physicalProgressTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			else if (tabIndex == 3)
				documentsTabColor = Constants.ACTIVE_MAIN_TAB_CLASS;
			formBean.setChannelOverviewTabColor(channelOverviewTabColor);
			formBean.setFinancialProgressTabColor(financialProgressTabColor);
			formBean.setPhysicalProgressTabColor(physicalProgressTabColor);
			formBean.setDocumentsTabColor(documentsTabColor);
		}
		} catch (Exception e) {
			logger.debug("Exception " + e.getMessage());
			e.printStackTrace(System.out);
		}
		return null;
	}
}