package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetTeamActivities extends Action {

	private static Logger logger = Logger.getLogger(GetTeamActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In get team activities");

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;

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

		int numRecords = Constants.NUM_RECORDS;
		int page = 0;
		boolean admin = true;

		if (request.getParameter("id") != null) {
			id = new Long(Long.parseLong(request.getParameter("id")));
		} else if (request.getAttribute("teamId") != null) {
			id = (Long) request.getAttribute("teamId");
		} else if (session.getAttribute("currentMember") != null) {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			id = tm.getTeamId();
			numRecords = tm.getAppSettings().getDefRecsPerPage();
			admin = false;
		}

		if (id != null) {
			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			page = (page < 0) ? 1 : page;

			taForm.setPage(page);

			AmpTeam ampTeam = TeamUtil.getAmpTeam(id);

			Collection col = null;

			if (admin == true) {
				col = DbUtil.getAllTeamActivities(id);
				List temp = (List) col;
				Collections.sort(temp);
				col = (Collection) temp;
			} else {

				if (session.getAttribute("teamActivityList") == null) {
					col = DbUtil.getAllTeamActivities(id);
					List temp = (List) col;
					logger.info("Activity Collection size = " + col.size());
					Collections.sort(temp);
					col = (Collection) temp;
					session.setAttribute("teamActivityList", col);
				}

				Collection actList = (Collection) session
						.getAttribute("teamActivityList");

				int stIndex = ((page - 1) * numRecords) + 1;
				int edIndex = page * numRecords;
				if (edIndex > actList.size()) {
					edIndex = actList.size();
				}

				Vector vect = new Vector();
				vect.addAll(actList);

				col = new ArrayList();
				for (int i = (stIndex - 1); i < edIndex; i++) {
					col.add(vect.get(i));
				}

				int numPages = actList.size() / numRecords;
				numPages += (actList.size() % numRecords != 0) ? 1 : 0;

				Collection pages = null;

				if (numPages > 1) {
					pages = new ArrayList();
					for (int i = 0; i < numPages; i++) {
						Integer pageNum = new Integer(i + 1);
						pages.add(pageNum);
					}
				}
				taForm.setCurrentPage(new Integer(page));
				taForm.setPages(pages);
				session.setAttribute("pageno", new Integer(page));
			}
			taForm.setActivities(col);
			taForm.setTeamId(id);
			taForm.setTeamName(ampTeam.getName());

			return mapping.findForward("forward");
		} else {
			return null;
		}
	}
}
