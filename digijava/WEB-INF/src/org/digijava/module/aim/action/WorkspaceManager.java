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

		final int NUM_RECORDS = 10;

		Collection workspaces = new ArrayList();
		WorkspaceForm wsForm = (WorkspaceForm) form;
		int page = 0;

		logger.debug("path = " + mapping.getPath());
		logger.debug("In workspace manager");

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			/*
			 * check whether the page is a valid integer
			 */
			page = Integer.parseInt(request.getParameter("page"));
		}

		Collection ampWorkspaces = (Collection) session
				.getAttribute("ampWorkspaces");
		logger.debug("got collection");
		if (ampWorkspaces == null) {
			logger.debug("collection is null ");
			ampWorkspaces = DbUtil.getAllTeams();
			logger
					.debug("collection has " + ampWorkspaces.size()
							+ " elements");
			session.setAttribute("ampWorkspaces", ampWorkspaces);
		} else {
			logger.debug("collection is not null");
			logger
					.debug("collection has " + ampWorkspaces.size()
							+ " elements");
		}

		int numPages = ampWorkspaces.size() / NUM_RECORDS;
		numPages += (ampWorkspaces.size() % NUM_RECORDS != 0) ? 1 : 0;

		/*
		 * check whether the numPages is less than the page . if yes return
		 * error.
		 */

		int stIndex = ((page - 1) * NUM_RECORDS) + 1;
		int edIndex = page * NUM_RECORDS;
		if (edIndex > ampWorkspaces.size()) {
			edIndex = ampWorkspaces.size();
		}

		Vector vect = new Vector();
		vect.addAll(ampWorkspaces);

		for (int i = (stIndex - 1); i < edIndex; i++) {
			logger.debug("adding col of index " + i);
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

		logger.debug("Workspace manager returning");
		return mapping.findForward("forward");
	}
}
