package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

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
		
		if(request.getParameter("reset")!=null && request.getParameter("reset").equalsIgnoreCase("true")){
			wsForm.setKeyword(null);
			wsForm.setWorkspaceType("all");
			wsForm.setNumPerPage(-1);
		}
		
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
		
		if(wsForm.getNumPerPage()!=-1) {
			NUM_RECORDS =wsForm.getNumPerPage();
		}else {
			NUM_RECORDS =ampWorkspaces.size();
		}
		
		/*
		 * check whether the numPages is less than the page . if yes return
		 * error.
		 */

		int currPage = wsForm.getPage();
		int stIndex = ((currPage - 1) * NUM_RECORDS);
		int edIndex = stIndex + NUM_RECORDS;
		
		Collection<AmpTeam> colAt=new ArrayList<AmpTeam>();
		for (AmpTeam at : ampWorkspaces) {
			at.setChildrenWorkspaces(TeamUtil.getAllChildrenWorkspaces(at.getAmpTeamId()));
			colAt.add(at);
		}
		Vector<AmpTeam> vect = new Vector<AmpTeam>();
		//vect.addAll(ampWorkspaces);
		vect.addAll(colAt);
		String workspaceType = wsForm.getWorkspaceType();
		for (AmpTeam ampTeam : vect) {
			if(workspaceType!=null){
			  if("all".equals(workspaceType)) {
				  workspaces.add(ampTeam);
			  }else if("team".equals(workspaceType) && "Team".equals(ampTeam.getAccessType())) {
				  workspaces.add(ampTeam);
			  }else if("management".equals(workspaceType) && "Management".equals(ampTeam.getAccessType())) {
				  workspaces.add(ampTeam);
			  }else if("computed".equals(workspaceType) && ampTeam.getComputation()!=null && ampTeam.getComputation()) {
				  workspaces.add(ampTeam);
			  }
			}
		//pages
		int numPages = workspaces.size() / NUM_RECORDS;
		numPages += (workspaces.size() % NUM_RECORDS != 0) ? 1 : 0;
		//workspaces for current page
		if(edIndex>workspaces.size()){
			edIndex=workspaces.size();	
		}
		workspaces=((List)workspaces).subList(stIndex, edIndex);
		
		//pages
		Collection<Integer> pages = null;
				
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
		
		if(workspacesFiltered.isEmpty() && keywords.isEmpty()){
			pages=setupPagination(workspaces, wsForm.getPage(), NUM_RECORDS);			
			wsForm.setWorkspaces(getWorkspacesForPage(workspaces,stIndex,edIndex));
		}else {			
			pages=setupPagination(workspacesFiltered, wsForm.getPage(), NUM_RECORDS);
			wsForm.setWorkspaces(getWorkspacesForPage(workspacesFiltered,stIndex,edIndex));
		}
		
		wsForm.setPages(pages);
		}
		return mapping.findForward("forward");
	}

	
	private Collection<Integer> setupPagination(Collection<AmpTeam> workspaces, int currentPage, int numberOfRecordsPerPage){		
		Collection<Integer> pages=null;
		int numPages = workspaces.size() / numberOfRecordsPerPage;
		numPages += (workspaces.size() % numberOfRecordsPerPage != 0) ? 1 : 0;
		
		int currPage = currentPage;
		int stIndex = ((currPage - 1) * numberOfRecordsPerPage);
		int edIndex = stIndex + numberOfRecordsPerPage;
		if(edIndex>workspaces.size()){
			edIndex=workspaces.size();
		}
		
		if (numPages > 1) {
			pages = new ArrayList<Integer>();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		return pages;
	}
	
	private Collection<AmpTeam> getWorkspacesForPage(Collection<AmpTeam> workspaces , int stIndex, int endIndex){
		Collection<AmpTeam> retVal=null;
		if(endIndex>workspaces.size()){
			endIndex=workspaces.size();
		}			
		retVal=((List)workspaces).subList(stIndex, endIndex);
		return retVal;
	}
}
