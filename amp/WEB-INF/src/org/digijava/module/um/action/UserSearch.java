package org.digijava.module.um.action;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

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
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.UserBean;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.form.ViewAllUsersForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;

public class UserSearch extends Action {

	private static Logger logger = Logger.getLogger(UserSearch.class);

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

		ViewAllUsersForm vwForm = (ViewAllUsersForm) form;

		int page = 0;

		JSONObject json = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		String startIndex = request.getParameter("startIndex");
		String results = request.getParameter("results");
		int startIndexInt = 0;
		int resultsInt = 10;
		if (startIndex != null)
			startIndexInt = Integer.parseInt(startIndex);
		if (results != null)
			resultsInt = Integer.parseInt(results);
		String sort = request.getParameter("sort");
		String sortBy = vwForm.getSortBy();
		String dir = request.getParameter("dir");
		String dirBy = vwForm.getSortDir();
		if ((sortBy == null && dirBy==null) || (sort != null && dir!=null && (!sortBy.equals(sort)||!dirBy.equals(dir)))) {
			myexecute(mapping, vwForm, request, response);
		}

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}

		if (vwForm.getNumResults() == 0) {
			vwForm.setTempNumResults(10);
		} else {
			// int stIndex = ((page - 1) * vwForm.getNumResults()) + 1;
			// int edIndex = page * vwForm.getNumResults();

			int stIndex = startIndexInt;
			int edIndex = startIndexInt + resultsInt;

			if (vwForm.isSelectedNoLetter()) {
				if (edIndex > vwForm.getUsers().size()) {
					edIndex = vwForm.getUsers().size();
				}
			} else {
				if (edIndex > vwForm.getAlphaUsers().size()) {
					edIndex = vwForm.getAlphaUsers().size();
				}
			}

			Vector vect = new Vector();

			if (vwForm.isSelectedNoLetter())
				vect.addAll(vwForm.getUsers());
			else
				vect.addAll(vwForm.getAlphaUsers());

			Collection tempCol = new ArrayList();

			// for (int i = (stIndex - 1); i < edIndex; i++) {
			for (int i = stIndex; i < edIndex; i++) {
				tempCol.add(vect.get(i));
			}

			vwForm.setPagedUsers(tempCol);
			vwForm.setCurrentPage(new Integer(page));

			// json.put("page", page);
			for (Iterator it = tempCol.iterator(); it.hasNext();) {
				UserBean user = (UserBean) it.next();
				JSONObject juser = new JSONObject();
				juser.put("ID", user.getId());
				juser.put("name", user.getFirstNames() + " "
						+ user.getLastName());
				juser.put("email", user.getEmail());
				JSONArray jsonItems = new JSONArray();
				Collection<AmpTeamMember> teamMembers = user.getTeamMembers();
				String ws = "";
				for (AmpTeamMember teamMember : teamMembers) {
					ws += "<li/>" + teamMember.getAmpTeam().getName() + " ("
							+ teamMember.getAmpMemberRole().getRole() + ")";
				}
				juser.put("workspaces", ws);
				juser.put("actions", "<a href=/um/viewEditUser.do~id=" +user.getId()+" title='Edit User'><img vspace='2' border='0' src='/repository/message/view/images/edit.gif'/></a>&nbsp;&nbsp;<a onclick='return banUser();' title='Ban User' href=/um/viewEditUser.do~id=" +user.getId()+"~ban=true><img vspace='2' border='0' src='/TEMPLATE/ampTemplate/images/deleteIcon.gif'/> </a>");
				jsonArray.add(juser);

			}
			Integer totalRecords = vwForm.getUsers().size();
			json.put("recordsReturned", tempCol.size());
			json.put("totalRecords", totalRecords);
			json.put("startIndex", startIndexInt);
			json.put("sort", null);
			json.put("dir", "asc");
			json.put("pageSize", 10);
			json.put("rowsPerPage",vwForm.getTempNumResults());

			json.put("users", jsonArray);
			//System.out.println(json.toString(1));

			response.setContentType("text/json-comment-filtered");
			OutputStreamWriter outputStream = null;

			try {
				outputStream = new OutputStreamWriter(response
						.getOutputStream(), "UTF-8");
				outputStream.write(json.toString());
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}

		}

		// return mapping.findForward("forward");
		return null;
	}

	private void myexecute(ActionMapping mapping, ViewAllUsersForm vwForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (request.getParameter("reset") != null
				&& request.getParameter("reset").equals("true")) {
			vwForm.reset(mapping, request);
		}

		RepairDbUtil.repairBannedUsersAreStillInATeam();

		if (request.getParameter("showBanned") != null) {
			if (request.getParameter("showBanned").equals("true")) {
				vwForm.setShowBanned(true);
				vwForm.setType(-1);
			} else {
				vwForm.setShowBanned(false);
			}
		}

		vwForm.setPagesToShow(10);
		vwForm.setReset("false");
		vwForm.setNumResults(vwForm.getTempNumResults());
		Collection ubCol = getUsers(vwForm, request);
		vwForm.setSelectedNoLetter(true);
		String alpha = vwForm.getCurrentAlpha();

		if (vwForm.getType() == 0) {
			for (Iterator ubIter = ubCol.iterator(); ubIter.hasNext();) {
				UserBean ub = (UserBean) ubIter.next();
				if (ub.getTeamMembers() != null
						&& !ub.getTeamMembers().isEmpty()) {
					ubIter.remove();
				}
			}

		} else if (vwForm.getType() == 1) {
			for (Iterator ubIter = ubCol.iterator(); ubIter.hasNext();) {
				UserBean user = (UserBean) ubIter.next();
				if (user.getTeamMembers() == null
						|| user.getTeamMembers().isEmpty()) {
					ubIter.remove();
				}
			}
		}

		if (vwForm.getKeyword() != null && ubCol != null) {
			for (Iterator ubIter = ubCol.iterator(); ubIter.hasNext();) {
				UserBean ub = (UserBean) ubIter.next();

				String firstAndLastName = ub.getFirstNames() + ub.getLastName();
				if (ub.getEmail().toLowerCase().indexOf(
						vwForm.getKeyword().toLowerCase()) == -1
						&& firstAndLastName.toLowerCase().indexOf(
								vwForm.getKeyword().toLowerCase()) == -1) {

					ubIter.remove();
				}
			}
		}

		if (ubCol != null && ubCol.size() > 0) {
			if (alpha == null || alpha.trim().length() == 0) {
				if (vwForm.getCurrentAlpha() != null) {
					vwForm.setCurrentAlpha(null);
				}
			} else {
				vwForm.setCurrentAlpha(alpha);
			}

			String[] alphaArray = new String[26];
			int i = 0;
			for (char c = 'A'; c <= 'Z'; c++) {
				Iterator itr = ubCol.iterator();
				while (itr.hasNext()) {
					UserBean us = (UserBean) itr.next();
					if (us.getFirstNames().toUpperCase().indexOf(c) == 0) {
						alphaArray[i++] = String.valueOf(c);
						break;
					}
				}
			}
			vwForm.setAlphaPages(alphaArray);
		} else {
			vwForm.setAlphaPages(null);
		}

		vwForm.setNumResults(vwForm.getTempNumResults());

		if (vwForm.getNumResults() == 0) {
			vwForm.setNumResults(10);
		}
		int stIndex = 1;
		int edIndex = vwForm.getNumResults();

		// If ALL was selected in pagination dropdown
		if (edIndex < 0) {
			edIndex = ubCol.size();
		}

		Vector vect = new Vector();
		int numPages;

		if (alpha == null || alpha.trim().length() == 0
				|| alpha.equals("viewAll")) {
			if (edIndex > ubCol.size()) {
				edIndex = ubCol.size();
			}
			vect.addAll(ubCol);
			numPages = ubCol.size() / vwForm.getNumResults();
			numPages += (ubCol.size() % vwForm.getNumResults() != 0) ? 1 : 0;
		} else {
			if (edIndex > vwForm.getAlphaUsers().size()) {
				edIndex = vwForm.getAlphaUsers().size();
			}
			vect.addAll(vwForm.getAlphaUsers());
			numPages = vwForm.getAlphaUsers().size() / vwForm.getNumResults();
			numPages += (vwForm.getAlphaUsers().size() % vwForm.getNumResults() != 0) ? 1
					: 0;
		}

		Collection tempCol = new ArrayList();
		for (int i = (stIndex - 1); i < edIndex; i++) {
			tempCol.add(vect.get(i));
		}

		Collection pages = null;

		if (numPages > 1) {
			pages = new ArrayList();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}

		vwForm.setUsers(ubCol);
		vwForm.setPagedUsers(tempCol);
		vwForm.setPages(pages);
		vwForm.setCurrentPage(new Integer(1));
	}

	private Collection<UserBean> getUsers(ViewAllUsersForm vwForm,
			HttpServletRequest request) {

		vwForm.setAlphaUsers(new ArrayList<UserBean>());
		Collection<User> users = null;
		String alpha = vwForm.getCurrentAlpha(); // request.getParameter("alpha");
		if (alpha == null || alpha.trim().length() == 0
				|| alpha.equals("viewAll")) {
			users = AmpUserUtil.getAllUsers(vwForm.getShowBanned());
			vwForm.setSelectedNoLetter(true);
		} else if (alpha != null && !alpha.equals("viewAll")) {
			users = new ArrayList<User>();
			Iterator iter = AmpUserUtil.getAllUsers(vwForm.getShowBanned())
					.iterator();
			while (iter.hasNext()) {
				User us = (User) iter.next();
				if (us.getFirstNames().toUpperCase().startsWith(alpha)) {
					users.add(us);
				}
			}
			vwForm.setSelectedNoLetter(false);
		}

		if (users != null) {
			List<User> sortedUser = new ArrayList(users);

			// sorting users
			if (request.getParameter("sort") != null) {
				vwForm.setSortBy(request.getParameter("sort"));
			}
			String sortBy = vwForm.getSortBy();
			if (request.getParameter("dir")!=null){
				vwForm.setSortDir(request.getParameter("dir"));
			}
			String sortDir = vwForm.getSortDir();

			if (sortBy != null && sortDir != null){
				if( sortBy.equals("name") && sortDir.equals("asc")) {
					Collections.sort(sortedUser,new DbUtil.HelperUserNameComparatorAsc());
				} else if (sortBy.equals("name") && sortDir.equals("desc")) {
					Collections.sort(sortedUser,new DbUtil.HelperUserNameComparatorDesc());
				} else if (sortBy.equals("email") && sortDir.equals("asc")) {
					Collections.sort(sortedUser,new DbUtil.HelperEmailComparatorAsc());
				} else if (sortBy.equals("email") && sortDir.equals("desc")) {
					Collections.sort(sortedUser,new DbUtil.HelperEmailComparatorDesc());
				}
			}else {
				Collections.sort(sortedUser,
						new DbUtil.HelperUserNameComparatorAsc());
			}

			Collection<UserBean> ubCol = new ArrayList();

			for (Iterator userIter = sortedUser.iterator(); userIter.hasNext();) {
				User user = (User) userIter.next();
				if (user != null) {
					UserBean ub = new UserBean();
					ub.setId(user.getId());
					ub.setEmail(user.getEmail());
					ub.setFirstNames(user.getFirstNames());
					ub.setLastName(user.getLastName());
					ub.setBan(user.isBanned());

					Collection members = TeamMemberUtil.getTeamMembers(user
							.getEmail());
					ub.setTeamMembers(members);
					ubCol.add(ub);
				}
			}

			if (alpha != null && !alpha.equals("viewAll")) {
				vwForm.setAlphaUsers(ubCol);
			}

			return ubCol;
		} else {
			return null;
		}
	}

}
