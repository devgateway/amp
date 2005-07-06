package org.digijava.module.aim.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.form.TeamPagesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class SaveTeamPageConfiguration extends Action {

	private static Logger logger = Logger
			.getLogger(SaveTeamPageConfiguration.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In SaveTeamPageConfiguration");
		
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
		
		TeamPagesForm tpForm = (TeamPagesForm) form;
		Long selFilters[] = tpForm.getSelFilters();
		
		Set set = new HashSet();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		AmpTeam ampTeam = TeamUtil.getAmpTeam(tm.getTeamId());

		Iterator teamPageFilterItr = DbUtil.getAllTeamPageFiltersOfTeam(
				tm.getTeamId()).iterator();
		while (teamPageFilterItr.hasNext()) {
			AmpTeamPageFilters teamPageFilter = (AmpTeamPageFilters) teamPageFilterItr
					.next();
			if (!(teamPageFilter.getPages().getAmpPageId().equals(tpForm
					.getPageId()))) {
				set.add(teamPageFilter);

			}
		}

		if (selFilters != null) {
			AmpPages ampPage = DbUtil.getAmpPage(tpForm.getPageId());

			for (int i = 0; i < selFilters.length; i++) {
				if (selFilters[i] != null) {
					AmpFilters ampFilter = DbUtil.getAmpFilter(selFilters[i]);
					AmpTeamPageFilters ampTPF = new AmpTeamPageFilters(ampPage,
							ampFilter);
					set.add(ampTPF);
				}
			}
		}

		ampTeam.setTeamPageFilters(set);

		try {
			DbUtil.update(ampTeam);
			tpForm.setUpdated(true);
		} catch (Exception e) {
			tpForm.setUpdated(false);
		}

		return mapping.findForward("forward");
	}
}
