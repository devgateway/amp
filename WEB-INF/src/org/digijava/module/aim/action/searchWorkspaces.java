package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.helper.UserBean;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.action.UserSearch;
import org.digijava.module.um.form.ViewAllUsersForm;
import org.digijava.module.um.util.AmpUserUtil;

public class searchWorkspaces extends Action {
	private static Logger logger = Logger.getLogger(searchWorkspaces.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		logger.debug("In User manager action");

		WorkspaceForm vwForm = (WorkspaceForm) form;

		int page = 0;

		JSONObject json = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		String startIndex = request.getParameter("startIndex");
		String results = request.getParameter("results");
		int startIndexInt = 0;
		int resultsInt = 10;
		if (startIndex != null){
			startIndexInt = Integer.parseInt(startIndex);
		}
		if (results != null){
			resultsInt = Integer.parseInt(results);
		}

		sortWorkspaces(request, vwForm);
		
		Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) vwForm.getWorkspaces();
		int stIndex = startIndexInt;
		int edIndex = (startIndexInt + resultsInt)>ampWorkspaces.size()?ampWorkspaces.size()-1:(startIndexInt + resultsInt);

		Vector vect = new Vector();

		vect.addAll(ampWorkspaces);

		Collection tempCol = new ArrayList();

		for (int i = stIndex; i < edIndex; i++) {
			tempCol.add(vect.get(i));
		}
		for (Iterator it = tempCol.iterator(); it.hasNext();) {
			AmpTeam team = (AmpTeam) it.next();
			JSONObject jteam = new JSONObject();
			jteam.put("ID", team.getAmpTeamId());
			jteam.put("name", team.getName());
			jsonArray.add(jteam);

		}
		Integer totalRecords = ampWorkspaces.size();
		json.put("recordsReturned", tempCol.size());
		json.put("totalRecords", totalRecords);
		json.put("startIndex", startIndexInt);
		json.put("sort", null);
		json.put("dir", "asc");
		json.put("pageSize", 10);
		json.put("rowsPerPage", 10);

		json.put("workspaces", jsonArray);

		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream = null;

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),
					"UTF-8");
			outputStream.write(json.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}

	private void sortWorkspaces(HttpServletRequest request, WorkspaceForm vwForm) {
		Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) vwForm.getWorkspaces();
		if (ampWorkspaces != null) {
			List<AmpTeam> ampTeams = new ArrayList(ampWorkspaces);

			String sortBy = request.getParameter("sort");
			String sortDir = request.getParameter("dir");

			if (sortBy != null && sortDir != null) {
				if (sortBy.equals("name") && sortDir.equals("asc")) {
					Collections.sort(ampTeams, new TeamUtil.HelperAmpTeamNameComparator());
				} else if (sortBy.equals("name") && sortDir.equals("desc")) {
					Collections.sort(ampTeams, new TeamUtil.HelperAmpTeamNameComparatorDesc());
				}
			}
			vwForm.setWorkspaces(ampTeams);
		}
	}

}