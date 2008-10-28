package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
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
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;

public class WorkspaceManager extends Action {

	private static Logger logger = Logger.getLogger(WorkspaceManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		final int NUM_RECORDS = 20;

		Collection workspaces = new ArrayList();
		WorkspaceForm wsForm = (WorkspaceForm) form;
		
		String temp = request.getParameter("page"); 
		if (temp != null) {
			wsForm.setPage(Integer.parseInt(temp));
		} else if (wsForm.getPage() <= 0) {
			wsForm.setPage(1);
		}
		
		Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) session.getAttribute("ampWorkspaces");
		if (ampWorkspaces == null) {
			ampWorkspaces = TeamUtil.getAllTeams();
			session.setAttribute("ampWorkspaces", ampWorkspaces);
		}

		int numPages = ampWorkspaces.size() / NUM_RECORDS;
		numPages += (ampWorkspaces.size() % NUM_RECORDS != 0) ? 1 : 0;

		/*
		 * check whether the numPages is less than the page . if yes return
		 * error.
		 */

		int currPage = wsForm.getPage();
		int stIndex = ((currPage - 1) * NUM_RECORDS) + 1;
		int edIndex = currPage * NUM_RECORDS;
		if (edIndex > ampWorkspaces.size()) {
			edIndex = ampWorkspaces.size();
		}
		Collection colAt=new ArrayList();
		for (AmpTeam at : ampWorkspaces) {
			at.setChildrenWorkspaces(TeamUtil.getAllChildrenWorkspaces(at.getAmpTeamId()));
			colAt.add(at);
		}
		Vector vect = new Vector();
		//vect.addAll(ampWorkspaces);
		vect.addAll(colAt);

		for (int i = (stIndex - 1); i < edIndex; i++) {
			workspaces.add(vect.get(i));
		}

		Collection pages = null;
		if (numPages > 1) {
			pages = new ArrayList();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		wsForm.setWorkspaces(workspaces);
		wsForm.setPages(pages);

		return mapping.findForward("forward");
	}
}
