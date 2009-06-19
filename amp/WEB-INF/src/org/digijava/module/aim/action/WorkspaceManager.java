package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
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

		int NUM_RECORDS = 10000;
		Collection<AmpTeam> workspaces = new ArrayList<AmpTeam>();
		WorkspaceForm wsForm = (WorkspaceForm) form;
		
		
		
		if(wsForm.getNumPerPage()!=-1) NUM_RECORDS =wsForm.getNumPerPage();
		String keyword = wsForm.getKeyword();
		Collection<String> keywords=new ArrayList<String>();
		StringTokenizer st = new StringTokenizer("");
		if(keyword!=null)
		 st = new StringTokenizer(keyword);
	     while (st.hasMoreTokens()) {
	         keywords.add(st.nextToken().toLowerCase());
	     }

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
		Collection<AmpTeam> colAt=new ArrayList<AmpTeam>();
		for (AmpTeam at : ampWorkspaces) {
			at.setChildrenWorkspaces(TeamUtil.getAllChildrenWorkspaces(at.getAmpTeamId()));
			colAt.add(at);
		}
		Vector<AmpTeam> vect = new Vector<AmpTeam>();
		//vect.addAll(ampWorkspaces);
		vect.addAll(colAt);
		String workspaceType = wsForm.getWorkspaceType();
 		for (int i = (stIndex - 1); i < edIndex; i++) {
			AmpTeam ateam=vect.get(i);
			
			if(workspaceType!=null){
			  if("all".equals(workspaceType)) workspaces.add(ateam);
			  if("team".equals(workspaceType) && "Team".equals(ateam.getAccessType())) workspaces.add(ateam);
			  if("management".equals(workspaceType) && "Management".equals(ateam.getAccessType())) workspaces.add(ateam);
			  if("computed".equals(workspaceType) && ateam.getComputation()!=null && ateam.getComputation()) workspaces.add(ateam);
			}
				
		}

		Collection<Integer> pages = null;
		if (numPages > 1) {
			pages = new ArrayList<Integer>();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		Collection<AmpTeam> workspacesFiltered=new ArrayList<AmpTeam>();
		if(!workspaces.isEmpty())
		{
			for (Iterator<AmpTeam> it = workspaces.iterator(); it.hasNext();) {
				AmpTeam team = (AmpTeam) it.next();
				boolean found=false;
				for (Iterator jt = keywords.iterator(); jt.hasNext();) {
					String keyw = (String) jt.next();
					if( (team.getDescription()!=null && team.getDescription().toLowerCase().contains(keyw)) 
							|| (team.getName() !=null && team.getName().toLowerCase().contains(keyw)) ) {
						found=true;break;
					}
				}
				if(found) workspacesFiltered.add(team);
			}
		}
		if(workspacesFiltered.isEmpty() && keywords.isEmpty())
		  wsForm.setWorkspaces(workspaces);
		else wsForm.setWorkspaces(workspacesFiltered);
		wsForm.setPages(pages);

		return mapping.findForward("forward");
	}
}
