package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.form.TeamPagesForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class ShowConfigureTeam extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		TeamPagesForm tpForm = (TeamPagesForm) form;

		ServletContext ampContext = getServlet().getServletContext();
		
		if (request.getParameter("pageId") == null)
			return mapping.findForward("index");

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

		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		Long pageId = new Long(Long.parseLong(request.getParameter("pageId")));
		AmpPages ampPage = DbUtil.getAmpPage(pageId);

		/* setting all the filter applicable to a page */
		Collection col = DbUtil.getAllPageFilters(pageId);
		

		Collections.sort((List) col);

		tpForm.setFilters(col);

		Collection teamPageFilters = DbUtil.getTeamPageFilters(tm.getTeamId(),
				pageId);
		
		int index = 0;
		Iterator iterator = col.iterator();
		Long filters[] = new Long[col.size()];
		while (iterator.hasNext()) {
			AmpFilters filt1 = (AmpFilters) iterator.next();
			Iterator itr2 = teamPageFilters.iterator();
			while (itr2.hasNext()) {
				Long filtId = (Long) itr2.next();
				if (filtId.equals(filt1.getAmpFilterId())) {
					filters[index] = filtId;
					break;
				}
			}
			index++;
		}

		tpForm.setPageName(ampPage.getPageName());
		tpForm.setSelFilters(filters);
		tpForm.setPageId(pageId);

		return mapping.findForward("showFilters");
	}
}