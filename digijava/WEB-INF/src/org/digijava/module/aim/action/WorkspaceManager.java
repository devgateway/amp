package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.WorkspaceForm;
import javax.servlet.http.*;
import java.util.*;

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
		
		Collection ampWorkspaces = (Collection) session
				.getAttribute("ampWorkspaces");
		if (ampWorkspaces == null) {
			ampWorkspaces = DbUtil.getAllTeams();
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
		Vector vect = new Vector();
		vect.addAll(ampWorkspaces);

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
